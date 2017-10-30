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
//add alert box to the app

angular.element(document).ready(function() {

    // add sanitization of textarea's and input's
    $('textarea, input[type!=password]').each(function() {
        $(this).attr('data-sanitize-html-input', '');
    });

    //systemModule contains some core decorators centralized on CMSXUI side such as contextualMenu
    window.smartedit.applications.filter(function(moduleName) {
        try {
            var module = angular.module(moduleName);
            angular.module('smartedit').requires.push(moduleName);
            return true;
        } catch (ex) {
            console.error('Failed to load module ' + moduleName + '; SmartEdit functionality may be compromised.');
            return false;
        }
    });

    angular.module('smartedit')
        .constant('domain', window.smartedit.domain)
        .constant('smarteditroot', window.smartedit.smarteditroot);
    angular.bootstrap(document, ["smartedit"]);
});
