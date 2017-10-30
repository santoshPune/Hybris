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
 *
 *
 */
exports.config = {
    allScriptsTimeout: 11000,

    specs: [
        'e2e/**/*Test.js'
        //        'e2e/**/*Interceptor*Test.js',
        //        'e2e/**/*drag*Test.js'
    ],

    seleniumServerJar: '../node_modules/protractor/selenium/selenium-server-standalone-2.45.0.jar',

    capabilities: {
        'browserName': 'chrome',
        'shardTestFiles': false,
        'maxInstances': 4,
        'chromeOptions': {
            args: ['lang=en-US']
        }
    },
    chromeDriver: '../node_modules/protractor/selenium/chromedriver/chromedriver',
    directConnect: true,

    baseUrl: 'http://127.0.0.1:7000',

    framework: 'jasmine2',

    jasmineNodeOpts: {
        defaultTimeoutInterval: 30000
    },

    //method found in node_modules/protractor/docs/referenceConf.js :
    onPrepare: function() {

        var jasmineReporters = require('jasmine-reporters');
        jasmine.getEnv().addReporter(new jasmineReporters.JUnitXmlReporter({
            consolidateAll: false,
            savePath: 'jsTarget/test/personalizationsmartedit/junit/protractor'
        }));

        //this is protractor but both protractor and browser instances are available in this closure
        browser.driver.manage().window().maximize();

        //If you are outputting logs into the shell after running a protractor test.
        //i.e. grunt connect:test protractor:run --specs='cmsxuiJSTests/e2e/apiAuthentication/apiAuthenticationTest.js'
        //Setting this value to anything other than 0 will wait in milliseconds between each log statement
        global.waitForSprintDemoLogTime = 0;
        //Set log levels to display in shell
        global.sprintDemoLogLevels = ["WARNING", "INFO"];
        //Show any log parsing errors - by default they are not shown
        global.sprintDemoShowLogParsingErrors = false;

        browser.waitForContainerToBeReady = function() {
            //click on load preview button
            browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('nav-expander'))), 20000, "could not find burger menu/toolbar when first loading app");
            browser.waitForAngular();
        };

        browser.waitForFrameToBeReady = function() {
            /*
             * protractor cannot nicely use browser api until angular app is bootstrapped.
             * to do so it needs to see ng-app attribute.
             * But in our smartEdit setup, app is bootstrapped programmatically, not through ng-app
             * workaround consists then in waiting arbitrary amount fo time
             */
            browser.wait(function() {
                browser.ignoreSynchronization = true;
                return element.all(by.css('body')).count().then(function(count) {
                    if (count === 1) {
                        return element(by.css('body')).getAttribute('data-smartedit-ready').then(function(attribute) {
                            return attribute === 'true';
                        });
                    } else {
                        return false;
                    }
                });
            }, 30000, "could not find data-smartedit-ready='true' attribute on the iframe body tag");
            browser.ignoreSynchronization = false;
        };

        browser.switchToIFrame = function() {
            var iframe = element(by.tagName('iframe'));
            browser.driver.switchTo().frame(iframe.getWebElement(''));
            browser.waitForFrameToBeReady();
        };

        browser.waitForWholeAppToBeReady = function() {
            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();
            browser.switchToParent();
        };

        browser.clickLoadPreview = function() {
            //click on load preview button
            browser.waitForContainerToBeReady();
            element(by.id('loadPreview')).click();
        };

        browser.switchToParent = function() {
            browser.driver.switchTo().defaultContent();
        };

        browser.click = function(selector, errorMessage) {
            var ele;
            if (typeof selector === 'string') {
                ele = $(selector);
            } else {
                ele = element(selector);
            }
            var message = errorMessage ? errorMessage : "could not find element " + selector;
            browser.wait(protractor.ExpectedConditions.elementToBeClickable(ele), 5000, message);
            ele.click();
        };


        //---------------------------------------------------------------------------------
        //-----------------------------------ACTIONS---------------------------------------
        //---------------------------------------------------------------------------------

        /* all keys of protractor.Key :
         *[ 'NULL', 'CANCEL', 'HELP', 'BACK_SPACE', 'TAB', 'CLEAR', 'RETURN', 'ENTER', 'SHIFT', 'CONTROL',
         *  'ALT', 'PAUSE', 'ESCAPE', 'SPACE', 'PAGE_UP', 'PAGE_DOWN', 'END', 'HOME', 'ARROW_LEFT', 'LEFT',
         *  'ARROW_UP', 'UP', 'ARROW_RIGHT', 'RIGHT', 'ARROW_DOWN', 'DOWN', 'INSERT', 'DELETE', 'SEMICOLON',
         *  'EQUALS', 'NUMPAD0', 'NUMPAD1', 'NUMPAD2', 'NUMPAD3', 'NUMPAD4', 'NUMPAD5', 'NUMPAD6', 'NUMPAD7',
         *  'NUMPAD8', 'NUMPAD9', 'MULTIPLY', 'ADD', 'SEPARATOR', 'SUBTRACT', 'DECIMAL', 'DIVIDE', 'F1',
         *  'F2', 'F3', 'F4', 'F5', 'F6', 'F7', 'F8', 'F9', 'F10', 'F11', 'F12', 'COMMAND', 'META', 'chord' ]
         */

        browser.press = function(protractorKey) {
            browser.actions().sendKeys(protractorKey).perform();
        };


        //---------------------------------------------------------------------------------
        //---------------------------------ASSERTIONS--------------------------------------
        //---------------------------------------------------------------------------------

        beforeEach(function() {
            jasmine.addMatchers({
                toEqualData: function() {
                    return {
                        compare: function(actual, expected) {
                            return {
                                pass: JSON.stringify(actual) === JSON.stringify(expected)
                            };
                        }
                    };
                },
                toBeEmptyString: function() {
                    return {
                        compare: function(actual, expected) {
                            return {
                                pass: actual === ''
                            };
                        }
                    };
                },
                toContain: function() {
                    return {
                        compare: function(actual, expected) {
                            return {
                                pass: actual.indexOf(expected) > -1
                            };
                        }
                    };
                }
            });

        });

    },
    params: {
        timeout: 5000
    }
};
