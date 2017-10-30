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
describe('slotContextualMenuDecorator', function() {

    var SMARTEDIT_ROOT = '.';
    var SMARTEDIT_COMPONENT_ID = 'componentID';
    var SMARTEDIT_COMPONENT_TYPE = 'componentType';
    var ACTIVE = false;
    var MOCK_ITEMS = {
        leftMenuItems: [{
            i18nKey: 'someKey',
            iconIdle: 'static-resources/images/edit_icon.png',
            iconNonIdle: 'static-resources/images/edit_icon_error.png',
            callback: jasmine.createSpy('callback'),
            displayClass: 'someClass'
        }, {
            key: 'someOtherTemplateKey',
            templateUrl: 'path/to/dummyTemplate.html'
        }],
        moreMenuItems: [{
            i18nKey: 'someOtherKey',
            iconIdle: 'static-resources/images/edit_icon.png',
            iconNonIdle: 'static-resources/images/edit_icon_error.png',
            callback: jasmine.createSpy('callback'),
            displayClass: 'someClass'
        }]
    };

    var element, scope, ctrl, $q, $httpBackend, $templateCache;
    var contextualMenuService, renderService;

    beforeEach(customMatchers);
    beforeEach(module('coretemplates'));
    beforeEach(module('pascalprecht.translate'));

    beforeEach(module('renderServiceModule', function($provide) {
        renderService = jasmine.createSpyObj('renderService', ['_resizeEmptySlots']);
        $provide.value('renderService', renderService);
    }));

    beforeEach(module('contextualMenuServiceModule', function($provide) {
        contextualMenuService = jasmine.createSpyObj('contextualMenuService', ['getContextualMenuItems']);
        $provide.value('contextualMenuService', contextualMenuService);
    }));

    beforeEach(module('slotContextualMenuDecoratorModule', function($provide) {
        $provide.constant('smarteditroot', SMARTEDIT_ROOT);
    }));

    beforeEach(inject(function($rootScope, $compile, _$q_, _$httpBackend_, _$templateCache_) {
        $q = _$q_;

        scope = $rootScope.$new();
        $.extend(scope, {
            smarteditComponentId: SMARTEDIT_COMPONENT_ID,
            smarteditComponentType: SMARTEDIT_COMPONENT_TYPE,
            active: ACTIVE
        });

        var originalElement = "<div>Hello!</div>";
        element = $compile('<div ' +
            'class="slotContextualMenu" ' +
            'data-smartedit-component-id="{{smarteditComponentId}}" ' +
            'data-smartedit-component-type="{{smarteditComponentType}}" ' +
            'data-active="active">' +
            originalElement +
            '</div>')(scope);
        scope.$digest();

        scope = element.isolateScope();
        ctrl = scope.ctrl;

        $httpBackend = _$httpBackend_;
        $templateCache = _$templateCache_;

        $templateCache.put('path/to/dummyTemplate.html',
            "<div class='dummy-template-class'>Dummy Template</div>"
        );
    }));

    beforeEach(function() {
        contextualMenuService.getContextualMenuItems.andReturn(MOCK_ITEMS);
    });

    describe('initialization', function() {
        describe('moreButton', function() {
            beforeEach(function() {
                ctrl.toggleDropdown = jasmine.createSpy('toggleDropdwon');
            });

            it('should have a key, an i18n key, and image icons relative to the smartedit root', function() {
                expect(ctrl.moreButton.key).toBeDefined();
                expect(ctrl.moreButton.i18nKey).toBeDefined();
                expect(ctrl.moreButton.iconIdle).toBeDefined();
                expect(ctrl.moreButton.iconNonIdle).toBeDefined();
                expect(ctrl.moreButton.iconOpen).toBeDefined();
            });

            it('should have a callback that triggers toggle dropdown', function() {
                ctrl.moreButton.callback();
                expect(ctrl.toggleDropdown).toHaveBeenCalled();
            });
        });
    });

    describe('controller', function() {
        describe('getItems', function() {
            var items;

            beforeEach(function() {
                items = ctrl.getItems();
            });

            it('should fetch items from the contextual menu service', function() {
                expect(contextualMenuService.getContextualMenuItems).toHaveBeenCalled();
                expect(items).toBe(MOCK_ITEMS);
            });
        });
    });

    describe('active template', function() {
        beforeEach(function() {
            ctrl.active = true;
            scope.$digest();
        });

        it('should transclude the original element', function() {
            expect(element.find('.yWrapperData').text()).toContain('Hello!');
        });

        describe('contextual menu item', function() {
            it('should display when active', function() {
                expect(element.find('.cmsx-ctx-btns').length).toBe(2);
            });

            it('should trigger item callback on click', function() {
                $(element.find('.cmsx-ctx-btns > div > img')[0]).click();
                expect(MOCK_ITEMS.leftMenuItems[0].callback).toHaveBeenCalled();
            });

            it('should load item template', function() {
                expect(element.find('.dummy-template-class').length).toBe(1);
            });

            it('should show idle icon by default', function() {
                expect($(element.find('.cmsx-ctx-btns > div > img')[0]).attr('src')).toBe('static-resources/images/edit_icon.png');
            });

            it('should show non-idle icon on hover', function() {
                $(element.find('.cmsx-ctx-btns > div > img')[0]).trigger('mouseover');
                expect($(element.find('.cmsx-ctx-btns > div >img')[0]).attr('src')).toBe('static-resources/images/edit_icon_error.png');
                $(element.find('.cmsx-ctx-btns > div > img')[0]).trigger('mouseout');
                expect($(element.find('.cmsx-ctx-btns > div > img')[0]).attr('src')).toBe('static-resources/images/edit_icon.png');
            });
        });

        describe('panel', function() {
            it('should be displayed with component ID when active', function() {
                expect(element.find('.decorative-panel-area')).toExist();
                expect(element.find('.decorative-panel-area').text()).toContain(SMARTEDIT_COMPONENT_ID);
            });
        });

    });

    describe('inactive template', function() {
        beforeEach(function() {
            ctrl.active = false;
            scope.$digest();
        });

        it('should remove the panel with the ID of the component when inactive', function() {
            expect(element.find('.decorative-panel')).not.toExist();
        });

        it('should remove the contextual menu items when inactive', function() {
            expect(element.find('.cmsx-ctx-btns')).not.toExist();
        });
    });

});
