/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 30 Oct, 2017 12:11:59 PM                    ---
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
 *  
 */
package de.hybris.platform.ldap.model;

import de.hybris.bootstrap.annotations.Accessor;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.cronjob.model.JobModel;
import de.hybris.platform.cronjob.model.MediaProcessCronJobModel;
import de.hybris.platform.impex.model.ImpExMediaModel;
import de.hybris.platform.ldap.enums.LDIFImportModeEnum;
import de.hybris.platform.ldap.model.ConfigurationMediaModel;
import de.hybris.platform.ldap.model.LDIFMediaModel;
import de.hybris.platform.servicelayer.model.ItemModelContext;

/**
 * Generated model class for type LDIFImportCronJob first defined at extension ldap.
 */
@SuppressWarnings("all")
public class LDIFImportCronJobModel extends MediaProcessCronJobModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "LDIFImportCronJob";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFImportCronJob.searchbase</code> attribute defined at extension <code>ldap</code>. */
	public static final String SEARCHBASE = "searchbase";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFImportCronJob.ldapquery</code> attribute defined at extension <code>ldap</code>. */
	public static final String LDAPQUERY = "ldapquery";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFImportCronJob.resultfilter</code> attribute defined at extension <code>ldap</code>. */
	public static final String RESULTFILTER = "resultfilter";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFImportCronJob.importmode</code> attribute defined at extension <code>ldap</code>. */
	public static final String IMPORTMODE = "importmode";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFImportCronJob.ldifFile</code> attribute defined at extension <code>ldap</code>. */
	public static final String LDIFFILE = "ldifFile";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFImportCronJob.configFile</code> attribute defined at extension <code>ldap</code>. */
	public static final String CONFIGFILE = "configFile";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFImportCronJob.configFile2</code> attribute defined at extension <code>ldap</code>. */
	public static final String CONFIGFILE2 = "configFile2";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFImportCronJob.destMedia</code> attribute defined at extension <code>ldap</code>. */
	public static final String DESTMEDIA = "destMedia";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDIFImportCronJob.dumpMedia</code> attribute defined at extension <code>ldap</code>. */
	public static final String DUMPMEDIA = "dumpMedia";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public LDIFImportCronJobModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public LDIFImportCronJobModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _importmode initial attribute declared by type <code>LDIFImportCronJob</code> at extension <code>ldap</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 */
	@Deprecated
	public LDIFImportCronJobModel(final LDIFImportModeEnum _importmode, final JobModel _job)
	{
		super();
		setImportmode(_importmode);
		setJob(_job);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _importmode initial attribute declared by type <code>LDIFImportCronJob</code> at extension <code>ldap</code>
	 * @param _job initial attribute declared by type <code>CronJob</code> at extension <code>processing</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 */
	@Deprecated
	public LDIFImportCronJobModel(final LDIFImportModeEnum _importmode, final JobModel _job, final ItemModel _owner)
	{
		super();
		setImportmode(_importmode);
		setJob(_job);
		setOwner(_owner);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFImportCronJob.configFile</code> attribute defined at extension <code>ldap</code>. 
	 * @return the configFile
	 */
	@Accessor(qualifier = "configFile", type = Accessor.Type.GETTER)
	public ConfigurationMediaModel getConfigFile()
	{
		return getPersistenceContext().getPropertyValue(CONFIGFILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFImportCronJob.configFile2</code> attribute defined at extension <code>ldap</code>. 
	 * @return the configFile2
	 */
	@Accessor(qualifier = "configFile2", type = Accessor.Type.GETTER)
	public ConfigurationMediaModel getConfigFile2()
	{
		return getPersistenceContext().getPropertyValue(CONFIGFILE2);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFImportCronJob.destMedia</code> attribute defined at extension <code>ldap</code>. 
	 * @return the destMedia
	 */
	@Accessor(qualifier = "destMedia", type = Accessor.Type.GETTER)
	public ImpExMediaModel getDestMedia()
	{
		return getPersistenceContext().getPropertyValue(DESTMEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFImportCronJob.dumpMedia</code> attribute defined at extension <code>ldap</code>. 
	 * @return the dumpMedia
	 */
	@Accessor(qualifier = "dumpMedia", type = Accessor.Type.GETTER)
	public ImpExMediaModel getDumpMedia()
	{
		return getPersistenceContext().getPropertyValue(DUMPMEDIA);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFImportCronJob.importmode</code> attribute defined at extension <code>ldap</code>. 
	 * @return the importmode
	 */
	@Accessor(qualifier = "importmode", type = Accessor.Type.GETTER)
	public LDIFImportModeEnum getImportmode()
	{
		return getPersistenceContext().getPropertyValue(IMPORTMODE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFImportCronJob.ldapquery</code> attribute defined at extension <code>ldap</code>. 
	 * @return the ldapquery
	 */
	@Accessor(qualifier = "ldapquery", type = Accessor.Type.GETTER)
	public String getLdapquery()
	{
		return getPersistenceContext().getPropertyValue(LDAPQUERY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFImportCronJob.ldifFile</code> attribute defined at extension <code>ldap</code>. 
	 * @return the ldifFile
	 */
	@Accessor(qualifier = "ldifFile", type = Accessor.Type.GETTER)
	public LDIFMediaModel getLdifFile()
	{
		return getPersistenceContext().getPropertyValue(LDIFFILE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFImportCronJob.resultfilter</code> attribute defined at extension <code>ldap</code>. 
	 * @return the resultfilter
	 */
	@Accessor(qualifier = "resultfilter", type = Accessor.Type.GETTER)
	public String getResultfilter()
	{
		return getPersistenceContext().getPropertyValue(RESULTFILTER);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDIFImportCronJob.searchbase</code> attribute defined at extension <code>ldap</code>. 
	 * @return the searchbase
	 */
	@Accessor(qualifier = "searchbase", type = Accessor.Type.GETTER)
	public String getSearchbase()
	{
		return getPersistenceContext().getPropertyValue(SEARCHBASE);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFImportCronJob.configFile</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the configFile
	 */
	@Accessor(qualifier = "configFile", type = Accessor.Type.SETTER)
	public void setConfigFile(final ConfigurationMediaModel value)
	{
		getPersistenceContext().setPropertyValue(CONFIGFILE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFImportCronJob.configFile2</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the configFile2
	 */
	@Accessor(qualifier = "configFile2", type = Accessor.Type.SETTER)
	public void setConfigFile2(final ConfigurationMediaModel value)
	{
		getPersistenceContext().setPropertyValue(CONFIGFILE2, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFImportCronJob.destMedia</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the destMedia
	 */
	@Accessor(qualifier = "destMedia", type = Accessor.Type.SETTER)
	public void setDestMedia(final ImpExMediaModel value)
	{
		getPersistenceContext().setPropertyValue(DESTMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFImportCronJob.dumpMedia</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the dumpMedia
	 */
	@Accessor(qualifier = "dumpMedia", type = Accessor.Type.SETTER)
	public void setDumpMedia(final ImpExMediaModel value)
	{
		getPersistenceContext().setPropertyValue(DUMPMEDIA, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFImportCronJob.importmode</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the importmode
	 */
	@Accessor(qualifier = "importmode", type = Accessor.Type.SETTER)
	public void setImportmode(final LDIFImportModeEnum value)
	{
		getPersistenceContext().setPropertyValue(IMPORTMODE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFImportCronJob.ldapquery</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the ldapquery
	 */
	@Accessor(qualifier = "ldapquery", type = Accessor.Type.SETTER)
	public void setLdapquery(final String value)
	{
		getPersistenceContext().setPropertyValue(LDAPQUERY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFImportCronJob.ldifFile</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the ldifFile
	 */
	@Accessor(qualifier = "ldifFile", type = Accessor.Type.SETTER)
	public void setLdifFile(final LDIFMediaModel value)
	{
		getPersistenceContext().setPropertyValue(LDIFFILE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFImportCronJob.resultfilter</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the resultfilter
	 */
	@Accessor(qualifier = "resultfilter", type = Accessor.Type.SETTER)
	public void setResultfilter(final String value)
	{
		getPersistenceContext().setPropertyValue(RESULTFILTER, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDIFImportCronJob.searchbase</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the searchbase
	 */
	@Accessor(qualifier = "searchbase", type = Accessor.Type.SETTER)
	public void setSearchbase(final String value)
	{
		getPersistenceContext().setPropertyValue(SEARCHBASE, value);
	}
	
}
