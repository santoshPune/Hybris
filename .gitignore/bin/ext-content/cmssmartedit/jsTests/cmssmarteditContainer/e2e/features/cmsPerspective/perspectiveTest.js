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
    var perspective = require('../util/perspective.js');
    var ribbon = require('../util/ribbon.js');
    var storefront = require('../util/storefront.js');

    var SLOT_ID = 'topHeaderSlot';

    describe('', function() {
        var cmsPerspective = "Basic CMS";
        var otherPerspective = "Some other perspective";

        beforeEach(function() {
            browser.get('jsTests/cmssmarteditContainer/e2e/features/cmsPerspective/perspectiveTest.html');
            browser.waitForWholeAppToBeReady();
        });

        it('WHEN other perspective than Basic CMS perspective is selected, no contextual menu button shows', function() {
            perspective.select(otherPerspective);
            storefront.moveToComponent('component1');

            var all = getFirstComponentDecoratorImages();
            expect(all.count()).toBe(0);
        });

        it('WHEN other perspective than Basic CMS perspective is selected, no toolbar button shows', function() {
            perspective.select(otherPerspective);
            ribbon.doesNotHaveAddComponentButton();
        });


        it('WHEN Basic CMS perspective is selected, SimpleResponsiveBannerComponent receives 3 contextual menu buttons : move, delete and edit', function() {
            perspective.select(cmsPerspective);
            storefront.moveToComponent('component1');

            var all = getFirstComponentDecoratorImages();
            expect(all.count()).toBe(3);
            expect(all.get(0).getAttribute('src')).toContain('/cmssmartedit/images/contextualmenu_move_off.png');
            expect(all.get(1).getAttribute('src')).toContain('/cmssmartedit/images/contextualmenu_delete_off.png');
            expect(all.get(2).getAttribute('src')).toContain('/cmssmartedit/images/contextualmenu_edit_off.png');
        });

        it("WHEN Basic CMS perspective is selected, white ribbon receives 'Add component' button", function() {
            perspective.select(cmsPerspective);
            ribbon.hasAddComponentButton();
        });


        function getFirstComponentDecoratorImages() {
            return element.all(by.css('.smartEditComponentX[data-smartedit-component-id="component1"] img'));
        }

    });

})();
