<#if isLoggedIn>
<div class="menu" style="position: fixed">
    <ul>
        <#list teams as team>
        <li>
            <a href="#menu${team_index}">${team.teamName}</a>
            <ul class="fn-none">
                <#list team.users as user>
                <li>
                    <a href="#menu${team_index}${user_index}0">${user.userName}</a>
                </li>
                </#list>
            </ul>
        </li>
        </#list>
    </ul>
</div>
</#if>