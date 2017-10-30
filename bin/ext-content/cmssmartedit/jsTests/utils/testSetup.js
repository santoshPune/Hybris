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
//new file which can be used to add helper methods for testing

//injects rootscope and q for global use
var $rootScope, $q;
var directiveScope, response, element, componentService;
var setupBackendResponse, templateSetup;

var testSetup = inject(function(_$rootScope_, _$q_) {
    $rootScope = _$rootScope_;
    $q = _$q_;
});

var setupDirectiveTest = function() {

    setupBackendResponse = function($httpBackend, uri) {
        response = {};
        $httpBackend.whenGET(uri).respond(response);
    };

    templateSetup = function(template, $compile, $rootScope) {
        directiveScope = $rootScope.$new();
        element = angular.element(template);
        $compile(element)(directiveScope);

        $rootScope.$digest();
        expect(element.scope()).toBe(directiveScope);
        return element;
    };

};
