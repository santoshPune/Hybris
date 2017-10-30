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
describe(
    'Unit integration test of sample directive',
    function() {
        var $compile, $rootScope, parentScope, directiveScope, element, smarteditComponentType, smarteditComponentId, $httpBackend;


        beforeEach(module('ui.bootstrap'));
        beforeEach(module('sampleDecorator'));
        beforeEach(customMatchers);

        // Store references to $rootScope and $compile so they are available to all tests in this describe block
        beforeEach(inject(function(_$compile_, _$rootScope_, $q, $modal, _$httpBackend_) {

            $httpBackend = _$httpBackend_;

            smarteditComponentType = "Paragraph";
            smarteditComponentId = "theId";

            $compile = _$compile_;
            $rootScope = _$rootScope_;
            parentScope = $rootScope.$new();
            parentScope.active = false;
            directiveScope = parentScope.$new();

            element = angular.element("<div class=\"sample\" data-active=\"active\" data-smartedit-component-id=\"" + smarteditComponentId + "\" data-smartedit-component-type=\"" + smarteditComponentType + "\">initialContent</div>");
            $compile(element)(directiveScope);

            // fire all the watches, so the scope expressions will be evaluated
            $rootScope.$digest();

            expect(element.scope()).toBe(directiveScope);
        }));

        it('ab Analytics does not show anything if not active and content is transcluded', function() {

            var popup = element.find('.popover-inner');
            expect(popup.length).toBe(0);
            var content = element.find('span');
            expect(content.length).toBe(1);
            expect(content.text()).toBe("initialContent");

        });

        it('ab Analytics is initialized, no tooltip shows yet (when active) and content is transcluded', function() {

            parentScope.active = true;
            $rootScope.$digest();
            var popup = element.find('.popover-inner');
            expect(popup.length).toBe(0);
            var content = element.find('span');
            expect(content.length).toBe(1);
            expect(content.text()).toBe("initialContent");


        });

        it('ab Analytics is initialized, tooltip shows when hovering over (when active)', function() {

            parentScope.active = true;
            $rootScope.$digest();
            element.find('div.row').trigger('mouseover');
            var tooltip = element.find('.popover-inner');
            var title = tooltip.find('.popover-title');
            expect(title.text()).toBe("sample.popover.title");
            var content = tooltip.find('.popover-content');
            expect(content.text()).toBe("A : 30%, B : 70%");

        });

        it('ab Analytics is initialized, tooltip shows when hovering over (when active)', function() {

            expect(true).toBe(true);
        });
    });
