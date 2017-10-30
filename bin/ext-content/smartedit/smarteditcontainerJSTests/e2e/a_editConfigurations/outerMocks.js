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
angular.module('OuterMocksModule', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule', 'functionsModule'])
    .constant('SMARTEDIT_ROOT', 'web/webroot')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smarteditcontainerJSTests/)
    .value('CONFIGURATION_AUTHORIZED', true)
    .value('CONFIGURATION_MOCK', [{
        "id": "8796093091245",
        "value": "\"thepreviewTicketURI\"",
        "key": "previewTicketURI"
    }, {
        "id": "432534534645",
        "value": "{malformed}",
        "key": "i18nAPIRoot"
    }, {
        "id": "446547578787",
        "key": "applications.i18nMockModule",
        "value": "{\"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/i18nMock.js\"}"
    }]);

try {
    angular.module('smarteditloader').requires.push('OuterMocksModule');
    angular.module('smarteditcontainer').requires.push('OuterMocksModule');
} catch (ex) {}
