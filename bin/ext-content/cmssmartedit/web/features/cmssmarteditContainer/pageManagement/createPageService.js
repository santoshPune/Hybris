(function() {
    /**
     * @ngdoc overview
     * @name createPageServiceModule
     * @description
     * # The createPageServiceModule
     *
     * The create page service module provides a service that allows creating new pages.
     *
     */
    angular.module('createPageServiceModule', ['restServiceFactoryModule', 'resourceLocationsModule'])
        /**
         * @ngdoc service
         * @name createPageServiceModule.service:createPageService
         *
         * @description
         * The createPageService allows creating new pages.
         *
         */
        .factory('createPageService', function(restServiceFactory, PAGES_LIST_RESOURCE_URI) {
            var pageRestService = restServiceFactory.get(PAGES_LIST_RESOURCE_URI);

            return {
                /**
                 * @ngdoc method
                 * @name createPageServiceModule.service:createPageService#createPage
                 * @methodOf createPageServiceModule.service:createPageService
                 *
                 * @description
                 * When called this service creates a new page based on the information provided.
                 *
                 * @param {String} siteUID The UID of the current site.
                 * @param {String} catalogId The id of the selected catalog.
                 * @param {String} catalogVersion The current catalog version.
                 * @param {String} page The payload containing the information necessary to create the new page.
                 * NOTE: The payload must at least provide the following fields.
                 * - type: The type of the page.
                 * - typeCode: The type code of the page.
                 * - template: The uid of the page template to use.
                 *
                 * @returns {Promise} A promise that will resolve after saving the page in the backend.
                 *
                 */
                createPage: function(siteUID, catalogId, catalogVersion, page) {
                    var payload = page;
                    payload.siteUID = siteUID;
                    payload.catalogId = catalogId;
                    payload.catalogVersion = catalogVersion;

                    return pageRestService.save(payload);
                }
            };
        });
})();
