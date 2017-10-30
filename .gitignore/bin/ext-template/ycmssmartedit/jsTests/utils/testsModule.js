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
customMatchers = function() {
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
        messageToBe: function(errormessage) {
            this.actual = this.actual.find("div.alert-success span").text();
            return this.actual === errormessage;
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
        flagToBeTrue: function(value) {
            this.actual = this.actual
                .find("> input[src='http/images/tick.png']").length;
            return this.actual == 1;

        },
        flagToBeFalse: function(value) {
            this.actual = this.actual
                .find("> input[src='http/images/no-tick.png']").length;
            return this.actual == 1;

        },
        flagToBeUndetermined: function(value) {
            this.actual = this.actual
                .find("> input[src='http/images/question.png']").length;
            return this.actual == 1;

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
