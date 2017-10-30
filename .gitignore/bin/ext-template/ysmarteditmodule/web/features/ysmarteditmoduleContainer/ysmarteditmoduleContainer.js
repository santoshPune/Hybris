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
angular.module('ysmarteditmoduleContainer', ['sampleModule', 'featureServiceModule'])
    .run(['$log', 'featureService', function($log, featureService) {
        ////////////////////////////////////////////////////
        // Create Toolbar Item
        ////////////////////////////////////////////////////
        // Create the toolbar item as a feature.
        featureService.addToolbarItem({
            toolbarId: 'experienceSelectorToolbar',
            key: 'yse.sample.toolbar',
            nameI18nKey: 'yse.sample.toolbar.name',
            descriptionI18nKey: 'yse.sample.toolbar.description',
            type: 'HYBRID_ACTION',
            callback: function() {
                $log.info("Action from training.js");
            },
            icons: {
                default: '/training/icons/info.png'
            }
        });
    }]);
