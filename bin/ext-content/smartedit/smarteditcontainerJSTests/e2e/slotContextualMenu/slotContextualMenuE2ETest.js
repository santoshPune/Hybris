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
describe('Slot Contextual Menu Decorator', function() {

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/slotContextualMenu/slotContextualMenuTest.html');
        browser.waitForWholeAppToBeReady();
        browser.driver.manage().timeouts().implicitlyWait(1000);

        perspectives = require("../utils/components/Perspectives.js");
        perspectives.selectPerspective(perspectives.DEFAULT_PERSPECTIVES.ALL);
        browser.waitForWholeAppToBeReady();

        browser.switchToIFrame();
    });

    xit('WHEN I hover over a content slot THEN the ID of the slot appears in the slot contextual menu decorator', function() {
        expect(slotNamePanels().count()).toBe(0);

        hoverOverSlot('topHeaderSlot');
        expect(slotNamePanels().count()).toBe(1);
        expect(slotNamePanel().getText()).toContain('topHeaderSlot');

        hoverOverSlot('bottomHeaderSlot');
        expect(slotNamePanels().count()).toBe(1);
        expect(slotNamePanel().getText()).toContain('bottomHeaderSlot');

        hoverOverSlot('footerSlot');
        expect(slotNamePanels().count()).toBe(1);
        expect(slotNamePanel().getText()).toContain('footerSlot');

        hoverOverSlot('otherSlot');
        expect(slotNamePanels().count()).toBe(1);
        expect(slotNamePanel().getText()).toContain('otherSlot');
    });

    it('WHEN I hover over a content slot THEN the slot contextual menu items appear in the slot contextual menu decorator', function() {
        expect(contextualMenuItemForSlot('slot.context.menu.title.dummy1', 'topHeaderSlot').isPresent()).toBe(false);

        hoverOverSlot('topHeaderSlot');
        expect(contextualMenuItemForSlot('slot.context.menu.title.dummy1', 'topHeaderSlot').isPresent()).toBe(true);
    });

    it('WHEN I click one of the slot contextual menu items THEN I expect the callback to be triggered', function() {
        hoverOverSlot('topHeaderSlot');
        expect(contextualMenuItemForSlot('slot.context.menu.title.dummy1', 'topHeaderSlot').isPresent()).toBe(true);
        expect(contextualMenuItemForSlot('slot.context.menu.title.dummy2', 'topHeaderSlot').isPresent()).toBe(true);

        contextualMenuItemForSlot('slot.context.menu.title.dummy1', 'topHeaderSlot').click();
        expect(contextualMenuItemForSlot('slot.context.menu.title.dummy1', 'topHeaderSlot').isPresent()).toBe(true);
        expect(contextualMenuItemForSlot('slot.context.menu.title.dummy2', 'topHeaderSlot').isPresent()).toBe(true);
    });

    // Actions
    function hoverOverSlot(slotName) {
        browser.click(by.css('.smartEditComponentX[data-smartedit-component-id="' + slotName + '"]'));
    }

    // Elements
    function slotNamePanels() {
        return element.all(by.css('.decorative-panel-area .decorative-panel-text'));
    }

    function slotNamePanel() {
        return element(by.css('.decorative-panel-area .decorative-panel-text'));
    }

    function contextualMenuItemForSlot(key, slotID) {
        return element(by.id(key + '-' + slotID + '-ContentSlot'));
    }

    function moreMenuForSlot() {
        return element(by.css('ul.dropdown-menu'));
    }

    function moreMenuItem(key) {
        return element(by.cssContainingText('a', key));
    }


});
