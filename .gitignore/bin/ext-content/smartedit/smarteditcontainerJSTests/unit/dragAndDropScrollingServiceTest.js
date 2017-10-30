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
describe('test outer dragAndDropScrollingServiceModule Module', function() {

    var scrollingService, $rootScope;
    var mockTranslateFilter, dragAndDropConf;

    // Page Content
    var body, iFrame, iFrameBody, slots, uiHints;

    var SM_UI = $.ui.smartedit;

    beforeEach(customMatchers);

    beforeEach(function() {
        module(function($provide) {
            $provide.value('translateFilter', mockTranslateFilter);
        });

        mockTranslateFilter = function(value) {
            return value;
        };
    });

    beforeEach(module('functionsModule'));
    beforeEach(module('dragAndDropScrollingModule'));

    beforeEach(inject(function(_$rootScope_, _dragAndDropScrollingService_) {
        $rootScope = _$rootScope_;
        scrollingService = _dragAndDropScrollingService_;
    }));

    beforeEach(function() {
        setBrowser("Chrome");
        createFakeContent();

        spyOn(scrollingService, '_getSelector').andCallFake(function(arg) {
            if (arg === 'body') {
                return body;
            } else if (arg === 'iframe') {
                return iFrame;
            }

            var uiHint = jasmine.createSpyObj("UIHint", ['addClass', 'append', 'bind', 'toggleClass', 'removeClass']);
            uiHints.push(uiHint);

            return uiHint;
        });
    });

    //// Tests
    it('GIVEN a dragAndDrop configuration has been set WHEN scrolling service is initialized THEN the UI hints should be added to the page', function() {
        // Arrange
        spyOn(scrollingService, '_refreshElementReferences');
        spyOn(scrollingService, '_addUIHints');
        spyOn(scrollingService, '_registerScrollingInPage');
        scrollingService.connectWithDragAndDrop(dragAndDropConf);

        // Act
        scrollingService.initialize();

        // Assert
        expect(scrollingService._refreshElementReferences).toHaveBeenCalled();
        expect(scrollingService._addUIHints).toHaveBeenCalled();
        expect(scrollingService._registerScrollingInPage).toHaveBeenCalled();
    });

    it('GIVEN drag and drop is started in the external frame and the call is made in the external frame too THEN UI hints must be displayed', function() {
        // Arrange
        var startedInExternalFrame = true;
        var isExternalFrame = true;
        spyOn(scrollingService, '_showUIHints');

        scrollingService.connectWithDragAndDrop(dragAndDropConf);
        scrollingService.initialize();

        // Act
        scrollingService.enableScrolling(startedInExternalFrame, isExternalFrame);

        // Assert
        expect(scrollingService._showUIHints).toHaveBeenCalledWith(isExternalFrame);
    });

    it('GIVEN drag and drop is started in the inner frame THEN UI hints must be displayed', function() {
        // Arrange
        var startedInExternalFrame = false;
        var isExternalFrame = false;
        spyOn(scrollingService, '_showUIHints');

        scrollingService.connectWithDragAndDrop(dragAndDropConf);
        scrollingService.initialize();

        // Act
        scrollingService.enableScrolling(startedInExternalFrame, isExternalFrame);

        // Assert
        expect(scrollingService._showUIHints).toHaveBeenCalledWith(isExternalFrame);
    });

    it('GIVEN drag and drop is started in the external frame and the call is not made in the external frame too THEN UI hints must not be displayed', function() {
        // Arrange
        var startedInExternalFrame = true;
        var isExternalFrame = false;
        spyOn(scrollingService, '_showUIHints');

        scrollingService.connectWithDragAndDrop(dragAndDropConf);
        scrollingService.initialize();

        // Act
        scrollingService.enableScrolling(startedInExternalFrame, isExternalFrame);

        // Assert
        expect(scrollingService._showUIHints).not.toHaveBeenCalled();
    });

    it('WHEN _showUIHints is called THEN UI hints shall be made visible', function() {
        // Arrange
        var isExternalFrame = true;

        scrollingService.connectWithDragAndDrop(dragAndDropConf);
        scrollingService.initialize();

        // Act
        scrollingService._showUIHints(isExternalFrame);

        // Assert
        expect(uiHints[0].toggleClass).toHaveBeenCalledWith('visible', true);
        expect(uiHints[1].toggleClass).toHaveBeenCalledWith('visible', true);
    });

    it('WHEN _showUIHints is called and its the inner frame and the browser is Firefox THEN all UI hints shall be made visible', function() {
        // Arrange
        var isExternalFrame = false;
        setBrowser("Firefox");

        scrollingService.connectWithDragAndDrop(dragAndDropConf);
        scrollingService.initialize();

        // Act
        scrollingService._showUIHints(isExternalFrame);

        // Assert
        expect(uiHints[0].toggleClass).toHaveBeenCalledWith('visible', true);
        expect(uiHints[1].toggleClass).toHaveBeenCalledWith('visible', true);
        expect(uiHints[1].toggleClass).toHaveBeenCalledWith('firefox-fix-applied', true);
        expect(uiHints[2].toggleClass).toHaveBeenCalledWith('visible', true);
        expect(uiHints[3].toggleClass).toHaveBeenCalledWith('visible', true);
    });

    it('GIVEN drag and drop is started in the external frame and the disable scrolling call is made in the external frame too THEN UI hints must be hidden', function() {
        // Arrange
        var startedInExternalFrame = true;
        var isExternalFrame = true;
        spyOn(scrollingService, '_hideUIHints');

        scrollingService.connectWithDragAndDrop(dragAndDropConf);
        scrollingService.initialize();

        // Act
        scrollingService.disableScrolling(startedInExternalFrame, isExternalFrame);

        // Assert
        expect(scrollingService._hideUIHints).toHaveBeenCalledWith(isExternalFrame);
    });

    it('GIVEN drag and drop is started in the inner frame and the disable scrolling is called THEN UI hints must be hidden', function() {
        // Arrange
        var startedInExternalFrame = false;
        var isExternalFrame = false;
        spyOn(scrollingService, '_hideUIHints');

        scrollingService.connectWithDragAndDrop(dragAndDropConf);
        scrollingService.initialize();

        // Act
        scrollingService.disableScrolling(startedInExternalFrame, isExternalFrame);

        // Assert
        expect(scrollingService._hideUIHints).toHaveBeenCalledWith(isExternalFrame);
    });

    it('GIVEN drag and drop is started in the external frame and the disable scrolling is not made in the external frame too THEN UI hints must not be displayed', function() {
        // Arrange
        var startedInExternalFrame = true;
        var isExternalFrame = false;
        spyOn(scrollingService, '_hideUIHints');

        scrollingService.connectWithDragAndDrop(dragAndDropConf);
        scrollingService.initialize();

        // Act
        scrollingService.disableScrolling(startedInExternalFrame, isExternalFrame);

        // Assert
        expect(scrollingService._hideUIHints).not.toHaveBeenCalled();
    });

    it('WHEN _hideUIHints is called THEN UI hints shall be made visible', function() {
        // Arrange
        var isExternalFrame = true;

        scrollingService.connectWithDragAndDrop(dragAndDropConf);
        scrollingService.initialize();

        // Act
        scrollingService._hideUIHints(isExternalFrame);

        // Assert
        expect(uiHints[0].removeClass).toHaveBeenCalledWith('visible');
        expect(uiHints[1].removeClass).toHaveBeenCalledWith('visible');
    });

    it('WHEN _hideUIHints is called and its the inner frame and the browser is Firefox THEN all UI hints shall be made visible', function() {
        // Arrange
        var isExternalFrame = false;
        setBrowser("Firefox");

        scrollingService.connectWithDragAndDrop(dragAndDropConf);
        scrollingService.initialize();

        // Act
        scrollingService._hideUIHints(isExternalFrame);

        // Assert
        expect(uiHints[0].removeClass).toHaveBeenCalledWith('visible');
        expect(uiHints[1].removeClass).toHaveBeenCalledWith('visible');
        expect(uiHints[1].removeClass).toHaveBeenCalledWith('firefox-fix-applied');
        expect(uiHints[2].removeClass).toHaveBeenCalledWith('visible');
        expect(uiHints[3].removeClass).toHaveBeenCalledWith('visible');
    });

    it('GIVEN no dragAndDrop configuration has been set WHEN scrolling service is initialized THEN nothing should be executed', function() {
        // Arrange
        spyOn(scrollingService, '_refreshElementReferences');
        spyOn(scrollingService, '_addUIHints');
        spyOn(scrollingService, '_registerScrollingInPage');

        // Act
        scrollingService.initialize();

        // Assert
        expect(scrollingService._refreshElementReferences).not.toHaveBeenCalled();
        expect(scrollingService._addUIHints).not.toHaveBeenCalled();
        expect(scrollingService._registerScrollingInPage).not.toHaveBeenCalled();
    });

    it('GIVEN no dragAndDrop configuration has been set WHEN scrolling service is enabled THEN nothing should be executed', function() {
        // Arrange
        spyOn(scrollingService, '_showUIHints');

        // Act
        scrollingService.enableScrolling();

        // Assert
        expect(scrollingService._showUIHints).not.toHaveBeenCalled();
    });

    it('GIVEN no dragAndDrop configuration has been set WHEN scrolling service is disabled THEN nothing should be executed', function() {
        // Arrange
        spyOn(scrollingService, '_hideUIHints');

        // Act
        scrollingService.disableScrolling();

        // Assert
        expect(scrollingService._hideUIHints).not.toHaveBeenCalled();
    });

    it('WHEN _registerScrollingInPage is called THEN the right events are registered in the UI Hints', function() {
        // Arrange
        scrollingService.connectWithDragAndDrop(dragAndDropConf);
        scrollingService._refreshElementReferences();
        scrollingService._addUIHints();

        // Act
        scrollingService._registerScrollingInPage();

        // Assert
        expect(uiHints[0].bind).toHaveBeenCalledWith('mouseenter', jasmine.any(Function));
        expect(uiHints[0].bind).toHaveBeenCalledWith('mouseleave', jasmine.any(Function));
        expect(uiHints[1].bind).toHaveBeenCalledWith('mouseleave', jasmine.any(Function));
        expect(uiHints[1].bind).toHaveBeenCalledWith('mouseleave', jasmine.any(Function));
        expect(iFrameBody.bind).toHaveBeenCalledWith('mousemove', jasmine.any(Function));
    });

    it('WHEN _addUIHints is called THEN the UI hints are created and appended into the body', function() {
        // Arrange

        // Act
        scrollingService.connectWithDragAndDrop({
            targetSelector: 'something'
        });
        scrollingService._refreshElementReferences();
        scrollingService._addUIHints();

        // Assert
        expect(uiHints.length).toBe(4);

        expect(body.append).toHaveBeenCalledWith(uiHints[0]);
        expect(uiHints[0].addClass).toHaveBeenCalledWith('top');

        expect(body.append).toHaveBeenCalledWith(uiHints[1]);
        expect(uiHints[1].addClass).toHaveBeenCalledWith('bottom');

        expect(iFrameBody.append).toHaveBeenCalledWith(uiHints[2]);
        expect(uiHints[2].addClass).toHaveBeenCalledWith('top-firefox-fix');

        expect(iFrameBody.append).toHaveBeenCalledWith(uiHints[3]);
        expect(uiHints[3].addClass).toHaveBeenCalledWith('bottom-firefox-fix');

    });

    it('WHEN _addUIHints is called and the browser is Firefox THEN the UI hints are created and appended into the body with the right classes', function() {
        // Arrange
        setBrowser("Firefox");

        scrollingService.connectWithDragAndDrop(dragAndDropConf);
        scrollingService._refreshElementReferences();

        // Act
        scrollingService._addUIHints();

        // Assert
        expect(uiHints.length).toBe(4);

        expect(body.append).toHaveBeenCalledWith(uiHints[0]);
        expect(uiHints[0].addClass).toHaveBeenCalledWith('top');

        expect(body.append).toHaveBeenCalledWith(uiHints[1]);
        expect(uiHints[1].addClass).toHaveBeenCalledWith('bottom');

        expect(iFrameBody.append).toHaveBeenCalledWith(uiHints[2]);
        expect(uiHints[2].addClass).toHaveBeenCalledWith('top-firefox-fix');

        expect(iFrameBody.append).toHaveBeenCalledWith(uiHints[3]);
        expect(uiHints[3].addClass).toHaveBeenCalledWith('bottom-firefox-fix');
    });

    // Helper Functions
    function createFakeContent() {
        createFakePageContent();
        dragAndDropConf = {
            targetSelector: 'some target selector'
        };
    }

    function setBrowser(browser) {
        SM_UI._getBrowser = function() {
            return browser;
        };
    }

    function createFakePageContent() {
        uiHints = [];
        body = jasmine.createSpyObj("pageBody", ['append']);
        iFrame = jasmine.createSpyObj("iFrame", ['append', 'contents']);
        iFrameBody = jasmine.createSpyObj('iFrameBody', ['append', 'bind']);
        slots = jasmine.createSpy('slots');

        iFrame.contents.andCallFake(function() {
            return {
                find: function(arg) {
                    if (arg === 'body') {
                        return iFrameBody;
                    } else {
                        return slots;
                    }
                }
            };
        });
    }

});
