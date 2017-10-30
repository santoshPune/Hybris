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
/**
 * @ngdoc overview
 * @name alertServiceModule
 * @description
 * <h1>The Alert service module</h1>
 * The alert service module provides a service to bind alerts to the $rootScope
 *
 */
angular.module('alertServiceModule', ['gatewayProxyModule', 'functionsModule'])

.factory('alertService', function(gatewayProxy, $rootScope, hitch) {

    /**
     * @ngdoc service
     * @name alertServiceModule.alertService
     * @description
     * The alert service provides a simple wrapper to bind alerts to the $rootScope
     *
     */
    var AlertService = function(gatewayId) {

        this.gatewayId = gatewayId;
        gatewayProxy.initForService(this);
        this.field = 'alerts';

    };

    /**
     * @ngdoc method
     * @name alertServiceModule.alertService.pushAlerts
     * @methodOf alertServiceModule.alertService
     * @description
     * Binds alerts to the $rootScope
     *
     * The below is the sample code snippet for the alert object:
     * <pre>
     * {
     * successful : true,
     * message : 'this is a successful alert',
     * closable : false
     * }
     * </pre>
     *
     * Example of use:
     * <pre>
     * alertService.pushAlerts(alerts);
     * </pre>
     *
     * @param {Object}  alerts an array of objects containing information related to each alert
     */
    AlertService.prototype.pushAlerts = function(alerts) {

        if (!$rootScope.$$phase) {
            $rootScope.$apply(hitch(this, function() { //necessary when called from jQuery
                $rootScope[this.field] = alerts;
            }));
        } else {
            $rootScope[this.field] = alerts;
        }

        if (!(alerts.length === 1 && alerts[0].closeable === false)) {
            setTimeout(hitch(this, function() {
                delete $rootScope[this.field];
                $rootScope.$digest();
            }), 3000);
        }

    };

    AlertService.prototype.removeAlertById = function(id) {
        $rootScope[this.field] = ($rootScope[this.field] || []).filter(function(alert) {
            return !alert.id || alert.id !== id;
        });
    };

    return new AlertService('alertService');

});
