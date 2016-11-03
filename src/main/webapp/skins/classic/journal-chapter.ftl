<#list teams as team>
<div class="module journal" id="menu${team_index}">
    <div class="module-header">
        ${team.teamName} <span class="ft-red">${team.done}</span>/${team.total}
        <span class="icon-chevron-up fn-right fn-pointer"/>
    </div>
    <div class="module-panel">
        <#list team.users as user>
        <div id="menu${team_index}${user_index}0" class="journal-user">
            <div>
                <a rel="nofollow" class="ft-gray"
                   href="/member/${user.userName}" 
                   title="${user.userName} (${user.userRealName})"><div class="avatar" style="background-image:url('${user.userAvatarURL}-64.jpg?${user.userUpdateTime?c}')"></div></a>
                <a class="ft-gray" href="/member/${user.userName}"><b>${user.userName}</b> (${user.userRealName})</a>
                <span class="ft-red">${user.done}</span>/7
            </div>

            <#list user.weekDays as day>
            <div class="journal-day<#if day.paragraphs?size == 0 && day_index == user.weekDays?size - 1> last</#if>">
                <#if day.paragraphs?size != 0>
                <span class="ft-red">${day.weekDayName}</span>
                <#--
                <#else>
                <span class="ft-fade">&nbsp;•&nbsp;</span>
                <i>本日没有记录</i>
                -->
                </#if>
            </div>

            <#if day.paragraphs?size != 0>
            <div class="list article-list">
                <ul>
                    <#list day.paragraphs as paragraph>
                    <li>
                        <div class="has-view">
                            <a href='${paragraph.articlePermalink}'>${paragraph.articleTitleEmoj}</a><span class="ft-fade">&nbsp;•&nbsp;</span>
                            <#list paragraph.articleTags?split(",") as articleTag>
                            <a rel="tag" class="tag" href="/tags/${articleTag?url('UTF-8')}">
                                ${articleTag}
                            </a>&nbsp;
                            </#list>
                            <span class="ft-fade">&nbsp;•&nbsp;</span>
                            <span class="ft-fade ft-13">${paragraph.timeAgo}</span>
                        </div>
                        <#if paragraph.articleCommentCount != 0>
                        <div title="${cmtLabel}" class="cmts">
                            <a class="count ft-gray" href="${paragraph.articlePermalink}">${paragraph.articleCommentCount}</a>
                        </div>
                        <div class="commenters">
                            <#list paragraph.articleParticipants as comment>
                            <a rel="nofollow" href="${article.articlePermalink}#${comment.commentId}" title="${comment.articleParticipantName}">
                                <div class="avatar-small" style="background-image:url('${comment.articleParticipantThumbnailURL}-64.jpg?${comment.articleParticipantThumbnailUpdateTime?c}')"></div>
                            </a>
                            </#list>
                        </div>
                        </#if>

                        <div class="content-reset">
                            ${paragraph.articleContent}
                        </div>
                    </li>
                    </#list>
                </ul>
            </div>
            </#if>
            </#list>
        </div>
        </#list>
    </div>
</div>
</#list>