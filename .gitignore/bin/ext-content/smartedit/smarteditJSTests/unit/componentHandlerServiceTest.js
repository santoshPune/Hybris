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
describe('test componentHandlerService', function() {

    var $window, jQueryObject;

    beforeEach(customMatchers);

    beforeEach(module('componentHandlerServiceModule'));

    beforeEach(inject(function(_$window_, _componentHandlerService_) {
        $window = _$window_;
        componentHandlerService = _componentHandlerService_;
        jQueryObject = {};
        spyOn(componentHandlerService, "getFromSelector").andReturn(jQueryObject);
    }));

    it('getPageUID request from the container will return the PageUID from html component\'s class attributes', function() {

        spyOn(componentHandlerService, '_isIframe').andReturn(false);

        jQueryObject = jasmine.createSpyObj("jQueryObject", ["contents"]);
        componentHandlerService.getFromSelector.andReturn(jQueryObject);

        var contents = jasmine.createSpyObj('contents', ['find']);
        jQueryObject.contents.andReturn(contents);

        var body = jasmine.createSpyObj('body', ['attr']);
        contents.find.andReturn(body);

        var contractPrefix = 'smartedit-page-uid-';
        var pageUID = 'page-!@#$%^&*()[]{}\\;:\'"?/.,<>|_+-=1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZpage-';
        var pageName = contractPrefix + pageUID;
        var gibberish1 = '1234567890!@#$%^&*()[]{}\\;:\'"?/.,<>|_+-=';
        var gibberish2 = '!@#$%^&*()[]{}\\;:\'"?/.,<>|_+-=1234567890';
        var attributes = gibberish1 + ' ' + pageName + ' ' + gibberish2;

        body.attr.andReturn(attributes);

        expect(componentHandlerService.getPageUID()).toBe(pageUID);

        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith('iframe');
    });

    it('getPageUID request from the smartedit will return the PageUID from html component\'s class attributes', function() {

        spyOn(componentHandlerService, '_isIframe').andReturn(true);

        var body = jasmine.createSpyObj('body', ['attr']);
        componentHandlerService.getFromSelector.andReturn(body);

        var contractPrefix = 'smartedit-page-uid-';
        var pageUID = 'page-!@#$%^&*()[]{}\\;:\'"?/.,<>|_+-=1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZpage-';
        var pageName = contractPrefix + pageUID;
        var gibberish1 = '1234567890!@#$%^&*()[]{}\\;:\'"?/.,<>|_+-=';
        var gibberish2 = '!@#$%^&*()[]{}\\;:\'"?/.,<>|_+-=1234567890';
        var attributes = gibberish1 + ' ' + pageName + ' ' + gibberish2;

        body.attr.andReturn(attributes);

        expect(componentHandlerService.getPageUID()).toBe(pageUID);

        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith('body');
    });

    it('getOverlay will get a jQuery reference on the overlay by id', function() {

        expect(componentHandlerService.getOverlay()).toBe(jQueryObject);
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith("#smarteditoverlay");
    });

    it('getComponent will get a jQuery reference on an object containing the given class and having the given id and type', function() {

        expect(componentHandlerService.getComponent('theid', 'thetype', 'myclass')).toBe(jQueryObject);
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(".myclass[data-smartedit-component-id='theid'][data-smartedit-component-type='thetype']");
    });

    it('getComponentUnderSlot will get a jQuery reference on an object containing the given class and having the given id and type', function() {

        expect(componentHandlerService.getComponentUnderSlot('theComponentId', 'thetype', 'theSlotId', 'myclass')).toBe(jQueryObject);
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith("[data-smartedit-component-id='theSlotId'][data-smartedit-component-type='ContentSlot'] > .myclass[data-smartedit-component-id='theComponentId'][data-smartedit-component-type='thetype']");
    });

    it('getOriginalComponent will get a jQuery reference on an object containing the smartEditComponent class and having the given id and type', function() {

        expect(componentHandlerService.getOriginalComponent('theid', 'thetype')).toBe(jQueryObject);
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(".smartEditComponent[data-smartedit-component-id='theid'][data-smartedit-component-type='thetype']");
    });

    it('getOriginalComponentWithinSlot will get a jQuery reference on an object containing the smartEditComponent class and having the given id and type within a given slot ID', function() {

        expect(componentHandlerService.getOriginalComponentWithinSlot('theid', 'thetype', 'theSlotId')).toBe(jQueryObject);
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith("[data-smartedit-component-id='theSlotId'][data-smartedit-component-type='ContentSlot'] > .smartEditComponent[data-smartedit-component-id='theid'][data-smartedit-component-type='thetype']");
    });

    it('getComponentInOverlay will get a jQuery reference on an object containing the smartEditComponentX class and having the given id and type', function() {

        expect(componentHandlerService.getComponentInOverlay('theid', 'thetype')).toBe(jQueryObject);
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(".smartEditComponentX[data-smartedit-component-id='theid'][data-smartedit-component-type='thetype']");
    });

    it('getParent of an original component will fetch closest parent in the storefront layer', function() {

        var parent = {};
        var component = jasmine.createSpyObj('component', ['attr', 'hasClass', 'closest']);
        component.attr.andCallFake(function(attribute) {
            if (attribute === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attribute === 'data-smartedit-component-type') {
                return 'thetype';
            }
        });

        component.hasClass.andCallFake(function(className) {
            if (className === 'smartEditComponent') {
                return true;
            } else if (className === 'smartEditComponentX') {
                return false;
            } else {
                return null;
            }
        });

        component.closest.andReturn(parent);
        expect(componentHandlerService.getParent(component)).toBe(parent);
        expect(component.closest).toHaveBeenCalledWith(".smartEditComponent[data-smartedit-component-id!='theid']");
    });

    it('getParent of an overlay component will fetch closest parent in the overlay', function() {

        var parent = {};
        var component = jasmine.createSpyObj('component', ['attr', 'hasClass', 'closest']);
        component.attr.andCallFake(function(attribute) {
            if (attribute === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attribute === 'data-smartedit-component-type') {
                return 'thetype';
            }
        });

        component.hasClass.andCallFake(function(className) {
            if (className === 'smartEditComponent') {
                return false;
            } else if (className === 'smartEditComponentX') {
                return true;
            } else {
                return null;
            }
        });

        component.closest.andReturn(parent);
        expect(componentHandlerService.getParent(component)).toBe(parent);
        expect(component.closest).toHaveBeenCalledWith(".smartEditComponentX[data-smartedit-component-id!='theid']");
    });

    it('getParent of a component from an unkown layer will throw an exception', function() {

        var parent = {};
        var component = jasmine.createSpyObj('component', ['attr', 'hasClass', 'closest']);
        component.attr.andCallFake(function(attribute) {
            if (attribute === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attribute === 'data-smartedit-component-type') {
                return 'thetype';
            }
        });

        component.hasClass.andReturn(null);

        component.closest.andReturn(parent);
        expect(function() {
            componentHandlerService.getParent(component);
        }).toThrow("componentHandlerService.getparent.error.component.from.unknown.layer");
    });


    it('setId will set the data-smartedit-component-id field of a given component', function() {

        var originalComponent = {
            key: 'value'
        };
        var component = jasmine.createSpyObj('component', ['attr']);
        componentHandlerService.getFromSelector.andReturn(component);
        componentHandlerService.setId(originalComponent, 'theid');

        expect(component.attr).toHaveBeenCalledWith('data-smartedit-component-id', 'theid');
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(originalComponent);
    });

    it('getId will get the data-smartedit-component-id field of a given component', function() {

        var originalComponent = {
            key: 'value'
        };
        var component = jasmine.createSpyObj('component', ['attr']);
        component.attr.andReturn('theid');

        componentHandlerService.getFromSelector.andReturn(component);
        expect(componentHandlerService.getId(originalComponent)).toBe('theid');
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(originalComponent);
    });

    it('setType will set the data-smartedit-component-type field of a given component', function() {

        var originalComponent = {
            key: 'value'
        };
        var component = jasmine.createSpyObj('component', ['attr']);
        componentHandlerService.getFromSelector.andReturn(component);
        componentHandlerService.setType(originalComponent, 'thetype');

        expect(component.attr).toHaveBeenCalledWith('data-smartedit-component-type', 'thetype');
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(originalComponent);
    });

    it('getSlotOperationRelatedId will get the data-smartedit-container-id when it is defined AND data-smartedit-container-type is defined', function() {

        var originalComponent = {
            key: 'value'
        };
        var component = jasmine.createSpyObj('component', ['attr']);
        component.attr.andCallFake(function(attr) {
            if (attr === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attr === 'data-smartedit-container-id') {
                return 'thecontainerid';
            } else if (attr === 'data-smartedit-container-type') {
                return 'thecontainertype';
            }
        });

        componentHandlerService.getFromSelector.andReturn(component);
        expect(componentHandlerService.getSlotOperationRelatedId(originalComponent)).toBe('thecontainerid');
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(originalComponent);
    });

    it('getSlotOperationRelatedId will get the data-smartedit-component-id when data-smartedit-container-id is defined BUT data-smartedit-container-type is undefined', function() {

        var originalComponent = {
            key: 'value'
        };
        var component = jasmine.createSpyObj('component', ['attr']);
        component.attr.andCallFake(function(attr) {
            if (attr === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attr === 'data-smartedit-container-id') {
                return 'thecontainerid';
            } else if (attr === 'data-smartedit-container-type') {
                return undefined;
            }
        });

        componentHandlerService.getFromSelector.andReturn(component);
        expect(componentHandlerService.getSlotOperationRelatedId(originalComponent)).toBe('theid');
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(originalComponent);
    });

    it('getSlotOperationRelatedId will get the data-smartedit-component-id when data-smartedit-container-id is undefined', function() {

        var originalComponent = {
            key: 'value'
        };
        var component = jasmine.createSpyObj('component', ['attr']);
        component.attr.andCallFake(function(attr) {
            if (attr === 'data-smartedit-component-id') {
                return 'theid';
            } else if (attr === 'data-smartedit-container-id') {
                return undefined;
            }
        });

        componentHandlerService.getFromSelector.andReturn(component);
        expect(componentHandlerService.getSlotOperationRelatedId(originalComponent)).toBe('theid');
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(originalComponent);
    });

    it('getType will get the data-smartedit-component-type field of a given component', function() {

        var originalComponent = {
            key: 'value'
        };
        var component = jasmine.createSpyObj('component', ['attr']);
        component.attr.andReturn('thetype');

        componentHandlerService.getFromSelector.andReturn(component);
        expect(componentHandlerService.getType(originalComponent)).toBe('thetype');
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(originalComponent);
    });

    it('getSlotOperationRelatedType will get the data-smartedit-container-type when it is defined AND data-smartedit-container-id is defined', function() {

        var originalComponent = {
            key: 'value'
        };
        var component = jasmine.createSpyObj('component', ['attr']);
        component.attr.andCallFake(function(attr) {
            if (attr === 'data-smartedit-component-type') {
                return 'thetype';
            } else if (attr === 'data-smartedit-container-type') {
                return 'thecontainertype';
            } else if (attr === 'data-smartedit-container-id') {
                return 'thecontainerid';
            }
        });

        componentHandlerService.getFromSelector.andReturn(component);
        expect(componentHandlerService.getSlotOperationRelatedType(originalComponent)).toBe('thecontainertype');
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(originalComponent);
    });

    it('getSlotOperationRelatedType will get the data-smartedit-component-type when data-smartedit-container-type is defined BUT data-smartedit-container-id is undefined', function() {

        var originalComponent = {
            key: 'value'
        };
        var component = jasmine.createSpyObj('component', ['attr']);
        component.attr.andCallFake(function(attr) {
            if (attr === 'data-smartedit-component-type') {
                return 'thetype';
            } else if (attr === 'data-smartedit-container-type') {
                return 'thecontainertype';
            } else if (attr === 'data-smartedit-container-id') {
                return undefined;
            }
        });

        componentHandlerService.getFromSelector.andReturn(component);
        expect(componentHandlerService.getSlotOperationRelatedType(originalComponent)).toBe('thetype');
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(originalComponent);
    });

    it('getSlotOperationRelatedType will get the data-smartedit-component-type when data-smartedit-container-type is undefined', function() {

        var originalComponent = {
            key: 'value'
        };
        var component = jasmine.createSpyObj('component', ['attr']);
        component.attr.andCallFake(function(attr) {
            if (attr === 'data-smartedit-component-type') {
                return 'thetype';
            } else if (attr === 'data-smartedit-container-type') {
                return undefined;
            }
        });

        componentHandlerService.getFromSelector.andReturn(component);
        expect(componentHandlerService.getSlotOperationRelatedType(originalComponent)).toBe('thetype');
        expect(componentHandlerService.getFromSelector).toHaveBeenCalledWith(originalComponent);
    });

    it('getAllComponentsSelector will return a jQuery selector matching all non-slots components', function() {

        expect(componentHandlerService.getAllComponentsSelector()).toBe(".smartEditComponent[data-smartedit-component-type!='ContentSlot']");
    });

    it('getAllSlotsSelector will return a jQuery selector matching all slots components', function() {

        expect(componentHandlerService.getAllSlotsSelector()).toBe(".smartEditComponent[data-smartedit-component-type='ContentSlot']");
    });

    it('getParentSlotForComponent will return slot ID for the given component', function() {

        var parent = jasmine.createSpyObj('parent', ['attr']);
        var component = jasmine.createSpyObj('component', ['closest']);

        component.closest.andReturn(parent);
        parent.attr.andReturn('slotId');

        expect(componentHandlerService.getParentSlotForComponent(component)).toBe('slotId');
    });
});
