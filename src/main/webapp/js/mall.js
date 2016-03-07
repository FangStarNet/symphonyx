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
 * @version 1.1.3.1, Mar 7, 2016
 */

/**
 * @description Mall function
 * @static
 */
var Mall = {
    /**
     * 根据 tag 进行过滤
     * @param {string} tag
     * @param {bom} it
     * @returns {undefined}
     */
    filter: function (tag, it) {
        $('.list li').each(function () {
            var $it = $(this),
                    $tag = $it.find('.tag');
            if (tag === $tag.text()) {
                $it.show();
            } else {
                $it.hide();
            }
            $(it).parent().find('.tag').removeClass('selected');
            $(it).addClass('selected');
        });
    },
    /**
     * 初始化前端过滤
     * @returns {undefined}
     */
    initFilter: function () {
        var tags = [];
        $('.list li').each(function () {
            var hasTag = false,
                    $it = $(this),
                    $tag = $it.find('.tag');
            for (var j = 0; j < tags.length; j++) {
                if (tags[j] === $tag.text()) {
                    hasTag = true;
                    break;
                }
            }
            if (!hasTag) {
                tags.push($tag.text());
            }
        });

        var tagsHTML = '';
        for (var a = 0, aMax = tags.length; a < aMax; a++) {
            tagsHTML += '<span class="tag fn-pointer" onclick="Mall.filter(\'' + tags[a] + '\', this)">' + tags[a] + '</span> &nbsp;';
        }

        $('.list').before('<div class=tags>' + tagsHTML + '</div><br/>');
    },
    /**
     * 修改购买数量
     * @param {type} it
     * @param {type} singlePoint
     * @param {type} label
     * @returns {undefined}
     */
    updateSum: function (it, singlePoint, label) {
        var count = parseInt($.trim($(it).val()));
        if ($.trim($(it).val()) === '') {
            $(it).val(1);
            count = 1;
        }
        if (!/^[1-9][0-9]?$/.test(count)) {
            $(it).val(99);
            count = 99;
        }

        var sum = count * singlePoint;
        $(it).next().text(sum + label);
    },
    /**
     * @description Buy product
     * @argument {String} productId product id
     * @argument {String} csrfToken CSRF token
     * @argument {String} tip confirm tip
     */
    buyProduct: function (productId, csrfToken, tip, it) {
        if (confirm(tip + ' ' + $(it).text() + ' ' + $(it).prev().val() + ' 份 ' + $(it).parent().prev().find('font').text() + '?')) {
            $(it).prop('disabled', true);
            var requestJSONObject = {
                "productId": productId,
                "num": $(it).prev().val()
            };

            $.ajax({
                url: "/mall/product/buy",
                type: "POST",
                headers: {"csrfToken": csrfToken},
                cache: false,
                data: JSON.stringify(requestJSONObject),
                error: function (jqXHR, textStatus, errorThrown) {
                    alert(errorThrown);
                    $(it).prop('disabled', false);
                },
                success: function (result, textStatus) {
                    alert(result.msg);
                    $(it).prop('disabled', false);

                    if (result.sc) {
                        window.location.href = result["goto"];
                    }
                }
            });
        }
    }
};

Mall.initFilter();