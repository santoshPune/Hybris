/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
ACCMOB.captcha = {

	bindAll: function ()
	{
		this.renderWidget();
	},

	renderWidget: function ()
	{
		$.ajax({
			url: ACCMOB.config.encodedContextPath + "/register/captcha/widget/recaptcha",
			type: 'GET',
			success: function (html)
			{
				if ($(html) != [])
				{
					$('ul#registerFormList.mFormList li:last').before($(html));
					$('ul#registerFormList.mFormList').find('div[data-role="fieldcontain"]').fieldcontain();
					$("#recaptcha_response_field").textinput();

					if ($('#recaptchaChallangeAnswered').val() == 'false')
					{
						$('#captcha_error').addClass('form_field_error');
					}

					ACCMOB.captcha.bindCaptcha();
				}
			}
		});
	},
	renderWidgetForUpdateEmailPage: function ()
	{
		$.ajax({
			url: ACCMOB.config.encodedContextPath + "/register/captcha/widget/recaptcha",
			type: 'GET',
			success: function (html)
			{
				if ($(html) != [])
				{
					$('ul#updateEmailFormList.mFormList li:last').before($(html));
					$('ul#updateEmailFormList.mFormList').find('div[data-role="fieldcontain"]').fieldcontain();
					$("#recaptcha_response_field").textinput();

					if ($('#recaptchaChallangeAnswered').val() == 'false')
					{
						$('#captcha_error').addClass('form_field_error');
					}

					ACCMOB.captcha.bindCaptcha();
				}
			}
		});
	},
	bindCaptcha: function ()
	{
		$.getScript('https://www.google.com/recaptcha/api/js/recaptcha_ajax.js', function ()
		{
			var publicKey = $('#recaptcha_widget').data('recaptchaPublicKey');
			if (!(typeof publicKey === 'undefined'))
			{
				Recaptcha.create(publicKey, "recaptcha_widget", {
					theme: "custom",
					lang: ACCMOB.config.language
				});
			}
		});
	}
}
$(document).ready(function ()
{
	if ($('#registerForm').html() != null)
	{
		ACCMOB.captcha.bindAll();
	}
	
	if ($('#updateEmailForm').html() != null)
	{
		ACCMOB.captcha.renderWidgetForUpdateEmailPage();
	}
});
