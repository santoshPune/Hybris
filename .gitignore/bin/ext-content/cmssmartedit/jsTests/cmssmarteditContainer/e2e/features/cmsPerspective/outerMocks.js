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
angular
    .module('e2eBackendMocks', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])
    .constant('SMARTEDIT_ROOT', 'buildArtifacts')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/jsTests/)
    .run(
        function($httpBackend, languageService, I18N_RESOURCE_URI) {

            var map = [{
                "id": "2",
                "value": "\"thepreviewTicketURI\"",
                "key": "previewTicketURI"
            }, {
                "id": "3",
                "value": "{\"smartEditLocation\":\"/jsTests/cmssmarteditContainer/e2e/features/cmsPerspective/innerMocks.js\"}",
                "key": "applications.BackendMockModule"
            }, {
                "id": "4",
                "value": "{\"smartEditLocation\":\"/web/webroot/cmssmartedit/js/cmssmartedit.js\"}",
                "key": "applications.cmssmartedit"
            }, {
                "id": "5",
                "value": "{\"smartEditContainerLocation\":\"/web/webroot/cmssmartedit/js/cmssmarteditContainer.js\"}",
                "key": "applications.cmssmarteditContainer"
            }, {
                "id": "6",
                "value": "{\"smartEditContainerLocation\":\"/jsTests/cmssmarteditContainer/e2e/features/cmsPerspective/dummyApp.js\"}",
                "key": "applications.otherApp"
            }];

            $httpBackend.whenGET(/configuration/).respond(
                function(method, url, data, headers) {
                    return [200, map];
                });

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
                languages: [{
                    nativeName: 'English',
                    isocode: 'en',
                    name: 'English',
                    required: true
                }]
            });

            $httpBackend
                .whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale())
                .respond({
                    "modal.administration.configuration.edit.title": "edit configuration",
                    "configurationform.actions.cancel": "cancel",
                    "configurationform.actions.submit": "submit",
                    "configurationform.actions.close": "close",
                    "actions.loadpreview": "load preview",
                    'unknown.request.error': 'Your request could not be processed! Please try again later!',
                    'se.cms.perspective.basic.name': 'Basic CMS',
                    'someperspective': 'Some other perspective'
                });

            $httpBackend.whenPOST(/thepreviewTicketURI/)
                .respond({
                    resourcePath: '/jsTests/cmssmarteditContainer/e2e/features/dummystorefront.html',
                    ticketId: 'dasdfasdfasdfa'
                });

            $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/.*\/versions\/.*\/types/).respond(function(method, url, data, headers) {
                return [200, {
                    "componentTypes": [{
                        "code": "CMSParagraphComponent",
                        "i18nKey": "type.cmsparagraphcomponent.name",
                        "name": "Paragraph",
                        "attributes": [{
                            "cmsStructureType": "RichText",
                            "i18nKey": "type.cmsparagraphcomponent.content.name",
                            "qualifier": "content",
                            "localized": true
                        }]
                    }, {
                        "code": "CMSProductListComponent",
                        "i18nKey": "type.cmsproductlistcomponent.name",
                        "name": "Product List Component",
                        "attributes": []
                    }]
                }];
            });


            $httpBackend.whenGET(/^\w+.*/).passThrough();

        });
angular.module('smarteditloader').requires.push('e2eBackendMocks');
angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
