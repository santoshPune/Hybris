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

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/dragAndDrop/dragAndDrop/dragAndDropTest.html');
    });

    describe('within iframe', function() {

        it("component1 will not move to slot2 as slot2 is disabled", function() {

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            browser.click("#component1", "component1 not found");

            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            expect(by.css('#slot2 #component1')).toBeAbsent();

            expect(element(by.css('#slot2')).getAttribute('class')).not.toContain('over-slot-disabled');

            startDraggingElement('#component1');
            dragToElement('#slot3');
            //drag component1 over slot2
            dragToElement('#slot2').then(function() {
                expect(element(by.css('#slot2')).getAttribute('class')).toContain('over-slot-disabled');
            });

            //drop component1 over slot2
            dropDraggedElement();

            browser.switchToParent();
            expect(element(by.binding('alert.message')).getText()).toBe('This item cannot be dropped to slot:slot2'); //assert drag and drop failure

            browser.switchToIFrame();

            expect(element(by.css('#slot1 #component1')).isPresent()).toBe(true);
            expect(by.css('#slot2 #component1')).toBeAbsent();

        });


        it('WHEN I sort a component in the inner frame and hover a non-eligible slot, THEN I expect the slot to contain a placeholder telling me I can\'t drop the component', function() {

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();

            startDraggingElement('#slot1 #component3');
            expect(element(by.id(HELPER_ID)).isPresent()).toBe(true);

            dragToElement('#slot2 #component4');
            expect(element(by.css('#slot2 ' + '.' + CSS_CLASSES.SORTABLE_PLACEHOLDER)).isPresent()).toBe(true);
            expect(element(by.css('#slot2')).getAttribute('class')).toContain(CSS_CLASSES.HIGHLIGHTED_SLOT);
            expect(element(by.css('#slot2' + '.' + CSS_CLASSES.INVALID_SLOT)).isPresent()).toBe(true);
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
