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
package de.hybris.platform.generated.c4ccustomeratddtests;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.Registry;
import de.hybris.platform.atddengine.framework.RobotSettings;
import de.hybris.platform.atddengine.framework.RobotTest;
import de.hybris.platform.atddengine.framework.RobotTestResult;
import de.hybris.platform.atddengine.framework.RobotTestSuite;
import de.hybris.platform.atddengine.framework.RobotTestSuiteFactory;
import de.hybris.platform.atddengine.framework.impl.DefaultPythonProvider;
import de.hybris.platform.atddengine.framework.impl.PythonAware;
import de.hybris.platform.atddengine.framework.impl.PythonRobotTestSuiteFactory;
import de.hybris.platform.atddengine.keywords.ImpExAdaptorAware;
import de.hybris.platform.atddengine.keywords.KeywordLibraryContext;
import de.hybris.platform.atddengine.keywords.KeywordLibraryContextHolder;
import de.hybris.platform.atddengine.keywords.RobotTestContext;
import de.hybris.platform.atddengine.keywords.RobotTestContextAware;
import de.hybris.platform.atddengine.keywords.impl.DefaultImpExAdaptor;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.util.Config;

import java.io.File;
import java.io.IOException;

import javax.annotation.Resource;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;


/*
 * This is a generated class. Do not edit, changes will be overriden.
 */
@SuppressWarnings("PMD")
@IntegrationTest
public class C4cSOAPMessageTest extends ServicelayerTest implements RobotTestContext
{
	public static RobotTestSuite robotTestSuite;
	
	private ModelService modelService;

	public static void startSuite() throws IOException
	{
		if (robotTestSuite == null)
		{
			final PythonAware pythonAware = new DefaultPythonProvider(Config.getParameter("atddengine.libraries-path"));

			final RobotSettings robotSettings = new RobotSettings();
			
			robotSettings.setOutputDir(new File(Config.getParameter("atddengine.report-path"), "c4ccustomeratddtests"));
			robotSettings.setLogName("C4cSOAPMessageTest-log");
			robotSettings.setOutputName("C4cSOAPMessageTest-output");
			robotSettings.setReportName("C4cSOAPMessageTest-report");
			
			final RobotTestSuiteFactory robotTestSuiteFactory = new PythonRobotTestSuiteFactory(pythonAware);

			robotTestSuite = robotTestSuiteFactory.parseTestSuite(robotSettings, new File(
					"/opt/bambooagent/xml-data/build-dir/MAINTNG-TD61076-PCS/maintenance/bin/ext-atddtests/c4ccustomeratddtests/genresources/robottests/C4cSOAPMessageTest.txt"));
		}

		if (!robotTestSuite.isStarted())
		{
			robotTestSuite.start();
		}

	}

	@AfterClass
	public static void tearDownSuite()
	{
		robotTestSuite.close();
	}

	private RobotTest currentRobotTest;

	@Resource(name = "impExAdaptorHolder")
	private ImpExAdaptorAware impExAdaptorHolder;

	@Resource(name = "keywordLibraryContext")
	private KeywordLibraryContext keywordLibraryContext;

