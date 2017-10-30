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
angular.module('cmssmartedit', [
        'resourceLocationsModule',
        'decoratorServiceModule',
        'contextualMenuServiceModule',
        'removeComponentServiceModule',
        'experienceInterceptorModule',
        'editorEnablerServiceModule',
        'alertServiceModule',
        'translationServiceModule',
        'featureServiceModule',
        'slotVisibilityButtonModule',
        'slotVisibilityServiceModule',
        'cmssmarteditTemplates',
        'componentHandlerServiceModule',
        'assetsServiceModule',
        'slotSharedButtonModule'

    ])
    .run(
        // Note: only instances can be injected in a run function
        ['$rootScope', '$translate', 'decoratorService', 'contextualMenuService', 'alertService', 'removeComponentService', 'editorEnablerService', 'featureService', 'componentHandlerService', 'assetsService', 'slotVisibilityService', function($rootScope, $translate, decoratorService, contextualMenuService, alertService, removeComponentService, editorEnablerService, featureService, componentHandlerService, assetsService, slotVisibilityService) {

            var retriggerMouseEvent = function(element, eventType, originalEvent) {
                // Not using jQuery trigger since it was not working as expected.
                var event;
                if (typeof window.Event == "function") {
                    event = new MouseEvent(eventType, {
                        "bubbles": true,
                        "cancelable": false,
                        "clientX": originalEvent.clientX,
                        "clientY": originalEvent.clientY,
                        "offsetX": originalEvent.offsetX,
                        "offsetY": originalEvent.offsetY,
                        "pageX": originalEvent.pageX,
                        "pageY": originalEvent.pageY,
                        "screenX": originalEvent.screenX,
                        "screenY": originalEvent.screenY,
                        "view": originalEvent.view
                    });
                } else {
                    // IE Fix
                    event = document.createEvent("MouseEvents");
                    event.initMouseEvent(
                        eventType,
                        true, // can bubble
                        false, // cancelable
                        originalEvent.view, // viewArg
                        0, // detailArg
                        originalEvent.offsetX, // screenX
                        originalEvent.offsetY, // screenY
                        originalEvent.clientX, // clientX
                        originalEvent.clientY, // clientY
                        false, // ctrlKeyArg
                        false, // altKeyArg
                        false, // shiftKeyArg
                        false, // metaKeyArg
                        0, // buttonArg
                        null); // relatedTargetArg
                }

                element.dispatchEvent(event);
            };

            editorEnablerService.enableForComponents(['^.*Component$']);

            decoratorService.addMappings({
                '^((?!Slot).)*$': ['se.contextualMenu'],
                '^.*Slot$': ['se.slotContextualMenu', 'se.basicSlotContextualMenu']
            });

            featureService.addContextualMenuButton({
                key: 'se.cms.dragandropbutton',
                nameI18nKey: 'contextmenu.title.dragndrop',
                regexpKeys: ['^((?!Slot).)*$'],
                condition: function(componentType, componentId) {
                    return true;
                },
                callback: function() {},
                callbacks: {
                    'mousedown': function(configuration, $event) {
                        var element = componentHandlerService.getOriginalComponentWithinSlot(configuration.componentId, configuration.componentType, configuration.slotId).get(0);
                        retriggerMouseEvent(element, 'mousedown', $event);
                    }
                },
                displayClass: 'movebutton',
                iconIdle: assetsService.getAssetsRoot() + '/images/contextualmenu_move_off.png',
                iconNonIdle: assetsService.getAssetsRoot() + '/images/contextualmenu_move_on.png',
                smallIcon: assetsService.getAssetsRoot() + '/images/contextualmenu_move_on.png'
            });

            featureService.addContextualMenuButton({
                key: 'se.cms.remove',
                nameI18nKey: 'contextmenu.title.Remove',
                regexpKeys: ['^((?!Slot).)*$'],
                condition: function(configuration) {
                    return true;
                },
                callback: function(configuration, $event) {
                    var element = componentHandlerService.getOriginalComponent(configuration.componentId, configuration.componentType).get(0);
                    var slotOperationRelatedId = componentHandlerService.getSlotOperationRelatedId(element);
                    var slotOperationRelatedType = componentHandlerService.getSlotOperationRelatedType(element);

                    removeComponentService.removeComponent({
                        slotId: configuration.slotId,
                        componentId: configuration.componentId,
                        componentType: configuration.componentType,
                        slotOperationRelatedId: slotOperationRelatedId,
                        slotOperationRelatedType: slotOperationRelatedType,
                    }).then(
                        function() {
                            $translate('alert.component.removed.from.slot', {
                                componentID: slotOperationRelatedId,
                                slotID: configuration.slotId
                            }).then(function(translation) {
                                alertService.pushAlerts([{
                                    successful: true,
                                    message: translation,
                                    closeable: true
                                }]);
                                $event.preventDefault();
                                $event.stopPropagation();
                            });
                        }
                    );
                },
                displayClass: 'removebutton',
                iconIdle: assetsService.getAssetsRoot() + '/images/contextualmenu_delete_off.png',
                iconNonIdle: assetsService.getAssetsRoot() + '/images/contextualmenu_delete_on.png',
                smallIcon: assetsService.getAssetsRoot() + '/images/contextualmenu_delete_on.png'
            });

            featureService.addContextualMenuButton({
                key: 'se.slotContextualMenuVisibility',
                nameI18nKey: 'slotcontextmenu.title.visibility',
                regexpKeys: ['^.*ContentSlot$'],
                callback: function() {},
                templateUrl: 'web/features/cmssmartedit/slotVisibility/slotVisibilityWidgetTemplate.html'
            });

            featureService.addContextualMenuButton({
                key: 'se.slotSharedButton',
                nameI18nKey: 'slotcontextmenu.title.shared.button',
                regexpKeys: ['^.*Slot$'],
                callback: function() {},
                templateUrl: 'web/features/cmssmartedit/slotShared/slotSharedTemplate.html'
            });
        }]);

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
 * @name editorEnablerServiceModule
 * @description
 * # The editorEnablerServiceModule
 *
 * The editor enabler service module provides a service that allows enabling the Edit Component contextual menu item,
 * providing a SmartEdit CMS admin the ability to edit various properties of the given component.
 */
