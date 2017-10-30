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
describe("GenericEditor form view", function() {
    beforeEach(function() {
        browser.get('smarteditcontainerJSTests/e2e/genericEditor/componentWithExternalTrue/genericEditorTest.html');
    });

    it("given both urlLink and external attributes are present, when the component is rendered then external is a radio selection with true selected", function() {
        expect(by.css("[id='external-checkbox']")).toBeAbsent();

        expect(element.all(by.css("[name='external']")).count()).toBe(2);

        expect(element(by.css("[id='external']")).isPresent()).toBe(true);
        expect(element(by.css("[id='internal']")).isPresent()).toBe(true);

        expect(element(by.css("[id='external']")).isSelected()).toBe(true);
        expect(element(by.css("[id='internal']")).isSelected()).toBe(false);
    });
});
