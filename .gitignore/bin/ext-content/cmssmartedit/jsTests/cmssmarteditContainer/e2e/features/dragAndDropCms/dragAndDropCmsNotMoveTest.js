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
//we do not seem to be able to specify position within slot with the current selenium api : playing with coordinates always puts at the end of slot
(function() {
    describe('E2E Test for CMS drag and drop service within page', function() {

        beforeEach(function() {
            browser.get('jsTests/cmssmarteditContainer/e2e/features/dragAndDropCms/dragAndDropCmsTest.html');

            browser.waitForWholeAppToBeReady();
            selectPerspective('se.cms.perspective.basic');
        });

        xit("component1 will not move to slot2 as slot2 is disabled", function() {

            //this test is not testing drag and drop; it is testing the message service
            browser.switchToIFrame();

            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            expect(element(by.css('#slot2 #component1')).isPresent()).toBe(false);

            moveMouseToElement('topHeaderSlot', 'component1').then(function() {
                browser.actions()
                    .mouseDown()
                    .perform();

                browser.switchToParent();
                dragToPositionFromElement(element(by.css('iframe')), 0, 0);
                browser.switchToIFrame();

                return getSlot('bottomHeaderSlot').getLocation();

            }).then(function(position) {
                browser.actions()
                    .mouseMove({
                        x: position.x + 1,
                        y: position.y + 1
                    }).perform();
                expect(getSlot('bottomHeaderSlot').getAttribute('class')).toContain('over-slot-disabled');
            });

            dropDraggedElement();

            browser.switchToParent();
            expect(element(by.binding('alert.message')).getText()).toBe('Component component1 not allowed in bottomHeaderSlot.'); //assert drag and drop failure

            browser.switchToIFrame();
            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            expect(element(by.css('#slot2 #component1')).isPresent()).toBe(false);

        });

        xit("component2 will fail to move to slot3 because of move API error", function() {

            browser.switchToIFrame();

            expect(element(by.css('#slot1 #component2')).isPresent()).toBe(true);
            expect(element(by.css('#slot3 #component2')).isPresent()).toBe(false);

            moveMouseToElement('topHeaderSlot', 'component2').then(function() {
                browser.actions()
                    .mouseDown()
                    .perform();

                browser.switchToParent();
                return dragToPositionFromElement(element(by.css('iframe')), 0, 0).then(function() {
                    browser.switchToIFrame();

                    return getSlot('footerSlot').getLocation();
                });

            }).then(function(position) {

                browser.actions()
                    .mouseMove({
                        x: position.x + 1,
                        y: position.y + 1
                    }).perform();

                expect(getSlot('footerSlot').getAttribute('class')).toContain('over-slot-enabled');
            });

            dropDraggedElement();

            browser.switchToParent();
            expect(element(by.binding('alert.message')).getText()).toBe('failed to move component component2 to slot footerSlot'); //assert drag and drop failure

            browser.switchToIFrame();
            expect(element(by.css('#slot1 #component2')).isPresent()).toBe(true);
            expect(element(by.css('#slot3 #component2')).isPresent()).toBe(false);

        });

    });

    describe('E2E Test for CMS drag and drop service within page when perspective NONE is selected', function() {

        beforeEach(function() {
            browser.get('jsTests/cmssmarteditContainer/e2e/features/dragAndDropCms/dragAndDropCmsTest.html');

            browser.waitForWholeAppToBeReady();
        });


        it("THEN drag and drop is disabled", function() {

            browser.switchToIFrame();

            moveMouseToText('test component 2').then(function() {
                return expect(getDnDButton('component2').isPresent()).toBe(false);
            });

        });

    });

    function getSlot(smartEditComponentId) {
        return element(by.css(".smartEditComponent[data-smartedit-component-id='" + smartEditComponentId + "']"));
    }

    // HELPER FUNCTIONS
    function moveMouseToElement(slotId, componentId) {
        browser.actions()
            .mouseMove(element(by.css(".smartEditComponentX[data-smartedit-component-id='" + slotId + "']")))
            .perform();
        browser.actions()
            .mouseMove(element(by.css(".smartEditComponentX[data-smartedit-component-id='" + componentId + "']")))
            .perform();
        return browser.actions()
            .mouseMove(element(by.css(".smartEditComponentX[data-smartedit-component-id='" + componentId + "'] .movebutton")))
            .perform();
    }

    function moveMouseToText(text) {
        return browser.actions()
            .mouseMove(element(by.cssContainingText('div', text)))
            .perform();

    }

    function getDnDButton(componentId) {
        return element(by.css(".smartEditComponentX[data-smartedit-component-id='" + componentId + "'] .movebutton"));
    }

    function dropDraggedElement() {
        return browser.actions().mouseUp().perform();
    }

    function dragToPositionFromElement(ele, xCoord, yCoord) {

        //show drag and drop button
        return browser.actions()
            .mouseMove(ele, {
                x: xCoord,
                y: yCoord
            })
            .perform();

    }

    function selectPerspective(perspectiveName) {
        $('.ySEPerspectiveSelector').click();
        element(by.cssContainingText('.ySEPerspectiveSelector li a', perspectiveName)).click();
    }

})();
