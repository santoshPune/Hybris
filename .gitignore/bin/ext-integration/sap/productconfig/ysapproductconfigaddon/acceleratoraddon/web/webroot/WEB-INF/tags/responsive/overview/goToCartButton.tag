<%@ tag language="java" pageEncoding="ISO-8859-1" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<spring:message code="sapproductconfig.addtocart.tooltip.anotherSaveValue" text="If selected, a duplicate of this configuration will be added to the Shopping Cart and you will continue with the choices you have already made intact" var="anotherSaveValueTooltip"/>

<button type="submit" id="cpqGoToCartBtn"
	class="cpq-btn-goToCart">
	<spring:theme code="cart.checkout" />
</button>
<div id="cpq-copyvalues-facet" class="cpq-copyvalues-facet cpq-js-overview-facet product__facet">
	<div class="facet js-facet">
		<div class="facet__values js-facet__values js-facet__form">
			<ul class="facet__list js-facet__list js-facet__top-values">
				<li>
					<label title="${anotherSaveValueTooltip}">
						<input id="copyAndRedirectCheckBox" name="copyAndRedirectCheckBox.selected" class="facet__checkbox sr-only" type="checkbox" value="true">
						<span class="facet__list__label">
							<span class="facet__list__mark"></span>
							<span class="facet__list__text">
								<spring:message code="sapproductconfig.addtocart.anotherSaveValue" text="Duplicate With Saved Values" />
							</span>
						</span>
					</label>
				</li>
			</ul>
		</div>
	</div>
</div>

