<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ attribute name="cartData" required="true" type="de.hybris.platform.commercefacades.order.data.AbstractOrderData" %>
<%@ taglib prefix="spring"  uri="http://www.springframework.org/tags"%>
<spring:htmlEscape defaultHtmlEscape="true" />


<div class="form-group js-voucher-respond">
    <spring:theme code="text.voucher.apply.input.placeholder" var="voucherInputPlaceholder"/>
    <label class="control-label cart-voucher__label" for="voucher-code"><spring:theme code="text.voucher.apply.input.label"/></label>
    <input type="text" class="js-voucher-code cart-voucher__input form-control input-sm" name="voucher-code" id="js-voucher-code-text" maxlength="100" placeholder="${voucherInputPlaceholder}">
    <button type="button" id="js-voucher-apply-btn" class="btn btn-primary btn-small cart-voucher__btn"><spring:theme code="text.voucher.apply.button.label"/></button>

    <div class="js-voucher-validation-container help-block cart-voucher__help-block"></div>
</div>


<ul id="js-applied-vouchers" class="selected_product_ids clearfix voucher-list">
    <c:forEach items="${cartData.appliedVouchers}" var="voucher">
        <li class="voucher-list__item">
            <span class="js-release-voucher voucher-list__item-box" id="voucher-code-${voucher}" data-voucher-code='${voucher}'>
                ${voucher}
                <span class="glyphicon glyphicon-remove js-release-voucher-remove-btn voucher-list__item-remove"/>
            </span>
        </li>
    </c:forEach>
</ul>


<script id="applied-voucher-codes-template" type="text/x-jquery-tmpl">
  {{each(index, voucher) appliedVouchers}}
    <li class="voucher-list__item">
        <span class="js-release-voucher voucher-list__item-box" id="voucher-code-\${voucher}" data-voucher-code='\${voucher}'>
            \${voucher}
            <span class="glyphicon glyphicon-remove js-release-voucher-remove-btn voucher-list__item-remove"/>
        </span>
     </li>
  {{/each}}
</script>