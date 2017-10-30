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
angular.module('dragAndDropServiceModule', ['dragAndDropServiceInterfaceModule', 'gatewayProxyModule'])

.factory('dragAndDropService', function(DragAndDropServiceInterface, gatewayProxy, $compile, $rootScope, hitch, extend) {

    var DragAndDropService = function(gatewayId) {
        this.gatewayId = gatewayId;

        gatewayProxy.initForService(this, ["register"]);
    };

    DragAndDropService = extend(DragAndDropServiceInterface, DragAndDropService);

    return new DragAndDropService("dragAndDrop");
});
