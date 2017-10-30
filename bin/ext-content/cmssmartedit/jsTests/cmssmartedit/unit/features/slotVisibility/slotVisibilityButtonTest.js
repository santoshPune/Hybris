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
describe('slotVisibilityButton', function() {
    var parentScope, scope, element, ctrl, $q;
    var slotVisibilityService;

    beforeEach(customMatchers);
    beforeEach(module('cmssmarteditTemplates'));
    beforeEach(module('pascalprecht.translate'));

    describe('when hidden component list is empty', function() {

        // initialize
        beforeEach(module('slotVisibilityServiceModule', function($provide) {
            slotVisibilityService = jasmine.createSpyObj('slotVisibilityService', ['getHiddenComponents']);
            $provide.value('slotVisibilityService', slotVisibilityService);
            slotVisibilityService.getHiddenComponents.andCallFake(function() {
                return $q.when([]);
            });
        }));
        beforeEach(module('slotVisibilityComponentModule'));
        beforeEach(module('slotVisibilityButtonModule'));
        beforeEach(inject(function($compile, $rootScope, _$q_) {
            parentScope = $rootScope.$new();
            $q = _$q_;
            $.extend(parentScope, {
                ctrl: {
                    smarteditComponentId: 'abc-slot-id'
                }
            });
            element = $compile('<slot-visibility-button data-slot-id="{{::ctrl.smarteditComponentId}}" ' +
                'data-set-remain-open="ctrl.setRemainOpen(button, remainOpen)"></slot-visibility-button>')(parentScope);
            parentScope.$digest();
            scope = element.isolateScope();
            ctrl = scope.ctrl;
        }));

        describe('template', function() {
            it('should not display the button', function() {
                expect(element.find('button[type=button]').length).toBe(0);
            });
        });

        describe('controller', function() {
            it('should set button visibility to false', function() {
                expect(ctrl.buttonVisible).toBe(false);
            });
            it('should expect that the hidden component list is empty', function() {
                expect(ctrl.hiddenComponents).toEqual([]);
            });
            it('should expect component list visibility status to not be opened', function() {
                expect(ctrl.isComponentListOpened).toBe(false);
            });
        });
    });

    describe('when hidden component list is not empty', function() {
        // initialize
        beforeEach(module('slotVisibilityServiceModule', function($provide) {
            slotVisibilityService = jasmine.createSpyObj('slotVisibilityService', ['getHiddenComponents']);
            $provide.value('slotVisibilityService', slotVisibilityService);
            slotVisibilityService.getHiddenComponents.andCallFake(function() {
                return $q.when([{
                    uid: '1',
                    name: 'component1',
                    typeCode: 'BannerComponent'
                }, {
                    uid: '2',
                    name: 'component2',
                    typeCode: 'SimpleBannerComponent'
                }]);
            });
        }));
        beforeEach(module('editorModalServiceModule', function($provide) {
            var editorModalService = jasmine.createSpyObj('slotVisibilityService', ['getHiddenComponents']);
            $provide.value('editorModalService', editorModalService);
        }));
        beforeEach(module('slotVisibilityComponentModule'));
        beforeEach(module('slotVisibilityButtonModule'));
        beforeEach(inject(function($compile, $rootScope, _$q_) {
            parentScope = $rootScope.$new();
            $q = _$q_;
            $.extend(parentScope, {
                ctrl: {
                    smarteditComponentId: 'abc-slot-id'
                }
            });
            element = $compile('<slot-visibility-button data-slot-id="{{::ctrl.smarteditComponentId}}" ' +
                'data-set-remain-open="ctrl.setRemainOpen(button, remainOpen)"></slot-visibility-button>')(parentScope);
            parentScope.$digest();
            scope = element.isolateScope();
            ctrl = scope.ctrl;
        }));

        describe('template', function() {
            // tests
            it('should not display the button', function() {
                expect(element.find('button[type=button]').length).toBe(1);
            });

            it('should display dropdown menu if the button is clicked', function() {
                element.find('button[type=button]').click();
                expect(element.find('.dropdown-menu').length).toBe(1);
            });

            it('should display components', function() {
                element.find('button[type=button]').click();
                expect(element.find('.slot-visibility-component').length).toBe(2);
            });
        });

        describe('controller', function() {
            it('should expect button to be visible', function() {
                expect(ctrl.buttonVisible).toBe(true);
            });
            it('should expect that the hidden component list is not empty', function() {
                expect(ctrl.hiddenComponents.length).toBe(2);
            });
            it('should expect component list visibility status to be opened', function() {
                element.find('button[type=button]').click();
                expect(ctrl.isComponentListOpened).toBe(true);
            });
        });

    });

});
