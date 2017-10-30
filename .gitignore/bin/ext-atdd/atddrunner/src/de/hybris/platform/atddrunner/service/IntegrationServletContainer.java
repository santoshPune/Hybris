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
package de.hybris.platform.atddrunner.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.Lifecycle;


/**
 * Configurable embedded servlet container.
 */
public class IntegrationServletContainer implements Lifecycle
{
	private static final Logger LOG = LoggerFactory.getLogger(IntegrationServletContainer.class);

	private String connectionPath;
	private String warPath;
	private Properties properties;
	private String logFilePath = "../../log/atddrunner.log";
	private String heartbeat = "";
	private String jettyConfigPath;
	private String libPath;
	private String webappRunnerJarFile = "jetty-runner.jar";
	private Integer port = 8080;
	private final List<ServletDefinition> servlets = new ArrayList<>();


	private volatile Process serverProcess;

	/**
	 * Start servlet container with {@code warPath} deployed.
	 */
	@Override
	public void start()
	{
		// TODO make thread-safe
		LOG.trace("Request to start application");
		if (serverProcess == null)
		{
			try
			{
				createServer();
				LOG.info("Application has started");
			}
			catch (final Exception e)
			{
				LOG.error("Error starting Web Server", e);
				throw new RuntimeException("Error staring application " + warPath, e);
			}
		}
		else
		{
			LOG.warn("Application has already started on port {}", port);
		}
	}

	/**
	 * Stop servlet container.
	 */
	@Override
	public void stop()
	{
		LOG.trace("Request to stop application");
		if (serverProcess != null)
		{
			serverProcess.destroy();
			serverProcess = null;
			LOG.info("Application was stopped");
		}
		else
		{
			LOG.warn("Application has not been started yet");
		}
	}

	@Override
	public boolean isRunning()
	{
		return serverProcess != null;
	}

	protected void createServer() throws Exception
	{
		final String pathToJava = getJavaPath();
		final String pathToRunner = new File(this.getClass().getClassLoader().getResource("bin/" + webappRunnerJarFile).getFile())
				.getAbsolutePath();
		final List<String> props = buildProperties();

		LOG.info("Initializing Web Server\n\tport = {}\n\tlogging to {}", port, new File(logFilePath).getAbsolutePath());

		final List<String> arguments = new ArrayList<>();
		arguments.add(pathToJava);
		arguments.addAll(props);
		arguments.add("-jar");
		arguments.add(pathToRunner);
		arguments.add("--port");
		arguments.add(String.valueOf(port));
		if (libPath != null)
		{
			final String libAbsolutePath = getAbsolutePath(libPath);
			arguments.add("--lib");
			arguments.add(libAbsolutePath);
			LOG.info("jetty libs are in {}", libAbsolutePath);

		}
		if (jettyConfigPath != null)
		{
			final String configAbsolutePath = getAbsolutePath(jettyConfigPath);
			arguments.add("--config");
			arguments.add(configAbsolutePath);
			LOG.info("jetty config is {}", configAbsolutePath);
		}
		if (warPath != null)
		{
			final ServletDefinition servletDefinition = new ServletDefinition();
			servletDefinition.setServletPath(warPath);
			if (connectionPath != null)
			{
				servletDefinition.setContextPath(connectionPath);
			}
			servletDefinition.setHeartbeat(heartbeat);
			servlets.add(servletDefinition);
		}

		for (final ServletDefinition servletDefinition : servlets)
		{

			if (servletDefinition.getContextPath() != null)
			{
				arguments.add("--path");
				final URI connectionURI = URI.create(servletDefinition.getContextPath());
				arguments.add(connectionURI.getPath());
			}
			final String pathToWar = getAbsolutePath(servletDefinition.getServletPath());
			arguments.add(pathToWar);
			LOG.info("Servlet \n\twar = {}, \n\tpath = {}", pathToWar, servletDefinition.getContextPath());
		}

		final ProcessBuilder processBuilder = new ProcessBuilder(arguments);
		final File output = new File(logFilePath).getAbsoluteFile();
		final File outputDir = output.getParentFile();
		if (!outputDir.exists())
		{
			outputDir.mkdirs();
		}
		processBuilder.redirectOutput(new File(logFilePath));
		processBuilder.redirectErrorStream(true);
		serverProcess = processBuilder.start();

		waitForServiceToStart();
	}

	private void waitForServiceToStart() throws IOException, InterruptedException
	{
		try
		{
			CompletableFuture.allOf(servlets.stream().map(this::createServletChecker).toArray(CompletableFuture[]::new)).join();
		}
		catch (final CompletionException e)
		{
			stop();
			throw new IllegalStateException("Error starting one of servlets", e.getCause());
		}
	}

