<div class="nav"> 
    <div class="wrapper fn-clear">
        <div class="head-fn fn-clear">
            <h1 class="fn-left">
                <a href="/" style="background-image: url('${staticServePath}/images/logo.jpg')" alt="${symphonyLabel}" 
                   title="${symphonyLabel}" width="42" class="fn-pointer"></a>
            </h1>
            <#-- 百度站内搜索不支持 HTTPS 
            <form class="responsive-hide" target="_blank" action="https://search.hacpai.com/cse/search">
                <span class="icon-search"></span>
                <input class="search" type="text" name="q">
                <input type="hidden" value="11228953646196486415" name="s">
                <input type="hidden" name="cc" value="hacpai.com">
                <input type="submit" class="fn-none" value="">
            </form>
            -->
            <div class="fn-right">
                <a href="/mall" class="icon-points last" title="${mallLabel}"></a>
                <a href="/timeline" class="icon-clock" title="${timelineLabel}"></a>
                <a href="/journals" class="icon-navigation" title="${journalLabel}"></a>
                <a href="/recent" class="icon-refresh" title="${recentArticleLabel}"></a>
            </div>
        </div>

        <div class="fn-clear user-nav">
            <#if isLoggedIn>
            <a id="logout" href="${logoutURL}" title="${logoutLabel}" class="last icon-logout"></a>
            <#if "adminRole" == userRole>
            <a href="/admin" title="${adminLabel}" class="icon-userrole"></a>
            </#if>
            <#if "mallAdminRole" == userRole>
            <a href="/admin/orders" title="${adminLabel}" class="icon-userrole"></a>
            </#if>
            <a href="/member/${currentUser.userName}" title="Home" class="nav-avatar">
                <span class="avatar-small" style="background-image:url('${currentUser.userAvatarURL}-64.jpg?${currentUser.userUpdateTime?c}')"></span>
            </a>
            <a href="/activities" title="${activityLabel}" class="icon-flag"></a>
            <a href="/pre-post" title="${addArticleLabel}" 
               class="icon-addfile responsive-show"></a>
            <a id="aNotifications" class="<#if unreadNotificationCount == 0>no-msg<#else>msg</#if>" href="/notifications" title="${messageLabel}">${unreadNotificationCount}</a>
            <#else>
            <a id="aRegister" href="javascript:Util.goRegister()" class="last icon-register" 
               title="${registerLabel}"></a>
            <a href="javascript: Util.showLogin();" class="icon-login" title="${loginLabel}"></a>
            <div class="form fn-none">
                <table cellspacing="0" cellpadding="0">
                    <tr>
                        <td width="40">
                            <label for="nameOrEmail">${accountLabel}</label>
                        </td>
                        <td>
                            <input id="nameOrEmail" type="text" placeholder="${nameOrEmailLabel}" />
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <label for="loginPassword">${passwordLabel}</label>
                        </td>
                        <td>
                            <input type="password" id="loginPassword" />
                        </td>
                    </tr>
                    <tr>
                        <td colspan="2" align="right">
                            <div id="loginTip" class="tip"></div><br/>
                            <button class="info" onclick="window.location.href='${servePath}/forget-pwd'">${forgetPwdLabel}</button>
                            <button class="red" onclick="Util.login()">${loginLabel}</button>
                        </td>
                    </tr>
                </table>
            </div>
            </#if>
        </div>
    </div>
</div>