angular.module("editorEnablerServiceModule", ["featureServiceModule", "editorModalServiceModule", "functionsModule"])

/**
 * @ngdoc service
 * @name editorEnablerServiceModule.service:editorEnablerService
 *
 * @description
 * Convenience service to attach the open editor modal action to the contextual menu of a given component type, or
 * given regex corresponding to a selection of component types.
 *
 * Example: The Edit button is added to the contextual menu of the CMSParagraphComponent, and all types postfixed
 * with BannerComponent.
 *
 * <pre>
    angular.module('app', ['editorEnablerServiceModule'])
        .run(function(editorEnablerService) {
            editorEnablerService.enableForComponents(['CMSParagraphComponent', '*BannerComponent']);
        });
 * </pre>
 */
.factory("editorEnablerService", ['featureService', 'editorModalService', 'hitch', function(featureService, editorModalService, hitch) {

    // Class Definition
    function EditorEnablerService() {}

    // Private


    EditorEnablerService.prototype._key = "se.cms.edit";

    EditorEnablerService.prototype._nameI18nKey = "contextmenu.nameI18nKey.edit";

    EditorEnablerService.prototype._descriptionI18nKey = "contextmenu.descriptionI18n.edit";

    EditorEnablerService.prototype._editDisplayClass = "editbutton";

    EditorEnablerService.prototype._editButtonIconIdle = "/cmssmartedit/images/contextualmenu_edit_off.png";

    EditorEnablerService.prototype._editButtonIconNonIdle = "/cmssmartedit/images/contextualmenu_edit_on.png";

    EditorEnablerService.prototype._editButtonSmallIcon = "/cmssmartedit/images/contextualmenu_edit_on.png";

    EditorEnablerService.prototype._editButtonCallback = function(configuration) {
        editorModalService.open(configuration.componentType, configuration.componentId);
    };

    // Public
    /**
     * @ngdoc method
     * @name editorEnablerServiceModule.service:editorEnablerService#enableForComponents
     * @methodOf editorEnablerServiceModule.service:editorEnablerService
     *
     * @description
     * Enables the Edit contextual menu item for the given component types.
     *
     * @param {Array} componentTypes The list of component types, as defined in the platform, for which to enable the
     * Edit contextual menu.
     */
    EditorEnablerService.prototype.enableForComponents = function(componentTypes) {
        var contextualMenuConfig = {
            key: this._key, // It's the same key as the i18n, however here we're not doing any i18n work.
            nameI18nKey: this._nameI18nKey,
            descriptionI18nKey: this._descriptionI18nKey,
            regexpKeys: componentTypes,
            displayClass: this._editDisplayClass,
            i18nKey: this._editI18nKey,
            iconIdle: this._editButtonIconIdle,
            iconNonIdle: this._editButtonIconNonIdle,
            smallIcon: this._editButtonSmallIcon,
            callback: this._editButtonCallback
        };

        featureService.addContextualMenuButton(contextualMenuConfig);
    };

    // Factory Definition
    return new EditorEnablerService();
}]);

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
 * @name editorModalServiceModule
 * @description
 * # The editorModalServiceModule
 *
 * The editor modal service module provides a service that allows opening an editor modal for a given component type and
 * component ID. The editor modal is populated with a save and cancel button, and is loaded with the {@link
 * editorTabsetModule.directive:editorTabset editorTabset} as its content, providing a way to edit
 * various fields of the given component.
 */
angular.module('editorModalServiceModule', ['gatewayProxyModule'])

/**
 * @ngdoc service
 * @name editorModalServiceModule.service:editorModalService
 *
 * @description
 * Convenience service to open an editor modal window for a given component type and component ID.
 *
 * Example: A button is bound to the function '$scope.onClick' via the ng-click directive. Clicking the button will
 * trigger the opening of an editor modal for a CMSParagraphComponent with the ID 'termsAndConditionsParagraph'
 *
 * <pre>
    angular.module('app', ['editorModalServiceModule'])
        .controller('someController', function($scope, editorModalService) {
            $scope.onClick = function() {
                editorModalService.open('CMSParagraphComponent', 'termsAndConditionsParagraph');
            };
        });
 * </pre>
*/
.factory('editorModalService', ['gatewayProxy', function(gatewayProxy) {
    function EditorModalService() {
        this.gatewayId = 'EditorModal';
        gatewayProxy.initForService(this, ["open", "openAndRerenderSlot"]);
    }

    /**
     * @ngdoc method
     * @name editorModalServiceModule.service:editorModalService#open
     * @methodOf editorModalServiceModule.service:editorModalService
     *
     * @description
     * Proxy function which delegates opening an editor modal for a given component type and component ID to the
     * SmartEdit container.
     *
     * @param {String} componentType The type of component as defined in the platform.
     * @param {String} componentId The ID of the component as defined in the database.
     *
     * @returns {Promise} A promise that resolves to the data returned by the modal when it is closed.
     */
    EditorModalService.prototype.open = function(componentType, componentId) {};

    /**
     * @ngdoc method
     * @name editorModalServiceModule.service:editorModalService#open
     * @methodOf editorModalServiceModule.service:editorModalService
     *
     * @description
     * Proxy function which delegates opening an editor modal for a given component type and component ID to the
     * SmartEdit container.
     *
     * @param {String} componentType The type of component as defined in the platform.
     * @param {String} componentId The ID of the component as defined in the database.
     * @param {String} slotId The ID of the slot as defined in the database.
     *
     * @returns {Promise} A promise that resolves to the data returned by the modal when it is closed.
     */
    EditorModalService.prototype.openAndRerenderSlot = function(componentType, componentId) {};

    return new EditorModalService();
}]);

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
angular.module('removeComponentServiceModule', ['restServiceFactoryModule', 'renderServiceModule', 'gatewayProxyModule', 'removeComponentServiceInterfaceModule', 'experienceInterceptorModule', 'functionsModule', 'resourceLocationsModule'])
    /**
     * @ngdoc service
     * @name removeComponentService.removeComponentService
     *
     * @description
     * Service to remove a component from a slot
     */
    .factory('removeComponentService', ['restServiceFactory', 'renderService', 'extend', 'gatewayProxy', '$q', '$log', 'RemoveComponentServiceInterface', 'experienceInterceptor', 'PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI', function(restServiceFactory, renderService, extend, gatewayProxy, $q, $log, RemoveComponentServiceInterface, experienceInterceptor, PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI) {
        var REMOVE_COMPONENT_CHANNEL_ID = "RemoveComponent";

        var RemoveComponentService = function(gatewayId) {
            this.gatewayId = gatewayId;

            gatewayProxy.initForService(this, ["removeComponent"]);
        };

        RemoveComponentService = extend(RemoveComponentServiceInterface, RemoveComponentService);

        var restServiceForRemoveComponent = restServiceFactory.get(PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI + '?slotId=:slotId&componentId=:componentId', 'componentId');

        RemoveComponentService.prototype.removeComponent = function(configuration) {

            return restServiceForRemoveComponent.remove({
                slotId: configuration.slotId,
                componentId: configuration.slotOperationRelatedId
            }).then(function() {
                renderService.renderRemoval(configuration.componentId, configuration.componentType, configuration.slotId);
            });

        };

        return new RemoveComponentService(REMOVE_COMPONENT_CHANNEL_ID);

    }]);

