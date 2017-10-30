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
angular.module('configurationExtractorServiceModule', ['functionsModule'])
    .factory('configurationExtractorService', function(convertToArray) {

        var SMARTEDITCONTAINER = 'smartEditContainer';
        var SMARTEDIT = 'smartEdit';

        function getAppsAndLocations(configurations, applicationLayer) {
            var locationName;
            switch (applicationLayer) {
                case SMARTEDITCONTAINER:
                    locationName = 'smartEditContainerLocation';
                    break;
                case SMARTEDIT:
                    locationName = 'smartEditLocation';
                    break;
            }

            var appsAndLocations = convertToArray(configurations).reduce(function(holder, current, index, array) {

                if (current.key.indexOf('applications') === 0 && typeof current.value !== 'string' && typeof current.value !== 'number' && !(current.value instanceof Array)) {
                    if (locationName in current.value) {
                        holder.applications.push(current.key.split('.')[1]);
                        var location = current.value[locationName];
                        if (/^https?\:\/\//.test(location)) {
                            holder.appLocations.push(location);
                        } else {
                            holder.appLocations.push(configurations.domain + location);
                        }
                    }
                    //authenticationMaps from smartedit modules
                    for (var key in current.value.authenticationMap) {
                        holder.authenticationMap[key] = current.value.authenticationMap[key];
                    }
                }
                //authenticationMap from smartedit
                if (current.key === 'authenticationMap') {
                    for (var key2 in current.value) {
                        holder.authenticationMap[key2] = current.value[key2];
                    }
                }
                return holder;
            }, {
                applications: [],
                appLocations: [],
                authenticationMap: {}
            });

            if (applicationLayer === SMARTEDITCONTAINER) {
                appsAndLocations.applications.push('administration');
                appsAndLocations.appLocations.push([configurations.smarteditroot, '/static-resources/smarteditcontainer/modules/administrationModule.js'].join(""));
            } else if (applicationLayer === SMARTEDIT) {
                appsAndLocations.applications.push('systemModule');
                appsAndLocations.appLocations.push([configurations.smarteditroot, '/static-resources/smartedit/modules/systemModule.js'].join(""));
            }
            return appsAndLocations;

        }
        return {
            extractSEContainerModules: function(configurations) {
                return getAppsAndLocations(configurations, SMARTEDITCONTAINER);
            },
            extractSEModules: function(configurations) {
                return getAppsAndLocations(configurations, SMARTEDIT);
            }

        };
    });
