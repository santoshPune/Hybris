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
/**
 * @ngdoc overview
 * @name featureInterfaceModule
 */
angular.module('featureInterfaceModule', ['functionsModule'])

/**
 * @ngdoc service
 * @name featureInterfaceModule.service:FeatureServiceInterface
 *
 * @description
 * The interface stipulates how to register features in the SmartEdit application and the SmartEdit container.
 * The SmartEdit implementation stores two instances of the interface across the {@link gatewayFactoryModule.gatewayFactory gateway}: one for the SmartEdit application and one for the SmartEdit container.
 */
.factory('FeatureServiceInterface', function($q, $log, hitch, isBlank) {

    function FeatureServiceInterface() {

    }

    FeatureServiceInterface.prototype._validate = function(configuration) {
        if (isBlank(configuration.key)) {
            throw new Error("featureService.configuration.key.error.required");
        }
        if (isBlank(configuration.nameI18nKey)) {
            throw new Error("featureService.configuration.nameI18nKey.error.required");
        }
        if (isBlank(configuration.enablingCallback) || typeof configuration.enablingCallback != 'function') {
            throw new Error("featureService.configuration.enablingCallback.error.not.function");
        }
        if (isBlank(configuration.disablingCallback) || typeof configuration.disablingCallback != 'function') {
            throw new Error("featureService.configuration.disablingCallback.error.not.function");
        }
    };
    /**
     * @ngdoc method
     * @name featureInterfaceModule.service:FeatureServiceInterface#register
     * @methodOf featureInterfaceModule.service:FeatureServiceInterface
     * @description
     * This method registers a feature.
     * When an end user selects a perspective, all the features that are bound to the perspective
     * will be enabled when their respective enablingCallback functions are invoked
     * and all the features that are not bound to the perspective will be disabled when their respective disablingCallback functions are invoked.
     * The SmartEdit application and the SmartEdit container hold/store an instance of the implementation because callbacks cannot cross the gateway as they are functions.
     *
     * this method is meant to register a feature (identified by a key).
     * When a perspective (registered through {@link perspectiveInterfaceModule.service:PerspectiveServiceInterface#methods_register PerspectiveServiceInterface.register}) is selected, all its bound features will be enabled by invocation of their respective enablingCallback functions
     * and any feature not bound to it will be disabled by invocation of its disablingCallback function.
     * Both SmartEdit and SmartEditContainer will hold a concrete implementation since Callbacks, being functions, cannot cross the gateway.
     * The function will keep a frame bound reference on a full feature in order to be able to invoke its callbacks when needed.
     * 
     * @param {Object} configuration The configuration that represents the feature to be registered.
     * @param {String} configuration.key The key that uniquely identifies the feature in the registry.
     * @param {String} configuration.nameI18nKey The i18n key that stores the feature name to be translated.
     * @param {String} configuration.descriptionI18nKey The i18n key that stores the feature description to be translated. The description is used as a tooltip in the web application. This is an optional parameter.
     * @param {Function} configuration.enablingCallback The callback function invoked to enable the feature when it is required by a perspective.
     * @param {Function} configuration.disablingCallback The callback function invoked to disable the feature when it is not required by a perspective.
     */
    FeatureServiceInterface.prototype.register = function(configuration) {

        this._validate(configuration);

        this.featuresToAlias = this.featuresToAlias || {};
        this.featuresToAlias[configuration.key] = {
            enablingCallback: configuration.enablingCallback,
            disablingCallback: configuration.disablingCallback
        };
        delete configuration.enablingCallback;
        delete configuration.disablingCallback;

        this._registerAliases(configuration);
    };

    FeatureServiceInterface.prototype.enable = function(key) {
        if (this.featuresToAlias && this.featuresToAlias[key]) {
            this.featuresToAlias[key].enablingCallback();
            return;
        } else {
            this._remoteEnablingFromInner(key);
        }
    };

    FeatureServiceInterface.prototype.disable = function(key) {
        if (this.featuresToAlias && this.featuresToAlias[key]) {
            this.featuresToAlias[key].disablingCallback();
            return;
        } else {
            this._remoteDisablingFromInner(key);
        }
    };

    FeatureServiceInterface.prototype._remoteEnablingFromInner = function(key) {};
    FeatureServiceInterface.prototype._remoteDisablingFromInner = function(key) {};

    /**
     * @ngdoc method
     * @name featureInterfaceModule.service:FeatureServiceInterface#_registerAliases
     * @methodOf featureInterfaceModule.service:FeatureServiceInterface
     * @description
     * This method registers a feature, identified by a unique key, across the {@link gatewayFactoryModule.gatewayFactory gateway}.
     * It is a simplified version of the register method, from which callbacks have been removed.
     * 
     * @param {Object} configuration the configuration representing the feature to register
     * @param {String} configuration.key The key that uniquely identifies the feature in the registry.
     * @param {String} configuration.nameI18nKey The i18n key that uniquely identifies the feature name to be translated.
     * @param {String} configuration.descriptionI18nKey The description of the l18n key to be translated. An optional parameter.
     */
    FeatureServiceInterface.prototype._registerAliases = function(configuration) {};

    /**
     * @ngdoc method
     * @name featureInterfaceModule.service:FeatureServiceInterface#addToolbarItem
     * @methodOf featureInterfaceModule.service:FeatureServiceInterface
     *
     * @description
     * This method registers toolbar items as features. It is a wrapper around {@link featureInterfaceModule.service:FeatureServiceInterface#methods_register register}.
     * 
     * @param {Object} configuration The configuration that represents the toolbar action item to be registered.
     * @param {String} configuration.toolbarId The key that uniquely identifies the toolbar that the feature is added to.
     * @param {String} configuration.keyThe key that uniquely identifies the toolbar item in the registry as as defined in the {@link toolbarInterfaceModule.ToolbarServiceInterface#addItems ToolbarServiceInterface.addItems} API.
     * @param {String} configuration.nameI18nKey The i18n key that stores the toolbar item name to be translated.
     * @param {String} configuration.descriptionI18nKey The i18n key that stores the toolbar item description to be translated. This is an optional parameter.
     * @param {Function} configuration.callback The callback that is triggered when the toolbar action item is clicked.
     * @param {String[]} configuration.icons A list of image URLs for the icon images to be displayed in the toolbar for the items. The images are only available for ACTION and HYBRID_ACTION toolbar items.
     * @param {String} configuration.type The type of toolbar item. The possible value are: TEMPLATE, ACTION, and HYBRID_ACTION.
     * @param {String} configuration.include The URL to the HTML template. By default, templates are available for TEMPLATE and HYBRID_ACTION toolbar items.

     */
    FeatureServiceInterface.prototype.addToolbarItem = function(configuration) {};

    /**
     * @ngdoc method
     * @name featureInterfaceModule.service:FeatureServiceInterface#addDecorator
     * @methodOf featureInterfaceModule.service:FeatureServiceInterface
     *
     * @description
     * this method registers decorator and delegates to the
     *  {@link decoratorServiceModule.service:decoratorService#methods_enable enable}
     *  {@link decoratorServiceModule.service:decoratorService#methods_disable disable} methods of 
     *  {@link decoratorServiceModule.service:decoratorService decoratorService}.
     * This method is not a wrapper around {@link decoratorServiceModule.service:decoratorService#addMappings decoratorService.addMappings}:
     * From a feature stand point, we deal with decorators, not their mappings to SmartEdit components.
     * We still need to have a separate invocation of {@link decoratorServiceModule.service:decoratorService#addMappings decoratorService.addMappings} 
     * @param {Object} configuration The configuration that represents the decorator to be registered.
     * @param {Arrays} configuration.key The decorator key defined in the {@link decoratorServiceModule.service:decoratorService#addMappings decoratorService.addMappings} API
     * @param {String} configuration.nameI18nKey the i18n key that stores the decorator name to be translated.
     * @param {String} configuration.descriptionI18nKey The i18n key that stores the decorator description to be translated. The description is used as a tooltip in the web application. This is an optional parameter.
     */
    FeatureServiceInterface.prototype.addDecorator = function(configuration) {};


    /**
     * @ngdoc method
     * @name featureInterfaceModule.service:FeatureServiceInterface#addContextualMenuButton
     * @methodOf featureInterfaceModule.service:FeatureServiceInterface
     *
     * @description
     * This method registers contextual menu buttons. It is a wrapper around {@link contextualMenuServiceModule.ContextualMenuService#methods_addItems ContextualMenuService.addItems}.
     *
     * @param {Object} configuration The configuration representing the decorator to be registered.
     * @param {Arrays} configuration.key The key that uniquely identifies the feature in the registry.
     * @param {String} configuration.regexpKey A regular expression, ant-like wildcard or strict match that identifies the component types eligible for the specified contextual menu button.
     * @param {String} configuration.nameI18nKey They key that stores the name of the button to be translated.
     * @param {String} configuration.descriptionI18nKey The key that stores the description of the button to be translated. An optional parameter.
     * @param {Object} configuration.condition An optional entry that stores the condition required to activate the menu item. it is invoked with:
     * <pre>
     * {
                    	componentType: the smartedit component type
                    	componentId: the smartedit component id
                    	containerType: the type of the container wrapping the component, if applicable
                    	containerId: the id of the container wrapping the component, if applicable
                    	element: the dom element of the component onto which the contextual menu is applied
		}
     * </pre>
     * @param {Object} configuration.callback The action to be performed by clicking on the menu item. It is invoked with
     * <pre>
     * {
                    	componentType: the smartedit component type
                    	componentId: the smartedit component id
                    	containerType: the type of the container wrapping the component, if applicable
                    	containerId: the id of the container wrapping the component, if applicable
                    	slotId: the id of the content slot containing the component
		}
     * </pre>
     * @param {Object} configuration.callbacks A object holding a list of functions where the key is the name of the event to be performed
     * on the element and the value is the event handler function to be invoked when that particular event is triggered.
     * @param {String} configuration.displayClass The CSS classes used to style the contextual menu item.
     * @param {String} configuration.iconIdle The location of the idle icon of the contextual menu item to be displayed.
     * @param {String} configuration.iconNonIdle The location of the non-idle icon of the contextual menu item to be displayed.
     * @param {String} configuration.smallIcon The location of the smaller version of the icon to be displayed when the menu item is part of the More... menu options.
     */
    FeatureServiceInterface.prototype.addContextualMenuButton = function(configuration) {};

    return FeatureServiceInterface;

});
