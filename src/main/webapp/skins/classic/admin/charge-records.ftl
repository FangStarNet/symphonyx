<#include "macro-admin.ftl">
<#include "../macro-pagination.ftl">
<@admin "chargeRecords">
<div class="list content admin">
    <button type="button" class="btn red" onclick="window.location = '/admin/point-charge'">${chargePointLabel}</button>
    <br/><br/>
    <table class="points">
        <#list rslts as item>
        <tr<#if item_index % 2 == 1> class="even"</#if>>
            <td class="date">
                ${item.time?string('yyyy-MM-dd')} 
                <span class="ft-gray">${item.time?string('HH:mm')}</span>
            </td> 
            <td class="name">  ${chargeUserNameLabel} <a href="/member/${item.userName}">${item.userRealName}</a></td>
            <td class="name">  ${handlerLabel} <a href="/member/${item.handlerName}">${item.handlerRealName}</a></td>
            <td class="name"> ${item.sum} ${pointLabel} </td>
            <td class="sum">${yuanLabel}${item.money}.00</td>
        </tr>
        </#list>
    </table>
    <br/>
    <span class="fn-right">${chargeSumLabel}<b class="ft-red">${yuanLabel} ${chargeSum}.00</b></span>
    <div class="fn-clear"></div>
    <@pagination url="/admin/charge-records"/>
</div>
</@admin>
