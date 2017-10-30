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
angular.module('decoratorServiceModule', ['functionsModule'])
    .factory('DecoratorServiceClass', function($q, uniqueArray, hitch, regExpFactory) {

        // Constants

        var DecoratorServiceClass = function() {
            this._activeDecorators = [];
            this.componentDecoratorsMap = {};
        };

        /**
         * @ngdoc method
         * @name decoratorServiceModule.service:decoratorService#addMappings
         * @methodOf decoratorServiceModule.service:decoratorService
         * @description
         * This method enables a list of decorators for a group of component types.
         * The list to be {@link decoratorServiceModule.service:decoratorService#methods_enable enable} is identified by a matching pattern.
         * The list is enabled when a perspective or referenced perspective that it is bound to is activated/enabled.
         * @param {Object} map A key-map value; the key is the matching pattern and the value is an array of decorator keys. The key can be an exact type, an ant-like wild card, or a full regular expression:
         * <pre>
         * decoratorService.addMappings({
            '*Suffix': ['decorator1', 'decorator2'],
            '.*Suffix': ['decorator2', 'decorator3'],
            'MyExactType': ['decorator3', 'decorator4'],
            '^((?!Middle).)*$': ['decorator4', 'decorator5']
        	});
         * </pre>
         */
        DecoratorServiceClass.prototype.addMappings = function(map) {

            for (var regexpKey in map) {
                var decoratorsArray = map[regexpKey];
                this.componentDecoratorsMap[regexpKey] = uniqueArray((this.componentDecoratorsMap[regexpKey] || []), decoratorsArray);
            }

        };

        /**
         * @ngdoc method
         * @name decoratorServiceModule.service:decoratorService#enable
         * @methodOf decoratorServiceModule.service:decoratorService
         * @description
         * Enables a decorator
         * 
         * @param {String} decoratorKey The key that uniquely identifies the decorator.
         */
        DecoratorServiceClass.prototype.enable = function(decoratorKey) {
            if (this._activeDecorators.indexOf(decoratorKey) == -1) {
                this._activeDecorators.push(decoratorKey);
            }
        };

        /**
         * @ngdoc method
         * @name decoratorServiceModule.service:decoratorService#disable
         * @methodOf decoratorServiceModule.service:decoratorService
         * @description
         * Disables a decorator
         * 
         * @param {String} decoratorKey the decorator key
         */
        DecoratorServiceClass.prototype.disable = function(decoratorKey) {
            var index = this._activeDecorators.indexOf(decoratorKey);
            if (index > -1) {
                this._activeDecorators.splice(index, 1);
            }
        };

        /**
         * @ngdoc method
         * @name decoratorServiceModule.service:decoratorService#getDecoratorsForComponent
         * @methodOf decoratorServiceModule.service:decoratorService
         * @description
         * This method retrieves a list of decorator keys that is eligible for the specified component type.
         * The list retrieved depends on which perspective is active.
         *
         * This method uses the list of decorators enabled by the {@link decoratorServiceModule.service:decoratorService#methods_addMappings addMappings} method.
         *
         * @param {String} componentType The component type to be decorated.
         * @returns {Promise} It returns a list of decorator keys.
         *
         */
        DecoratorServiceClass.prototype.getDecoratorsForComponent = function(componentType) {
            return this._getDecorators(componentType);
        };

        DecoratorServiceClass.prototype._getDecorators = function(componentType) {
            var decoratorArray = [];
            if (this.componentDecoratorsMap) {
                for (var regexpKey in this.componentDecoratorsMap) {
                    if (regExpFactory(regexpKey).test(componentType)) {
                        decoratorArray = uniqueArray(decoratorArray, this.componentDecoratorsMap[regexpKey]);
                    }
                }
            }

            return decoratorArray.filter(hitch(this, function(dec) {
                return this._activeDecorators.indexOf(dec) > -1;
            }));
        };

        return DecoratorServiceClass;
    })
    /**
     * @ngdoc service
     * @name decoratorServiceModule.service:decoratorService
     *
     * @description
     * This service enables and disables decorators. It also maps decorators to SmartEdit component types–regardless if they are enabled or disabled.
     * 
     */
    .factory('decoratorService', function(DecoratorServiceClass) {
        return new DecoratorServiceClass();
    });
