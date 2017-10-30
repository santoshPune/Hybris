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
angular
    .module('LoadingTemplates', ['catalogDetailsModule'])
    .run(function(catalogDetailsService) {
        catalogDetailsService.addItems([{
            include: '../../smarteditcontainerJSTests/e2e/catalogDetails/templateOne.html'
        }]);
        catalogDetailsService.addItems([{
            include: '../../smarteditcontainerJSTests/e2e/catalogDetails/templateTwo.html'
        }]);
    });
angular.module('smarteditcontainer').requires.push('LoadingTemplates');
