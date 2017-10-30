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
describe('E2E Test for drag and drop service with helpers', function() {

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/dragAndDrop/dragAndDropWithHelpers/dragAndDropWithHelpersTest.html');
    });

    describe('within iframe', function() {

        it("when handler is included in configuration, dragged icon will change to handler and stay until mouse is released", function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            expect(element(by.css('#slot1 #component3')).isPresent()).toBe(true);
            expect(by.id(HELPER_ID)).toBeAbsent();

            //drag component1 in sortable to the third position in the slot
            var elementToDrag = element(by.id('component1'));

            startDraggingElement('#component1');

            dragToElement('#slot1 #component3')
                .then(function() {
                    expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);
                    return getElementAttributes(elementToDrag);
                })
                .then(function(originalItemProperties) {
                    var helperContent = element(by.css("#" + HELPER_ID + " > img"));

                    getElementAttributes(helperContent).then(function(helperProperties) {
                        hasCopiedProperties(originalItemProperties, helperProperties);
                    });
                    dropDraggedElement();
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
            browser.waitForFrameToBeReady();

            browser.switchToParent();
            scrollIframe(150);
            browser.switchToIFrame();

            browser.click("#component1", "component1 not found");

            expect(element(by.css('#slot1 #component3')).isPresent()).toBe(true);
            expect(element(by.css('#slot3 #component5')).isPresent()).toBe(true);
            expect(by.css('#slot3 #component3')).toBeAbsent();
            expect(by.id(HELPER_ID)).toBeAbsent();

            //drag component1 in sortable to the third position in the slot
            startDraggingElement('#component3');
            dragToElement('#slot3 #component5')
                .then(function() {
                    expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);
                    dropDraggedElement();
                })
                .then(function() {
                    expect(by.id(HELPER_ID)).toBeAbsent();

                    expect(by.css('#slot1 #component3')).toBeAbsent();
                    expect(element(by.css('#slot3 #component3')).isPresent()).toBe(true);
                });

        });

        it("when handler is included in configuration, dragged icon will change to handler and stay until mouse is released in restricted slot", function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            expect(by.css('#slot2 #component1')).toBeAbsent();
            expect(by.id(HELPER_ID)).toBeAbsent();

            //drag component1 in sortable to the third position in the slot
            startDraggingElement('#component1');
            dragToElement('#slot3')
                .then(function() {
                    return dragToElement('#slot2');
                })
                .then(function() {
                    expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);
                    dropDraggedElement();
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


        it('WHEN I sort a component in the inner frame and position the mouse on the bottom UI Hint, THEN I expect the page to scroll down until I move the mouse out of the UI hint', function() {

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
            var currentScrollTop;
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

        it('WHEN I sort a component in the inner frame and hover a slot, THEN I expect the slot to contain a placeholder telling me if I can or can\'t drop the component', function() {

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            startDraggingElement('#slot1 #component3');
            expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);

            dragToElement('#slot1 #component2');

            browser.wait(protractor.ExpectedConditions.presenceOf(element(by.css('#slot1 .' + CSS_CLASSES.SORTABLE_PLACEHOLDER))), 5000, 'Slot 1 with sortable placeholder class not found');

            expect(element(by.css('#slot1 ' + '.' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).isPresent()).toBe(true);
            expect(element(by.css('#slot1')).getAttribute('class')).toContain(CSS_CLASSES.HIGHLIGHTED_SLOT);
            expect(element.all(by.css('.' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).count()).toEqual(1);


            dragToElement('#slot2');
            dragToElement('#slot2 #component4');

            browser.wait(protractor.ExpectedConditions.presenceOf(element(by.css('#slot2 ' + '.' + CSS_CLASSES.SORTABLE_PLACEHOLDER))), 5000, 'Slot 2 with sortable placeholder class not found');
            browser.wait(protractor.ExpectedConditions.presenceOf(element(by.css('#slot2' + '.' + CSS_CLASSES.INVALID_SLOT))), 5000, 'Slot 2 with invalid slot class not found');

            expect(element(by.css('#slot2 ' + '.' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).isPresent()).toBe(true);
            expect(element(by.css('#slot2')).getAttribute('class')).toContain(CSS_CLASSES.HIGHLIGHTED_SLOT);
            expect(element(by.css('#slot2' + '.' + CSS_CLASSES.INVALID_SLOT)).isPresent()).toBe(true);
            expect(element.all(by.css('.' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).count()).toEqual(1);

            dragToElement('#slot3 #component5');

            browser.wait(protractor.ExpectedConditions.presenceOf(element(by.css('#slot3 .' + CSS_CLASSES.SORTABLE_PLACEHOLDER))), 5000, 'Slot 3 with sortable placeholder class not found');

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

        // Helper Functions
        function getElementAttributes(element) {
            return browser.executeScript(
                'var items = {}; ' +
                'for ( var i = 0; i < arguments[0].attributes.length; i++) { ' +
                'items[arguments[0].attributes[i].nodeName] = arguments[0].attributes[i].nodeValue ' +
                '};' +
                'return items;', element.getWebElement());
        }

        function hasCopiedProperties(expectedAttributes, actualAttributes) {

            for (var key in expectedAttributes) {
                if (key !== 'id' && key !== 'class' && key !== 'style' && key !== 'data-content') {
                    expect(actualAttributes[key]).not.toBeUndefined();
                    expect(actualAttributes[key]).toBe(expectedAttributes[key]);
                }
            }
        }
    });

});
