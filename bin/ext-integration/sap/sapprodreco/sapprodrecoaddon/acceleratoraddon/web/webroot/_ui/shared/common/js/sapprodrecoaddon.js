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
function retrieveRecommendations(id, productCode, componentId){
	var baseUrl = $("#" + id).data("baseUrl");
    ajaxUrl = baseUrl+ '/action/recommendations/';
	$.get(ajaxUrl,
			{
				id: id,
				productCode: productCode,
				componentId: componentId
			},
			addRecommendation(id));	
};

function registerClickthrough(id, prodId, leadingitemdstype, scenarioId, prodURL, prodImageURL){
	var baseUrl = $("#" + id).parent().data("baseUrl")
    ajaxUrl = baseUrl + '/action/interaction/';
	
	$.post(ajaxUrl, {
		id: prodId,
		leadingitemdstype: leadingitemdstype,
		scenarioId: scenarioId,
		prodURL: window.location.origin + prodURL,
		prodImageURL: prodImageURL 
	}, null);
};
