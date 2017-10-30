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
angular.module('contextualMenuServiceModule', ['functionsModule'])

/**
 * @ngdoc service
 * @name contextualMenuServiceModule.ContextualMenuService
 *
 * @description
 * The ContextualMenuService is used to add contextual menu items for each component.
 *
 * You add contextual menu items to SmartEdit using the Contextual MenuService of the Contextual Menu Service Module.
 * To add items to the contextual menu, you must call the addItems method of the contextualMenuService and pass a map of the component-type array of contextual menu items mapping.
 * The component type names are the keys in the mapping. The component name can be the full name of the component type, an ant-like wildcard (such as  *middle*Suffix), or a valid regex that includes or excludes a set of component types.
 *
 */
.factory('ContextualMenuService', ['hitch', 'uniqueArray', 'regExpFactory', function(hitch, uniqueArray, regExpFactory) {

    var ContextualMenuService = function() {
        this.contextualMenus = {};
    };

    var contextualMenusCallback = function(map, componentType) {
        var componentTypeContextualMenus = map[componentType].filter(hitch(this, function(item) {
            if (!item.key) {
                throw new Error("Item doesn't have key.");
            }

            if (this._featuresList.indexOf(item.key) !== -1) {
                throw new Error("Item with that key already exist.");
            }
            return true;
        }));

        this.contextualMenus[componentType] = uniqueArray((this.contextualMenus[componentType] || []), componentTypeContextualMenus);
    };

    var getFeaturesList = function(contextualMenus) {
        // Would be better to use a set for this, but it's not currently supported by all browsers.
        var featuresList = [];
        for (var key in contextualMenus) {
            featuresList = featuresList.concat(contextualMenus[key].map(function(entry) {
                return entry.key;
            }));
        }

        return featuresList.reduce(function(previous, current) {
            if (previous.indexOf(current) == -1) {
                previous.push(current);
            }

            return previous;
        }, []);
    };

    var getUniqueItemArray = function(array1, array2) {
        var currItem;
        var notEqualToCurrentItem = function(item) {
            return (currItem.key !== item.key);
        };

        array2.forEach(function(item) {
            currItem = item;
            if (array1.every(notEqualToCurrentItem)) {
                array1.push(currItem);
            }
        });

        return array1;
    };

    /**
     * @ngdoc method
     * @name contextualMenuServiceModule.ContextualMenuService#addItems
     * @methodOf contextualMenuServiceModule.ContextualMenuService
     *
     * @description
     * The method called to add contextual menu items to component types in the SmartEdit application.
     * The contextual menu items are then retrieved by the contextual menu decorator to wire the set of menu items to the specified component.
     *
     * Sample Usage:
     * <pre>
     * contextualMenuService.addItems({
     * '.*Component': [{
     *  key: 'itemKey',
     *  i18nKey: 'CONTEXTUAL_MENU',
     *  condition: function(componentType, componentId) {
     * 	return componentId === 'ComponentType';
     * 	},
     *  callback: function(componentType, componentId) {
     * 	alert('callback for ' + componentType + "_" + componentId);
     * 	},
     *  displayClass: 'democlass',
     *  iconIdle: '.../icons/icon.png',
     *  iconNonIdle: '.../icons/icon.png',
     * }]
     * });
     * </pre>
     *
     * @param {Object} contextualMenuItemsMap An object containing list of componentType to contextual menu items mapping
     *
     * The object contains a list that maps component types to arrays of contextual menu items. The mapping is a key-value pair.
     * The key is the name of the component type, for example, Simple Responsive Banner Component, and the value is an array of contextual menu items, like add, edit, localize, etc.
     *
     * The name of the component type is the key in the mapping. The name can be the full name of the component type, an ant-like wildcard (such as *middle), or a vlide regex that includes or excludes a set of component types.
     * The value in the mapping is an array of contextual menu items to be activated for the component type match.
     *
     * The contextual menu items can have the following properties:
     * @param {String} contextualMenuItemsMap.key key Is the key that identifies a contextual menu item.
     * @param {String} contextualMenuItemsMap.i18nKey i18nKey Is the message key of the contextual menu item to be translated.
     * @param {Function} contextualMenuItemsMap.condition condition Is an optional entry that holds the condition function required to activate the menu item. It is invoked with the following payload:
     * <pre>
     * {
                    	componentType: the smartedit component type
                    	componentId: the smartedit component id
                    	containerType: the type of the container wrapping the component, if applicable
                    	containerId: the id of the container wrapping the component, if applicable
                    	element: the dom element of the component onto which the contextual menu is applied
		}
     * </pre>
     * @param {Function} contextualMenuItemsMap.callback Is the action to be performed by clicking on the menu item. It is invoked with:
     * <pre>
     * {
                    	componentType: the smartedit component type
                    	componentId: the smartedit component id
                    	containerType: the type of the container wrapping the component, if applicable
                    	containerId: the id of the container wrapping the component, if applicable
                    	slotId: the id of the content slot containing the component
		}
     * </pre>
     * @param {String} contextualMenuItemsMap.displayClass Contains the CSS classes used to style the contextual menu item
     * @param {String} contextualMenuItemsMap.iconIdle iconIdle Contains the location of the idle icon of the contextual menu item to be displayed.
     * @param {String} contextualMenuItemsMap.iconNonIdle iconNonIdle Contains the location of the non-idle icon of the contextual menu item to be displayed.
     * @param {String} contextualMenuItemsMap.smallIcon smallIcon Contains the location of the smaller version of the icon to be displayed when the menu item is part of the More... menu options.
     *
     */
    ContextualMenuService.prototype.addItems = function(map) {

        try {
            if (map !== undefined) {
                this._featuresList = getFeaturesList(this.contextualMenus);

                var componentTypes = Object.keys(map);
                componentTypes.forEach(hitch(this, contextualMenusCallback, map));
            }
        } catch (e) {
            throw new Error("addItems() - Cannot add items. " + e);
        }
    };

    /**
     * @ngdoc method
     * @name contextualMenuServiceModule.ContextualMenuService#removeItemByKey
     * @methodOf contextualMenuServiceModule.ContextualMenuService
     *
     * @description
     * This method removes the menu items identified by the provided key.
     *
     * @param {String} itemKey The key that identifies the menu items to remove.
     */
    ContextualMenuService.prototype.removeItemByKey = function(itemKey) {
        var filterFunction = function(item) {
            return (item.key !== itemKey);
        };

        for (var contextualMenuKey in this.contextualMenus) {
            var contextualMenuItems = this.contextualMenus[contextualMenuKey];
            this.contextualMenus[contextualMenuKey] = contextualMenuItems.filter(filterFunction);

            if (this.contextualMenus[contextualMenuKey].length === 0) {
                // Remove if the contextual menu is empty.
                delete this.contextualMenus[contextualMenuKey];
            }
        }
    };

    /**
     * @ngdoc method
     * @name contextualMenuServiceModule.ContextualMenuService#getContextualMenuByType
     * @methodOf contextualMenuServiceModule.ContextualMenuService
     *
     * @description
     * Will return an array of contextual menu items for a specific component type.
     * For each key in the contextual menus' object, the method converts each component type into a valid regex using the regExpFactory of the function module and then compares it with the input componentType and, if matched, will add it to an array and returns the array.
     *
     * @param {String} componentType The type code of the selected component
     *
     * @returns {Array} An array of contextual menu items assigned to the type.
     *
     */
    ContextualMenuService.prototype.getContextualMenuByType = function(componentType) {
        var contextualMenuArray = [];
        if (this.contextualMenus) {
            for (var regexpKey in this.contextualMenus) {
                if (regExpFactory(regexpKey).test(componentType)) {
                    contextualMenuArray = getUniqueItemArray(contextualMenuArray, this.contextualMenus[regexpKey]);
                }
            }
        }
        return contextualMenuArray;
    };

    /**
     * @ngdoc method
     * @name contextualMenuServiceModule.ContextualMenuService#getContextualMenuItems
     * @methodOf contextualMenuServiceModule.ContextualMenuService
     *
     * @description
     * Will return an object that contains a list of contextual menu items that are visible and those that are to be added to the More... options.
     *
     * For each component and display limit size, two arrays are generated.
     * One array contains the menu items that can be displayed and the other array contains the menu items that are available under the more menu items action.
     *
     * @param {Object} configuration The configuration used to determine the selected components
     * @param {String} configuration.componentType The type code of the selected component.
     * @param {String} configuration.componentId The ID of the selected component.
     * @param {String} configuration.containerType The type code of the container of the component if applicable, this is optional.
     * @param {String} configuration.containerId The ID of the container of the component if applicable, this is optional.
     * @param {Number} configuration.iLeftBtns The number of visible contextual menu items for a specified component.
     * @param {Element} configuration.element The DOM element of selected component
     * @returns {Object} An array of contextual menu items assigned to the component type.
     *
     * The returned object contains the following properties
     * - leftMenuItems : An array of menu items that can be displayed on the component.
     * - moreMenuItems : An array of menu items that are available under the more menu items action.
     *
     */
    ContextualMenuService.prototype.getContextualMenuItems = function(configuration) {
        var iLeftBtns = configuration.iLeftBtns;
        delete configuration.iLeftBtns;

        var newMenuItems = [];
        var newMoreItems = [];
        var menuItems = this.getContextualMenuByType(configuration.componentType);

        for (var i = 0; i < menuItems.length; i++) {
            if (newMenuItems.length < iLeftBtns) {
                if (menuItems[i].condition === undefined || menuItems[i].condition(configuration) === true) {
                    newMenuItems.push(menuItems[i]);
                }
            } else {
                if (menuItems[i].condition === undefined || menuItems[i].condition(configuration) === true) {
                    newMoreItems.push(menuItems[i]);
                }
            }
        }

        return {
            'leftMenuItems': newMenuItems,
            'moreMenuItems': newMoreItems
        };
    };

    return ContextualMenuService;
}])

/**
 * @ngdoc service
 * @name contextualMenuServiceModule.contextualMenuService
 *
 * @description
 * The contextual menu service factory creates an instance of the  {@link contextualMenuServiceModule.ContextualMenuService ContextualMenuService}
 * each time it is loaded for a component type and a component ID.
 *
 */
.factory('contextualMenuService', ['ContextualMenuService', function(ContextualMenuService) {
    return new ContextualMenuService();
}]);
