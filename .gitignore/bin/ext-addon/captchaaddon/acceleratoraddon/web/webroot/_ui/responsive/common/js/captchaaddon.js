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
ACC.captcha = {


	bindAll: function ()
	{
		this.renderWidget();
	},

	renderWidget: function ()
	{
		$.ajax({
			url: ACC.config.encodedContextPath + "/register/captcha/widget/recaptcha",
			type: 'GET',
			cache: false,
			success: function (html)
			{
				if ($(html) != [])
				{
					$(html).appendTo('.js-recaptcha-captchaaddon');
					if ($('#recaptchaChallangeAnswered').val() == 'false')
					{
						$('.control-group.clear.clearfix').addClass('error');
					}

					ACC.captcha.bindCaptcha();

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
					lang: ACC.config.language
				});
			}
		});
	}
}

$(document).ready(function ()
{
	if ($('#registerForm').html() != null || $('#updateEmailForm').html() != null)
	{
		ACC.captcha.bindAll();
	}
});
