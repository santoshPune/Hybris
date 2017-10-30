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
exports.config = {
    allScriptsTimeout: 11000,

    specs: [
        'e2e/**/*Test.js'
    ],

    capabilities: {
        'browserName': 'chrome',
        'shardTestFiles': false,
        'maxInstances': 10,
        'chromeOptions': {
            args: ['lang=en-US']
        }
    },

    chromeDriver: '../node_modules/protractor/selenium/chromedriver/chromedriver',

    directConnect: true,

    troubleshoot: false,

    baseUrl: 'http://127.0.0.1:7000',

    framework: 'jasmine2',

    jasmineNodeOpts: {
        defaultTimeoutInterval: 60000
    },



    //method found in node_modules/protractor/docs/referenceConf.js :
    onPrepare: function() {

        var DEFAULT_IMPLICIT_WAIT = 5000;

        var jasmineReporters = require('jasmine-reporters');
        jasmine.getEnv().addReporter(new jasmineReporters.JUnitXmlReporter({
            consolidateAll: false,
            savePath: 'jsTarget/test/smarteditContainer/junit/protractor'
        }));

        //this is protractor but both protractor and browser instances are available in this closure
        browser.driver.manage().window().setSize(1366, 820);

        // Wait up to 5 seconds for trying to find an element before failing
        browser.driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICIT_WAIT);

        //If you are outputting logs into the shell after running a protractor test.
        //i.e. grunt connect:test protractor:run --specs='cmsxuiJSTests/e2e/apiAuthentication/apiAuthenticationTest.js'
        //Setting this value to anything other than 0 will wait in milliseconds between each log statement
        global.waitForSprintDemoLogTime = 0;
        //Set log levels to display in shell
        global.sprintDemoLogLevels = ["WARNING", "INFO"];
        //Show any log parsing errors - by default they are not shown
        global.sprintDemoShowLogParsingErrors = false;


        browser.dumpLogs = function() {
            browser.manage().logs().get('browser').then(function(browserLogs) {
                browserLogs.forEach(function(log) {
                    console.log(log.message);
                });
            });
        };

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

        browser.switchToIFrame = function(waitForFrameToBeReady) {
            var iframe = element(by.tagName('iframe'));
            browser.driver.switchTo().frame(iframe.getWebElement(''));
            if (waitForFrameToBeReady !== false) {
                browser.waitForFrameToBeReady();
            }
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

        browser.waitForUrlToMatch = function(regex) {
            browser.wait(function() {
                return browser.getCurrentUrl().then(function(url) {
                    return regex.test(url);
                });
            }, 5000, 'URL did not change');
        };

        browser.disableAnimations = function() {
            var disableAnimationsClosure = function() {
                var style = document.createElement('style');
                style.type = 'text/css';
                style.innerHTML = '* {' +
                    '-webkit-transition: none !important;' +
                    '-moz-transition: none !important' +
                    '-o-transition: none !important' +
                    '-ms-transition: none !important' +
                    'transition: none !important' +
                    '}';

                document.getElementsByTagName('head')[0].appendChild(style);
            };

            browser.executeScript(disableAnimationsClosure);
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
            return ele.click();
        };

        browser.waitUntil = function(assertionFunction, errorMessage) {
            var message = errorMessage ? errorMessage : "could not match condition";
            return browser.wait(assertionFunction, 5000, errorMessage);
        };

        browser.scrollToBottom = function(scrollElm) {
            return browser.executeScript('arguments[0].scrollTop = arguments[0].scrollHeight;', scrollElm.getWebElement());
        };

        browser.scrollToTop = function(scrollElm) {
            return browser.executeScript('arguments[0].scrollTop = 0;', scrollElm.getWebElement());
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
                },
                toBeDisplayed: function() {
                    return {
                        compare: function(actual) {
                            return {
                                pass: actual.isDisplayed()
                            };
                        }
                    };
                },
                toBeWithinRange: function() {
                    return {
                        compare: function(actual, expected, range) {
                            range = range || 1;
                            return {
                                pass: Math.abs(expected - actual) < range
                            };
                        }
                    };
                },
                toBeAbsent: function() {
                    return {
                        compare: function(locator) {
                            var message = 'Expected element with locator ' + locator + ' to be present in DOM';
                            return {
                                pass: browser.driver.manage().timeouts().implicitlyWait(0).then(function() {
                                    return browser.wait(function() {
                                        return element(locator).isPresent().then(function(isPresent) {
                                            return !isPresent;
                                        });
                                    }, 5000, message).then(function(result) {
                                        return browser.driver.manage().timeouts().implicitlyWait(DEFAULT_IMPLICIT_WAIT).then(function() {
                                            return result;
                                        });
                                    });
                                }),
                                message: message
                            };
                        }
                    };
                }
            });

        });

    },
    params: {
        //		protractor.getInstance().params
        //		browser.params
    }
};
