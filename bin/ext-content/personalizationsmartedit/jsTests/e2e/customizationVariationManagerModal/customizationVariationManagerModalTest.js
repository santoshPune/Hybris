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
describe('Customization Modal - ', function() {

    beforeEach(function() {
        browser.get('jsTests/e2e/customizationVariationManagerModal/customizationVariationManagerModalTest.html');
        browser.waitForWholeAppToBeReady();

        openPerspectiveSelector();
        clickPersonalizationPerspective();
        clickManage();
        clickCreateNewCustomization();
    });

    it('GIVEN user provides customization name and target group name WHEN user selects a segment THEN add button is clickable', function() {
        // GIVEN
        enterCustomizationName("customizationA");
        clickNextButton();
        enterTargetGroupName("TARGETGROUPA");

        // WHEN
        openSegmentList();
        selectFirstSegmentFromTop();

        // THEN
        addButtonIsClickable();
    });

    it('GIVEN user provides customization name and target group name WHEN user adds a segment THEN save button is clickable', function() {
        // GIVEN
        enterCustomizationName("customizationA");
        clickNextButton();
        enterTargetGroupName("TARGETGROUPA");

        // WHEN
        openSegmentList();
        selectFirstSegmentFromTop();
        clickAddButton();

        // THEN
        saveButtonIsClickable();
    });

    it('GIVEN user provides customization name and target group name WHEN two segments are being added THEN criteria radio buttons are visible', function() {
        // GIVEN
        enterCustomizationName("customizationA");
        clickNextButton();
        enterTargetGroupName("TARGETGROUPA");

        // WHEN
        openSegmentList();
        selectFirstSegmentFromTop();
        openSegmentList();
        selectFirstSegmentFromTop();

        // THEN
        criteriaRadioButtonsAreEnabled();
    });

    it('GIVEN user provides customization name and target group name WHEN he adds a target group with two segments with "all" criteria THEN "all" criteria is displayed with the group', function() {
        // GIVEN
        enterCustomizationName("customizationA");
        clickNextButton();
        enterTargetGroupName("TARGETGROUPA");

        // WHEN
        openSegmentList();
        selectFirstSegmentFromTop();
        openSegmentList();
        selectFirstSegmentFromTop();
        clickAddButton();

        // THEN
        targetGroupWithUseAllSegmentsFirstFromTop();
    });

    it('GIVEN user provides customization name and target group name WHEN he adds a target group with two segments with "any" criteria THEN "any" criteria is displayed with the group', function() {
        // GIVEN
        enterCustomizationName("customizationA");
        clickNextButton();
        enterTargetGroupName("TARGETGROUPA");

        // WHEN
        openSegmentList();
        selectFirstSegmentFromTop();
        openSegmentList();
        selectFirstSegmentFromTop();
        clickUseAnySegments();
        clickAddButton();

        // THEN
        targetGroupWithUseAnySegmentsFirstFromTop();
    });

    it('GIVEN a customization has at least one segment WHEN " /edit" is selected THEN "APPLY" and "CANCEL" buttons are clickable', function() {
        // GIVEN
        enterCustomizationName("customizationA");
        clickNextButton();
        enterTargetGroupName("TARGETGROUPA");
        openSegmentList();
        selectFirstSegmentFromTop();
        clickAddButton();

        // WHEN
        openFirstActionDropdownFromTop();
        clickEdit();

        //THEN
        changesButtonsAreEnabled();
    });

    it('GIVEN a customization has one segment WHEN " /remove" is selected THEN customization has no segments', function() {
        // GIVEN
        enterCustomizationName("customizationA");
        clickNextButton();
        enterTargetGroupName("TARGETGROUPA");
        openSegmentList();
        selectFirstSegmentFromTop();
        clickAddButton();

        // WHEN
        openFirstActionDropdownFromTop();
        clickRemove();
        clickConfirmOk();

        //THEN
        customizationHasNoTargetGroups();
    });

    it('GIVEN two target groups exist WHEN user moves up or moves down a target group THEN target groups are rearranged properly', function() {
        var targetGroup1Name = "TARGETGROUPA";
        var targetGroup2Name = "TARGETGROUPB";

        //GIVEN
        enterCustomizationName("customizationA");
        clickNextButton();

        enterTargetGroupName(targetGroup1Name);
        openSegmentList();
        selectFirstSegmentFromTop();
        clickAddButton();

        enterTargetGroupName(targetGroup2Name);
        openSegmentList();
        selectFirstSegmentFromTop();
        clickAddButton();

        targetGroupIsNthFromTop(targetGroup1Name, 0);
        targetGroupIsNthFromTop(targetGroup2Name, 1);

        //WHEN
        openActionDropdownByTargetGroupName(targetGroup1Name);
        clickMoveDown();

        //THEN
        targetGroupIsNthFromTop(targetGroup1Name, 1);
        targetGroupIsNthFromTop(targetGroup2Name, 0);

        //WHEN
        openActionDropdownByTargetGroupName(targetGroup1Name);
        clickMoveUp();

        //THEN
        targetGroupIsNthFromTop(targetGroup1Name, 0);
        targetGroupIsNthFromTop(targetGroup2Name, 1);
    });

    // Actions
    function openPerspectiveSelector() {
        var perspectiveSelectorButton = $("div[class*='ySEPerspectiveSelector']").$("button");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(perspectiveSelectorButton), browser.params.timeout, "Could not open perspective selector");
        perspectiveSelectorButton.click();
    }

    function clickPersonalizationPerspective() {
        var personalizationPerspectiveButton = $$("li[class*='ySEPerspectiveList--item']").filter(function(elm) {
            return elm.$("a").getText().then(function(text) {
                return text == 'PERSONALIZATION';
            });
        }).first();

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(personalizationPerspectiveButton), browser.params.timeout, "Could not select personalization perspective");
        personalizationPerspectiveButton.click();
    }

    function clickManage() {
        var manageButton = $("span[data-translate='personalization.manager.toolbar']").element(by.xpath('..'));

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(manageButton), browser.params.timeout, "Could not click 'MANAGE'");
        manageButton.click();
    }

    function clickCustomize() {
        var customizeButton = $("span[data-translate='personalization.right.toolbar']").element(by.xpath('..'));

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(customizeButton), browser.params.timeout, "Could not click 'CUSTOMIZE'");
        customizeButton.click();
    }

    function clickManagePersonalization() {
        var managePersonalizationButton = $("a[data-translate='personalization.manager.modal.title']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(managePersonalizationButton), browser.params.timeout, "Could not select 'MANAGE PERSONALIZATION'");
        managePersonalizationButton.click();
    }

    function clickCreateNewCustomization() {
        var createNewCustomizationButton = $("a[data-translate='personalization.manager.modal.add.button']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(createNewCustomizationButton), browser.params.timeout, "Could not select 'CREATE NEW CUSTOMIZATION'");
        createNewCustomizationButton.click();
    }

    function enterCustomizationName(customizationName) {
        var customizationNameInput = element(by.model('customization.name'));

        browser.wait(protractor.ExpectedConditions.visibilityOf(customizationNameInput), browser.params.timeout, "could not find input for customization name");
        customizationNameInput.sendKeys(customizationName);
    }

    function enterCustomizationDetails(customizationDetails) {
        var customizationDetailsInput = element(by.model('customization.description'));

        browser.wait(protractor.ExpectedConditions.visibilityOf(customizationDetailsInput), browser.params.timeout, "could not find input for customization description");
        customizationDetailsInput.sendKeys(customizationDetails);
    }

    function enterTargetGroupName(targetGroupName) {
        var targetGroupNameInput = element(by.model('edit.name'));

        browser.wait(protractor.ExpectedConditions.visibilityOf(targetGroupNameInput), browser.params.timeout, "could not find input for target group name");
        targetGroupNameInput.sendKeys(targetGroupName);
    }

    function clickNextButton() {
        var nextButton = $('#confirmNext');

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(nextButton), browser.params.timeout, "could not click on NEXT");
        nextButton.click();
    }

    function openSegmentList() {
        var segmentsInput = element(by.model('edit.selectedSegments')).$("input[role='combobox']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(segmentsInput), browser.params.timeout, "could not find input for segments");
        segmentsInput.click();
    }

    function selectFirstSegmentFromTop() {
        var firstSegment = element.all(by.repeater('item in $select.items')).first();

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(firstSegment), browser.params.timeout, "could not click on the first segment");
        firstSegment.click();
    }

    function clickAddButton() {
        var addButton = $("button[class*='add-target-group']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(addButton), browser.params.timeout, "Could not click ADD button");
        addButton.click();
    }

    function openFirstActionDropdownFromTop() {
        var firstActionButton = element.all(by.repeater('variation in customization.variations')).first().$("button");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(firstActionButton), browser.params.timeout, "Could not open dropdown");
        firstActionButton.click();
    }

    function openActionDropdownByTargetGroupName(targetGroupName) {
        var targetGroupRow = element.all(by.repeater('variation in customization.variations')).filter(function(elm) {
            return elm.element(by.binding('variation.name')).getText().then(function(text) {
                return text == targetGroupName;
            });
        }).first().$("div[class*='row target-group-list']");

        var actionButton = targetGroupRow.$("button");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(actionButton), browser.params.timeout, "Could not open dropdown");
        actionButton.click();
    }

    function clickEdit() {
        var editButton = element.all(by.repeater('variation in customization.variations')).first().element(by.xpath("..")).$("div[class*='open']")
            .$("a[data-translate='personalization.modal.customizationvariationmanagement.targetgrouptab.action.edit']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(editButton), browser.params.timeout, "Could not click on EDIT in dropdown menu");
        editButton.click();
    }

    function clickRemove() {
        var removeButton = element.all(by.repeater('variation in customization.variations')).first().element(by.xpath("..")).$("div[class*='open']")
            .$("a[data-translate='personalization.modal.customizationvariationmanagement.targetgrouptab.action.remove']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(removeButton), browser.params.timeout, "Could not click on REMOVE in dropdown menu");
        removeButton.click();
    }

    function clickMoveUp() {
        var moveUpButton = element.all(by.repeater('variation in customization.variations')).first().element(by.xpath("..")).$("div[class*='open']")
            .$("a[data-translate='personalization.modal.customizationvariationmanagement.targetgrouptab.action.moveup']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(moveUpButton), browser.params.timeout, "Could not click on MOVE UP in dropdown menu");
        moveUpButton.click();
    }

    function clickMoveDown() {
        var moveDownButton = element.all(by.repeater('variation in customization.variations')).first().element(by.xpath("..")).$("div[class*='open']")
            .$("a[data-translate='personalization.modal.customizationvariationmanagement.targetgrouptab.action.movedown']");

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(moveDownButton), browser.params.timeout, "Could not click on MOVE DOWN in dropdown menu");
        moveDownButton.click();
    }

    function clickUseAllSegments() {
        var allSegmentsRadioButton = element.all(by.model('edit.allSegmentsChecked')).get(0);

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(allSegmentsRadioButton), browser.params.timeout, "Could not click on 'Use all segments' checkbox");
        allSegmentsRadioButton.click();
    }

    function clickUseAnySegments() {
        var anySegmentsRadioButton = element.all(by.model('edit.allSegmentsChecked')).get(1);

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(anySegmentsRadioButton), browser.params.timeout, "Could not click on 'Use any segments' checkbox");
        anySegmentsRadioButton.click();
    }

    function clickConfirmOk() {
        var confirmOkButton = element(by.id('smartedit-modal-title-confirmation.modal.title')).element(by.xpath('../..')).element(by.id('confirmOk'));

        browser.wait(protractor.ExpectedConditions.elementToBeClickable(confirmOkButton), browser.params.timeout, "Could not click on OK button in confirmation modal");
        confirmOkButton.click();
    }

    // Expectations
    function addButtonIsClickable() {
        var addButton = $("button[class*='add-target-group']");

        expect(addButton.isDisplayed()).toBe(true);
        expect(addButton.isEnabled()).toBe(true);
    }

    function saveButtonIsClickable() {
        var saveButton = element(by.id('confirmOk'));

        expect(saveButton.isDisplayed()).toBe(true);
        expect(saveButton.isEnabled()).toBe(true);
    }

    function criteriaRadioButtonsAreEnabled() {
        var criteriaRadioButtons = element.all(by.model('edit.allSegmentsChecked'));

        expect(criteriaRadioButtons.count()).toBe(2);
        expect(criteriaRadioButtons.get(0).isDisplayed()).toBe(true);
        expect(criteriaRadioButtons.get(0).isEnabled()).toBe(true);
        expect(criteriaRadioButtons.get(1).isDisplayed()).toBe(true);
        expect(criteriaRadioButtons.get(1).isEnabled()).toBe(true);

        expect($("label[data-translate='personalization.modal.customizationvariationmanagement.targetgrouptab.anysegments']").isDisplayed()).toBe(true);
        expect($("label[data-translate='personalization.modal.customizationvariationmanagement.targetgrouptab.allsegments']").isDisplayed()).toBe(true);
    }

    function changesButtonsAreEnabled() {
        var cancelChangesButton = $("button[class*='cancel-target-group-edit']");
        var saveChangesButton = $("button[class*='submit-target-group-edit']");

        expect(cancelChangesButton.isDisplayed()).toBe(true);
        expect(cancelChangesButton.isEnabled()).toBe(true);
        expect(saveChangesButton.isDisplayed()).toBe(true);
        expect(saveChangesButton.isEnabled()).toBe(true);
    }

    function targetGroupWithUseAnySegmentsFirstFromTop() {
        expect(
                $("span[data-translate='personalization.modal.customizationvariationmanagement.targetgrouptab.criteria.colon']")
                .element(by.xpath(".."))
                .element(by.exactBinding('getCriteriaDescrForVariation(variation)'))
                .getText())
            .toEqual("Match any segment");
    }

    function targetGroupWithUseAllSegmentsFirstFromTop() {
        expect(
                $("span[data-translate='personalization.modal.customizationvariationmanagement.targetgrouptab.criteria.colon']")
                .element(by.xpath(".."))
                .element(by.exactBinding('getCriteriaDescrForVariation(variation)'))
                .getText())
            .toEqual("Match all segments");
    }

    function customizationHasNoTargetGroups() {
        var noTargetGroupsLabel = $("h6[data-translate='personalization.modal.customizationvariationmanagement.targetgrouptab.notargetgroups']");
        expect(noTargetGroupsLabel.isPresent()).toBe(true);
        expect(noTargetGroupsLabel.isDisplayed()).toBe(true);
    }

    function targetGroupIsNthFromTop(searchedTargetGroupName, n) {
        var nthTargetGroupName = element.all(by.binding('variation.name')).get(n).getText();
        expect(nthTargetGroupName).toEqual(searchedTargetGroupName);
    }
});
