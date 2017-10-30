<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="template" tagdir="/WEB-INF/tags/responsive/template"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="formElement" tagdir="/WEB-INF/tags/responsive/formElement" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<spring:url value="/mobile/unbind" var="action" />
<spring:url value="/verification-code/create-code" var="getVerificationCodeUrl" />

<template:page pageTitle="${pageTitle}">

	<div class="row">
		<div class="col-md-6">
			<div class="yCmsContentSlot login-left-content-slot">
				<div class="yCmsComponent login-left-content-component">
					<div class="login-section">
						<div class="headline">
							<spring:theme code="mobile.unbind.headline.text" text="Unbind your mobile number"/>
						</div>
						<p>
							<spring:theme code="mobile.verify.receive.help.text" text="If your mobile cannot receive message, please contact customer services." />
						</p>
						<form:form method="post" commandName="verificationCodeForm">
							<input type="hidden" id="after-send-btn-text" value="<spring:theme code='profile.register.btn.sent.text'/>"/>
							<input type="hidden" name="codeType" value="unbinding"/>
							
							<div class="form-group">
								<label class="control-label " for="mobileNumber">
									<spring:theme code="mobile.bound.text" text="Bound Mobile Number"/>
								</label>
								<input readonly="readonly" name="mobileNumber" class=" form-control" type="text" value="${verificationCodeForm.mobileNumber}">
							</div>
							 	
							<formElement:formInputBox idKey="verificationCode" labelKey="profile.register.verificationCode" path="verificationCode" placeholder="profile.register.verificationCode" />
							
							<div class="row">
		                        <div class="col-sm-6 col-sm-push-6 binding-btn">
		                            <div class="accountActions">
	                                    <button type="submit" class="btn btn-primary btn-block">
											<spring:theme code="mobile.verify.submit" text="Submit"/>
										</button>
		                            </div>
		                        </div>
		                        <div class="col-sm-6 col-sm-pull-6 binding-btn">
		                            <div class="accountActions">
	                                    <button id="unbind-code-btn" type="button" data-url="${getVerificationCodeUrl}" class="btn btn-default btn-block">
											<spring:theme code="profile.register.btn.send.text"/>
										</button>
		                            </div>
		                        </div>
		                    </div>
						</form:form>
					</div>
				</div>
			</div>
		</div>
	</div>
</template:page>
