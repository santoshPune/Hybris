(function() {
    /**
     * @ngdoc overview
     * @name pageTemplateServiceModule
     * @description
     * # The pageTemplateServiceModule
     *
     * The page template service module provides a service that allows the retrieval of page templates associated to a page type.
     *
     */
    angular.module('pageTemplateServiceModule', ['restServiceFactoryModule', 'resourceLocationsModule'])
        .constant('NON_SUPPORTED_TEMPLATES', [
            "layout/landingLayout1Page",
            "layout/landingLayout3Page",
            "layout/landingLayout4Page",
            "layout/landingLayout5Page",
            "layout/landingLayout6Page",
            "layout/landingLayoutPage",
            "account/accountRegisterPage",
            "checkout/checkoutRegisterPage"
        ])
        /**
         * @ngdoc service
         * @name pageTemplateServiceModule.service:pageTemplateService
         *
         * @description
         * This service allows the retrieval of page templates associated to a page type.
         *
         */
        .factory('pageTemplateService', function(restServiceFactory, PAGE_TEMPLATES_URI, NON_SUPPORTED_TEMPLATES) {
            var pageTemplateRestService = restServiceFactory.get(PAGE_TEMPLATES_URI);

            return {
                /**
                 * @ngdoc method
                 * @name pageTemplateServiceModule.service:pageTemplateService#getPageTemplatesForType
                 * @methodOf pageTemplateServiceModule.service:pageTemplateService
                 *
                 * @description
                 * When called, this method retrieves the page templates associated to the provided page type.
                 *
                 * @param {String} siteUID The UID of the current site.
                 * @param {String} catalogId The id of the selected catalog.
                 * @param {String} catalogVersion The current catalog version.
                 * @param {String} pageType The page type for which to retrieve its associated page templates.
                 *
                 * @returns {Promise} A promise that will resolve with the page templates retrieved for the provided page type.
                 *
                 */
                getPageTemplatesForType: function(siteUID, catalogId, catalogVersion, pageType) {
                    return pageTemplateRestService.get({
                        siteUID: siteUID,
                        catalogId: catalogId,
                        catalogVersion: catalogVersion,
                        pageTypeCode: pageType,
                        active: true
                    }).then(function(pageTemplates) {
                        return {
                            templates: pageTemplates.templates.filter(function(pageTemplate) {
                                return NON_SUPPORTED_TEMPLATES.indexOf(pageTemplate.frontEndName) === -1;
                            })
                        };
                    });
                }
            };
        });
})();
