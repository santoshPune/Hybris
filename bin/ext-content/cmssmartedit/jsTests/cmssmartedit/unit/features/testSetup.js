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
//new file which can be used to add helper methods for testing

//injects rootscope and q for global use
var $rootScope, $q;
testSetup = inject(function(_$rootScope_, _$q_) {
    $rootScope = _$rootScope_;
    $q = _$q_;
});
