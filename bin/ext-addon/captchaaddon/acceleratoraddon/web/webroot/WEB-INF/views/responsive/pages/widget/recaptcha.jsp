<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="theme" tagdir="/WEB-INF/tags/shared/theme" %>
<%@ taglib prefix="ycommerce" uri="http://hybris.com/tld/ycommercetags" %>

<c:if test="${requestScope.captcaEnabledForCurrentStore}">
	<div id="recaptcha_widget" style="display:none"  data-recaptcha-public-key="${requestScope.recaptchaPublicKey}">

		<div id="recaptcha_image" class="left"></div>
		<div class="left recaptcha_icons">
			<a href="javascript:Recaptcha.reload()" class="cicon glyphicon glyphicon-repeat"></a>
			<div class="recaptcha_only_if_image"><a href="javascript:Recaptcha.switch_type('audio')" class="cicon glyphicon glyphicon-volume-up"></a></div>
			<div class="recaptcha_only_if_audio"><a href="javascript:Recaptcha.switch_type('image')" class="cicon glyphicon glyphicon-picture"></a></div>
		</div>

		<div class="recaptcha_only_if_incorrect_sol" style="color:red"><spring:theme code="recaptch.incorrect.text"/></div>

		<div class="control-group clear clearfix">

			<label class="control-label " for="recaptcha_response_field">
				<span class="recaptcha_only_if_image"><spring:theme code="recaptch.enterwordsabove.text"/></span>
				<span class="recaptcha_only_if_audio"><spring:theme code="recaptch.enternumbersyouhear.text"/></span>
			</label>
			<div class="controls">
                <div class="input-group form-group">
                    <input type="text" id="recaptcha_response_field" name="recaptcha_response_field" class="left form-control" />
                    <a href="javascript:Recaptcha.showhelp()" class="input-group-addon glyphicon glyphicon-info-sign recaptcha-help-icon" ></a>
                </div>

			</div>
		</div>

	</div>
</c:if>
