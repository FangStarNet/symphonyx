<#include "macro-admin.ftl">
<@admin "products">
<div class="content">
    <div class="module">
        <div class="module-header">
            <h2>${modifiableLabel}</h2>
        </div>
        <div class="module-panel form fn-clear">
            <form action="/admin/product/${product.oId}" method="POST">
                <label>${productNameLabel}</label>
                <input name="productName" type="text" value="${product.productName}"/>

                <label>${descriptionLabel}</label>
                <input name="productDescription" type="text" value="${product.productDescription}"/>

                <label>${priceLabel}</label>
                <input name="productPrice" type="number" value="${product.productPrice}"/>
                
                <label>${categoryLabel}</label>
                <input name="productCategory" type="text" value="${product.productCategory}"/>

                <label>${statusLabel}</label>
                <select id="productStatus" name="productStatus">
                    <option value="0"<#if 0 == product.productStatus> selected</#if>>${onShelfLabel}</option>
                    <option value="1"<#if 1 == product.productStatus> selected</#if>>${offShelfLabel}</option>
                </select>

                <br/><br/>
                <button type="submit" class="green fn-right">${submitLabel}</button>
            </form>
        </div>
    </div>   
</div>
</@admin>