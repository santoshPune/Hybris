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
angular.module('i18nMockModule', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])
    .run(function($httpBackend, I18N_RESOURCE_URI, languageService) {

        $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({
            'landingpage.title': 'Your Touchpoints',
            'cataloginfo.pagelist': 'PAGE LIST',
            'cataloginfo.lastsynced': 'LAST SYNCED',
            'cataloginfo.button.sync': 'SYNC',
            'se.cms.perspective.basic.name': 'Basic CMS',
            'se.cms.perspective.advanced.name': 'Advanced CMS',
            'slot.shared.popover.message': 'This slot is shared, any changes you make will affect other pages using the same slot.'
        });
    });

try {
    angular.module('smarteditloader').requires.push('i18nMockModule');
    angular.module('smarteditcontainer').requires.push('i18nMockModule');
} catch (ex) {}
