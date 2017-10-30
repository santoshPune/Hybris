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
describe("Configuration Editor", function() {
    var defaultConfigData = require('./defaultConfigurations.json');
    var configurations = require("../utils/components/Configurations.js");
    var page = require("../utils/components/Page.js");
    var login = require('../utils/components/Login.js');

    beforeEach(function() {
        page.getAndWaitForLogin('smarteditcontainerJSTests/e2e/a_editConfigurations/editConfigurationsTest.html');
        page.setWaitForPresence(1000);
        browser.disableAnimations();
    });

    describe('Permissions', function() {
        afterEach(function() {
            browser.wait(protractor.ExpectedConditions.elementToBeClickable(login.logoutButton()), 5000, "Timed out waiting for logout button");
            login.logoutButton().click();
        });

        it("GIVEN I am logged in as a user without permission to view the configuration editor THEN configuration center will be hidden", function() {
            login.loginAsCmsManager();
            configurations.openLeftToolbar();
            expect(configurations.getConfigurationCenterButton().isPresent()).toBe(false);
        });

        it("GIVEN I am logged in as a user with permission to view the configuration editor THEN configuration center will be visible", function() {
            login.loginAsAdmin();
            configurations.openLeftToolbar();
            expect(configurations.getConfigurationCenterButton().isPresent()).toBe(true);
        });
    });

    describe('Page', function() {
        beforeEach(function() {
            login.loginAsAdmin();
            configurations.openConfigurationEditor();
            configurations.waitForConfigurationModal(3);
        });

        afterEach(function() {
            browser.wait(protractor.ExpectedConditions.elementToBeClickable(login.logoutButton()), 5000, "Timed out waiting for logout button");
            login.logoutButton().click();
        });

        it("GIVEN I'm in the Configuration Editor WHEN I close the modal THEN the left toolbar should be back to the first-level menu", function() {
            configurations.clickCancel();
            configurations.openLeftToolbar();

            expect(configurations.leftToolbarFirstLevelMenu().getAttribute('class')).not.toContain('ySELeftHideLevel1');
            expect(configurations.leftToolbarFirstLevelMenu().getAttribute('class')).toContain('ySELeftShowLevel1');
            expect(configurations.leftToolbarSecondLevelMenu().getAttribute('class')).toContain('ySELeftHideLevel2');
            expect(configurations.leftToolbarSecondLevelMenu().getAttribute('class')).not.toContain('ySELeftShowLevel2');
        });
    });

    describe('Modified Configurations', function() {
        beforeEach(function() {
            login.loginAsAdmin();
            configurations.openConfigurationEditor();
            configurations.waitForConfigurationModal(3);
        });

        afterEach(function() {
            configurations.clickCancel();
            configurations.clickConfirmOk();
            login.logoutUser();
        });

        it("GIVEN I'm in the Configuration Editor WHEN I attempt to add a duplicate key THEN I expect to see an error", function() {
            configurations.clickAdd();
            configurations.waitForConfigurationsToPopulate(4);
            configurations.setConfigurationKeyAndValue(3, 'previewTicketURI', 'previewTicketURI'); //add key and value
            configurations.clickSave();
            configurations.waitForErrorForKey("previewTicketURI");

            expect(configurations.getErrorForKey("previewTicketURI").getText()).toEqual("This is a duplicate key");
        });

        it("GIVEN I'm in the Configuration Editor WHEN user types an absolute URL THEN the editor shall display a checkbox", function() {
            configurations.setConfigurationValue(0, '"https://someuri"'); //add key and value
            expect(configurations.getAbsoluteUrlCheckbox().isDisplayed()).toBeTruthy();
        });


        it("GIVEN I'm in the Configuration Editor WHEN user types does not type an absolute URL THEN the editor shall not display a checkbox", function() {
            configurations.setConfigurationValue(0, '"/someuri/"'); //add key and value
            expect(configurations.getAbsoluteUrlCheckbox().isPresent()).toBeFalsy();
        });
    });

    describe('Configurations', function() {
        beforeEach(function() {
            login.loginAsAdmin();
            configurations.openConfigurationEditor();
            configurations.waitForConfigurationModal(3);
        });

        afterEach(function() {
            configurations.clickCancel();
            login.logoutUser();
        });

        it("GIVEN I am in the Configuration Editor THEN I expect to see a title, a save and cancel button, and configurations as defined in the backend", function() {
            expect(configurations.getConfigurationTitle().getText()).toBe('edit configuration ');
            expect(configurations.getCancelButton().isPresent()).toBe(true);
            expect(configurations.getSaveButton().isPresent()).toBe(true);
            expect(configurations.getConfigurations()).toEqualData(defaultConfigData);
        });

        it("GIVEN I'm in the Configuration Editor WHEN I delete a configuration entry AND I reopen the configuration editor THEN I expect to see the remaining configurations", function() {
            configurations.deleteConfiguration(1); //delete the 2nd configuration
            configurations.clickSave();
            configurations.clickCancel();
            configurations.openConfigurationEditor();

            expect(configurations.getConfigurations()).toEqualData([{
                "key": "previewTicketURI",
                "value": "\"thepreviewTicketURI\""
            }, {
                "key": "applications.i18nMockModule",
                "value": "{\n  \"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/i18nMock.js\"\n}"
            }]);
        });

        it("GIVEN I'm in the Configuration Editor WHEN I attempt to add a malformed configuration THEN an error is displayed", function() {
            configurations.clickAdd();
            configurations.waitForConfigurationsToPopulate(4);
            configurations.setConfigurationKeyAndValue(3, 'newkey', '{othermalformed}');
            configurations.clickSave();
            configurations.waitForErrorForKey("newkey");

            expect(configurations.getErrorForKey("newkey").getText()).toEqual("this value should be a valid JSON format");
        });

        it("GIVEN I'm in the Configuration Editor WHEN I attempt to add a malformed configuration AND re-open the configuration editor THEN I expect to see the original configurations", function() {
            configurations.clickAdd();
            configurations.waitForConfigurationsToPopulate(4);
            configurations.setConfigurationKeyAndValue(3, 'newkey', '{othermalformed}');
            configurations.clickSave();
            configurations.waitForErrorForKey("newkey");
            configurations.clickCancel();
            configurations.openConfigurationEditor();

            expect(configurations.getConfigurations()).toEqual([{
                "key": "previewTicketURI",
                "value": "\"thepreviewTicketURI\""
            }, {
                "key": 'i18nAPIRoot',
                "value": "{malformed}"
            }, {
                "key": "applications.i18nMockModule",
                "value": "{\n  \"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/i18nMock.js\"\n}"
            }]);
        });

        it("GIVEN I'm in the Configuration Editor WHEN I attempt to add a new well-formed configuration THEN the configuration will be added", function() {
            configurations.clickAdd();
            configurations.waitForConfigurationsToPopulate(4);
            configurations.setConfigurationKeyAndValue(3, 'newkey', '\"new value\"');
            configurations.clickSave();
            configurations.clickCancel();
            configurations.openConfigurationEditor();

            expect(configurations.getConfigurations()).toEqual([{
                "key": "previewTicketURI",
                "value": "\"thepreviewTicketURI\""
            }, {
                "key": 'i18nAPIRoot',
                "value": "{malformed}"
            }, {
                "key": "applications.i18nMockModule",
                "value": "{\n  \"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/i18nMock.js\"\n}"
            }, {
                "key": "newkey",
                "value": "\"new value\""
            }]);
        });

        it("GIVEN I'm in the Configuration Editor WHEN I attempt to modify an configuration with a well-formed configuration THEN I expect to see the configuration modified", function() {
            configurations.setConfigurationValue(1, '\"new\"');
            configurations.clickSave();
            configurations.clickCancel();
            configurations.openConfigurationEditor();

            expect(configurations.getConfigurations()).toEqualData([{
                "key": "previewTicketURI",
                "value": "\"thepreviewTicketURI\""
            }, {
                "key": "i18nAPIRoot",
                "value": "\"new\""
            }, {
                "key": "applications.i18nMockModule",
                "value": "{\n  \"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/i18nMock.js\"\n}"
            }]);
        });




        it("GIVEN I'm in the Configuration Editor WHEN I attempt to add a duplicate key AND click cancel THEN I expect to see the original configuration in tact", function() {
            configurations.clickAdd();
            configurations.waitForConfigurationsToPopulate(4);
            configurations.setConfigurationKeyAndValue(3, 'previewTicketURI', 'previewTicketURI'); //add key and value
            configurations.clickSave();
            configurations.waitForErrorForKey("previewTicketURI");
            configurations.clickCancel();
            configurations.clickConfirmOk();
            configurations.openConfigurationEditor();

            expect(configurations.getConfigurations()).toEqual([{
                "key": "previewTicketURI",
                "value": "\"thepreviewTicketURI\""
            }, {
                "key": 'i18nAPIRoot',
                "value": "{malformed}"
            }, {
                "key": "applications.i18nMockModule",
                "value": "{\n  \"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/i18nMock.js\"\n}"
            }]);
        });

        it("GIVEN I'm in the Configuration Editor WHEN user types an absolute URL and does not tick the checkbox THEN the editor shall highlight the message and not save", function() {
            configurations.setConfigurationValue(0, '"https://someuri"'); //add key and value
            expect(configurations.getAbsoluteUrlCheckbox().isDisplayed()).toBeTruthy();

            expect(configurations.getAbsoluteUrlText().getAttribute('class')).not.toMatch(' not-checked');
            configurations.clickSave();
            expect(configurations.getAbsoluteUrlText().getAttribute('class')).toMatch(' not-checked');

            configurations.clickCancel();
            configurations.openConfigurationEditor();

            expect(configurations.getConfigurations()).toEqual([{
                "key": "previewTicketURI",
                "value": "\"thepreviewTicketURI\""
            }, {
                "key": 'i18nAPIRoot',
                "value": "{malformed}"
            }, {
                "key": "applications.i18nMockModule",
                "value": "{\n  \"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/i18nMock.js\"\n}"
            }]);
        });

        it("GIVEN I'm in the Configuration Editor WHEN user types an absolute URL and ticks the checkbox THEN the editor shall not highlight the message and save the content", function() {
            configurations.setConfigurationValue(0, '"https://someuri"'); //add key and value
            expect(configurations.getAbsoluteUrlCheckbox().isDisplayed()).toBeTruthy();
            configurations.getAbsoluteUrlCheckbox().click();

            expect(configurations.getAbsoluteUrlText().getAttribute('class')).not.toMatch(' not-checked');
            configurations.clickSave();
            expect(configurations.getAbsoluteUrlText().getAttribute('class')).not.toMatch(' not-checked');

            configurations.clickCancel();
            configurations.openConfigurationEditor();

            expect(configurations.getConfigurations()).toEqual([{
                "key": "previewTicketURI",
                "value": "\"https://someuri\""
            }, {
                "key": 'i18nAPIRoot',
                "value": "{malformed}"
            }, {
                "key": "applications.i18nMockModule",
                "value": "{\n  \"smartEditLocation\": \"/smarteditcontainerJSTests/e2e/utils/commonMockedModules/i18nMock.js\"\n}"
            }]);
        });
    });

});
