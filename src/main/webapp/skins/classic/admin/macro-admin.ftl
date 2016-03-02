<#macro admin type>
<#include "../macro-head.ftl">
<!DOCTYPE html>
<html>
    <head>
        <#if type == "index">
        <@head title="${symphonyLabel} - ${consoleIndexLabel}"></@head>
        </#if>
        <#if type == "users">
        <@head title="${symphonyLabel} - ${userAdminLabel}"></@head>
        </#if>
        <#if type == "addUser">
        <@head title="${symphonyLabel} - ${addUserLabel}"></@head>
        </#if>
        <#if type == "articles">
        <@head title="${symphonyLabel} - ${articleAdminLabel}"></@head>
        </#if>
        <#if type == "comments">
        <@head title="${symphonyLabel} - ${commentAdminLabel}"></@head>
        </#if>
        <#if type == "tags">
        <@head title="${symphonyLabel} - ${tagAdminLabel}"></@head>
        </#if>
        <#if type == "products">
        <@head title="${symphonyLabel} - ${productAdminLabel}"></@head>
        </#if>
        <#if type == "addProduct">
        <@head title="${symphonyLabel} - ${addProductLabel}"></@head>
        </#if>
        <#if type == "orders">
        <@head title="${symphonyLabel} - ${orderAdminLabel}"></@head>
        </#if>
        <#if type == "misc">
        <@head title="${symphonyLabel} - ${miscAdminLabel}"></@head>
        </#if>
        <link type="text/css" rel="stylesheet" href="${staticServePath}/css/home${miniPostfix}.css?${staticResourceVersion}" />
    </head>
    <body>
        <#include "../header.ftl">
        <div class="main">
            <div class="wrapper">
                <#nested>
                <div class="side">
                    <ul class="note-list">
                        <#if "adminRole" == userRole>
                        <li<#if type == "index"> class="current"</#if>><a href="/admin">${consoleIndexLabel}</a></li>
                        <li<#if type == "users" || type == "addUser"> class="current"</#if>><a href="/admin/users">${userAdminLabel}</a></li>
                        <li<#if type == "articles"> class="current"</#if>><a href="/admin/articles">${articleAdminLabel}</a></li>
                        <li<#if type == "comments"> class="current"</#if>><a href="/admin/comments">${commentAdminLabel}</a></li>
                        <li<#if type == "tags"> class="current"</#if>><a href="/admin/tags">${tagAdminLabel}</a></li>
                        <li<#if type == "orders"> class="current"</#if>><a href="/admin/orders">${orderAdminLabel}</a></li>
                        <li<#if type == "products" || type == "addProduct"> class="current"</#if>><a href="/admin/products">${productAdminLabel}</a></li>
                        <li<#if type == "misc"> class="current"</#if>><a href="/admin/misc">${miscAdminLabel}</a></li>
                        <#elseif "mallAdminRole" == userRole>
                        <li<#if type == "orders"> class="current"</#if>><a href="/admin/orders">${orderAdminLabel}</a></li>
                        <li<#if type == "products" || type == "addProduct"> class="current"</#if>><a href="/admin/products">${productAdminLabel}</a></li>
                        </#if>
                    </ul>
                </div>
            </div>
        </div>
        <#include "../footer.ftl">
    </body>
</html>
</#macro>
