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
selectLocalizedTab = function(language, qualifier, isHidden) {

    if (isHidden) {
        element.all(by.xpath("//*[@id='" + qualifier + "']//*[@data-toggle='dropdown']")).click();
    }

    element.all(by.xpath('//*[@id="' + qualifier + '"]//li[@data-tab-id="' + language + '"]')).click();

};

switchToIframeForRichTextAndAddContent = function(iframeId, content) {

    browser.switchTo().frame(element(by.css(iframeId)).getWebElement(''));
    browser.driver.findElement(by.tagName('body')).sendKeys(content);

};

switchToIframeForRichTextAndValidateContent = function(iframeId, content) {

    browser.switchTo().frame(element(by.css(iframeId)).getWebElement(''));
    expect(browser.driver.findElement(by.tagName('body')).getText()).toEqual(content);

};

getValidationErrorElements = function(qualifier) {
    return element.all(by.css('[id="' + qualifier + '"] se-generic-editor-field-errors span'));
};

getValidationErrorElementByLanguage = function(qualifier, language) {
    return element(by.css('[data-tab-id="' + language + '"] [validation-id="' + qualifier + '"] se-generic-editor-field-errors div'));
};

addMedia = function(language, searchKey) {
    browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.xpath("//*[@id='media']//li[@data-tab-id='" + language + "']"))), 20000, "could not find tab for language: " + language);
    element.all(by.xpath("//*[@id='media']//li[@data-tab-id='" + language + "']")).click();
    browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.xpath("//*[@data-tab-id='" + language + "']//*[text()='Search...']"))), 20000, "could not find 'Search...' placeholder for language tab: " + language);
    element.all(by.xpath("//*[@data-tab-id='" + language + "']//*[text()='Search...']")).click();
    browser.wait(protractor.ExpectedConditions.visibilityOf(element(by.xpath("//*[@data-tab-id='" + language + "']//div[@id='media-selector']//input[@aria-label='Select box']"))), 20000, "could not enter mask in media search for language tab: " + language);
    element(by.xpath("//*[@data-tab-id='" + language + "']//div[@id='media-selector']//input[@aria-label='Select box']")).sendKeys(searchKey);
    browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.xpath("//*[@data-tab-id='" + language + "']//li[@role='option']"))), 20000, "could not click on media selection for language tab: " + language);
    element(by.xpath("//*[@data-tab-id='" + language + "']//li[@role='option']")).click();

};

getMediaElement = function(language) {

    return element(by.xpath("//*[@data-tab-id='" + language + "']//img"));
};

switchToDefaultContent = function() {

    browser.driver.switchTo().defaultContent();

};

clickSubmit = function() {
    browser.click(by.id('submit'));
};
