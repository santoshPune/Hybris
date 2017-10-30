<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="orderData" required="true" type="de.hybris.platform.commercefacades.order.data.OrderData"%>
<%@ attribute name="orderHistoryEntries" required="true" type="java.util.List"%>
<%@ attribute name="quoteOrderDecisionURL" required="true" type="java.lang.String"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="order" tagdir="/WEB-INF/tags/addons/b2bacceleratoraddon/responsive/order" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<order:quoteNegotiationActivity orderHistoryEntries="${orderHistoryEntries}"/>

<div>
	<div class="quote-header">
		<div class="quote-comments-headline">
			<spring:theme code="text.quote.comments.label" />
		</div>
		<div class="quote-header-link">
			<c:if test="${orderData.status=='APPROVED_QUOTE' || orderData.status=='REJECTED_QUOTE' || orderData.status=='PENDING_QUOTE'}">
				<order:quoteOrderDecisionForm quoteOrderDecisionURL="${quoteOrderDecisionURL}" orderCode="${orderData.code}"
											  selectedQuoteDecision="ADDADDITIONALCOMMENT" displayAsLink="true"
											  decisionLabelKey="text.quotes.addAdditionalComment.displayName"
											  modalTitleLabelKey="text.quotes.addAdditionalComment.displayName"
											  mobileText="text.quotes.addAdditionalComment.displayNameMobile"
											  modalInputLabelKey="text.quotes.typeYourComment.label" modalCommentRequired="true" />
			</c:if>
		</div>
	</div>
</div>

<div class="order-approval-list quote-comments-details">
	<div class="account-orderdetail well well-tertiary">
		<div class="well-headline">
			<spring:theme code="text.quote.commentsDetails.label"	/>
		</div>
	</div>
	<table class="orderListTable responsive-table quote-approval-table">
		<thead>
			<tr class="responsive-table-head hidden-xs">
				<th><spring:theme code="text.quote.orderHistoryEntry.date" /></th>
				<th><spring:theme code="text.quote.orderHistoryEntry.user" /></th>
				<th><spring:theme code="text.quote.orderHistoryEntry.comment" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${orderData.b2bCommentData}" var="b2bComment">
				<tr class="responsive-table-item">
					<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.quote.orderHistoryEntry.date" /></td>
					<td class="responsive-table-cell">${fn:escapeXml(b2bComment.timeStamp)}</td>

					<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.quote.orderHistoryEntry.user" /></td>
					<td class="responsive-table-cell">${fn:escapeXml(b2bComment.ownerData.name)}</td>

					<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.quote.orderHistoryEntry.comment" /></td>
					<td class="responsive-table-cell">${fn:escapeXml(b2bComment.comment)}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>