<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="orderData" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData"%>
<%@ attribute name="quoteOrderDecisionURL" required="true" type="java.lang.String"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/order" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<c:set var="isStatusApprovedOrRejectedQuote" value="${orderData.status=='APPROVED_QUOTE' || orderData.status=='REJECTED_QUOTE'}"/>

<div class="accountActions-link">
    <c:if test="${isStatusApprovedOrRejectedQuote}">
        <div class="enable-link">
            <order:quoteOrderDecisionForm quoteOrderDecisionURL="${quoteOrderDecisionURL}" orderCode="${orderData.code}"
                                          selectedQuoteDecision="NEGOTIATEQUOTE" displayAsLink="true"
                                          decisionLabelKey="text.quotes.requestNewQuoteButton.displayName"
                                          modalTitleLabelKey="text.quotes.requestNewQuoteButton.displayName"
                                          modalInputLabelKey="text.quotes.requestNewQuote.label" modalCommentRequired="true" />
        </div>
    </c:if>

    <c:if test="${isStatusApprovedOrRejectedQuote || orderData.status=='PENDING_QUOTE'}">
        <div class="disable-link">
            <order:quoteOrderDecisionForm quoteOrderDecisionURL="${quoteOrderDecisionURL}" orderCode="${orderData.code}"
                                          selectedQuoteDecision="CANCELQUOTE" displayAsLink="true"
                                          decisionLabelKey="text.quotes.cancelQuoteButton.displayName"
                                          modalTitleLabelKey="text.quotes.cancelQuoteButton.displayName"
                                          modalInputLabelKey="text.quotes.typeYourComment.label" />
        </div>
    </c:if>
</div>