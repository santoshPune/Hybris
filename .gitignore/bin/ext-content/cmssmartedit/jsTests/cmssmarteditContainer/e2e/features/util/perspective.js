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

    ADVANCED_CMS_PERSPECTIVE: "Advanced CMS",
    BASIC_CMS_PERSPECTIVE: "Basic CMS",

    select: function(perspectiveName) {
        return browser.switchToParent().then(function() {
            return element(by.css('.ySEPerspectiveSelector button')).getText().then(function(perspectiveSelected) {
                if (perspectiveSelected.toUpperCase() !== perspectiveName.toUpperCase()) {
                    return browser.click(by.css('.ySEPerspectiveSelector')).then(function() {
                        return browser.click(by.cssContainingText('.ySEPerspectiveSelector ul li ', perspectiveName), "perspective " + perspectiveName + " is not clickable");
                    });
                } else {
                    return;
                }
            });
        });
    },

    selectBasicPerspective: function() {
        this.select(BASIC_CMS_PERSPECTIVE);
    },

    selectAdvancedPerspective: function() {
        this.select(ADVANCED_CMS_PERSPECTIVE);
    }
};
