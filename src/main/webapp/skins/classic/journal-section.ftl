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
                <li id="menu${team_index}${user_index}0">
                    <div class="fn-flex">
                        <a rel="nofollow" class="ft-gray"
                           href="/member/${user.userName}" 
                           title="${user.userName} (${user.userRealName})"><div class="avatar" style="background-image:url('${user.userAvatarURL}-64.jpg?${user.userUpdateTime?c}')"></div></a>
                        <div>
                            <a class="ft-gray" href='/member/${user.userName}'><b>${user.userName}</b> (${user.userRealName})</a>
                            <i>本日没有记录</i>
                        </div>
                    </div>
                </li>
                </#if>
                <#list user.paragraphs as paragraph>
                <li<#if paragraph_index == 0> id="menu${team_index}${user_index}0"</#if> 
                    <#if user.paragraphs?size != 1>
                    class="<#if paragraph_index == 0>start<#elseif paragraph_index == user.paragraphs?size - 1>end<#else>other</#if>"
                    </#if>
                    >
                    <div class="fn-flex">
                        <#if paragraph_index == 0>
                        <a rel="nofollow" class="ft-gray"
                           href="/member/${user.userName}" 
                           title="${user.userName} (${user.userRealName})"><div class="avatar" style="background-image:url('${user.userAvatarURL}-64.jpg?${user.userUpdateTime?c}')"></div></a>
                        <#else>
                        <div class="avatar"></div>
                        </#if>
                        <div class="fn-flex-1<#if paragraph_index == user.paragraphs?size - 1> last</#if>">
                            <h2 class="has-view">
                                <a href='${paragraph.articlePermalink}'>${paragraph.articleTitle}</a>
                                <span class="ft-fade">&nbsp;•&nbsp;</span>
                                <#list paragraph.articleTags?split(",") as articleTag>
                                <a rel="tag" class="tag" href="/tags/${articleTag?url('UTF-8')}">
                                    ${articleTag}
                                </a>&nbsp;
                                </#list>
                                <span class="ft-fade">&nbsp;•&nbsp;</span>
                                <span class="ft-fade ft-13">${paragraph.timeAgo}</span>
                            </h2>
                            <div class="content-reset">
                                ${paragraph.articleContent}
                            </div>
                        </div>
                    </div>

                    <#if paragraph.articleCommentCount != 0>
                    <div class="cmts" title="${cmtLabel}">
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
                </li>
                </#list>
                </#list>
            </ul>
        </div>
    </div>
</div>
</#list>