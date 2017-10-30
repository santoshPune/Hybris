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
describe('slotVisibilityComponent', function() {
    var parentScope, scope, element, ctrl;
    var editorModalService;
    var HEADER_SLOT_ID = 'HeaderSlotId';

    beforeEach(customMatchers);
    beforeEach(module('cmssmarteditTemplates'));

    beforeEach(module('editorModalServiceModule', function($provide) {
        editorModalService = jasmine.createSpyObj('editorModalService', ['openAndRerenderSlot']);
        $provide.value('editorModalService', editorModalService);
    }));

    beforeEach(module('slotVisibilityComponentModule'));

    beforeEach(inject(function($compile, $rootScope) {
        var component = {
            uid: '1',
            name: 'xyz',
            typeCode: 'BannerComponent'
        };

        parentScope = $rootScope.$new();
        $.extend(parentScope, {
            component: component,
            slotId: HEADER_SLOT_ID
        });

        element = $compile('<slot-visibility-component ' +
            'data-component="component"> </slot-visibility-component>')(parentScope);
        parentScope.$digest();

        scope = element.isolateScope();
        ctrl = scope.ctrl;
    }));

    describe('template', function() {

        it('should display the default component picture', function() {
            expect(element.find('img').attr('src').trim()).toContain('/cmssmartedit/images/component_default.png');
        });

        it('should have the name in the component link', function() {
            expect(element.find('a').text().trim()).toBe('xyz');
        });

        it('should have the name in the link', function() {
            expect(element.find('p').text()).toBe('BannerComponent');
        });
    });

    describe('controller', function() {
        it('should display the component', function() {
            expect(ctrl.component).toEqual({
                uid: '1',
                name: 'xyz',
                typeCode: 'BannerComponent'
            });
        });

        it('should have the name in the link', function() {
            element.find('a').click();
            expect(editorModalService.openAndRerenderSlot).toHaveBeenCalledWith(ctrl.component.typeCode, ctrl.component.uid, ctrl.slotId);
        });
    });
});
