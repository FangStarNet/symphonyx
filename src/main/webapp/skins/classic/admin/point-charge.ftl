<#include "macro-admin.ftl">
<@admin "pointCharge">
<div class="content">
    <div class="module">
        <div class="module-header">
            <h2>${chargePointLabel}</h2>
        </div>
        <div class="module-panel form fn-clear">
            <form action="/admin/charge-point" method="POST">
                <label>${userNameLabel}</label>
                <input type="text" name="userName" value="" />

                <label>${pointLabel}</label>
                <input type="text" name="point" value="" />

                <label>${yuanLabel}</label>
                <input type="text" name="memo" value="" placeholder="${chargePointPlaceholderLabel}" />

                <br/><br/>
                <button type="submit" class="green fn-right">${submitLabel}</button>
            </form>
        </div>
    </div>
</div>
</@admin>