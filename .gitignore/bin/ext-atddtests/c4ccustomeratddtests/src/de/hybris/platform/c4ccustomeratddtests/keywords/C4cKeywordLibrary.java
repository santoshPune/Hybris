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
 *
 */
package de.hybris.platform.c4ccustomeratddtests.keywords;

import de.hybris.deltadetection.jalo.DeltadetectionManager;
import de.hybris.platform.atddengine.keywords.AbstractKeywordLibrary;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.core.systemsetup.datacreator.impl.EncodingsDataCreator;
import de.hybris.platform.cronjob.model.CronJobModel;
import de.hybris.platform.embeddedserver.api.EmbeddedServer;
import de.hybris.platform.embeddedserver.api.EmbeddedServerBuilder;
import de.hybris.platform.embeddedserver.base.EmbeddedExtension;
import de.hybris.platform.impex.model.cronjob.ImpExExportCronJobModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.cronjob.CronJobService;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.AddressService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.Utilities;
import de.hybris.y2ysync.jalo.Y2ysyncManager;
import de.hybris.y2ysync.model.Y2YSyncJobModel;
import de.hybris.y2ysync.services.SyncExecutionService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.hybris.datahub.core.rest.DataHubCommunicationException;
import com.hybris.datahub.core.rest.DataHubOutboundException;


/**
 * ATDD keyword library.
 */
@Configurable
public class C4cKeywordLibrary extends AbstractKeywordLibrary
{
	private static final Logger LOG = LoggerFactory.getLogger(C4cKeywordLibrary.class);

	public static final String Y2Y_SYNC_HOME_URL_KEY = "y2ysync.home.url";
	public static final String Y2Y_SYNC_DATAHUB_USERNAME_KEY = "datahubadapter.datahuboutbound.user";
	public static final String Y2Y_SYNC_DATAHUB_PASSWORD_KEY = "datahubadapter.datahuboutbound.password";

	// There would be only one instance of this keyword library for all tests
	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";

	private EmbeddedServer embeddedServer;

	@Value("${c4ccustomeratddtests.tomcat.port}")
	private int embeddedServerPort;

	@Value("${c4ccustomeratddtests.jetty.port}")
	private int datahubServerPort;

	@Value("${c4ccustomeratddtests.datahub.url}")
	private String y2ysyncDatahubUrl;

	@Value("${c4ccustomeratddtests.platform.url}")
	private String y2ysyncHomeUrl;

	@Value("${c4ccustomeratddtests.datahubadapter.datahuboutbound.user}")
	private String y2ysyncDatahubUsername;

	@Value("${c4ccustomeratddtests.datahubadapter.datahuboutbound.password}")
	private String y2ysyncDatahubPassword;

	@Autowired
	private AddressService addressService;

	@Autowired
	private ModelService modelService;

	@Autowired
	private UserService userService;

	@Autowired
	private CronJobService cronJobService;

	@Autowired
	private SyncExecutionService syncExecutionService;

	@Autowired
	private FlexibleSearchService flexibleSearchService;

	@Autowired
	private EmbeddedServerBuilder tomcatEmbeddedServerBuilder;

	@Autowired
	private CommonI18NService commonI18NService;

	@Value("${c4ccustomeratddtests.c4c.payload.filename}")
	private String fileName;
	@Value("${c4ccustomeratddtests.c4c.url.filename}")
	private String urlFileName;

	private final Transformer transformer;

