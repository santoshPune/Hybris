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
describe('BasicSlotContextualMenuDecorator', function() {

    var perspectives = require("../utils/components/Perspectives.js");
    var page = require('../utils/components/Page.js');
    var slotContextualMenu = require('../utils/components/SlotContextualMenu.js');
    var storefront = require('../utils/components/Storefront.js');

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/basicSlotContextualMenu/basicSlotContextualMenuTest.html');
        browser.waitForWholeAppToBeReady();
        perspectives.selectPerspective(perspectives.DEFAULT_PERSPECTIVES.ALL);
        page.setWaitForPresence(1000);
        browser.switchToIFrame();
    });

    it('GIVEN I am in a mode in which the slot contextual menu decorator is enabled THEN the borders should be hidden by default', function() {
        expect(slotContextualMenu.getDottedSlotBorderForNonEmptySlot().isPresent()).toBe(false, 'Expected slot contextual menu for non-empty slot not to be present');
        expect(slotContextualMenu.getDottedSlotBorderForEmptySlot().isPresent()).toBe(false, 'Expected slot contextual menu for empty slot not to be present');
    });

    it('GIVEN I am in a mode in which the slot contextual menu decorator is enabled WHEN I hover over a non-empty slot THEN the border should be visible', function() {
        storefront.moveToComponent(storefront.TOP_HEADER_SLOT_ID);
        expect(slotContextualMenu.getDottedSlotBorderForNonEmptySlot().isPresent()).toBe(true, 'Expected slot contextual menu for non-empty slot to be present');
        expect(slotContextualMenu.getDottedSlotBorderForNonEmptySlot().isDisplayed()).toBe(true, 'Expected slot contextual menu for non-empty slot to be visible');
    });

    it('GIVEN I am in a mode in which the slot contextual menu decorator is enabled WHEN I hover over an empty slot THEN the border should not be visible', function() {
        storefront.moveToComponent(storefront.OTHER_SLOT_ID);
        expect(slotContextualMenu.getDottedSlotBorderForEmptySlot().isPresent()).toBe(false, 'Expected slot contextual menu for empty slot not to be present');
    });
});
