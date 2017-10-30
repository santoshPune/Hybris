<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<style type="text/css">div.nav-bottom{display:none}</style>
<div id="vertical_navigation_bar">
	<ul id="banner_menu_wrap">
		<c:forEach var="component" items="${components}">
			<li>
				<a href="<c:url value='${component.link.url}'/>">
					<c:out value="${component.link.linkName}" />
				</a>
				<c:if test="${not empty component.link.navigationNodes}">
					<div class="banner_menu_content">
						<ul>
							<c:forEach var="link" items="${component.link.navigationNodes}">
								<c:if test="${!fn:startsWith(link.uid, 'Mobile')}">
									<c:forEach var="child" items="${link.children}">
										<c:forEach var="childlink" items="${child.links}">
											<c:set var="isThumbnailOnly" value="${childlink.thumbnailOnly}" />
											<li <c:if test="${isThumbnailOnly}">class="thumbnail_only"</c:if>>
												<a href="<c:url value='${childlink.url}'/>" title="${childlink.linkName}">
													<img alt="${childlink.linkName}" src="${childlink.thumbnail.downloadURL}" />
													<c:if test="${not isThumbnailOnly}">
														<c:out value="${childlink.linkName}" />
													</c:if>
												</a>
											</li>
										</c:forEach>
									</c:forEach>
								</c:if>
							</c:forEach>
						</ul>
					</div>
				</c:if>
			</li>
		</c:forEach>
	</ul>
</div>