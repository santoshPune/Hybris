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
require('../../../dragAndDropFunctions.js');

//we do not seem to be able to specify position within slot with the current selenium api : playing with coordinates always puts at the end of slot
describe('E2E Test for drag and drop service', function() {
    var alerts;

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/dragAndDrop/dragAndDrop/dragAndDropTest.html');
        alerts = require("../../../../utils/components/Alerts.js");
    });

    describe('within iframe', function() {
        it("component1 will move to slot3, which is enabled and move back to slot1", function() {

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            browser.click("#component1", "component1 not found");

            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            expect(element(by.css('#slot3 #component5')).isPresent()).toBe(true);
            expect(by.css('#slot3 #component1')).toBeAbsent();

            //drag and drop component1 onto slot3
            expect(element(by.css('#slot3')).getAttribute('class')).not.toContain('active-slot');
            expect(element(by.css('#slot3')).getAttribute('class')).not.toContain('over-slot-disabled');

            startDraggingElement('#component1');
            dragToElement('#slot3').then(function() {
                dragToElement('#component5')
                    .then(function() {
                        expect(element(by.css('#slot3')).getAttribute('class')).toContain('eligible-slot');
                    });
            });

            //drop component1 over slot3
            dropDraggedElement();

            browser.switchToParent();
            expect(alerts.alertMsg().isDisplayed()).toBe(true); //alert message to be displayed
            expect(element(by.binding('alert.message')).getText()).toBe('This item is dropped to slot:slot3 at index:1'); //assert successful drag and drop

            browser.switchToIFrame();

            expect(by.css('#slot1 #component1')).toBeAbsent();
            expect(element(by.css('#slot3 #component1')).isPresent()).toBe(true);

            startDraggingElement('#component1');
            dragToElement('#slot1');
            //drop component1 over slot1
            dropDraggedElement().then(function() {
                expect(by.css('#slot3 #component1')).toBeAbsent();
                expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            });
        });

        it("secondaryComponent1 will move to secondarySlot2 and then move back to secondarySlot1", function() {

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            browser.switchToParent();
            scrollIframe(250);
            browser.switchToIFrame();

            browser.click("#component1", "component1 not found");

            expect(element(by.css('#secondarySlot1 #secondaryComponent1')).isPresent()).toBe(true);
            expect(by.css('#secondarySlot2 #secondaryComponent1')).toBeAbsent();

            startDraggingElement('#secondaryComponent1');
            dragToElement('#secondarySlot2');
            dropDraggedElement().then(function() {
                expect(by.css('#secondarySlot1 #secondaryComponent1')).toBeAbsent();
                expect(element(by.css('#secondarySlot2 #secondaryComponent1')).isPresent()).toBe(true);
            });

            browser.sleep(1000);

            startDraggingElement('#secondaryComponent1');
            dragToElement('#secondarySlot1');
            dropDraggedElement().then(function() {
                expect(element(by.css('#secondarySlot1 #secondaryComponent1')).isPresent()).toBe(true);
                expect(by.css('#secondarySlot2 #secondaryComponent1')).toBeAbsent();
            });

        });

        xit('WHEN I sort a component in the inner frame and position the mouse on the bottom UI Hint, THEN I expect the page to scroll down until I move the mouse out of the UI hint', function() {

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            startDraggingElement('#slot1 #component1');
            expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);

            browser.switchToParent();
            getScrollTop().then(function(scrollTop) {
                expect(scrollTop).toBe(0);
            });

            expect(element(by.css(".UIhintDragAndDrop.bottom")).isDisplayed()).toBeTruthy();

            dragToElement('.UIhintDragAndDrop.bottom').then(function() {
                browser.sleep(1000);
                browser.switchToParent();

                getScrollTop().then(function(scrollTop) {
                    expect(scrollTop).toBeGreaterThan(0);
                });

            }).then(function() {
                browser.switchToParent();
                var iframe = element(by.css('iframe'));
                dragToPositionFromElement(iframe, 0, 0);
                dropDraggedElement();
                browser.sleep(1000);
                expect(element(by.css(".UIhintDragAndDrop.bottom")).isDisplayed()).toBeFalsy();
            });

        });


        it('WHEN I sort a component in the inner frame and position the mouse on the top UI Hint, THEN I expect the page to scroll up until I move the mouse out of the UI hint', function() {

            var currentScrollTop = 0;

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();
            browser.switchToParent();

            scrollIframe(120);

            browser.switchToIFrame();

            startDraggingElement('#slot1 #component3');
            expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);

            browser.switchToParent();
            getScrollTop().then(function(scrollTop) {
                currentScrollTop = scrollTop;
                expect(scrollTop).not.toBe(0);
            });

            expect(element(by.css(".UIhintDragAndDrop.top")).isDisplayed()).toBeTruthy();

            dragToElement('.UIhintDragAndDrop.top').then(function() {
                browser.sleep(1000);
                browser.switchToParent();

                getScrollTop().then(function(scrollTop) {
                    expect(scrollTop).toBeLessThan(currentScrollTop);
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

        it('WHEN I sort a component in the inner frame and hover an eligible slot, THEN I expect the slot to contain a placeholder telling me that I can drop the component', function() {

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            startDraggingElement('#slot1 #component3');
            expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);

            dragToElement('#slot1 #component2');
            expect(element(by.css('#slot1 ' + '.' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).isPresent()).toBe(true);
            expect(element(by.css('#slot1')).getAttribute('class')).toContain(CSS_CLASSES.HIGHLIGHTED_SLOT);
            expect(element.all(by.css('.' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).count()).toEqual(1);

            dragToElement('#slot3 #component5');
            expect(element(by.css('#slot3 ' + '.' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).isPresent()).toBe(true);
            expect(element(by.css('#slot3')).getAttribute('class')).toContain(CSS_CLASSES.HIGHLIGHTED_SLOT);
            expect(element.all(by.css('.' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).count()).toEqual(1);

            browser.switchToParent();
            var iframe = element(by.css('iframe'));
            dragToPositionFromElement(iframe, 0, 0);
            dropDraggedElement();

            expect(by.id(HELPER_ID)).toBeAbsent();
            expect(element.all(by.css('.' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).count()).toEqual(0);

        });

    });

});
