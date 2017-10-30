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
 *
 *
 */
angular.module('personalizationsmarteditComponentLightUpDecorator', ['personalizationsmarteditTemplates', 'personalizationsmarteditContextServiceModule', 'personalizationsmarteditCommons'])
    .directive('personalizationsmarteditComponentLightUp', function(personalizationsmarteditContextService, personalizationsmarteditUtils) {
        return {
            templateUrl: 'web/features/personalizationsmartedit/componentLightUpDecorator/personalizationsmarteditComponentLightUpDecoratorTemplate.html',
            restrict: 'C',
            transclude: true,
            replace: false,
            scope: {
                smarteditComponentId: '@',
                smarteditComponentType: '@'
            },
            link: function($scope, element, attrs) {
                var isElementSelected = function() {
                    var ctxEnabled = personalizationsmarteditContextService.isPersonalizationContextEnabled();
                    var containerId = personalizationsmarteditUtils.getContainerIdForElement(element);
                    var elementSelected = $.inArray(containerId, personalizationsmarteditContextService.selectedComponents) > -1;
                    return ctxEnabled && elementSelected;
                };

                var isComponentSelected = function() {
                    var componentSelected = isElementSelected();
                    componentSelected = componentSelected && angular.isArray(personalizationsmarteditContextService.selectedVariations);
                    return componentSelected;
                };

                var isVariationComponentSelected = function() {
                    var componentSelected = isElementSelected();
                    componentSelected = componentSelected && angular.isObject(personalizationsmarteditContextService.selectedVariations);
                    componentSelected = componentSelected && !angular.isArray(personalizationsmarteditContextService.selectedVariations);
                    return componentSelected;
                };

                $scope.getPersonalizationComponentBorderClass = function() {
                    var container = element.parent().closest('[data-smartedit-container-id][data-smartedit-container-type="CxCmsComponentContainer"]');
                    container.removeClass("personalizationsmarteditVariationComponentSelected");
                    container.removeClass("personalizationsmarteditVariationComponentSelected-icon");
                    container.removeClass("personalizationsmarteditComponentSelected");
                    if (isVariationComponentSelected()) {
                        container.addClass("personalizationsmarteditVariationComponentSelected");
                        container.addClass("personalizationsmarteditVariationComponentSelected-icon");
                    }
                    if (isComponentSelected()) {
                        container.addClass("personalizationsmarteditComponentSelected");
                    }
                };
            }
        };
    });
