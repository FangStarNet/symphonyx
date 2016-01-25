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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.After;
import org.b3log.latke.servlet.annotation.Before;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.symphony.model.Article;
import org.b3log.symphony.processor.advice.AnonymousViewCheck;
import org.b3log.symphony.processor.advice.LoginCheck;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchEndAdvice;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchStartAdvice;
import org.b3log.symphony.service.SearchQueryService;
import org.b3log.symphony.util.Filler;
import org.json.JSONObject;

/**
 * Search processor.
 *
 * <ul>
 * <li>Searches keyword (/search), GET</li>
 * </ul>
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Jan 22, 2016
 * @since 1.4.0
 */
@RequestProcessor
public class SearchProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(SearchProcessor.class.getName());

    /**
     * Search query service.
     */
    @Inject
    private SearchQueryService searchQueryService;

    /**
     * Filler.
     */
    @Inject
    private Filler filler;

    /**
     * Searches.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/search", method = HTTPRequestMethod.GET)
    @Before(adviceClass = {StopwatchStartAdvice.class, AnonymousViewCheck.class})
    @After(adviceClass = StopwatchEndAdvice.class)
    public void search(final HTTPRequestContext context, final HttpServletRequest request, final HttpServletResponse response)
            throws Exception {
        final String keyword = request.getParameter("key");

        final JSONObject result = searchQueryService.search(Article.ARTICLE, keyword, 0, 20);

        context.renderJSON(result);
    }
}
