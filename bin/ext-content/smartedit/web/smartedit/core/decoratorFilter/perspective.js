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
angular.module('PerspectiveModule', ['perspectiveServiceInterfaceModule', 'decoratorServiceModule', 'ngCookies'])

.constant('PerspectiveCookieVersion', "1.0")

.factory('systemPerspectives', function(Perspective) {
    var perspectiveAll = new Perspective(
        'perspectives.perspectivename.all', [],
        function() {
            return featureService.getFeatureKeys();
        },
        true);

    var perspectiveNone = new Perspective(
        'perspectives.perspectivename.none', [],
        undefined,
        true);

    return [perspectiveAll, perspectiveNone];
})

.factory('perspectivesCookieSerializer', function(PerspectiveCookieVersion) {
    return {
        serialize: function(perspectivesArr) {
            var serializedData = {
                version: PerspectiveCookieVersion,
                perspectives: []
            };
            if (perspectivesArr) {
                for (var index in perspectivesArr) {
                    serializedData.perspectives.push({
                        name: perspectivesArr[index].name,
                        decorators: perspectivesArr[index].getFeatures()
                    });
                }
            }
            return serializedData;
        }
    };
})

.factory('perspectiveCookieDeserializer', function(Perspective, $log) {

    return {
        deserialize: function(cookieData) {
            $log.debug("Deserializing perspective cookie with version " + cookieData.version);
            try {
                switch (cookieData.version) {
                    case "1.0":
                        var perspectives = [];
                        for (var index in cookieData.perspectives) {
                            var storedPerspective = cookieData.perspectives[index];
                            perspectives.push(new Perspective(storedPerspective.name, storedPerspective.decorators));
                        }
                        return perspectives;

                    default:
                        $log.error("Unknown perspective serial version " + cookieData.version);
                }
            } catch (err) {
                $log.error("Error serializing perspective cookie");
            }
            // fallback
            return [];
        }
    };
})

.factory('perspectiveStorage', function($cookies, Perspective, perspectiveCookieDeserializer, perspectivesCookieSerializer, systemPerspectives, $log) {

    var perspectiveCookieName = 'cmsxperspectives';

    return {
        storePerspectives: function(perspectives) {
            $log.debug('Storing perspectives to cookie');
            var expiryDate = new Date();
            expiryDate.setMonth(expiryDate.getMonth() + 2);

            // strip out system perspectives, we do not want to store system perspectives
            var perspectivesToStore = [];
            for (var p in perspectives) {
                if (perspectives[p].system !== true) {
                    perspectivesToStore.push(perspectives[p]);
                }
            }
            var perspectiveCookieData = perspectivesCookieSerializer.serialize(perspectivesToStore);

            $cookies.putObject(perspectiveCookieName, perspectiveCookieData, {
                expires: expiryDate
            });
        },

        loadPerspectives: function() {
            $log.debug('Loading perspectives from cookie');
            var storedPerspectives = $cookies.getObject(perspectiveCookieName);
            var deserializedPerspectives = [];
            if (storedPerspectives) {
                deserializedPerspectives = perspectiveCookieDeserializer.deserialize(storedPerspectives);
            }

            // prepend all system perspectives
            var allPerspectives = [];
            for (var p in systemPerspectives) {
                allPerspectives.push(systemPerspectives[p]);
            }
            for (var index in deserializedPerspectives) {
                allPerspectives.push(deserializedPerspectives[index]);
            }

            return allPerspectives;
        }
    };
})

