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
angular.module('personalizationsmarteditPreviewServiceModule', ['personalizationsmarteditRestServiceModule',
        'sharedDataServiceModule',
        'personalizationsmarteditCommons',
        'personalizationsmarteditContextServiceModule'
    ])
    .factory('personalizationsmarteditPreviewService', function($q, $filter, personalizationsmarteditRestService, sharedDataService, personalizationsmarteditUtils, personalizationsmarteditMessageHandler, personalizationsmarteditContextService) {

        var previewService = {};

        previewService.removePersonalizationDataFromPreview = function(previewTicketId) {
            var deferred = $q.defer();
            previewService.updatePreviewTicketWithVariations(previewTicketId, []).then(function successCallback(previewTicket) {
                deferred.resolve(previewTicket);
            }, function errorCallback(response) {
                deferred.reject(response);
            });
            return deferred.promise;
        };

        previewService.updatePreviewTicketWithVariations = function(previewTicketId, variations) {
            var deferred = $q.defer();
            personalizationsmarteditRestService.getPreviewTicket(previewTicketId).then(function successCallback(previewTicket) {
                previewTicket.variations = variations;
                deferred.resolve(personalizationsmarteditRestService.updatePreviewTicket(previewTicket));
            }, function errorCallback(response) {
                if (response.status === 404) {
                    //preview ticket not found - let's try to create a new one with the same parameters
                    deferred.resolve(previewService.createPreviewTicket(variations));
                } else {
                    personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.gettingpreviewticket'));
                    deferred.reject(response);
                }
            });

            return deferred.promise;
        };

        previewService.createPreviewTicket = function(variationsForPreview) {
            var experience = personalizationsmarteditContextService.getSeExperienceData();
            var configuration = personalizationsmarteditContextService.getSeConfigurationData();

            var deferred = $q.defer();

            var resourcePath = configuration.domain + experience.siteDescriptor.previewUrl;

            var previewTicket = {
                catalog: experience.catalogDescriptor.catalogId,
                catalogVersion: experience.catalogDescriptor.catalogVersion,
                language: experience.languageDescriptor.isocode,
                resourcePath: resourcePath,
                variations: variationsForPreview
            };

            personalizationsmarteditRestService.createPreviewTicket(previewTicket).then(function successCallback(response) {
                previewService.storePreviewTicketData(response.resourcePath, response.ticketId);
                personalizationsmarteditContextService.refreshPreviewData();
                personalizationsmarteditMessageHandler.sendInformation($filter('translate')('personalization.info.newpreviewticketcreated'));
                deferred.resolve(response);
            }, function errorCallback(response) {
                personalizationsmarteditMessageHandler.sendError($filter('translate')('personalization.error.creatingpreviewticket'));
                deferred.reject(response);
            });

            return deferred.promise;
        };

        previewService.storePreviewTicketData = function(resourcePathToStore, previewTicketIdToStore) {
            var previewToStore = {
                previewTicketId: previewTicketIdToStore,
                resourcePath: resourcePathToStore
            };
            sharedDataService.set('preview', previewToStore);
        };


        return previewService;
    });
