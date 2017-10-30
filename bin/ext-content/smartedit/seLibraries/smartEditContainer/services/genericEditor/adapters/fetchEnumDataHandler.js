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
angular.module('fetchEnumDataHandlerModule', ['FetchDataHandlerInterfaceModule', 'restServiceFactoryModule', 'resourceLocationsModule'])

.factory('fetchEnumDataHandler', function($q, FetchDataHandlerInterface, restServiceFactory, isBlank, extend, ENUM_RESOURCE_URI) {

    var cache = {};

    var FetchEnumDataHandler = function() {
        this.restServiceForEnum = restServiceFactory.get(ENUM_RESOURCE_URI);
    };

    FetchEnumDataHandler = extend(FetchDataHandlerInterface, FetchEnumDataHandler);

    FetchEnumDataHandler.prototype.findByMask = function(field, search) {
        return (cache[field.cmsStructureEnumType] ? $q.when(cache[field.cmsStructureEnumType]) : this.restServiceForEnum.get({
            enumClass: field.cmsStructureEnumType
        })).then(function(response) {
            cache[field.cmsStructureEnumType] = response;
            return cache[field.cmsStructureEnumType].enums.filter(function(element) {
                return isBlank(search) || element.label.toUpperCase().indexOf(search.toUpperCase()) > -1;
            });
        });
    };

    return new FetchEnumDataHandler();
});
