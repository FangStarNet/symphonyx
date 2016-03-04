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
package org.b3log.symphony.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import org.apache.commons.lang.StringUtils;
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.model.Pagination;
import org.b3log.latke.model.User;
import org.b3log.latke.repository.Query;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.SortDirection;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.latke.util.CollectionUtils;
import org.b3log.latke.util.Paginator;
import org.b3log.symphony.model.Order;
import org.b3log.symphony.model.UserExt;
import org.b3log.symphony.repository.OrderRepository;
import org.b3log.symphony.repository.UserRepository;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Order query service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.1, Mar 1, 2016
 * @since 1.4.0
 */
@Service
public class OrderQueryService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(OrderQueryService.class);

    /**
     * Order repository.
     */
    @Inject
    private OrderRepository orderRepository;

    /**
     * User repository.
     */
    @Inject
    private UserRepository userRepository;

    /**
     * Gets orders by the specified request json object.
     *
     * @param requestJSONObject the specified request json object, for example,      <pre>
     * {
     *     "paginationCurrentPageNum": 1,
     *     "paginationPageSize": 20,
     *     "paginationWindowSize": 10
     * }, see {@link Pagination} for more details
     * </pre>
     *
     * @param orderFields the specified order fields to return
     * @return for example,      <pre>
     * {
     *     "pagination": {
     *         "paginationPageCount": 100,
     *         "paginationPageNums": [1, 2, 3, 4, 5]
     *     },
     *     "orders": [{
     *         "oId": "",
     *         "orderUserName": "",
     *         "orderRealUserName": "",
     *
     *         ....
     *      }, ....]
     * }
     * </pre>
     *
     * @throws ServiceException service exception
     * @see Pagination
     */
    public JSONObject getOrders(final JSONObject requestJSONObject, final Map<String, Class<?>> orderFields) throws ServiceException {
        final JSONObject ret = new JSONObject();

        final int currentPageNum = requestJSONObject.optInt(Pagination.PAGINATION_CURRENT_PAGE_NUM);
        final int pageSize = requestJSONObject.optInt(Pagination.PAGINATION_PAGE_SIZE);
        final int windowSize = requestJSONObject.optInt(Pagination.PAGINATION_WINDOW_SIZE);
        final Query query = new Query().setCurrentPageNum(currentPageNum).setPageSize(pageSize)
                .addSort(Order.ORDER_STATUS, SortDirection.ASCENDING)
                .addSort(Keys.OBJECT_ID, SortDirection.DESCENDING);
        for (final Map.Entry<String, Class<?>> field : orderFields.entrySet()) {
            query.addProjection(field.getKey(), field.getValue());
        }

        JSONObject result = null;

        try {
            result = orderRepository.get(query);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Gets order failed", e);

            throw new ServiceException(e);
        }

        final int pageCount = result.optJSONObject(Pagination.PAGINATION).optInt(Pagination.PAGINATION_PAGE_COUNT);

        final JSONObject pagination = new JSONObject();
        ret.put(Pagination.PAGINATION, pagination);
        final List<Integer> pageNums = Paginator.paginate(currentPageNum, pageSize, pageCount, windowSize);
        pagination.put(Pagination.PAGINATION_PAGE_COUNT, pageCount);
        pagination.put(Pagination.PAGINATION_PAGE_NUMS, pageNums);

        final JSONArray data = result.optJSONArray(Keys.RESULTS);
        final List<JSONObject> orders = CollectionUtils.<JSONObject>jsonArrayToList(data);

        for (final JSONObject order : orders) {
            final String buyerId = order.optString(Order.ORDER_BUYER_ID);
            final String handlerId = order.optString(Order.ORDER_HANDLER_ID);

            try {
                final JSONObject buyer = userRepository.get(buyerId);
                final String userName = buyer.optString(User.USER_NAME);
                final String userRealName = buyer.optString(UserExt.USER_REAL_NAME);

                order.put(Order.ORDER_T_BUYER_NAME, userName);
                order.put(Order.ORDER_T_BUYER_REAL_NAME, userRealName);

                order.put(Order.ORDER_T_HANDLER_NAME, "");
                order.put(Order.ORDER_T_HANDLER_REAL_NAME, "");

                if (StringUtils.isNotBlank(handlerId)) {
                    final JSONObject handler = userRepository.get(handlerId);
                    final String handlerName = handler.optString(User.USER_NAME);
                    final String handlerRealName = handler.optString(UserExt.USER_REAL_NAME);

                    order.put(Order.ORDER_T_HANDLER_NAME, handlerName);
                    order.put(Order.ORDER_T_HANDLER_REAL_NAME, handlerRealName);
                }
            } catch (final RepositoryException e) {
                LOGGER.log(Level.ERROR, "Query user fialed", e);
            }

            final long createTime = order.optLong(Order.ORDER_CREATE_TIME);
            order.put(Order.ORDER_CREATE_TIME, new Date(createTime));

            final long confirmTime = order.optLong(Order.ORDER_CONFIRM_TIME);
            order.put(Order.ORDER_CONFIRM_TIME, new Date(confirmTime));
        }

        ret.put(Order.ORDERS, orders);

        return ret;
    }

    /**
     * Gets a order by the specified id.
     *
     * @param orderId the specified id
     * @return order, return {@code null} if not found
     * @throws ServiceException service exception
     */
    public JSONObject getOrder(final String orderId) throws ServiceException {
        try {
            return orderRepository.get(orderId);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Gets a order [orderId=" + orderId + "] failed", e);

            throw new ServiceException(e);
        }
    }
}
