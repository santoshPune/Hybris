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

package de.hybris.platform.cluster;


import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.cluster.jgroups.JGroupsHelper;

import org.junit.Test;

@UnitTest
public class JGroupsHelperTest
{
	@Test
	public void testDefaultUdpConfig()
	{
		final boolean isJGroupsConfiguredForTCP = JGroupsHelper.isTCPJGroupsConfig("jgroups-udp.xml");

		assertThat(isJGroupsConfiguredForTCP).isFalse();
	}

	@Test
	public void testDefaultTCPConfig()
	{
		final boolean isJGroupsConfiguredForTCP = JGroupsHelper.isTCPJGroupsConfig("jgroups-tcp.xml");

		assertThat(isJGroupsConfiguredForTCP).isTrue();
	}
}
