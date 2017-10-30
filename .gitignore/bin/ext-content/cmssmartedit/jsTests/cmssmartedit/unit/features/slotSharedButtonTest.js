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
describe('slotSharedButton', function() {
    var $q, $rootScope, slotSharedService, element, ctrl, scope;

    beforeEach(customMatchers);
    beforeEach(function() {
        angular.module('componentHandlerServiceModule', []);
    });
    beforeEach(module('cmssmarteditTemplates'));
    beforeEach(module('slotSharedButtonModule'));

    beforeEach(module('pascalprecht.translate', function($translateProvider) {
        $translateProvider.translations('en', {});
        $translateProvider.preferredLanguage('en');
    }));

    beforeEach(module('slotSharedServiceModule', function($provide) {
        slotSharedService = jasmine.createSpyObj('slotSharedService', ['isSlotShared', 'reloadSharedSlotMap']);
        $provide.value('slotSharedService', slotSharedService);
    }));

    describe('given slot is shared', function() {
        beforeEach(inject(function(_$rootScope_, _$q_, $compile) {
            $rootScope = _$rootScope_;
            $q = _$q_;
            slotSharedService.isSlotShared.andCallFake(function() {
                return $q.when(true);
            });
            scope = $rootScope.$new();
            $.extend(scope, {
                ctrl: {
                    smarteditComponentId: 'abc-slot-id'
                }
            });
            element = $compile('<slot-shared-button  data-slot-id="{{::ctrl.smarteditComponentId}}"></slot-shared-button>')(scope);
            scope.$digest();
            scope = element.isolateScope();
            ctrl = scope.ctrl;
        }));

        describe('controller', function() {
            it('should set the slotSharedFlag to true', function() {
                expect(slotSharedService.isSlotShared.toHaveBeenCalled);
                expect(ctrl.slotSharedFlag).toEqual(true);
            });
        });

        describe('template', function() {
            it('should display the image', function() {
                var images = element.find('img');
                expect(images.length).toBe(1);
            });

            it('should display the shared button', function() {
                expect(element.find('button').length).toBe(1);
            });
        });
    });

    describe('given slot is not shared', function() {
        beforeEach(inject(function(_$rootScope_, _$q_, $compile) {
            $rootScope = _$rootScope_;
            $q = _$q_;
            slotSharedService.isSlotShared.andCallFake(function() {
                return $q.when(false);
            });
            scope = $rootScope.$new();
            $.extend(scope, {
                ctrl: {
                    smarteditComponentId: 'abc-slot-id'
                }
            });
            element = $compile('<slot-shared-button  data-slot-id="{{::ctrl.smarteditComponentId}}"></slot-shared-button>')(scope);
            scope.$digest();
            scope = element.isolateScope();
            ctrl = scope.ctrl;
        }));

        describe('controller', function() {
            it('should set the slotSharedFlag to false', function() {
                expect(slotSharedService.isSlotShared.toHaveBeenCalled);
                expect(ctrl.slotSharedFlag).toEqual(false);
            });
        });

        describe('template', function() {
            it('should not display the image', function() {
                var images = element.find('img');
                expect(images.length).toBe(0);
            });

            it('should not display the shared button', function() {
                expect(element.find('button').length).toBe(0);
            });
        });
    });
});
