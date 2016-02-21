/*
 * Copyright (c) 2012-2016, b3log.org & hacpai.com & fangstar.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.b3log.symphony.processor;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.time.DateFormatUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.Pagination;
import org.b3log.latke.model.User;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.After;
import org.b3log.latke.servlet.annotation.Before;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.freemarker.AbstractFreeMarkerRenderer;
import org.b3log.latke.util.Paginator;
import org.b3log.latke.util.Strings;
import org.b3log.symphony.model.Article;
import org.b3log.symphony.model.Common;
import org.b3log.symphony.processor.advice.AnonymousViewCheck;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchEndAdvice;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchStartAdvice;
import org.b3log.symphony.service.ArchiveMgmtService;
import org.b3log.symphony.service.ArticleMgmtService;
import org.b3log.symphony.service.JournalQueryService;
import org.b3log.symphony.service.UserQueryService;
import org.b3log.symphony.util.Filler;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

/**
 * Journal processor.
 *
 * <ul>
 * <li>Shows journals (/journals), GET</li>
 * <li>Generates today's journal section (/journal/gen/section), GET</li>
 * <li>Generates journal chapter this week (/journal/gen/chapter), GET</li>
 * </ul>
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.2.1.2, Feb 16, 2016
 * @since 1.4.0
 */
@RequestProcessor
public class JournalProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(JournalProcessor.class.getName());

    /**
     * Article management service.
     */
    @Inject
    private ArticleMgmtService articleMgmtService;

    /**
     * Archive management service.
     */
    @Inject
    private ArchiveMgmtService archiveMgmtService;

    /**
     * Journal query service.
     */
    @Inject
    private JournalQueryService journalQueryService;

    /**
     * User query service.
     */
    @Inject
    private UserQueryService userQueryService;

    /**
     * Filler.
     */
    @Inject
    private Filler filler;

    /**
     * Shows journals.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/journals", method = HTTPRequestMethod.GET)
    @Before(adviceClass = {StopwatchStartAdvice.class, AnonymousViewCheck.class})
    @After(adviceClass = StopwatchEndAdvice.class)
    public void showJournals(final HTTPRequestContext context,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("journals.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();

        String pageNumStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageNumStr) || !Strings.isNumeric(pageNumStr)) {
            pageNumStr = "1";
        }

        final int pageNum = Integer.valueOf(pageNumStr);
        final int pageSize = Symphonys.getInt("latestArticlesCnt");
        final int windowSize = Symphonys.getInt("latestArticlesWindowSize");

        final List<JSONObject> latestArticles = journalQueryService.getRecentJournals(pageNum, pageSize);
        dataModel.put(Common.LATEST_ARTICLES, latestArticles);

        final int pageCount = latestArticles.isEmpty() ? 0 : latestArticles.get(0).optInt(Pagination.PAGINATION_PAGE_COUNT);

        final List<Integer> pageNums = Paginator.paginate(pageNum, pageSize, pageCount, windowSize);
        if (!pageNums.isEmpty()) {
            dataModel.put(Pagination.PAGINATION_FIRST_PAGE_NUM, pageNums.get(0));
            dataModel.put(Pagination.PAGINATION_LAST_PAGE_NUM, pageNums.get(pageNums.size() - 1));
        }

        dataModel.put(Pagination.PAGINATION_CURRENT_PAGE_NUM, pageNum);
        dataModel.put(Pagination.PAGINATION_PAGE_COUNT, pageCount);
        dataModel.put(Pagination.PAGINATION_PAGE_NUMS, pageNums);

        filler.fillHeaderAndFooter(request, response, dataModel);
        filler.fillRandomArticles(dataModel);
        filler.fillHotArticles(dataModel);
        filler.fillSideTags(dataModel);
        filler.fillLatestCmts(dataModel);
    }

    /**
     * Generates today's journal section.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws IOException io exception
     * @throws ServletException servlet exception
     */
    @RequestProcessing(value = "/journal/gen/section", method = HTTPRequestMethod.GET)
    @Before(adviceClass = StopwatchStartAdvice.class)
    @After(adviceClass = StopwatchEndAdvice.class)
    public void genSection(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
        final String key = Symphonys.get("keyOfSymphony");
        if (!key.equals(request.getParameter("key"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);

            return;
        }

        context.renderJSON();

        if (journalQueryService.hasSectionToday()) {
            return;
        }

        try {

            final JSONObject admin = userQueryService.getSA();

            final JSONObject section = new JSONObject();
            final String title = DateFormatUtils.format(new Date(), "yyyyMMdd E", Locale.US);
            section.put(Article.ARTICLE_TITLE, title);
            section.put(Article.ARTICLE_TAGS, "航海日记,节");
            section.put(Article.ARTICLE_CONTENT, "");
            section.put(Article.ARTICLE_EDITOR_TYPE, 0);
            section.put(Article.ARTICLE_AUTHOR_EMAIL, admin.optString(User.USER_EMAIL));
            section.put(Article.ARTICLE_AUTHOR_ID, admin.optString(Keys.OBJECT_ID));
            section.put(Article.ARTICLE_TYPE, Article.ARTICLE_TYPE_C_JOURNAL_SECTION);

            articleMgmtService.addArticle(section);
            archiveMgmtService.refreshTeams(System.currentTimeMillis());

            context.renderTrueResult();
        } catch (final ServiceException e) {
            LOGGER.log(Level.ERROR, "Generates section failed", e);
        }
    }

    /**
     * Generates journal chapter this week.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws IOException io exception
     * @throws ServletException servlet exception
     */
    @RequestProcessing(value = "/journal/gen/chapter", method = HTTPRequestMethod.GET)
    @Before(adviceClass = StopwatchStartAdvice.class)
    @After(adviceClass = StopwatchEndAdvice.class)
    public void genChapter(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
        final String key = Symphonys.get("keyOfSymphony");
        if (!key.equals(request.getParameter("key"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);

            return;
        }

        context.renderJSON();

        if (journalQueryService.hasChapterWeek()) {
            return;
        }

        try {
            final JSONObject admin = userQueryService.getSA();

            final JSONObject chapter = new JSONObject();

            final Calendar cal = Calendar.getInstance();
            final SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            final String from = df.format(cal.getTime());
            cal.setFirstDayOfWeek(Calendar.MONDAY);
            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

            final String title = from + " - " + df.format(cal.getTime());
            chapter.put(Article.ARTICLE_TITLE, title);
            chapter.put(Article.ARTICLE_TAGS, "航海日记,章");
            chapter.put(Article.ARTICLE_CONTENT, "");
            chapter.put(Article.ARTICLE_EDITOR_TYPE, 0);
            chapter.put(Article.ARTICLE_AUTHOR_EMAIL, admin.optString(User.USER_EMAIL));
            chapter.put(Article.ARTICLE_AUTHOR_ID, admin.optString(Keys.OBJECT_ID));
            chapter.put(Article.ARTICLE_TYPE, Article.ARTICLE_TYPE_C_JOURNAL_CHAPTER);

            articleMgmtService.addArticle(chapter);

            context.renderTrueResult();
        } catch (final ServiceException e) {
            LOGGER.log(Level.ERROR, "Generates section failed", e);
        }
    }
}
