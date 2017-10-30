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
(function() {

    angular.module('e2eOnLoadingSetup')
        .run(function(personalizationsmarteditContextService) {

            personalizationsmarteditContextService.getSeExperienceData = function() {
                return {
                    siteDescriptor: {
                        name: "some name",
                        previewUrl: "/someURI/?someSite=site",
                        uid: "some uid"
                    },
                    catalogDescriptor: {
                        name: "some cat name",
                        catalogId: "some cat uid",
                        catalogVersion: "some cat version"
                    },
                    languageDescriptor: {
                        isocode: "some language isocode",
                    },
                    time: null
                };
            };

        });


})();
