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
var assertModalTitleIs = function(modalTitleLabel, modalTitleText) {
    expect(element(by.id(modalTitleLabel)).getText()).toBe(modalTitleText);
};

var assertModalWindow = function(modal_message) {
    expect(element(by.css('#modalBody')).getText()).toContain(modal_message);
};

var assertFailureMessage = function(failure_message) {
    expect(element(by.css('#failureMessage')).getText()).toBe(failure_message);
};

describe("Test extended modal-service", function() {

    var EC = protractor.ExpectedConditions;

    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/modalservice/extended');
    });

    it("Given a modal configuration with only a title - when the modal opens then it has the correct text", function() {
        browser.click(by.id('modalWithTitle'));
        element(by.css('.modal-content')).isDisplayed();

        assertModalTitleIs('smartedit-modal-title-modal.title.lamp', 'I love lamp ');
    });

    it("Given a modal configuration with title and buttons - when the modal opens then it has title and buttons, ", function() {
        browser.click(by.id('modalWithTitleAndButtons'));
        element(by.css('.modal-content')).isDisplayed();

        expect(element(by.id('smartedit-modal-title-modal.title.lamp')).isDisplayed());

        expect(element(by.id('modalButton')).isDisplayed());
        expect(element(by.id('modalButton')).isEnabled());
        expect(EC.elementToBeClickable($('#modalButton')));
    });

    it("Given a modal configuration with a disabled button - when the modal opens then it has the disabled button, ", function() {
        browser.click(by.id('modalWithDisabledButton'));
        element(by.css('.modal-content')).isDisplayed();

        expect(element(by.id('modalButton')).isDisplayed());
        expect(element(by.id('modalButton')).isEnabled()).toBe(false);
    });

});
