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
var customMatchers = function() {

    var PromiseMatcherHelper = {
        states: {
            RESOLVED: 'resolved',
            REJECTED: 'rejected'
        },
        getPromiseInfo: function(promise) {
            var that = this;
            var rootScope;
            angular.mock.inject(function($injector) {
                rootScope = $injector.get('$rootScope');
            });

            var promiseInfo = {};
            promise.then(function(data) {
                promiseInfo.status = that.states.RESOLVED;
                promiseInfo.data = data;
            }, function(data) {
                promiseInfo.status = that.states.REJECTED;
                promiseInfo.data = data;
            });

            rootScope.$apply(); // Trigger promise resolution
            return promiseInfo;
        }
    };

    this.addMatchers({
        toEqualData: function(expected) {
            return angular.equals(this.actual, expected);
        },
        toHaveClass: function(className) {
            return this.actual.hasClass(className);
        },
        fail: function(errormessage) {
            this.actual = null;
            return false === true;
        },
        toHaveThatManyAlerts: function(count) {
            this.actual = this.actual.find("div.alert span").length;
            return this.actual === count;
        },
        alertToBe: function(errormessage) {
            this.actual = this.actual.find("div.alert-danger span").text();
            return this.actual === errormessage;
        },
        inputToBe: function(value) {
            this.actual = this.actual.find("div input[type=text]").val();
            return this.actual === value;
        },
        displayToBe: function(value) {
            this.actual = this.actual.find('span').html();
            return this.actual === value;

        },
        toBeInEditMode: function() {
            this.actual = this.actual.find("> div > input[type=text][data-ng-model='editor.temp']").length;
            return this.actual === 1;
        },
        notToBeInEditMode: function() {
            this.actual = this.actual.find("> div input[type=text][data-ng-model='editor.temp']").length;
            return this.actual === 0;
        },
        calendarToBeDisplayed: function() {
            //this.actual = this.actual.find("> div  li > table").length;
            this.actual = this.actual.find("ul.dropdown-menu").css('display');
            return this.actual === 'block';
        },
        toBeEmptyFunction: function() {
            if (typeof this.actual !== 'function') {
                return false;
            }
            return this.actual.toString().match(/\{([\s\S]*)\}/m)[1].trim() === '';
        },
        toBeRejected: function() {
            return PromiseMatcherHelper.getPromiseInfo(this.actual).status === PromiseMatcherHelper.states.REJECTED;
        },
        toBeResolved: function() {
            return PromiseMatcherHelper.getPromiseInfo(this.actual).status === PromiseMatcherHelper.states.RESOLVED;
        },
        toBeRejectedWithData: function(expected) {
            var promiseInfo = PromiseMatcherHelper.getPromiseInfo(this.actual);
            return promiseInfo.status === PromiseMatcherHelper.states.REJECTED && angular.equals(promiseInfo.data, expected);
        },
        toBeResolvedWithData: function(expected) {
            var promiseInfo = PromiseMatcherHelper.getPromiseInfo(this.actual);
            return promiseInfo.status === PromiseMatcherHelper.states.RESOLVED && angular.equals(promiseInfo.data, expected);
        }
    });
};

$.fn.extend({
    sendKeys: function(keys) {
        return this.each(function() {
            $(this).find("div input").val(keys).trigger('input');
        });
    },
    openCalendar: function() {
        return this.each(function() {
            $(this).find(".datepickerbutton").click();
        });
    },
    selectDate: function(dateNumber) {
        return this.each(function() {
            $(this).find("span:contains('" + dateNumber + "')").click();
        });
    },
    pressEnter: function() {
        return this.each(function() {
            // $(this).trigger('keypress')
            $(this).trigger($.Event('keypress', {
                which: 13
            }));
        });
    },
    reset: function() {
        return this.each(function() {
            $(this).find("input[type=image]").click();
        });
    }
});
$.extend({
    getOptions: function(element) {
        return element.find("div.dropdown span");
    },
    getTristateOptions: function(element) {
        return element.find("div.dropdown > span"); // > input[type=image]
    }

});

$.extend($.expr[':'], {
    "block": function(a, i, m) {
        return $(a).css("display") == "block";
    }
});
