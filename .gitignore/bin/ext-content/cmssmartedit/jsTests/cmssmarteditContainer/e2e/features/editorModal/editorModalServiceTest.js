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
describe('Test Editor Modal Window', function() {

    beforeEach(function() {
        browser.get('jsTests/cmssmarteditContainer/e2e/features/editorModal/editorModalServiceTest.html');
    });

    it("Given the editor callback is attached to a button assigned to a component - " +
        "When the button is clicked - " +
        "Then the Editor Modal is displayed with Save button visible.",
        function() {
            openEditorModalForCMSParagraphComponent();
            expect(element(by.id('save')).isDisplayed()).toBe(true);

        });

    it("Given the editor callback is attached to a button assigned to a component - " +
        "When the button is clicked - " +
        "Then the Editor Modal is displayed with the Save button disabled.",
        function() {
            openEditorModalForCMSParagraphComponent();
            expect(element(by.id('save')).isEnabled()).toBe(false);
        });

    it("Given the editor callback is attached to a button assigned to a component - " +
        "When the button is clicked - " +
        "Then the Generic Editor is loaded in the modal content for the given component",
        function() {
            openEditorModalForCMSParagraphComponent();
            expect(element(by.name('id')).getAttribute('value')).toBe('MyParagraph');
        });

    it("Given the Editor Modal is visible - " +
        "And the Save button is disabled - " +
        "When I make changes to the content - " +
        "The save button is enabled",
        function() {
            openEditorModalForCMSParagraphComponent();
            expect(element(by.id('save')).isEnabled()).toBe(false);
            makeChangesFromPristineState();
            expect(element(by.id('save')).isEnabled()).toBe(true);
        });

    it('Given the Save button is enabled - ' +
        'When I click the Save Button - ' +
        'And the save succeeds - ' +
        'Then the editor modal closes',
        function() {
            mockBackendToPassSave();
            openEditorAndMakeChangesAndClickSave();
            element.all(by.css('.modal-dialog')).then(function(modalDialogs) {
                expect(modalDialogs.length).toBe(0);
            });
        });

    it('Given the Save button is enabled - ' +
        'When I click the Save Button - ' +
        'And the save fails - ' +
        'Then the editor modal remains open',
        function() {
            mockBackendToFailSave();
            openEditorAndMakeChangesAndClickSave();
            element.all(by.css('.modal-dialog')).then(function(modalDialogs) {
                expect(modalDialogs.length).toBe(1);
            });
        });

    it('Given the Editor Modal is open - ' +
        'And there are no changes to commit - ' +
        'When I click the Cancel button - ' +
        'Then the modal window is closed',
        function() {
            openEditorAndClickCancel();
            element.all(by.css('.modal-dialog')).then(function(modalDialogs) {
                expect(modalDialogs.length).toBe(0);
            });
        });

    it('Given the Editor Modal is open - ' +
        'And there are changes to commit - ' +
        'When I click the Cancel button - ' +
        'Then a cancel confirmation modal appears',
        function() {
            openEditorAndMakeChangesAndClickCancel();
            expect(element(by.id('confirmationModalDescription')).isPresent()).toBe(true);
        });

    it('Given the Editor Cancel confirmation modal is visible - ' +
        'When I click OK to confirm the cancellation - ' +
        'Then the modal window is closed',
        function() {
            openEditorAndMakeChangesAndClickCancelAndConfirm();
            element.all(by.css('.modal-dialog')).then(function(modalDialogs) {
                expect(modalDialogs.length).toBe(0);
            });
        });

    it('Given the Editor Cancel confirmation modal is visible - ' +
        'When I click the Cancel button to dismiss the cancellation - ' +
        'Then the modal window remains open',
        function() {
            openEditorAndMakeChangesAndClickCancelAndDismiss();
            element.all(by.css('.modal-dialog')).then(function(modalDialogs) {
                expect(modalDialogs.length).toBe(1);
            });
        });

    it('Given the Editor Modal is open - ' +
        'And there are no changes to commit - ' +
        'When I click the X button - ' +
        'Then the modal window is closed',
        function() {
            openEditorAndClickXButton();
            element.all(by.css('.modal-dialog')).then(function(modalDialogs) {
                expect(modalDialogs.length).toBe(0);
            });
        });

    it('Given the Editor Modal is open - ' +
        'And there are changes to commit - ' +
        'When I click the X button - ' +
        'Then a cancel confirmation modal appears',
        function() {
            openEditorAndMakeChangesAndClickXButton();
            expect(element(by.id('confirmationModalDescription')).isPresent()).toBe(true);
        });

    it('Given the Editor Cancel confirmation modal is visible after clicking the X Button - ' +
        'When I click OK to confirm the cancellation - ' +
        'Then the modal window is closed',
        function() {
            openEditorAndMakeChangesAndClickXButtonAndConfirm();
            element.all(by.css('.modal-dialog')).then(function(modalDialogs) {
                expect(modalDialogs.length).toBe(0);
            });
        });

    it('Given the Editor Cancel confirmation modal is visible after clicking the X Button - ' +
        'When I click the Cancel button to dismiss the cancellation - ' +
        'Then the modal window remains open',
        function() {
            openEditorAndMakeChangesAndClickXButtonAndDismiss();
            element.all(by.css('.modal-dialog')).then(function(modalDialogs) {
                expect(modalDialogs.length).toBe(1);
            });
        });

    it('Given the Editor modifies a tab - ' +
        'WHEN I click the save button and the system returns an unrelated validation error - ' +
        'THEN an error will be highlighted in the faulty tab',
        function() {
            clickTriggerUnrelatedValidationErrorsButton();
            openEditorModalForCMSParagraphComponent();
            moveToTab('basicTab');
            makeChangesToSecondTab();
            clickSave();

            // Expect generic tab to be modified.
            expect(element(by.css('li[data-tab-id="genericTab"] a')).getAttribute('class')).toMatch('sm-tab-error');

            moveToTab('genericTab');
            expect(element(by.css('#headline se-generic-editor-field-errors')).getText()).toBe('This is an unrelated validation error');
        });

    // Helper Functions
    function openEditorAndMakeChangesAndClickCancelAndConfirm() {
        openEditorAndMakeChangesAndClickCancel();
        confirmConfirmationModal();
    }

    function openEditorAndMakeChangesAndClickCancelAndDismiss() {
        openEditorAndMakeChangesAndClickCancel();
        dismissConfirmationModal();
    }

    function openEditorAndMakeChangesAndClickSave() {
        openEditorModalForCMSParagraphComponent();
        makeChangesFromPristineState();
        clickSave();
    }

    function openEditorAndMakeChangesAndClickCancel() {
        openEditorModalForCMSParagraphComponent();
        makeChangesFromPristineState();
        clickCancel();
    }

    function openEditorAndClickCancel() {
        openEditorModalForCMSParagraphComponent();
        clickCancel();
    }

    function openEditorAndClickXButton() {
        openEditorModalForCMSParagraphComponent();
        clickXButton();
    }

    function openEditorAndMakeChangesAndClickXButton() {
        openEditorModalForCMSParagraphComponent();
        makeChangesFromPristineState();
        clickXButton();
    }

    function openEditorAndMakeChangesAndClickXButtonAndConfirm() {
        openEditorAndMakeChangesAndClickXButton();
        confirmConfirmationModal();
    }

    function openEditorAndMakeChangesAndClickXButtonAndDismiss() {
        openEditorAndMakeChangesAndClickXButton();
        dismissConfirmationModal();
    }

    function moveToTab(tabId) {
        browser.click(by.css('li[data-tab-id="' + tabId + '"]'));
    }

    function openEditorModalForCMSParagraphComponent() {
        browser.click(by.id('openCMSParagraphComponentEditorModal'));
        browser.click(by.css('li[data-tab-id="genericTab"]'));
    }

    function makeChangesFromPristineState() {
        element(by.name('headline')).sendKeys('blabla');
    }

    function makeChangesToSecondTab() {
        element(by.id('name-shortstring')).sendKeys('blabla');
    }

    function clickSave() {
        browser.click(by.id('save'));
    }

    function clickCancel() {
        browser.click(by.id('cancel'));
    }

    function clickXButton() {
        browser.click(by.css(".close"));
    }

    function confirmConfirmationModal() {
        browser.click(by.id('confirmOk'));
    }

    function dismissConfirmationModal() {
        browser.click(by.id('confirmCancel'));
    }

    function clickTriggerUnrelatedValidationErrorsButton() {
        browser.click(by.id('unrelatedValidation'));
    }

    function attachFailingSaveCallback() {
        browser.click(by.id('attachFailingSaveCallback'));
    }

    function mockBackendToPassSave() {
        browser.click(by.id('mockBackendToPassSave'));
    }

    function mockBackendToFailSave() {
        browser.click(by.id('mockBackendToFailSave'));
    }

});
