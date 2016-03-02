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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.Pagination;
import org.b3log.latke.model.User;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.servlet.HTTPRequestContext;
import org.b3log.latke.servlet.HTTPRequestMethod;
import org.b3log.latke.servlet.annotation.After;
import org.b3log.latke.servlet.annotation.Before;
import org.b3log.latke.servlet.annotation.RequestProcessing;
import org.b3log.latke.servlet.annotation.RequestProcessor;
import org.b3log.latke.servlet.renderer.freemarker.AbstractFreeMarkerRenderer;
import org.b3log.latke.util.CollectionUtils;
import org.b3log.latke.util.Requests;
import org.b3log.latke.util.Strings;
import org.b3log.symphony.model.Common;
import org.b3log.symphony.model.Product;
import org.b3log.symphony.processor.advice.AnonymousViewCheck;
import org.b3log.symphony.processor.advice.CSRFCheck;
import org.b3log.symphony.processor.advice.CSRFToken;
import org.b3log.symphony.processor.advice.LoginCheck;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchEndAdvice;
import org.b3log.symphony.processor.advice.stopwatch.StopwatchStartAdvice;
import org.b3log.symphony.service.ProductMgmtService;
import org.b3log.symphony.service.ProductQueryService;
import org.b3log.symphony.util.Filler;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

/**
 * Mall processor.
 *
 * <ul>
 * <li>Shows products (/mall), GET</li>
 * </ul>
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Feb 26, 2016
 * @since 1.4.0
 */
@RequestProcessor
public class MallProcessor {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(MallProcessor.class.getName());

    /**
     * Product query service.
     */
    @Inject
    private ProductQueryService productQueryService;

    /**
     * Product management service.
     */
    @Inject
    private ProductMgmtService productMgmtService;

    /**
     * Filler.
     */
    @Inject
    private Filler filler;

    /**
     * Language service.
     */
    @Inject
    private LangPropsService langPropsService;

    /**
     * Buys a product.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/mall/product/buy", method = HTTPRequestMethod.POST)
    @Before(adviceClass = {StopwatchStartAdvice.class, LoginCheck.class, CSRFCheck.class})
    @After(adviceClass = {StopwatchEndAdvice.class})
    public void buyProduct(final HTTPRequestContext context,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        context.renderJSON().renderFalseResult();

        final JSONObject requestJSONObject = Requests.parseRequestJSONObject(request, context.getResponse());

        final String productId = requestJSONObject.optString(Common.PRODUCT_ID);
        final JSONObject currentUser = (JSONObject) request.getAttribute(User.USER);
        final String userId = currentUser.optString(Keys.OBJECT_ID);

        final JSONObject ret = productMgmtService.buyProduct(productId, userId);

        if (ret.optBoolean(Keys.STATUS_CODE)) {
            context.renderJSON().renderTrueResult().renderMsg(langPropsService.get("buySuccLabel"));
        } else {
            context.renderJSON().renderTrueResult().renderMsg(langPropsService.get("buyFailedLabel")
                    + " - " + ret.optString(Keys.MSG));
        }
    }

    /**
     * Shows timeline.
     *
     * @param context the specified context
     * @param request the specified request
     * @param response the specified response
     * @throws Exception exception
     */
    @RequestProcessing(value = "/mall", method = HTTPRequestMethod.GET)
    @Before(adviceClass = {StopwatchStartAdvice.class, AnonymousViewCheck.class})
    @After(adviceClass = {CSRFToken.class, StopwatchEndAdvice.class})
    public void showMall(final HTTPRequestContext context,
            final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        request.setAttribute(Keys.TEMAPLTE_DIR_NAME, Symphonys.get("skinDirName"));
        final AbstractFreeMarkerRenderer renderer = new SkinRenderer();
        context.setRenderer(renderer);
        renderer.setTemplateName("mall.ftl");
        final Map<String, Object> dataModel = renderer.getDataModel();

        String pageNumStr = request.getParameter("p");
        if (Strings.isEmptyOrNull(pageNumStr) || !Strings.isNumeric(pageNumStr)) {
            pageNumStr = "1";
        }

        final int pageNum = Integer.valueOf(pageNumStr);
        final int pageSize = Symphonys.PAGE_SIZE;
        final int windowSize = Symphonys.WINDOW_SIZE;

        final JSONObject requestJSONObject = new JSONObject();
        requestJSONObject.put(Pagination.PAGINATION_CURRENT_PAGE_NUM, pageNum);
        requestJSONObject.put(Pagination.PAGINATION_PAGE_SIZE, pageSize);
        requestJSONObject.put(Pagination.PAGINATION_WINDOW_SIZE, windowSize);

        final String name = request.getParameter(Common.NAME);
        if (!Strings.isEmptyOrNull(name)) {
            requestJSONObject.put(Product.PRODUCT_NAME, name);
        }

        final Map<String, Class<?>> fields = new HashMap<String, Class<?>>();
        fields.put(Keys.OBJECT_ID, String.class);
        fields.put(Product.PRODUCT_CATEGORY, String.class);
        fields.put(Product.PRODUCT_DESCRIPTION, String.class);
        fields.put(Product.PRODUCT_NAME, String.class);
        fields.put(Product.PRODUCT_PRICE, Double.class);
        fields.put(Product.PRODUCT_IMG_URL, String.class);
        fields.put(Product.PRODUCT_STATUS, Integer.class);

        final JSONObject result = productQueryService.getOnShelfProducts(requestJSONObject, fields);
        final List<JSONObject> products = CollectionUtils.jsonArrayToList(result.optJSONArray(Product.PRODUCTS));

        for (final JSONObject product : products) {
            final double price = product.optDouble(Product.PRODUCT_PRICE);
            final int point = (int) Math.floor(price * Symphonys.getInt("pointExchangeUnit"));

            product.put(Product.PRODUCT_T_POINT, point);
        }

        dataModel.put(Product.PRODUCTS, products);

        filler.fillHeaderAndFooter(request, response, dataModel);
        filler.fillRandomArticles(dataModel);
        filler.fillHotArticles(dataModel);
        filler.fillSideTags(dataModel);
        filler.fillLatestCmts(dataModel);
    }
}
