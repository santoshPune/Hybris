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
angular.module('InnerMocks', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])
    .run(function($httpBackend, languageService, I18N_RESOURCE_URI) {

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({});

        $httpBackend.whenGET(/cmswebservices\/v1\/languages/).respond({
            languages: [{
                language: 'en',
                required: true
            }, {
                language: 'pl',
                required: true
            }, {
                language: 'it'
            }]
        });

    });
