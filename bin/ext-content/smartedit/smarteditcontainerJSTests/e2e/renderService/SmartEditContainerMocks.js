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
angular.module('SmartEditContainerMocksModule', [])
    .constant('SMARTEDIT_ROOT', 'web/webroot')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/smarteditcontainerJSTests/)
    .value('CONFIGURATION_MOCK', [{
        "id": "1",
        "key": "i18nAPIRoot",
        "value": "\"somepath\""
    }, {
        "id": "2",
        "key": "applications.RenderDecoratorsModule",
        "value": "{\"smartEditLocation\":\"/smarteditcontainerJSTests/e2e/utils/decorators/RenderDecorators.js\"}"
    }, {
        "id": "3",
        "key": "applications.i18nMockModule",
        "value": "{\"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/i18nMock.js\"}"
    }, {
        "id": "3",
        "key": "applications.OthersMockModule",
        "value": "{\"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/OthersMock.js\"}"
    }]);

angular.module('smarteditloader').requires.push('SmartEditContainerMocksModule');
angular.module('smarteditcontainer').requires.push('SmartEditContainerMocksModule');