angular.module('slotSharedButtonModule', ['slotSharedServiceModule'])
    .controller('slotSharedButtonController', ['slotSharedService', '$scope', '$timeout', function(slotSharedService, $scope, $timeout) {
        this.sharedOffImageUrl = '/cmssmartedit/images/shared_slot_menu_off.png';
        this.sharedOnImageUrl = '/cmssmartedit/images/shared_slot_menu_on.png';
        this.slotSharedFlag = false;
        this.isPopupOpened = false;
        this.buttonName = 'slotSharedButton';

        slotSharedService.isSlotShared(this.slotId).then(function(result) {
            this.slotSharedFlag = result;
        }.bind(this));

        this.positionHiddenComponentList = function() {
            $timeout(function() {
                var buttonElement = $('#sharedSlotButton-' + this.slotId);
                var dropdownElement = $('#shared-slot-list-' + this.slotId);
                var overflow = dropdownElement.offset().left + dropdownElement.width() - $('body').width();
                if (overflow >= 0) {
                    dropdownElement.offset({
                        left: buttonElement.offset().left + buttonElement.width() - dropdownElement.width() - 45,
                        top: dropdownElement.offset().top
                    });
                }
            }.bind(this));
        };

        this.openPopup = function($event) {
            $event.preventDefault();
            $event.stopPropagation();

            this.positionHiddenComponentList();
            this.isPopupOpened = true;
            this.setRemainOpen({
                button: this.buttonName,
                remainOpen: true
            });
        };
    }])
    .directive('slotSharedButton', function() {
        return {
            templateUrl: 'web/features/cmssmartedit/slotShared/slotSharedButtonTemplate.html',
            restrict: 'E',
            controller: 'slotSharedButtonController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                setRemainOpen: '&',
                active: '=',
                slotId: '@'
            }
        };
    });

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
 * @name slotSharedServiceModule.slotSharedService
 * @description
 * SlotSharedService provides methods to interact with the backend for shared slot information. 
 */
angular.module('slotSharedServiceModule', ['resourceModule', 'componentHandlerServiceModule', 'resourceLocationsModule'])
    .service('slotSharedService', ['restServiceFactory', '$q', 'componentHandlerService', 'PAGES_CONTENT_SLOT_RESOURCE_URI', function(restServiceFactory, $q, componentHandlerService, PAGES_CONTENT_SLOT_RESOURCE_URI) {
        var pagesContentSlotsResource = restServiceFactory.get(PAGES_CONTENT_SLOT_RESOURCE_URI);
        var currentPageId = componentHandlerService.getPageUID();
        var sharedSlotsPromise;

        var getMapFragmentPromiseByUniqueSlotId = function(slotId) {
            return pagesContentSlotsResource.get({
                slotId: slotId
            }).then(function(pagesContentSlotsResponse) {
                var uniquePageIds = pagesContentSlotsResponse.pageContentSlotList.map(function(pageContentSlotComponent) {
                    return pageContentSlotComponent.pageId;
                }).filter(getUniqueArray);

                var mapFragment = {};
                mapFragment[slotId] = uniquePageIds.length > 1;
                return mapFragment;
            });
        };

        var getUniqueArray = function(id, position, ids) {
            return ids.indexOf(id) === position;
        };

        var getSlotIdsFromList = function(pageContentSlotComponent) {
            return pageContentSlotComponent.slotId;
        };

        /**
         * @ngdoc method
         * @name slotSharedServiceModule.slotSharedService#reloadSharedSlotMap
         * @methodOf slotSharedServiceModule.slotSharedService
         *
         * @description
         * This function fetches all the slots with the pageUID and after filtering the slotIDs list it populates slot shared Map Fragment for the unique slotIds
         * and from the Map Fragment slot shared status can be extracted based on the slot id.
         *
         * @param {String} pageUID of the page
         */
        this.reloadSharedSlotMap = function() {
            sharedSlotsPromise = pagesContentSlotsResource.get({
                pageId: currentPageId
            }).then(function(pagesContentSlotsResponse) {
                var mapFragmentPromises = (pagesContentSlotsResponse.pageContentSlotList || [])
                    .map(getSlotIdsFromList)
                    .filter(getUniqueArray)
                    .map(getMapFragmentPromiseByUniqueSlotId);
                return $q.all(mapFragmentPromises).then(function(mapFragments) {
                    return mapFragments.reduce(function(mapCopy, mapFragment) {
                        return angular.extend({}, mapCopy, mapFragment);
                    }, {});
                });
            });
            return sharedSlotsPromise;
        };

        /**
         * @ngdoc method
         * @name slotSharedServiceModule.slotSharedService#isSlotShared
         * @methodOf slotSharedServiceModule.slotSharedService
         *
         * @description
         * Checks if the slot is shared and returns true in case slot is shared and returns false if it is not. 
         * Based on this service method the slot shared button is shown or hidden for a particular slotId
         *
         * @param {String} slotId of the slot
         */
        this.isSlotShared = function(slotId) {
            return sharedSlotsPromise.then(function(slotMap) {
                return slotMap[slotId];
            });
        };
    }])
    .run(['slotSharedService', function(slotSharedService) {
        slotSharedService.reloadSharedSlotMap();
    }]);

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
 * @name slotVisibilityButtonModule
 * @description
 *
 * The slot visibility button module provides a directive and controller to manage the button within the slot contextual menu 
 * and the hidden component list, which is also part of the dropdown menu associated with the directive's template.     
 */
