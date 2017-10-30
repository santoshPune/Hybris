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
package de.hybris.platform.cmswebservices.util;

import static de.hybris.platform.cmswebservices.util.models.CatalogVersionModelMother.CatalogVersion.STAGED;
import static de.hybris.platform.cmswebservices.util.models.ContentCatalogModelMother.CatalogTemplate.ID_APPLE;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.util.SpringCustomContextLoader;
import de.hybris.platform.cmswebservices.constants.CmswebservicesConstants;
import de.hybris.platform.cmswebservices.util.apiclient.AbstractApiClientInvocation;
import de.hybris.platform.cmswebservices.util.apiclient.ApiClient;
import de.hybris.platform.cmswebservices.util.apiclient.impl.JaxRsApiClientInvocation;
import de.hybris.platform.cmswebservices.util.apiclient.impl.SpringApiClientInvocation;
import de.hybris.platform.cmswebservices.util.models.SiteModelMother;
import de.hybris.platform.core.Registry;
import de.hybris.platform.impex.jalo.ImpExException;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.webservicescommons.jaxb.Jaxb2HttpMessageConverter;
import de.hybris.platform.webservicescommons.webservices.AbstractWebServicesTest;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.junit.Before;
import org.junit.Ignore;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.util.UriTemplate;


@Ignore("Just a testing base class.")
@IntegrationTest
@ContextConfiguration(locations =
{ "classpath:/cmswebservices-spring-test.xml" })
public class ApiBaseIntegrationTest extends AbstractWebServicesTest
{
	protected static final String VERSION_ID = "versionId";
	protected static final String CATALOG_ID = "catalogId";
	protected static final String TOKEN_TEMPLATE = "bearer %s";

	private ApiClient apiClient;
	private String authorizationToken;

	protected static SpringCustomContextLoader springCustomContextLoader = null;

	private static final String FAILURE_MESSAGE = "Wrong HTTP status at response";

	@Resource(name = "cmsJsonHttpMessageConverter")
	private Jaxb2HttpMessageConverter cmsJsonHttpMessageConverter;

	@Resource
	private MappingJackson2HttpMessageConverter cmsMappingJackson2HttpMessageConverter;

	@Resource
	private ConfigurationService configurationService;

