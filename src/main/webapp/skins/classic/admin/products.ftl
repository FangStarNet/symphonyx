<#include "macro-admin.ftl">
<#include "../macro-pagination.ftl">
<@admin "products">
<div class="list content admin">
    <button type="button" class="btn red" onclick="window.location = '/admin/add-product'">${addProductLabel}</button>
    <br/><br/>
    <ul>
        <#list products as item>
        <li>
            <div>
                <div class="avatar-mid icon-goods" id="goodsImg" style="<#if item.productImgURL?? && item.productImgURL != ''>font-size: 0;background-image:url(${item.productImgURL})</#if>"></div>
                ${item.productPrice} ${item.productCategory}
            </div>
            <div class="fn-clear first">
                <a href="/member/${item.productName}">${item.productName}</a>
                <a href="/admin/product/${item.oId}" class="fn-right icon-edit" title="${editLabel}"></a> &nbsp;
                <#if item.productStatus == 0>
                <span class="ft-gray">${onShelfedLabel}</span>
                <#elseif item.productStatus == 1>
                <span class="ft-red">${offShelfedLabel}</span>
                </#if>
            </div>
            <div class="fn-clear">
                <span title="${productNameLabel}"></span>
                ${item.productDescription}
            </div>
        </li>
        </#list>
    </ul>
    <@pagination url="/admin/products"/>
</div>
</@admin>
