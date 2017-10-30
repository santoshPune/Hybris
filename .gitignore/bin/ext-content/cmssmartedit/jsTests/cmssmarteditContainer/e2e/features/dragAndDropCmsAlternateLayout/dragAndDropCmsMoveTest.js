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
(function() {
    //we do not seem to be able to specify position within slot with the current selenium api : playing with coordinates always puts at the end of slot
    describe('E2E Test for CMS drag and drop service within page', function() {

        beforeEach(function() {
            browser.get('jsTests/cmssmarteditContainer/e2e/features/dragAndDropCmsAlternateLayout/dragAndDropCmsTest.html');

            browser.waitForWholeAppToBeReady();
            selectPerspective('se.cms.perspective.basic');
        });

        xit("component1 will successfully move to empty slot4", function() {

            browser.switchToIFrame();

            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            expect(element(by.css('#slot4 #component1')).isPresent()).toBe(false);

            moveMouseToElement('topHeaderSlot', 'component1').then(function() {
                browser.actions()
                    .mouseDown()
                    .perform();

                browser.switchToParent();
                return dragToPositionFromElement(element(by.css('iframe')), 0, 0).then(function() {
                    browser.switchToIFrame();

                    return getSlot('otherSlot').getLocation();

                });

            }).then(function(position) {
                browser.actions()
                    .mouseMove({
                        x: position.x + 1,
                        y: position.y + 1
                    }).perform();

                expect(getSlot('otherSlot').getAttribute('class')).toContain('over-slot-enabled');

            });

            dropDraggedElement();

            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(false);
            expect(element(by.css('#slot4 #component1')).isPresent()).toBe(true);

        });


        xit("will set eligible-slot css class to all eligible containers upon starting to drag", function() {

            browser.switchToIFrame();

            moveMouseToElement('topHeaderSlot', 'component1').then(function() {
                browser.actions()
                    .mouseDown()
                    .mouseMove({
                        x: 1,
                        y: 0
                    }) // just move 1px to the right
                    .perform();

                expect(element.all(by.css('.smartEditComponent.eligible-slot')).count()).toBe(4);

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
