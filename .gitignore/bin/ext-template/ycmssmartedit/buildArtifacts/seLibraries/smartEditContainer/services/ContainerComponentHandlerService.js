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
 * @name containerComponentHandlerServiceModule
 * @description
 * 
 * this module aims at handling smartEdit container components both on the original storefront and the smartEdit overlay
 * 
 */
angular.module('containerComponentHandlerServiceModule', [])
    .factory(
        'containerComponentHandlerService',
        function() {
            return {
                /**
                 * @ngdoc method
                 * @name componentHandlerServiceModule.componentHandlerService#methodsOf_getFromSelector
                 * @methodOf componentHandlerServiceModule.componentHandlerService
                 *
                 * @description
                 * This is a wrapper around jQuery selector
                 *
                 * @param {String} selector String selector as per jQuery API
                 * 
                 * @return {Object} a jQuery object for the given selector
                 */
                getFromSelector: function(selector) {
                    return $(selector);
                }
            };
        });
