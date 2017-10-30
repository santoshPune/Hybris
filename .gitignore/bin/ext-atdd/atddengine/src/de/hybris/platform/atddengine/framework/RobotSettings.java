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
package de.hybris.platform.atddengine.framework;

import java.io.File;


public class RobotSettings
{
	private String logName;

	private File outputDir;

	private String outputName;

	private String reportName;

	public String getLogName()
	{
		return logName;
	}

	public File getOutputDir()
	{
		return outputDir;
	}

	public String getOutputName()
	{
		return outputName;
	}

	public String getReportName()
	{
		return reportName;
	}

	public void setLogName(final String logName)
	{
		this.logName = logName;
	}

	public void setOutputDir(final File outputDir)
	{
		this.outputDir = outputDir;
	}

	public void setOutputName(final String outputName)
	{
		this.outputName = outputName;
	}

	public void setReportName(final String reportName)
	{
		this.reportName = reportName;
	}
}