	public C4cKeywordLibrary() throws TransformerConfigurationException
	{
		transformer = TransformerFactory.newInstance().newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "3");
	}

	/**
	 * Creates address for customer with uid.
	 *
	 * @param uid
	 *           user's uid
	 * @throws DataHubOutboundException
	 * @throws DataHubCommunicationException
	 * @return address created
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public AddressModel createAddressForCustomer(final String uid) throws DataHubOutboundException, DataHubCommunicationException
	{
		final UserModel user = userService.getUserForUID(uid);
		final AddressModel address = addressService.createAddressForUser(user);
		modelService.saveAll();
		return address;
	}

	/**
	 *
	 * @param model
	 *           model to remove
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public void deleteItem(final ItemModel model)
	{
		modelService.remove(model);
	}

	/**
	 * Convert String country code to Country ISO code.
	 *
	 * @param country
	 *           ISO code
	 * @return ISOcode converted
	 */
	public CountryModel convertToISOcode(final String country)
	{
		return StringUtils.isEmpty(country) ? null : commonI18NService.getCountry(country);
	}

	/**
	 * Save changes made in model.
	 *
	 * @param model
	 *           model to save
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public void updateDbItem(final ItemModel model)
	{
		modelService.save(model);
	}

	/**
	 * Get customer by its uid.
	 *
	 * @param uid
	 * @return The user model.
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public UserModel getCustomerByUid(final String uid)
	{
		return userService.getUserForUID(uid);
	}

	/**
	 * creates customer.
	 *
	 * @param uid
	 *           user uid
	 * @param name
	 *           user name
	 * @throws DataHubOutboundException
	 * @throws DataHubCommunicationException
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public CustomerModel createCustomer(final String uid, final String name)
			throws DataHubOutboundException, DataHubCommunicationException
	{
		final CustomerModel customer = new CustomerModel();
		customer.setUid(uid);
		customer.setName(name);
		customer.setCustomerID(uid);
		modelService.save(customer);
		return customer;
	}

	/**
	 * Change name of existing customer.
	 *
	 * @param uid
	 *           customer id
	 * @param name
	 *           new name
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public void setCustomerName(final String uid, final String name)
	{
		final UserModel customerModel = getCustomerByUid(uid);
		customerModel.setName(name);
		modelService.save(customerModel);
	}

	/**
	 * Sets default shipment address for a given customer.
	 *
	 * @param uid
	 *           user uid
	 * @param address
	 *           address to be default shipment address
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public void setDefaultShipmentAddressForCustomer(final AddressModel address, final String uid)
	{
		final UserModel customerModel = getCustomerByUid(uid);
		customerModel.setDefaultShipmentAddress(address);
		modelService.save(customerModel);
	}

	/**
	 * Sets default billing address for a given customer.
	 *
	 * @param uid
	 *           user uid
	 * @param address
	 *           address to be default shipment address
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public void setDefaultBillingAddressForCustomer(final AddressModel address, final String uid)
	{

		final UserModel customerModel = getCustomerByUid(uid);
		customerModel.setDefaultPaymentAddress(address);
		modelService.save(customerModel);
	}

	@SuppressWarnings("boxing")
	protected void applyDelay(final String msg, final int millisecs)
	{
		try
		{
			LOG.debug(String.format(msg, millisecs));
			Thread.sleep(millisecs);
		}
		catch (final InterruptedException e)
		{
			// Not Important.
		}
	}

	/**
	 * @return Content of XML SOAP request to C4C.
	 * @throws IOException
	 */
	public String getURL() throws IOException
	{
		final File file = getUrlFile();
		LOG.info("Reading URL File: " + file.getAbsolutePath());
		return IOUtils.toString(new FileInputStream(file), "UTF-8");
	}

	/**
	 * @return Content of XML SOAP request to C4C.
	 * @throws IOException
	 */
	public String readData() throws IOException
	{
		final File file = getPayloadFile();
		LOG.info("Reading PayloadFile: " + file.getAbsolutePath());
		return IOUtils.toString(new FileInputStream(file), "UTF-8");
	}

	/**
	 * Waits until payload file appears or the timeout expires, whichever happens first.
	 *
	 * @throws DataHubCommunicationException
	 *            if the wait timed out
	 */
	public void waitForDataProcessed() throws DataHubCommunicationException
	{
		final File file = getPayloadFile();
		LOG.info("Waiting for file to appear: " + file.getAbsolutePath());
		for (int i = 0; i < 60; i++)
		{
			if (file.exists())
			{
				return;
			}
			applyDelay("Waiting %d millisecs before next check for payload.xml file.", 3000);
		}
		throw new DataHubCommunicationException("C4C data is not available");
	}

	/**
	 * Remove payload.xml file.
	 */
	public void removeData()
	{
		getPayloadFile().delete();
		getUrlFile().delete();
	}

	/**
	 * Set atdd properties.
	 */
	public void setAtddProperties()
	{
		Config.setParameter(Y2Y_SYNC_HOME_URL_KEY, y2ysyncHomeUrl);
		Config.setParameter(Y2Y_SYNC_DATAHUB_USERNAME_KEY, y2ysyncDatahubUsername);
		Config.setParameter(Y2Y_SYNC_DATAHUB_PASSWORD_KEY, y2ysyncDatahubPassword);
	}

	/**
	 * Set platform properties.
	 */
	public void setPlatformProperties()
	{
		Config.setParameter(Y2Y_SYNC_HOME_URL_KEY, y2ysyncHomeUrl);
		Config.setParameter(Y2Y_SYNC_DATAHUB_USERNAME_KEY, y2ysyncDatahubUsername);
		Config.setParameter(Y2Y_SYNC_DATAHUB_PASSWORD_KEY, y2ysyncDatahubPassword);
	}

	/**
	 * Prepare environment.
	 *
	 * @throws Exception
	 */
	public void prepareEnvironment() throws Exception
	{
		ensureEmbeddedServerIsRunning();

		new EncodingsDataCreator().populateDatabase();
		ServicelayerTest.createCoreData();
		final Map<String, String> map = Collections.emptyMap();
		DeltadetectionManager.getInstance().createEssentialData(map, null);
		Y2ysyncManager.getInstance().createEssentialData(map, null);

		// Synchronization cron jobs work in parallel, and each one creates own parent ImpEx-Export job
		// instead of having shared parent.
		// To prevent that, I create the parent in advance:
		final ImpExExportCronJobModel cronJob = modelService.create("ImpExExportCronJob");
		LOG.info("cronJob created [" + cronJob.getCode() + "]");
	}

	/**
	 * Extract value from XML by XPath.
	 *
	 * @param xml
	 *           the xml
	 * @param xPath
	 *           the xpath
	 * @return The result from the evaluation.
	 */
	public String extractDataFromXmlByXPath(final String xml, final String xPath)
			throws ParserConfigurationException, IOException, SAXException, XPathExpressionException
	{
		LOG.info("xPath:\n" + xPath + "\n");
		LOG.info("XML:\n" + prettyFormat(xml) + "\n");
		String result = "";
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		final DocumentBuilder builder = factory.newDocumentBuilder();
		final Document doc = builder.parse(new InputSource(new StringReader(xml)));

		// Create XPathFactory object
		final XPathFactory xpathFactory = XPathFactory.newInstance();

		// Create XPath object
		final XPath xpath = xpathFactory.newXPath();

		final XPathExpression expr = xpath.compile(xPath);

		result = (String) expr.evaluate(doc, XPathConstants.STRING);
		LOG.info("result: " + result);
		return result;
	}

	private String prettyFormat(final String input)
	{
		final StringWriter stringWriter = new StringWriter();
		final StreamResult xmlOutput = new StreamResult(stringWriter);
		StreamSource xmlInput = null;
		try
		{
			xmlInput = new StreamSource(new StringReader(input));
			transformer.transform(xmlInput, xmlOutput);
			return xmlOutput.getWriter().toString();
		}
		catch (final Exception e)
		{
			throw new RuntimeException(e);
		}
		finally
		{
			IOUtils.closeQuietly(stringWriter);
			IOUtils.closeQuietly(xmlOutput.getOutputStream());
			IOUtils.closeQuietly(xmlInput.getInputStream());
		}
	}

	/**
	 * Triggers CronJob.
	 *
	 * @throws InterruptedException
	 *            if thread was terminated
	 * @throws IOException
	 *            If there are problems when publishing information manually
	 * @throws IllegalStateException
	 *            when cron job was terminated by timeout
	 */
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public void triggerCronJob(final String jobId, final boolean expectToStart) throws InterruptedException, IOException
	{
		// Before triggering the cronJob, we give deltadetection the chance to compute properly
		// possible changes.
		applyDelay("Giving deltadetection the chance to compute properly possible changes for %d millisecs", 1000);
		final Y2YSyncJobModel job = getJob(jobId);
		final CronJobModel cronJob = syncExecutionService.startSync(job, SyncExecutionService.ExecutionMode.SYNC);
		pollForFinishedCronJob(cronJob, expectToStart);
	}

	private Y2YSyncJobModel getJob(final String code)
	{
		final FlexibleSearchQuery fQuery = new FlexibleSearchQuery("SELECT {PK} FROM {Y2YSyncJob} WHERE {code}=?code");
		fQuery.addQueryParameter("code", code);
		return flexibleSearchService.searchUnique(fQuery);
	}

	private void pollForFinishedCronJob(final CronJobModel cronJob, final boolean expectToStart)
	{
		final DateTime start = new DateTime().plusSeconds(120);
		while (new DateTime().isBefore(start))
		{
			applyDelay("Waiting %d millisecs before next cronJob check.", 1000);
			modelService.refresh(cronJob);
			if (!cronJobService.isRunning(cronJob))
			{
				if (!cronJobService.isFinished(cronJob))
				{
					throw new IllegalStateException(
							"Synchronization job failed to complete. Job status is " + cronJob.getStatus().getCode());
				}
				return;
			}
		}

		if (expectToStart)
		{
			throw new IllegalStateException(
					"Synchronization cron job was terminated. Job status after 2 min is " + cronJob.getStatus().getCode());
		}
	}

	private void ensureEmbeddedServerIsRunning()
	{
		if (embeddedServer == null)
		{
			final EmbeddedServerBuilder builder = getEmbeddedServerBuilder();
			embeddedServer = builder.needEmbeddedServer().runningOnPort(embeddedServerPort)
					.withApplication(new EmbeddedExtension(Utilities.getExtensionInfo("mediaweb")).withContext("/medias"))
					.withApplication(new EmbeddedExtension(Utilities.getExtensionInfo("y2ysync")).withContext("/y2ysync_junit"))
					.build();
			embeddedServer.start();
		}
	}

	private EmbeddedServerBuilder getEmbeddedServerBuilder()
	{
		return tomcatEmbeddedServerBuilder;
	}

	/**
	 * Generates td.
	 *
	 * @return Generated td
	 */
	public String generateId()
	{
		return UUID.randomUUID().toString();
	}

	protected File getPayloadFile()
	{
		return new File(System.getProperty("java.io.tmpdir"), fileName);
	}

	protected File getUrlFile()
	{
		return new File(System.getProperty("java.io.tmpdir"), urlFileName);
	}

	public String getDatahubServerPort()
	{
		return Integer.toString(datahubServerPort);
	}
}
