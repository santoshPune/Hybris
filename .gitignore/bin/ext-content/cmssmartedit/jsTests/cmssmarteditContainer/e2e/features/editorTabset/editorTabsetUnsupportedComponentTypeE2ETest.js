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
describe('Editor TabSet for Unsupported Component Type - ', function() {

    var editorTabset = require('./EditorTabsetPageObject.js');

    beforeEach(function() {
        browser.get('jsTests/cmssmarteditContainer/e2e/features/editorTabset/editorTabset.html');
        browser.click(by.id('unsupportedType-button'));
    });

    it("GIVEN component with unsupported type WHEN I load the editorTabset THEN I don't expect to see the generic tab", function() {

        expect(editorTabset.editorTabset().isPresent()).toBe(true, 'Expected editorTabset to be present');

        expect(editorTabset.basicTab().isPresent()).toBe(true, 'Expected basicTab to be present');
        expect(editorTabset.basicTabContent().isPresent()).toBe(true, 'Expected basicTabContent to be present');
        expect(editorTabset.adminTab().isPresent()).toBe(true, 'Expected adminTab to be present');
        expect(editorTabset.adminTabContent().isPresent()).toBe(true, 'Expected adminTabContent to be present');
        expect(editorTabset.visibilityTab().isPresent()).toBe(true, 'Expected visibilityTab to be present');
        expect(editorTabset.visibilityTabContent().isPresent()).toBe(true, 'Expected visibilityTabContent to be present');
        expect(editorTabset.genericTab().isPresent()).toBe(false, 'Expected genericTab not to be present');
        expect(editorTabset.genericTabContent().isPresent()).toBe(false, 'Expected genericTabContent not to be present');
    });

    it("GIVEN component with unsupported type WHEN I load the editorTabset THEN I expect to see tab1 in nav-tabs", function() {

        expect(editorTabset.editorTabset().isPresent()).toBe(true, 'Expected editorTabset to be present');

        expect(editorTabset.tab1Tab().isPresent()).toBe(true, 'Expected tab1Tab to be present');
        expect(editorTabset.tab1TabContent().isPresent()).toBe(true, 'Expected tab1TabContent to be present');
        expect(editorTabset.tab2Tab().isPresent()).toBe(true, 'Expected tab2DropdownMenu to be present');
        expect(editorTabset.tab2TabContent().isPresent()).toBe(true, 'Expected tab2Content to be present');
        expect(editorTabset.tab3Tab().isPresent()).toBe(true, 'Expected tab3DropdownMenu to be present');
        expect(editorTabset.tab3TabContent().isPresent()).toBe(true, 'Expected tab3TabContent to be present');
    });

});
