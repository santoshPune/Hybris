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
describe('seErrorsList', function() {
    var parentScope, scope, element, ctrl;

    beforeEach(customMatchers);
    beforeEach(module('coretemplates'));

    beforeEach(module('seErrorsListModule'));

    beforeEach(inject(function($compile, $rootScope) {
        parentScope = $rootScope.$new();
        $.extend(parentScope, {
            errors: [{
                subject: 'code',
                message: 'some code error'
            }, {
                subject: 'code',
                message: 'some other code error'
            }, {
                subject: 'description',
                message: 'some description error'
            }],
            subject: 'code'
        });

        element = $compile('<se-errors-list ' +
            'data-errors="errors" ' +
            'data-subject="subject">' +
            '</se-errors-list>')(parentScope);
        parentScope.$digest();

        scope = element.isolateScope();
        ctrl = scope.ctrl;
    }));

    describe('template', function() {
        it('should display errors for given subject', function() {
            expect(element.text().trim()).toContain('some code error');
            expect(element.text().trim()).toContain('some other code error');
            expect(element.text().trim()).not.toContain('some description error');
        });
    });
});
