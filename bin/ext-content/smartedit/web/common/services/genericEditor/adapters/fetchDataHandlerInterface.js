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
angular.module('FetchDataHandlerInterfaceModule', [])
    /**
     * @ngdoc service
     * @name FetchDataHandlerInterfaceModule.FetchDataHandlerInterface
     *
     * @description
     * Interface describing the contract of a fetchDataHandler fetched through dependency injection by the
     * {@link genericEditorModule.service:GenericEditor GenericEditor} to populate dropdowns
     */
    .factory('FetchDataHandlerInterface', function() {

        var FetchDataHandlerInterface = function() {};

        /**
         * @ngdoc method
         * @name FetchDataHandlerInterfaceModule.FetchDataHandlerInterface#getById
         * @methodOf FetchDataHandlerInterfaceModule.FetchDataHandlerInterface
         *
         * @description
         * will returns a promise resolving to an entity, of type defined by field, matching the given identifier
         *
         * @param {object} field the field descriptor in {@link genericEditorModule.service:GenericEditor GenericEditor}
         * @param {string} identifier the value identifying the entity to fetch
         * @returns {object} an entity
         */
        FetchDataHandlerInterface.prototype.getById = function(field, identifier) {
            throw "getById is not implemented";
        };

        /**
         * @ngdoc method
         * @name FetchDataHandlerInterfaceModule.FetchDataHandlerInterface#findBymask
         * @methodOf FetchDataHandlerInterfaceModule.FetchDataHandlerInterface
         *
         * @description
         * will returns a promise resolving to list of entities, of type defined by field, eligible for a given search mask
         *
         * @param {object} field the field descriptor in {@link genericEditorModule.service:GenericEditor GenericEditor}
         * @param {string} mask the value against witch to fetch entities.
         * @returns {Array} a list of eligible entities
         */
        FetchDataHandlerInterface.prototype.findBymask = function(field, mask) {
            throw "findBymask is not implemented";
        };

        return FetchDataHandlerInterface;
    });