angular.module('slotVisibilityButtonModule', ['slotVisibilityServiceModule', 'slotVisibilityComponentModule'])

/**
 * @ngdoc controller
 * @name slotVisibilityButtonModule.controller:slotVisibilityButtonController
 *
 * @description
 * The slot visibility button controller is responsible for enabling and disabling the hidden components button, 
 * as well as displaying the hidden components list. It also provides functions to open and close the hidden component list.
 *
 * @param {Object} slotVisibilityService slot visibility service instance
 * @param {Object} $scope current scope instance
 */
.controller('slotVisibilityButtonController', ['slotVisibilityService', '$scope', '$timeout', function(slotVisibilityService, $scope, $timeout) {
        this.buttonName = 'slotVisibilityButton';
        this.eyeOnImageUrl = '/cmssmartedit/images/visibility_slot_menu_on.png';
        this.eyeOffImageUrl = '/cmssmartedit/images/visibility_slot_menu_off.png';
        this.eyeImageUrl = this.eyeOffImageUrl;
        this.closeImageUrl = '/cmssmartedit/images/close_button.png';
        this.buttonVisible = false;
        this.hiddenComponents = [];
        this.isComponentListOpened = false;

        $scope.$watch('ctrl.isComponentListOpened', function(newValue, oldValue) {
            this.eyeImageUrl = (newValue ? this.eyeOnImageUrl : this.eyeOffImageUrl);
            if (newValue !== oldValue) {
                this.setRemainOpen({
                    button: this.buttonName,
                    remainOpen: this.isComponentListOpened
                });
            }
        }.bind(this));

        this.onInit = function() {
            slotVisibilityService.getHiddenComponents(this.slotId).then(function(hiddenComponents) {
                this.hiddenComponents = hiddenComponents;
                this.hiddenComponentCount = hiddenComponents.length;
                if (this.hiddenComponentCount > 0) {
                    this.buttonVisible = true;
                }
            }.bind(this));
        };

        this.positionHiddenComponentList = function() {
            $timeout(function() {
                var buttonElement = $('#slot-visibility-button-' + this.slotId);
                var dropdownElement = $('#slot-visibility-list-' + this.slotId);
                var overflow = dropdownElement.offset().left + dropdownElement.width() - $('body').width();
                if (overflow >= 0) {
                    dropdownElement.offset({
                        left: buttonElement.offset().left + buttonElement.width() - dropdownElement.width(),
                        top: dropdownElement.offset().top
                    });
                }
            }.bind(this));
        };

        this.openHiddenComponentList = function($event) {
            $event.preventDefault();
            $event.stopPropagation();

            this.positionHiddenComponentList();
            this.isComponentListOpened = true;
            this.setRemainOpen({
                button: this.buttonName,
                remainOpen: true
            });
        };

        this.closeHiddenComponentList = function($event) {
            $event.preventDefault();
            $event.stopPropagation();
            this.isComponentListOpened = false;
            this.setRemainOpen({
                button: this.buttonName,
                remainOpen: false
            });
        };

        this.onInit();
    }])
    /**
     * @ngdoc directive
     * @name slotVisibilityButtonModule.directive:slotVisibilityButton
     *
     * @description
     * The slot visibility button directive is used inside the slot contextual menu and provides a button 
     * image that displays the number of hidden components, as well as a dropdown menu of hidden component.
     *
     * The directive expects that the parent, the slot contextual menu, has a setRemainOpen function and a 
     * slotId value on the parent's scope. setRemainOpen is used to send a command to the parent to leave 
     * the slot contextual menu open.
     */
    .directive('slotVisibilityButton', function() {
        return {
            templateUrl: 'web/features/cmssmartedit/slotVisibility/slotVisibilityButtonTemplate.html',
            restrict: 'E',
            transclude: true,
            replace: false,
            controller: 'slotVisibilityButtonController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                setRemainOpen: '&',
                slotId: '@',
                initButton: '@'
            }
        };
    });

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
 * @name slotVisibilityComponentModule
 * @description
 *
 * The slot visibility component module provides a directive and controller to display the hidden components of a specified content slot. 
 */
angular.module('slotVisibilityComponentModule', ['editorModalServiceModule'])
    /**
     * @ngdoc controller
     * @name slotVisibilityComponentModule.controller:slotVisibilityComponentController
     *
     * @description
     * The slot visibility component controller is responsible for controlling the directive, as well as adding 
     * methods to interact with other modules and directives. The controller provides a function to open the generic editor modal.
     *
     * @param {Object} editorModalService the editor modal service instance
     */
    .controller('slotVisibilityComponentController', ['editorModalService', function(editorModalService) {
        this.imageRoot = '/cmssmartedit/images';

        this.openEditorModal = function() {
            editorModalService.openAndRerenderSlot(this.component.typeCode, this.component.uid, this.slotId);
        };
    }])
    /**
     * @ngdoc directive
     * @name slotVisibilityComponentModule.directive:slotVisibilityComponent
     *
     * @description
     * The slot visibility component directive is used to display information about a specified hidden component.
     * It receives the component on its scope and it binds it to its own controller.
     */
    .directive('slotVisibilityComponent', function() {
        return {
            templateUrl: 'web/features/cmssmartedit/slotVisibility/slotVisibilityComponentTemplate.html',
            restrict: 'E',
            transclude: false,
            replace: true,
            controller: 'slotVisibilityComponentController',
            controllerAs: 'ctrl',
            scope: {},
            bindToController: {
                component: '=',
                slotId: '@'
            }
        };
    });

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
 * @name slotVisibilityServiceModule
 * @description
 *
 * The slot visibility service module provides factories and services to manage all backend calls and loads an internal 
 * structure that provides the necessary data to the slot visibility button and slot visibility component.
 */
