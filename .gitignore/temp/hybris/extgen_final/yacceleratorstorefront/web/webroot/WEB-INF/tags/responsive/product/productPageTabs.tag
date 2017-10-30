<%@ tag body-content="empty" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="product" tagdir="/WEB-INF/tags/responsive/product"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="cms" uri="http://hybris.com/tld/cmstags"%>

<div class="tabs js-tabs tabs-responsive">
	<div class="tabhead">
		<a href=""><spring:theme code="product.product.details" /></a> <span
			class="glyphicon"></span>
	</div>
	<div class="tabbody">
		<div class="container-lg">
			<div class="row">
				<div class="col-md-6 col-lg-4">
					<div class="tab-container">
						<product:productDetailsTab product="${product}" />
					</div>
				</div>
			</div>
		</div>
	</div>

	<div class="tabhead">
		<a href=""><spring:theme code="product.product.spec" /></a> <span
			class="glyphicon"></span>
	</div>
	<div class="tabbody">
		<div class="container-lg">
			<div class="row">
				<div class="col-md-6 col-lg-4">
					<div class="tab-container">
						<product:productDetailsClassifications product="${product}" />
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="tabreview" class="tabhead">
		<a href=""><spring:theme code="review.reviews" /></a> <span
			class="glyphicon"></span>
	</div>
	<div class="tabbody">
		<div class="container-lg">
			<div class="row">
				<div class=" col-md-6 col-lg-4">
					<div class="tab-container">
						<product:productPageReviewsTab product="${product}" />
					</div>
				</div>
			</div>
		</div>
	</div>

	<cms:pageSlot position="Tabs" var="tabs">
		<cms:component component="${tabs}" />
	</cms:pageSlot>

</div>