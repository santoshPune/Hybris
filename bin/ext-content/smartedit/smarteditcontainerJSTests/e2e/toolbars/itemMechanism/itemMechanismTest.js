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
var itemMechanismPath = 'smarteditcontainerJSTests/e2e/toolbars/itemMechanism';
var itemMechanismIconPath = '../../' + itemMechanismPath;

describe("Configure toolbar through outer toolbarservice", function() {

    beforeEach(function() {
        browser.get(itemMechanismPath);
        browser.driver.manage().timeouts().implicitlyWait(0);
    });

    it("items of type 'ACTION' and 'HYBRID_ACTION' will be added", function() {
        expect(element.all(by.css('.yTemplateToolbar')).count()).toBe(0);
        browser.click(by.id('sendActionsOuter'));
        expect(element.all(by.css('.yTemplateToolbar')).count()).toBe(3);

        expect(element(by.id('toolbar_option_toolbar.action.action5')).getAttribute('alt')).toBe('action5');
        expect(element(by.id('toolbar_option_toolbar.action.action5')).getAttribute('data-ng-src')).toBe(itemMechanismIconPath + '/icon5.png');
        expect(element(by.id('toolbar_option_toolbar.action.action6')).getAttribute('alt')).toBe('action6');
        expect(element(by.id('toolbar_option_toolbar.action.action6')).getAttribute('data-ng-src')).toBe(itemMechanismIconPath + '/icon6.png');
    });


    it("item of type 'HYBRID_ACTION' will display its template", function() {
        expect(element.all(by.css('.yTemplateToolbar')).count()).toBe(0);
        browser.click(by.id('sendActionsOuter'));
        expect(element.all(by.css('.yTemplateToolbar')).count()).toBe(3);

        expect(element(by.id('hybridActiontemplate')).getText()).toBe('HYBRID ACTION TEMPLATE');
    });


    xit("Callbacks will be executed successfully when items of type 'ACTION' and 'HYBRID_ACTION", function() {
        browser.click(by.id('sendActionsOuter'));

        browser.click(by.id('toolbar_option_toolbar.action.action5'));
        expect(element(by.id('message')).getText()).toBe('Action 5 called');
        browser.switchToIFrame();
        expect(element(by.id('message')).getText()).toBe('');
        browser.switchToParent();

        browser.click(by.id('toolbar_option_toolbar.action.action6'));
        expect(element(by.id('message')).getText()).toBe('Action 6 called');
        browser.switchToIFrame();
        expect(element(by.id('message')).getText()).toBe('');
    });

    it("item of type 'TEMPLATE' will display its template", function() {
        expect(element.all(by.css('.yTemplateToolbar')).count()).toBe(0);
        browser.click(by.id('sendActionsOuter'));
        expect(element.all(by.css('.yTemplateToolbar')).count()).toBe(3);

        expect(element(by.id('standardTemplate')).getText()).toBe('STANDARD TEMPLATE');
    });

    it('can remove toolbar items', function() {
        // Arrange
        browser.click(by.id('sendActionsOuter'));
        expect(element(by.id('standardTemplate')).isPresent()).toBe(true);
        expect(element(by.css('button img[title="action5"]')).isPresent()).toBe(true);
        expect(element(by.css('button img[title="action6"]')).isPresent()).toBe(true);

        // Act
        browser.click(by.id('removeActionsOuter'));

        // Assert
        expect(element(by.id('standardTemplate')).isPresent()).toBe(false);
        expect(element(by.css('button img[title="action5"]')).isPresent()).toBe(false);
        expect(element(by.css('button img[title="action6"]')).isPresent()).toBe(true);
    });
});

