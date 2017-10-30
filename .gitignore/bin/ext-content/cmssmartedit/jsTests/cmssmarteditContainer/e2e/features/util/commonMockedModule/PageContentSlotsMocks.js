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
angular.module('pagesContentSlotsMocks', ['ngMockE2E'])
    .run(function($httpBackend) {

        $httpBackend.whenGET(/pagescontentslots\?slotId=otherSlot/).respond({
            pageContentSlotList: [{
                pageId: "homepage",
                slotId: "otherSlot",
                position: 0
            }, {
                pageId: "homepage",
                slotId: "otherSlot",
                position: 1
            }, {
                pageId: "homepage",
                slotId: "otherSlot",
                position: 2
            }]
        });

        $httpBackend.whenGET(/pagescontentslots\?slotId=footerSlot/).respond({
            pageContentSlotList: [{
                pageId: "homepage",
                slotId: "footerSlot",
                position: 0
            }, {
                pageId: "homepage",
                slotId: "footerSlot",
                position: 1
            }, {
                pageId: "homepage",
                slotId: "footerSlot",
                position: 2
            }]
        });

        $httpBackend.whenGET(/pagescontentslots\?slotId=bottomHeaderSlot/).respond({
            pageContentSlotList: [{
                pageId: "homepage",
                slotId: "topHeaderSlot",
                position: 0
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
                position: 1
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
                position: 2
            }]
        });

        $httpBackend.whenGET(/pagescontentslots\?slotId=topHeaderSlot/).respond({
            pageContentSlotList: [{
                pageId: "homepage",
                slotId: "topHeaderSlot",
                position: 0
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
                position: 1
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
                position: 2
            }, {
                pageId: "homepage1",
                slotId: "topHeaderSlot",
                position: 3
            }, {
                pageId: "homepage2",
                slotId: "topHeaderSlot",
                position: 4
            }, {
                pageId: "homepage3",
                slotId: "topHeaderSlot",
                position: 0
            }]
        });

        $httpBackend.whenGET(/pagescontentslots\?pageId=.*/).respond({
            pageContentSlotList: [{
                pageId: "homepage",
                slotId: "topHeaderSlot",
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
            }, {
                pageId: "homepage",
                slotId: "topHeaderSlot",
            }, {
                pageId: "homepage",
                slotId: "bottomHeaderSlot",
            }, {
                pageId: "homepage",
                slotId: "footerSlot",
            }, {
                pageId: "homepage",
                slotId: "otherSlot",
            }]
        });
    });
