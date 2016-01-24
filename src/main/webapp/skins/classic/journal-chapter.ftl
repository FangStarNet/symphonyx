<#list teams as team>
<div class="module journal">
    <div class="module-header">
        ${team.teamName}
        <span class="icon-chevron-up fn-right fn-pointer"/>
    </div>
    <div class="module-panel">
        <div class='list'>
            <ul>
                <#list team.users as user>
                <li>
                    <a rel="nofollow" class="ft-gray"
                       href="/member/${user.userName}" 
                       title="${user.userName}"><img class="avatar" src="${user.userAvatarURL}-64.jpg?${user.userUpdateTime?c}" /></a>
                    </a> &nbsp;
                    <a class="ft-gray" href='e'>${user.userName}</a>
                    <span class="ft-red">6</span>/7
                    <#list user.weekDays as weekDay>
                    <div class="journal-section" id="menu${user}${weekDay}">
                        <span class="ft-red">星期一</span>
                        <span class="ft-fade">&nbsp;•&nbsp;</span>
                        <a href='e'>title</a><span class="ft-fade">&nbsp;•&nbsp;</span>
                        <a rel="tag" class="tag" href="/tags/%E8%88%AA%E6%B5%B7%E6%97%A5%E8%AE%B0">航海日记</a>
                        <a title="评论" class="cmt-count" href="/article/1453533976430">1</a>
                        <div class="article-content">
                            <p>D248 2016-1-22</p> 
                            <p>0900 晨会</p> 
                            <p>0930 新房通设计评审</p> 
                            <p>1030 聂娟谈话</p> 
                            <p>1600 新房通产品再沟通</p> 
                            <p>”通往牛逼的路上，风景差得让人只想说脏话，但创业者在意的是远方“–罗永浩</p>
                        </div>
                    </div>
                    </#list>
                </li>
                </#list>
            </ul>
        </div>
    </div>
</div>
</#list>