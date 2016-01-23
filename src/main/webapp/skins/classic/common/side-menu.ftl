<#if isLoggedIn>
<div class="module">
    <div class="module-header">
        Menu
    </div>
    <div class="module-panel">
        <ul>
            <#list [1, 2, 3] as item>
            <li>
                <a href="F">FSERP</a>
                <ul>
                    <#list [1, 2, 3] as item>
                    <li>
                        <a href="V">Vanessa</a>
                    </li>
                    </#list>
                </ul>
            </li>
            </#list>
        </ul>
    </div>
</div>
</#if>