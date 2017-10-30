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
(function() {
    var perspectives;
    var somePerspective = "somenameI18nKey";
    var defaultPerspectiveName = "PERSPECTIVE.NONE.NAME";

    describe('', function() {

        var waitForAppToBeReady = function() {
            perspectives = require("../utils/components/Perspectives.js");

            browser.waitForContainerToBeReady();
            browser.switchToIFrame();
            browser.waitForFrameToBeReady();
        };

        beforeEach(function() {
            browser.get('smarteditcontainerJSTests/e2e/perspectiveService/perspectiveTest.html');
            waitForAppToBeReady();
        });

        it('WHEN application starts THEN default perspective is selected.', function() {

            // Act
            browser.switchToParent();

            // Assert
            perspectives.perspectiveIsSelected(defaultPerspectiveName);
        });

        it('IF application has selected perspective WHEN new perspective is selected THEN features are activated', function() {

            expect(element(by.model('component1')).getText()).toContain('test component 1');
            expect(perspectives.getElementInOverlay('component1', 'componentType1').isPresent()).toBe(false);

            // Act
            perspectives.selectPerspective(somePerspective);

            // Assert
            perspectives.perspectiveIsSelected(somePerspective);

            browser.switchToIFrame();
            expect(element(by.model('component1')).getText()).toContain('test component 1');
            expect(perspectives.getElementInOverlay('component1', 'componentType1').getText()).toContain('Text_is_been_displayed_TextDisplayDecorator');
        });

        it('IF application has not changed default perspective WHEN deep linked THEN default perspective is still selected', function() {

            browser.switchToParent();
            perspectives.perspectiveIsSelected(defaultPerspectiveName);

            // Act
            browser.switchToIFrame();
            element(by.id('deepLink')).click();
            browser.waitForFrameToBeReady();

            // Assert
            browser.switchToParent();
            perspectives.perspectiveIsSelected(defaultPerspectiveName);

            browser.switchToIFrame();
            expect(perspectives.getElementInOverlay('component2', 'componentType2').isPresent()).toBe(false);
            expect(element(by.css('#component2 div')).getInnerHtml()).toContain('component2');
        });

    });

})();
