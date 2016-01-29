<#include "macro-admin.ftl">
<@admin "addUser">
<div class="content">
    <div class="module">
        <div class="module-header">
            <h2>${addUserLabel}</h2>
        </div>
        <div class="module-panel form fn-clear">
            <form action="/admin/add-user" method="POST">
                <label>${userNameLabel}</label>
                <input name="userName" type="text" />

                <label>${realNameLabel}</label>
                <input name="userRealName" type="text" />

                <label>${emailLabel}</label>
                <input name="userEmail" type="text" />

                <label>${passwordLabel}</label>
                <input name="userPassword" type="text" value="${userPassword}" />

                <label>${teamLabel}</label>
                <select id="userTeam" name="userTeam">
                    <#list teams as team>
                    <option value="${team}">${team}</option>
                    </#list>
                </select>

                <br/><br/>
                <button type="submit" class="green fn-right">${submitLabel}</button>
            </form>
        </div>
    </div>   
</div>
</@admin>