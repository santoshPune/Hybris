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
 * @name pageTypeServiceModule
 *
 * @description
 * The Page Type Service module provides a service that fetches page types
 *
 */
angular.module('pageTypeServiceModule', ['functionsModule', 'languageServiceModule'])

/**
 * @ngdoc service
 * @name pageTypeServiceModule.pageTypeService
 *
 * @description
 * The Page Type Service fetches page types
 */
.service('pageTypeService', function($q, hitch, languageService) {

    var PageTypeService = function() {
        // NOTE: This is a temporary method. The actual map will be retrieved directly from the backend.
        this.localizeField = function(fieldValue, languagesList) {
            var localizedField = {};

            languagesList.forEach(function(language) {
                localizedField[language.isoCode] = fieldValue;
            });

            return localizedField;
        };

        /**
         * @ngdoc method
         * @name pageTypeServiceModule.pageTypeService#getPageTypeIDs
         * @methodOf pageTypeServiceModule.pageTypeService
         *
         * @description
         * Returns the list of page types.
         *
         * @returns {Array} An array of page types.
         */
        this.getPageTypeIDs = function() {
            return languageService.getToolingLanguages().then(hitch(this, function(languages) {
                var pageTypes = [
                    /*{
                                        code: 'EmailPage',
                                        type: 'emailPageData',
                                        name: this.localizeField('cms.pagetype.email.name', languages),
                                        description: this.localizeField('cms.pagetype.email.description', languages)
                                    },*/
                    {
                        code: 'ContentPage',
                        type: 'contentPageData',
                        name: this.localizeField('cms.pagetype.contentpage.name', languages),
                        description: this.localizeField('cms.pagetype.contentpage.description', languages)
                    }
                ];

                return $q.when(pageTypes);
            }));
        };
    };

    return new PageTypeService();
});