angular.module('slotVisibilityServiceModule', ['resourceModule', 'componentHandlerServiceModule', 'functionsModule', 'renderServiceModule'])
    /**
     * @ngdoc service
     * @name SlotVisibilityService
     * @description
     *
     * The SlotVisibilityService class provides methods to interact with the backend. 
     * The definition of the class is not instantiated immediately, whereas the service instance of 
     * this same class (@see slotVisibilityService) returns an instance of this service definition.  
     * 
     * @param {Object} itemsResource Gets all components based on their IDs. 
     * @param {Object} pagesContentSlotsComponentsResource Gets content slots and components based on their page IDs.
     * @param {Object} componentHandlerService Gets the current page ID. 
     */
    .service('SlotVisibilityService', ['itemsResource', 'pagesContentSlotsComponentsResource', 'componentHandlerService', 'renderService', function(itemsResource, pagesContentSlotsComponentsResource, componentHandlerService, renderService) {
        function SlotVisibilityService() {
            var hiddenComponentsMapPromise;

            var currentPageId = componentHandlerService.getPageUID();

            var loadHiddenComponentsByComponents = function(pagesContentSlotsComponents, components) {
                var hiddenComponents = (components.componentItems || []).filter(function(component) {
                    return !component.visible;
                }).reduce(function(map, component) {
                    map[component.uid] = component;
                    return map;
                }, {});

                var hiddenComponentsMap = (pagesContentSlotsComponents.pageContentSlotComponentList || [])
                    .reduce(function(map, pageContentSlotComponent) {
                        map[pageContentSlotComponent.slotId] = map[pageContentSlotComponent.slotId] || [];
                        if (hiddenComponents[pageContentSlotComponent.componentId]) {
                            map[pageContentSlotComponent.slotId].push(hiddenComponents[pageContentSlotComponent.componentId]);
                        }
                        return map;
                    }, {});

                return hiddenComponentsMap;
            };

            var loadHiddenComponentsBySlotsAndComponents = function(pagesContentSlotsComponents) {
                var componentIds = (pagesContentSlotsComponents.pageContentSlotComponentList || [])
                    .map(function(pageContentSlotComponent) {
                        return pageContentSlotComponent.componentId;
                    });

                return itemsResource.get({
                    uids: componentIds.join(',')
                }).then(function(components) {
                    return loadHiddenComponentsByComponents(pagesContentSlotsComponents, components);
                });
            };

            var loadHiddenComponentsMapPromise = function() {
                hiddenComponentsMapPromise = pagesContentSlotsComponentsResource.get({
                        pageId: currentPageId
                    })
                    .then(loadHiddenComponentsBySlotsAndComponents);
                return hiddenComponentsMapPromise;
            };

            var reRenderSlots = function(hiddenComponentsMapOld, hiddenComponentsMapNew) {
                var slotIds = [];
                Object.keys(hiddenComponentsMapOld).forEach(function(slotId) {
                    // check if the number of visible components has changed
                    if (hiddenComponentsMapOld[slotId] && hiddenComponentsMapNew[slotId] &&
                        hiddenComponentsMapOld[slotId].length !== hiddenComponentsMapNew[slotId].length) {
                        slotIds.push(slotId);
                    }
                });
                // if there is at least one slot that has been changed, then re-render the slots
                if (slotIds.length > 0) {
                    renderService.renderSlots(slotIds);
                }
            };

            /**
             * Function to load the hidden components for the slots in the page. 
             * If the hiddenComponentsMapPromise does not exist, then it only load the hidden components, 
             * otherwise it will re-load the components for the slots that have changed the status.  
             */
            this.loadHiddenComponents = function() {
                if (!hiddenComponentsMapPromise) {
                    hiddenComponentsMapPromise = loadHiddenComponentsMapPromise();
                    return hiddenComponentsMapPromise;
                }
                return this.reLoadHiddenComponents();
            };

            /**
             * re-load the hidden components data structure and re-render slots that have changed. 
             */
            this.reLoadHiddenComponents = function() {
                hiddenComponentsMapPromise.then(function(hiddenComponentsMap) {
                    var hiddenComponentsMapOld = angular.copy(hiddenComponentsMap);

                    hiddenComponentsMapPromise = loadHiddenComponentsMapPromise();
                    hiddenComponentsMapPromise.then(function(hiddenComponentsMapNew) {
                        reRenderSlots(hiddenComponentsMapOld, hiddenComponentsMapNew);
                    });
                });

                return hiddenComponentsMapPromise;
            };

            /**
             * Function to get the hidden component count. 
             * @param slotId the slot id 
             * @return the promise with the number of hidden components for slotId
             */
            this.getHiddenComponentCount = function(slotId) {
                return this.getHiddenComponents(slotId).then(function(hiddenComponents) {
                    return hiddenComponents.length;
                });
            };

            /**
             * Function to get the hidden components for a certain slot. 
             * @param slotId the slot id 
             * @return the promise with the hidden components list for slotId
             */
            this.getHiddenComponents = function(slotId) {
                return (hiddenComponentsMapPromise || this.loadHiddenComponents()).then(function(hiddenComponentsMap) {
                    return hiddenComponentsMap[slotId] || [];
                }, function(error) {
                    return [];
                });
            };
        }
        return SlotVisibilityService;
    }])
    .service('slotVisibilityService', ['SlotVisibilityService', 'systemEventService', 'throttle', '$q', function(SlotVisibilityService, systemEventService, throttle, $q) {
        var instance = new SlotVisibilityService();
        instance.loadHiddenComponents();

        var throttledNotification = throttle(function() {
            instance.loadHiddenComponents();
        }, 2000);

        systemEventService.registerEventHandler('SLOT_CONTEXTUAL_MENU_ACTIVE', function() {
            throttledNotification();
            return $q.when();
        });
        return instance;
    }]);

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
angular.module('synchronizeDecorator', ['restServiceFactoryModule', 'translationServiceModule', 'functionsModule'])
    .factory("SynchronizeService", ['restServiceFactory', 'hitch', '$q', function(restServiceFactory, hitch, $q) {

        var SynchronizeService = function(smarteditComponentType, smarteditComponentId) {
            this.smarteditComponentType = smarteditComponentType;
            this.smarteditComponentId = smarteditComponentId;
            this.restService = restServiceFactory.get("/cmswebservices/:smarteditComponentType/:smarteditComponentId/synchronize");
            this.synced = true;
        };

        SynchronizeService.prototype.load = function() {
            var deferred = $q.defer();
            this.restService.get({
                smarteditComponentType: this.smarteditComponentType,
                smarteditComponentId: this.smarteditComponentId
            }).then(hitch(this, function(response) {
                this.synced = response.status;
                deferred.resolve(this.synced);
            }));
            return deferred.promise;
        };

        SynchronizeService.prototype.synchronize = function() {
            var deferred = $q.defer();
            this.restService.save({
                smarteditComponentType: this.smarteditComponentType,
                smarteditComponentId: this.smarteditComponentId
            }, {}).then(hitch(this, function(response) {
                this.synced = response.status;
                deferred.resolve(this.synced);
            }));
            return deferred.promise;
        };

        return SynchronizeService;
    }])
    .directive('synchronize', ['$timeout', 'SynchronizeService', function($timeout, SynchronizeService) {
        return {
            templateUrl: 'web/features/cmssmartedit/synchronize/synchronizeDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@',
                active: '='
            },

            link: function($scope, element, attrs) {

                $scope.service = new SynchronizeService($scope.smarteditComponentType, $scope.smarteditComponentId);
                $scope.service.load();

                $scope.isContainer = $scope.smarteditComponentType == 'ContentSlotModel';

                $scope.visible = false;
                $scope.mouseleave = function() {
                    $timeout(function() {
                        $scope.visible = false;
                    }, 1000);
                };
                $scope.mouseenter = function() {
                    $scope.visible = true;
                };
            }
        };
    }]);

