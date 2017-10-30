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
package de.hybris.platform.cmswebservices.data;

import de.hybris.platform.cmswebservices.data.MediaData;
import java.util.List;

public  class MediaListData  implements java.io.Serializable 
{


	/** <i>Generated property</i> for <code>MediaListData.media</code> property defined at extension <code>cmswebservices</code>. */
		
	private List<MediaData> media;
	
	public MediaListData()
	{
		// default constructor
	}
	
	
	
	public void setMedia(final List<MediaData> media)
	{
		this.media = media;
	}
	
	
	
	public List<MediaData> getMedia() 
	{
		return media;
	}
	


}