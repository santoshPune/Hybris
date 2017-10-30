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
    visibilityButtonBySlotId: function(slotId) {
        return element(by.id('slot-visibility-button-' + slotId));
    },
    visibilityDropdownBySlotId: function(slotId) {
        return element(by.id('slot-visibility-list-' + slotId));
    },
    visibilityListBySlotId: function(slotId) {
        return this.visibilityDropdownBySlotId(slotId).all(by.css('li'));
    },
    sharedSlotButtonBySlotId: function(slotId) {
        return element(by.id('sharedSlotButton-' + slotId));
    },
    sharedSlotDropdownBySlotId: function(slotId) {
        return element(by.id('shared-slot-list-' + slotId));
    }
};
