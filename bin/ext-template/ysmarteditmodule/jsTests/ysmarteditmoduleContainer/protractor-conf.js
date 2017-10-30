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
        'e2e/*Test.js'
    ],

    capabilities: {
        'browserName': 'chrome'
            /*,
                'chromeOptions': {
                    args: ['--disable-web-security',//to allow CORS
                           '--ignore-certificate-errors',
                           ]
                }*/
    },

    chromeDriver: '../../node_modules/protractor/selenium/chromedriver/chromedriver',

    directConnect: true,

    troubleshoot: true,

    baseUrl: 'http://localhost:7000',

    framework: 'jasmine',

    jasmineNodeOpts: {
        defaultTimeoutInterval: 30000
    },

    //method found in node_modules/protractor/docs/referenceConf.js :
    onPrepare: function() {

        //this is protractor but both protractor and browser instances are available in this closure
        browser.driver.manage().window().maximize();

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
            this.addMatchers({
                toEqualData: function(expected) {
                    return angular.equals(this.actual, expected);
                }
            });
        });

    },
    params: {
        //		protractor.getInstance().params
        //		browser.params
    }
};
