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
describe('test sakExecutor', function() {

    var $q;
    var sakExecutor, decorators, compiler, compiled;

    beforeEach(customMatchers);

    beforeEach(module('sakExecutorDecorator', function($provide) {
        $provide.constant("NUM_SE_SLOTS", 5);
        smarteditComponentType = "smarteditComponentType";
        smarteditComponentId = "smarteditComponentId";

        decoratorService = jasmine.createSpyObj('decoratorService', ['getDecoratorsForComponent']);
        decorators = ['decorator1', 'decorator2'];

        $provide.value('decoratorService', decoratorService);

        compiler = jasmine.createSpyObj('compiler', ['compile']);
        compiled = angular.element("<div>compiled</div>").html();
        compiler.compile.andReturn(compiled);
        $provide.value('$compile', function(template, transcludeFn) {
            return compiler.compile(template, transcludeFn);
        });
    }));

    beforeEach(inject(function(_sakExecutor_, _$q_, _$rootScope_) {
        sakExecutor = _sakExecutor_;
        $q = _$q_;
    }));

    beforeEach(function() {
        decoratorService.getDecoratorsForComponent.andReturn(decorators);
    });

    it('sakExecutor.wrapDecorators fetches eligible decorators for given component type and compiles a stack of those decorators around the clone + the sakDecorator', function() {

        var transcludeFn = function() {};

        var result = sakExecutor.wrapDecorators(transcludeFn, smarteditComponentId, smarteditComponentType);

        expect(result).toBe(compiled);
        expect(decoratorService.getDecoratorsForComponent).toHaveBeenCalledWith(smarteditComponentType);
        expect(compiler.compile).toHaveBeenCalledWith("<div class='decorator2' " +
            "data-active='active' " +
            "data-smartedit-component-id='{{smarteditComponentId}}' " +
            "data-smartedit-component-type='{{smarteditComponentType}}' " +
            "data-smartedit-container-id='{{smarteditContainerId}}' " +
            "data-smartedit-container-type='{{smarteditContainerType}}'>" +
            "<div class='decorator1' " +
            "data-active='active' " +
            "data-smartedit-component-id='{{smarteditComponentId}}' " +
            "data-smartedit-component-type='{{smarteditComponentType}}' " +
            "data-smartedit-container-id='{{smarteditContainerId}}' " +
            "data-smartedit-container-type='{{smarteditContainerType}}'>" +
            "<div data-ng-transclude></div></div></div>", transcludeFn);
    });

    xit('areAllDecoratorsProcessed will only return true when the number of processed decorators is greater or equal to the reset value', function() {

        sakExecutor.resetCounters(3);
        expect(sakExecutor.areAllDecoratorsProcessed()).toBe(false);

        sakExecutor.markDecoratorProcessed("type1", "id1");
        expect(sakExecutor.areAllDecoratorsProcessed()).toBe(false);

        sakExecutor.markDecoratorProcessed("type1", "id2");
        expect(sakExecutor.areAllDecoratorsProcessed()).toBe(false);

        //entering same value has no effect
        sakExecutor.markDecoratorProcessed("type1", "id2");
        expect(sakExecutor.areAllDecoratorsProcessed()).toBe(false);

        sakExecutor.markDecoratorProcessed("type2", "id3");
        expect(sakExecutor.areAllDecoratorsProcessed()).toBe(true);
    });

});
