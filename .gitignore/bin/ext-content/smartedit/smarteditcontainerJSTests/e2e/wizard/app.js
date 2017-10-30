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
window.smartedit = {
    i18nAPIRoot: "somepath"
};

angular.module('app', ['ngMockE2E', 'coretemplates', 'resourceLocationsModule', 'languageServiceModule', 'wizardServiceModule'])

.service('personValidator', function() {

    return {

        isValidPerson: function(person) {
            return (this.isValidName(person));
        },

        isValidName: function(person) {
            if (person.name) {
                return true;
            } else {
                return false;
            }
        }
    };
})

.controller('personBuilderController', ['$q', '$log', 'wizardActions', 'wizardManager', 'personValidator',
    function($q, $log, wizardActions, wizardManager, personValidator) {

        var extraStep = {
            id: 'extra',
            name: 'Appology',
            title: 'wizard.title.extra',
            templateUrl: 'smarteditcontainerJSTests/e2e/wizard/stepExtra.html'
        };

        var stepToggle = false;

        function updateOffendedStep(toggle) {
            var stepIsAdded = wizardManager.containsStep('extra');
            if (toggle) {
                if (!stepIsAdded) {
                    wizardManager.addStep(extraStep, 2);
                }
            } else if (stepIsAdded) {
                wizardManager.removeStepById('extra');
            }
        }


        this.toggleExtraStep = function() {
            stepToggle = !stepToggle;
            updateOffendedStep(stepToggle);
        };

        this.person = {};

        this.isFormValid = function(stepId) {
            switch (stepId) {
                case 'name':
                    return personValidator.isValidName(this.person);
                default:
                    return true;
            }
        }.bind(this);

        this.onNext = function(stepId) {
            switch (stepId) {
                case 'gender':
                    updateOffendedStep(this.person.offended);
                    break;

                default:
                    return true;
            }
        }.bind(this);

        this.onCancel = function() {
            return $q(function(resolve, reject) {
                if (confirm("Are you sure you want to cancel?")) {
                    resolve();
                } else {
                    reject();
                }
            });
        };

        this.onDone = function() {
            return $q(function(resolve, reject) {
                if (confirm("Save and close?")) {
                    resolve(this.person);
                } else {
                    reject();
                }
            });
        };

        this.getResult = function() {
            return this.person;
        }.bind(this);

        this.getWizardConfig = function() {
            return {

                // action strategy
                isFormValid: this.isFormValid,
                onNext: this.onNext,
                onCancel: this.onCancel,
                onDone: this.onDone,
                doneLabel: 'action.save',
                //nextLabel: 'action.next2',
                //backLabel: 'action.back2',
                //cancelLabel: 'action.cancel2',

                resultFn: this.getResult,

                // wizard model
                steps: [{
                    id: 'name',
                    name: 'Name',
                    title: 'wizard.title.name',
                    templateUrl: 'smarteditcontainerJSTests/e2e/wizard/stepName.html'
                }, {
                    id: 'gender',
                    name: 'Gender',
                    title: 'wizard.title.sex',
                    templateUrl: 'smarteditcontainerJSTests/e2e/wizard/stepSex.html'
                }, {
                    id: 'age',
                    name: 'Age',
                    title: 'wizard.title.age',
                    templateUrl: 'smarteditcontainerJSTests/e2e/wizard/stepAge.html'
                }, {
                    id: 'summary',
                    name: 'Summary',
                    title: 'wizard.title.save',
                    templateUrl: 'smarteditcontainerJSTests/e2e/wizard/stepDone.html'
                }]
            };
        }.bind(this);

    }
])

.factory('wizardPersonBuilder', function(modalWizard) {
    return {
        buildPerson: function() {
            return modalWizard.open({
                controller: 'personBuilderController',
                controllerAs: 'personController'
            });
        }
    };
})

.controller('defaultController', function($scope, $httpBackend, $log, I18N_RESOURCE_URI, languageService, wizardPersonBuilder) {

    $httpBackend.whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale()).respond({
        "action.complete": "Complete",
        "action.done": "DONE",
        "action.next": "NEXT",
        "action.cancel": "CANCEL",
        "action.back": "BACK",
        "action.save": "SAVE",
        "action.gotonext": "Go to next step",
        "wizard.title.name": "What is your name?",
        "wizard.title.sex": "What gender do you identify yourself as?",
        "wizard.title.age": "How old are you?",
        "wizard.title.extra": "Some extra step",
        "wizard.title.save": "Person Details"
    });
    $httpBackend.whenGET(/Template/).passThrough();
    $httpBackend.whenGET(/step/).passThrough();



    $scope.openWizard = function() {
        wizardPersonBuilder.buildPerson().then(function(result) {
            $scope.person = result;
        }, function() {
            $scope.person = 'Wizard cancelled.';
        });
    };

});
