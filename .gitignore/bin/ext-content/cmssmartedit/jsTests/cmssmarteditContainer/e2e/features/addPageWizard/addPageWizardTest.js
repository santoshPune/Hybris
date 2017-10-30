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

var PageWizard = require('./wizardObject.js');
var WizardStep = require('./wizardStepObject.js');

describe('addPageWizard - ', function() {

    var pageWizard, wizardStep;

    beforeEach(function() {
        browser.get('jsTests/cmssmarteditContainer/e2e/features/addPageWizard/addPageWizardTest.html');
        browser.waitForContainerToBeReady();

        pageWizard = new PageWizard();
        wizardStep = new WizardStep();

        navigateToFirstCatalogPageList();
    });

    it('WHEN clicking addNewPage THEN the wizard is opened', function() {
        // Arrange/Act
        pageWizard.open();

        // Assert
        expect(pageWizard.window().isDisplayed()).toBeTruthy();
    });

    it('GIVEN the wizard is open on the first tab and the user has not selected a type WHEN the user clicks next THEN the wizard will not move to the next tab', function() {
        // Arrange
        pageWizard.open().then(function() {
            // Act
            pageWizard.moveNext();

            // Assert
            expect(pageWizard.getCurrentStep()).toBe(1);
        });
    });

    it('GIVEN the wizard is open on the first tab and the user has selected a type WHEN the user clicks next THEN the wizard will move to the next tab', function() {
        // Arrange
        pageWizard.open().then(function() {
            wizardStep.selectItemByIndex(0);

            // Act
            pageWizard.moveNext();

            // Assert
            expect(pageWizard.getCurrentStep()).toBe(2);
        });
    });

    it('GIVEN the wizard is opened on the second tab and the user has not selected a template WHEN the user clicks next THEN the wizard will not move to the next tab', function() {
        // Arrange
        pageWizard.open().then(function() {
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Act
            pageWizard.moveNext();

            // Assert
            expect(pageWizard.getCurrentStep()).toBe(2);
        });
    });

    it('GIVEN the wizard is opened on the second tab and the user has selected a template WHEN the user clicks next THEN the wizard will move to the next tab', function() {
        // Arrange
        pageWizard.open().then(function() {
            // First screen
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Second screen
            wizardStep.selectItemByIndex(0);

            // Act
            pageWizard.moveNext();

            // Assert
            expect(pageWizard.getCurrentStep()).toBe(3);
        });
    });

    it('GIVEN the wizard is opened on the third tab and the user has not entered any content WHEN the user clicks submit THEN the modal wont close', function() {
        // Arrange
        pageWizard.open().then(function() {
            // First screen
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Second screen
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Act
            pageWizard.submit();

            // Assert
            expect(pageWizard.getCurrentStep()).toBe(3);
            expect(pageWizard.window().isDisplayed()).toBeTruthy();
        });
    });

    it('GIVEN the wizard is opened on the third tab and the user has not entered any content WHEN the user clicks back THEN the modal will return to the second tab', function() {
        // Arrange
        pageWizard.open().then(function() {
            // First screen
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Second screen
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Act
            pageWizard.moveBack();

            // Assert
            expect(pageWizard.getCurrentStep()).toBe(2);
        });
    });

    it('GIVEN the wizard is opened on the third tab and the user has not entered any content WHEN the user clicks the first header THEN the modal will return to the first tab', function() {
        // Arrange
        pageWizard.open().then(function() {
            // First screen
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Second screen
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Act
            pageWizard.moveToStepByIndex(1);

            // Assert
            expect(pageWizard.getCurrentStep()).toBe(1);
        });
    });

    it('GIVEN the wizard is opened on the second tab and the user has not selected a template WHEN the user clicks back THEN the wizard moves to the previous tab', function() {
        // Arrange
        pageWizard.open().then(function() {
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Act
            pageWizard.moveBack();

            // Assert
            expect(pageWizard.getCurrentStep()).toBe(1);
        });
    });

    it('GIVEN the wizard is opened on the second tab WHEN the user clicks on the first tab header THEN the wizard moves to the previous tab', function() {
        // Arrange
        pageWizard.open().then(function() {
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Act
            pageWizard.moveToStepByIndex(1);

            // Assert
            expect(pageWizard.getCurrentStep()).toBe(1);
        });
    });

    it('GIVEN the wizard is opened on the third tab and the user has entered wrong data WHEN the user clicks submit THEN the wizard will show an error message and stays in the third tab', function() {
        // Arrange
        pageWizard.open().then(function() {
            // First screen
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Second screen
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Act
            wizardStep.enterFieldData('uid-shortstring', 'bla');
            pageWizard.submit();

            // Assert
            //expect(wizardStep.fieldError('uid'));
            expect(pageWizard.getCurrentStep()).toBe(3);
            expect(pageWizard.window().isDisplayed()).toBeTruthy();
        });
    });

    it('GIVEN the wizard is opened on the third tab and the user has entered valid data WHEN the user clicks submit THEN the wizard closes', function() {
        // Arrange
        pageWizard.open().then(function() {
            // First screen
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Second screen
            wizardStep.selectItemByIndex(0);
            pageWizard.moveNext();

            // Act
            wizardStep.enterFieldData('uid-shortstring', 'something valid');
            pageWizard.submit();

            // Assert
            expect(pageWizard.isWindowsOpen()).toBeFalsy();
        });
    });

    // Helper Functions
    function navigateToFirstCatalogPageList() {
        return element(by.css('.catalog-container .page-list-link-item a')).click();
    }
});
