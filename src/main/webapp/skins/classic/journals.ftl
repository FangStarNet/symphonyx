<#include "macro-head.ftl">
<#include "macro-pagination.ftl">
<!DOCTYPE html>
<html>
    <head>
        <@head title="${symphonyLabel} - ${journalLabel}">
        <meta name="description" content="${journalLabel}"/>
        </@head>
        <link type="text/css" rel="stylesheet" href="${staticServePath}/css/index${miniPostfix}.css?${staticResourceVersion}" />
    </head>
    <body>
        <#include "header.ftl">
        <div class="main">
            <div class="wrapper">
                <div class="content fn-clear">
                    <p>
                        航海日记开始于 2016 年情人节，之前的内容存档于<a href="http://wiki.fangstar.net/doku.php?id=teamblog">房星の征途</a><sup>Team Blog</sup>
                    </p>
                    <br/> 
                    <p>
                        这是一份所有伙伴一起编写的公司日志，旨在记录房星和每一位伙伴的成长历程
                    </p>
                    <p>
                        帮助伙伴们了解房星的历史以及其他人的工作内容和进度
                    </p>
                    <p>
                        以周为章，以天为节，以个人为段落，将每个人在房星每天的经历记录在此
                    </p>   <br/> 

                    <div class="article-list list">
                        <ul>
                            <#assign articleIds = "">
                            <#list latestArticles as article>
                            <#assign articleIds = articleIds + article.oId>
                            <#if article_has_next><#assign articleIds = articleIds + ","></#if>
                            <li>
                                <div class="fn-flex">
                                    <div class="fn-flex-1 has-view">
                                        <h2>
                                            <#if article_index == 0>
                                            <a data-id="${article.oId}" data-type="${article.articleType}" rel="bookmark" href="${article.articlePermalink}">当前节</a>
                                            <#elseif article_index == 1>
                                            <a data-id="${article.oId}" data-type="${article.articleType}" rel="bookmark" href="${article.articlePermalink}">当前章</a>
                                            <#else>
                                            <a data-id="${article.oId}" data-type="${article.articleType}" rel="bookmark" href="${article.articlePermalink}">${article.articleTitleEmoj}</a>
                                            </#if>
                                            <span class="ft-fade"> &nbsp;•&nbsp;</span>
                                            <span class="ft-fade ft-13">${article.timeAgo}</span>
                                        </h2>
                                    </div>
                                </div>
                                <#if article.articleCommentCount != 0>
                                <div class="cmts" title="${cmtLabel}">
                                    <a class="count ft-gray" href="${article.articlePermalink}">${article.articleCommentCount}</a>
                                </div>
                                </#if>
                                <div class="commenters">
                                    <#list article.articleParticipants as comment>
                                    <a rel="nofollow" href="${article.articlePermalink}#${comment.commentId}" title="${comment.articleParticipantName}">
                                        <img class="avatar-small" src="${comment.articleParticipantThumbnailURL}-64.jpg?${comment.articleParticipantThumbnailUpdateTime?c}" />
                                    </a>
                                    </#list>
                                </div>
                                <i class="heat" style="width:${article.articleHeat*3}px"></i>
                            </li>
                            </#list>
                        </ul>
                    </div>
                    <script type="text/javascript" src="${staticServePath}/js/lib/ws-flash/swfobject.js"></script>
                    <script type="text/javascript" src="${staticServePath}/js/lib/ws-flash/web_socket.js"></script>
                    <script type="text/javascript" src="${staticServePath}/js/lib/reconnecting-websocket.min.js"></script>
                    <script type="text/javascript" src="${staticServePath}/js/channel${miniPostfix}.js?${staticResourceVersion}"></script>
                    <script>
                        WEB_SOCKET_SWF_LOCATION = "${staticServePath}/js/lib/ws-flash/WebSocketMain.swf";

                        // Init [Article List] channel
                        ArticleListChannel.init("ws://${serverHost}:${serverPort}/article-list-channel?articleIds=${articleIds}");
                    </script>
                    <@pagination url="/journals"/>
                </div>
                <div class="side">
                    <#include "side.ftl">
                </div>
            </div>
        </div>
        <#include "footer.ftl">
        <script>
            Util.initArticlePreview();
        </script>
    </body>
</html>
