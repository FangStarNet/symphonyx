<#if isLoggedIn>
<div class="module" style="position: fixed">
    <div class="module-header">
        Menu
    </div>
    <div class="module-panel">
        <ul>
            <#list [1, 2, 3] as itema>
            <li>
                <a href="#menu${itema}">FSERP</a>
                <ul>
                    <#list [1, 2, 3] as item>
                    <li>
                        <a href="#menu${itema}${item}">Vanessa</a>
                    </li>
                    </#list>
                </ul>
            </li>
            </#list>
        </ul>
    </div>
</div>
</#if>