	public ApiBaseIntegrationTest()
	{
		if (springCustomContextLoader == null)
		{
			try
			{
				springCustomContextLoader = new SpringCustomContextLoader(getClass());
				springCustomContextLoader.loadApplicationContexts((GenericApplicationContext) Registry.getCoreApplicationContext());
				springCustomContextLoader.loadApplicationContextByConvention((GenericApplicationContext) Registry
						.getCoreApplicationContext());
			}
			catch (final Exception e)
			{
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	@Override
	protected Client createClient(final ClientConfig config) throws NoSuchAlgorithmException, KeyManagementException
	{
		config.register(MultiPartFeature.class);
		return super.createClient(config);
	}

	@Before
	public void setupApiClient()
	{
		apiClient =  new ApiClient(generateAuthorizationHeader()) ;
		apiClient.setApiClientInvocation(getJaxRsApiClientInvocation());
	}

	private String generateAuthorizationHeader() {
		try
		{
			importCsv("/cmswebservices/test/impex/essentialTestDataAuth.impex", "utf-8");
		}
		catch (final ImpExException e)
		{
			throw new RuntimeException(e.getMessage(), e);
		}

		final String accessToken = getOAuth2TokenUsingResourceOwnerPassword(setupWebResource(getAuthorizationExtensionInfo()), "trusted_client", "secret", "cmsmanager", "1234");
		return String.format(TOKEN_TEMPLATE, accessToken);
	}

	/**
	 * Unmarshall a response into a DTO.
	 *
	 * @param response
	 *           - the WS response
	 * @param clazz
	 *           - the DTO class type into which to unmarshall the response
	 * @return the unmarshalled DTO
	 * @throws JAXBException
	 *            - when an error occurs during the unmarshalling process
	 */
	@Deprecated
	protected <C> C unmarshallResult(final Response response, final Class<C> clazz)
			throws JAXBException
	{
		final Unmarshaller unmarshaller = cmsJsonHttpMessageConverter.createUnmarshaller(clazz);

		final StreamSource source = new StreamSource(response.readEntity(InputStream.class));
		final C entity = unmarshaller.unmarshal(source, clazz).getValue();
		return entity;
	}

	/**
	 * Get the default implementation of the API Client
	 * @return apiClient instance
	 */
	public ApiClient getApiClientInstance() {
		return apiClient;
	}

	private AbstractApiClientInvocation getSpringApiClientInvocation()
	{
		final SpringApiClientInvocation apiClientInvocation = new SpringApiClientInvocation();
		apiClientInvocation.setTargetUrl(this.webResource.getUriBuilder().toTemplate());
		apiClientInvocation.setMessageConverters(Arrays.asList(cmsMappingJackson2HttpMessageConverter));
		return apiClientInvocation;
	}

	private AbstractApiClientInvocation getJaxRsApiClientInvocation() {
		final JaxRsApiClientInvocation apiClientInvocation = new JaxRsApiClientInvocation();
		apiClientInvocation.setWebResource(this.webResource);
		apiClientInvocation.setMessageConverter(this.cmsJsonHttpMessageConverter);
		return apiClientInvocation;
	}

	/**
	 * Assert that the response has the given HTTP status code
	 *
	 * @param response
	 *           - the response
	 * @param statusCode
	 *           - the expected status code
	 */
	protected void assertStatusCode(final Response response, final int statusCode)
	{
		assertThat(FAILURE_MESSAGE, response.getStatus(), is( statusCode ) );
	}

	/**
	 * Assert that the response status has the given HTTP status code
	 *
	 * @param response
	 *           - the response
	 * @param statusCode
	 *           - the expected status code
	 */
	protected void assertStatusCode(final de.hybris.platform.cmswebservices.util.apiclient.Response response, final int statusCode)
	{
		assertThat(FAILURE_MESSAGE, response.getStatus(), is( statusCode ) );
	}

	@Override
	public String getExtensionName() {
		return CmswebservicesConstants.EXTENSIONNAME;
	}

	@Override
	protected String getAuthorizationExtensionName()
	{
		return OAuth2Constants.EXTENSIONNAME;
	}

	/**
	 * Replace all placeholders in the URI with their respective values provided in the map of variables. When
	 * isDefaultCatalogVersion is enabled, the catalog and catalog version will be set to their default values.
	 *
	 * @param uri
	 *           - with placeholders to be replaced by values in the map of variables
	 * @param variables
	 *           - to replace placeholders in the URI
	 * @return URI where placeholders will be populated by the values contained in the map of variables
	 */
	protected String replaceUriVariables(final String uri, final Map<String, String> variables)
	{
		return new UriTemplate(uri).expand(variables).toASCIIString();
	}

	protected String replaceUriVariablesWithDefaultCatalog(final String uri, final Map<String, String> variables)
	{
		variables.put(CmswebservicesConstants.URI_CATALOG_ID, ID_APPLE.name());
		return replaceUriVariables(uri, variables);
	}

	protected String replaceUriVariablesWithDefaultCatalogVersion(final String uri, final Map<String, String> variables)
	{
		variables.put(CmswebservicesConstants.URI_VERSION_ID, STAGED.getVersion());
		return replaceUriVariables(uri, variables);
	}

	protected String replaceUriVariablesWithDefaultCatalogAndCatalogVersion(final String uri, final Map<String, String> variables)
	{
		variables.put(CmswebservicesConstants.URI_CATALOG_ID, ID_APPLE.name());
		variables.put(CmswebservicesConstants.URI_VERSION_ID, STAGED.getVersion());
		return replaceUriVariables(uri, variables);
	}

	protected String replaceUriVariablesWithElectronicsSiteAndDefaultCatalogAndCatalogVersion(final String uri, final Map<String, String> variables)
	{
		variables.put(CmswebservicesConstants.URI_SITE_ID, SiteModelMother.ELECTRONICS);
		variables.put(CmswebservicesConstants.URI_CATALOG_ID, ID_APPLE.name());
		variables.put(CmswebservicesConstants.URI_VERSION_ID, STAGED.getVersion());
		return replaceUriVariables(uri, variables);
	}

	protected String replaceUriVariablesWithDefaultSiteAndCatalogAndCatalogVersion(final String uri, final Map<String, String> variables)
	{
		variables.put(CmswebservicesConstants.URI_CATALOG_ID, ID_APPLE.name());
		variables.put(CmswebservicesConstants.URI_VERSION_ID, STAGED.getVersion());
		variables.put(CmswebservicesConstants.URI_SITE_ID, SiteModelMother.ELECTRONICS);
		return replaceUriVariables(uri, variables);
	}

	protected String replaceUriVariablesWithDefaultSite(final String uri, final Map<String, String> variables)
	{
		variables.put(CmswebservicesConstants.URI_SITE_ID, SiteModelMother.ELECTRONICS);
		return replaceUriVariables(uri, variables);
	}

	protected String getAuthorizationToken()
	{
		return authorizationToken;
	}

	public void setAuthorizationToken(final String authorizationToken)
	{
		this.authorizationToken = authorizationToken;
	}
}
