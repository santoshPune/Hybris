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
angular.module('perspectiveServiceModule', ['featureServiceModule', 'functionsModule', 'perspectiveServiceInterfaceModule', 'gatewayProxyModule', 'iFrameManagerModule', 'storageServiceModule', 'eventServiceModule'])
    /**
     * @ngdoc service
     * @name perspectiveInterfaceModule.service:renderGateway
     * @description
     * Instance of the {@link gatewayFactoryModule.MessageGateway MessageGateway} dealing with rendering related events
     */
    .factory('renderGateway', function(gatewayFactory) {
        return gatewayFactory.createGateway("render");
    })
    .factory('perspectiveService', function($log, $rootScope, $q, extend, isBlank, uniqueArray, hitch, systemEventService, Perspective, PerspectiveServiceInterface, featureService, gatewayProxy, renderGateway, iFrameManager, storageService, NONE_PERSPECTIVE, ALL_PERSPECTIVE, EVENTS) {

        // Constants
        var PERSPECTIVE_COOKIE_NAME = "smartedit-perspectives";

        var PerspectiveService = function() {
            this.perspectives = [];

            this.data = {
                activePerspective: undefined,
                previousPerspective: undefined
            };
            this.gatewayId = "perspectiveService";
            gatewayProxy.initForService(this, ['register', 'switchTo', 'hasActivePerspective', 'isEmptyPerspectiveActive', 'selectDefault']);

            this._addDefaultPerspectives();
            this._registerEventHandlers();
        };

        PerspectiveService = extend(PerspectiveServiceInterface, PerspectiveService);

        PerspectiveService.prototype._addDefaultPerspectives = function() {
            this.register({
                key: NONE_PERSPECTIVE,
                nameI18nKey: 'perspective.none.name',
                descriptionI18nKey: 'perspective.none.description'
            });

            this.register({
                key: ALL_PERSPECTIVE,
                nameI18nKey: 'perspective.all.name',
                descriptionI18nKey: 'perspective.all.description'
            });
        };

        PerspectiveService.prototype._validate = function(configuration) {
            if (isBlank(configuration.key)) {
                throw new Error("perspectiveService.configuration.key.error.required");
            }
            if (isBlank(configuration.nameI18nKey)) {
                throw new Error("perspectiveService.configuration.nameI18nKey.error.required");
            }
            if ([NONE_PERSPECTIVE, ALL_PERSPECTIVE].indexOf(configuration.key) === -1 && (isBlank(configuration.features) || configuration.features.length === 0)) {
                throw new Error("perspectiveService.configuration.features.error.required");
            }
        };

        PerspectiveService.prototype._findByKey = function(key) {
            var perspective = this.perspectives.filter(function(persp) {
                return persp.key === key;
            })[0];
            return perspective;
        };

        PerspectiveService.prototype._fetchAllFeatures = function(perspective, holder) {
            if (!holder) {
                holder = [];
            }

            if (perspective.key === ALL_PERSPECTIVE) {
                uniqueArray(holder, (featureService.getFeatureKeys() || []));
            } else {
                uniqueArray(holder, (perspective.features || []));

                (perspective.perspectives || []).forEach(hitch(this, function(perspectiveKey) {
                    var nestedPerspective = this._findByKey(perspectiveKey);
                    if (nestedPerspective) {
                        this._fetchAllFeatures(nestedPerspective, holder);
                    } else {
                        $log.debug("nested perspective " + perspectiveKey + " was not found in the registry");
                    }
                }));
            }
        };

        PerspectiveService.prototype.register = function(configuration) {

            this._validate(configuration);

            var perspective = this._findByKey(configuration.key);

            if (!perspective) {
                perspective = new Perspective(configuration.nameI18nKey, [], true); //constructor will be modified once dependant code is refactored
                perspective.key = configuration.key;
                perspective.descriptionI18nKey = configuration.descriptionI18nKey;
                this.perspectives.push(perspective);
                systemEventService.sendAsynchEvent("perspectives:update");
            }

            perspective.features = uniqueArray(perspective.features || [], configuration.features || []);
            perspective.perspectives = uniqueArray(perspective.perspectives || [], configuration.perspectives || []);
        };

        PerspectiveService.prototype.getPerspectives = function() {
            return this.perspectives;
        };

        PerspectiveService.prototype.hasActivePerspective = function() {
            return Boolean(this.data.activePerspective);
        };

        PerspectiveService.prototype.isEmptyPerspectiveActive = function() {
            return (this.data.activePerspective && this.data.activePerspective.key === NONE_PERSPECTIVE);
        };

        PerspectiveService.prototype.switchTo = function(key) {

            if (!this._changeActivePerspective(key)) {
                return;
            }

            iFrameManager.showWaitModal();
            var featuresFromPreviousPerspective = [];
            if (this.data.previousPerspective) {
                this._fetchAllFeatures(this.data.previousPerspective, featuresFromPreviousPerspective);
            }
            var featuresFromNewPerspective = [];
            this._fetchAllFeatures(this.data.activePerspective, featuresFromNewPerspective);

            //deactivating any active feature not belonging to either the perspective or one of its nested pespectives
            featuresFromPreviousPerspective.filter(function(feature) {
                return !featuresFromNewPerspective.some(function(f) {
                    return feature == f;
                });
            }).forEach(function(feature) {
                featureService.disable(feature);
            });

            //activating any feature belonging to either the perspective or one of its nested pespectives
            featuresFromNewPerspective.filter(function(feature) {
                return !featuresFromPreviousPerspective.some(function(f) {
                    return feature == f;
                });
            }).forEach(function(feature) {
                featureService.enable(feature);
            });

            renderGateway.publish("rerender", this.data.activePerspective.key != NONE_PERSPECTIVE);

            if (this.data.activePerspective.key == NONE_PERSPECTIVE) {
                iFrameManager.hideWaitModal();
            }
        };

        PerspectiveService.prototype._retrievePerspective = function(key) {
            // Validation
            // Change the perspective only if it makes sense.
            if (this.data.activePerspective && this.data.activePerspective.key === key) {
                return null;
            }

            var newPerspective = this._findByKey(key);
            if (!newPerspective) {
                throw new Error("switchTo() - Couldn't find perspective with key " + key);
            }

            return newPerspective;
        };

        PerspectiveService.prototype._changeActivePerspective = function(newPerspectiveKey) {
            var newPerspective = this._retrievePerspective(newPerspectiveKey);
            if (newPerspective) {
                this.data.previousPerspective = this.data.activePerspective;
                this.data.activePerspective = newPerspective;
                storageService.putValueInCookie(PERSPECTIVE_COOKIE_NAME, newPerspective.key, true);
            }
            return newPerspective;
        };

        PerspectiveService.prototype.selectDefault = function() {
            return storageService.getValueFromCookie(PERSPECTIVE_COOKIE_NAME, true).then(hitch(this, function(cookieValue) {
                var defaultPerspective = (cookieValue && this._findByKey(cookieValue)) ? cookieValue : NONE_PERSPECTIVE;
                var perspective = (this.data.previousPerspective) ? this.data.previousPerspective.key : defaultPerspective;

                this.switchTo(perspective);
            }));
        };

        PerspectiveService.prototype.clearActivePerspective = function() {
            this.data.previousPerspective = this.data.activePerspective;
            delete this.data.activePerspective;
        };

        PerspectiveService.prototype._registerEventHandlers = function() {
            systemEventService.registerEventHandler(EVENTS.CLEAR_PERSPECTIVE_FEATURES, hitch(this, this._clearPerspectiveFeatures));
            systemEventService.registerEventHandler(EVENTS.LOGOUT, hitch(this, this._onLogoutPerspectiveCleanup));
        };

        PerspectiveService.prototype._clearPerspectiveFeatures = function() {
            // De-activates all current perspective's features (Still leaves the cookie in the system).
            var perspectiveFeatures = [];
            if (this.data && this.data.activePerspective) {
                this._fetchAllFeatures(this.data.activePerspective, perspectiveFeatures);
            }

            perspectiveFeatures.forEach(function(feature) {
                featureService.disable(feature);
            });

            return $q.when();
        };

        PerspectiveService.prototype._onLogoutPerspectiveCleanup = function() {
            return this._clearPerspectiveFeatures().then(hitch(this, function() {
                this.clearActivePerspective();

                return $q.when();
            }));
        };

        return new PerspectiveService();

    });
