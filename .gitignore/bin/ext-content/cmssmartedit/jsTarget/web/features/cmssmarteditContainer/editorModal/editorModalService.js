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
angular.module('editorModalServiceModule', ['modalServiceModule', 'gatewayProxyModule', 'coretemplates', 'editorTabsetModule', 'confirmationModalServiceModule'])

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
.factory('editorModalService', ['modalService', 'MODAL_BUTTON_ACTIONS', 'MODAL_BUTTON_STYLES', 'systemEventService', 'gatewayProxy', 'renderService', 'confirmationModalService', '$q', '$log', function(modalService, MODAL_BUTTON_ACTIONS, MODAL_BUTTON_STYLES, systemEventService, gatewayProxy, renderService, confirmationModalService, $q, $log) {
        function EditorModalService() {
            this.gatewayId = 'EditorModal';
            gatewayProxy.initForService(this, ["open", "openAndRerenderSlot"]);
        }

        var _openEditorModalWithSaveCallback = function(componentType, componentId, saveCallback) {
            return modalService.open({
                title: 'type.' + componentType.toLowerCase() + '.name',
                titleSuffix: 'editor.title.suffix',
                templateInline: '<editor-tabset control="modalController.tabsetControl" model="modalController.componentData"></editor-tabset>',
                controller: ['editorModalService', 'modalManager', 'systemEventService', 'hitch', '$scope', '$log', '$q',
                    function(editorModalService, modalManager, systemEventService, hitch, $scope, $log, $q) {
                        this.isDirty = false;

                        this.componentData = {
                            componentId: componentId,
                            componentType: componentType
                        };

                        this.onSave = function() {
                            return this.tabsetControl.saveTabs().then(function(item) {
                                saveCallback();
                                return item;
                            });
                        };

                        this.onCancel = function() {
                            var deferred = $q.defer();
                            if (this.isDirty) {
                                confirmationModalService.confirm({
                                    description: 'editor.cancel.confirm'
                                }).then(hitch(this, function() {
                                    this.tabsetControl.cancelTabs().then(function() {
                                        deferred.resolve();
                                    }, function() {
                                        deferred.reject();
                                    });
                                }), function() {
                                    deferred.reject();
                                });
                            } else {
                                deferred.resolve();
                            }

                            return deferred.promise;
                        };

                        this.init = function() {
                            modalManager.setDismissCallback(hitch(this, this.onCancel));

                            modalManager.setButtonHandler(hitch(this, function(buttonId) {
                                switch (buttonId) {
                                    case 'save':
                                        return this.onSave();
                                    case 'cancel':
                                        return this.onCancel();
                                    default:
                                        $log.error('A button callback has not been registered for button with id', buttonId);
                                        break;
                                }
                            }));

                            $scope.$watch(hitch(this, function() {
                                var isDirty = this.tabsetControl && typeof this.tabsetControl.isDirty === 'function' && this.tabsetControl.isDirty();
                                return isDirty;
                            }), hitch(this, function(isDirty) {
                                if (typeof isDirty === 'boolean') {
                                    if (isDirty) {
                                        this.isDirty = true;
                                        modalManager.enableButton('save');
                                    } else {
                                        this.isDirty = false;
                                        modalManager.disableButton('save');
                                    }
                                }
                            }));
                        };
                    }
                ],
                buttons: [{
                    id: 'cancel',
                    label: 'compoment.confirmation.modal.cancel',
                    style: MODAL_BUTTON_STYLES.SECONDARY,
                    action: MODAL_BUTTON_ACTIONS.DISMISS
                }, {
                    id: 'save',
                    label: 'component.confirmation.modal.save',
                    action: MODAL_BUTTON_ACTIONS.CLOSE,
                    disabled: true
                }]
            });
        };

        /**
         * @ngdoc method
         * @name editorModalServiceModule.service:editorModalService#open
         * @methodOf editorModalServiceModule.service:editorModalService
         *
         * @description
         * Uses the {@link modalServiceModule.modalService modalService} to open an editor modal.
         *
         * The editor modal is initialized with a title in the format '<TypeName> Editor', ie: 'Paragraph Editor'. The
         * editor modal is also wired with a save and cancel button.
         *
         * The content of the editor modal is the {@link editorTabsetModule.directive:editorTabset editorTabset}.
         *
         * @param {String} componentType The type of component as defined in the platform.
         * @param {String} componentId The ID of the component as defined in the database.
         *
         * @returns {Promise} A promise that resolves to the data returned by the modal when it is closed.
         */
        EditorModalService.prototype.open = function(componentType, componentId) {
            return _openEditorModalWithSaveCallback(componentType, componentId, function() {
                renderService.renderComponent(componentId, componentType);
            });
        };

        /**
         * @ngdoc method
         * @name editorModalServiceModule.service:editorModalService#openAndRerenderSlot
         * @methodOf editorModalServiceModule.service:editorModalService
         *
         * @description
         * Uses the {@link modalServiceModule.modalService modalService} to open an editor modal.
         *
         * The editor modal is initialized with a title in the format '<TypeName> Editor', ie: 'Paragraph Editor'. The
         * editor modal is also wired with a save and cancel button.
         *
         * The content of the editor modal is the {@link editorTabsetModule.directive:editorTabset editorTabset}.
         *
         * @param {String} componentType The type of component as defined in the platform.
         * @param {String} componentId The ID of the component as defined in the database.
         * @param {String} slotId The ID of the slot as defined in the database.
         *
         * @returns {Promise} A promise that resolves to the data returned by the modal when it is closed.
         */
        EditorModalService.prototype.openAndRerenderSlot = function(componentType, componentId, slotId) {
            return _openEditorModalWithSaveCallback(componentType, componentId, function() {
                renderService.renderSlots(slotId);
            });
        };

        return new EditorModalService();
    }])
    .run(['editorModalService', function(editorModalService) {
        // Trigger instantiation of editorModalService
    }]);
