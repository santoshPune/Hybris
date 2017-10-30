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
angular.module('commonSlotMocksModule', ['ngMockE2E'])
    .run(function($httpBackend) {
        $httpBackend.whenGET(/cmswebservices\/v1\/sites\/apparel-uk\/catalogs\/apparel-ukContentCatalog\/versions\/Staged\/items\?uids=.*/).
        respond({
            componentItems: [{
                uid: "component1",
                name: "Component 1",
                visible: true,
                typeCode: "SimpleResponsiveBannerComponent"
            }, {
                uid: "component2",
                name: "Component 2",
                visible: true,
                typeCode: "componentType2"
            }, {
                uid: "component3",
                name: "Component 3",
                visible: true,
                typeCode: "componentType3"
            }, {
                uid: "component4",
                name: "Component 4",
                visible: true,
                typeCode: "componentType4"
            }, {
                uid: "component5",
                name: "Component 5",
                visible: true,
                typeCode: "componentType5"
            }, {
                uid: "hiddenComponent1",
                name: "Hidden Component 1",
                visible: false,
                typeCode: "CMSParagraphComponent"
            }, {
                uid: "hiddenComponent2",
                name: "Hidden Component 2",
                visible: false,
                typeCode: "BannerComponent"
            }, {
                uid: "hiddenComponent3",
                name: "Hidden Component 3",
                visible: false,
                typeCode: "SimpleBannerComponent"
            }]
        });
    });
