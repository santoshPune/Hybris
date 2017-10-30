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
 * @name perspectiveInterfaceModule
 */
angular.module('perspectiveServiceInterfaceModule', ['functionsModule'])
    //'decorator' wording to be replaced, constructor to be modified and ngdoc to be added once dependent code is refactored
    .constant('ALL_PERSPECTIVE', 'se.all')
    .constant('NONE_PERSPECTIVE', 'se.none')
    .factory('Perspective', function(copy) {
        var Perspective = function(name, features, system) {
            this.name = name;
            this.system = system === undefined ? false : system;
            this.features = features;
            this.setFeatures = function(features) {
                this.features = copy(features);
            };
            this.getFeatures = function() {
                return this.features;
            };
        };

        Perspective.prototype.clone = function() {
            var featureClone = [];
            var thisFeatures = this.getFeatures();
            for (var pk in thisFeatures) {
                featureClone.push(thisFeatures[pk]);
            }
            return new Perspective(this.name, featureClone, this.system);
        };

        return Perspective;
    })
    /**
     * @ngdoc service
     * @name perspectiveInterfaceModule.service:PerspectiveServiceInterface
     *
     */
    .factory('PerspectiveServiceInterface', function() {


        function PerspectiveServiceInterface() {}

        /**
         * @ngdoc method
         * @name perspectiveInterfaceModule.service:PerspectiveServiceInterface#register
         * @methodOf perspectiveInterfaceModule.service:PerspectiveServiceInterface
         *
         * @description
         * This method registers a perspective.
         * When an end user selects a perspective in the SmartEdit web application,
         * all features bound to the perspective will be enabled when their respective enablingCallback functions are invoked
         * and all features not bound to the perspective will be disabled when their respective disablingCallback functions are invoked.
         * 
         * @param {Object} configuration The configuration that represents the feature to be registered.
         * @param {String} configuration.key The key that uniquely identifies the perspective in the registry.
         * @param {String} configuration.nameI18nKey The i18n key that stores the perspective name to be translated.
         * @param {String} configuration.descriptionI18nKey The i18n key that stores the perspective description to be translated. The description is used as a tooltip in the web application. This is an optional parameter.
         * @param {Array}  configuration.features A list of features to be bound to the perspective.
         * @param {Array}  configuration.perspectives A list of referenced perspectives to be bound to this system perspective. This list is optional.
         */
        PerspectiveServiceInterface.prototype.register = function(configuration) {};

        /**
         * @ngdoc method
         * @name perspectiveInterfaceModule.service:PerspectiveServiceInterface#switchTo
         * @methodOf perspectiveInterfaceModule.service:PerspectiveServiceInterface
         *
         * @description
         * This method actives a perspective identified by its key and deactivates the currently active perspective.
         * Activating a perspective consists in activating any feature that is bound to the perspective
         * or any feature that is bound to the perspective's referenced perspectives and deactivating any features
         * that are not bound to the perspective or to its referenced perspectives.
         *
         * @param {String} key The key that uniquely identifies the perspective to be activated. This is the same key as the key used in the {@link perspectiveInterfaceModule.service:PerspectiveServiceInterface#methods_register register} method.
         */
        PerspectiveServiceInterface.prototype.switchTo = function(key) {};

        /**
         * @ngdoc method
         * @name perspectiveInterfaceModule.service:PerspectiveServiceInterface#hasActivePerspective
         * @methodOf perspectiveInterfaceModule.service:PerspectiveServiceInterface
         *
         * @description
         * 	This method returns true if a perspective is selected.
         *
         * @returns {Boolean} The key of the active perspective.
         */
        PerspectiveServiceInterface.prototype.hasActivePerspective = function() {};


        /**
         * @ngdoc method
         * @name perspectiveInterfaceModule.service:PerspectiveServiceInterface#selectDefault
         * @methodOf perspectiveInterfaceModule.service:PerspectiveServiceInterface
         *
         * @description
         * This method switches the currently-selected perspective to the default perspective.
         * If no value has been stored in the smartedit-perspectives cookie, the value of the default perspective is se.none.
         * If a value is stored in the cookie, that value is used as the default perspective.
         *
         */
        PerspectiveServiceInterface.prototype.selectDefault = function() {};

        /**
         * @ngdoc method
         * @name perspectiveInterfaceModule.service:PerspectiveServiceInterface#isEmptyPerspectiveActive
         * @methodOf perspectiveInterfaceModule.service:PerspectiveServiceInterface
         *
         * @description
         * This method returns true if the current active perspective is the Preview mode (No active overlay).
         *
         * @returns {Boolean} Flag that indicates if the current perspective is the Preview mode.
         */
        PerspectiveServiceInterface.prototype.isEmptyPerspectiveActive = function() {};

        return PerspectiveServiceInterface;

    });
