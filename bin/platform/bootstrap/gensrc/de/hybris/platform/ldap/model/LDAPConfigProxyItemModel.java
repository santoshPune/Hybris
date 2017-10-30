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
import de.hybris.platform.core.model.security.PrincipalModel;
import de.hybris.platform.ldap.enums.JNDIAuthenticationEnum;
import de.hybris.platform.ldap.enums.LDAPVersionEnum;
import de.hybris.platform.servicelayer.model.ItemModelContext;
import java.util.Collection;

/**
 * Generated model class for type LDAPConfigProxyItem first defined at extension ldap.
 */
@SuppressWarnings("all")
public class LDAPConfigProxyItemModel extends ItemModel
{
	/**<i>Generated model type code constant.</i>*/
	public final static String _TYPECODE = "LDAPConfigProxyItem";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.minimumFailbackTime</code> attribute defined at extension <code>ldap</code>. */
	public static final String MINIMUMFAILBACKTIME = "minimumFailbackTime";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.localAccountsOnly</code> attribute defined at extension <code>ldap</code>. */
	public static final String LOCALACCOUNTSONLY = "localAccountsOnly";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.poolInitsize</code> attribute defined at extension <code>ldap</code>. */
	public static final String POOLINITSIZE = "poolInitsize";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.poolPrefsize</code> attribute defined at extension <code>ldap</code>. */
	public static final String POOLPREFSIZE = "poolPrefsize";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.poolMaxsize</code> attribute defined at extension <code>ldap</code>. */
	public static final String POOLMAXSIZE = "poolMaxsize";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.poolTimeout</code> attribute defined at extension <code>ldap</code>. */
	public static final String POOLTIMEOUT = "poolTimeout";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.poolEnabled</code> attribute defined at extension <code>ldap</code>. */
	public static final String POOLENABLED = "poolEnabled";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.loginField</code> attribute defined at extension <code>ldap</code>. */
	public static final String LOGINFIELD = "loginField";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.jndiFactory</code> attribute defined at extension <code>ldap</code>. */
	public static final String JNDIFACTORY = "jndiFactory";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.socketFactory</code> attribute defined at extension <code>ldap</code>. */
	public static final String SOCKETFACTORY = "socketFactory";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.cacerts</code> attribute defined at extension <code>ldap</code>. */
	public static final String CACERTS = "cacerts";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.clientcerts</code> attribute defined at extension <code>ldap</code>. */
	public static final String CLIENTCERTS = "clientcerts";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.caKeystorePwd</code> attribute defined at extension <code>ldap</code>. */
	public static final String CAKEYSTOREPWD = "caKeystorePwd";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.clientKeystorePwd</code> attribute defined at extension <code>ldap</code>. */
	public static final String CLIENTKEYSTOREPWD = "clientKeystorePwd";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.caKeystoreType</code> attribute defined at extension <code>ldap</code>. */
	public static final String CAKEYSTORETYPE = "caKeystoreType";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.clientKeystoreType</code> attribute defined at extension <code>ldap</code>. */
	public static final String CLIENTKEYSTORETYPE = "clientKeystoreType";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.jndiVersion</code> attribute defined at extension <code>ldap</code>. */
	public static final String JNDIVERSION = "jndiVersion";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.jndiAuthentication</code> attribute defined at extension <code>ldap</code>. */
	public static final String JNDIAUTHENTICATION = "jndiAuthentication";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.jndiPrincipals</code> attribute defined at extension <code>ldap</code>. */
	public static final String JNDIPRINCIPALS = "jndiPrincipals";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.jndiCredentials</code> attribute defined at extension <code>ldap</code>. */
	public static final String JNDICREDENTIALS = "jndiCredentials";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.securityProtocol</code> attribute defined at extension <code>ldap</code>. */
	public static final String SECURITYPROTOCOL = "securityProtocol";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.serverUrl</code> attribute defined at extension <code>ldap</code>. */
	public static final String SERVERURL = "serverUrl";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.serverRootDN</code> attribute defined at extension <code>ldap</code>. */
	public static final String SERVERROOTDN = "serverRootDN";
	
