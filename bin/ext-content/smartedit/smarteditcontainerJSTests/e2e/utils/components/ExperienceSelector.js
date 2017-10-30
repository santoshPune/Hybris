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
    widget: {
        button: function() {
            return element(by.id('experience-selector-btn', 'Experience Selector button not found'));
        },
        text: function() {
            return element(by.css('[id=\'experience-selector-btn\'] > span:nth-child(2)', 'Selector widget not found')).getText();
        }
    },
    catalog: {
        label: function() {
            return element(by.id('catalog-label', 'Experience Selector Catalog Field Label not found'));
        },
        selectedOption: function() {
            return element(by.css('[id=\'catalog\'] span[ng-bind=\'$select.selected.label\']', 'Experience Selector Catalog Field not found'));
        },
        dropdown: function() {
            return element(by.css('[id=\'catalog\'] [class^=\'ui-select-container\'] > a'));
        },
        option: function(index) {
            return element(by.css('[id=\'catalog\'] ul[role=\'listbox\'] li[role=\'option\']:nth-child(' + index + ') span'));
        },
        options: function() {
            return element.all(by.css('[id=\'catalog\'] ul[role=\'listbox\'] li[role=\'option\'] span'));
        }
    },
    dateAndTime: {
        label: function() {
            return element(by.id('time-label', 'Experience Selector Date and Time Field Label not found'));
        },
        field: function() {
            return element(by.css('input[name=\'time\']', 'Experience Selector Date and Time Field not found'));
        },
        button: function() {
            return element(by.css('[id=\'time\'] div[class*=\'date\'] span[class=\'input-group-addon\']'));
        }
    },
    language: {
        label: function() {
            return element(by.id('language-label', 'Experience Selector Language Field Label not found'));
        },
        selectedOption: function() {
            return element(by.css('[id=\'language\'] span[ng-bind=\'$select.selected.label\']', 'Experience Selector Language Field not found'));
        },
        dropdown: function() {
            return element(by.css('[id=\'language\'] [class^=\'ui-select-container\'] > a'));
        },
        option: function(index) {
            return element(by.css('[id=\'language\'] ul[role=\'listbox\'] li[role=\'option\']:nth-child(' + index + ') span'));
        },
        options: function() {
            return element.all(by.css('[id=\'language\'] ul[role=\'listbox\'] li[role=\'option\'] span'));
        }
    },
    buttons: {
        ok: function() {
            return element(by.id('submit', 'Experience Selector Apply Button not found'));
        },
        cancel: function() {
            return element(by.id('cancel', 'Experience Selector Cancel Button not found'));
        }
    },
    page: {
        iframe: function() {
            return element(by.css('#js_iFrameWrapper iframe', 'iFrame not found'));
        }
    },
    actions: {
        clickInIframe: function() {
            browser.switchToIFrame();
            element(by.css('.noOffset1')).click();
            browser.switchToParent();
        },
        clickInApplication: function() {
            element(by.css('.ySmartEditAppLogo')).click();
        },
        selectExpectedDate: function() {
            element(by.css('div[class*=\'datepicker-days\'] th[class*=\'picker-switch\']')).click();
            element(by.css('div[class*=\'datepicker-months\'] th[class*=\'picker-switch\']')).click();
            element(by.cssContainingText('span[class*=\'year\']', '2016')).click();
            element(by.css('span[class*=\'month\']:first-child')).click();
            element(by.xpath('.//*[.="1" and contains(@class,\'day\') and not(contains(@class, \'old\')) and not(contains(@class, \'new\'))]')).click();
            element(by.css('span[class*=\'glyphicon-time\']')).click();
            element(by.css('div[class=\'timepicker-picker\'] tr:first-child td:first-child a')).click();
            element(by.css('div[class=\'timepicker-picker\'] tr:nth-child(2) button')).click();
        }
    }
};
