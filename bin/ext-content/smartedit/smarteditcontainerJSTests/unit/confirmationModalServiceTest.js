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
describe('ConfirmationModalService', function() {

    // Service Under Test
    var confirmationModalService;

    // Mocks
    var modalService;
    var MODAL_BUTTON_ACTIONS;
    var MODAL_BUTTON_STYLES;

    beforeEach(customMatchers);

    // Set-up Mocks
    beforeEach(function() {
        module("modalServiceModule", function($provide) {
            modalService = jasmine.createSpyObj("modalService", ["open"]);

            MODAL_BUTTON_ACTIONS = {
                NONE: "none",
                CLOSE: "close",
                DISMISS: "dismiss"
            };

            MODAL_BUTTON_STYLES = {
                DEFAULT: "default",
                PRIMARY: "primary",
                SECONDARY: "default"
            };

            $provide.value("modalService", modalService);
            $provide.value("MODAL_BUTTON_ACTIONS", MODAL_BUTTON_ACTIONS);
            $provide.value("MODAL_BUTTON_STYLES", MODAL_BUTTON_STYLES);
        });
    });

    // Set-up Service Under Test
    beforeEach(function() {
        module("confirmationModalServiceModule");
        inject(function(_confirmationModalService_) {
            confirmationModalService = _confirmationModalService_;
        });
    });

    // Tests
    it('confirm will throw an exception when no description is provided', function() {
        // Arrange

        // Act

        // Assert
        expect(function() {
            confirmationModalService.confirm({});
        }).toThrow(new Error('confirmation.modal.missing.description'));
    });

    it('confirm will call open on the modalService with the given description when provided with only a description', function() {
        // Arrange

        // Act
        confirmationModalService.confirm({
            description: 'my.confirmation.message'
        });

        // Assert
        expect(modalService.open).toHaveBeenCalledWith({
            size: 'md',
            title: 'confirmation.modal.title',
            templateInline: '<div id="confirmationModalDescription">{{ "my.confirmation.message" | translate }}</div>',
            buttons: [{
                id: 'confirmCancel',
                label: 'confirmation.modal.cancel',
                style: MODAL_BUTTON_STYLES.SECONDARY,
                action: MODAL_BUTTON_ACTIONS.DISMISS
            }, {
                id: 'confirmOk',
                label: 'confirmation.modal.ok',
                action: MODAL_BUTTON_ACTIONS.CLOSE
            }]
        });
    });

    it('confirm will call open on the modalService with the given description and title when provided with a description and title', function() {
        // Arrange

        // Act
        confirmationModalService.confirm({
            title: 'my.confirmation.title',
            description: 'my.confirmation.message'
        });

        // Assert
        expect(modalService.open).toHaveBeenCalledWith({
            size: 'md',
            title: 'my.confirmation.title',
            templateInline: '<div id="confirmationModalDescription">{{ "my.confirmation.message" | translate }}</div>',
            buttons: [{
                id: 'confirmCancel',
                label: 'confirmation.modal.cancel',
                style: MODAL_BUTTON_STYLES.SECONDARY,
                action: MODAL_BUTTON_ACTIONS.DISMISS
            }, {
                id: 'confirmOk',
                label: 'confirmation.modal.ok',
                action: MODAL_BUTTON_ACTIONS.CLOSE
            }]
        });
    });

    it('confirm will call open on the modalService with the given description, placeholders, and title when provided with a description, placeholders and title', function() {

        confirmationModalService.confirm({
            title: 'my.confirmation.title',
            description: 'my.confirmation.message',
            descriptionPlaceholders: {}
        });

        expect(modalService.open).toHaveBeenCalledWith({
            size: 'md',
            title: 'my.confirmation.title',
            templateInline: '<div id="confirmationModalDescription">{{ "my.confirmation.message" | translate: modalController.descriptionPlaceholders }}</div>',
            controller: [jasmine.any(Function)],
            buttons: [{
                id: 'confirmCancel',
                label: 'confirmation.modal.cancel',
                style: MODAL_BUTTON_STYLES.SECONDARY,
                action: MODAL_BUTTON_ACTIONS.DISMISS
            }, {
                id: 'confirmOk',
                label: 'confirmation.modal.ok',
                action: MODAL_BUTTON_ACTIONS.CLOSE
            }]
        });
    });
});
