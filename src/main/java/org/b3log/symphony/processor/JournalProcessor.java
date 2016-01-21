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
import java.util.Locale;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.time.DateFormatUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.User;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.After;
import org.b3log.latke.servlet.annotation.Before;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.symphony.model.Article;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchEndAdvice;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchStartAdvice;
import org.b3log.symphony.service.ArticleMgmtService;
import org.b3log.symphony.service.JournalQueryService;
import org.b3log.symphony.service.UserQueryService;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

/**
 * Journal processor.
 *
 * <ul>
 * <li>Generates today's journal section (/journal/gen/section), GET</li>
 * <li>Generates journal chapter this week (/journal/gen/chapter), GET</li>
 * </ul>
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Jan 21, 2016
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
            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

            final String from = df.format(cal.getTime());

            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            cal.add(Calendar.WEEK_OF_YEAR, 1);

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
