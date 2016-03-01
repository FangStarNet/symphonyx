<#include "macro-admin.ftl">
<#include "../macro-pagination.ftl">
<@admin "products">
<div class="list content">
    <button type="button" class="btn red" onclick="window.location = '/admin/add-product'">${addProductLabel}</button>
    <br/><br/>
    <ul>
        <#list products as item>
        <li class="fn-flex comment-list-item">
            <div class="avatar icon-goods" style="<#if item.productImgURL?? && item.productImgURL != ''>font-size: 0;background-image:url(${item.productImgURL})</#if>"></div>
            <div class="fn-flex-1">
                <div class="fn-clear">
                    <h2 class="fn-left">
                        <a href="/member/${item.productName}">${item.productName}</a>
                        <span class="tag">${item.productCategory}</span>
                    </h2>
                    <a href="/admin/product/${item.oId}" class="fn-right icon-edit" title="${editLabel}"></a> 
                </div>
                <div>
                    ${yuanLabel}${item.productPrice}.00 
                    <#if item.productStatus == 0>
                    <span class="ft-gray">${onShelfedLabel}</span>
                    <#elseif item.productStatus == 1>
                    <span class="ft-red">${offShelfedLabel}</span>
                    </#if>
                </div>
            </div>
        </li>
        </#list>
    </ul>
    <@pagination url="/admin/products"/>
</div>
</@admin>
