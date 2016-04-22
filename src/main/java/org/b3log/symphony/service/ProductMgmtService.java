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
import org.b3log.latke.repository.Transaction;
import org.b3log.latke.repository.annotation.Transactional;
import org.b3log.latke.service.LangPropsService;
import org.b3log.latke.service.ServiceException;
import org.b3log.latke.service.annotation.Service;
import org.b3log.symphony.model.Order;
import org.b3log.symphony.model.Pointtransfer;
import org.b3log.symphony.model.Product;
import org.b3log.symphony.model.UserExt;
import org.b3log.symphony.repository.ProductRepository;
import org.b3log.symphony.repository.UserRepository;
import org.b3log.symphony.util.Results;
import org.b3log.symphony.util.Symphonys;
import org.json.JSONObject;

/**
 * Product management service.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.2.1.2, Apr 22, 2016
 * @since 1.4.0
 */
@Service
public class ProductMgmtService {

    /**
     * Logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ProductMgmtService.class);

    /**
     * Product repository.
     */
    @Inject
    private ProductRepository productRepository;

    /**
     * User repository.
     */
    @Inject
    private UserRepository userRepository;

    /**
     * Order manager service.
     */
    @Inject
    private OrderMgmtService orderMgmtService;

    /**
     * Pointtransfer management service.
     */
    @Inject
    private PointtransferMgmtService pointtransferMgmtService;

    /**
     * Language service.
     */
    @Inject
    private LangPropsService langPropsService;

    /**
     * Adds the specified product.
     *
     * @param product the specified product
     * @return product id
     * @throws ServiceException service exception
     */
    @Transactional
    public String addProduct(final JSONObject product) throws ServiceException {
        try {
            return productRepository.add(product);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Adds product failed", e);

            throw new ServiceException(e);
        }
    }

    /**
     * Updates the specified product.
     *
     * @param product the specified product
     * @throws ServiceException service exception
     */
    @Transactional
    public void updateProduct(final JSONObject product) throws ServiceException {
        try {
            final String productId = product.optString(Keys.OBJECT_ID);

            productRepository.update(productId, product);
        } catch (final RepositoryException e) {
            LOGGER.log(Level.ERROR, "Updates product failed", e);

            throw new ServiceException(e);
        }
    }

    /**
     * Buys a product.
     *
     * @param productId the specified productId
     * @param userId the specified buyer id
     * @param num the specified number
     * @return result
     */
    public synchronized JSONObject buyProduct(final String productId, final String userId, final int num) {
        final JSONObject ret = Results.falseResult();

        try {
            if (num < 0 || num > 99) {
                ret.put(Keys.MSG, langPropsService.get("invalidOrderStatusLabel"));

                return ret;
            }

            final JSONObject product = productRepository.get(productId);
            if (Product.PRODUCT_STATUS_C_ONSHELF != product.optInt(Product.PRODUCT_STATUS)) {
                ret.put(Keys.MSG, langPropsService.get("productOffShelfLabel"));

                return ret;
            }

            if (product.optInt(Product.PRODUCT_COUNT) < 1) {
                ret.put(Keys.MSG, langPropsService.get("productSellOutLabel"));

                return ret;
            }

            if (num > product.optInt(Product.PRODUCT_COUNT)) {
                ret.put(Keys.MSG, langPropsService.get("insufficientProductCountLabel"));

                return ret;
            }

            final double price = product.optDouble(Product.PRODUCT_PRICE);
            final int point = (int) Math.floor(price * Symphonys.getInt("pointExchangeUnit"));

            if (point * num < 0) {
                ret.put(Keys.MSG, langPropsService.get("invalidOrderStatusLabel"));

                return ret;
            }

            final JSONObject user = userRepository.get(userId);
            final int balance = user.optInt(UserExt.USER_POINT);

            if (balance - point * num < Symphonys.getInt("pointExchangeMin")) {
                String msg = langPropsService.get("exchangeMinLabel");
                msg = msg.replace("{point}", Symphonys.get("pointExchangeMin"));
                ret.put(Keys.MSG, langPropsService.get("insufficientBalanceLabel") + langPropsService.get("colonLabel")
                        + msg);

                return ret;
            }

            int sellCount = 0;
            for (int i = 0; i < num; i++) {
                final JSONObject order = new JSONObject();

                order.put(Order.ORDER_POINT, point);
                order.put(Order.ORDER_PRICE, price);
                order.put(Order.ORDER_PRODUCT_NAME, product.optString(Product.PRODUCT_NAME));
                order.put(Order.ORDER_PRODUCT_CATEGORY, product.optString(Product.PRODUCT_CATEGORY));
                order.put(Order.ORDER_BUYER_ID, userId);

                final String orderId = orderMgmtService.addOrder(order);
                final boolean succ = null != pointtransferMgmtService.transfer(userId, "sys",
                        Pointtransfer.TRANSFER_TYPE_C_BUY_PRODUCT, point, orderId);
                ret.put(Keys.STATUS_CODE, succ);

                if (!succ) {
                    LOGGER.log(Level.ERROR, "Order [id=" + orderId + "] failed");

                    return ret;
                }

                sellCount++;
            }

            if (sellCount > 0) {
                final Transaction transaction = productRepository.beginTransaction();

                try {
                    final int remainCount = product.optInt(Product.PRODUCT_COUNT);
                    product.put(Product.PRODUCT_COUNT, remainCount - sellCount);

                    productRepository.update(productId, product);

                    transaction.commit();
                } catch (final Exception e) {
                    if (null != transaction && transaction.isActive()) {
                        transaction.rollback();
                    }

                    LOGGER.log(Level.ERROR, "Decreas product count failed", e);
                }
            }
        } catch (final Exception e) {
            LOGGER.log(Level.ERROR, "Buys product failed", e);

            ret.put(Keys.MSG, e.getMessage());
        }

        return ret;
    }
}