	private CompletableFuture createServletChecker(final ServletDefinition servletDefinition)
	{
		return CompletableFuture.supplyAsync(() ->
		{
			final HttpClient client = new HttpClient();
			final HostConfiguration hostConfig = new HostConfiguration();
			final URI connectionURI = URI.create(connectionPath != null ? connectionPath : "http://localhost:" + port);
			hostConfig.setHost(connectionURI.getHost(), port, "http");
			client.setHostConfiguration(hostConfig);
			final HttpClientParams params = new HttpClientParams();
			params.setSoTimeout(3000);
			client.setParams(params);
			final GetMethod method = new GetMethod(servletDefinition.getHeartbeat());
			int countdown = 40;
			while (countdown-- > 0)
			{
				try
				{
					Thread.sleep(3000);
					final int code = client.executeMethod(hostConfig, method);
					LOG.info("Executed request " + method.getPath() + " with result " + code);
					return code;
				}
				catch (final HttpException hx)
				{
					LOG.info("Application responded with ", hx);
					break;
				}
				catch (final IOException ex)
				{
					// It's OK: application is still starting
				}
				catch (final InterruptedException ex)
				{
					LOG.info("Thread was interrupted");
					return -1;
				}
			}
			throw new IllegalStateException("Timeout exceeded while waiting for one of servlets startup");
		});
	}

	protected List<String> buildProperties()
	{
		return properties.keySet().stream().map(it -> "-D" + it + '=' + properties.getProperty(it.toString()))
				.collect(Collectors.toList());
	}

	protected String getJavaPath()
	{
		final String separator = System.getProperty("file.separator");
		return System.getProperty("java.home") + separator + "bin" + separator + "java";
	}

	/**
	 * The method constructs absolute path. Default implementation takes {@code path} as a path, relative to location of
	 * the {@code IntegrationServletContainer} class itself.
	 *
	 * @return absolute path to file
	 * @throws FileNotFoundException if given file does not exist
	 */
	protected String getAbsolutePath(final String path) throws Exception
	{
		final File fullPath = new File(path).getAbsoluteFile();
		final Path parentDir = fullPath.getParentFile().toPath();
		try (final DirectoryStream<Path> paths = Files.newDirectoryStream(parentDir, fullPath.getName()))
		{
			final List<Path> filesFound = StreamSupport.stream(paths.spliterator(), false).collect(Collectors.toList());
			if (filesFound.isEmpty())
			{
				throw new FileNotFoundException("File not found: " + fullPath.toString());
			}
			if (filesFound.size() > 1)
			{
				throw new FileNotFoundException(
						"Ambiguous file path " + fullPath.toString() + ": found \n" + StringUtils.join(filesFound, '\n'));
			}
			return filesFound.get(0).toFile().getCanonicalFile().getAbsolutePath();
		}
	}

	public String getConnectionPath()
	{
		return connectionPath;
	}

	public String getWarPath()
	{
		return warPath;
	}

	public Properties getProperties()
	{
		return properties;
	}

	public String getLogFilePath()
	{
		return logFilePath;
	}

	public String getHeartbeat()
	{
		return heartbeat;
	}

	public Process getServerProcess()
	{
		return serverProcess;
	}

	public String getWebappRunnerJarFile()
	{
		return webappRunnerJarFile;
	}

	/**
	 * Sets the webappRunner jar file. For instance: bin/jetty-runner.jar
	 *
	 * @param webappRunnerJarFile The jar file name and location.
	 */
	public void setWebappRunnerJarFile(final String webappRunnerJarFile)
	{
		this.webappRunnerJarFile = webappRunnerJarFile;
	}

	/**
	 * @param properties properties to pass to servlet
	 */
	public void setProperties(final Properties properties)
	{
		this.properties = properties;
	}

	/**
	 * File where to store server console output. Absolute or relative to platform path.
	 * <p>
	 * Directory provided must exist. If the file exists, log info will be appended. If there is no such file, it will be
	 * created. No log rotation.
	 * </p>
	 * <p>
	 * Default value is {@code ../../log/atddrunner.log}
	 * </p>
	 *
	 * @param path path to log file
	 */
	public void setLogFilePath(final String path)
	{
		logFilePath = path;
	}

	/**
	 * Path to resource, which can indicate the servlet is alive.
	 *
	 * @param path absolute path to heartbeat resource
	 */
	public void setHeartbeat(final String path)
	{
		heartbeat = path;
	}

	/**
	 * @param connectionPath servlet end point URI
	 */
	public void setConnectionPath(final String connectionPath)
	{
		this.connectionPath = connectionPath;
	}

	/**
	 * Path to servlet file. Can be absolute or relative to directory that contains this class
	 *
	 * @param warPath path to servlet file
	 */
	public void setWarPath(final String warPath)
	{
		this.warPath = warPath;
	}

	/**
	 * Path to jetty config file. Can be absolute or relative to directory that contains this class
	 *
	 * @param jettyConfigPath path to custom jetty config file
	 */
	public void setJettyConfigPath(final String jettyConfigPath)
	{
		this.jettyConfigPath = jettyConfigPath;
	}

	/**
	 * Path to jetty lib folder. Can be absolute or relative to directory that contains this class
	 *
	 * @param libPath path to jetty lib folder
	 */
	public void setLibPath(final String libPath)
	{
		this.libPath = libPath;
	}

	/**
	 * Port to run jetty. Default value is 8080.
	 *
	 * @param port the port number of the jetty instance.
	 */
	public void setPort(final Integer port)
	{
		this.port = port;
	}

	/**
	 * Servlet configurations to run. If you have some servlets to run on jetty you can use this method to set list of
	 * them. If {@code warPath} was configured (optionally with {@code connectionPath}) it also would be added to jetty
	 * as an extra ServletDefinition together with the list.
	 *
	 * @param servlets list of servlet configurations
	 */
	public void setServlets(final List<ServletDefinition> servlets)
	{
		this.servlets.addAll(servlets);
	}
}
