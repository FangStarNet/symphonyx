<#include "macro-head.ftl">
<!DOCTYPE html>
<html>
    <head>
        <@head title="${symphonyLabel} - ${mallLabel}">
        <meta name="description" content="${mallLabel}"/>
        </@head>
        <link type="text/css" rel="stylesheet" href="${staticServePath}/css/index${miniPostfix}.css?${staticResourceVersion}" />
    </head>
    <body>
        <#include "header.ftl">
        <div class="main">
            <div class="wrapper">
                <div class="content">
                    <h2>${mallLabel}</h2>
                    <div class="list">
                        <ul id="ul">
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
                                        ￥${item.productPrice}.00 
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
                    </div>
                </div>
                <div class="side">
                    <#include "side.ftl">
                </div>
            </div>
        </div>
        <#include "footer.ftl">
        <script type="text/javascript" src="${staticServePath}/js/mall${miniPostfix}.js?${staticResourceVersion}"></script>
    </body>
</html>
