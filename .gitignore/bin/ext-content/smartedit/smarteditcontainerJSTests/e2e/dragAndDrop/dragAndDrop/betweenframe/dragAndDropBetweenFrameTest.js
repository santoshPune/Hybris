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
require('../../dragAndDropFunctions.js');

//we do not seem to be able to specify position within slot with the current selenium api : playing with coordinates always puts at the end of slot
describe('E2E Test for drag and drop service', function() {

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/dragAndDrop/dragAndDrop/dragAndDropTest.html');
    });

    describe('between frames', function() {

        it("from parent frame, component0 will be dragged and dropped to slot1 in the iframe", function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();
            browser.switchToParent();

            startDraggingElement('#component0');
            expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);

            dragToPositionFromElement(element(by.css('iframe')), 0, 0);
            browser.switchToIFrame();

            getElementPosition('#slot1 #component2').then(function(elementPosition) {
                dragToPosition(elementPosition.x, elementPosition.y);
                getElementPosition('#slot1 #component1').then(function(element2Position) {
                    dragToPosition(element2Position.x - elementPosition.x, element2Position.y - elementPosition.y);

                    expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);
                    expect(element(by.css('#slot1')).getAttribute('class')).toContain(CSS_CLASSES.HIGHLIGHTED_SLOT);
                    expect(element(by.css('#slot1')).getAttribute('class')).toContain(CSS_CLASSES.VALID_SLOT);

                    dropDraggedElement().then(function() {
                        browser.switchToParent();
                        expect(element(by.binding('alert.message')).getText()).toBe('This item is dropped to slot:slot1 at index:0');

                        browser.switchToIFrame();
                        expect(by.id(HELPER_ID)).toBeAbsent();
                        expect(element(by.css("#slot1 > [data-smartedit-component-id='component0']")).isPresent()).toBe(true);
                    });
                });
            });
        });

        it("from parent frame, component0 cannot be dragged and dropped to slot2 (disabled) in the iframe", function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            expect(by.css('#slot2 #component0')).toBeAbsent();
            expect(by.id(HELPER_ID)).toBeAbsent();

            browser.switchToParent();

            startDraggingElement('#component0');
            expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);

            dragToPositionFromElement(element(by.css('iframe')), 0, 0);
            browser.switchToIFrame();

            getElementPosition('#slot2 #component4').then(function(elementPosition) {
                dragToPosition(elementPosition.x + 10, elementPosition.y + 10); // To ensure proper containment
                expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);
                expect(element(by.css('#slot2')).getAttribute('class')).toContain(CSS_CLASSES.HIGHLIGHTED_SLOT);
                expect(element(by.css('#slot2')).getAttribute('class')).toContain(CSS_CLASSES.INVALID_SLOT);

                dropDraggedElement().then(function() {
                    browser.switchToParent();
                    expect(element(by.binding('alert.message')).getText()).toBe('This item cannot be dropped to slot:slot2');

                    browser.switchToIFrame();
                    expect(by.id(HELPER_ID)).toBeAbsent();
                    expect(by.css("#slot2 > [data-smartedit-component-id='component0']")).toBeAbsent();
                });
            });

        });

        it('WHEN I drag a component from the outer frame into the inner frame and hover over a slot, THEN I expect the slot to be highlighted until the mouse is moved', function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            // No slot highlighted
            expect(element(by.css('#slot1')).getAttribute('class')).not.toContain(CSS_CLASSES.VALID_SLOT);
            expect(element(by.css('#slot2')).getAttribute('class')).not.toContain(CSS_CLASSES.INVALID_SLOT);

            browser.switchToParent();
            startDraggingElement('#component0');
            dragToPositionFromElement(element(by.css('iframe')), 0, 0);

            browser.switchToIFrame();
            getElementPosition('#slot1 #component1').then(function(elementPosition) {
                // First slot highlighted
                dragToPosition(elementPosition.x + 1, elementPosition.y + 1);

                browser.wait(protractor.ExpectedConditions.presenceOf(element(by.css('#slot1 .' + CSS_CLASSES.SORTABLE_PLACEHOLDER))), 5000, 'Sortable placeholder not found');
                browser.wait(protractor.ExpectedConditions.stalenessOf(element(by.css('#slot2 .' + CSS_CLASSES.SORTABLE_PLACEHOLDER))), 5000, 'Sortable placeholder not found');

                expect(element(by.css('#slot1')).getAttribute('class')).toContain(CSS_CLASSES.VALID_SLOT);
                expect(element(by.css('#slot1 .' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).isPresent()).toBe(true);
                expect(element(by.css('#slot2')).getAttribute('class')).not.toContain(CSS_CLASSES.INVALID_SLOT);
                expect(by.css('#slot2 .' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).toBeAbsent();

                browser.switchToParent();
                dragToPositionFromElement(element(by.css('iframe')), 0, 0);
                browser.switchToIFrame();

                return getElementPosition('#slot2 #component4');
            }).then(function(elementPosition) {
                // Second slot highlighted
                dragToPosition(elementPosition.x + 1, elementPosition.y + 1);

                browser.wait(protractor.ExpectedConditions.stalenessOf(element(by.css('#slot1 .' + CSS_CLASSES.SORTABLE_PLACEHOLDER))), 5000, 'Sortable placeholder not found');
                browser.wait(protractor.ExpectedConditions.presenceOf(element(by.css('#slot2 .' + CSS_CLASSES.SORTABLE_PLACEHOLDER))), 5000, 'Sortable placeholder not found');

                expect(element(by.css('#slot1')).getAttribute('class')).not.toContain(CSS_CLASSES.VALID_SLOT);
                expect(by.css('#slot1 .' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).toBeAbsent();
                expect(element(by.css('#slot2')).getAttribute('class')).toContain(CSS_CLASSES.INVALID_SLOT);
                expect(element(by.css('#slot2 .' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).isPresent()).toBe(true);

                browser.switchToParent();
                dragToPositionFromElement(element(by.css('iframe')), 0, 0);
                browser.switchToIFrame();

                //// No slot highlighted
                expect(element(by.css('#slot1')).getAttribute('class')).not.toContain(CSS_CLASSES.INVALID_SLOT);
                expect(element(by.css('#slot2')).getAttribute('class')).not.toContain(CSS_CLASSES.INVALID_SLOT);
            });
        });

        it('WHEN I drag a component from the outer frame into the inner frame and move through slots, THEN I expect the right component position to be highlighted', function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            // No slot highlighted
            expect(element(by.css('#slot1')).getAttribute('class')).not.toContain(CSS_CLASSES.VALID_SLOT);
            expect(by.css('#slot1 .' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).toBeAbsent();

            // Get the element from the outer frame
            browser.switchToParent();
            startDraggingElement('#component0');
            dragToPositionFromElement(element(by.css('iframe')), 0, 0);

            browser.switchToIFrame();
            getElementPosition('#slot1 #component1').then(function(elementPosition) {
                // First slot highlighted
                dragToPosition(elementPosition.x + 5, elementPosition.y + 10);

                // First position highlighted
                dragToPosition(0, -5);
                expect(element.all(by.css('#slot1 > div')).first().getAttribute('class')).toContain(CSS_CLASSES.SORTABLE_PLACEHOLDER);

                // Second position highlighted
                browser.sleep(500);
                dragToPosition(0, 90);
                expect(element.all(by.css('#slot1 > div')).get(1).getAttribute('class')).toContain(CSS_CLASSES.SORTABLE_PLACEHOLDER);

                // Third position highlighted
                browser.sleep(500);
                dragToPosition(0, 50);
                expect(element.all(by.css('#slot1 > div')).get(2).getAttribute('class')).toContain(CSS_CLASSES.SORTABLE_PLACEHOLDER);

                // Last position highlighted
                browser.sleep(500);
                dragToPosition(0, 60);
                expect(element.all(by.css('#slot1 > div')).get(3).getAttribute('class')).toContain(CSS_CLASSES.SORTABLE_PLACEHOLDER);
            });
        });

        it('WHEN I drag a component from the outer frame into the inner frame and hover over a slot, THEN I expect the helper to follow the mouse movement', function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            // Check no helper
            expect(by.id(HELPER_ID)).toBeAbsent();

            browser.switchToParent();
            startDraggingElement('#component0');
            dragToPositionFromElement(element(by.css('#component0')), 0, 0);
            getElementPosition('#component0').then(function(elementPosition) {
                isHelperInMousePosition(elementPosition);
            });

            dragToPositionFromElement(element(by.css('iframe')), 0, 0);
            isHelperInMousePosition({
                x: 0,
                y: TOOLBAR_HEIGHT
            }); // The toolbar position.

            browser.switchToIFrame();
            getElementPosition('#slot1 #component1').then(function(elementPosition) {
                dragToPosition(elementPosition.x, elementPosition.y);
                isHelperInMousePosition(elementPosition);

                browser.switchToParent();
                dragToPositionFromElement(element(by.css('iframe')), 0, 0);
                isHelperInMousePosition({
                    x: 0,
                    y: TOOLBAR_HEIGHT
                });

                browser.switchToIFrame();
                return getElementPosition('#slot2 #component4');
            }).then(function(elementPosition) {
                dragToPosition(elementPosition.x, elementPosition.y);
                isHelperInMousePosition(elementPosition);

                browser.switchToParent();
                dragToPositionFromElement(element(by.css('iframe')), 0, 0);
                isHelperInMousePosition({
                    x: 0,
                    y: TOOLBAR_HEIGHT
                });
            });
        });

        it("from parent frame, component0 is not droppable into secondarySlot1", function() {

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();

            element(by.css('#secondarySlot1 #secondaryComponent1')).getLocation().then(function(navDivLocation) {
                initTop = navDivLocation.y;
                initLeft = navDivLocation.x;

                expect(by.css('#secondarySlot1 #component0')).toBeAbsent();
                expect(by.css('#secondarySlot2 #component0')).toBeAbsent();

                browser.switchToParent();

                browser.actions().dragAndDrop(element(by.id('component0')), {
                    x: initLeft,
                    y: initTop
                }).perform();

            });

            browser.switchToIFrame();

            //jQuery strips the component of its id when crossing frames to ensure ids remain unique
            expect(by.css('#secondarySlot1 div[data-smartedit-component-type=componentType0]')).toBeAbsent();
            expect(by.css('#secondarySlot1 div[data-smartedit-component-type=componentType0]')).toBeAbsent();

        });

        // Scrolling Tests.
        it("GIVEN that I am on the store front, WHEN I drag a component from the outer frame into the inner frame, THEN I expect to see the UI Hints at the top and the bottom of the page", function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();
            browser.switchToParent();

            startDraggingElement('#component0');

            browser.sleep(1000);

            expect(element(by.css(".UIhintDragAndDrop.top")).isDisplayed()).toBeTruthy();
            expect(element(by.css(".UIhintDragAndDrop.bottom")).isDisplayed()).toBeTruthy();

            dropDraggedElement();

            browser.sleep(1000);

            expect(element(by.css(".UIhintDragAndDrop.top")).isDisplayed()).toBeFalsy();
            expect(element(by.css(".UIhintDragAndDrop.bottom")).isDisplayed()).toBeFalsy();

        });

        it('WHEN I drag a component from the outer frame into the inner frame and position the mouse on the bottom UI Hint, THEN I expect the page to scroll down until the mouse is moved out', function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();
            browser.switchToParent();

            // No scrolling yet
            startDraggingElement('#component0');
            expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);
            expect(element(by.css(".UIhintDragAndDrop.bottom")).isDisplayed()).toBeTruthy();

            browser.switchToParent();
            getScrollTop().then(function(scrollTop) {
                expect(scrollTop).toBe(0);
            });

            // Scroll for one second
            dragToElement('.UIhintDragAndDrop.bottom').then(function() {
                browser.sleep(1000);
                browser.switchToParent();

                getScrollTop().then(function(scrollTop) {
                    expect(scrollTop).toBeGreaterThan(0);
                });

            }).then(function() {
                browser.switchToParent();
                var iFrame = element(by.css('iframe'));
                dragToPositionFromElement(iFrame, 0, 0);
                dropDraggedElement();
                browser.sleep(1000);
                expect(element(by.css(".UIhintDragAndDrop.bottom")).isDisplayed()).toBeFalsy();
            });
        });

        it('WHEN I drag a component from the outer frame into the inner frame and position the mouse on the top UI Hint, THEN I expect the page to scroll down until the mouse is moved out', function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();
            browser.switchToParent();

            // Scroll via protractor to an initial position
            scrollIframe(120);

            // No 'SmartEdit' scrolling yet
            startDraggingElement('#component0');
            expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);
            expect(element(by.css(".UIhintDragAndDrop.top")).isDisplayed()).toBeTruthy();

            browser.switchToParent();
            getScrollTop().then(function(scrollTop) {
                expect(scrollTop).not.toBe(0);
            });

            // Scroll up for one second
            dragToElement('.UIhintDragAndDrop.top').then(function() {
                browser.sleep(1000);

                getScrollTop().then(function(scrollTop) {
                    expect(scrollTop).toBe(5);
                });

            }).then(function() {
                browser.switchToParent();
                var iframe = element(by.css('iframe'));
                dragToPositionFromElement(iframe, 0, 0);
                dropDraggedElement();
                browser.sleep(1000);
                expect(element(by.css(".UIhintDragAndDrop.top")).isDisplayed()).toBeFalsy();
            });
        });
    });
});
