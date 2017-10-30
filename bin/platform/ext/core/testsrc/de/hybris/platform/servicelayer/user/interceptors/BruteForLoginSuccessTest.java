package de.hybris.platform.servicelayer.user.interceptors;

import static com.google.common.collect.ImmutableMap.of;
import static java.lang.String.format;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.core.model.user.BruteForceLoginAttemptsModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.exceptions.ModelNotFoundException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;

import com.google.common.collect.ImmutableSet;

@IntegrationTest
public class BruteForLoginSuccessTest extends BruteForceLoginTest
{
	@Resource(name = "bruteForceLoginSuccess")
	private BruteForceLoginSuccess listener;




	@Test
	public void testWithoutAttemptsRecord() throws Exception
	{
		listener.onApplicationEvent(new AuthenticationSuccessEvent(new UsernamePasswordAuthenticationToken(testUid, "pwd")));
		try
		{
			findAttempts();
			org.junit.Assert.fail("should not be any attempt record");
		}
		catch (ModelNotFoundException e)
		{

		}
	}

	@Test
	public void testReset() throws Exception
	{
		BruteForceLoginAttemptsModel attempts = modelService.create(BruteForceLoginAttemptsModel.class);
		attempts.setUid(userService.getUserForUID(testUid).getUid());
		attempts.setAttempts(1);
		modelService.save(attempts);
		listener.onApplicationEvent(new AuthenticationSuccessEvent(new UsernamePasswordAuthenticationToken(testUid, "pwd")));
		assertThat(findAttempts().getAttempts()).isEqualTo(0);
	}


}
