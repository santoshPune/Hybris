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
var PLUS_BUTTON_SELECTOR = "div.ySmartEditExperienceSelectorToolbar button.yHybridAction.ySEComponentMenuW--button img[src='../cmssmartedit/images/icon_add.png']";

module.exports = {
    hasAddComponentButton: function() {
        expect(element(by.css(PLUS_BUTTON_SELECTOR)).isPresent()).toBe(true);
    },

    doesNotHaveAddComponentButton: function() {
        expect(by.css(PLUS_BUTTON_SELECTOR)).toBeAbsent();
    }
};
