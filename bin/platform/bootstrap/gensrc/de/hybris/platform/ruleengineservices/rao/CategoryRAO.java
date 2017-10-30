/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2013 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 */
package de.hybris.platform.ruleengineservices.rao;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

public class CategoryRAO  implements java.io.Serializable 
{

	/** <i>Generated property</i> for <code>CategoryRAO.code</code> property defined at extension <code>ruleengineservices</code>. */
	private String code;
		
	public CategoryRAO()
	{
		// default constructor
	}
	
		
	public void setCode(final String code)
	{
		this.code = code;
	}
	
		
	public String getCode() 
	{
		return code;
	}
		
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("code", code).toString();
	}

 	@Override
    public boolean equals(Object obj)
    {
       if (obj == null || !this.getClass().equals(obj.getClass())) {
         return false;
       } else if (obj == this) {
         return true;
       }
       CategoryRAO other = (CategoryRAO)obj;
       return new EqualsBuilder()
          .append(code, other.code)
          .isEquals();
    }
    	
	@Override
	public int hashCode()
	{
		return new HashCodeBuilder().append(code).toHashCode();
	}
}