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
describe('Editor Tabset Directive ', function() {

    var tabsetService, genericEditor;

    beforeEach(function() {
        angular.module('tabsetModule', []);
        angular.module('eventServiceModule', []);
        angular.module('genericEditorModule', []);
        angular.module('experienceInterceptorModule', []);
    });

    beforeEach(function() {
        module('editorTabsetModule', function($provide) {
            genericEditor = jasmine.createSpy('genericEditor');

            $provide.value('genericEditor', genericEditor);
        });

        inject(function(editorTabsetService) {
            tabsetService = editorTabsetService;
        });
    });

    function getTabById(tabsList, tabId) {
        return tabsList.filter(function(tab) {
            if (tab) {
                return tab.id == tabId;
            }
            return false;
        })[0];
    }

    it('getTabs will return all default tabs', function() {
        // Arrange
        var expectedTabs = ['adminTab', 'basicTab', 'genericTab'];

        // Act
        var tabsList = tabsetService.getTabsList();

        // Assert
        expectedTabs.forEach(function(tabId) {
            var tab = getTabById(tabsList, tabId);

            expect(tab).not.toBeUndefined();
            expect(tab.id).not.toBeUndefined();
            expect(tab.title).not.toBeUndefined();
            expect(tab.templateUrl).not.toBeUndefined();
            expect(tab.hasErrors).toBe(false);
        });
    });

    it('registerTab will register a tab for a group of content types and will return them with getTabs', function() {
        // Arrange
        var tabId = 'tab1';
        var tabTitle = 'some title';
        var tabTemplateURL = 'some URL';
        var expectedTabs = ['adminTab', 'basicTab', 'genericTab', 'tab1'];
        spyOn(tabsetService, '_validateTab').andCallFake(function() {});

        // Act
        tabsetService.registerTab(tabId, tabTitle, tabTemplateURL);

        // Assert
        var tabsList = tabsetService.getTabsList();
        expect(tabsetService._validateTab).toHaveBeenCalledWith(tabId, tabTitle, tabTemplateURL);
        expectedTabs.forEach(function(tabId) {
            var tab = getTabById(tabsList, tabId);
            expect(tab).not.toBeUndefined();
            expect(tab.id).not.toBeUndefined();
            expect(tab.title).not.toBeUndefined();
            expect(tab.templateUrl).not.toBeUndefined();
            expect(tab.hasErrors).toBe(false);
        });

        var registeredTab = getTabById(tabsList, tabId);
        expect(registeredTab.id).toBe(tabId);
        expect(registeredTab.title).toBe(tabTitle);
        expect(registeredTab.templateUrl).toBe(tabTemplateURL);
    });

    it('registerTab will throw an exception if validation unsuccessful', function() {
        // Arrange
        var tabId = 'tab1';
        var tabTitle = 'some title';
        var tabTemplateURL = 'some URL';
        spyOn(tabsetService, '_validateTab').andThrow("Some error message");

        // Act/Assert
        expect(function() {
            tabsetService.registerTab(tabId, tabTitle, tabTemplateURL);
        }).toThrow("Some error message");
    });

    it('_validateTab will throw an exception for invalid ID', function() {
        // Arrange/Act/Assert
        expect(function() {
            tabsetService._validateTab(null, "some Title", "some URL");
        }).toThrow(new Error("editorTabsetService.registerTab.invalidTabID"));
    });

    it('_validateTab will throw an exception for missing tab title', function() {
        // Arrange/Act/Assert
        expect(function() {
            tabsetService._validateTab("Some ID", null, "some URL");
        }).toThrow(new Error("editorTabsetService.registerTab.missingTabTitle"));
    });

    it('_validateTab will throw an exception for missing template Url', function() {
        // Arrange/Act/Assert
        expect(function() {
            tabsetService._validateTab("Some ID", "some Title", null);
        }).toThrow(new Error("editorTabsetService.registerTab.missingTemplateUrl"));
    });

    it('removeTab will remove a tab for a group of content types', function() {
        // Arrange
        var tabToRemove = 'genericTab';
        var expectedTabs = ['adminTab', 'basicTab'];

        // Act
        tabsetService.deleteTab(tabToRemove);

        // Assert
        var tabsList = tabsetService.getTabsList();
        expectedTabs.forEach(function(tabId) {
            var tab = getTabById(tabsList, tabId);

            expect(tab).not.toBeUndefined();
            expect(tab.id).not.toBeUndefined();
            expect(tab.title).not.toBeUndefined();
            expect(tab.templateUrl).not.toBeUndefined();
            expect(tab.hasErrors).toBe(false);
        });

        expect(tabsList[tabToRemove]).toBeUndefined();
    });
});
