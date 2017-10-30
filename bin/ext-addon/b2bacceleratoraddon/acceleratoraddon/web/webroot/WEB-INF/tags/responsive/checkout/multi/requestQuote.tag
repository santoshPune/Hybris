<%@ tag body-content="empty" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<script type="text/javascript"> // set vars
	var cartDataQuoteAllowed = ${cartData.quoteAllowed};
	var negotiateQuote = ${placeOrderForm.negotiateQuote};
</script>

<div style="display:none">
	<div id="requestQuoteForm">
		<div class="headline"><spring:theme code="responsive.checkout.summary.requestQuote.quoteReason"/></div>

		<form:textarea cssClass="text" id="quoteRequestDescription" path="quoteRequestDescription" maxlength="600" />

		<div class="form-actions clearfix" style="clear:both;">
			<form:input type="hidden" class="requestQuoteClass" path="negotiateQuote"/>
			<div class="help-block">
				<spring:theme code="responsive.checkout.summary.requestQuote.maxchars"/>
			</div>

            <div class="modal-actions">
                <div class="row">
                    <div class="col-sm-6 col-sm-push-6">
                        <button type="submit" class="btn btn-primary btn-block" id="placeRequestQuote" disabled="disabled">
                            <spring:theme code="responsive.checkout.summary.requestQuote.proceed"/>
                        </button>
                    </div>
                    <div class="col-sm-6 col-sm-pull-6">
                        <button type="button" class="btn btn-default btn-block" id="cancelRequestQuote">
                            <spring:theme code="checkout.summary.requestQuote.cancel"/>
                        </button>
                    </div>
                </div>
            </div>
        </div>
	</div>
</div>
