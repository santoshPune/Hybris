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
describe("Editor Enabler Service: ", function() {

    // Mocks
    var featureService;
    var systemEventService;
    var editorModalService;
    var $q;
    var $rootScope;

    // Service Under Test
    var editorEnablerService;

    // Test Values
    var COMPONENT_ID = "mycomponent";
    var COMPONENT_TYPE = "mytype";

    beforeEach(function() {
        angular.module('gatewayFactoryModule', []);
        angular.module('gatewayProxyModule', []);
        angular.module('featureServiceModule', []);
    });

    beforeEach(module('editorEnablerServiceModule', function($provide) {
        featureService = jasmine.createSpyObj("featureService", ["addContextualMenuButton"]);
        $provide.value("featureService", featureService);

        systemEventService = jasmine.createSpyObj('systemEventService', ['sendAsynchEvent', 'registerEventHandler', 'unRegisterEventHandler']);
        $provide.value('systemEventService', systemEventService);

        editorModalService = {
            open: function() {}
        };
        $provide.value('editorModalService', editorModalService);

    }));
    // Set-up Module Under Test
    beforeEach(inject(function(_editorEnablerService_, _$q_, _$rootScope_) {
        editorEnablerService = _editorEnablerService_;
        $q = _$q_;
        $rootScope = _$rootScope_;
    }));

    // Tests
    it("enableForComponent will add the Edit button to the contextual menu for a specified component types", function() {
        // Act
        editorEnablerService.enableForComponents(["SimpleResponsiveBannerComponent"]);

        // Assert
        expect(featureService.addContextualMenuButton).toHaveBeenCalledWith({
            key: 'se.cms.edit',
            nameI18nKey: 'contextmenu.nameI18nKey.edit',
            descriptionI18nKey: 'contextmenu.descriptionI18n.edit',
            regexpKeys: ['SimpleResponsiveBannerComponent'],
            displayClass: 'editbutton',
            iconIdle: '/cmssmartedit/images/contextualmenu_edit_off.png',
            iconNonIdle: '/cmssmartedit/images/contextualmenu_edit_on.png',
            smallIcon: '/cmssmartedit/images/contextualmenu_edit_on.png',
            callback: editorEnablerService._editButtonCallback
        });
    });

    it('_editButtonCallback delegates to the editor modal service', function() {
        //// Arrange
        spyOn(editorModalService, 'open').andCallFake(function() {
            var deferred = $q.defer();
            deferred.resolve();
            return deferred.promise;
        });

        // Act
        editorEnablerService._editButtonCallback({
            componentType: COMPONENT_TYPE,
            componentId: COMPONENT_ID
        });
        $rootScope.$digest();

        // Assert
        expect(editorModalService.open).toHaveBeenCalledWith(COMPONENT_TYPE, COMPONENT_ID);
    });

});
