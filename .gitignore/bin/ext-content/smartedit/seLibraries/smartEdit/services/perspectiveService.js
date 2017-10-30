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
angular.module('perspectiveServiceModule', ['functionsModule', 'perspectiveServiceInterfaceModule', 'gatewayProxyModule'])

.factory('perspectiveService', function(extend, PerspectiveServiceInterface, gatewayProxy) {

    var PerspectiveService = function() {
        this.gatewayId = "perspectiveService";
        gatewayProxy.initForService(this);
    };

    PerspectiveService = extend(PerspectiveServiceInterface, PerspectiveService);

    return new PerspectiveService();
});
