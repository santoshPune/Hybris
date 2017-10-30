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
    get: function(url) {
        browser.get(url);
    },
    getAndWaitForWholeApp: function(url) {
        this.get(url);
        browser.waitForWholeAppToBeReady();
    },
    getAndWaitForLogin: function(url) {
        this.get(url);
        this.clearCookies();
        this.waitForLoginModal();
    },
    waitForLoginModal: function() {
        browser.wait(protractor.ExpectedConditions.elementToBeClickable(element(by.css('input[id^="username_"]'))), 20000, "Timed out waiting for username input");
        browser.waitForAngular();
    },
    setWaitForPresence: function(implcitWait) {
        browser.driver.manage().timeouts().implicitlyWait(implcitWait);
    },
    dumpAllLogsToConsole: function() {
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
    },
    clearCookies: function() {
        browser.driver.wait(function() {
            return browser.driver.manage().deleteAllCookies().then(function() {
                return true;
            }, function(err) {
                throw err;
            });
        }, 5000, 'Timed out waiting for cookies to clear');
    }
};
