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
angular.module('toolbarInterfaceModule', ['functionsModule'])

/**
 * @ngdoc service
 * @name toolbarInterfaceModule.ToolbarServiceInterface
 *
 * @description
 * Provides an abstract extensible toolbar service. Used to manage and perform actions to either the SmartEdit
 * application or the SmartEdit container.
 *
 * This class serves as an interface and should be extended, not instantiated.
 */
.factory('ToolbarServiceInterface', ['$q', '$log', 'hitch', function($q, $log, hitch) {

    /////////////////////////////////////
    // ToolbarServiceInterface Prototype
    /////////////////////////////////////

    function ToolbarServiceInterface() {}

    ToolbarServiceInterface.prototype.getItems = function() {
        return this.actions;
    };

    ToolbarServiceInterface.prototype.getAliases = function() {
        return this.aliases;
    };

    /**
     * @ngdoc method
     * @name toolbarInterfaceModule.ToolbarServiceInterface#addItems
     * @methodOf toolbarInterfaceModule.ToolbarServiceInterface
     *
     * @description
     * Takes an array of actions and maps them internally for display and trigger through an internal callback key.
     *
     * @param {Object[]} actions - List of actions
     * @param {String} actions.key - Unique identifier of the toolbar action item.
     * @param {Function} actions.callback - Callback triggered when this toolbar action item is clicked.
     * @param {String} actions.i18nkey - Description translation key
     * @param {String[]} actions.icons - List of image URLs for the icon images (only for ACTION and HYBRID_ACTION)
     * @param {String} actions.type - TEMPLATE, ACTION, or HYBRID_ACTION
     * @param {String} actions.include - HTML template URL (only for TEMPLATE and HYBRID_ACTION)
     * @param {Integer} actions.priority - Determines the position of the item in the toolbar, ranging from 0-1000 with the default priority being 500.
     * @param {String} actions.section - Determines the sections(left, middle or right) of the item in the toolbar.
     * An item with a higher priority number will be to the right of an item with a lower priority number in the toolbar.
     */
    ToolbarServiceInterface.prototype.addItems = function(actions) {
        var aliases = actions.filter(hitch(this, function(action) {
            // Validate provided actions -> The filter will return only valid items.
            var includeAction = true;

            if (!action.key) {
                $log.error("addItems() - Cannot add action without key.");
                includeAction = false;
            } else if (action.key in this.actions) {
                $log.debug("addItems() - Action already exists in toolbar with key " + action.key);
                includeAction = false;
            }

            return includeAction;
        })).map(function(action) {
            var key = action.key;
            this.actions[key] = action.callback;
            return {
                key: key,
                name: action.i18nKey,
                icons: action.icons,
                type: action.type,
                include: action.include,
                className: action.className,
                priority: action.priority || 500,
                section: action.section || 'left'
            };
        }, this);

        if (aliases.length > 0) {
            this.addAliases(aliases);
        }
    };

    /////////////////////////////////////
    // Proxied Functions : these functions will be proxied if left unimplemented
    /////////////////////////////////////

    ToolbarServiceInterface.prototype.addAliases = function() {};

    ToolbarServiceInterface.prototype.removeItemByKey = function() {};

    ToolbarServiceInterface.prototype._removeItemOnInner = function() {};

    ToolbarServiceInterface.prototype.removeAliasByKey = function() {};

    ToolbarServiceInterface.prototype.addItemsStyling = function() {};

    ToolbarServiceInterface.prototype.triggerActionOnInner = function() {};

    return ToolbarServiceInterface;
}]);
