/*
 * [y] hybris Platform
 * 
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 * 
 * This software is the confidential and proprietary information of SAP 
 * Hybris ("Confidential Information"). You shall not disclose such 
 * Confidential Information and shall use it only in accordance with the 
 * terms of the license agreement you entered into with SAP Hybris.
 */
package de.hybris.platform.webservices.util.objectgraphtransformer;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * This annotation provides property specific as well as property mapping specific configuration.
 * 
 * @author denny.strietzbaum
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GraphProperty
{
	Class<? extends PropertyInterceptor> interceptor() default PropertyInterceptor.class;

	boolean typecheck() default true;

	boolean virtual() default false;
}
