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
describe('E2E Test for decorator service module', function() {
    var perspectives = require("../utils/components/Perspectives.js");
    var page = require('../utils/components/Page.js');
    var storefront = require('../utils/components/Storefront.js');

    beforeEach(function() {
        page.getAndWaitForWholeApp('smarteditcontainerJSTests/e2e/decorator/decoratorTest.html');
        perspectives.selectPerspective(perspectives.DEFAULT_PERSPECTIVES.ALL);
        page.setWaitForPresence(0);
        browser.switchToIFrame();
    });

    it('WHEN a component is wired with a single decorator THEN I expect only that decorator to be present with the transcluded content', function() {
        expect(storefront.component1().getText()).toContain('test component 1');
        expect(perspectives.getElementInOverlay('component1', 'componentType1').getText()).toContain('Text_is_been_displayed_TextDisplayDecorator');
        expect(perspectives.getElementInOverlay('component1', 'componentType1').getText()).not.toContain('Button_is_been_Displayed');
    });

    it('WHEN a component is wired with some other decorator THEN I expect only that decorator to be present with the transcluded content', function() {
        expect(storefront.component2().getText()).toContain('test component 2');
        expect(perspectives.getElementInOverlay('component2', 'componentType2').getText()).not.toContain('Text_is_been_displayed_TextDisplayDecorator');
        expect(perspectives.getElementInOverlay('component2', 'componentType2').getText()).toContain('Button_is_been_Displayed');
    });

    it('WHEN a component is wired with multiple decorators THEN I expect those decorators to be present with the transcluded content', function() {
        expect(storefront.component3().getText()).toContain('test component 3');
        expect(perspectives.getElementInOverlay('component3', 'componentType3').getText()).toContain('Text_is_been_displayed_TextDisplayDecorator');
        expect(perspectives.getElementInOverlay('component3', 'componentType3').getText()).toContain('Button_is_been_Displayed');
    });

    it('WHEN decorators are wired for both a component and its slot THEN I expect to see both slot and component decorators appear', function() {
        expect(perspectives.getElementInOverlay('topHeaderSlot', 'ContentSlot').getText()).toContain('slot_text_is_been_displayed_SlotTextDisplayDecorator');
        expect(perspectives.getElementInOverlay('topHeaderSlot', 'ContentSlot').getText()).toContain('Slot_button_is_been_Displayed');
    });

    it('WHEN switching back to preview mode, THEN add to cart button still works', function() {
        perspectives.selectPerspective(perspectives.DEFAULT_PERSPECTIVES.NONE);
        browser.switchToIFrame();
        storefront.addToCartButton().click();
        expect(storefront.addToCartFeedback().getText()).toBe('1');
    });
});