	/** <i>Generated constant</i> - Attribute key of <code>LDAPConfigProxyItem.jndiConnectTimeout</code> attribute defined at extension <code>ldap</code>. */
	public static final String JNDICONNECTTIMEOUT = "jndiConnectTimeout";
	
	
	/**
	 * <i>Generated constructor</i> - Default constructor for generic creation.
	 */
	public LDAPConfigProxyItemModel()
	{
		super();
	}
	
	/**
	 * <i>Generated constructor</i> - Default constructor for creation with existing context
	 * @param ctx the model context to be injected, must not be null
	 */
	public LDAPConfigProxyItemModel(final ItemModelContext ctx)
	{
		super(ctx);
	}
	
	/**
	 * <i>Generated constructor</i> - Constructor with all mandatory attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _loginField initial attribute declared by type <code>LDAPConfigProxyItem</code> at extension <code>ldap</code>
	 * @param _serverUrl initial attribute declared by type <code>LDAPConfigProxyItem</code> at extension <code>ldap</code>
	 */
	@Deprecated
	public LDAPConfigProxyItemModel(final String _loginField, final Collection<String> _serverUrl)
	{
		super();
		setLoginField(_loginField);
		setServerUrl(_serverUrl);
	}
	
	/**
	 * <i>Generated constructor</i> - for all mandatory and initial attributes.
	 * @deprecated Since 4.1.1 Please use the default constructor without parameters
	 * @param _loginField initial attribute declared by type <code>LDAPConfigProxyItem</code> at extension <code>ldap</code>
	 * @param _owner initial attribute declared by type <code>Item</code> at extension <code>core</code>
	 * @param _serverUrl initial attribute declared by type <code>LDAPConfigProxyItem</code> at extension <code>ldap</code>
	 */
	@Deprecated
	public LDAPConfigProxyItemModel(final String _loginField, final ItemModel _owner, final Collection<String> _serverUrl)
	{
		super();
		setLoginField(_loginField);
		setOwner(_owner);
		setServerUrl(_serverUrl);
	}
	
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.cacerts</code> attribute defined at extension <code>ldap</code>. 
	 * @return the cacerts
	 */
	@Accessor(qualifier = "cacerts", type = Accessor.Type.GETTER)
	public String getCacerts()
	{
		return getPersistenceContext().getPropertyValue(CACERTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.caKeystorePwd</code> attribute defined at extension <code>ldap</code>. 
	 * @return the caKeystorePwd
	 */
	@Accessor(qualifier = "caKeystorePwd", type = Accessor.Type.GETTER)
	public String getCaKeystorePwd()
	{
		return getPersistenceContext().getPropertyValue(CAKEYSTOREPWD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.caKeystoreType</code> attribute defined at extension <code>ldap</code>. 
	 * @return the caKeystoreType
	 */
	@Accessor(qualifier = "caKeystoreType", type = Accessor.Type.GETTER)
	public String getCaKeystoreType()
	{
		return getPersistenceContext().getPropertyValue(CAKEYSTORETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.clientcerts</code> attribute defined at extension <code>ldap</code>. 
	 * @return the clientcerts
	 */
	@Accessor(qualifier = "clientcerts", type = Accessor.Type.GETTER)
	public String getClientcerts()
	{
		return getPersistenceContext().getPropertyValue(CLIENTCERTS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.clientKeystorePwd</code> attribute defined at extension <code>ldap</code>. 
	 * @return the clientKeystorePwd
	 */
	@Accessor(qualifier = "clientKeystorePwd", type = Accessor.Type.GETTER)
	public String getClientKeystorePwd()
	{
		return getPersistenceContext().getPropertyValue(CLIENTKEYSTOREPWD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.clientKeystoreType</code> attribute defined at extension <code>ldap</code>. 
	 * @return the clientKeystoreType
	 */
	@Accessor(qualifier = "clientKeystoreType", type = Accessor.Type.GETTER)
	public String getClientKeystoreType()
	{
		return getPersistenceContext().getPropertyValue(CLIENTKEYSTORETYPE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.jndiAuthentication</code> attribute defined at extension <code>ldap</code>. 
	 * @return the jndiAuthentication
	 */
	@Accessor(qualifier = "jndiAuthentication", type = Accessor.Type.GETTER)
	public JNDIAuthenticationEnum getJndiAuthentication()
	{
		return getPersistenceContext().getPropertyValue(JNDIAUTHENTICATION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.jndiConnectTimeout</code> attribute defined at extension <code>ldap</code>. 
	 * @return the jndiConnectTimeout
	 */
	@Accessor(qualifier = "jndiConnectTimeout", type = Accessor.Type.GETTER)
	public Integer getJndiConnectTimeout()
	{
		return getPersistenceContext().getPropertyValue(JNDICONNECTTIMEOUT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.jndiCredentials</code> attribute defined at extension <code>ldap</code>. 
	 * @return the jndiCredentials
	 */
	@Accessor(qualifier = "jndiCredentials", type = Accessor.Type.GETTER)
	public String getJndiCredentials()
	{
		return getPersistenceContext().getPropertyValue(JNDICREDENTIALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.jndiFactory</code> attribute defined at extension <code>ldap</code>. 
	 * @return the jndiFactory
	 */
	@Accessor(qualifier = "jndiFactory", type = Accessor.Type.GETTER)
	public String getJndiFactory()
	{
		return getPersistenceContext().getPropertyValue(JNDIFACTORY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.jndiPrincipals</code> attribute defined at extension <code>ldap</code>. 
	 * @return the jndiPrincipals
	 */
	@Accessor(qualifier = "jndiPrincipals", type = Accessor.Type.GETTER)
	public String getJndiPrincipals()
	{
		return getPersistenceContext().getPropertyValue(JNDIPRINCIPALS);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.jndiVersion</code> attribute defined at extension <code>ldap</code>. 
	 * @return the jndiVersion
	 */
	@Accessor(qualifier = "jndiVersion", type = Accessor.Type.GETTER)
	public LDAPVersionEnum getJndiVersion()
	{
		return getPersistenceContext().getPropertyValue(JNDIVERSION);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.localAccountsOnly</code> attribute defined at extension <code>ldap</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the localAccountsOnly
	 */
	@Accessor(qualifier = "localAccountsOnly", type = Accessor.Type.GETTER)
	public Collection<PrincipalModel> getLocalAccountsOnly()
	{
		return getPersistenceContext().getPropertyValue(LOCALACCOUNTSONLY);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.loginField</code> attribute defined at extension <code>ldap</code>. 
	 * @return the loginField
	 */
	@Accessor(qualifier = "loginField", type = Accessor.Type.GETTER)
	public String getLoginField()
	{
		return getPersistenceContext().getPropertyValue(LOGINFIELD);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.minimumFailbackTime</code> attribute defined at extension <code>ldap</code>. 
	 * @return the minimumFailbackTime
	 */
	@Accessor(qualifier = "minimumFailbackTime", type = Accessor.Type.GETTER)
	public Integer getMinimumFailbackTime()
	{
		return getPersistenceContext().getPropertyValue(MINIMUMFAILBACKTIME);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.poolEnabled</code> attribute defined at extension <code>ldap</code>. 
	 * @return the poolEnabled
	 */
	@Accessor(qualifier = "poolEnabled", type = Accessor.Type.GETTER)
	public Boolean getPoolEnabled()
	{
		return getPersistenceContext().getPropertyValue(POOLENABLED);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.poolInitsize</code> attribute defined at extension <code>ldap</code>. 
	 * @return the poolInitsize
	 */
	@Accessor(qualifier = "poolInitsize", type = Accessor.Type.GETTER)
	public Integer getPoolInitsize()
	{
		return getPersistenceContext().getPropertyValue(POOLINITSIZE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.poolMaxsize</code> attribute defined at extension <code>ldap</code>. 
	 * @return the poolMaxsize
	 */
	@Accessor(qualifier = "poolMaxsize", type = Accessor.Type.GETTER)
	public Integer getPoolMaxsize()
	{
		return getPersistenceContext().getPropertyValue(POOLMAXSIZE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.poolPrefsize</code> attribute defined at extension <code>ldap</code>. 
	 * @return the poolPrefsize
	 */
	@Accessor(qualifier = "poolPrefsize", type = Accessor.Type.GETTER)
	public Integer getPoolPrefsize()
	{
		return getPersistenceContext().getPropertyValue(POOLPREFSIZE);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.poolTimeout</code> attribute defined at extension <code>ldap</code>. 
	 * @return the poolTimeout
	 */
	@Accessor(qualifier = "poolTimeout", type = Accessor.Type.GETTER)
	public Integer getPoolTimeout()
	{
		return getPersistenceContext().getPropertyValue(POOLTIMEOUT);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.securityProtocol</code> attribute defined at extension <code>ldap</code>. 
	 * @return the securityProtocol
	 */
	@Accessor(qualifier = "securityProtocol", type = Accessor.Type.GETTER)
	public String getSecurityProtocol()
	{
		return getPersistenceContext().getPropertyValue(SECURITYPROTOCOL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.serverRootDN</code> attribute defined at extension <code>ldap</code>. 
	 * @return the serverRootDN
	 */
	@Accessor(qualifier = "serverRootDN", type = Accessor.Type.GETTER)
	public String getServerRootDN()
	{
		return getPersistenceContext().getPropertyValue(SERVERROOTDN);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.serverUrl</code> attribute defined at extension <code>ldap</code>. 
	 * Consider using FlexibleSearchService::searchRelation for pagination support of large result sets.
	 * @return the serverUrl
	 */
	@Accessor(qualifier = "serverUrl", type = Accessor.Type.GETTER)
	public Collection<String> getServerUrl()
	{
		return getPersistenceContext().getPropertyValue(SERVERURL);
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LDAPConfigProxyItem.socketFactory</code> attribute defined at extension <code>ldap</code>. 
	 * @return the socketFactory
	 */
	@Accessor(qualifier = "socketFactory", type = Accessor.Type.GETTER)
	public String getSocketFactory()
	{
		return getPersistenceContext().getPropertyValue(SOCKETFACTORY);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.cacerts</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the cacerts
	 */
	@Accessor(qualifier = "cacerts", type = Accessor.Type.SETTER)
	public void setCacerts(final String value)
	{
		getPersistenceContext().setPropertyValue(CACERTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.caKeystorePwd</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the caKeystorePwd
	 */
	@Accessor(qualifier = "caKeystorePwd", type = Accessor.Type.SETTER)
	public void setCaKeystorePwd(final String value)
	{
		getPersistenceContext().setPropertyValue(CAKEYSTOREPWD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.caKeystoreType</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the caKeystoreType
	 */
	@Accessor(qualifier = "caKeystoreType", type = Accessor.Type.SETTER)
	public void setCaKeystoreType(final String value)
	{
		getPersistenceContext().setPropertyValue(CAKEYSTORETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.clientcerts</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the clientcerts
	 */
	@Accessor(qualifier = "clientcerts", type = Accessor.Type.SETTER)
	public void setClientcerts(final String value)
	{
		getPersistenceContext().setPropertyValue(CLIENTCERTS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.clientKeystorePwd</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the clientKeystorePwd
	 */
	@Accessor(qualifier = "clientKeystorePwd", type = Accessor.Type.SETTER)
	public void setClientKeystorePwd(final String value)
	{
		getPersistenceContext().setPropertyValue(CLIENTKEYSTOREPWD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.clientKeystoreType</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the clientKeystoreType
	 */
	@Accessor(qualifier = "clientKeystoreType", type = Accessor.Type.SETTER)
	public void setClientKeystoreType(final String value)
	{
		getPersistenceContext().setPropertyValue(CLIENTKEYSTORETYPE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.jndiAuthentication</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the jndiAuthentication
	 */
	@Accessor(qualifier = "jndiAuthentication", type = Accessor.Type.SETTER)
	public void setJndiAuthentication(final JNDIAuthenticationEnum value)
	{
		getPersistenceContext().setPropertyValue(JNDIAUTHENTICATION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.jndiConnectTimeout</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the jndiConnectTimeout
	 */
	@Accessor(qualifier = "jndiConnectTimeout", type = Accessor.Type.SETTER)
	public void setJndiConnectTimeout(final Integer value)
	{
		getPersistenceContext().setPropertyValue(JNDICONNECTTIMEOUT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.jndiCredentials</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the jndiCredentials
	 */
	@Accessor(qualifier = "jndiCredentials", type = Accessor.Type.SETTER)
	public void setJndiCredentials(final String value)
	{
		getPersistenceContext().setPropertyValue(JNDICREDENTIALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.jndiFactory</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the jndiFactory
	 */
	@Accessor(qualifier = "jndiFactory", type = Accessor.Type.SETTER)
	public void setJndiFactory(final String value)
	{
		getPersistenceContext().setPropertyValue(JNDIFACTORY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.jndiPrincipals</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the jndiPrincipals
	 */
	@Accessor(qualifier = "jndiPrincipals", type = Accessor.Type.SETTER)
	public void setJndiPrincipals(final String value)
	{
		getPersistenceContext().setPropertyValue(JNDIPRINCIPALS, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.jndiVersion</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the jndiVersion
	 */
	@Accessor(qualifier = "jndiVersion", type = Accessor.Type.SETTER)
	public void setJndiVersion(final LDAPVersionEnum value)
	{
		getPersistenceContext().setPropertyValue(JNDIVERSION, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.localAccountsOnly</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the localAccountsOnly
	 */
	@Accessor(qualifier = "localAccountsOnly", type = Accessor.Type.SETTER)
	public void setLocalAccountsOnly(final Collection<PrincipalModel> value)
	{
		getPersistenceContext().setPropertyValue(LOCALACCOUNTSONLY, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.loginField</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the loginField
	 */
	@Accessor(qualifier = "loginField", type = Accessor.Type.SETTER)
	public void setLoginField(final String value)
	{
		getPersistenceContext().setPropertyValue(LOGINFIELD, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.minimumFailbackTime</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the minimumFailbackTime
	 */
	@Accessor(qualifier = "minimumFailbackTime", type = Accessor.Type.SETTER)
	public void setMinimumFailbackTime(final Integer value)
	{
		getPersistenceContext().setPropertyValue(MINIMUMFAILBACKTIME, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.poolEnabled</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the poolEnabled
	 */
	@Accessor(qualifier = "poolEnabled", type = Accessor.Type.SETTER)
	public void setPoolEnabled(final Boolean value)
	{
		getPersistenceContext().setPropertyValue(POOLENABLED, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.poolInitsize</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the poolInitsize
	 */
	@Accessor(qualifier = "poolInitsize", type = Accessor.Type.SETTER)
	public void setPoolInitsize(final Integer value)
	{
		getPersistenceContext().setPropertyValue(POOLINITSIZE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.poolMaxsize</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the poolMaxsize
	 */
	@Accessor(qualifier = "poolMaxsize", type = Accessor.Type.SETTER)
	public void setPoolMaxsize(final Integer value)
	{
		getPersistenceContext().setPropertyValue(POOLMAXSIZE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.poolPrefsize</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the poolPrefsize
	 */
	@Accessor(qualifier = "poolPrefsize", type = Accessor.Type.SETTER)
	public void setPoolPrefsize(final Integer value)
	{
		getPersistenceContext().setPropertyValue(POOLPREFSIZE, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.poolTimeout</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the poolTimeout
	 */
	@Accessor(qualifier = "poolTimeout", type = Accessor.Type.SETTER)
	public void setPoolTimeout(final Integer value)
	{
		getPersistenceContext().setPropertyValue(POOLTIMEOUT, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.securityProtocol</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the securityProtocol
	 */
	@Accessor(qualifier = "securityProtocol", type = Accessor.Type.SETTER)
	public void setSecurityProtocol(final String value)
	{
		getPersistenceContext().setPropertyValue(SECURITYPROTOCOL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.serverRootDN</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the serverRootDN
	 */
	@Accessor(qualifier = "serverRootDN", type = Accessor.Type.SETTER)
	public void setServerRootDN(final String value)
	{
		getPersistenceContext().setPropertyValue(SERVERROOTDN, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.serverUrl</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the serverUrl
	 */
	@Accessor(qualifier = "serverUrl", type = Accessor.Type.SETTER)
	public void setServerUrl(final Collection<String> value)
	{
		getPersistenceContext().setPropertyValue(SERVERURL, value);
	}
	
	/**
	 * <i>Generated method</i> - Setter of <code>LDAPConfigProxyItem.socketFactory</code> attribute defined at extension <code>ldap</code>. 
	 *  
	 * @param value the socketFactory
	 */
	@Accessor(qualifier = "socketFactory", type = Accessor.Type.SETTER)
	public void setSocketFactory(final String value)
	{
		getPersistenceContext().setPropertyValue(SOCKETFACTORY, value);
	}
	
}
