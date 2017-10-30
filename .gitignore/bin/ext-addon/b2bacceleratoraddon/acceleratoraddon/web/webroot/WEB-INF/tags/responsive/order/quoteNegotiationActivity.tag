<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ attribute name="orderHistoryEntries" required="true" type="java.util.List"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<spring:htmlEscape defaultHtmlEscape="true" />

<div class="order-approval-list">
	<table class="orderListTable responsive-table">
		<thead>
			<tr class="responsive-table-head hidden-xs">
				<th><spring:theme code="text.quote.orderHistoryEntry.date" /></th>
				<th><spring:theme code="text.quote.orderHistoryEntry.status" /></th>
				<th><spring:theme code="text.quote.orderHistoryEntry.user" /></th>
				<th><spring:theme code="text.quote.orderHistoryEntry.price" /></th>
				<th><spring:theme code="text.quote.orderHistoryEntry.comment" /></th>
				<th><spring:theme code="text.quote.orderHistoryEntry.quoteExpirationDate" /></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${orderHistoryEntries}" var="orderHistoryEntryData">
				<tr class="responsive-table-item">
					<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.quote.orderHistoryEntry.date" /></td>
					<td class="responsive-table-cell">${fn:escapeXml(orderHistoryEntryData.timeStamp)}</td>

					<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.quote.orderHistoryEntry.status" /></td>
					<td class="responsive-table-cell"><spring:theme code="text.account.order.status.display.${orderHistoryEntryData.previousOrderVersionData.statusDisplay}"/></td>

					<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.quote.orderHistoryEntry.user" /></td>
					<td class="responsive-table-cell">${fn:escapeXml(orderHistoryEntryData.ownerData.name)}</td>

					<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.quote.orderHistoryEntry.price" /></td>
					<td class="responsive-table-cell">${fn:escapeXml(orderHistoryEntryData.previousOrderVersionData.totalPrice.formattedValue)}</td>

					<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.quote.orderHistoryEntry.comment" /></td>
					<td class="responsive-table-cell">${fn:escapeXml(orderHistoryEntryData.previousOrderVersionData.b2BComment.comment)}</td>

					<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.quote.orderHistoryEntry.quoteExpirationDate" /></td>
					<td class="responsive-table-cell">${fn:escapeXml(orderHistoryEntryData.previousOrderVersionData.quoteExpirationDate)}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
</div>