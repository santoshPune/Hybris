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

// CONSTANTS
HELPER_ID = "_sm_helper";
HELPER_HEIGHT = 30;
TOOLBAR_HEIGHT = 130;
PIXEL_THRESHOLD = 2;

CSS_CLASSES = {
    VALID_SLOT: 'over-slot-enabled',
    INVALID_SLOT: 'over-slot-disabled',
    HIGHLIGHTED_SLOT: 'eligible-slot',
    SORTABLE_PLACEHOLDER: 'ySEDnDPlaceHolder'
};

startDraggingElement = function(elementToDragSelector) {
    return browser.actions()
        .mouseMove(element(by.css(elementToDragSelector)))
        .mouseDown()
        .mouseMove({ // This is to show the helper and start the dragging
            x: 0,
            y: 1
        }).perform();
};

dragToPosition = function(xCoord, yCoord) {
    return browser.actions()
        .mouseMove({
            x: xCoord,
            y: yCoord
        })
        .perform();
};

dragToPositionFromElement = function(element, xCoord, yCoord) {
    return browser.actions()
        .mouseMove(element, {
            x: xCoord,
            y: yCoord
        })
        .perform();
};

dragToElement = function(elementDestinationSelector) {
    return browser.actions()
        .mouseMove(element(by.css(elementDestinationSelector)))
        .perform();
};

dropDraggedElement = function() {
    return browser.actions().mouseUp().perform();
};

getElementPosition = function(elementSelector) {
    return element(by.css(elementSelector)).getLocation();
};

getScrollTop = function() {
    return browser.executeScript('return document.querySelector("iframe").contentWindow.document.body.scrollTop;');
};

scrollIframe = function(pixels) {
    return browser.executeScript('return document.querySelector("iframe").contentWindow.document.body.scrollTop = ' + pixels + ';');
};

isHelperInMousePosition = function(currentMousePosition) {
    getElementPosition('#_sm_helper').then(function(helperPosition) {
        expect(currentMousePosition.x).toBeWithinRange(helperPosition.x, PIXEL_THRESHOLD);
        expect(currentMousePosition.y).toBeWithinRange(helperPosition.y, PIXEL_THRESHOLD);
    });
};
