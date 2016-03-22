<#include "macro-admin.ftl">
<#include "order-macro-pagination.ftl">
<@admin "orders">
<div class="list content admin">
    <form method="GET" action="orders" class="form">
        <input name="category" type="text" placeholder="${categoryLabel}" <#if category??>value="${category}"</#if> />
        <select id="status" name="status">
            <option value="0"<#if "0" == status> selected</#if>>${toHandleLabel}</option>
            <option value="1"<#if "1" == status> selected</#if>>${buySuccLabel}</option>
            <option value="2"<#if "2" == status> selected</#if>>${refundProcessLabel}</option>
        </select>

        <input name="from" type="date" value="${from}" />
        <input name="to" type="date" value="${to}"/>
        <button type="submit" class="green">${searchLabel}</button>
    </form>
    <br/>
    <ul>
        <#list orders as item>
        <li class="fn-clear">
            <div class="fn-left">
                <h2>
                    ${item.orderProductName}
                    <span class="ft-gray"> ${yuanLabel}${item.orderPrice}.00</span>
                </h2>
                <div>
                    <span class="ft-gray">${buyerLabel}</span>
                    <a href="/member/${item.orderBuyerName}">${item.orderBuyerRealName}</a>
                    <br/>
                    <span class="icon-date ft-gray" title="${orderTimeLabel}"></span>
                    <span class="ft-gray">${item.orderCreateTime?string('yyyy-MM-dd HH:mm')}</span>
                </div>
            </div>
            <div class="fn-right orders-align">
                <#if "" != item.orderHandlerName>
                <#if item.orderStatus == 1>
                <span class="tag">${buySuccLabel}</span>
                <#else>
                <span class="tag ft-red">${refundProcessLabel}</span>
                </#if>
                <br/>
                <span class="ft-gray">${handlerLabel}</span>
                <a href="/member/${item.orderHandlerName}">${item.orderHandlerRealName}</a> <br/>
                <span class="icon-date ft-gray" title="${handleTimeLabel}"></span>
                <span class="ft-gray">${item.orderConfirmTime?string('yyyy-MM-dd HH:mm')}</span>
                <#else>
                <button class="green orders-button" onclick="confirmSumbit('${item.oId}')">${confirmConsumeLabel}</button> &nbsp;
                <button class="red orders-button" onclick="refund('${item.oId}')">${refundProcessLabel}</button>
                </#if>
            </div>
        </li>
        </#list>
    </ul>
    <@pagination url="/admin/orders" query="status=${status}&from=${from}&to=${to}&category=${category}" />
</div>
</@admin>

<script>
    function confirmSumbit(orderId) {
        if (confirm('${confirmConsumeLabel}？')) {
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
    }

    function refund(orderId) {
        if (confirm('${confirmRefundLabel}？')) {
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
    }
</script>
