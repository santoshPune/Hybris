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

angular.module('wizardServiceModule', ['ui.bootstrap', 'translationServiceModule', 'functionsModule', 'coretemplates', 'modalServiceModule'])

.factory('wizardActions', function($log) {

    var defaultAction = {
        id: "wizard_action_id",
        i18n: 'wizard_action_label',
        isMainAction: true,
        enableIfCondition: function() {
            return true;
        },
        executeIfCondition: function() {
            return true;
        },
        execute: function(wizardService) {}
    };

    function newAction(conf) {
        var defaultCopy = angular.copy(defaultAction);
        return angular.merge(defaultCopy, conf);
    }

    return {

        customAction: function(conf) {
            return newAction(conf);
        },

        done: function(conf) {
            var nextConf = {
                id: 'ACTION_DONE',
                i18n: 'action.done',
                execute: function(wizardService) {
                    wizardService.close();
                }
            };
            angular.merge(nextConf, conf);
            return newAction(nextConf);
        },

        next: function(conf) {
            var nextConf = {
                id: 'ACTION_NEXT',
                i18n: 'action.next',
                execute: function(wizardService) {
                    if (this.nextStepId) {
                        wizardService.goToStepWithId(this.nextStepId);
                    } else if (this.nextStepIndex) {
                        wizardService.goToStepWithIndex(this.nextStepIndex);
                    } else {
                        wizardService.goToStepWithIndex(wizardService.getCurrentStepIndex() + 1);
                    }
                }
            };

            angular.merge(nextConf, conf);
            return newAction(nextConf);
        },

        navBarAction: function(conf) {
            if (!conf.wizardService || conf.destinationIndex === null) {
                throw "Error initializating navBarAction, must provide the wizardService and destinationIndex fields";
            }

            var nextConf = {
                id: 'ACTION_GOTO',
                i18n: 'action.goto',
                enableIfCondition: function() {
                    return conf.wizardService.getCurrentStepIndex() >= conf.destinationIndex;
                },
                execute: function(wizardService) {
                    wizardService.goToStepWithIndex(conf.destinationIndex);
                }
            };

            angular.merge(nextConf, conf);
            return newAction(nextConf);
        },

        back: function(conf) {
            var nextConf = {
                id: 'ACTION_BACK',
                i18n: 'action.back',
                isMainAction: false,
                execute: function(wizardService) {
                    if (this.backStepId) {
                        wizardService.goToStepWithId(this.backStepId);
                    } else if (this.backStepIndex) {
                        wizardService.goToStepWithIndex(this.backStepIndex);
                    } else {
                        var currentIndex = wizardService.getCurrentStepIndex();
                        if (currentIndex <= 0) {
                            throw "Failure to execute BACK action, no previous index exists!";
                        }
                        wizardService.goToStepWithIndex(currentIndex - 1);
                    }
                }
            };

            angular.merge(nextConf, conf);
            return newAction(nextConf);
        },

        cancel: function() {
            return newAction({
                id: 'ACTION_CANCEL',
                i18n: 'action.cancel',
                isMainAction: false,
                execute: function(wizardService) {
                    wizardService.cancel();
                }
            });
        }

    };
})

.service('modalWizard', function(modalService, modalWizardControllerFactory) {

    this.validateConfig = function(config) {
        if (!config.controller) {
            throw "WizardService - initialization exception. No controller provided";
        }
    };

    this.open = function(config) {
        this.validateConfig(config);
        return modalService.open({
            templateUrl: 'web/common/services/wizard/modalWizardTemplate.html',
            controller: modalWizardControllerFactory.fromConfig(config),
            controllerAs: 'wizardController'
        });
    };
})


