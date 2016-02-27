<#include "macro-admin.ftl">
<#include "../macro-pagination.ftl">
<@admin "orders">
<div class="list content admin">
    <ul>
        <#list orders as item>
        <li>
            <div class="fn-clear first">
                ${item.orderProductName}
            </div>

            <div class="fn-clear">
                ${yuanLabel}${item.orderPrice}
            </div>

            <div class="fn-clear">
                ${item.orderPoint} ${pointLabel}
            </div>

            ${buyerLabel}
            <div class="fn-clear">
                ${userNameLabel}${item.orderBuyerName}
            </div>

            <div class="fn-clear">
                ${realNameLabel}${item.orderBuyerRealName}
            </div>

            <div class="fn-clear">
                ${orderTimeLabel}${item.orderCreateTime?string('yyyy-MM-dd HH:mm')}
            </div>

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
