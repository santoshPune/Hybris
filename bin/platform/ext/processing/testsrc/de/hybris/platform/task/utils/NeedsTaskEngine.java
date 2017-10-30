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

package de.hybris.platform.task.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Marker annotation which let's test framework know, that this test needs a running task engine. <br/>
 * For tests marked with this annotation, test framework will automatically start task engine before, and stop after the
 * test.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(
{ ElementType.TYPE })
public @interface NeedsTaskEngine
{
}
