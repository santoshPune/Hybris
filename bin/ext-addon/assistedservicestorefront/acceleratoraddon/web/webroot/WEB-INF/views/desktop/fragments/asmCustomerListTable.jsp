<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="nav" tagdir="/WEB-INF/tags/responsive/nav" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>
<%@ taglib prefix="pagination" tagdir="/WEB-INF/tags/responsive/nav/pagination" %>

<c:set var="searchUrl" value="/assisted-service-querying/listCustomers?sort=${searchPageData.pagination.sort}&customerListUId=${defaultList}"/>
<c:set var="baseUrl" value="/assisted-service-querying/listCustomers"/>
<spring:url value="/assisted-service/emulate/?customerId=" var="emulateCustomerUrl"/>
<spring:url value="${baseUrl}" var="sortUrl" htmlEscape="true"><spring:param name="customerListUId" value="${defaultList}"/><spring:param name="sort" value=""/></spring:url>

<div class="account-section">
    <c:if test="${empty searchPageData || empty searchPageData.results}">
        <div class="account-section-content	col-md-6 col-md-push-3 content-empty">
            <ycommerce:testId code="orderHistory_noOrders_label">
                <spring:theme code="text.asm.customerList.noCustomers" />
            </ycommerce:testId>
        </div>
    </c:if>
    <c:if test="${not empty searchPageData.results}">
        <div class="account-section-content	">
            <div class="account-orderhistory">
                <div class="account-orderhistory-pagination js-customer-list-sorting" data-sort-url="${sortUrl}">
                    <nav:pagination top="true" msgKey="text.account.customerList.page" showCurrentPageInfo="true" hideRefineButton="true" supportShowPaged="${isShowPageAllowed}" supportShowAll="${isShowAllAllowed}" searchPageData="${searchPageData}" searchUrl="${searchUrl}"  numberPagesShown="${numberPagesShown}"/>
                </div>
                <div class="account-overview-table">
                    <table class="orderhistory-list-table responsive-table">
                        <tr class="account-orderhistory-table-head responsive-table-head hidden-xs">
                            <th><spring:theme code="text.asm.customerList.name" /></th>
                            <th><spring:theme code="text.asm.customerList.email" /></th>
                            <th><spring:theme code="text.asm.customerList.phone" /></th>
                            <th><spring:theme code="text.asm.customerList.cart" /></th>
                            <th><spring:theme code="text.asm.customerList.orders" /></th>
                        </tr>
                        <c:forEach items="${searchPageData.results}" var="customer">
                            <tr class="responsive-table-item">
                                <ycommerce:testId code="orderHistoryItem_orderDetails_link">
                                    <td class="responsive-table-cell">
                                        <a href="${emulateCustomerUrl}${customer.uid}" class="responsive-table-link">
                                            ${customer.name}
                                        </a>
                                    </td>
                                    <td class="status">
                                         ${customer.displayUid}
                                    </td>
                                    <td class="responsive-table-cell">
                                     <c:if test="${(not empty customer.defaultAddress) && (not empty customer.defaultAddress.phone)}">
								        ${customer.defaultAddress.phone}
								    </c:if>
                                    </td>
                                    <td class="responsive-table-cell responsive-table-cell-bold">
                                    <c:if test="${null != customer.latestCartId}">
                                    	<spring:url value="/assisted-service/emulate/" var="cartEmulationUrl" htmlEscape="true">
                                    		<spring:param name="customerId" value="${customer.uid}"/>
                                    		<spring:param name="cartId" value="${customer.latestCartId}"/>
                                    	</spring:url>
                                        <a href="${cartEmulationUrl}" class="responsive-table-link">
                                          <spring:theme code="text.asm.customerList.viewCarts" />
                                        </a>
                                     </c:if>
                                    </td>
                                    <td class="responsive-table-cell responsive-table-cell-bold">
                                         ----
                                    </td>
                                </ycommerce:testId>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
        </div>
    </c:if>
</div>