.service('modalWizardControllerFactory', function($controller, wizardServiceFactory, wizardActions, MODAL_BUTTON_STYLES, $q) {

    this.fromConfig = function(config) {
        return ['$scope', '$rootScope', 'modalManager',
            function WizardController($scope, $rootScope, modalManager) {

                var wizardServiceImpl = wizardServiceFactory.newWizardService();

                angular.extend(this, $controller(config.controller, {
                    $scope: $scope,
                    wizardManager: wizardServiceImpl
                }));
                if (config.controllerAs) {
                    $scope[config.controllerAs] = this;
                }

                this._wizardContext = {};
                if (typeof this.getWizardConfig !== 'function') {
                    throw "The provided controller must provide a getWizardConfig() function.";
                }
                var modalConfig = this.getWizardConfig();
                var controller = this;

                this.executeAction = function(action) {
                    wizardServiceImpl.getActionExecutor().executeAction(action);
                };

                function setupNavBar(steps) {
                    controller._wizardContext.navActions = steps.map(function(step, index) {
                        return wizardActions.navBarAction({
                            stepIndex: index,
                            wizardService: wizardServiceImpl,
                            destinationIndex: index,
                            i18n: step.name,
                            isCurrentStep: function() {
                                return this.stepIndex === wizardServiceImpl.getCurrentStepIndex();
                            }
                        });
                    });
                }

                function setupModal(modalConfig) {
                    controller._wizardContext.templateOverride = modalConfig.templateOverride;
                    if (modalConfig.cancelAction) {
                        modalManager.setDismissCallback(function() {
                            wizardServiceImpl.getActionExecutor().executeAction(modalConfig.cancelAction);
                            return $q.reject();
                        });
                    }
                    if (modalConfig.cancelAction) {
                        modalManager.setDismissCallback(function() {
                            wizardServiceImpl.getActionExecutor().executeAction(modalConfig.cancelAction);
                            return $q.reject();
                        });
                    }

                    // strategy stuff TODO - move to strategy layer
                    setupNavBar(modalConfig.steps);
                }

                function actionToButtonConf(action) {
                    return {
                        id: action.id,
                        style: action.isMainAction ? MODAL_BUTTON_STYLES.PRIMARY : MODAL_BUTTON_STYLES.SECONDARY,
                        label: action.i18n,
                        callback: function() {
                            wizardServiceImpl.getActionExecutor().executeAction(action);
                        }
                    };
                }

                wizardServiceImpl.onLoadStep = function(stepIndex, step) {
                    modalManager.title = step.title;
                    controller._wizardContext.templateUrl = step.templateUrl;
                    modalManager.removeAllButtons();
                    (step.actions || []).forEach(function(action) {

                        if (typeof action.enableIfCondition === 'function') {
                            // TODO - this has pretty bad implications if someone tries to do server side validation, and asynch is not possible I guess
                            $rootScope.$watch(action.enableIfCondition, function(newVal, oldVal, scope) {
                                if (newVal) {
                                    modalManager.enableButton(action.id);
                                } else {
                                    modalManager.disableButton(action.id);
                                }
                            });
                        }
                        modalManager.addButton(actionToButtonConf(action));
                    }.bind(this));
                };

                wizardServiceImpl.onClose = function(result) {
                    modalManager.close(result);
                };

                wizardServiceImpl.onCancel = function() {
                    modalManager.dismiss();
                };

                wizardServiceImpl.onStepsUpdated = function(steps) {
                    setupNavBar(steps);
                };

                wizardServiceImpl.initialize(modalConfig);
                setupModal(modalConfig);

            }
        ];
    };
})


.service('defaultWizardActionStrategy', function(wizardActions) {

    function applyOverrides(wizardService, action, label, executeCondition, enableCondition) {
        if (label) {
            action.i18n = label;
        }
        if (executeCondition) {
            action.executeIfCondition = function() {
                return executeCondition(wizardService.getCurrentStepId());
            };
        }
        if (enableCondition) {
            action.enableIfCondition = function() {
                return enableCondition(wizardService.getCurrentStepId());
            };
        }
        return action;
    }

    this.applyStrategy = function(wizardService, conf) {
        var nextAction = applyOverrides(wizardService, wizardActions.next(), conf.nextLabel, conf.onNext, conf.isFormValid);
        var doneAction = applyOverrides(wizardService, wizardActions.done(), conf.doneLabel, conf.onDone, conf.isFormValid);

        var backConf = conf.backLabel ? {
            i18n: conf.backLabel
        } : null;
        var backAction = wizardActions.back(backConf);

        conf.steps.forEach(function(step, index) {
            step.actions = [];
            if (index > 0) {
                step.actions.push(backAction);
            }
            if (index === (conf.steps.length - 1)) {
                step.actions.push(doneAction);
            } else {
                step.actions.push(nextAction);
            }
        });

        conf.cancelAction = applyOverrides(wizardService, wizardActions.cancel(), conf.cancelLabel, conf.onCancel, null);
        conf.templateOverride = 'web/common/services/wizard/modalWizardNavBarTemplate.html';
    };
})

.service('wizardServiceFactory', ['WizardService', function(WizardService) {
    this.newWizardService = function() {
        return new WizardService();
    };
}])


