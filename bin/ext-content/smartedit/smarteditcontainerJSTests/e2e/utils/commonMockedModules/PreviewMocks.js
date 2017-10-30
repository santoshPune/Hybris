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
angular.module('PreviewMocksModule', ['ngMockE2E', 'functionsModule'])
    .run(function($httpBackend, PREVIEW_RESOURCE_URI, resourceLocationToRegex) {
        $httpBackend.whenPOST(resourceLocationToRegex(PREVIEW_RESOURCE_URI)).respond({
            ticketId: 'dasdfasdfasdfa',
            resourcePath: document.location.origin + '/smarteditcontainerJSTests/e2e/dummystorefront.html'
        });
    });

angular.module('smarteditloader').requires.push('PreviewMocksModule');
angular.module('smarteditcontainer').requires.push('PreviewMocksModule');
