/*
 * Copyright (c) 2012-2016, b3log.org & hacpai.com
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
import java.util.Set;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.User;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.Before;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.util.Requests;
import org.b3log.symphony.model.Article;
import org.b3log.symphony.model.Comment;
import org.b3log.symphony.processor.advice.CSRFCheck;
import org.b3log.symphony.processor.advice.LoginCheck;
import org.b3log.symphony.processor.advice.validate.CommentAddValidation;
import org.b3log.symphony.service.ArticleQueryService;
import org.b3log.symphony.service.CommentMgmtService;
import org.b3log.symphony.service.UserQueryService;
import org.json.JSONObject;

/**
 * Comment processor.
 *
 * <ul>
 * <li>Adds a comment (/comment) <em>locally</em>, POST</li>
 * <li>Thanks a comment (/comment/thank), POST</li>
 * </ul>
 *
 * <p>
 * The '<em>locally</em>' means user post a comment on Symphony directly rather than receiving a comment from externally
 * (for example Solo).
 * </p>
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 2.2.1.10, Jan 21, 2016
 * @since 0.2.0
 */
@RequestProcessor
public class CommentProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(CommentProcessor.class.getName());

    /**
     * User query service.
     */
    @Inject
    private UserQueryService userQueryService;

    /**
     * Comment management service.
     */
    @Inject
    private CommentMgmtService commentMgmtService;

    /**
     * Article query service.
     */
    @Inject
    private ArticleQueryService articleQueryService;

    /**
     * Language service.
     */
    @Inject
    private LangPropsService langPropsService;

    /**
     * Adds a comment locally.
     *
     * <p>
     * The request json object (a comment):
     * <pre>
     * {
     *     "articleId": "",
     *     "commentContent": ""
     * }
     * </pre>
     * </p>
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws IOException io exception
     * @throws ServletException servlet exception
     */
    @RequestProcessing(value = "/comment", method = HTTPRequestMethod.POST)
    @Before(adviceClass = {CSRFCheck.class, CommentAddValidation.class})
    public void addComment(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
        context.renderJSON();

        final JSONObject requestJSONObject = (JSONObject) request.getAttribute(Keys.REQUEST);

        final String articleId = requestJSONObject.optString(Article.ARTICLE_T_ID);
        final String commentContent = requestJSONObject.optString(Comment.COMMENT_CONTENT);
        final String ip = Requests.getRemoteAddr(request);

        final JSONObject comment = new JSONObject();
        comment.put(Comment.COMMENT_CONTENT, commentContent);
        comment.put(Comment.COMMENT_ON_ARTICLE_ID, articleId);
        comment.put(Comment.COMMENT_IP, "");
        if (StringUtils.isNotBlank(ip)) {
            comment.put(Comment.COMMENT_IP, ip);
        }

        try {
            final JSONObject currentUser = userQueryService.getCurrentUser(request);
            if (null == currentUser) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);

                return;
            }

            final String currentUserName = currentUser.optString(User.USER_NAME);
            final JSONObject article = articleQueryService.getArticle(articleId);
            final String articleContent = article.optString(Article.ARTICLE_CONTENT);
            final String articleAuthorId = article.optString(Article.ARTICLE_AUTHOR_ID);
            final JSONObject articleAuthor = userQueryService.getUser(articleAuthorId);
            final String articleAuthorName = articleAuthor.optString(User.USER_NAME);

            final Set<String> userNames = userQueryService.getUserNames(articleContent);
            if (Article.ARTICLE_TYPE_C_DISCUSSION == article.optInt(Article.ARTICLE_TYPE)
                    && !articleAuthorName.equals(currentUserName)) {
                boolean invited = false;
                for (final String userName : userNames) {
                    if (userName.equals(currentUserName)) {
                        invited = true;

                        break;
                    }
                }

                if (!invited) {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);

                    return;
                }
            }

            comment.put(Comment.COMMENT_AUTHOR_ID, currentUser.optString(Keys.OBJECT_ID));

            final String authorEmail = currentUser.optString(User.USER_EMAIL);
            comment.put(Comment.COMMENT_AUTHOR_EMAIL, authorEmail);

            comment.put(Comment.COMMENT_T_COMMENTER, currentUser);

            commentMgmtService.addComment(comment);
            context.renderTrueResult();
        } catch (final ServiceException e) {
            context.renderMsg(e.getMessage());
        }
    }

    /**
     * Thanks a comment.
     *
     * <p>
     * The request json object:
     * <pre>
     * {
     *     "commentId": "",
     * }
     * </pre>
     * </p>
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws IOException io exception
     * @throws ServletException servlet exception
     */
    @RequestProcessing(value = "/comment/thank", method = HTTPRequestMethod.POST)
    @Before(adviceClass = {LoginCheck.class, CSRFCheck.class})
    public void thankComment(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws IOException, ServletException {
        context.renderJSON();

        JSONObject requestJSONObject;
        try {
            requestJSONObject = Requests.parseRequestJSONObject(request, context.getResponse());
            request.setAttribute(Keys.REQUEST, requestJSONObject);
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Thank comment error", e);

            return;
        }

        final JSONObject currentUser = (JSONObject) request.getAttribute(User.USER);
        final String commentId = requestJSONObject.optString(Comment.COMMENT_T_ID);

        try {
            commentMgmtService.thankComment(commentId, currentUser.optString(Keys.OBJECT_ID));

            context.renderTrueResult().renderMsg(langPropsService.get("thankSentLabel"));
        } catch (final ServiceException e) {
            context.renderMsg(e.getMessage());
        }
    }
}
