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
describe('dateTimePickerLocalizationService', function() {
    var dateTimePickerLocalizationService, languageService, $q, $rootScope, tooltipsMap, resolvedLocaleToMomentLocaleMap, $translate, datetimepicker, momentLocale, otherLocale, untranslatedTooltips, translatedTooltips;


    beforeEach(customMatchers);
    beforeEach(module('dateTimePickerModule', function($provide) {
        languageService = jasmine.createSpyObj('languageService', ['getResolveLocale']);
        momentLocale = 'zz';
        otherLocale = 'aa';
        resolvedLocaleToMomentLocaleMap = {
            'en': momentLocale
        };
        $translate = jasmine.createSpyObj('$translate', ['instant']);
        $translate.instant.andCallFake(function(string) {
            return '_' + string;
        });
        datetimepicker = jasmine.createSpyObj('datetimepicker', ['locale', 'tooltips']);


        untranslatedTooltips = {
            today: 'datetimepicker.today',
            clear: 'datetimepicker.clear',
            close: 'datetimepicker.close',
            selectMonth: 'datetimepicker.selectmonth',
            prevMonth: 'datetimepicker.previousmonth',
            nextMonth: 'datetimepicker.nextmonth',
            selectYear: 'datetimepicker.selectyear',
            prevYear: 'datetimepicker.prevyear',
            nextYear: 'datetimepicker.nextyear',
            selectDecade: 'datetimepicker.selectdecade',
            prevDecade: 'datetimepicker.prevdecade',
            nextDecade: 'datetimepicker.nextdecade',
            prevCentury: 'datetimepicker.prevcentury',
            nextCentury: 'datetimepicker.nextcentury',
            pickHour: 'datetimepicker.pickhour',
            incrementHour: 'datetimepicker.incrementhour',
            decrementHour: 'datetimepicker.decrementhour',
            pickMinute: 'datetimepicker.pickminute',
            incrementMinute: 'datetimepicker.incrementminute',
            decrementMinute: 'datetimepicker.decrementminute',
            pickSecond: 'datetimepicker.picksecond',
            incrementSecond: 'datetimepicker.incrementsecond',
            decrementSecond: 'datetimepicker.decrementsecond',
            togglePeriod: 'datetimepicker.toggleperiod',
            selectTime: 'datetimepicker.selecttime'
        };

        translatedTooltips = {
            today: '_datetimepicker.today',
            clear: '_datetimepicker.clear',
            close: '_datetimepicker.close',
            selectMonth: '_datetimepicker.selectmonth',
            prevMonth: '_datetimepicker.previousmonth',
            nextMonth: '_datetimepicker.nextmonth',
            selectYear: '_datetimepicker.selectyear',
            prevYear: '_datetimepicker.prevyear',
            nextYear: '_datetimepicker.nextyear',
            selectDecade: '_datetimepicker.selectdecade',
            prevDecade: '_datetimepicker.prevdecade',
            nextDecade: '_datetimepicker.nextdecade',
            prevCentury: '_datetimepicker.prevcentury',
            nextCentury: '_datetimepicker.nextcentury',
            pickHour: '_datetimepicker.pickhour',
            incrementHour: '_datetimepicker.incrementhour',
            decrementHour: '_datetimepicker.decrementhour',
            pickMinute: '_datetimepicker.pickminute',
            incrementMinute: '_datetimepicker.incrementminute',
            decrementMinute: '_datetimepicker.decrementminute',
            pickSecond: '_datetimepicker.picksecond',
            incrementSecond: '_datetimepicker.incrementsecond',
            decrementSecond: '_datetimepicker.decrementsecond',
            togglePeriod: '_datetimepicker.toggleperiod',
            selectTime: '_datetimepicker.selecttime'
        };

        $provide.value('languageService', languageService);
        $provide.constant('resolvedLocaleToMomentLocaleMap', resolvedLocaleToMomentLocaleMap);
        $provide.value('$translate', $translate);

    }));

    beforeEach(inject(function(_dateTimePickerLocalizationService_, _$q_, _$rootScope_, _tooltipsMap_) {
        dateTimePickerLocalizationService = _dateTimePickerLocalizationService_;
        $q = _$q_;
        languageService.getResolveLocale.andReturn($q.when('en'));
        $rootScope = _$rootScope_;
        tooltipsMap = _tooltipsMap_;
    }));

    describe('localizeDateTimePicker', function() {
        it('should not localize the tool nor tooltips when both are already localized', function() {

            datetimepicker.locale.andCallFake(localeReturnSame);
            datetimepicker.tooltips.andCallFake(tooltipsReturnSame);

            dateTimePickerLocalizationService.localizeDateTimePicker(datetimepicker);
            $rootScope.$digest();

            expect(datetimepicker.locale).toHaveBeenCalledWith();
            expect(datetimepicker.locale).not.toHaveBeenCalledWith(momentLocale);
            expect(datetimepicker.tooltips).toHaveBeenCalledWith();
            expect(datetimepicker.tooltips).not.toHaveBeenCalledWith(translatedTooltips);

        });

        it('should localize only the tool not the tooltips when tooltips are already localized but the tool itself is not', function() {

            datetimepicker.locale.andCallFake(localeReturnDifferent);
            datetimepicker.tooltips.andCallFake(tooltipsReturnSame);


            dateTimePickerLocalizationService.localizeDateTimePicker(datetimepicker);
            $rootScope.$digest();

            expect(datetimepicker.locale).toHaveBeenCalledWith();
            expect(datetimepicker.locale).toHaveBeenCalledWith(momentLocale);
            expect(datetimepicker.locale.calls.length).toEqual(2);
            expect(datetimepicker.tooltips).toHaveBeenCalledWith();
            expect(datetimepicker.tooltips).not.toHaveBeenCalledWith(translatedTooltips);


        });

        it('should localize only the tooltips nor the tool when the tool is already localized but not the tooltips', function() {

            datetimepicker.locale.andCallFake(localeReturnSame);
            datetimepicker.tooltips.andCallFake(tooltipsReturnDifferent);

            dateTimePickerLocalizationService.localizeDateTimePicker(datetimepicker);
            $rootScope.$digest();

            expect(datetimepicker.locale).toHaveBeenCalledWith();
            expect(datetimepicker.locale).not.toHaveBeenCalledWith(momentLocale);
            expect(datetimepicker.tooltips).toHaveBeenCalledWith();
            expect(datetimepicker.tooltips).toHaveBeenCalledWith(translatedTooltips);
            expect(datetimepicker.tooltips.calls.length).toEqual(2);

        });


        it('should localize both the tool and tooltips when they are not already localized', function() {

            datetimepicker.locale.andCallFake(localeReturnDifferent);
            datetimepicker.tooltips.andCallFake(tooltipsReturnDifferent);

            dateTimePickerLocalizationService.localizeDateTimePicker(datetimepicker);
            $rootScope.$digest();

            expect(datetimepicker.locale).toHaveBeenCalledWith();
            expect(datetimepicker.locale).toHaveBeenCalledWith(momentLocale);
            expect(datetimepicker.locale.calls.length).toEqual(2);
            expect(datetimepicker.tooltips).toHaveBeenCalledWith();
            expect(datetimepicker.tooltips).toHaveBeenCalledWith(translatedTooltips);
            expect(datetimepicker.tooltips.calls.length).toEqual(2);

        });
    });

    var localeReturnSame = function(locale) {
        if (locale) {
            return;
        } else {
            return momentLocale;
        }
    };

    var localeReturnDifferent = function(locale) {
        if (locale) {
            return;
        } else {
            return otherLocale;
        }
    };

    var tooltipsReturnSame = function(tooltips) {
        if (tooltips) {
            return;
        } else {
            return translatedTooltips;
        }
    };

    var tooltipsReturnDifferent = function(tooltips) {
        if (tooltips) {
            return;
        } else {
            return untranslatedTooltips;
        }
    };



});
