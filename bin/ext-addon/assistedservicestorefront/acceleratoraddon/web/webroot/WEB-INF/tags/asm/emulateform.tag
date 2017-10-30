<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ attribute name="actionNameKey" required="true" type="java.lang.String" %>
<%@ attribute name="actionNameKeyEnding" required="true" type="java.lang.String" %>
<%@ attribute name="action" required="true" type="java.lang.String" %>
<%@ attribute name="disabledButton" required="false" type="java.lang.Boolean" %>

<c:set var="usernamePlaceholder"><spring:theme code="asm.emulate.username.placeholder"/></c:set>
<c:set var="cartPlaceholder">
	<c:choose>
	    <c:when test="${not emulateByOrder}">
			<spring:theme code="asm.emulate.cart.placeholder"/>
		</c:when>
		<c:otherwise>
			<spring:theme code="asm.emulate.cart-order.placeholder"/>	
		</c:otherwise>
	</c:choose>
</c:set>
	
<form action="${action}" method="post" id="_asmPersonifyForm" class="asmForm">
	<fieldset>
        <span class="ASM_icon ASM_icon-contacts hidden-xs hidden-sm hidden-md"></span>
        <div class="ASM_input_holder customerId">
            <label for="customerName"><spring:theme code="asm.emulate.username.label"/></label>
		    <input name="customerId" type="hidden" value="${customerId}" placeholder="${usernamePlaceholder}" class="ASM-input"/>
            <input name="customerName" type="text" value="${customerName}" placeholder="${usernamePlaceholder}" class="ASM-input"/>
    </div>

        <div class="ASM_session_andor_text"><spring:theme code="asm.emulate.andor"/></div>

        <span class="ASM_icon ASM_icon-cart hidden-xs hidden-sm hidden-md"></span>
        <div class="ASM_input_holder cartId">
            <c:choose>
                <c:when test="${not emulateByOrder}">
		            <label for="cartId"><spring:theme code="asm.emulate.cart.label"/></label>
		            <input name="cartId" type="text" value="${cartId}" placeholder="${cartPlaceholder}" class="ASM-input"/>
        		</c:when>
	        	<c:otherwise>
	        		<label for="cartId"><spring:theme code="asm.emulate.cart-order.label"/></label>
		            <input name="cartId" type="text" value="${cartId}" placeholder="${cartPlaceholder}" class="ASM-input"/>
        		</c:otherwise>
        	</c:choose>
        </div>

		<input type="hidden" name="CSRFToken" value="${CSRFToken}">

		<button type="submit" class="ASM-btn ASM-btn-start-session" disabled><spring:theme code="${actionNameKey}"/>
            <span class="hidden-xs hidden-sm hidden-md"><spring:theme code="${actionNameKeyEnding}"/></span>
        </button>
	</fieldset>
</form>