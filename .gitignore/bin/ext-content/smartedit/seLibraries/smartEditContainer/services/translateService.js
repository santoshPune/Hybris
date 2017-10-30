/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */
/**
 * @ngdoc service
 * @name translationServiceModule
 *
 * @description
 * 
 * This module is used to configure the translate service, the filter, and the directives from the 'pascalprecht.translate' package. The configuration consists of:
 * 
 * <br/>- Initializing the translation map from the {@link i18nInterceptorModule.object:I18NAPIROOT I18NAPIROOT} constant.
 * <br/>- Setting the preferredLanguage to the {@link i18nInterceptorModule.object:UNDEFINED_LOCALE UNDEFINED_LOCALE} so that the {@link i18nInterceptorModule.service:i18nInterceptor#methods_request i18nInterceptor request} can replace it with the appropriate URI combined with the runtime browser locale retrieved from the {@link languageServiceModule.service:languageService#methods_getBrowserLocale languageService.getBrowserLocale}, which is unaccessible at configuration time.
 */
angular.module('translationServiceModule', ['pascalprecht.translate', 'i18nInterceptorModule'])
    .config(function($translateProvider, I18NAPIROOT, UNDEFINED_LOCALE) {

        /*
         * hard coded url that is always intercepted by i18nInterceptor so as to replace by value from configuration REST call
         */
        $translateProvider.useStaticFilesLoader({
            prefix: '/' + I18NAPIROOT + '/',
            suffix: ''
        });

        // Tell the module what language to use by default
        $translateProvider.preferredLanguage(UNDEFINED_LOCALE);

    });
