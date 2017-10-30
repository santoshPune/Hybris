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
describe('outer storage service', function() {

    var $q, $rootScope, $cookies, storageService;

    beforeEach(customMatchers);

    beforeEach(module('gatewayProxyModule', function($provide) {

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);
    }));

    beforeEach(module('storageServiceModule', function($provide) {

        $cookies = jasmine.createSpyObj('$cookies', ['get', 'put', 'remove']);
        $provide.value('$cookies', $cookies);
    }));

    beforeEach(inject(function(_$q_, _$rootScope_, _storageService_) {

        $q = _$q_;
        $rootScope = _$rootScope_;
        storageService = _storageService_;

    }));

    it('initialized by gatewayProxy', function() {

        expect(storageService.gatewayId).toBe("storage");
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(storageService, ['isInitialized', 'storeAuthToken', 'getAuthToken', 'removeAuthToken', 'removeAllAuthTokens', 'storePrincipalIdentifier', 'getPrincipalIdentifier', 'removePrincipalIdentifier', 'getValueFromCookie']);
    });

    it('removeAllAuthTokens will remove from smartedit-sessions cookie', function() {

        storageService.removeAllAuthTokens();

        expect($cookies.remove).toHaveBeenCalledWith("smartedit-sessions");
    });

    it('removeAuthToken for entryPoint1 will remove the entry from smartedit-sessions cookie', function() {

        authTokens = {
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            },
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            }
        };
        $cookies.get.andReturn(btoa(JSON.stringify(authTokens)));

        storageService.removeAuthToken("entryPoint1");

        expect($cookies.put).toHaveBeenCalledWith("smartedit-sessions", btoa(JSON.stringify({
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            }
        })));
    });

    it('removeAuthToken for entryPoint1 will remove the entire smartedit-sessions cookie', function() {

        authTokens = {
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            }
        };
        $cookies.get.andReturn(btoa(JSON.stringify(authTokens)));

        storageService.removeAuthToken("entryPoint1");

        expect($cookies.remove).toHaveBeenCalledWith("smartedit-sessions");
    });


    it('getAuthToken will get the auth token specific to the given entry point from smartedit-sessions cookie', function() {

        authTokens = {
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            },
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            }
        };
        $cookies.get.andReturn(btoa(JSON.stringify(authTokens)));

        expect(storageService.getAuthToken("entryPoint2")).toEqual({
            access_token: 'access_token2',
            token_type: 'bearer'
        });

        expect($cookies.get).toHaveBeenCalledWith("smartedit-sessions");
    });

    it('storeAuthToken will store the given auth token in a new map with the entryPoint as the key in smartedit-sessions cookie', function() {

        $cookies.get.andReturn(null);

        storageService.storeAuthToken("entryPoint1", {
            access_token: 'access_token1',
            token_type: 'bearer'
        });

        expect($cookies.put).toHaveBeenCalledWith("smartedit-sessions", btoa(JSON.stringify({
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            }
        })));
    });

    it('storeAuthToken will store the given auth token in existing map with the entryPoint as the key in pre-existing smartedit-sessions cookie', function() {

        authTokens = {
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            }
        };
        $cookies.get.andReturn(btoa(JSON.stringify(authTokens)));

        storageService.storeAuthToken("entryPoint1", {
            access_token: 'access_token1',
            token_type: 'bearer'
        });

        expect($cookies.put).toHaveBeenCalledWith("smartedit-sessions", btoa(JSON.stringify({
            entryPoint2: {
                access_token: 'access_token2',
                token_type: 'bearer'
            },
            entryPoint1: {
                access_token: 'access_token1',
                token_type: 'bearer'
            }
        })));
    });

    it('storePrincipalIdentifier  will store the given principalUID in pre-existing smartedit-sessions cookie', function() {

        principalUID = {
            'principal-uid': 'admin'
        };
        $cookies.get.andReturn(btoa(JSON.stringify(principalUID)));

        storageService.storePrincipalIdentifier('admin');

        expect($cookies.put).toHaveBeenCalledWith("smartedit-sessions", btoa(JSON.stringify(principalUID)));
    });

    it('removePrincipalIdentifier  will remove the existing principalUID from smartedit-sessions cookie', function() {

        principalUID = 'admin';
        principalMap = {
            'principal-uid': principalUID
        };
        $cookies.get.andReturn(btoa(JSON.stringify(principalMap)));

        storageService.removePrincipalIdentifier();

        expect($cookies.remove).toHaveBeenCalledWith("smartedit-sessions");
    });

    it('getPrincipalIdentifier  will get the existing principalUID from smartedit-sessions cookie', function() {

        principalUID = 'admin';
        principalMap = {
            'principal-uid': principalUID
        };
        $cookies.get.andReturn(btoa(JSON.stringify(principalMap)));

        // Act
        var value = storageService.getPrincipalIdentifier();

        // Assert
        expect($cookies.get).toHaveBeenCalled();
        expect(value).toBe(principalUID);
    });

    it('IF no cookie is stored WHEN getValueFromCookie is called THEN null is returned', function() {
        // Arrange
        $cookies.get.andReturn(null);

        // Act
        var value = storageService.getValueFromCookie('someCookie', true);

        // Assert
        expect($cookies.get).toHaveBeenCalledWith('someCookie');
        expect(value).toBe(null);
    });

    it('IF cookie value is not JSON parsable WHEN getValueFromCookie is called THEN null is returned', function() {
        // Arrange
        $cookies.get.andReturn("{");

        // Act
        var value = storageService.getValueFromCookie('someCookie', true);

        // Assert
        expect($cookies.get).toHaveBeenCalledWith('someCookie');
        expect(value).toBe(null);
    });


    it('IF a cookie is stored and its value is not encoded WHEN getValueFromCookie is called THEN the value is returned', function() {
        // Arrange
        var rawValue = "se.none";
        $cookies.get.andReturn(JSON.stringify(rawValue));

        // Act
        var value = storageService.getValueFromCookie('someCookie', false);

        // Assert
        expect($cookies.get).toHaveBeenCalledWith('someCookie');
        expect(value).toBe(rawValue);
    });

    it('IF no cookie is stored and its value is encoded WHEN getValueFromCookie is called THEN the un-encoded value is returned', function() {
        // Arrange
        var rawValue = "se.none";
        var encodedValue = "InNlLm5vbmUi";
        $cookies.get.andReturn(encodedValue);

        // Act
        var value = storageService.getValueFromCookie('someCookie', true);

        // Assert
        expect($cookies.get).toHaveBeenCalledWith('someCookie');
        expect(value).toBe(rawValue);
    });

    it('WHEN putValueInCookie is called and the encode flag is not set THEN the un-encoded value is stored', function() {
        // Arrange
        var rawValue = {
            key: "se.none"
        };
        $cookies.put.andReturn(null);

        // Act
        storageService.putValueInCookie('someCookie', rawValue, false);

        // Assert
        expect($cookies.put).toHaveBeenCalledWith('someCookie', JSON.stringify(rawValue));
    });

    it('WHEN putValueInCookie is called and the encode flag is set THEN the encoded value is stored', function() {
        // Arrange
        var rawValue = '"se.none"';
        var encodedValue = "Ilwic2Uubm9uZVwiIg==";
        $cookies.put.andReturn(null);

        // Act
        storageService.putValueInCookie('someCookie', rawValue, true);

        // Assert
        expect($cookies.put).toHaveBeenCalledWith('someCookie', encodedValue);
    });
});
