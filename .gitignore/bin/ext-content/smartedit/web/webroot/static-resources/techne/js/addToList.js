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
$(document).ready(function(){
    $(document).on('click', '.y_addTolistHandle', function(e){

        e.preventDefault();
        $(e.currentTarget).parents('.y_addToListContainer').find('.panel-heading').toggleClass('active')

    });
});
