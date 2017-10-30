angular.module('l10nModule', ['languageServiceModule', 'eventServiceModule'])
    /**
     * @ngdoc filter
     * @name l10nModule.filter:l10n
     * @description
     * Filter that accepts a localized map as input and returns the value corresponding to the resolvedLocale of {@link languageServiceModule} and defaults to the first entry.
     *
     * @param {Object} localizedMap the map of language isocodes / values
     * This class serves as an interface and should be extended, not instantiated.
     *
     */
    .filter('l10n', function(languageService, systemEventService, SWITCH_LANGUAGE_EVENT) {

        var l10n;

        function prepareFilter() {
            l10n = function initialFilter(str) {
                return str + ' filtered initially';
            };
            l10n.$stateful = false;

            languageService.getResolveLocale().then(function(resolvedLanguage) {
                l10n = function localizedFilter(localizedMap) {
                    return localizedMap[resolvedLanguage] ? localizedMap[resolvedLanguage] : localizedMap[Object.keys(localizedMap)[0]];
                };
                l10n.$stateful = false;
            });
        }
        prepareFilter();

        systemEventService.registerEventHandler(SWITCH_LANGUAGE_EVENT, prepareFilter);




        return function(localizedMap) {
            return l10n(localizedMap);
        };
    });
