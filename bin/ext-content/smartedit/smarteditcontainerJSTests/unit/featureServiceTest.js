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
describe('outer featureService', function() {

    var gatewayProxy, toolbarServiceFactory, FeatureServiceInterface, featureService;

    beforeEach(customMatchers);

    beforeEach(module('featureServiceModule', function($provide) {

        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener']);
        $provide.value('gatewayFactory', gatewayFactory);

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);

        toolbarServiceFactory = jasmine.createSpyObj('toolbarServiceFactory', ['getToolbarService']);
        $provide.value('toolbarServiceFactory', toolbarServiceFactory);

        toolbarService = jasmine.createSpyObj('toolbarService', ['addItems', 'removeItemByKey']);
        toolbarServiceFactory.getToolbarService.andReturn(toolbarService);
        $provide.value('toolbarService', toolbarService);

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
        expect(featureService.features).toEqual([]);
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(featureService, ['_registerAliases', 'addToolbarItem', 'register', 'enable', 'disable', '_remoteEnablingFromInner', '_remoteDisablingFromInner', 'addDecorator', 'addContextualMenuButton']);
    });

    it('does not reimplement register', function() {
        expect(featureService.register).toBe(FeatureServiceInterface.prototype.register);
    });

    it('leaves addDecorator unimplemented', function() {
        expect(featureService.addDecorator).toBeEmptyFunction();
    });

    it('leaves addContextualMenuButton unimplemented', function() {
        expect(featureService.addContextualMenuButton).toBeEmptyFunction();
    });

    it('_registerAliases pushes to the features list and assigns an id', function() {
        featureService._registerAliases({
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey'
        });
        expect(featureService.features).toEqual([{
            id: 'c29tZWtleQ==',
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey'
        }]);
    });

    it('_registerAliases does not push a feature it already contains', function() {
        featureService._registerAliases({
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey'
        });
        featureService._registerAliases({
            key: 'someOtherKey',
            nameI18nKey: 'someOthernameI18nKey',
            descriptionI18nKey: 'someOthertdescriptionI18nKey'
        });
        featureService._registerAliases({
            key: 'somekey',
            nameI18nKey: 'fsgdfgwrteg',
            descriptionI18nKey: 'sdfgstgrwwwwr'
        });

        expect(featureService.features).toEqual([{
            id: 'c29tZWtleQ==',
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey'
        }, {
            id: 'c29tZU90aGVyS2V5',
            key: 'someOtherKey',
            nameI18nKey: 'someOthernameI18nKey',
            descriptionI18nKey: 'someOthertdescriptionI18nKey'
        }]);
    });

    it('addToolbarItem will keep a reference on a toolbar in the feature callbacks', function() {

        spyOn(featureService, 'register').andReturn();

        var configuration = {
            toolbarId: 'sometoolbarId',
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey',
            icons: ['url1', 'url2'],
            type: 'HYBRID_ACTION',
            include: 'sometemplate.html',
            callback: jasmine.createSpy("callback")
        };

        featureService.addToolbarItem(configuration);

        expect(featureService.register.callCount).toBe(1);
        expect(featureService.register).toHaveBeenCalledWith({
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey',
            enablingCallback: jasmine.any(Function),
            disablingCallback: jasmine.any(Function)
        });

        expect(toolbarServiceFactory.getToolbarService).toHaveBeenCalledWith('sometoolbarId');

        var subconfig = featureService.register.calls[0].args[0];

        expect(toolbarService.addItems).not.toHaveBeenCalled();
        expect(toolbarService.removeItemByKey).not.toHaveBeenCalled();

        subconfig.enablingCallback();

        expect(toolbarService.addItems).toHaveBeenCalledWith([{
            key: 'somekey',
            toolbarId: 'sometoolbarId',
            icons: ['url1', 'url2'],
            type: 'HYBRID_ACTION',
            include: 'sometemplate.html',
            callback: configuration.callback
        }]);

        expect(toolbarService.removeItemByKey).not.toHaveBeenCalled();

        subconfig.disablingCallback();

        expect(toolbarService.addItems.callCount).toBe(1);
        expect(toolbarService.removeItemByKey).toHaveBeenCalledWith('somekey');
    });

    it('getFeatureKeys fetches the list of features keys', function() {
        featureService.features = [{
            key: 'key1',
            name: 'dfgsdfg'
        }, {
            key: 'key2',
            name: 'fgsfd'
        }, {
            key: 'key3',
            name: 'afgfdgsdf'
        }];
        expect(featureService.getFeatureKeys()).toEqual(['key1', 'key2', 'key3']);
    });
});
