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
 * @name editorEnablerServiceModule
 * @description
 * # The editorEnablerServiceModule
 *
 * The editor enabler service module provides a service that allows enabling the Edit Component contextual menu item,
 * providing a SmartEdit CMS admin the ability to edit various properties of the given component.
 */
angular.module("editorEnablerServiceModule", ["featureServiceModule", "editorModalServiceModule", "functionsModule"])

/**
 * @ngdoc service
 * @name editorEnablerServiceModule.service:editorEnablerService
 *
 * @description
 * Convenience service to attach the open editor modal action to the contextual menu of a given component type, or
 * given regex corresponding to a selection of component types.
 *
 * Example: The Edit button is added to the contextual menu of the CMSParagraphComponent, and all types postfixed
 * with BannerComponent.
 *
 * <pre>
    angular.module('app', ['editorEnablerServiceModule'])
        .run(function(editorEnablerService) {
            editorEnablerService.enableForComponents(['CMSParagraphComponent', '*BannerComponent']);
        });
 * </pre>
 */
.factory("editorEnablerService", ['featureService', 'editorModalService', 'hitch', function(featureService, editorModalService, hitch) {

    // Class Definition
    function EditorEnablerService() {}

    // Private


    EditorEnablerService.prototype._key = "se.cms.edit";

    EditorEnablerService.prototype._nameI18nKey = "contextmenu.nameI18nKey.edit";

    EditorEnablerService.prototype._descriptionI18nKey = "contextmenu.descriptionI18n.edit";

    EditorEnablerService.prototype._editDisplayClass = "editbutton";

    EditorEnablerService.prototype._editButtonIconIdle = "/cmssmartedit/images/contextualmenu_edit_off.png";

    EditorEnablerService.prototype._editButtonIconNonIdle = "/cmssmartedit/images/contextualmenu_edit_on.png";

    EditorEnablerService.prototype._editButtonSmallIcon = "/cmssmartedit/images/contextualmenu_edit_on.png";

    EditorEnablerService.prototype._editButtonCallback = function(configuration) {
        editorModalService.open(configuration.componentType, configuration.componentId);
    };

    // Public
    /**
     * @ngdoc method
     * @name editorEnablerServiceModule.service:editorEnablerService#enableForComponents
     * @methodOf editorEnablerServiceModule.service:editorEnablerService
     *
     * @description
     * Enables the Edit contextual menu item for the given component types.
     *
     * @param {Array} componentTypes The list of component types, as defined in the platform, for which to enable the
     * Edit contextual menu.
     */
    EditorEnablerService.prototype.enableForComponents = function(componentTypes) {
        var contextualMenuConfig = {
            key: this._key, // It's the same key as the i18n, however here we're not doing any i18n work.
            nameI18nKey: this._nameI18nKey,
            descriptionI18nKey: this._descriptionI18nKey,
            regexpKeys: componentTypes,
            displayClass: this._editDisplayClass,
            i18nKey: this._editI18nKey,
            iconIdle: this._editButtonIconIdle,
            iconNonIdle: this._editButtonIconNonIdle,
            smallIcon: this._editButtonSmallIcon,
            callback: this._editButtonCallback
        };

        featureService.addContextualMenuButton(contextualMenuConfig);
    };

    // Factory Definition
    return new EditorEnablerService();
}]);