.factory('decoratorFilterService', function(decoratorService, perspectiveStorage, Perspective) {

    var observers = [];
    var perspectives = perspectiveStorage.loadPerspectives();
    var currentPerspective = perspectives[0];

    var notifyPerspectiveChanged = function() {
        for (var o in observers) {
            observers[o](currentPerspective);
        }
    };

    //------------------------------------------------
    return {

        setCurrentPerspective: function(perspectiveName) {
            for (var p in perspectives) {
                if (perspectives[p].name === perspectiveName) {
                    currentPerspective = perspectives[p];
                    notifyPerspectiveChanged();
                    return;
                }
            }
            $log.warn('Could not set current perspective. Perspective not found: ' + perspectiveName);
        },

        getCurrentPerspective: function() {
            if (currentPerspective === null || currentPerspective === undefined) {
                $log.error('Error retrieving current perspective: No current perspective is set!');
            } else {
                return currentPerspective.clone();
            }
        },

        getDecoratorsForComponent: function(componentType) {

            // get al the decorators elligable for this component type
            var decorators = decoratorService.getDecoratorsForComponent(componentType) || [];

            // get the current decorator set for the active perspective
            // this is kind of a weird hack, the pligins list form the application manager, use the module name: XYZDecorator
            // but the directive is actually "XYZ", so we need to strip the appended 'Decorator' form the name
            var decoratorWord = 'decorator';
            var perspDecorators = [];
            var persp = this.getCurrentPerspective();
            var gins = persp.getFeatures();
            for (var p in gins) {
                perspDecorators.push(gins[p].substring(0, gins[p].length - decoratorWord.length));
            }

            var toReturn = [];
            for (var p1 in decorators) {
                for (var p2 in perspDecorators) {
                    if (decorators[p1] == perspDecorators[p2]) {
                        toReturn.push(decorators[p1]);
                    }
                }
            }
            return toReturn;
        },

        getPerspectives: function() {
            var perspectivesClone = [];
            for (var key in perspectives) {
                perspectivesClone.push(perspectives[key].clone());
            }
            return perspectivesClone;
        },

        renamePerspective: function(oldName, newName) {
            $log.debug('Perspective Service - renamePerspective, old: ' + oldName + ', new: ' + newName);
            for (var p in perspectives) {
                if (perspectives[p].name === oldName) {
                    perspectives[p].name = newName;
                    perspectiveStorage.storePerspectives(perspectives);
                    return true;
                }
            }
            $log.error('Could not rename perspective. No perspective found with the name: ' + oldName);
            return false;
        },

        setDecoratorsForPerspective: function(perspectiveName, decorators) {
            for (var p in perspectives) {
                if (perspectives[p].name === perspectiveName) {

                    perspectives[p].setFeatures(decorators);

                    // if we're updating the decorators for the active perspective, then we need to
                    // fire an event to perspectiveChanged
                    if (perspectiveName === currentPerspective.name) {
                        notifyPerspectiveChanged();
                    }
                    perspectiveStorage.storePerspectives(perspectives);
                    return;
                }
            }
            $log.warn('Could not update perspective: ' + perspectiveName + ", no perspective found with that name.");
        },

        addPerspective: function(newPerspective) {
            $log.debug('Perspective Service - addPerspective: ' + newPerspective);
            if (newPerspective instanceof Perspective) {
                perspectives.push(newPerspective.clone());
                perspectiveStorage.storePerspectives(perspectives);
            }
        },

        removePerspective: function(perspectiveName) {
            $log.debug('Perspective Service - removePerspective: ' + perspectiveName);
            for (var p in perspectives) {
                if (perspectives[p].name === perspectiveName) {
                    if (perspectives[p].system === true) {
                        $log.warn('Cannot delete perspective [' + perspectiveName + '], this is a system perspective.');
                        break;
                    }
                    perspectives.splice(p, 1);

                    if (currentPerspective.name == perspectiveName) {
                        if (perspectives.length > 0) {
                            this.setCurrentPerspective(perspectives[0].name);
                        }
                    }
                    // else? shouldn't be possible to have no perspectives
                    perspectiveStorage.storePerspectives(perspectives);
                    return;
                }
            }
            $log.warn('Could not remove perspective: ' + perspectiveName + ", no perspective found.");
        },

        registerPerspectiveChangedListener: function(fnCallback) {
            if (observers.indexOf(fnCallback) >= 0) {
                $log.warn("This callback was already registered");
            } else {
                observers.push(fnCallback);
            }
        },

        unRegisterPerspectiveChangedListener: function(fnCallback) {
            if (observers.indexOf(fnCallback) >= 0) {
                observers.splice(observers.indexOf(fnCallback), 1);
            } else {
                $log.warn("This callback was never registered");
            }
        },

    };
})

