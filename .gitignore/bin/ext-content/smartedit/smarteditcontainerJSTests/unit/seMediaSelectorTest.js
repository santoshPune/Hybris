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
describe('seMediaSelectorController', function() {
    var ctrl;

    beforeEach(customMatchers);

    beforeEach(module('seMediaSelectorModule'));

    beforeEach(inject(function($rootScope, $compile, _$q_, _$controller_) {
        ctrl = _$controller_('seMediaSelectorController', {
            $scope: $rootScope.$new()
        });
    }));

    describe('onDelete', function() {
        var selectMock;
        var eventMock;

        beforeEach(function() {
            selectMock = {
                selected: 'some value'
            };
            eventMock = jasmine.createSpyObj('$event', ['stopPropagation', 'preventDefault']);
            ctrl.onDelete(selectMock, eventMock);
        });

        it('should clear the selected value', function() {
            expect(selectMock.selected).toBeUndefined();
        });

        it('should stop the event from propogating and prevent the default behaviour', function() {
            expect(eventMock.stopPropagation).toHaveBeenCalled();
            expect(eventMock.preventDefault).toHaveBeenCalled();
        });
    });
});
