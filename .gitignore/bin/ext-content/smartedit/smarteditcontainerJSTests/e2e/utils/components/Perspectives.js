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
    DEFAULT_PERSPECTIVES: {
        ALL: 'perspective.all.name',
        NONE: 'perspective.none.name'
    },
    selectPerspective: function(perspectiveName) {
        browser.switchToParent();

        element(by.css('.ySEPerspectiveSelector button')).getText().then(function(perspectiveSelected) {
            if (perspectiveSelected.toUpperCase() !== perspectiveName.toUpperCase()) {
                $('.ySEPerspectiveSelector').click();
                element(by.cssContainingText('.ySEPerspectiveSelector ul li ', perspectiveName)).click();
            }
        });
    },
    perspectiveIsSelected: function(perspectiveName) {
        expect(element(by.css('.ySEPerspectiveSelector button span')).getText()).toBe(perspectiveName.toUpperCase());
    },
    getElementInOverlay: function(componentID, componentType) {
        var selector = '#smarteditoverlay .smartEditComponentX[data-smartedit-component-id="' + componentID + '"]';
        if (componentType) {
            selector += '[data-smartedit-component-type="' + componentType + '"]';
        }
        return element(by.css(selector));
    }

};