.controller('PerspectiveUIController', function($scope, $modalInstance, Perspective, decoratorFilterService) {

    var updateDecoratorSet = function(enabledDecorators) {
        //clear all existing
        $scope.decoratorSet.splice(0, $scope.decoratorSet.length);

        for (var i in $scope.allDecorators) {

            var decoratorName = $scope.allDecorators[i];
            var decoratorFound = false;
            for (var j in enabledDecorators) {
                if (enabledDecorators[j] == decoratorName) {
                    decoratorFound = true;
                    break;
                }
            }
            $scope.decoratorSet.push({
                name: $scope.allDecorators[i],
                checked: decoratorFound
            });
        }
    };

    $scope.warningForSystem = 'modal.perpectives.info.system.perspective';

    // get a copy of all perspectives and their decorators
    $scope.perspectives = decoratorFilterService.getPerspectives();

    // find the current (active) perspective in the $scope.perspectives
    // remember $scope.perspectives is a clone of the list, so we need to find the specific instance
    // in the clone, not a copy of it, so the binding can work properly with ng-model in the html
    var currentPerspName = decoratorFilterService.getCurrentPerspective().name;
    for (var key in $scope.perspectives) {
        if ($scope.perspectives[key].name == currentPerspName) {
            $scope.perspective = $scope.perspectives[key];
            break;
        }
    }

    $scope.allDecorators = []; //ApplicationManagerService.getAllDecorators();

    // The set of decorators enabled / disabled on the decorators list in the modal
    $scope.decoratorSet = [];

    $scope.perspectiveChanged = function() {
        $scope.perspective.originalName = $scope.perspective.name;
        updateDecoratorSet($scope.perspective.getFeatures());
    };
    $scope.perspectiveChanged(); // init perspective set with active perspective

    $scope.save = function() {

        var enabledDecoratorsForSelectedSet = [];
        for (var index in $scope.decoratorSet) {
            if ($scope.decoratorSet[index].checked === true) {
                enabledDecoratorsForSelectedSet.push($scope.decoratorSet[index].name);
            }
        }

        decoratorFilterService.setDecoratorsForPerspective($scope.perspective.name, enabledDecoratorsForSelectedSet);
        $scope.perspective.setFeatures(enabledDecoratorsForSelectedSet);
    };

    $scope.nameChanged = function() {
        if (!$scope.perspective.name || !$scope.perspective.name.trim()) {
            $scope.renameError = 'modal.perpectives.validation.error.nameempty';
            return;
        }
        for (var key in $scope.perspectives) {
            if ($scope.perspective !== $scope.perspectives[key]) {
                if ($scope.perspective.name === $scope.perspectives[key].name) {
                    //another perspective already exists with this name
                    $scope.renameError = 'modal.perpectives.validation.error.nameexists';
                    return;
                }
            }
        }

        $scope.renameError = "";
        decoratorFilterService.renamePerspective($scope.perspective.originalName, $scope.perspective.name);
        $scope.perspective.originalName = $scope.perspective.name;
    };

    $scope.createPerspective = function() {

        var perspNames = [];
        for (var p in $scope.perspectives) {
            perspNames.push($scope.perspectives[p].name);
        }

        var name = 'new';
        var i = 0;
        while (perspNames.indexOf(name) >= 0) {
            i++;
            name = 'new' + i;
        }

        var newPerspective = new Perspective(name, []);

        decoratorFilterService.addPerspective(newPerspective);
        $scope.perspectives.push(newPerspective);
        $scope.perspective = newPerspective;
        $scope.perspectiveChanged();
    };

    $scope.deletePerspective = function() {
        decoratorFilterService.removePerspective($scope.perspective.name);
        $scope.perspectives.splice($scope.perspectives.indexOf($scope.perspective), 1);
        $scope.perspective = $scope.perspectives[0];
        $scope.perspectiveChanged();
    };

    $scope.close = function() {
        $modalInstance.close();
    };
})


.directive('decoratorFilter', function($modal, $translate, decoratorFilterService) {
    return {
        templateUrl: 'web/smartedit/core/decoratorFilter/perspectiveTemplate.html',
        restrict: 'E',
        transclude: true,
        replace: false,
        link: function($scope) {


            var init = function() {
                // get a copy of all perspectives and their decorators
                $scope.perspectives = decoratorFilterService.getPerspectives();

                // find the current (active) perspective in the $scope.perspectives
                // remember $scope.perspectives is a clone of the list, so we need to find the specific instance
                // in the clone, not a copy of it, so the binding can work properly with ng-model in the html
                var currentPerspName = decoratorFilterService.getCurrentPerspective().name;
                for (var key in $scope.perspectives) {
                    if ($scope.perspectives[key].name == currentPerspName) {
                        $scope.perspective = $scope.perspectives[key];
                        break;
                    }
                }
            };
            init();

            var MODES = {
                button: 'button',
                full: 'full'
            };
            var mode = MODES.button;

            $scope.mouseOn = function() {
                mode = MODES.full;
            };

            $scope.mouseOff = function() {
                mode = MODES.button;
            };

            $scope.perspectiveSelected = function(newPersp) {
                $scope.mouseOff();
                $scope.perspective = newPersp;
                decoratorFilterService.setCurrentPerspective($scope.perspective.name);
            };

            $scope.showButtonOnly = function() {
                return mode === MODES.button;
            };

            $scope.manage = function() {
                var modalInstance = $modal.open({
                    templateUrl: 'web/smartedit/core/decoratorFilter/managePerspectivesTemplate.html',
                    controller: 'PerspectiveUIController'
                        //size: 'sm'
                });

                modalInstance.result.then(function(result) {
                    init();
                }, function() {
                    init();
                });
            };

        }
    };
});
