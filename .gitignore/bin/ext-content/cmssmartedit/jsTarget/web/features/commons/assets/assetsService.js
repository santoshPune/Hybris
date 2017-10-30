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
 * @name assetsServiceModule
 * @description
 * # The assetsServiceModule
 *
 * The assetsServiceModule provides methods to handle assets such as images
 *
 */
angular.module('assetsServiceModule', [])
    /**
     * @ngdoc object
     * @name cmsConstantsModule.object:testAssets
     *
     * @description
     * overridable constant to specify whether cmssmartedit is in test mode
     */
    .constant('testAssets', false)
    /**
     * @ngdoc object
     * @name cmsConstantsModule.service:assetsService
     *
     * @description
     * returns the assets resources root depending whether or not we are in test mode
     */
    .factory('assetsService', ['testAssets', function(testAssets) {
        return {
            getAssetsRoot: function() {
                return testAssets ? '/web/webroot' : '/cmssmartedit';
            }
        };
    }]);
