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
package de.hybris.platform.sap.core.odata.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.olingo.odata2.api.ODataCallback;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.ExpandSelectTreeNode;

/**
 * Establish HTTP Connection and read an ODataEntry or an ODataFeed. This is based on the Apache Olingo
 */
public class ODataClientService
{
	private static final String PROXY_HOST = "proxyhost";
	private static final String PROXY_PORT = "proxyport";
	private static final String SEPARATOR = "/";

	private static final String HTTP_METHOD_PUT = "PUT";
	private static final String HTTP_METHOD_POST = "POST";
	private static final String HTTP_METHOD_GET = "GET";
	private static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	private static final String HTTP_HEADER_ACCEPT = "Accept";
	private static final String COOKIE = "cookie";

	private static final String APPLICATION_XML = "application/xml";
	private int connectionTimeout;
	private int readTimeout;

	private static final Logger LOG = Logger.getLogger(ODataClientService.class.getName());
	private final Properties properties = new Properties();

	private HttpURLConnection initializeConnection(final String absoluteUri, final String contentType, final String httpMethod,
			final String user, final String password) throws IOException
	{
		HttpURLConnection connection = null;
		final URL url = new URL(absoluteUri + "?saml2=disabled");
		if (getProxyHost() != null)
		{
			final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(getProxyHost(), getProxyPort()));
			connection = (HttpURLConnection) url.openConnection(proxy);
		}
		else
		{
			connection = (HttpURLConnection) url.openConnection();
		}

		connection.setRequestProperty(HTTP_HEADER_ACCEPT, contentType);
		connection.setRequestMethod(httpMethod); // throws
		connection.setConnectTimeout(connectionTimeout);
		connection.setReadTimeout(readTimeout);
	