	@Resource(name = "robotTestContextHolder")
	private RobotTestContextAware robotTestContextHolder;

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.atddengine.keywords.RobotTestContext#getCurrentRobotTest()
	 */
	@Override
	public RobotTest getCurrentRobotTest()
	{
		return currentRobotTest;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.hybris.platform.atddengine.keywords.RobotTestContext#getProjectName()
	 */
	@Override
	public String getProjectName()
	{
		return "c4ccustomeratddtests";
	}

	@Before
	public void setUp() throws IOException
	{
		impExAdaptorHolder.setImpExAdaptor(new DefaultImpExAdaptor());
		
		robotTestContextHolder.setRobotTestContext(this);

		KeywordLibraryContextHolder.setKeywordLibraryContext(keywordLibraryContext);
		
		modelService = (ModelService)Registry.getApplicationContext().getBean("modelService");

		startSuite();
	}
	
	@Test
	public void C4CIntegration_Test_Verify_senderParty()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_Verify_senderParty";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_recipientParty()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_recipientParty";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_message_UUID()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_message_UUID";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_CustomerId()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_CustomerId";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_uId()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_uId";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_GivenName_and_FamilyName()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_GivenName_and_FamilyName";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_GivenName()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_GivenName";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_postalCode()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_postalCode";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_streetName()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_streetName";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_streetNumber()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_streetNumber";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_pobox()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_pobox";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_district()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_district";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_town()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_town";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_country()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_country";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_phone1()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_phone1";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_phone2()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_phone2";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_fax()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_fax";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_cellphone()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_cellphone";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_roleCode()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_roleCode";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_formOfAddress()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_formOfAddress";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_gender()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_gender";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_CanSetEmptyCustomerName()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_CanSetEmptyCustomerName";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_CanSetEmptyStreetName()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_CanSetEmptyStreetName";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_Check_Default_Shipment_Address_Is_Null()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_Check_Default_Shipment_Address_Is_Null";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_Check_Default_Shipment_Address()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_Check_Default_Shipment_Address";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_Check_Default_Billing_Address_Is_Null()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_Check_Default_Billing_Address_Is_Null";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_Check_Default_Billing_Address()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_Check_Default_Billing_Address";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_Check_Default_Address_Is_Null()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_Check_Default_Address_Is_Null";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_Check_Default_Address()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_Check_Default_Address";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_Check_Shipment_And_Payment_Address()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_Check_Shipment_And_Payment_Address";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_Check_Two_Addresses()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_Check_Two_Addresses";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_Reset_Shipment_Address()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_Reset_Shipment_Address";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_Check_Reset_Payment_Address()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_Check_Reset_Payment_Address";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Mapping_Rules_For_Phone1()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Mapping_Rules_For_Phone1";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Mapping_Rules_For_Phone2()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Mapping_Rules_For_Phone2";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Mapping_Rules_For_Fax()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Mapping_Rules_For_Fax";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Mapping_Rules_For_CellPhone()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Mapping_Rules_For_CellPhone";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_EMail_in_2_addresses()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_EMail_in_2_addresses";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Email_in_Customer_Raw()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Email_in_Customer_Raw";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Email_in_Customer_Raw_and_in_Address_Raw()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Email_in_Customer_Raw_and_in_Address_Raw";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Synchronization_Sending_Only_Delta_Customer()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Synchronization_Sending_Only_Delta_Customer";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Synchronization_Not_Sending_The_Only_Removed_Address()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Synchronization_Not_Sending_The_Only_Removed_Address";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Synchronization_Not_Sending_One_of_Removed_Address()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Synchronization_Not_Sending_One_of_Removed_Address";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Synchronization_Sending_Only_Delta_Customer_with_Changed_Customer_Attributes2()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Synchronization_Sending_Only_Delta_Customer_with_Changed_Customer_Attributes2";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Synchronization_Sending_Only_Delta_Customer_with_Address()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Synchronization_Sending_Only_Delta_Customer_with_Address";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Synchronization_Sending_Only_Delta_Customer_with_Changed_Customer_Attributes()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Synchronization_Sending_Only_Delta_Customer_with_Changed_Customer_Attributes";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Existed_Addresses_go_to_Correct_Updated_Customers()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Existed_Addresses_go_to_Correct_Updated_Customers";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_New_Addresses_go_to_Correct_Existed_Customers()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_New_Addresses_go_to_Correct_Existed_Customers";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_New_Addresses_go_to_Correct_Updated_Customers_Without_Adresses()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_New_Addresses_go_to_Correct_Updated_Customers_Without_Adresses";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Changed_Addresses_go_to_Correct_Existed_Customers()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Changed_Addresses_go_to_Correct_Existed_Customers";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Verify_Deleted_Addresses_go_to_Correct_Existed_Customers()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Verify_Deleted_Addresses_go_to_Correct_Existed_Customers";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_TestAddressCreation()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_TestAddressCreation";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_TestAddressModification()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_TestAddressModification";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_TestAddressRemoval()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_TestAddressRemoval";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_OneAddress_EmailIsNotUid()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_OneAddress_EmailIsNotUid";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_OneAddress_EmailIsUid()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_OneAddress_EmailIsUid";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_OneAddress_MixedAsUid()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_OneAddress_MixedAsUid";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_OneAddress_Empty()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_OneAddress_Empty";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_TwoAddresses_EmailAsUidAndNotAsUid()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_TwoAddresses_EmailAsUidAndNotAsUid";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_TwoAddresses_EmailNotAsUid()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_TwoAddresses_EmailNotAsUid";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_TwoAddresses_EmailAsUidAndPostal()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_TwoAddresses_EmailAsUidAndPostal";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_TwoAddresses_EmailNotAsUidAndPostal()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_TwoAddresses_EmailNotAsUidAndPostal";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_TwoAddresses_2Postal()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_TwoAddresses_2Postal";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_TwoAddresses_PostalAndMixedNotAsUid()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_TwoAddresses_PostalAndMixedNotAsUid";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_TwoAddresses_EmailAsUidAndMixedNotAsUid()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_TwoAddresses_EmailAsUidAndMixedNotAsUid";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_TwoAddresses_EmailNotAsUidAndMixedNotAsUid()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_TwoAddresses_EmailNotAsUidAndMixedNotAsUid";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_TwoAddresses_2MixedAsUid()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_TwoAddresses_2MixedAsUid";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_TwoAddresses_MixedNotAsUidAndAsUid()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_TwoAddresses_MixedNotAsUidAndAsUid";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	
	@Test
	public void C4CIntegration_Test_TwoAddresses_EmailNotAsUidAndMixedAsUid()
	{
		modelService.detachAll();
		
		final String robotTestName = "C4CIntegration_Test_TwoAddresses_EmailNotAsUidAndMixedAsUid";
		
		final RobotTest robotTest = robotTestSuite.getRobotTest(robotTestName);
		
		currentRobotTest = robotTestSuite.getRobotTest(robotTestName);
		assertNotNull("Robot test: '" + robotTestName + "' not found", currentRobotTest);

		final RobotTestResult robotTestResult = robotTest.run();
		assertTrue(robotTestResult.getMessage(), robotTestResult.isSuccess());
	}
	

}
