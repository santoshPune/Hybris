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
angular.module('otherApp', ['perspectiveServiceModule'])
    .run(function(perspectiveService) {
        perspectiveService.register({
            key: 'someperspective',
            nameI18nKey: 'someperspective',
            descriptionI18nKey: 'someperspective',
            features: ['se.contextualMenu'],
            perspectives: []
        });
    });
