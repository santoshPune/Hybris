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
angular.module('ysmarteditmodule', [
        'featureServiceModule', // Feature API Module from SmartEdit Application
        'perspectiveServiceModule', // Perspective API Module from SmartEdit Application
        'decoratorServiceModule', // Decorator API Module from SmartEdit Application
        'sampleDecorator' // Decorators must be added as dependencies to be wired into SmartEdit
    ])
    .run(
        function(decoratorService, featureService, perspectiveService) { // Parameters are injected factory methods

            ////////////////////////////////////////////////////
            // Create Decorator
            ////////////////////////////////////////////////////
            // First define the new decorator mapping (which components it will be assigned to).
            decoratorService.addMappings({
                'SimpleResponsiveBannerComponent': ['yse.sample.decorator'],
                'CMSParagraphComponent': ['yse.sample.decorator']
            });

            // Then create the decorator as a feature.
            featureService.addDecorator({
                key: 'yse.sample.decorator',
                nameI18nKey: 'yse.sample.decorator.name'
            });

            ////////////////////////////////////////////////////
            // Create  Perspective and assign features.
            ////////////////////////////////////////////////////
            // Group the features created above in a perspective. This will enable the features once the user selects this new perspective.
            perspectiveService.register({
                key: 'yse.sample.perspective',
                nameI18nKey: 'yse.sample.perspective.name',
                descriptionI18nKey: 'yse.sample.perspective.description',
                features: ['yse.sample.toolbar', 'yse.sample.decorator'],
                perspectives: []
            });

        });
