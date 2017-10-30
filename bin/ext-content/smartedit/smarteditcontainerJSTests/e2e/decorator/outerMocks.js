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
angular.module('OuterMocksModule', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])
    .constant('SMARTEDIT_ROOT', 'web/webroot')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smarteditcontainerJSTests/)
    .value('CONFIGURATION_MOCK', [{
        "id": "2",
        "value": "\"thepreviewTicketURI\"",
        "key": "previewTicketURI"
    }, {
        "id": "8",
        "value": "\"somepath\"",
        "key": "i18nAPIRoot"
    }, {
        "id": "9",
        "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/i18nMock.js\"}",
        "key": "applications.i18nMockModule"
    }, {
        "id": "3",
        "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/OthersMock.js\"}",
        "key": "applications.OthersMockModule"
    }, {
        "id": "7",
        "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/utils/decorators/DummyDecorators.js\"}",
        "key": "applications.DummyDecoratorsModule"
    }]);

try {
    angular.module('smarteditloader').requires.push('OuterMocksModule');
    angular.module('smarteditcontainer').requires.push('OuterMocksModule');
} catch (ex) {}
