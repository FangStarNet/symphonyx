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

import javax.inject.Inject;
import org.b3log.latke.Keys;
import org.b3log.latke.logging.Level;
import org.b3log.latke.logging.Logger;
import org.b3log.latke.repository.RepositoryException;
import org.b3log.latke.repository.annotation.Transactional;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.symphony.model.Order;
import org.b3log.symphony.repository.OrderRepository;
import org.json.JSONObject;

/**
 * Order management service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Feb 24, 2016
 * @since 1.4.0
 */
@Service
public class OrderMgmtService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(OrderMgmtService.class);

    /**
     * Order repository.
     */
    @Inject
    private OrderRepository orderRepository;

    /**
     * Adds the specified order.
     *
     * @param order the specified order
     * @return order id
     * @throws ServiceException service exception
     */
    @Transactional
    public String addOrder(final JSONObject order) throws ServiceException {
        try {
            order.put(Order.ORDER_CONFIRM_TIME, 0);
            order.put(Order.ORDER_CREATE_TIME, System.currentTimeMillis());
            order.put(Order.ORDER_HANDLER_ID, "");
            order.put(Order.ORDER_STATUS, Order.ORDER_STATUS_C_INIT);

            return orderRepository.add(order);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Adds order failed", e);

            throw new ServiceException(e);
        }
    }

    /**
     * Updates the specified order.
     *
     * @param order the specified order
     * @throws ServiceException service exception
     */
    @Transactional
    public void updateOrder(final JSONObject order) throws ServiceException {
        try {
            final String orderId = order.optString(Keys.OBJECT_ID);

            orderRepository.update(orderId, order);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Updates order failed", e);

            throw new ServiceException(e);
        }
    }
}
