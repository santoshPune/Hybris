/*
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

package de.hybris.platform.test.structure;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.bootstrap.config.ConfigUtil;
import de.hybris.platform.core.Registry;
import de.hybris.platform.servicelayer.ServicelayerBaseTest;
import org.fest.assertions.BasicDescription;
import org.junit.Before;
import org.junit.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.fest.assertions.Assertions.assertThat;


@IntegrationTest
public class ProjectStructureTest extends ServicelayerBaseTest
{

	List<List<String>> duplicateJarPairsToIgnore = new ArrayList<>();

	@Before
	public void setUp()
	{
		final Map<String, String> ignoredMD5 = Registry.getCurrentTenantNoFallback().getConfig()
				.getParametersMatching("duplicatejarchecker\\.ignore\\.(.*)", true);
		final Set<String> md5Pairs = ignoredMD5.keySet();
		for (final String md5Pair : md5Pairs)
		{
			final List<String> md5SumsSpited = Arrays.asList(md5Pair.split("-"));
			assertThat(md5SumsSpited).hasSize(2).overridingErrorMessage("Duplicate jars checker parameter should be in expected format \n");

			duplicateJarPairsToIgnore.add(md5SumsSpited);

		}
	}

	@Test
	public void testDuplicateJarFiles() throws IOException, NoSuchAlgorithmException
	{

		final String platformHome = ConfigUtil.getPlatformConfig(ProjectStructureTest.class).getPlatformHome().toString();
		final Set<String> allPlatformExtensionNames = ConfigUtil.getPlatformConfig(ProjectStructureTest.class)
				.getAllPlatformExtensionNames();

		final Set<String> dirToParse = new HashSet<>();
		dirToParse.add(platformHome);

		for (final String extensionName : allPlatformExtensionNames)
		{
			final String extensionDirectory = ConfigUtil.getPlatformConfig(ProjectStructureTest.class).getExtensionInfo(extensionName)
					.getExtensionDirectory().toString();

			if (!extensionDirectory.contains(platformHome))
			{
				dirToParse.add(extensionName);
			}
		}

		final List<Path> jarList = new ArrayList<>();
		for (final String dir : dirToParse)
		{
			Files.walk(Paths.get(dir)).filter(Files::isRegularFile).filter(p -> p.toAbsolutePath().toString().contains("lib"))
					.filter(p -> !p.toAbsolutePath().toString().contains("platform" + File.separator + "resources"))
					.filter(p -> !p.toAbsolutePath().toString().contains("platform" + File.separator + "apache-ant"))
					.filter(p -> p.getFileName().toString().endsWith(".jar")).forEach(path -> jarList.add(path));
		}

		final List<Hashtable<String, String>> listOfConflicts = new ArrayList<>();

		for (int i = 0; i < jarList.size(); i++)
		{
			for (int j = i + 1; j < jarList.size(); j++)
			{
				final String filename1 = jarList.get(i).getFileName().toString();
				final String filename2 = jarList.get(j).getFileName().toString();
				final Pattern p = Pattern.compile("-(\\d*[^a-zA-Z])+");  // insert your pattern here
				final Matcher m = p.matcher(filename1);
				Integer index1 = null;
				Integer index2 = null;

				if (m.find()) {
					index1 = m.start();
				}

				final Matcher m2 = p.matcher(filename2);
				if (m2.find()) {
					index2 = m2.start();
				}

				final String parsedFileName1 = (index1 != null) ? filename1.substring(0, index1) : filename1;
				final String parsedFileName2 = (index2 != null) ? filename2.substring(0, index2) : filename2;

				if (parsedFileName1.equals(parsedFileName2))
				{
					if (!(jarList.get(i).toString().contains("WEB-INF") && jarList.get(i).toString().contains("WEB-INF")))
					{
						final String hash_i = DatatypeConverter
								.printHexBinary(MessageDigest.getInstance("MD5").digest(Files.readAllBytes(jarList.get(i))));
						final String hash_j = DatatypeConverter
								.printHexBinary(MessageDigest.getInstance("MD5").digest(Files.readAllBytes(jarList.get(j))));

						final Hashtable<String, String> conflict = new Hashtable<>();
						conflict.put(jarList.get(i).toString(), hash_i);
						conflict.put(jarList.get(j).toString(), hash_j);
						listOfConflicts.add(conflict);
					}
				}
			}
		}

        final List<Hashtable<String, String>> copyOfListConflicts = new ArrayList<>(listOfConflicts);

		for (int i = 0; i < listOfConflicts.size(); i++)
		{
			for (final List<String> duplicateToIgnore : duplicateJarPairsToIgnore)
			{
				final String duplicateFromParameter1 = duplicateToIgnore.get(0);
				final String duplicateFromParameter2 = duplicateToIgnore.get(1);

				final Hashtable<String, String> duplicateFound = listOfConflicts.get(i);
				final List<String> duplicatePairFound = new ArrayList<>(duplicateFound.values());
				final String duplicateFound1 = duplicatePairFound.get(0);
				final String duplicateFound2 = duplicatePairFound.get(1);

				if ((duplicateFromParameter1.equals(duplicateFound1) && duplicateFromParameter2.equals(duplicateFound2))
						|| (duplicateFromParameter1.equals(duplicateFound2) && duplicateFromParameter1.equals(duplicateFound1)))
				{
					copyOfListConflicts.remove(i);
				}

			}
		}
		assertThat(copyOfListConflicts).hasSize(0).as(new BasicDescription("Can be overridden in project.properties"));

	}

}