		if (HTTP_METHOD_POST.equals(httpMethod) || HTTP_METHOD_PUT.equals(httpMethod))
		{
			connection.setDoOutput(true);
			connection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, contentType);
		}

		if (user != null)
		{
			String authorization = "Basic ";
			authorization += new String(Base64.encodeBase64((user + ":" + password).getBytes()));
			connection.setRequestProperty("Authorization", authorization);
		}
		return connection;
	}

	/**
	 * @return PROXY_HOST
	 */
	protected String getProxyHost()
	{
		return properties.getProperty(PROXY_HOST);
	}

	/**
	 * @return PROXY_PORT
	 */
	protected int getProxyPort()
	{
		final String value = properties.getProperty(PROXY_PORT);
		return Integer.parseInt(value);
	}

	private InputStream execute(final String relativeUri, final String contentType, final String httpMethod, final String user,
			final String password) throws IOException
	{
		InputStream content = null;
		final HttpURLConnection connection = initializeConnection(relativeUri, contentType, httpMethod, user, password);

		connection.connect();

		final HttpStatusCodes httpStatusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());
		//			LOG.info("\n\n httpStatusCode : " + httpStatusCode + "\n\n");
		if (400 <= httpStatusCode.getStatusCode() && httpStatusCode.getStatusCode() <= 599)
		{
			final String msg = "Http Connection failed with status " + httpStatusCode.getStatusCode() + " "
					+ httpStatusCode.toString();
			throw new IOException(msg);
		}
		content = connection.getInputStream();

		return content;
	}

	/**
	 * Read a single entry (ODataEntry)
	 *
	 * @param serviceUri
	 * @param contentType
	 * @param entitySetName
	 * @param select
	 * @param filter
	 * @param expand
	 * @param keyValue
	 * @param user
	 * @param password
	 * @param client
	 * @return ODE
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 */

	public ODataEntry readEntry(final String serviceUri, final String contentType, final String entitySetName,
			final String select, final String filter, final String expand, final String keyValue, final String user,
			final String password, final String client) throws ODataException, URISyntaxException, IOException
	{

		final String absoluteUri = this.createUri(serviceUri, entitySetName, keyValue, expand, select, filter, null, client);

		InputStream content = null;
		ODataEntry oDE = null;
		try
		{
			final Edm edm = this.readEdm(serviceUri, user, password);
			final EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
			content = execute(absoluteUri, APPLICATION_XML, HTTP_METHOD_GET, user, password);
			oDE = EntityProvider.readEntry(contentType, entityContainer.getEntitySet(entitySetName), content,
					EntityProviderReadProperties.init().build());
		}
		catch (final MalformedURLException e)
		{
			LOG.error("HTTP Destination is not configured correctly", e);
			throw new ODataException("HTTP Destination is not configured correctly", e);
		}
		catch (final SocketTimeoutException e)
		{
			LOG.error("Connection to the backend system has timed-out. System not reachable.", e);
			throw new ODataException("HTTP Destination is not configured correctly", e);
		}
		finally
		{
   		if (content != null)
   		{
				try
				{
					content.close();
				}
				catch (final IOException e)
				{
					LOG.error("Error occurred while closing connection");
				}
   		}   		
		}
		return oDE;
	}

	/**
	 * Read a data feed (ODataFeed)
	 *
	 * @param serviceUri
	 * @param contentType
	 * @param entitySetName
	 * @param user
	 * @param password
	 * @param client
	 * @return feed
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public ODataFeed readFeed(final String serviceUri, final String contentType, final String entitySetName, final String user,
			final String password, final String client) throws ODataException, URISyntaxException, IOException
	{
		return readFeed(serviceUri, contentType, entitySetName, null, null, null, null, user, password, client);
	}

	/**
	 * Read a data feed (ODataFeed)
	 *
	 * @param serviceUri
	 * @param contentType
	 * @param entitySetName
	 * @param expand
	 * @param select
	 * @param filter
	 * @param orderby
	 * @param user
	 * @param password
	 * @param client
	 * @return oDF
	 * @throws ODataException
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public ODataFeed readFeed(final String serviceUri, final String contentType, final String entitySetName, final String expand,
			final String select, final String filter, final String orderby, final String user, final String password,
			final String client) throws ODataException, URISyntaxException, IOException
	{
		EdmEntityContainer entityContainer = null;
		InputStream content = null;
		ODataFeed oDF = null;
		try
		{
			final String absoluteUri = createUri(serviceUri, entitySetName, null, expand, select, filter, orderby, client);
			final Edm edm = this.readEdm(serviceUri, user, password);
			entityContainer = edm.getDefaultEntityContainer();
			content = execute(absoluteUri, contentType, HTTP_METHOD_GET, user, password);
			oDF = EntityProvider.readFeed(contentType, entityContainer.getEntitySet(entitySetName), content,
					EntityProviderReadProperties.init().build());

		}
		finally
		{
			if (content != null)
			{
				try
				{
					content.close();
				}
				catch (final IOException e)
				{
					LOG.error("Error occurred while closing connection");
				}
			}
		}
		return oDF;
	}

	private Edm readEdm(final String serviceUrl, final String user, final String password) throws ODataException, IOException
	{
		InputStream content = null;
		Edm edm = null;
		try
		{
			content = execute(serviceUrl + SEPARATOR + "$metadata", APPLICATION_XML, HTTP_METHOD_GET, user, password); // throw
			edm = EntityProvider.readMetadata(content, false); // throw
			// EntityProviderException
		}
		catch (final ODataException e)
		{
			if (content != null)
			{
				content.close();
			}
			throw new ODataException("HTTP Destination is not configured correctly", e);
		}
		
		if (content != null)
		{
			content.close();
		}
		return edm;
	}

	private String createUri(final String serviceUri, final String entitySetName, final String id, final String expand,
			final String select, final String filter, final String orderby, final String client) throws URISyntaxException
	{
		UriBuilder uriBuilder = null;

		if(StringUtils.isNotEmpty(serviceUri) & StringUtils.isNotEmpty(entitySetName))
		{
			if(StringUtils.isNotEmpty(id))
			{
				uriBuilder = UriBuilder.serviceUri(serviceUri, entitySetName, id);
			}
			else
			{
				uriBuilder = UriBuilder.serviceUri(serviceUri, entitySetName);
			}
			if(StringUtils.isNotEmpty(expand))
			{
				uriBuilder = uriBuilder.addQuery("$expand", expand);
			}
			if(StringUtils.isNotEmpty(select))
			{
				uriBuilder = uriBuilder.addQuery("$select", select);
			}
			if(StringUtils.isNotEmpty(filter))
			{
				uriBuilder = uriBuilder.addQuery("$filter", filter);
			}
			if(StringUtils.isNotEmpty(orderby))
			{
				uriBuilder = uriBuilder.addQuery("$orderby", orderby);
			}
			// if a sap client has been specified in the HTTP Destination URL in
			// backoffice
			if(StringUtils.isNotEmpty(client))
			{
				uriBuilder = uriBuilder.addQuery("sap-client", client);
			}
			
			uriBuilder = uriBuilder.addQuery("saml2", "disabled");
		}
		
		if(uriBuilder != null)
		{
			return new URI(null, uriBuilder.build(), null).toASCIIString();
		}
		else
		{
			return null;
		}
	}

	/**
	 * UriBuilder Class
	 *
	 */
	private static class UriBuilder
	{
		private final StringBuilder uri;
		private final StringBuilder query;

		private UriBuilder(final String serviceUri, final String entitySetName)
		{
			uri = new StringBuilder(serviceUri).append(SEPARATOR).append(entitySetName);
			query = new StringBuilder();
		}

		public static UriBuilder serviceUri(final String serviceUri, final String entitySetName)
		{
			return new UriBuilder(serviceUri, entitySetName);
		}

		public static UriBuilder serviceUri(final String serviceUri, final String entitySetName, final String id)
		{
			final UriBuilder b = new UriBuilder(serviceUri, entitySetName);
			return b.id(id);
		}

		private UriBuilder id(final String id)
		{
			if (id == null)
			{
				throw new IllegalArgumentException("Null is not an allowed id");
			}
			uri.append("(").append(id).append(")");
			return this;
		}

		public UriBuilder addQuery(final String queryParameter, final String value)
		{
			if (value != null)
			{
				if (query.length() == 0)
				{
					query.append("/?");
				}
				else
				{
					query.append("&");
				}
				query.append(queryParameter).append("=").append(value);
			}
			return this;
		}

		public String build()
		{
			return uri.toString() + query.toString();
		}
	}

	/**
	 * @param serviceUri
	 * @param entitySetName
	 * @param data
	 * @param contentType
	 * @param httpMethod
	 * @param user
	 * @param password
	 * @param client
	 * @param entities
	 * @param headerValues
	 * @param myCallback
	 * @return ODataEntry
	 * @throws EdmException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws EntityProviderException
	 * @throws URISyntaxException
	 */
	public ODataEntry writeEntity(final String serviceUri, final String entitySetName, final Map<String, Object> data,
			final String contentType, final String httpMethod, final String user, final String password, final String client,
			final List<String> entities, final Map<String, Object> headerValues, final MyCallback myCallback) throws EdmException,
			MalformedURLException,
			IOException, EntityProviderException, URISyntaxException
	{
		final String absoluteUri = this.createUri(serviceUri, entitySetName, null, null, null, null, null, client);
		String csrfToken = null;
		List<String> cookies = null;
		
		final EdmEntitySet entitySet = ((Edm) headerValues.get("edm")).getDefaultEntityContainer().getEntitySet(entitySetName);

		final URI serviceRoot = URI.create(serviceUri + "?saml2=disabled");

		final DataStore dataStore = new DataStore(data);
		final Map<String, ODataCallback> callbacks = new HashMap<String, ODataCallback>();
		myCallback.setDataStore(dataStore);
		myCallback.setServiceRoot(serviceRoot);
		final List<String> navigationPropertyNames = new ArrayList<String>();
		for (final String entity : entities)
		{
			callbacks.put(entity, myCallback);
			navigationPropertyNames.add(entity);
		}

		final ExpandSelectTreeNode expandSelectTreeNode = ExpandSelectTreeNode.entitySet(entitySet)
				.expandedLinks(navigationPropertyNames).selectedProperties(Collections.emptyList()).build();

		final EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(serviceRoot)
				.omitJsonWrapper(true).responsePayload(false).expandSelectTree(expandSelectTreeNode).callbacks(callbacks).build();

		// serialize data into ODataResponse object
		final ODataResponse response = EntityProvider.writeEntry(contentType, entitySet, data, properties);
		final ByteArrayOutputStream array = bufferStream((InputStream) response.getEntity());
		if (headerValues.containsKey("csrfToken") && headerValues.get("csrfToken") != null)
		{
			csrfToken = headerValues.get("csrfToken").toString();
		}
		
		if (headerValues.containsKey(COOKIE) && headerValues.get(COOKIE) != null)
      {
            cookies = (List<String>) headerValues.get(COOKIE);
      }

		final HttpURLConnection connection = initializeBatchConnection(absoluteUri, contentType, httpMethod, user, password,
				csrfToken, cookies);
		InputStream content = null;
		try
		{
			connection.getOutputStream().write(array.toByteArray());
   		final HttpStatusCodes statusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());
   		if (statusCode == HttpStatusCodes.CREATED)
   		{  
   			content = connection.getInputStream();
   			final ByteArrayOutputStream byteArrayOutputStream = bufferStream(content);
     			final InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
   			final ODataEntry entry = EntityProvider.readEntry(contentType, entitySet, inputStream, EntityProviderReadProperties
   					.init().build());
   			return entry;
   		}
		}
		catch(IOException e)
		{
			LOG.error("Error occurred while writing entity");
		}
		finally
		{
			if (connection != null)
			{
				connection.disconnect();
			}
			if (content != null)
			{
				try
				{
					content.close();
				}
				catch (IOException e)
				{
					LOG.error("Error occurred while closing connection");
				}
			}
		}
   	return null;
	}

	protected ByteArrayOutputStream bufferStream(final InputStream stream)
	{
		final byte[] buffer = new byte[16384];
		int bytesRead;
		try (final ByteArrayOutputStream output = new ByteArrayOutputStream())
		{
			while ((bytesRead = stream.read(buffer)) != -1)
			{
				output.write(buffer, 0, bytesRead);
			}
			return output;
		}
		catch(IOException e)
		{
			LOG.error("Error occurred while reading stream");
			return null;
		}
		
	}

	/**
	 * @param serviceUri
	 * @param contentType
	 * @param httpMethod
	 * @param user
	 * @param password
	 * @return headerValues
	 * @throws IOException 
	 */
	public Map<String, Object> getCSRFAndCookie(final String serviceUri, final String contentType, final String httpMethod,
			final String user, final String password)
	{

		final Map<String, Object> headerValues = new HashMap<String, Object>();
		String csrfToken = null;
		String cookie = null;
		List<String> cookies = null;
		Edm edm = null;
		HttpURLConnection connection = null;
		InputStream content = null;
		try
		{
			connection = initializeConnection(serviceUri + "/$metadata", contentType, httpMethod, user,
					password);
			connection.setRequestProperty("x-csrf-token", "fetch");
			connection.connect();
			content = connection.getInputStream();

			/**
			 * Read EDM
			 */
			try
			{
				edm = EntityProvider.readMetadata(content, false);
			}
			catch (final EntityProviderException e)
			{
				LOG.error("Error occurred while reading metadata");
			}

			if (HttpURLConnection.HTTP_OK == connection.getResponseCode())
			{
				csrfToken = connection.getHeaderField("x-csrf-token");
				cookies = connection.getHeaderFields().get("Set-Cookie");
				
				if (cookies == null)
				{
					cookie = connection.getHeaderField("Set-Cookie");
					if (StringUtils.isNotEmpty(cookie))
					{
						cookies = new ArrayList<String>();
						cookies.add(cookie);
					}
				}
				else
				{
					if (cookies.size() == 0)
					{
						LOG.warn("No cookies retrieved while reading metadata");
					}
				}		
			}
			connection.disconnect();
		}
		catch (final IOException e)
		{
			LOG.error("Error occurred while getting CSRF token and cookie", e);
		}
		finally
		{
			if (connection != null)
			{
				connection.disconnect();
			}
			if (content != null)
			{
				try
				{
					content.close();
				}
				catch (IOException e)
				{
					LOG.error("Error occurred while closing connection");
				}
			}
		}
		headerValues.put("csrfToken", csrfToken);
		headerValues.put(COOKIE, cookies);
		headerValues.put("edm", edm);
		return headerValues;
	}

	private HttpsURLConnection initializeBatchConnection(final String absoluteUri, final String contentType,
			final String httpMethod, final String user, final String password, final String csrfToken, final List<String> cookies) //final String cookie)
			throws IOException
	{
		final URL url = new URL(absoluteUri);
		HttpsURLConnection connection;
		if (getProxyHost() != null)
		{
			final Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(getProxyHost(), getProxyPort()));
			connection = (HttpsURLConnection) url.openConnection(proxy);
		}
		else
		{
			connection = (HttpsURLConnection) url.openConnection();
		}
		if (connection != null)
		{
			connection.setRequestProperty(HTTP_HEADER_ACCEPT, contentType);
			connection.setRequestProperty("x-requested-with", "xmlhttprequest");
			connection.setRequestMethod(httpMethod);
			connection.setConnectTimeout(connectionTimeout);
			connection.setReadTimeout(readTimeout);

			if (httpMethod.equals(HTTP_METHOD_POST) || httpMethod.equals(HTTP_METHOD_PUT))
			{
				connection.setDoOutput(true);
				connection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, contentType);
			}

			connection.setRequestProperty("x-csrf-token", csrfToken);
			for (String ncookie : cookies) 
         {
             connection.addRequestProperty("Cookie", ncookie.split(";", 2)[0]);
         }
		}
		return connection;
	}

	/**
	 * @return connectionTimeout
	 */
	public int getConnectionTimeout()
	{
		return connectionTimeout;
	}

	/**
	 * @param connectionTimeout
	 */
	public void setConnectionTimeout(final int connectionTimeout)
	{
		this.connectionTimeout = connectionTimeout;
	}

	/**
	 * @return readTimeout
	 */
	public int getReadTimeout()
	{
		return readTimeout;
	}

	/**
	 * @param readTimeout
	 */
	public void setReadTimeout(final int readTimeout)
	{
		this.readTimeout = readTimeout;
	}

}
