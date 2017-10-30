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
 *
 *
 */
describe('personalizationsmarteditContextMenu', function() {
    var mockModules = {};
    setupMockModules(mockModules);

    var personalizationsmarteditContextModal, modalService, modalManager, scope, controller;
    var mockComponentList = [{
        typeCode: "componentType1",
        uid: "component1"
    }, {
        typeCode: "notsupported",
        uid: "component2"
    }];
    var mockComponentTypeList = [{
        category: "COMPONENT",
        code: "componentType1"
    }, {
        category: "COMPONENT",
        code: "componentType2"
    }];

    beforeEach(function() {
        module(function($provide) {
            $provide.value('translateFilter', function(value) {
                return value;
            });
        });
    });

    beforeEach(module('personalizationsmarteditRestServiceModule', function($provide) {
        mockModules.personalizationsmarteditRestService = jasmine.createSpyObj('personalizationsmarteditRestService', ['getComponents', 'getNewComponentTypes', 'getComponent']);
        $provide.value('personalizationsmarteditRestService', mockModules.personalizationsmarteditRestService);
    }));

    beforeEach(module('personalizationsmarteditContextMenu'));
    beforeEach(inject(function(_$rootScope_, _$q_, _$controller_, _personalizationsmarteditContextModal_, _modalService_) {
        personalizationsmarteditContextModal = _personalizationsmarteditContextModal_;
        modalService = _modalService_;
        controller = _$controller_;
        modalManager = {
            setButtonHandler: function(val) {},
            disableButton: function(val) {},
            enableButton: function(val) {}
        };
        spyOn(modalManager, 'setButtonHandler').andCallThrough();

        mockModules.modalService.open.andCallFake(function() {
            return _$q_.defer().promise;
        });

        scope = _$rootScope_.$new();
        scope.componentId = "mockComponentId";
        scope.modalManager = modalManager;

        mockModules.personalizationsmarteditRestService.getComponents.andCallFake(function() {
            var deferred = _$q_.defer();
            deferred.resolve({
                componentItems: mockComponentList
            });
            return deferred.promise;
        });

        mockModules.personalizationsmarteditRestService.getNewComponentTypes.andCallFake(function() {
            var deferred = _$q_.defer();
            deferred.resolve({
                componentTypes: mockComponentTypeList
            });
            return deferred.promise;
        });

        mockModules.personalizationsmarteditRestService.getComponent.andCallFake(function(componentId) {
            var deferred = _$q_.defer();
            deferred.resolve({});
            return deferred.promise;
        });

    }));

    describe('openDeleteAction', function() {

        it('should be defined', function() {
            expect(personalizationsmarteditContextModal.openDeleteAction).toBeDefined();
        });

        it('is called proper functions should be called', function() {
            // when
            personalizationsmarteditContextModal.openDeleteAction("mockComponentType", "mockComponentId");
            // then
            expect(modalService.open).toHaveBeenCalled();
        });

    });

    describe('openAddAction', function() {

        it('should be defined', function() {
            expect(personalizationsmarteditContextModal.openAddAction).toBeDefined();
        });

        it('is called proper functions should be called', function() {
            // when
            personalizationsmarteditContextModal.openAddAction("mockComponentType", "mockComponentId");
            // then
            expect(modalService.open).toHaveBeenCalled();
        });

    });

    describe('openEditAction', function() {

        it('should be defined', function() {
            expect(personalizationsmarteditContextModal.openEditAction).toBeDefined();
        });

        it('is called proper functions should be called', function() {
            // when
            personalizationsmarteditContextModal.openEditAction("mockComponentType", "mockComponentId");
            // then
            expect(modalService.open).toHaveBeenCalled();
        });

    });

    describe('modalDeleteActionController', function() {

        it('is instantiated scope is properly initialized', function() {
            controller('modalDeleteActionController', {
                $scope: scope
            });
            personalizationsmarteditContextModal.openDeleteAction("mockComponentType", "mockComponentId");
            expect(scope.selectedVariation).toBeDefined();
            expect(scope.selectedCustomization).toBeDefined();
            expect(scope.modalManager.setButtonHandler).toHaveBeenCalled();
        });

    });

    describe('modalAddActionController', function() {

        it('is instantiated scope is properly initialized', function() {
            scope.editEnabled = false;
            controller('modalAddEditActionController', {
                $scope: scope
            });
            controller('modalAddActionController', {
                $scope: scope,
            });
            personalizationsmarteditContextModal.openAddAction("mockComponentType", "mockComponentId");
            expect(scope.selectedVariation).toBeDefined();
            expect(scope.selectedCustomization).toBeDefined();
            expect(scope.actions).toBeDefined();
            expect(scope.newComponentTypeSelectedEvent).toBeDefined();
            expect(scope.component).toBeDefined({});
            expect(scope.newComponent).toBeDefined({});
            expect(scope.newComponentTypes).toBeDefined({});
            expect(scope.components).toBeDefined([]);
            expect(scope.modalManager.setButtonHandler).toHaveBeenCalled();
            expect(scope.editEnabled).toBe(false);
            expect(scope.action).toEqual({});
        });

        it('has a filtered list of components loaded', function() {
            scope.editEnabled = true;
            controller('modalAddEditActionController', {
                $scope: scope
            });
            controller('modalAddActionController', {
                $scope: scope,
            });
            scope.componentSearchInputKeypress({
                which: 155
            }, '');
            scope.$apply();
            expect(scope.newComponentTypes).toEqual(
                mockComponentTypeList
            );
            expect(scope.components).toEqual(
                [{
                    typeCode: "componentType1",
                    uid: "component1"
                }]
            );
        });

    });

    describe('modalEditActionController', function() {

        it('is instantiated scope is properly initialized', function() {
            scope.editEnabled = true;
            controller('modalAddEditActionController', {
                $scope: scope
            });
            controller('modalEditActionController', {
                $scope: scope
            });
            personalizationsmarteditContextModal.openEditAction("mockComponentType", "mockComponentId");
            expect(scope.selectedVariation).toBeDefined();
            expect(scope.selectedCustomization).toBeDefined();
            expect(scope.actions).toBeDefined();
            expect(scope.newComponentTypeSelectedEvent).toBeDefined();
            expect(scope.component).toBeDefined({});
            expect(scope.newComponent).toBeDefined({});
            expect(scope.newComponentTypes).toBeDefined({});
            expect(scope.components).toBeDefined([]);
            expect(scope.modalManager.setButtonHandler).toHaveBeenCalled();
            expect(scope.editEnabled).toBe(true);
            expect(scope.action).not.toEqual({});
        });

    });

});
