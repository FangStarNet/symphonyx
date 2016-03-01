<#include "macro-admin.ftl">
<@admin "products">
<div class="content">
    <div class="module">
        <div class="module-header">
            <h2>${modifiableLabel}</h2>
        </div>
        <div class="module-panel form fn-clear fn-relative">
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

                <label>${goodsImgLabel}</label>
                <div class="fn-clear"></div>
                <div class="avatar-mid icon-goods" id="goodsImg" style="<#if productImgURL?? && productImgURL != ''>font-size: 0;background-image:url(${productImgURL})</#if>"></div>
                <input name="productImgURL" type="hidden" id="goodsImgInput" value="<#if productImgURL??>${productImgURL}</#if>"/>
                <br/><br/>
                <button type="submit" class="green fn-right">${submitLabel}</button>
            </form>
            <form id="goodsImgUp" class="upload-file" method="POST" enctype="multipart/form-data">
                <label class="btn">
                    ${uploadLabel}<input type="file" name="file">
                </label>
            </form>
        </div>
    </div>   
</div>
</@admin>
<script type="text/javascript" src="${staticServePath}/js/lib/jquery/file-upload-9.10.1/jquery.fileupload.min.js"></script>
<script>
    Util.initUpload({
        id: 'goodsImgUp',
        qiniuUploadToken: '<#if qiniuUploadToken??>${qiniuUploadToken}</#if>',
        userId: '${currentUser.oId}'
    }, function (data) {
        var qiniuKey = data.result.key;
        $('#goodsImg').css({
            "background-image": 'url(' + qiniuKey + ')',
            'font-size': '0'
        });
        $('#goodsImgInput').val(qiniuKey);
    }, function (data) {
        var qiniuKey = data.result.key,
                t = new Date().getTime();
        $('#goodsImg').css({
            "background-image": '<#if qiniuDomain??>url(${qiniuDomain}</#if>/' + qiniuKey + '?' + t + ')',
            'font-size': '0'
        });
        $('#goodsImgInput').val('<#if qiniuDomain??>${qiniuDomain}</#if>/' + qiniuKey);
    });
    
</script>