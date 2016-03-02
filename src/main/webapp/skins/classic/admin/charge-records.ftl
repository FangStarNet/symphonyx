<#include "macro-admin.ftl">
<#include "../macro-pagination.ftl">
<@admin "chargeRecords">
<div class="list content admin">
    <button type="button" class="btn red" onclick="window.location = '/admin/point-charge'">${chargePointLabel}</button>
    <br/>

    <ul>
        <#list rslts as item>
        <li class="fn-flex comment-list-item">
            ${chargeUserNameLabel}${item.userName}(${item.userRealName})
            ${handlerLabel}${item.handlerName}(${item.handlerRealName})
            ${yuanLabel}${item.money} - ${item.sum} ${pointLabel}
        </li>
        </#list>
    </ul>
    <@pagination url="/admin/charge"/>
</div>
</@admin>
