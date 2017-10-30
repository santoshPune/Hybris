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
describe('test outer toolbarServiceInterface Module', function() {

    var $rootScope, $log, ToolbarServiceInterface;

    beforeEach(customMatchers);

    beforeEach(module('toolbarInterfaceModule'));
    beforeEach(inject(function(_$rootScope_, _ToolbarServiceInterface_, _$log_) {
        $rootScope = _$rootScope_;
        ToolbarServiceInterface = _ToolbarServiceInterface_;
        $log = _$log_;
    }));

    it('ToolbarServiceInterface declares the expected set of empty functions', function() {
        expect(ToolbarServiceInterface.prototype.addAliases).toBeEmptyFunction();
        expect(ToolbarServiceInterface.prototype.removeItemByKey).toBeEmptyFunction();
        expect(ToolbarServiceInterface.prototype.removeAliasByKey).toBeEmptyFunction();
        expect(ToolbarServiceInterface.prototype.addItemsStyling).toBeEmptyFunction();
        expect(ToolbarServiceInterface.prototype.triggerActionOnInner).toBeEmptyFunction();
    });

    it('ToolbarServiceInterface.addItems converts actions into aliases (key-callback mapping of actions) before appending them by means of addAliases', function() {

        var toolbarService = new ToolbarServiceInterface();
        toolbarService.actions = {};
        toolbarService.aliases = [];

        spyOn(toolbarService, 'addAliases').andCallThrough();
        spyOn(toolbarService, 'getAliases').andCallThrough();

        var callback1 = function() {};
        var callback2 = function() {};

        expect(toolbarService.getAliases()).toEqualData([]);

        // Execution
        toolbarService.addItems([{
            key: 'key1',
            i18nKey: 'i18nKey1',
            callback: callback1,
            icons: 'icons1',
            type: 'type1',
            include: 'include1'
        }]);

        var actionsAfterFirstCall = toolbarService.getItems();

        toolbarService.addItems([{
            key: 'key2',
            i18nKey: 'i18nKey2',
            callback: callback2,
            icons: 'icons2',
            type: 'type2',
            include: 'include2'
        }]);

        var actionsAfterSecondCall = toolbarService.getItems();

        // Tests
        expect(toolbarService.addAliases.calls[0].args[0]).toEqualData([{
            key: 'key1',
            name: 'i18nKey1',
            icons: 'icons1',
            type: 'type1',
            include: 'include1',
            priority: 500,
            section: 'left'
        }]);


        expect(toolbarService.getItems()).toEqualData({
            'key1': callback1
        });

        expect(toolbarService.addAliases.calls[1].args[0]).toEqualData([{
            key: 'key2',
            name: 'i18nKey2',
            icons: 'icons2',
            type: 'type2',
            include: 'include2',
            priority: 500,
            section: 'left'
        }]);


        expect(toolbarService.getItems()).toEqualData({
            'key1': callback1,
            'key2': callback2
        });
    });

    it('addItems logs an error when key is not provided in the configuration', function() {
        // Arrange
        var toolbarService = new ToolbarServiceInterface();

        spyOn($log, 'error');
        spyOn(toolbarService, 'addAliases');

        var callbacks = {
            callback1: function() {}
        };

        // Act
        toolbarService.addItems([{
            i18nKey: 'i18nKey1',
            callback: callbacks.callback1
        }]);

        // Assert
        expect(toolbarService.addAliases).not.toHaveBeenCalled();
        expect($log.error).toHaveBeenCalledWith('addItems() - Cannot add action without key.');
    });

    it('addItems logs an error when a duplicate key is provided in the configuration', function() {
        // Arrange
        var duplicatedKey = 'somekey1';
        var toolbarService = new ToolbarServiceInterface();
        toolbarService.actions = {
            'somekey1': {
                key: duplicatedKey
            }
        };
        toolbarService.aliases = [];

        spyOn($log, 'debug');
        spyOn(toolbarService, 'addAliases');

        var callbacks = {
            callback1: function() {}
        };

        // Act
        toolbarService.addItems([{
            key: duplicatedKey,
            i18nKey: 'i18nKey1',
            callback: callbacks.callback1
        }]);

        // Assert
        expect(toolbarService.addAliases).not.toHaveBeenCalled();
        expect($log.debug).toHaveBeenCalledWith('addItems() - Action already exists in toolbar with key ' + duplicatedKey);
    });

});
