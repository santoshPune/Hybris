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
describe('inner featureService', function() {

    var $log, gatewayProxy, decoratorService, contextualMenuService, FeatureServiceInterface, featureService, renderService;

    beforeEach(customMatchers);

    beforeEach(module('featureServiceModule', function($provide) {

        renderService = jasmine.createSpyObj('renderService', ['_resizeEmptySlots']);
        $provide.value('renderService', renderService);

        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener']);
        $provide.value('gatewayFactory', gatewayFactory);

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);

        decoratorService = jasmine.createSpyObj('decoratorService', ['enable', 'disable']);
        $provide.value('decoratorService', decoratorService);

        contextualMenuService = jasmine.createSpyObj('contextualMenuService', ['addItems', 'removeItemByKey']);
        $provide.value('contextualMenuService', contextualMenuService);

        $log = jasmine.createSpyObj('$log', ['warn']);
        $provide.value('$log', $log);

    }));

    beforeEach(inject(function(_FeatureServiceInterface_, _featureService_) {
        featureService = _featureService_;
        FeatureServiceInterface = _FeatureServiceInterface_;
    }));

    it('extends FeatureServiceInterface', function() {
        expect(featureService instanceof FeatureServiceInterface).toBe(true);
    });
    it('initializes and invokes gatewayProxy', function() {
        expect(featureService.gatewayId).toBe("featureService");
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(featureService);
    });

    it('does not reimplement register', function() {
        expect(featureService.register).toBe(FeatureServiceInterface.prototype.register);
    });

    it('leaves _registerAliases unimplemented', function() {
        expect(featureService._registerAliases).toBeEmptyFunction();
    });

    it('leaves addToolbarItem unimplemented', function() {
        expect(featureService.addToolbarItem).toBeEmptyFunction();
    });


    it('GIVEN that feature alias is found in the same frame, THEN _remoteEnablingFromInner will call the enabling callback', function() {

        var enablingCallback = jasmine.createSpy('enablingCallback');
        var disablingCallback = jasmine.createSpy('disablingCallback');

        featureService.featuresToAlias = {
            'key1': {
                enablingCallback: enablingCallback,
                disablingCallback: disablingCallback
            }
        };

        featureService._remoteEnablingFromInner('key1');

        expect(enablingCallback).toHaveBeenCalled();
        expect(disablingCallback).not.toHaveBeenCalled();
        expect($log.warn).not.toHaveBeenCalled();
    });

    it('GIVEN that feature alias is not found in the same frame, THEN _remoteEnablingFromInner will raise a warning', function() {

        var enablingCallback = jasmine.createSpy('enablingCallback');
        var disablingCallback = jasmine.createSpy('disablingCallback');

        featureService.featuresToAlias = {
            'key1': {
                enablingCallback: enablingCallback,
                disablingCallback: disablingCallback
            }
        };

        featureService._remoteEnablingFromInner('key2');

        expect(enablingCallback).not.toHaveBeenCalled();
        expect(disablingCallback).not.toHaveBeenCalled();
        expect($log.warn).toHaveBeenCalledWith('could not enable feature named key2, it was not found in the iframe');
    });

    it('GIVEN that feature alias is found in the same frame, THEN _remoteDisablingFromInner will call the disabling callback', function() {

        var enablingCallback = jasmine.createSpy('enablingCallback');
        var disablingCallback = jasmine.createSpy('disablingCallback');

        featureService.featuresToAlias = {
            'key1': {
                enablingCallback: enablingCallback,
                disablingCallback: disablingCallback
            }
        };

        featureService._remoteDisablingFromInner('key1');

        expect(enablingCallback).not.toHaveBeenCalled();
        expect(disablingCallback).toHaveBeenCalled();
        expect($log.warn).not.toHaveBeenCalled();
    });

    it('GIVEN that feature alias is not found in the same frame, THEN _remoteDisablingFromInner will raise a warning', function() {

        var enablingCallback = jasmine.createSpy('enablingCallback');
        var disablingCallback = jasmine.createSpy('disablingCallback');

        featureService.featuresToAlias = {
            'key1': {
                enablingCallback: enablingCallback,
                disablingCallback: disablingCallback
            }
        };

        featureService._remoteDisablingFromInner('key2');

        expect(enablingCallback).not.toHaveBeenCalled();
        expect(disablingCallback).not.toHaveBeenCalled();
        expect($log.warn).toHaveBeenCalledWith('could not disable feature named key2, it was not found in the iframe');
    });

    it('addDecorator will delegate to decoratorService and prepare callbacks with decoratorService.enable and decoratorService.disable functions', function() {

        spyOn(featureService, 'register').andReturn();

        var config = {
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey',
        };

        featureService.addDecorator(config);



        expect(featureService.register.calls.length).toBe(1);
        expect(featureService.register).toHaveBeenCalledWith({
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey',
            enablingCallback: jasmine.any(Function),
            disablingCallback: jasmine.any(Function)
        });

        var subconfig = featureService.register.calls[0].args[0];

        expect(decoratorService.enable).not.toHaveBeenCalled();
        expect(decoratorService.disable).not.toHaveBeenCalled();

        subconfig.enablingCallback();

        expect(decoratorService.enable).toHaveBeenCalledWith('somekey');
        expect(decoratorService.disable).not.toHaveBeenCalled();

        subconfig.disablingCallback();

        expect(decoratorService.enable.calls.length).toBe(1);
        expect(decoratorService.disable).toHaveBeenCalledWith('somekey');

    });

    it('addContextualMenuButton will call register and add contextualMenuService.addItems and contextualMenuService.removeItemByKey into the callbacks', function() {

        spyOn(featureService, 'register').andReturn();

        var button = {
            key: 'somekey',
            regexpKeys: ['someregexpKey', 'strictType'],
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey',
            i18nkey: 'somei18nKey',
            condition: jasmine.createSpy("condition"),
            callback: jasmine.createSpy("callback"),
            displayClass: 'displayClass1 displayClass2',
            iconIdle: 'pathToIconIdle',
            iconNonIdle: 'pathToIconNonIdle',
            smallIcon: 'pathToSmallIcon'
        };

        featureService.addContextualMenuButton(button);

        expect(featureService.register.callCount).toBe(1);
        expect(featureService.register).toHaveBeenCalledWith({
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey',
            enablingCallback: jasmine.any(Function),
            disablingCallback: jasmine.any(Function)
        });

        var subconfig = featureService.register.calls[0].args[0];

        expect(contextualMenuService.addItems).not.toHaveBeenCalled();
        expect(contextualMenuService.removeItemByKey).not.toHaveBeenCalled();

        subconfig.enablingCallback();

        expect(contextualMenuService.addItems).toHaveBeenCalledWith({
            someregexpKey: [{
                key: 'somekey',
                i18nkey: 'somei18nKey',
                condition: button.condition,
                callback: button.callback,
                displayClass: 'displayClass1 displayClass2',
                iconIdle: 'pathToIconIdle',
                iconNonIdle: 'pathToIconNonIdle',
                smallIcon: 'pathToSmallIcon'
            }],
            strictType: [{
                key: 'somekey',
                i18nkey: 'somei18nKey',
                condition: button.condition,
                callback: button.callback,
                displayClass: 'displayClass1 displayClass2',
                iconIdle: 'pathToIconIdle',
                iconNonIdle: 'pathToIconNonIdle',
                smallIcon: 'pathToSmallIcon'
            }]
        });

        expect(contextualMenuService.removeItemByKey).not.toHaveBeenCalled();

        subconfig.disablingCallback();

        expect(contextualMenuService.addItems.callCount).toBe(1);
        expect(contextualMenuService.removeItemByKey).toHaveBeenCalledWith('somekey');
    });

    describe('addSlotContextualMenuButton', function() {

        var button, expectedFeatureCall, expectedContextualMenuServiceCall;

        beforeEach(function() {
            button = {
                key: 'somekey',
                regexpKeys: ['someregexpKey', 'strictType'],
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                i18nkey: 'somei18nKey',
                condition: jasmine.createSpy("condition"),
                callback: jasmine.createSpy("callback"),
                displayClass: 'displayClass1 displayClass2',
                iconIdle: 'pathToIconIdle',
                iconNonIdle: 'pathToIconNonIdle',
                smallIcon: 'pathToSmallIcon'
            };

            expectedFeatureCall = {
                key: 'somekey',
                nameI18nKey: 'somenameI18nKey',
                descriptionI18nKey: 'somedescriptionI18nKey',
                enablingCallback: jasmine.any(Function),
                disablingCallback: jasmine.any(Function)
            };

            expectedContextualMenuServiceCall = {
                someregexpKey: [{
                    key: 'somekey',
                    i18nkey: 'somei18nKey',
                    condition: button.condition,
                    callback: button.callback,
                    displayClass: 'displayClass1 displayClass2',
                    iconIdle: 'pathToIconIdle',
                    iconNonIdle: 'pathToIconNonIdle',
                    smallIcon: 'pathToSmallIcon'
                }],
                strictType: [{
                    key: 'somekey',
                    i18nkey: 'somei18nKey',
                    condition: button.condition,
                    callback: button.callback,
                    displayClass: 'displayClass1 displayClass2',
                    iconIdle: 'pathToIconIdle',
                    iconNonIdle: 'pathToIconNonIdle',
                    smallIcon: 'pathToSmallIcon'
                }]
            };

            spyOn(featureService, 'register').andReturn();
            featureService.addContextualMenuButton(button);
        });

        it('should call register', function() {

            expect(featureService.register.callCount).toBe(1);
            expect(featureService.register).toHaveBeenCalledWith(expectedFeatureCall);

            expect(contextualMenuService.addItems).not.toHaveBeenCalled();
            expect(contextualMenuService.removeItemByKey).not.toHaveBeenCalled();
        });

        it('should add template by enabling callback', function() {
            var subconfig = featureService.register.calls[0].args[0];

            subconfig.enablingCallback();

            expect(contextualMenuService.addItems).toHaveBeenCalledWith(expectedContextualMenuServiceCall);
            expect(contextualMenuService.removeItemByKey).not.toHaveBeenCalled();
        });

        it('should remove template by disabling callback', function() {
            var subconfig = featureService.register.calls[0].args[0];
            subconfig.enablingCallback();
            subconfig.disablingCallback();

            expect(contextualMenuService.addItems.callCount).toBe(1);
            expect(contextualMenuService.removeItemByKey).toHaveBeenCalledWith('somekey');
        });

    });

});
