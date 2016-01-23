<#list teams as team>
<div class="module journal">
    <div class="module-header" id="menu${team_index}">
        ${team.teamName} <span class="ft-red">${team.done}</span>/${team.total}
        <span class="icon-chevron-up fn-right fn-pointer"/>
    </div>
    <div class="module-panel">
        <div class='article-list list'>
            <ul>
                <#list team.users as user>
                <#if user.paragraphs?size == 0>
                <li id="menu${team_index}${user_index}">
                    <div class="fn-flex">
                        <a rel="nofollow" class="ft-gray"
                           href="/member/${user.userName}" 
                           title="${user.userName}"><img class="avatar" src="${user.userAvatarURL}-64.jpg?${user.userUpdateTime?c}" /></a>
                        <div class="fn-flex-1">
                            <div class="article-content">
                                <a class="ft-gray" href='/member/${user.userName}'><b>${user.userName}</b></a>
                                <i>本日没有记录</i>
                            </div>
                        </div>
                    </div>
                </li>
                </#if>
                <#list user.paragraphs as paragraph>
                <li>
                    <div class="fn-flex">
                        <a rel="nofollow" class="ft-gray"
                           href="/member/${user.userName}" 
                           title="${user.userName}"><img class="avatar" src="${user.userAvatarURL}-64.jpg?${user.userUpdateTime?c}" /></a>
                        <div class="fn-flex-1">
                            <h2>
                                <a href='${paragraph.articlePermalink}'>${paragraph.articleTitle}</a>
                            </h2>
                            <div class="article-content">
                                ${paragraph.articleContent}
                            </div>
                            <#list paragraph.articleTags?split(",") as articleTag>
                            <a rel="tag" class="tag" href="/tags/${articleTag?url('UTF-8')}">
                                ${articleTag}
                            </a>&nbsp;
                            </#list>

                            <span class="ft-fade">&nbsp;•&nbsp;</span>
                            <a class="ft-gray" href='/member/${user.userName}'><b>${user.userName}</b></a>
                            <span class="ft-fade">&nbsp;•&nbsp;${paragraph.timeAgo}</span>

                        </div>
                    </div>
                    <#if paragraph.articleCommentCount != 0>
                    <div class="cmts" title="${cmtLabel}">
                        <a class="count ft-gray" href="${paragraph.articlePermalink}">${paragraph.articleCommentCount}</a>
                    </div>
                    </#if>
                    <div class="commenters">
                        <#list paragraph.articleParticipants as comment>
                        <a rel="nofollow" href="${article.articlePermalink}#${comment.commentId}" title="${comment.articleParticipantName}">
                            <img class="avatar-small" src="${comment.articleParticipantThumbnailURL}-64.jpg?${comment.articleParticipantThumbnailUpdateTime?c}" />
                        </a>
                        </#list>
                    </div>
                </li>
                </#list>
                </#list>
            </ul>
        </div>
    </div>
</div>
</#list>