angular.module('cmssmarteditTemplates', []).run(['$templateCache', function($templateCache) {
  'use strict';

  $templateCache.put('web/features/cmssmartedit/slotShared/slotSharedButtonTemplate.html',
    "<div class=\"shared-slot-button-template\">\n" +
    "\t<div class=\"btn-group shared-slot-button-template__btn-group\" dropdown dropdown-append-to-body is-open=\"ctrl.isPopupOpened\">\n" +
    "\t\t<button type=\"button\"  id=\"sharedSlotButton-{{::ctrl.slotId}}\" class=\"btn btn-primary dropdown-toggle shared-slot-button-template__btn\" \n" +
    "\t\t\t\tdata-ng-if=\"ctrl.slotSharedFlag\" data-ng-click=\"ctrl.openPopup($event)\">\n" +
    "\t\t\t<img class=\"shared-slot-button-template__btn__img\" data-ng-src=\"{{ ctrl.isPopupOpened && ctrl.sharedOnImageUrl || ctrl.sharedOffImageUrl }}\"/>\n" +
    "\t\t</button>\n" +
    "\t\t<ul class=\"dropdown-menu dropdown-menu-right shared-slot-button-template__menu\" role=\"menu\" id=\"shared-slot-list-{{::ctrl.slotId}}\">\n" +
    "\t\t\t<lh class=\"shared-slot-button-template__menu__title\">\n" +
    "\t\t\t\t<p class=\"shared-slot-button-template__menu__title__text\">{{::'slot.shared.popover.message'| translate}}</p>\n" +
    "\t\t\t</lh>\n" +
    "\t\t</ul>\n" +
    "\t</div>\n" +
    "</div>\n"
  );


  $templateCache.put('web/features/cmssmartedit/slotShared/slotSharedTemplate.html',
    "<slot-shared-button data-slot-id=\"{{::ctrl.smarteditComponentId}}\"></slot-shared-button>\n"
  );


  $templateCache.put('web/features/cmssmartedit/slotVisibility/slotVisibilityButtonTemplate.html',
    "<div class=\"slot-visibility-button-template\">\n" +
    "\t<div class=\"btn-group slot-visibility-button-template__btn-group\" dropdown dropdown-append-to-body is-open=\"ctrl.isComponentListOpened\">\n" +
    "\t\t<button type=\"button\" class=\"btn btn-primary dropdown-toggle slot-visibility-button-template__btn\" \n" +
    "\t\t\t\tdata-ng-click=\"ctrl.openHiddenComponentList($event)\"\n" +
    "\t\t\t\tdata-ng-if=\"ctrl.buttonVisible\" id=\"slot-visibility-button-{{::ctrl.slotId}}\">\n" +
    "\t\t\t<img class=\"slot-visibility-button-template__btn__img \" data-ng-src=\"{{ ctrl.eyeImageUrl }}\"/>{{ ::ctrl.hiddenComponentCount }}\n" +
    "\t\t</button>\n" +
    "\t\t<ul class=\"dropdown-menu dropdown-menu-right slot-visibility-button-template__menu\" role=\"menu\" id=\"slot-visibility-list-{{::ctrl.slotId}}\">\n" +
    "\t\t\t<lh class=\"slot-visibility-button-template__menu__title\">\n" +
    "\t\t\t\t<p class=\"slot-visibility-button-template__menu__title__text\">{{'slotvisibility.list.title' | translate}}</p>\n" +
    "\t\t\t\t<img class=\"slot-visibility-button-template__menu__title__close-btn\" data-ng-src=\"{{ ::ctrl.closeImageUrl }}\" data-ng-click=\"ctrl.closeHiddenComponentList($event)\"/>\n" +
    "\t\t\t</lh>\n" +
    "\t\t\t<li class=\"slot-visibility-component slot-visibility-button-template__menu__component\" data-ng-repeat=\"component in ctrl.hiddenComponents\">\n" +
    "\t\t\t\t<slot-visibility-component data-component=\"component\" data-slot-id=\"{{::ctrl.slotId}}\"></slot-visibility-component>\n" +
    "\t\t\t</li>\n" +
    "\t\t</ul>\n" +
    "\t</div>\n" +
    "</div>\n"
  );


  $templateCache.put('web/features/cmssmartedit/slotVisibility/slotVisibilityComponentTemplate.html',
    "<div class=\"slot-visiblity-component-template\">\n" +
    "\t<div class=\"slot-visiblity-component-template__icon\">\n" +
    "\t\t<img data-ng-src=\"{{::ctrl.imageRoot}}/component_default.png\" class=\"slot-visiblity-component-template__icon__image\" alt=\"{{::ctrl.component.typeCode}}\"/>\n" +
    "\t</div>\n" +
    "\t<div class=\"slot-visiblity-component-template__text\">\n" +
    "\t\t<a class=\"slot-visiblity-component-template__text__name\" href=\"#\" data-ng-click=\"ctrl.openEditorModal()\">{{ ::ctrl.component.name }}</a>\n" +
    "\t\t<p class=\"slot-visiblity-component-template__text__type\">{{ ::ctrl.component.typeCode }}</p>\n" +
    "\t</div>\n" +
    "</div>\n"
  );


  $templateCache.put('web/features/cmssmartedit/slotVisibility/slotVisibilityWidgetTemplate.html',
    "<slot-visibility-button data-slot-id=\"{{::ctrl.smarteditComponentId}}\" \n" +
    "\t\t\t\t\t\tdata-set-remain-open=\"ctrl.setRemainOpen(button, remainOpen)\"/>\n"
  );


  $templateCache.put('web/features/cmssmartedit/synchronize/synchronizeDecoratorTemplate.html',
    "<div>\n" +
    "    <div data-ng-mouseleave=\"mouseleave()\">\n" +
    "        <img title=\"{{'synchronize.notok.tooltip' | translate}}\" data-ng-if=\"!service.synced && visible\" class=\"ng-class:{synchronize:true, clickable:true, 'pull-right':!isContainer}\" data-ng-click=\"service.synchronize()\" data-ng-src=/cmssmartedit/images/synchronization_notok.gif />\n" +
    "        <img title=\"{{'synchronize.ok.tooltip' | translate}}\" data-ng-if=\"service.synced && visible\" class=\"ng-class:{synchronize:true, clickable:true, 'pull-right':!isContainer}\" data-ng-src=/cmssmartedit/images/synchronization_ok.gif />\n" +
    "        <div data-ng-mouseenter=\"mouseenter()\" class=\"ng-class:{minDivHeight:true, contentSlot:isContainer && visible}\">\n" +
    "            <div data-ng-transclude></div>\n" +
    "        </div>\n" +
    "    </div>\n" +
    "</div>\n"
  );

}]);

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
 * @name assetsServiceModule
 * @description
 * # The assetsServiceModule
 *
 * The assetsServiceModule provides methods to handle assets such as images
 *
 */
