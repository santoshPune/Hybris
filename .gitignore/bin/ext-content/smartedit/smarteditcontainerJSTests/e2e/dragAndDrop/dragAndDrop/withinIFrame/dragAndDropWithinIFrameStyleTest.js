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

    describe('within iframe', function() {

        it("will set minimum slot size and border color to slot1 when component1 dragging starts", function() {

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();

            browser.click("#component1", "component1 not found");

            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            expect(by.css('#slot2 #component1')).toBeAbsent();

            expect(element(by.css('#slot1')).getAttribute('class')).not.toContain('eligible-slot');
            expect(element(by.css('#slot1')).getAttribute('class')).not.toContain('active-slot');

            browser.actions().mouseMove(element(by.id('component1'))).mouseDown()
                .mouseMove({
                    x: 1,
                    y: 0
                }) // just move 1px to the right
                .perform()
                .then(function() {
                    //assert that the startCallback is triggered and the style (slot size) is applied
                    expect(element(by.css('#slot1')).getAttribute('class')).toContain('eligible-slot');
                    expect(element(by.css('#slot1')).getAttribute('class')).toContain('active-slot');
                });
        });

        it("when component1 is dragged will set the styles and will remove minimum slot size and border color styles to slot1 when mouse is released", function() {

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();

            browser.click("#component1", "component1 not found");

            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            expect(by.css('#slot2 #component1')).toBeAbsent();

            expect(element(by.css('#slot1')).getAttribute('class')).not.toContain('eligible-slot');
            expect(element(by.css('#slot1')).getAttribute('class')).not.toContain('active-slot');

            browser.actions().mouseMove(element(by.id('component1'))).mouseDown()
                .mouseMove({
                    x: 1,
                    y: 0
                }) // just move 1px to the right
                .perform()
                .then(function() {
                    //assert that the startCallback is triggered and the style (slot size) is applied
                    expect(element(by.css('#slot1')).getAttribute('class')).toContain('eligible-slot');
                    expect(element(by.css('#slot1')).getAttribute('class')).toContain('active-slot');
                });

            browser.actions().mouseUp()
                .perform()
                .then(function() {
                    //assert that the stopCallback is triggered and the style (slot size) is removed
                    expect(element(by.css('#slot1')).getAttribute('class')).not.toContain('eligible-slot');
                    expect(element(by.css('#slot1')).getAttribute('class')).not.toContain('active-slot');
                });
        });

        it("when handler is included in configuration, dragged icon will change to handler and stay until mouse is released", function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();

            browser.click("#component1", "component1 not found");

            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            expect(element(by.css('#slot1 #component3')).isPresent()).toBe(true);
            expect(by.id(HELPER_ID)).toBeAbsent();

            //drag component1 in sortable to the third position in the slot
            browser.actions().mouseMove(element(by.id('component1')))
                .mouseDown()
                .mouseMove(element(by.id('component3')))
                .perform()
                .then(function() {
                    expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);
                    return browser.actions().mouseUp().perform();
                })
                .then(function() {
                    expect(by.id(HELPER_ID)).toBeAbsent();

                    expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
                    expect(element(by.css('#slot1 #component3')).isPresent()).toBe(true);
                });
        });

        it("between sortables, when handler is included in configuration, dragged icon will change to handler and stay until mouse is released", function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();

            browser.click("#component1", "component1 not found");

            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            expect(element(by.css('#slot3 #component5')).isPresent()).toBe(true);
            expect(by.css('#slot3 #component1')).toBeAbsent();
            expect(by.id(HELPER_ID)).toBeAbsent();

            //drag component1 in sortable to the third position in the slot
            browser.actions()
                .mouseMove(element(by.id('component1')))
                .mouseDown()
                .mouseMove(element(by.id('slot3')))
                .mouseMove(element(by.id('component5'))).perform()
                .then(function() {
                    expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);
                    return browser.actions().mouseUp().perform();
                })
                .then(function() {
                    expect(by.id(HELPER_ID)).toBeAbsent();

                    expect(by.css('#slot1 #component1')).toBeAbsent();
                    expect(element(by.css('#slot3 #component1')).isPresent()).toBe(true);
                });
        });

        it("when handler is included in configuration, dragged icon will change to handler and stay until mouse is released in restricted slot", function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();

            browser.click("#component1", "component1 not found");

            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            expect(by.css('#slot2 #component1')).toBeAbsent();
            expect(by.id(HELPER_ID)).toBeAbsent();

            //drag component1 in sortable to the third position in the slot
            browser.actions()
                .mouseMove(element(by.id('component1'))).mouseDown().mouseMove(element(by.id('slot3'))).perform()
                .then(function() {
                    return browser.actions().mouseMove(element(by.id('slot2'))).perform();
                })
                .then(function() {
                    expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);
                    return browser.actions().mouseUp().perform();
                })
                .then(function() {
                    expect(by.id(HELPER_ID)).toBeAbsent();

                    expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
                    expect(by.css('#slot2 #component1')).toBeAbsent();
                });
        });


        it("GIVEN that I am on the store front, WHEN I sort a component in the inner frame, THEN I expect to see the UI Hints at the top and the bottom of the page", function() {

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            startDraggingElement('#slot1 #component1');

            browser.sleep(500);

            browser.switchToParent();

            expect(element(by.css(".UIhintDragAndDrop.top")).isDisplayed()).toBeTruthy();
            expect(element(by.css(".UIhintDragAndDrop.bottom.firefox-fix-applied")).isPresent()).toBeTruthy();

            dropDraggedElement();

            browser.sleep(1500);

            expect(element(by.css(".UIhintDragAndDrop.top")).isDisplayed()).toBeFalsy();
            expect(element(by.css(".UIhintDragAndDrop.bottom")).isDisplayed()).toBeFalsy();

        });

    });


});
