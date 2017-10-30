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
module.exports = {
    COMPONENT_1_ID: 'component1',
    COMPONENT_2_ID: 'component2',
    COMPONENT_3_ID: 'component3',
    TOP_HEADER_SLOT_ID: 'topHeaderSlot',
    OTHER_SLOT_ID: 'otherSlot',

    componentButton: function() {
        return element(by.css('#submitButton'));
    },

    secondComponentButton: function() {
        return element(by.css('#secondaryButton'));
    },

    component1: function() {
        return this.componentById(this.COMPONENT_1_ID);
    },

    component2: function() {
        return this.componentById(this.COMPONENT_2_ID);
    },

    component3: function() {
        return this.componentById(this.COMPONENT_3_ID);
    },

    componentById: function(componentId) {
        return element(by.css('.smartEditComponent[data-smartedit-component-id=' + componentId + ']'));
    },

    secondPageLink: function() {
        return element(by.id('deepLink'));
    },

    addToCartButton: function() {
        return element(by.id('addToCart'));
    },

    addToCartFeedback: function() {
        return element(by.id('feedback'));
    },

    secondPage: {
        component2: function() {
            return element(by.css('#component2 div'));
        }
    },

    deepLink: function() {
        browser.switchToIFrame();
        this.secondPageLink().click();
        browser.switchToParent();
        browser.waitForWholeAppToBeReady();
    },

    moveToComponent: function(componentId) {
        browser.actions()
            .mouseMove(this.componentById(componentId))
            .perform();
    }
};
