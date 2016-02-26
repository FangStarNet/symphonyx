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

/**
 * @fileoverview mall.
 *
 * @author <a href="http://88250.b3log.org">Liang Ding</a>
 * @author <a href="http://vanessa.b3log.org">Liyuan Li</a>
 * @version 1.0.0.0, Feb 26, 2016
 */

/**
 * @description Mall function
 * @static
 */
var Mall = {
    /**
     * @description Buy product
     * @argument {String} productId product id
     * @argument {String} csrfToken CSRF token
     */
    buyProduct: function (productId, csrfToken) {
        var requestJSONObject = {
            "productId": productId
        };

        $.ajax({
            url: "/mall/product/buy",
            type: "POST",
            headers: {"csrfToken": csrfToken},
            cache: false,
            data: JSON.stringify(requestJSONObject),
            beforeSend: function () {
            },
            error: function (jqXHR, textStatus, errorThrown) {
                alert(errorThrown);
            },
            success: function (result, textStatus) {
                alert(result.msg);
            }
        });
    },
    /**
     * @description Init
     */
    init: function () {
    }
};

Mall.init();
