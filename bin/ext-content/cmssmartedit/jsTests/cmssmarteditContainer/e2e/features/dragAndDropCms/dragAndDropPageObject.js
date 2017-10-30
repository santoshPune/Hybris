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
var DragAndDropPage = function() {


    this.pageURI = 'jsTests/cmssmarteditContainer/e2e/features/dragAndDropCms/dragAndDropCmsTest.html';
    browser.get(this.pageURI);
};

DragAndDropPage.prototype = Object.create({}, {

    //COMPONENT MENU
    componentMenuButton: {
        get: function() {
            browser.switchToParent();
            return element(by.css('#experienceSelectorToolbar_option_0', "experienceSelectorToolbar not found"));
        }
    },
    itemsTab: {
        get: function() {
            browser.switchToParent();
            return element(by.css("[heading='Customized Components']", "customized components heading not found"));
        }
    },
    menuNextDayDeliveryBanner: {
        get: function() {
            browser.switchToParent();
            return element(by.css(".smartEditComponent[data-smartedit-component-id='NextDayDeliveryBanner']"));
        }
    },
    typesTab: {
        get: function() {
            browser.switchToParent();
            return element(by.css("[heading='Component Types']", " component types heading not found"));
        }
    },
    //SLOTS
    slot1: {
        get: function() {
            browser.switchToParent();
            browser.switchToIFrame();
            return element(by.css('#slot1'));
        }
    },
    slot2: {
        get: function() {
            browser.switchToParent();
            browser.switchToIFrame();
            return element(by.css('#slot2'));
        }
    },
    slot3: {
        get: function() {
            browser.switchToParent();
            browser.switchToIFrame();
            return element(by.css('#slot3'));
        }
    },
    slot4: {
        get: function() {
            browser.switchToParent();
            browser.switchToIFrame();
            return element(by.css('#slot4'));
        }
    },
    storeNextDayDeliveryBanner: {
        get: function() {
            browser.switchToParent();
            browser.switchToIFrame();
            return element(by.css(".smartEditComponent[data-smartedit-component-id='NextDayDeliveryBanner']"));
        }
    },
    //helper function
    siblingOf: {
        value: function(originalElement) {
            return originalElement.element(by.xpath('following-sibling::div'));
        }
    },
    openComponentMenu: {
        get: function() {
            return function() {
                browser.switchToParent();
                this.componentMenuButton.click();
                return this;
            }.bind(this);

        }
    },
    selectItemsTab: {
        get: function() {
            return function() {
                browser.switchToParent();
                this.itemsTab.click();
                return this;
            }.bind(this);
        }
    },
    dragAndDropFromMenuToCoord: {
        value: function(source, destination) {
            var that = this;
            destination.getLocation().then(function(navDivLocation) {
                initLeft = navDivLocation.x;
                initTop = navDivLocation.y;
                browser.switchToParent();
                source.getLocation().then(function(orgDivLocation) {
                    originTop = orgDivLocation.y;
                    initTop = initTop - originTop;
                    var sibling = that.siblingOf(source);
                    browser.actions().mouseMove(source)
                        .mouseDown()
                        .mouseMove(sibling)
                        .mouseMove({
                            x: initLeft,
                            y: initTop
                        })
                        .perform();
                    browser.switchToIFrame();
                    browser.actions().mouseUp().perform();

                });
            });

            return that;
        }
    }

});

module.exports = DragAndDropPage;