angular.module('assetsServiceModule', [])
    /**
     * @ngdoc object
     * @name cmsConstantsModule.object:testAssets
     *
     * @description
     * overridable constant to specify whether cmssmartedit is in test mode
     */
    .constant('testAssets', false)
    /**
     * @ngdoc object
     * @name cmsConstantsModule.service:assetsService
     *
     * @description
     * returns the assets resources root depending whether or not we are in test mode
     */
    .factory('assetsService', ['testAssets', function(testAssets) {
        return {
            getAssetsRoot: function() {
                return testAssets ? '/web/webroot' : '/cmssmartedit';
            }
        };
    }]);

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
 * @name removeComponentServiceInterfaceModule
 * @description
 * # The removeComponentServiceInterfaceModule
 *
 * Provides a service with the ability to remove a component from a slot.
 */
angular.module('removeComponentServiceInterfaceModule', [])
    /**
     * @ngdoc service
     * @name removeComponentServiceInterfaceModule.service:RemoveComponentServiceInterface
     * @description
     * Service interface specifying the contract used to remove a component from a slot.
     *
     * This class serves as an interface and should be extended, not instantiated.
     */
    .factory('RemoveComponentServiceInterface', function() {
        function RemoveComponentServiceInterface() {}

        /**
         * @ngdoc method
         * @name removeComponentServiceInterfaceModule.service:RemoveComponentServiceInterface#removeComponent
         * @methodOf removeComponentServiceInterfaceModule.service:RemoveComponentServiceInterface
         *
         * @description
         * Removes the component specified by the given ID from the component specified by the given ID.
         *
         * @param {String} slotId The ID of the slot from which to remove the component.
         * @param {String} componentId The ID of the component to remove from the slot.
         */
        RemoveComponentServiceInterface.prototype.removeComponent = function(slotId, componentId) {};

        return RemoveComponentServiceInterface;
    });

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
(function() {
    /**
     * @ngdoc overview
     * @name resourceModule
     *
     * @description
     * The resource module provides $resource factories.  
     */
    angular.module('resourceModule', ['restServiceFactoryModule', 'resourceLocationsModule'])
        .factory('itemsResource', ['restServiceFactory', 'ITEMS_RESOURCE_URI', function(restServiceFactory, ITEMS_RESOURCE_URI) {
            return restServiceFactory.get(ITEMS_RESOURCE_URI);
        }])
        .factory('pagesContentSlotsComponentsResource', ['restServiceFactory', 'PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI', function(restServiceFactory, PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI) {
            return restServiceFactory.get(PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI);
        }]);
})();

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
(function() {

    var CONTEXT_CATALOG = 'CURRENT_CONTEXT_CATALOG';
    var CONTEXT_CATALOG_VERSION = 'CURRENT_CONTEXT_CATALOG_VERSION';
    var CONTEXT_SITE_ID = 'CURRENT_CONTEXT_SITE_ID';

    angular.module('resourceLocationsModule')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:TYPES_RESOURCE_URI
     *
     * @description
     * Resource URI of the component types REST service.
     */
    .constant('TYPES_RESOURCE_URI', '/cmswebservices/v1/types')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:ITEMS_RESOURCE_URI
     *
     * @description
     * Resource URI of the custom components REST service.
     */
    .constant('ITEMS_RESOURCE_URI', '/cmswebservices/v1/sites/' + CONTEXT_SITE_ID + '/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/items')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI
     *
     * @description
     * Resource URI of the pages content slot component REST service.
     */
    .constant('PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI', '/cmswebservices/v1/sites/' + CONTEXT_SITE_ID + '/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/pagescontentslotscomponents')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI
     *
     * @description
     * Resource URI of the content slot type restrictions REST service.
     */
    .constant('CONTENT_SLOT_TYPE_RESTRICTION_RESOURCE_URI', '/cmswebservices/v1/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/pages/:pageUid/contentslots/:slotUid/typerestrictions')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGES_LIST_RESOURCE_URI
     *
     * @description
     * Resource URI of the pages REST service.
     */
    .constant('PAGES_LIST_RESOURCE_URI', '/cmswebservices/v1/sites/:siteUID/catalogs/:catalogId/versions/:catalogVersion/pages')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGE_LIST_PATH
     *
     * @description
     * Path of the page list
     */
    .constant('PAGE_LIST_PATH', '/pages/:siteId/:catalogId/:catalogVersion')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGES_CONTENT_SLOT_RESOURCE_URI
     *
     * @description
     * Resource URI of the page content slots REST service
     */
    .constant('PAGES_CONTENT_SLOT_RESOURCE_URI', '/cmswebservices/v1/sites/' + CONTEXT_SITE_ID + '/catalogs/' + CONTEXT_CATALOG + '/versions/' + CONTEXT_CATALOG_VERSION + '/pagescontentslots')

    /**
     * @ngdoc object
     * @name resourceLocationsModule.object:PAGE_TEMPLATES_URI
     *
     * @description
     * Resource URI of the page templates REST service
     */
    .constant('PAGE_TEMPLATES_URI', '/cmswebservices/v1/sites/:siteUID/catalogs/:catalogId/versions/:catalogVersion/pagetemplates');

})();

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
angular.module('componentServiceModule', ['restServiceFactoryModule', 'functionsModule', 'resourceLocationsModule'])
    /**
     * @ngdoc service
     * @name componentMenuModule.ComponentService
     *
     * @description
     * Service which manages component types and items
     */
    .factory('ComponentService', ['restServiceFactory', 'hitch', '$q', '$log', 'parseQuery', 'TYPES_RESOURCE_URI', 'ITEMS_RESOURCE_URI', 'PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI', function(restServiceFactory, hitch, $q, $log, parseQuery, TYPES_RESOURCE_URI, ITEMS_RESOURCE_URI, PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI) {

        var restServiceForTypes = restServiceFactory.get(TYPES_RESOURCE_URI);
        var restServiceForItems = restServiceFactory.get(ITEMS_RESOURCE_URI);
        var restServiceForAddNewComponent = restServiceFactory.get(ITEMS_RESOURCE_URI);
        var restServiceForAddExistingComponent = restServiceFactory.get(PAGES_CONTENT_SLOT_COMPONENT_RESOURCE_URI);

        var _typesLoaded = false;
        var _itemsLoaded = false;
        var _listOfComponentTypes = {};
        var _listOfComponentItems = {};
        var _payload = {};

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#addNewComponent
         * @methodOf componentMenuModule.ComponentService
         *
         * @description given a component type and a slot id, a new componentItem is created and added to a slot
         *
         * @param {String} componenCode of the ComponentType to be created and added to the slot.
         * @param {String} componentName of the new component to be created.
         * @param {String} pageId used to identify the current page template.
         * @param {String} slotId used to identify the slot in the current template.
         * @param {String} position used to identify the position in the slot in the current template.
         */
        var _addNewComponent = function(componentName, componentCode, pageId, slotId, position) {

            var deferred = $q.defer();

            _payload.name = componentName;
            _payload.slotId = slotId;
            _payload.pageId = pageId;
            _payload.position = position;
            _payload.typeCode = componentCode;

            restServiceForAddNewComponent.save(_payload).then(
                function(response) {
                    deferred.resolve(response);
                },
                function(response) {
                    deferred.reject(response);
                }
            );

            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#addExistingComponent
         * @methodOf componentMenuModule.ComponentService
         *
         * @description add an existing component item to a slot
         *
         * @param {String} pageId used to identify the page containing the slot in the current template.
         * @param {String} componentId used to identify the existing component which will be added to the slot.
         * @param {String} slotId used to identify the slot in the current template.
         * @param {String} position used to identify the position in the slot in the current template.
         */
        var _addExistingComponent = function(pageId, componentId, slotId, position) {

            var deferred = $q.defer();

            _payload.pageId = pageId;
            _payload.slotId = slotId;
            _payload.componentId = componentId;
            _payload.position = position;

            restServiceForAddExistingComponent.save(_payload).then(
                function(response) {
                    deferred.resolve();
                },
                function(response) {
                    deferred.reject(response);
                }
            );

            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#loadComponentTypes
         * @methodOf componentMenuModule.ComponentService
         *
         * @description all component types are retrieved
         */
        var _loadComponentTypes = function() {

            var deferred = $q.defer();

            restServiceForTypes.get().then(
                function(response) {
                    angular.copy(response, _listOfComponentTypes);
                    deferred.resolve(_listOfComponentTypes);
                    _typesLoaded = true;
                    return deferred.promise;

                },
                function() {
                    _typesLoaded = false;
                    deferred.reject();
                    return deferred.promise;
                }
            );

            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#loadComponentItem
         * @methodOf componentMenuModule.ComponentService
         *
         * @description load a component identified by its id
         */
        var _loadComponentItem = function(id) {
            return restServiceForItems.getById(id);
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#loadComponentItems
         * @methodOf componentMenuModule.ComponentService
         *
         * @description all existing component items for the current catalog are retrieved
         */
        var _loadComponentItems = function() {

            var deferred = $q.defer();

            restServiceForItems.get().then(
                function(response) {
                    angular.copy(response, _listOfComponentItems);
                    deferred.resolve(_listOfComponentItems);
                    _itemsLoaded = true;
                },
                function() {
                    _itemsLoaded = false;
                    deferred.reject();
                }
            );
            return deferred.promise;
        };

        /**
         * @ngdoc method
         * @name componentMenuModule.ComponentService#loadPagedComponentItems
         * @methodOf componentMenuModule.ComponentService
         *
         * @description all existing component items for the current catalog are retrieved in the form of pages
         * used for pagination especially when the result set is very large.
         * 
         * @param {String} mask the search string to filter the results.
         * @param {String} pageSize the number of elements that a page can contain.
         * @param {String} page the current page number.
         */
        var _loadPagedComponentItems = function(mask, pageSize, page) {

            return restServiceForItems.get({
                pageSize: pageSize,
                currentPage: page,
                mask: mask,
                sort: 'name'
            });
        };

        return {
            loadComponentTypes: _loadComponentTypes,
            listOfComponentTypes: _listOfComponentTypes,
            loadComponentItem: _loadComponentItem,
            loadComponentItems: _loadComponentItems,
            loadPagedComponentItems: _loadPagedComponentItems,
            listOfComponentItems: _listOfComponentItems,
            addNewComponent: _addNewComponent,
            addExistingComponent: _addExistingComponent,
        };

    }]);

