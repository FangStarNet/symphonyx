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
 * This class defines product model relevant keys.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @version 1.0.0.2, Mar 1, 2016
 * @since 1.4.0
 */
public final class Product {

    /**
     * Key of product.
     */
    public static final String PRODUCT = "product";

    /**
     * Key of products.
     */
    public static final String PRODUCTS = "products";

    /**
     * Key of product name.
     */
    public static final String PRODUCT_NAME = "productName";

    /**
     * Key of product category.
     */
    public static final String PRODUCT_CATEGORY = "productCategory";

    /**
     * Key of product description.
     */
    public static final String PRODUCT_DESCRIPTION = "productDescription";

    /**
     * Key of product price.
     */
    public static final String PRODUCT_PRICE = "productPrice";

    /**
     * Key of product image URL.
     */
    public static final String PRODUCT_IMG_URL = "productImgURL";

    /**
     * Key of product status.
     */
    public static final String PRODUCT_STATUS = "productStatus";

    //// Transient ////
    /**
     * Key of product point.
     */
    public static final String PRODUCT_T_POINT = "productPoint";

    //// Status constants
    /**
     * Product status - on shelf.
     */
    public static final int PRODUCT_STATUS_C_ONSHELF = 0;

    /**
     * Product status - off shelf.
     */
    public static final int PRODUCT_STATUS_C_OFFSHELF = 1;
}
