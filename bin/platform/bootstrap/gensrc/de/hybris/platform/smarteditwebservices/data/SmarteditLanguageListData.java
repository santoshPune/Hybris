/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN!
 * --- Generated at 30 Oct, 2017 12:12:01 PM
 * ----------------------------------------------------------------
 *
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
package de.hybris.platform.smarteditwebservices.data;

import de.hybris.platform.smarteditwebservices.data.SmarteditLanguageData;
import java.util.List;

public  class SmarteditLanguageListData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>SmarteditLanguageListData.languages</code> property defined at extension <code>smarteditwebservices</code>. */
		
	private List<SmarteditLanguageData> languages;
	
	public SmarteditLanguageListData()
	{
		// default constructor
	}
	
	
	
	public void setLanguages(final List<SmarteditLanguageData> languages)
	{
		this.languages = languages;
	}
	
	
	
	public List<SmarteditLanguageData> getLanguages() 
	{
		return languages;
	}
	


}