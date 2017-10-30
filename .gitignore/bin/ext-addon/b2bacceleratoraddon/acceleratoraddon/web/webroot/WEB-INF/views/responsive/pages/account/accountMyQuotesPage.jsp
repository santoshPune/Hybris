<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags"%>

<spring:htmlEscape defaultHtmlEscape="true" />
<spring:url value="/my-account/my-quote/" var="orderQuoteLink" />

<c:set var="searchUrl" value="/my-account/my-quotes?sort=${ycommerce:encodeUrl(searchPageData.pagination.sort)}" />

<div class="account-section-header">
	<spring:theme code="text.account.quotes.myquotes" />
</div>

<c:if test="${empty searchPageData.results}">
	<div class="row">
		<div class="col-md-6 col-md-push-3">
			<div class="account-section-content	content-empty">
				<ycommerce:testId code="orderHistory_noOrders_label">
					<spring:theme code="text.account.quotes.noQuotes" />
				</ycommerce:testId>
			</div>
		</div>
	</div>
</c:if>

<c:if test="${not empty searchPageData.results}">
	<div class="account-section-content	">
		<div class="account-orderhistory">
			<div class="account-orderhistory-pagination">
				<nav:pagination top="true" msgKey="text.account.orderHistory.page" showCurrentPageInfo="true" hideRefineButton="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
			</div>

			<div class="responsive-table">
				<table class="responsive-table">
					<thead>
						<tr class="responsive-table-head hidden-xs">
							<th id="header1"><spring:theme code="text.account.quoteHistory.orderNumber" /></th>
							<th id="header2"><spring:theme code="text.account.quoteHistory.purchaseOrderNumber" /></th>
							<th id="header3"><spring:theme code="text.account.quoteHistory.orderStatus" /></th>
							<th id="header4"><spring:theme code="text.account.quoteHistory.accountManager" /></th>
							<th id="header5"><spring:theme code="text.account.quoteHistory.datePlaced" /></th>
							<th id="header6"><spring:theme code="text.account.order.total" /></th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${searchPageData.results}" var="order">
							<tr class="responsive-table-item">
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme code='text.account.quoteHistory.orderNumber'/></td>
								<td headers="header1" class="responsive-table-cell">
									<div class="row">
										<div class="col-xs-6">
											<ycommerce:testId code="quoteHistory_orderNumber_link">
												<a href="${orderQuoteLink}${ycommerce:encodeUrl(order.code)}"  class="responsive-table-link">${fn:escapeXml(order.code)}</a>
											</ycommerce:testId>
										</div>
									</div>
								</td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme code='text.account.quoteHistory.purchaseOrderNumber'/></td>
								<td headers="header2" class="responsive-table-cell">
									<ycommerce:testId code="quoteHistory_purchaseOrderNumber_label">				    
										${fn:escapeXml(order.purchaseOrderNumber)}
									</ycommerce:testId></td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme code='text.account.quoteHistory.orderStatus'/></td>
								<td headers="header3" class="status">
									<ycommerce:testId code="quoteHistory_orderStatus_label">
											<spring:theme code="text.account.order.status.display.${order.statusDisplay}" />
									</ycommerce:testId></td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme code='text.account.quoteHistory.accountManager'/></td>
								<td headers="header4" class="responsive-table-cell">
									<ycommerce:testId code="quoteHistory_accountManager_label">
										<c:forEach items="${order.managers}" var="manager">
											${fn:escapeXml(manager.name)}[${fn:escapeXml(manager.uid)}]<br />
										</c:forEach>
									</ycommerce:testId></td>
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme code='text.account.quoteHistory.datePlaced'/></td>
								<td headers="header5" class="responsive-table-cell">
									<fmt:formatDate value="${order.placed}" dateStyle="medium" timeStyle="short" type="both" />
								</td>							
								<td class="hidden-sm hidden-md hidden-lg"><spring:theme code="text.account.order.total" /></td>
								<td headers="header6" class="responsive-table-cell responsive-table-cell-bold">
									${fn:escapeXml(order.total.formattedValue)}
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<div class="account-orderhistory-pagination">
			<nav:pagination top="false" msgKey="text.account.orderHistory.page" showCurrentPageInfo="true" hideRefineButton="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
		</div>
	</div>
</c:if>