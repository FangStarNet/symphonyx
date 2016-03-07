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
                <div class="content mall">
                    <div>
                        <font style="color: red;">♥</font> <a href="/charge/point">${chargePointLabel}</a>
                    </div>
                    <br/>   
                    <div class="list form">
                        <ul>
                            <#list products as item>
                            <li class="fn-flex comment-list-item">
                                <div class="avatar icon-goods" style="<#if item.productImgURL?? && item.productImgURL != ''>font-size: 0;background-image:url(${item.productImgURL})</#if>"></div>
                                <div class="fn-flex-1">
                                    <div class="fn-clear">
                                        <h2 class="fn-left">
                                            <font>${item.productName}</font>
                                            <span class="tag">${item.productCategory}</span>
                                        </h2>
                                    </div>
                                    <div class="ft-gray">
                                        ${item.productDescription}
                                    </div>
                                </div>
                                <div class="action">
                                    <input type="number" max="99" min="1" value="1" onchange="Mall.updateSum(this, '${item.productPoint?c}', ' ${pointLabel} ${buyLabel}')"
                                           onkeyup="Mall.updateSum(this, '${item.productPoint?c}', ' ${pointLabel} ${buyLabel}')" />
                                    <button class="green" onclick="Mall.buyProduct('${item.oId}', '${csrfToken}', '${confirmConsumeLabel}', this)">${item.productPoint?c} ${pointLabel} ${buyLabel}</button>
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
        <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery.isotope/2.2.2/isotope.pkgd.min.js"></script>
    </body>
</html>
