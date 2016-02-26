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
package org.b3log.symphony.model;

/**
 * This class defines order model relevant keys.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.0, Feb 24, 2016
 * @since 1.4.0
 */
public final class Order {

    /**
     * Key of order.
     */
    public static final String ORDER = "order";

    /**
     * Key of orders.
     */
    public static final String ORDERS = "orders";

    /**
     * Key of order user id.
     */
    public static final String ORDER_USER_ID = "orderUserId";

    /**
     * Key of order product name.
     */
    public static final String ORDER_PRODUCT_NAME = "orderProductName";

    /**
     * Key of order price.
     */
    public static final String ORDER_PRICE = "orderPrice";

    /**
     * Key of order point.
     */
    public static final String ORDER_POINT = "orderPoint";

    /**
     * Key of order status.
     */
    public static final String ORDER_STATUS = "orderStatus";

    /**
     * Key of order financial user id.
     */
    public static final String ORDER_FINANCIAL_USER_ID = "orderFinancialUserId";

    /**
     * Key of order create time.
     */
    public static final String ORDER_CREATE_TIME = "orderCreateTime";

    /**
     * Key of order confirm time.
     */
    public static final String ORDER_CONFIRM_TIME = "orderConfirmTime";

    //// Status constants
    /**
     * Order status - init.
     */
    public static final int ORDER_STATUS_C_INIT = 0;

    /**
     * Order status - confirmed.
     */
    public static final int ORDER_STATUS_C_CONFIRMED = 1;

    /**
     * Order status - refunded.
     */
    public static final int ORDER_STATUS_C_REFUNDED = 2;
}
