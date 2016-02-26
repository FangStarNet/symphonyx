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
                            <#list products as product>
                            <li>
                                ${product.productName} ${yuanLabel}${product.productPrice} - ${product.productPoint} ${pointLabel}
                                <button class="green small" onclick="Mall.buyProduct('${product.oId}', '${csrfToken}')">${buyLabel}
                                </button>
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
