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

    TOP_HEADER_SLOT_ID: 'topHeaderSlot',
    BOTTOM_HEADER_SLOT_ID: 'bottomHeaderSlot',
    FOOTER_SLOT_ID: 'footerSlot',
    OTHER_SLOT_ID: 'otherSlot',

    componentById: function(componentId) {
        return element(by.css('.smartEditComponent[data-smartedit-component-id=' + componentId + ']'));
    },
    slotById: function(slotId) {
        return this.componentById(slotId);
    },
    moveToComponent: function(componentId) {
        browser.switchToIFrame();
        browser.actions()
            .mouseMove(this.componentById(componentId))
            .perform();
    }
};
