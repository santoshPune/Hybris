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
angular.module('i18nMockModule', ['ngMockE2E', 'resourceLocationsModule', 'languageServiceModule'])
    .run(function($httpBackend, I18N_RESOURCE_URI, languageService) {
        $httpBackend.whenGET(I18N_RESOURCE_URI + '/' + languageService.getBrowserLocale()).respond({
            'modal.administration.configuration.edit.title': 'edit configuration',
            'configurationform.actions.cancel': 'cancel',
            'configurationform.actions.submit': 'submit',
            'configurationform.actions.close': 'close',
            'configurationform.json.parse.error': 'this value should be a valid JSON format',
            'configurationform.duplicate.entry.error': 'This is a duplicate key',
            'configurationform.save.error': 'Save error',
            'actions.loadpreview': 'load preview',
            'unknown.request.error': 'Your request could not be processed! Please try again later!',
            'logindialogform.username.or.password.invalid': 'Invalid username or password',
            'logindialogform.username.and.password.required': 'Username and password required',
            'authentication.form.input.username': 'username',
            'authentication.form.input.password': 'password',
            'authentication.form.button.submit': 'submit',
            'type.componenttype1.content.name': 'Content',
            'type.componenttype1.name.name': 'Name',
            'type.componenttype1.mediaContainer.name': 'Media Container',
            'componentform.actions.exit': 'Exit',
            'componentform.actions.cancel': 'Cancel',
            'componentform.actions.submit': 'Submit',
            'abanalytics.popover.title': 'ab analytics',
            'type.componenttype1.content.tooltip': 'enter content',
            'compoment.confirmation.modal.cancel': 'Cancel',
            'component.confirmation.modal.save': 'Save',
            'toolbar.action.render.component': 'Render Component',
            'toolbar.action.render.slot': 'Render Slot'
        });
    });

try {
    angular.module('smarteditloader').requires.push('i18nMockModule');
    angular.module('smarteditcontainer').requires.push('i18nMockModule');
} catch (ex) {}
