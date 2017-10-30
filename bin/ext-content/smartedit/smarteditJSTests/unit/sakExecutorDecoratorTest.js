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
    'Unit integration test of sakExecutorDecorator directive',
    function() {
        var decorators, decoratorService, componentHandlerService, sakExecutor, parent;
        var $rootScope, $compile, parentScope, directiveScope, element, smarteditComponentType, smarteditComponentId;

        beforeEach(module('ui.bootstrap'));
        beforeEach(module('coretemplates'));

        beforeEach(module('sakExecutorDecorator', function($provide) {
            decorators = ['decorator1', 'decorator2'];
            decoratorService = jasmine.createSpyObj('decoratorService', ['getDecoratorsForComponent']);
            $provide.value('decoratorService', decoratorService);
            $provide.constant("NUM_SE_SLOTS", 1);

            componentHandlerService = jasmine.createSpyObj('componentHandlerService', ['getOriginalComponent', 'getParent']);
            parent = jasmine.createSpyObj('parent', ['attr']);
            var realElement = {};
            componentHandlerService.getOriginalComponent.andReturn(realElement);
            componentHandlerService.getParent.andReturn(parent);
            $provide.value('componentHandlerService', componentHandlerService);

        }));

        beforeEach(customMatchers);

        // Store references to $rootScope and $compile so they are available to all tests in this describe block
        beforeEach(inject(function(_$rootScope_, _sakExecutor_, _$compile_) {
            $compile = _$compile_;
            sakExecutor = _sakExecutor_;
            spyOn(sakExecutor, 'markDecoratorProcessed').andCallThrough();
            spyOn(sakExecutor, 'registerScope').andCallThrough();
            smarteditComponentType = "ContentSlot";
            smarteditComponentId = "theId";
            $rootScope = _$rootScope_;
            parentScope = $rootScope.$new();
            parentScope.active = false;
            directiveScope = parentScope.$new();
        }));

        function compileDirective() {
            element = angular.element("<div class=\"smartEditComponentX\" data-smartedit-component-id=\"" + smarteditComponentId + "\" data-smartedit-component-type=\"" + smarteditComponentType + "\">initialContent</div>");
            $compile(element)(directiveScope);
            // fire all the watches, so the scope expressions will be evaluated
            $rootScope.$digest();
            expect(element.scope()).toBe(directiveScope);
            expect(sakExecutor.registerScope).toHaveBeenCalled();
        }

        it('sakExecutor stacks decorators in this order : decorator2, decorator1', function() {
            decoratorService.getDecoratorsForComponent.andReturn(decorators);
            compileDirective();
            expect(element.find('> div.decorator2').length).toBe(1);
            expect(element.find('> div.decorator2 > div.decorator1').length).toBe(1);

            expect(sakExecutor.markDecoratorProcessed).toHaveBeenCalledWith('ContentSlot', 'theId');
        });

        it('sakExecutor WILL process all decorators', function() {
            decoratorService.getDecoratorsForComponent.andReturn(decorators);
            compileDirective();
            expect(sakExecutor.areAllDecoratorsProcessed()).toBe(true);
        });

    });
