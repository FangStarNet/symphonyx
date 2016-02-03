<#list teams as team>
<div class="module journal" id="menu${team_index}">
    <div class="module-header">
        ${team.teamName} <span class="ft-red">${team.done}</span>/${team.total}
        <span class="icon-chevron-up fn-right fn-pointer"/>
    </div>
    <div class="module-panel">
        <div class='list'>
            <ul>
                <#list team.users as user>
                <li id="menu${team_index}${user_index}0">
                    <div>
                        <a rel="nofollow" class="ft-gray"
                           href="/member/${user.userName}" 
                           title="${user.userName}"><img class="avatar-small" src="${user.userAvatarURL}-64.jpg?${user.userUpdateTime?c}" /></a>
                        </a> &nbsp;
                        <a class="ft-gray" href="/member/${user.userName}"><b>${user.userName}</b> (${user.userRealName})</a>
                        <span class="ft-red">${user.done}</span>/7
                    </div><br/>
                    
                    <#list user.weekDays as day>
                    <span class="ft-red">${day.weekDayName}</span>
                    
                    <#if day.paragraphs?size == 0>
                    <span class="ft-fade">&nbsp;•&nbsp;</span>
                    <i>本日没有记录</i>
                    </#if>
                    
                    <div class="journal-section">
                        <#list day.paragraphs as paragraph>
                        <a href='${paragraph.articlePermalink}'>${paragraph.articleTitle}</a><span class="ft-fade">&nbsp;•&nbsp;</span>
                        <#list paragraph.articleTags?split(",") as articleTag>
                        <a rel="tag" class="tag" href="/tags/${articleTag?url('UTF-8')}">
                            ${articleTag}
                        </a>&nbsp;
                        </#list>
                        <#if paragraph.articleCommentCount != 0>
                        <div class="cmts" title="${cmtLabel}">
                            <a class="count ft-gray" href="${paragraph.articlePermalink}">${paragraph.articleCommentCount}</a>
                        </div>
                        </#if>
                        <div class="content-reset">
                            ${paragraph.articleContent}
                        </div>
                        </#list>
                    </div>
                    </#list>
                </li>
                </#list>
            </ul>
        </div>
    </div>
</div>
</#list>