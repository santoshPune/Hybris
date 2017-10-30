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
MAIN_AUTH_SUFFIX = 'L2F1dGhvcml6YXRpb25zZXJ2ZXIvb2F1dGgvdG9rZW4';
FAKE1_AUTH_SUFFIX = 'L2F1dGhFbnRyeVBvaW50MQ';
FAKE2_AUTH_SUFFIX = 'L2F1dGhFbnRyeVBvaW50Mg';
STORE_FRONT_HOME_PAGE = "dummystorefront.html";

afterEachAuthentificationTest = function() {

    if (global.waitForSprintDemoLogTime !== null && global.waitForSprintDemoLogTime > 0) {
        // List logs
        var logs = browser.driver.manage().logs(),
            logType = 'browser'; // browser
        logs.getAvailableLogTypes().then(function(logTypes) {
            if (logTypes.indexOf(logType) > -1) {
                browser.driver.manage().logs().get(logType).then(function(logsEntries) {

                    var len = logsEntries.length;
                    for (var i = 0; i < len; ++i) {

                        var logEntry = logsEntries[i];
                        var showLog = hasLogLevel(logEntry.level.name, global.sprintDemoLogLevels);
                        if (showLog) {
                            waitForSprintDemo(global.waitForSprintDemoLogTime);
                            try {
                                var msg = JSON.parse(logEntry.message);
                                console.log(msg.message.text);
                            } catch (err) {
                                if (global.sprintDemoShowLogParsingErrors) {
                                    console.log("Error parsing log:  " + logEntry.message);
                                }
                            }
                        }
                    }
                }, null);
            }
        });
    }

};


hasLogLevel = function(logLevelName, sprintDemoLogLevels) {
    var len = sprintDemoLogLevels.length;
    for (var i = 0; i < len; ++i) {
        if (sprintDemoLogLevels[i] == logLevelName) {
            return true;
        }
    }
    return false;
};

waitForSprintDemo = function(milliseconds) {
    var start = new Date().getTime();
    for (var i = 0; i < 1e7; i++) {
        if ((new Date().getTime() - start) > milliseconds) {
            break;
        }
    }
};


loginUser = function(hasReAuthModal, redirectLocationIsLandingPage, perspectives) {
    browser.driver.manage().deleteAllCookies();
    browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('username_' + MAIN_AUTH_SUFFIX))), 10000, "username input field is not clickable");
    element(by.id('username_' + MAIN_AUTH_SUFFIX)).sendKeys('customermanager');
    element(by.id('password_' + MAIN_AUTH_SUFFIX)).sendKeys('123');
    browser.click(by.id('submit_' + MAIN_AUTH_SUFFIX));

    if (!redirectLocationIsLandingPage) {
        browser.waitForWholeAppToBeReady();

        perspectives.selectPerspective(perspectives.DEFAULT_PERSPECTIVES.ALL);
        browser.waitForWholeAppToBeReady();

    }

    if (hasReAuthModal) {
        browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.id('submit_' + FAKE2_AUTH_SUFFIX))), 20000, "submit input field is not clickable in iframe login 2");
    }
};

logoutUser = function() {
    browser.switchToParent();
    browser.click(by.id('nav-expander'));
    browser.click(by.css('.leftNav--user> li:nth-of-type(3) > a'));
};

redirectUserToLandingPage = function() {
    browser.switchToParent();
    browser.click(by.id('nav-expander'));
    browser.click(by.css('.leftNav a[data-ng-click="showSites()"]'));
};