.factory('WizardService', ['$q', 'defaultWizardActionStrategy', 'generateIdentifier', function($q, defaultWizardActionStrategy, generateIdentifier) {

    function validateConfig(config) {
        if (!config.steps || config.steps.length <= 0) {
            throw "Invalid WizardService configuration - no steps provided";
        }

        config.steps.forEach(function(step) {
            if (!step.templateUrl) {
                throw "Invalid WizardService configuration - Step missing a url: " + step;
            }
        });
    }

    function validateStepUids(steps) {
        var stepIds = {};
        steps.forEach(function(step) {
            if (step.id === undefined && step.id === null) {
                step.id = generateIdentifier();
            } else {
                if (stepIds[step.id]) {
                    throw "Invalid (Duplicate) step id: " + step.id;
                }
                stepIds[step.id] = step.id;
            }
        });
    }

    function WizardService() {
        // the overridable callbacks
        this.onLoadStep = function(step) {};
        this.onClose = function() {};
        this.onCancel = function() {};
        this.onStepsUpdated = function() {};
    }

    WizardService.prototype.initialize = function(conf) {

        validateConfig(conf);

        this._actionStrategy = conf.actionStrategy || defaultWizardActionStrategy;
        this._actionStrategy.applyStrategy(this, conf);

        this._currentIndex = 0;
        this._conf = angular.copy(conf);
        this._steps = this._conf.steps;
        this._getResult = conf.resultFn;
        validateStepUids(this._steps);

        this.goToStepWithIndex(0);
    };

    WizardService.prototype.goToStepWithIndex = function(index) {
        var nextStep = this.getStepWithIndex(index);
        if (nextStep) {
            this.onLoadStep(index, nextStep);
            this._currentIndex = index;
        }
    };

    WizardService.prototype.getActionExecutor = function() {
        return this;
    };

    WizardService.prototype.goToStepWithId = function(id) {
        this.goToStepWithIndex(this.getStepIndexFromId(id));
    };

    WizardService.prototype.addStep = function(newStep, index) {
        if (newStep.id !== 0 && !newStep.id) {
            newStep.id = generateIdentifier();
        }
        if (!index) {
            index = 0;
        }
        if (this._currentIndex >= index) {
            this._currentIndex++;
        }
        this._steps.splice(index, 0, newStep);
        validateStepUids(this._steps);
        this._actionStrategy.applyStrategy(this, this._conf);
        this.onStepsUpdated(this._steps);
    };

    WizardService.prototype.removeStepById = function(id) {
        this.removeStepByIndex(this.getStepIndexFromId(id));
    };

    WizardService.prototype.removeStepByIndex = function(index) {
        if (index >= 0 && index < this.getStepsCount()) {
            this._steps.splice(index, 1);
            if (index === this._currentIndex) {
                this.goToStepWithIndex(0);
            }
            this._actionStrategy.applyStrategy(this, this._conf);
            this.onStepsUpdated(this._steps);
        }
    };

    WizardService.prototype.close = function() {
        var result;
        if (typeof this._getResult === 'function') {
            result = this._getResult();
        }
        this.onClose(result);
    };

    WizardService.prototype.cancel = function() {
        this.onCancel();
    };

    WizardService.prototype.executeAction = function(action) {
        if (action.executeIfCondition) {
            $q.when(action.executeIfCondition()).then(function(result) {
                return $q.when(action.execute(this));
            }.bind(this));
        } else {
            return $q.when(action.execute(this));
        }

    };

    WizardService.prototype.getSteps = function() {
        return this._steps;
    };


    // Helpers ------------------------------------------------

    WizardService.prototype.getStepIndexFromId = function(stepId) {
        var index = this._steps.findIndex(function(step) {
            return step.id === stepId;
        });
        return index;
    };

    WizardService.prototype.containsStep = function(stepId) {
        return this.getStepIndexFromId(stepId) >= 0;
    };

    WizardService.prototype.getCurrentStepId = function() {
        return this.getCurrentStep().id;
    };

    WizardService.prototype.getCurrentStepIndex = function() {
        return this._currentIndex;
    };

    WizardService.prototype.getCurrentStep = function() {
        return this.getStepWithIndex(this._currentIndex);
    };

    WizardService.prototype.getStepsCount = function() {
        return this._steps.length;
    };

    WizardService.prototype.getStepWithId = function(id) {
        var index = this.getStepIndexFromId(id);
        if (index >= 0) {
            return this.getStepWithIndex(index);
        }
    };

    WizardService.prototype.getStepWithIndex = function(index) {
        if (index >= 0 && index < this.getStepsCount()) {
            return this._steps[index];
        }
        throw ("wizardService.getStepForIndex - Index out of bounds: " + index);
    };

    return WizardService;
}]);
