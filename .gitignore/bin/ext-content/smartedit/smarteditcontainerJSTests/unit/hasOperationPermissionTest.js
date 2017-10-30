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
describe('hasOperationPermission', function() {
    var GRANTED_PERMISSION = 'GRANTED_PERMISSION';
    var DENIED_PERMISSION = 'DENIED_PERMISSION';
    var FAILED_OPERATION = 'FAILED_OPERATION';
    var EVENTS = {
        AUTHORIZATION_SUCCESS: 'AUTHORIZATION_SUCCESS'
    };

    var parentScope, scope, element, ctrl;
    var authorizationService, systemEventService;
    var $q;

    beforeEach(customMatchers);
    beforeEach(module('coretemplates'));
    beforeEach(module('hasOperationPermissionModule'));

    beforeEach(module('eventServiceModule', function($provide) {
        systemEventService = jasmine.createSpyObj('systemEventService', ['registerEventHandler', 'unRegisterEventHandler']);
        $provide.value('systemEventService', systemEventService);
        $provide.value('EVENTS', EVENTS);
    }));

    beforeEach(module('authorizationModule', function($provide) {
        $provide.service('authorizationService', function($q) {
            this.canPerformOperation = jasmine.createSpy('canPerformOperation');
            this.canPerformOperation.andCallFake(function(operation) {
                if (operation === FAILED_OPERATION) {
                    return $q.reject();
                }
                return $q.when(operation === GRANTED_PERMISSION);
            });
        });
    }));

    beforeEach(inject(function($compile, $rootScope, _$q_, _authorizationService_) {
        $q = _$q_;
        authorizationService = _authorizationService_;

        parentScope = $rootScope.$new();
        $.extend(parentScope, {
            permissionKey: GRANTED_PERMISSION
        });

        element = $compile('<div ' +
            'data-has-operation-permission="permissionKey">' +
            '<span>Permission Granted</span>' +
            '</div>')(parentScope);
        parentScope.$digest();

        scope = element.isolateScope();
        ctrl = scope.ctrl;
    }));

    it('should call the authorization service when first instantiated', function() {
        expect(authorizationService.canPerformOperation).toHaveBeenCalledWith(GRANTED_PERMISSION);
    });

    it('should transclude the nested element if the given operation is permitted by the user', function() {
        expect(element.find('span')).toExist();
        expect(element.text()).toContain('Permission Granted');
    });

    it('should attach an event listener for the authorization success event', function() {
        expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(EVENTS.AUTHORIZATION_SUCCESS, jasmine.any(Function));
    });

    it('should call the authorization service when the operation is changed', function() {
        parentScope.permissionKey = DENIED_PERMISSION;
        parentScope.$digest();

        expect(authorizationService.canPerformOperation).toHaveBeenCalledWith(DENIED_PERMISSION);
    });

    it('should remove the nested element from the DOM if the given operation is changed a denied permission', function() {
        parentScope.permissionKey = DENIED_PERMISSION;
        parentScope.$digest();

        expect(element.find('span')).not.toExist();
        expect(element.text()).not.toContain('Permission Granted');
    });
});
