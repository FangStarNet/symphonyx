<#include "macro-admin.ftl">
<#include "../macro-pagination.ftl">
<@admin "orders">
<div class="list content admin">
    <ul>
        <#list orders as item>
        <li class="fn-flex comment-list-item">
            <div class="avatar icon-goods" style="<#if item.img?? && item.img != ''>font-size: 0;background-image:url(${item.img})</#if>"></div>
            <div class="fn-flex-1">
                <div class="fn-clear">
                    <h2 class="fn-left">
                        <a href="/member/${item.orderProductName}">${item.orderProductName}</a>
                        <span class="tag">${item.productCategory}</span>
                    </h2>
                    <a href="/admin/product/${item.oId}" class="fn-right icon-edit" title="${editLabel}"></a> 
                </div>
                <div>
                    ${yuanLabel}${item.orderPrice}.00  ==  ${item.orderPoint} ${pointLabel}
                    ${buyerLabel}

                    <div class="fn-clear">
                        ${realNameLabel}${item.orderBuyerRealName}
                    </div>

                    <div class="fn-clear">
                        ${orderTimeLabel}${item.orderCreateTime?string('yyyy-MM-dd HH:mm')}
                    </div>
                    ${item.orderStatus}
                    <#if "" != item.orderHandlerName>
                    <div class="fn-clear">
                        ${handlerLabel}

                        ${item.orderHandlerName}(${item.orderHandlerRealName})
                        ${handleTimeLabel}${item.orderConfirmTime?string('yyyy-MM-dd HH:mm')}
                    </div>
                    <#else>
                    <button class="small green" onclick="confirm('${item.oId}')">${confirmConsumeLabel}</button>
                    <button class="small red" onclick="refund('${item.oId}')">${confirmRefundLabel}</button>
                    </#if>
                </div>
            </div>
        </li>
        </#list>
    </ul>
    <@pagination url="/admin/orders"/>
</div>
</@admin>

<script>
    function confirm(orderId) {
        $.ajax({
            url: "/admin/order/" + orderId + "/confirm",
            type: "POST",
            headers: {"csrfToken": "${csrfToken}"},
            cache: false,
            success: function (result, textStatus) {
                alert(result.msg);

                location.reload();
            }
        });
    }

    function refund(orderId) {
        $.ajax({
            url: "/admin/order/" + orderId + "/refund",
            type: "POST",
            headers: {"csrfToken": "${csrfToken}"},
            cache: false,
            success: function (result, textStatus) {
                alert(result.msg);

                location.reload();
            }
        });
    }
</script>