describe("Configure toolbar through inner toolbarservice", function() {

    beforeEach(function() {
        browser.get(itemMechanismPath);
    });

    it("items of type 'ACTION' will be added", function() {
        expect(element.all(by.css('.yTemplateToolbar')).count()).toBe(0);
        browser.switchToIFrame();
        browser.click(by.id('sendActionsInner'));
        browser.switchToParent();
        expect(element.all(by.css('.yTemplateToolbar')).count()).toBe(2);

        expect(element(by.id('toolbar_option_toolbar.action.action3')).getAttribute('alt')).toBe('action3');
        expect(element(by.id('toolbar_option_toolbar.action.action4')).getAttribute('alt')).toBe('action4');
    });


    it("Callbacks will be executed successfully when items of type 'ACTION'", function() {
        browser.switchToIFrame();
        browser.click(by.id('sendActionsInner'));

        browser.switchToParent();
        browser.click(by.id('toolbar_option_toolbar.action.action3'));
        expect(element(by.id('message')).getText()).toBe('');
        browser.switchToIFrame(false);
        expect(element(by.id('message')).getText()).toBe('Action 3 called');

        browser.switchToParent();
        browser.click(by.id('toolbar_option_toolbar.action.action4')).then(function() {
            expect(element(by.id('message')).getText()).toBe('');
            browser.switchToIFrame(false);
            expect(element(by.id('message')).getText()).toBe('Action 4 called');
        });
    });

    it(' can remove toolbar items', function() {
        // Arrange
        browser.switchToIFrame();
        browser.click(by.id('sendActionsInner'));

        browser.switchToParent();
        expect(element(by.css('button img[title="action3"]')).isPresent()).toBe(true);
        expect(element(by.css('button img[title="action4"]')).isPresent()).toBe(true);
        browser.switchToIFrame();

        // Act
        browser.click(by.id('removeAction'));

        // Assert
        browser.switchToParent();
        expect(element(by.css('button img[title="action3"]')).isPresent()).toBe(true);
        expect(element(by.css('button img[title="action4"]')).isPresent()).toBe(false);
    });
});


describe("Configure toolbar through inner AND outer toolbarservice", function() {

    beforeEach(function() {
        browser.get(itemMechanismPath);
    });


    it('Actions will not conflict', function() {
        browser.click(by.id('sendActionsOuter'));
        browser.switchToIFrame();
        browser.click(by.id('sendActionsInner'));
        browser.switchToParent();
        expect(element.all(by.css('.yTemplateToolbar')).count()).toBe(5);

        expect(element(by.id('toolbar_option_toolbar.action.action3')).getAttribute('alt')).toBe('action3');
        expect(element(by.id('toolbar_option_toolbar.action.action4')).getAttribute('alt')).toBe('action4');
        expect(element(by.id('toolbar_option_toolbar.action.action5')).getAttribute('alt')).toBe('action5');
        expect(element(by.id('toolbar_option_toolbar.action.action6')).getAttribute('alt')).toBe('action6');
    });

    it('removing items does not conflict with each other', function() {
        // Arrange
        browser.switchToIFrame();
        browser.click(by.id('sendActionsInner'));

        browser.switchToParent();
        browser.click(by.id('sendActionsOuter'));

        expect(element(by.id('standardTemplate')).isPresent()).toBe(true);
        expect(element(by.css('button img[title="action3"]')).isPresent()).toBe(true);
        expect(element(by.css('button img[title="action4"]')).isPresent()).toBe(true);
        expect(element(by.css('button img[title="action5"]')).isPresent()).toBe(true);
        expect(element(by.css('button img[title="action6"]')).isPresent()).toBe(true);
        browser.switchToIFrame();

        // Act
        browser.click(by.id('removeAction'));
        browser.switchToParent();
        browser.click(by.id('removeActionsOuter'));

        // Assert
        expect(element(by.id('standardTemplate')).isPresent()).toBe(false);
        expect(element(by.css('button img[title="action3"]')).isPresent()).toBe(true);
        expect(element(by.css('button img[title="action4"]')).isPresent()).toBe(false);
        expect(element(by.css('button img[title="action5"]')).isPresent()).toBe(false);
        expect(element(by.css('button img[title="action6"]')).isPresent()).toBe(true);
    });

});
