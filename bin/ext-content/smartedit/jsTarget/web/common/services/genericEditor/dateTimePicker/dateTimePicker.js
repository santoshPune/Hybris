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
/**
 * @ngdoc overview
 * @name dateTimePickerModule
 * @description
 * # The dateTimePickerModule
 *
 * The date time picker service module is a module used for displaying a date time picker
 *
 * Use the {@link dateTimePickerModule.directive:dateTimePicker dateTimePicker} to open the date time picker.
 *
 * Once the datetimepicker is opened, its {@link dateTimePickerModule.service:dateTimePickerLocalizationService dateTimePickerLocalizationService} is used to localize the tooling.
 *
 *
 */
angular.module('dateTimePickerModule', ['languageServiceModule', 'translationServiceModule'])
    /**
     * @ngdoc directive
     * @name dateTimePickerModule.directive:dateTimePicker
     * @description
     * # The dateTimePicker
     *
     */
    .directive('dateTimePicker', ['$rootScope', 'dateTimePickerLocalizationService', function($rootScope, dateTimePickerLocalizationService) {
        return {
            templateUrl: 'web/common/services/genericEditor/dateTimePicker/dateTimePickerTemplate.html',
            restrict: 'E',
            transclude: true,
            replace: false,
            scope: {
                name: '=',
                model: '=',
                isEditable: '='
            },
            link: function($scope, elem) {
                $scope.placeholderText = 'componentform.select.date';


                if ($scope.isEditable) {
                    $(elem.children()[0])
                        .datetimepicker({
                            format: 'L LT',
                            showClear: true,
                            showClose: true,
                            useCurrent: false,
                            minDate: 0,
                            keepOpen: true
                        })
                        .on('dp.change', function() {
                            $scope.model = elem.find('input')[0].value;
                            $rootScope.$digest();
                        })
                        .on('dp.show', function() {
                            var datetimepicker = $(elem.children()[0]).datetimepicker().data("DateTimePicker");
                            dateTimePickerLocalizationService.localizeDateTimePicker(datetimepicker);
                        });
                }

            }
        };
    }])
    /**
     * @ngdoc object
     * @name dateTimePickerModule.object:resolvedLocaleToMomentLocaleMap
     *
     * @description
     * Contains a map of all inconsistent locales ISOs between SmartEdit and MomentJS
     *
     */
    .constant('resolvedLocaleToMomentLocaleMap', {
        'in': 'id',
        'zh': 'zh-cn'
    })
    /**
     * @ngdoc object
     * @name dateTimePickerModule.object: tooltipsMap
     *
     * @description
     * Contains a map of all tooltips to be localized in the date time picker
     *
     */
    .constant('tooltipsMap', {
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
    })
    /**
     * @ngdoc service
     * @name dateTimePickerModule.service:dateTimePickerLocalizationService
     *
     * @description
     * The dateTimePickerLocalizationService is responsible for both localizing the date time picker as well as the tooltips
     */
    .service('dateTimePickerLocalizationService', ['$translate', 'resolvedLocaleToMomentLocaleMap', 'tooltipsMap', 'languageService', function($translate, resolvedLocaleToMomentLocaleMap, tooltipsMap, languageService) {

        var convertResolvedToMomentLocale = function(resolvedLocale) {
            var conversion = resolvedLocaleToMomentLocaleMap[resolvedLocale];
            if (conversion) {
                return conversion;
            } else {
                return resolvedLocale;
            }
        };

        var getLocalizedTooltips = function() {

            var localizedTooltips = {};


            for (var index in tooltipsMap) {
                localizedTooltips[index] = $translate.instant(tooltipsMap[index]);
            }

            return localizedTooltips;

        };

        var compareTooltips = function(tooltips1, tooltips2) {
            for (var index in tooltipsMap) {
                if (tooltips1[index] !== tooltips2[index]) {
                    return false;
                }
            }
            return true;
        };

        var localizeDateTimePickerUI = function(datetimepicker) {
            languageService.getResolveLocale().then(function(language) {

                var momentLocale = convertResolvedToMomentLocale(language);

                //This if statement was added to prevent infinite recursion, at the moment it triggers twice
                //due to what seems like datetimepicker.locale(<string>) broadcasting dp.show
                if (datetimepicker.locale() !== momentLocale) {
                    datetimepicker.locale(momentLocale);
                }

            });

        };

        var localizeDateTimePickerTooltips = function(datetimepicker) {
            var currentTooltips = datetimepicker.tooltips();
            var translatedTooltips = getLocalizedTooltips();

            //This if statement was added to prevent infinite recursion, at the moment it triggers twice
            //due to what seems like datetimepicker.tooltips(<tooltips obj>) broadcasting dp.show
            if (!compareTooltips(currentTooltips, translatedTooltips)) {
                datetimepicker.tooltips(translatedTooltips);
            }

        };

        this.localizeDateTimePicker = function(datetimepicker) {
            localizeDateTimePickerUI(datetimepicker);
            localizeDateTimePickerTooltips(datetimepicker);
        };

    }]);
