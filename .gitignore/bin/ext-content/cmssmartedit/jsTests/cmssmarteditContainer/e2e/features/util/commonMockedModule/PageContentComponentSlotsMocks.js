/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2016 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
angular.module('pagesContentSlotsComponentsMocks', ['ngMockE2E'])
    .run(function($httpBackend) {

        $httpBackend.whenGET(/cmswebservices\/v1\/sites\/apparel-uk\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/pagescontentslotscomponents\?pageId=.*/).respond({
            pageContentSlotComponentList: [{
                pageId: "homepage",
                slotId: "topHeaderSlot",
                componentId: "component1",
                position: 0
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
                componentId: "component2",
                position: 1
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
                componentId: "component3",
                position: 2
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
                componentId: "hiddenComponent1",
                position: 3
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
                componentId: "hiddenComponent2",
                position: 4
            }, {
                pageId: "homepage",
                slotId: "bottomHeaderSlot",
                componentId: "component4",
                position: 0
            }, {
                pageId: "homepage",
                slotId: "footerSlot",
                componentId: "component5",
                position: 0
            }, {
                pageId: "homepage",
                slotId: "otherSlot",
                componentId: "hiddenComponent3",
                position: 0
            }]
        });

        $httpBackend.whenGET(/cmswebservices\/v1\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/pagescontentslotscomponents\?pageId=.*/).respond({
            pageContentSlotComponentList: [{
                pageId: "homepage",
                slotId: "topHeaderSlot",
                componentId: "component1",
                position: 0
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
                componentId: "component2",
                position: 1
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
                componentId: "component3",
                position: 2
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
                componentId: "hiddenComponent1",
                position: 3
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
                componentId: "hiddenComponent2",
                position: 4
            }, {
                pageId: "homepage",
                slotId: "bottomHeaderSlot",
                componentId: "component4",
                position: 0
            }, {
                pageId: "homepage",
                slotId: "footerSlot",
                componentId: "component5",
                position: 0
            }, {
                pageId: "homepage",
                slotId: "otherSlot",
                componentId: "hiddenComponent3",
                position: 0
            }]
        });
    });
