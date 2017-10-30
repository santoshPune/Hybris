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
angular
    .module('e2eBackendMocks', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])
    .constant('SMARTEDIT_ROOT', 'buildArtifacts')
    .constant('SMARTEDIT_RESOURCE_URI_REGEXP', /^(.*)\/jsTests/)
    .run(
        function($httpBackend, languageService, I18N_RESOURCE_URI) {

            var map = [{
                "id": "2",
                "value": "\"/thepreviewTicketURI\"",
                "key": "previewTicketURI"
            }, {
                "id": "3",
                "value": "\"somepath\"",
                "key": "i18nAPIRoot"
            }, {
                "id": "7",
                "value": "{\"smartEditContainerLocation\":\"/jsTarget/personalizationsmarteditcontainer.js\"}",
                "key": "applications.personalizationsmarteditcontainermodule"
            }, {
                "id": "8",
                "value": "{\"smartEditLocation\":\"/jsTarget/personalizationsmartedit.js\"}",
                "key": "applications.personalizationsmarteditmodule"
            }, {
                "id": "9",
                "value": "{\"smartEditLocation\":\"/jsTests/e2e/customizationVariationManagerModal/innerMocksForCustomizationVariationManagerModal.js\"}",
                "key": "applications.InnerMocks"
            }];

            $httpBackend.whenGET(/configuration/).respond(
                function(method, url, data, headers) {
                    return [200, map];
                });

            $httpBackend.whenPUT(/configuration/).respond(404);

            $httpBackend
                .whenGET(I18N_RESOURCE_URI + "/" + languageService.getBrowserLocale())
                .respond({
                    'personalization.right.toolbar.customization.heading': 'CUSTOMIZATIONS ON THIS PAGE',
                    'personalization.right.toolbar.customization.description': 'Select a customization and target group and customize a component on this page',
                    'personalization.right.toolbar.customization.cust.onpage.action': '',
                    'personalization.right.toolbar.addmorecustomizations.button': 'ADD MORE',
                    'personalization.right.toolbar.addmorecustomizations.button.add': 'ADD',
                    'personalization.right.toolbar.addmorecustomizations.customization.library.search.placeholder': 'Search customizations in the library',
                    'personalization.right.toolbar': 'CUSTOMIZE',
                    'personalization.right.toolbar.customization.button': 'CUSTOMIZATION',
                    'personalization.context.add.action': 'ADD ACTION',
                    'personalization.context.edit.action': 'EDIT ACTION',
                    'personalization.context.delete.action': 'DELETE ACTION',
                    'personalization.context.info.action': 'INFO',
                    'personalization.manager.toolbar': 'LIBRARY',
                    'personalization.manager.modal.title': 'Manage Customization Library',
                    'personalization.manager.modal.customization.label': 'customizations on this catalog version',
                    'personalization.manager.modal.deletecustomization.content': 'Are you sure you want to remove selected customization ?',
                    'personalization.manager.modal.deletevariation.content': 'Are you sure you want to remove selected target group ?',
                    'personalization.manager.modal.grid.header.customization': 'Customization',
                    'personalization.manager.modal.grid.header.variations': 'Target Group',
                    'personalization.manager.modal.grid.header.components': 'Components',
                    'personalization.manager.modal.grid.header.status': 'Status',
                    'personalization.manager.modal.add.button': 'CREATE NEW CUSTOMIZATION',
                    'personalization.manager.modal.search.button': 'Search!',
                    'personalization.manager.modal.search.placeholder': 'Search Customization',
                    'personalization.manager.modal.search.result.label': 'Search Result(s)',
                    'personalization.manager.modal.button.action': '',
                    'personalization.manager.modal.button.edit': 'EDIT',
                    'personalization.manager.modal.button.delete': 'DELETE PERMANENTLY',
                    'personalization.manager.modal.button.moveup': 'MOVE UP',
                    'personalization.manager.modal.button.movedown': 'MOVE DOWN',
                    'personalization.perspective.name': 'PERSONALIZATION',
                    'personalization.perspective.description': 'Personalization description',
                    'personalization.right.toolbar.library.name': 'CUSTOMIZATION LIBRARY',
                    'personalization.topmenu.library.info': 'The library contains all saved customizations for the entire catalog (version)',
                    'personalization.topmenu.library.manager.name': 'MANAGE LIBRARY',
                    'personalization.modal.button.cancel': 'Cancel',
                    'personalization.modal.button.submit': 'Save',
                    'personalization.modal.button.ok': 'Ok',
                    'personalization.modal.button.next': 'Next',
                    'personalization.modal.deleteaction.title': 'Confirm',
                    'personalization.modal.deleteaction.content': 'Are you sure you want to remove selected action ?',
                    'personalization.modal.infoaction.title': 'Information',
                    'personalization.modal.infoaction.content': 'Make a selection - select customization and target group.',
                    'personalization.modal.addaction.title': 'Customize Component',
                    'personalization.modal.editaction.title': 'Customize Component',
                    'personalization.modal.createcomponent.title': 'Create new component',
                    'personalization.modal.addeditaction.createnewcomponent': 'Replace master component creating new one',
                    'personalization.modal.addeditaction.createnewcomponent.banner': 'Banner',
                    'personalization.modal.addeditaction.createnewcomponent.paragraph': 'Paragraph',
                    'personalization.modal.addeditaction.createnewcomponent.simplebanner': 'Simple Banner',
                    'personalization.modal.addeditaction.createnewcomponent.simpleresponsivebanner': 'Simple Responsive Banner',
                    'personalization.modal.addeditaction.usecomponent': 'Replace master component with another saved component',
                    'personalization.modal.addeditaction.selected.customization.title': 'selected customization',
                    'personalization.modal.addeditaction.selected.variation.title': 'selected target group',
                    'personalization.modal.addeditaction.selected.mastercomponent.title': 'master component info',
                    'personalization.modal.addeditaction.selected.actions.title': 'action',
                    'personalization.modal.addeditaction.dropdown.placeholder': 'Select an action type',
                    'personalization.modal.addeditaction.dropdown.componentlist.placeholder': 'Search for a component in the library',
                    'personalization.modal.addeditaction.dropdown.componenttype.placeholder': 'Select component type',
                    'personalization.modal.customizationvariationmanagement.title': 'Customization',
                    'personalization.modal.customizationvariationmanagement.basicinformationtab': 'Basic information *',
                    'personalization.modal.customizationvariationmanagement.basicinformationtab.name': 'Name *',
                    'personalization.modal.customizationvariationmanagement.basicinformationtab.name.placeholder': 'Enter a name for this customization',
                    'personalization.modal.customizationvariationmanagement.basicinformationtab.details': 'Details',
                    'personalization.modal.customizationvariationmanagement.basicinformationtab.details.placeholder': 'Enter a description for this customization',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab': 'Target group *',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.cancelconfirmation': 'Confirm you want to leave and lose unsaved information',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.invalidbuttonid': 'A button callback has not been registered for button with id',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.customization': 'Customization',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.targetgroupname': 'Name *',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.notargetgroups': 'No target group created',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.targetgroupname.placeholder': 'Enter target group name',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.segments': 'Segment *',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.segments.placeholder': 'Search segments',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.criteria': 'Criteria',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.criteria.colon': 'Criteria :',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.allsegments': 'Match all segments',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.anysegments': 'Match any segment',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.addvariation': 'ADD',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.savechanges': 'APPLY',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.cancelchanges': 'CANCEL',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.action': ' ',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.action.edit': 'Edit',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.action.remove': 'Remove',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.action.moveup': 'Move up',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.action.movedown': 'Move down',
                    'personalization.modal.customizationvariationmanagement.targetgrouptab.targetgroup.uniquename.validation.message': 'Target group name needs to be unique',
                    'personalization.pagination.rowsperpage': 'Rows per page',
                    'personalization.info.newpreviewticketcreated': 'Preview ticket has been successfully created',
                    'personalization.info.updatingaction': 'Action has been successfully changed',
                    'personalization.info.creationgaction': 'Action has been successfully added',
                    'personalization.error.gettingcomponents': 'Error during getting components',
                    'personalization.error.creatingcomponent': 'Error during creating component',
                    'personalization.error.removingcomponent': 'Error during removing component',
                    'personalization.error.gettingcustomizations': 'Error during getting customizations',
                    'personalization.error.gettingcustomization': 'Error during getting customization',
                    'personalization.error.creatingcustomization': 'Error during creating customization',
                    'personalization.error.updatingcustomization': 'Error during updating customization',
                    'personalization.error.deletingcustomization': 'Error during deleting customization',
                    'personalization.error.gettingsegments': 'Error during getting segments',
                    'personalization.error.gettingvariation': 'Error during getting variation',
                    'personalization.error.editingvariation': 'Error during editing variation',
                    'personalization.error.deletingvariation': 'Error during deleting variation',
                    'personalization.error.gettingcomponentsforvariation': 'Error during getting components for target group',
                    'personalization.error.gettingpreviewticket': 'Error during getting preview ticket',
                    'personalization.error.updatingpreviewticket': 'Error during updating preview ticket',
                    'personalization.error.creatingpreviewticket': 'Error during creating preview ticket',
                    'personalization.error.previewticketexpired': 'Current preview ticket has expired! Creating new preview ticket',
                    'personalization.error.updatingaction': 'Error during updating action',
                    'personalization.error.creatingaction': 'Error during creating action',
                    'personalization.error.replacingcomponent': 'Error during replacing component with container'
                });

            $httpBackend.whenPOST("/thepreviewTicketURI")
                .respond({
                    resourcePath: '/jsTests/e2e/dummystorefront.html',
                    ticketId: 'dasdfasdfasdfa'
                });

            $httpBackend.whenGET(/fragments/).passThrough();

            $httpBackend.whenGET(/cmswebservices\/v1\/sites\/.*\/languages/).respond({
                languages: [{
                    nativeName: 'English',
                    isocode: 'en',
                    name: 'English',
                    required: true
                }]
            });

            $httpBackend.whenGET(/cmswebservices\/v1\/items\/componentId/).respond({
                catalog: 'Some Catalog - Some Catalog Version',
                dateAndTime: 'Some Date',
                language: 'English'
            });

            $httpBackend.whenGET(/personalizationwebservices\/v1\/catalogs\/.*\/catalogVersions\/.*\/customizations/).respond({
                "customizations": [{
                    "code": "WinterSale",
                    "rank": 0,
                    "variations": [{
                        "active": true,
                        "code": "variationWinterSale1",
                        "rank": 0
                    }, {
                        "active": true,
                        "code": "variationWinterSale2",
                        "rank": 1
                    }]
                }, {
                    "code": "CategoryLover",
                    "rank": 1,
                    "variations": [{
                        "active": true,
                        "code": "variationCategoryLover1",
                        "rank": 0
                    }, {
                        "active": true,
                        "code": "variationCategoryLover2",
                        "rank": 1
                    }]
                }]
            });

            $httpBackend.whenGET(/personalizationwebservices\/v1\/segments/).respond({
                "segments": [{
                    "code": "segment1"
                }, {
                    "code": "segment2"
                }, {
                    "code": "VIPGold"
                }]
            });

        });
angular.module('smarteditloader').requires.push('e2eBackendMocks');
angular.module('smarteditcontainer').requires.push('e2eBackendMocks');
angular.module('editorModalServiceModule', []);
