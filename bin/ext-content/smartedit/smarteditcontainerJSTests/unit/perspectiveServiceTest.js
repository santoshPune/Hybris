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
describe('outer perspectiveService', function() {

    var $log, $q, storageService, gatewayProxy, renderGateway, gatewayFactory, systemEventService, Perspective, PerspectiveServiceInterface, perspectiveService, featureService, iFrameManager, NONE_PERSPECTIVE, EVENTS;

    beforeEach(customMatchers);

    beforeEach(module('eventServiceModule', function($provide) {

        EVENTS = {
            LOGOUT: 'logout',
            CLEAR_PERSPECTIVE_FEATURES: 'clear'
        };
        $provide.value('EVENTS', EVENTS);

        NONE_PERSPECTIVE = 'se.none';
        $provide.value('NONE_PERSPECTIVE', NONE_PERSPECTIVE);
    }));

    beforeEach(module('perspectiveServiceModule', function($provide) {

        iFrameManager = jasmine.createSpyObj('iFrameManager', ['showWaitModal', 'hideWaitModal']);
        $provide.value('iFrameManager', iFrameManager);

        gatewayFactory = jasmine.createSpyObj('gatewayFactory', ['initListener', 'createGateway']);
        $provide.value('gatewayFactory', gatewayFactory);
        renderGateway = jasmine.createSpyObj('renderGateway', ['publish']);
        gatewayFactory.createGateway.andReturn(renderGateway);

        gatewayProxy = jasmine.createSpyObj('gatewayProxy', ['initForService']);
        $provide.value('gatewayProxy', gatewayProxy);

        featureService = jasmine.createSpyObj('featureService', ['enable', 'disable']);
        $provide.value('featureService', featureService);

        systemEventService = jasmine.createSpyObj('systemEventService', ['sendAsynchEvent', 'registerEventHandler']);
        $provide.value('systemEventService', systemEventService);

        storageService = jasmine.createSpyObj('storageService', ['getValueFromCookie', 'putValueInCookie']);
        $provide.value('storageService', storageService);
    }));

    beforeEach(inject(function(_$log_, _$q_, _Perspective_, _PerspectiveServiceInterface_, _perspectiveService_, _featureService_, _EVENTS_) {
        $q = _$q_;
        $log = _$log_;
        Perspective = _Perspective_;
        perspectiveService = _perspectiveService_;
        PerspectiveServiceInterface = _PerspectiveServiceInterface_;
        featureService = _featureService_;
        EVENTS = _EVENTS_;
    }));

    it('extends PerspectiveServiceInterface', function() {
        expect(perspectiveService instanceof PerspectiveServiceInterface).toBe(true);
    });

    it('initializes and invokes gatewayProxy', function() {
        expect(perspectiveService.gatewayId).toBe("perspectiveService");
        expect(gatewayProxy.initForService).toHaveBeenCalledWith(perspectiveService, ['register', 'switchTo', 'hasActivePerspective', 'isEmptyPerspectiveActive', 'selectDefault']);
        expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(EVENTS.LOGOUT, jasmine.any(Function));
        expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(EVENTS.CLEAR_PERSPECTIVE_FEATURES, jasmine.any(Function));
    });

    it('WHEN _registerEventHandlers is called THEN the right event handlers will be registered', function() {
        // Arrange
        spyOn(perspectiveService, '_registerEventHandlers').andCallThrough();
        spyOn(perspectiveService, '_clearPerspectiveFeatures').andCallFake(function() {});
        spyOn(perspectiveService, '_onLogoutPerspectiveCleanup').andCallFake(function() {});
        systemEventService.registerEventHandler.reset();

        // Act
        perspectiveService._registerEventHandlers();

        // Assert
        expect(systemEventService.registerEventHandler.calls.length).toBe(2);
        expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(EVENTS.LOGOUT, jasmine.any(Function));
        expect(systemEventService.registerEventHandler).toHaveBeenCalledWith(EVENTS.CLEAR_PERSPECTIVE_FEATURES, jasmine.any(Function));

        var clearCallback = systemEventService.registerEventHandler.calls[0].args[1];
        clearCallback();
        expect(perspectiveService._clearPerspectiveFeatures).toHaveBeenCalled();

        var logoutCallback = systemEventService.registerEventHandler.calls[1].args[1];
        logoutCallback();
        expect(perspectiveService._onLogoutPerspectiveCleanup).toHaveBeenCalled();
    });

    it('perspectiveService WILL set two default perspectives WHEN initializes', function() {
        expect(perspectiveService.gatewayId).toBe("perspectiveService");
        var perspectives = perspectiveService.getPerspectives();

        var none = perspectives.filter(function(element) {
            return element.key === 'se.none';
        }).reduce(function(obj) {
            return obj;
        });

        var all = perspectives.filter(function(element) {
            return element.key === 'se.all';
        }).reduce(function(obj) {
            return obj;
        });

        expect(perspectives.length).toBe(2);
        expect(none.name).toBe('perspective.none.name');
        expect(all.name).toBe('perspective.all.name');
    });

    it('register throws error if key is not provided', function() {

        expect(function() {
            perspectiveService.register({
                nameI18nKey: 'somenameI18nKey',
                features: ['abc', 'xyz']
            });
        }).toThrow("perspectiveService.configuration.key.error.required");

    });

    it('register throws error if nameI18nKey is not provided', function() {

        expect(function() {
            perspectiveService.register({
                key: 'somekey',
                features: ['abc', 'xyz']
            });
        }).toThrow("perspectiveService.configuration.nameI18nKey.error.required");

    });

    it('register throws error if features is not provided and perspective is neither se.none nor se.all', function() {

        expect(function() {
            perspectiveService.register({
                key: 'somekey',
                nameI18nKey: 'somenameI18nKey'
            });
        }).toThrow("perspectiveService.configuration.features.error.required");

    });

    it('register throws error is features is empty', function() {

        expect(function() {
            perspectiveService.register({
                key: 'somekey',
                nameI18nKey: 'somenameI18nKey',
                features: []
            });
        }).toThrow("perspectiveService.configuration.features.error.required");

    });

    it('register does no throw error if loader is provided', function() {

        expect(function() {
            perspectiveService.register({
                key: 'se.none',
                nameI18nKey: 'somenameI18nKey',
                loader: function() {}
            });
        }).not.toThrow();

    });

    it('register does no throw error if features is not provided and perspective is se.none', function() {

        expect(function() {
            perspectiveService.register({
                key: 'se.none',
                nameI18nKey: 'somenameI18nKey'
            });
        }).not.toThrow();

    });

    it('register does no throw error if features is not provided and perspective is se.all', function() {

        expect(function() {
            perspectiveService.register({
                key: 'se.all',
                nameI18nKey: 'somenameI18nKey'
            });
        }).not.toThrow();

    });

    it('GIVEN that perspective configuration has features, THEN register pushes to the features list a Perspective instantiated from configuration and sends a notification', function() {

        perspectiveService.register({
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey',
            features: ['abc', 'xyz'],
            perspectives: ['persp1', 'persp2']
        });

        expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith("perspectives:update");

        var perspectiveArray = perspectiveService.getPerspectives();
        //Expect to have 2 default + 1 registered
        expect(perspectiveArray.length).toBe(3);
        var perspective = perspectiveArray[2];

        expect(perspective instanceof Perspective).toBe(true);

        expect(perspective).toEqual(jasmine.objectContaining({
            key: 'somekey',
            name: 'somenameI18nKey',
            system: true,
            dynamicDecoratorLoader: undefined,
            features: ['abc', 'xyz'],
            perspectives: ['persp1', 'persp2'],
            descriptionI18nKey: 'somedescriptionI18nKey'
        }));

    });

    it('register does not override existing perspectives but merges features and nested perspectives', function() {
        perspectiveService.register({
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey',
            descriptionI18nKey: 'somedescriptionI18nKey',
            features: ['abc', 'xyz'],
            perspectives: ['persp1', 'persp2']
        });

        perspectiveService.register({
            key: 'somekey3',
            nameI18nKey: 'somenameI18nKey3',
            descriptionI18nKey: 'somedescriptionI18nKey3',
            features: ['zzz'],
            perspectives: ['xxx']
        });


        perspectiveService.register({
            key: 'somekey',
            nameI18nKey: 'somenameI18nKey2',
            descriptionI18nKey: 'somedescriptionI18nKey2',
            features: ['xyz', 'def'],
            perspectives: ['persp2', 'persp3']
        });


        expect(systemEventService.sendAsynchEvent).toHaveBeenCalledWith("perspectives:update");

        var perspectiveArray = perspectiveService.getPerspectives();

        var res1 = perspectiveArray.filter(function(element) {
            return element.key === 'somekey';
        });

        var res2 = perspectiveArray.filter(function(element) {
            return element.key === 'somekey3';
        });
        expect(res1).toEqual([jasmine.objectContaining({
            key: 'somekey',
            name: 'somenameI18nKey',
            system: true,
            dynamicDecoratorLoader: undefined,
            features: ['abc', 'xyz', 'def'],
            perspectives: ['persp1', 'persp2', 'persp3'],
            descriptionI18nKey: 'somedescriptionI18nKey'
        })]);

        expect(res2).toEqual([jasmine.objectContaining({
            key: 'somekey3',
            name: 'somenameI18nKey3',
            system: true,
            dynamicDecoratorLoader: undefined,
            features: ['zzz'],
            perspectives: ['xxx'],
            descriptionI18nKey: 'somedescriptionI18nKey3'
        })]);

    });

    it('_fetchAllFeatures collects all features of nested perspectives, returns a set of unique features, and log message if a nested perspective does not exist', function() {

        spyOn($log, 'debug').andReturn();

        var perspective1 = {
            key: 'persp1',
            features: ['feat1', 'feat2'],
            perspectives: ['persp2']
        };
        var perspective2 = {
            key: 'persp2',
            features: ['feat2', 'feat3'],
            perspectives: ['persp3', 'persp4']
        };
        var perspective4 = {
            key: 'persp4',
            features: ['feat3', 'feat4'],
            perspectives: []
        };
        perspectiveService.perspectives = [perspective1, perspective2, perspective4];

        var holder = [];
        perspectiveService._fetchAllFeatures(perspective1, holder);

        expect(holder).toEqual(['feat1', 'feat2', 'feat3', 'feat4']);

        expect($log.debug).toHaveBeenCalledWith('nested perspective persp3 was not found in the registry');
    });

    it('switchTo will silently do nothing if trying to switch to the same perspecitve as the activate one', function() {
        perspectiveService.data = {
            activePerspective: {
                key: 'aperspective'
            }
        };
        perspectiveService.switchTo('aperspective');

        expect(storageService.putValueInCookie).not.toHaveBeenCalled();
        expect(iFrameManager.showWaitModal).not.toHaveBeenCalled();
        expect(featureService.enable).not.toHaveBeenCalled();
        expect(featureService.disable).not.toHaveBeenCalled();
        expect(renderGateway.publish).not.toHaveBeenCalled();
    });

    it('switchTo will throw an error if required perspective is not found', function() {
        perspectiveService.data = {
            activePerspective: {
                key: 'previousperspective'
            }
        };

        spyOn(perspectiveService, '_findByKey').andReturn(null);

        expect(function() {
            perspectiveService.switchTo('aperspective');
        }).toThrow("switchTo() - Couldn't find perspective with key aperspective");

        expect(perspectiveService._findByKey).toHaveBeenCalledWith('aperspective');
        expect(storageService.putValueInCookie).not.toHaveBeenCalled();
        expect(iFrameManager.showWaitModal).not.toHaveBeenCalled();
        expect(featureService.enable).not.toHaveBeenCalled();
        expect(featureService.disable).not.toHaveBeenCalled();
        expect(renderGateway.publish).not.toHaveBeenCalled();
    });

    it('switchTo NONE_PERSPECTIVE will publish a rerender/false and hide the wait modal', function() {

        var nonPerspective = {
            key: NONE_PERSPECTIVE,
            features: []
        };
        perspectiveService.perspectives = [nonPerspective];
        perspectiveService.switchTo(NONE_PERSPECTIVE);


        expect(iFrameManager.showWaitModal).toHaveBeenCalled();
        expect(renderGateway.publish).toHaveBeenCalledWith("rerender", false);
        expect(iFrameManager.hideWaitModal).toHaveBeenCalled();
    });


    it('switchTo will activate all (nested) features of a perspective and notify to rerender', function() {

        var perspective0 = {
            key: 'persp0',
            features: ['feat0']
        };
        var perspective1 = {
            key: 'persp1',
            features: ['feat1', 'feat2'],
            perspectives: ['persp2']
        };
        var perspective2 = {
            key: 'persp2',
            features: ['feat2', 'feat3'],
            perspectives: ['persp3', 'persp4']
        };
        var perspective4 = {
            key: 'persp4',
            features: ['feat3', 'feat4'],
            perspectives: []
        };
        perspectiveService.perspectives = [perspective0, perspective1, perspective2, perspective4];
        perspectiveService.data.activePerspective = perspective0;
        perspectiveService.switchTo('persp1');

        expect(storageService.putValueInCookie).toHaveBeenCalledWith('smartedit-perspectives', 'persp1', true);

        expect(perspectiveService.data.activePerspective).toBe(perspective1);
        expect(iFrameManager.showWaitModal).toHaveBeenCalled();
        expect(featureService.enable.callCount).toBe(4);
        expect(featureService.enable.calls[0].args).toEqual(['feat1']);
        expect(featureService.enable.calls[1].args).toEqual(['feat2']);
        expect(featureService.enable.calls[2].args).toEqual(['feat3']);
        expect(featureService.enable.calls[3].args).toEqual(['feat4']);

        expect(gatewayFactory.createGateway).toHaveBeenCalledWith("render");
        expect(renderGateway.publish).toHaveBeenCalledWith("rerender", true);
        expect(iFrameManager.hideWaitModal).not.toHaveBeenCalled();
    });

    it('switchTo will disable features of previous perspective not present in new one and activate features of new perspective not present in previous one', function() {

        var perspective0 = {
            key: 'persp0',
            features: ['feat0', 'feat2', 'feat3']
        };

        var perspective1 = {
            key: 'persp1',
            features: ['feat1', 'feat2'],
            perspectives: ['persp2']
        };
        var perspective2 = {
            key: 'persp2',
            features: ['feat2', 'feat3'],
            perspectives: ['persp3', 'persp4']
        };
        var perspective4 = {
            key: 'persp4',
            features: ['feat3', 'feat4'],
            perspectives: []
        };
        perspectiveService.perspectives = [perspective0, perspective1, perspective2, perspective4];
        perspectiveService.data.activePerspective = perspective0;

        perspectiveService.switchTo('persp1');

        expect(storageService.putValueInCookie).toHaveBeenCalledWith('smartedit-perspectives', 'persp1', true);
        expect(perspectiveService.data.activePerspective).toBe(perspective1);

        expect(featureService.disable.callCount).toBe(1);
        expect(featureService.disable).toHaveBeenCalledWith('feat0');

        expect(iFrameManager.showWaitModal).toHaveBeenCalled();
        expect(featureService.enable.callCount).toBe(2);
        expect(featureService.enable.calls[0].args).toEqual(['feat1']);
        expect(featureService.enable.calls[1].args).toEqual(['feat4']);

        expect(gatewayFactory.createGateway).toHaveBeenCalledWith("render");
        expect(renderGateway.publish).toHaveBeenCalledWith("rerender", true);
        expect(iFrameManager.hideWaitModal).not.toHaveBeenCalled();
    });

    it('switchTo WILL throw an error WHEN the perspective is not found', function() {

        var perspective0 = {
            key: 'persp0',
            features: ['feat0', 'feat2', 'feat3']
        };

        var perspective1 = {
            key: 'persp1',
            features: ['feat1', 'feat2'],
            perspectives: ['persp2']
        };

        perspectiveService.perspectives = [perspective0, perspective1];
        perspectiveService.data.activePerspective = perspective0;

        expect(
            function() {
                perspectiveService.switchTo('whatever');
            }
        ).toThrow();
        expect(iFrameManager.showWaitModal).not.toHaveBeenCalled();
    });

    it('clearActivePerspective WILL delete active perspective', function() {
        var perspectives = perspectiveService.getPerspectives();
        expect(perspectives.length).toBe(2);

        perspectiveService.data.activePerspective = perspectives[0];

        perspectiveService.clearActivePerspective();

        expect(perspectiveService.data.activePerspective).toBeUndefined();
    });

    it('selectDefault WILL select NONE_PERSPECTIVE if none is found in smartedit-perspectives cookie', function() {

        spyOn(perspectiveService, 'switchTo').andReturn();
        storageService.getValueFromCookie.andReturn($q.when(null));

        expect(perspectiveService.data.activePerspective).toBeUndefined();

        var result = perspectiveService.selectDefault();
        $rootScope.$digest();

        result.then(function() {
            expect(storageService.getValueFromCookie).toHaveBeenCalledWith("smartedit-perspectives", true);
            expect(perspectiveService.switchTo).toHaveBeenCalledWith(NONE_PERSPECTIVE);
        });
    });


    it('isEmptyPerspectiveActive returns true if active perspective is NONE_PERSPECTIVE', function() {
        perspectiveService.data = {
            activePerspective: {
                key: NONE_PERSPECTIVE
            }
        };
        expect(perspectiveService.isEmptyPerspectiveActive()).toBe(true);
    });

    it('isEmptyPerspectiveActive returns false if active perspective is not NONE_PERSPECTIVE', function() {
        perspectiveService.data = {
            activePerspective: {
                key: 'someotherperspective'
            }
        };
        expect(perspectiveService.isEmptyPerspectiveActive()).toBe(false);
    });

    it('isEmptyPerspectiveActive returns false if there is no active perspective', function() {
        perspectiveService.data = {};
        expect(perspectiveService.isEmptyPerspectiveActive()).toBe(undefined);
    });

    it('isEmptyPerspectiveActive returns false if there is no active perspective', function() {
        perspectiveService.data = {};
        expect(perspectiveService.isEmptyPerspectiveActive()).toBe(undefined);
    });

    it('WHEN _onLogoutPerspectiveCleanup is called THEN it disables all active features and removes current active perspective', function() {
        // Arrange
        var features = ["feature1", "feature2"];
        var currentPerspective = "some perspective";

        perspectiveService.data.activePerspective = currentPerspective;
        spyOn(perspectiveService, '_fetchAllFeatures').andCallFake(function(perspectiveName, tempArray) {
            features.forEach(function(feature) {
                tempArray.push(feature);
            });
        });
        spyOn(perspectiveService, 'clearActivePerspective').andCallThrough();
        spyOn(perspectiveService, '_clearPerspectiveFeatures').andCallThrough();

        // Act
        var response = perspectiveService._onLogoutPerspectiveCleanup();
        $rootScope.$digest();

        // Assert
        response.then(function() {
            expect(featureService.disable.calls.length).toBe(2);
            expect(featureService.disable).toHaveBeenCalledWith(features[0]);
            expect(featureService.disable).toHaveBeenCalledWith(features[1]);
            expect(perspectiveService._fetchAllFeatures).toHaveBeenCalledWith(currentPerspective, jasmine.any(Array));
            expect(perspectiveService.clearActivePerspective).toHaveBeenCalled();
        });
    });

});
