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
describe('Test Personalizationsmartedit Common Module', function() {
    var mockModules = {};
    setupMockModules(mockModules);

    var mockHtml = '<div data-smartedit-component-id="myId" data-smartedit-component-type="myType"></div><div data-smartedit-container-id="myContainer" data-smartedit-container-type="CxCmsComponentContainer" data-smartedit-component-id="myId" data-smartedit-component-type="myType"></div>';
    var mockHtmlWithSlot = '<div data-smartedit-component-id="anotherIdSlot" data-smartedit-component-type="ContentSlot">' + mockHtml + '</div>';

    var personalizationsmarteditMessageHandler, personalizationsmarteditUtils;

    beforeEach(module('personalizationsmarteditCommons'));
    beforeEach(inject(function(_personalizationsmarteditMessageHandler_, _personalizationsmarteditUtils_) {
        personalizationsmarteditMessageHandler = _personalizationsmarteditMessageHandler_;
        personalizationsmarteditUtils = _personalizationsmarteditUtils_;
    }));

    it('All used methods exists in error handler', function() {
        expect(personalizationsmarteditMessageHandler.sendInformation).toBeDefined();
        expect(personalizationsmarteditMessageHandler.sendError).toBeDefined();
    });

    it('GIVEN that sendInformation method properly forward message to alertService', function() {
        personalizationsmarteditMessageHandler.sendInformation("test message");

        expect(mockModules.alertService.pushAlerts).toHaveBeenCalled();
        expect(mockModules.alertService.pushAlerts).toHaveBeenCalledWith([{
            successful: true,
            message: "test message",
        }]);
    });

    it('GIVEN that sendError method properly forward message to alertService', function() {
        personalizationsmarteditMessageHandler.sendError("test error");

        expect(mockModules.alertService.pushAlerts).toHaveBeenCalled();
        expect(mockModules.alertService.pushAlerts).toHaveBeenCalledWith([{
            successful: false,
            message: "test error",
        }]);
    });

    it('All used methods exists in utils library', function() {
        expect(personalizationsmarteditUtils.pushToArrayIfValueExists).toBeDefined();
        expect(personalizationsmarteditUtils.getContainerIdForComponent).toBeDefined();
        expect(personalizationsmarteditUtils.getSlotIdForComponent).toBeDefined();
        expect(personalizationsmarteditUtils.getVariationCodes).toBeDefined();
    });

    it('GIVEN that to array are only added values that exists', function() {
        var testArray = [];
        personalizationsmarteditUtils.pushToArrayIfValueExists(testArray, 'myKey', 'myValue');
        personalizationsmarteditUtils.pushToArrayIfValueExists(testArray, 'myKey2', null);
        personalizationsmarteditUtils.pushToArrayIfValueExists(testArray, 'myKey3', undefined);
        personalizationsmarteditUtils.pushToArrayIfValueExists(testArray, 'myKey4', 'myValue4');

        expect(testArray.length).toEqual(2);
    });

    it('GIVEN that codes for variations array are properly returned', function() {
        var correctArray = [{
            code: "first",
            value: "next"
        }, {
            code: "second",
            value: "none",
            link: "empty"
        }];
        var incorrectArray = [{
            link: "my link",
            value: "next"
        }, {
            connection: "second",
            value: "none",
            link: "empty"
        }];

        var callForEmpty = personalizationsmarteditUtils.getVariationCodes([]);
        var callForCorrect = personalizationsmarteditUtils.getVariationCodes(correctArray);
        var callForIncorrect = personalizationsmarteditUtils.getVariationCodes(incorrectArray);

        expect(callForEmpty.length).toBe(0);

        expect(callForCorrect.length).toBe(2);
        expect(callForCorrect).toContain('first');
        expect(callForCorrect).toContain('second');

        expect(callForIncorrect.length).toBe(0);
    });

    it('GIVEN that element with specified id and type not exist, container id should be null', function() {
        var ret = personalizationsmarteditUtils.getContainerIdForComponent("mockId", "mockType");
        expect(ret).toBe(null);
    });

    it('GIVEN that element with specified id and type not exist, slot id should be null', function() {
        var ret = personalizationsmarteditUtils.getSlotIdForComponent("mockId", "mockType");
        expect(ret).toBe(null);
    });

    it('GIVEN that element exist, container id should be not null', function() {
        var element = angular.element(mockHtml);
        var ret = personalizationsmarteditUtils.getContainerIdForElement(element);
        expect(ret).toBe("myContainer");
    });

    it('GIVEN that element exist, slot id should be not null', function() {
        var element = angular.element(mockHtmlWithSlot);
        var ret = personalizationsmarteditUtils.getSlotIdForElement(element);
        expect(ret).toBe("anotherIdSlot");
    });

});
