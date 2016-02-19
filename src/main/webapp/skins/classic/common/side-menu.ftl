<#if isLoggedIn>
<div class="menu" id="journalSide" style="position: fixed;overflow: auto">
    <ul>
        <#list teams as team>
        <li>
            <a href="#menu${team_index}">${team.teamName} <span class="ft-red">${team.done}</span>/${team.total}</a>
            <ul class="fn-none">
                <#list team.users as user>
                <li>
                    <a href="#menu${team_index}${user_index}0">
                        <div class="avatar-small" style="background-image:url('${user.userAvatarURL}-64.jpg?${user.userUpdateTime?c}')"></div>
                        <b>${user.userName}</b> (${user.userRealName})
                        <#if 6 == article.articleType>
                        <span class="ft-red">${user.done}</span>/7
                        </#if>
                    </a>
                </li>
                </#list>
            </ul>
        </li>
        </#list>
    </ul>
</div>
</#if>