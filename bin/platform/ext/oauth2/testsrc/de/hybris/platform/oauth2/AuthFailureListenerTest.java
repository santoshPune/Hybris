package de.hybris.platform.oauth2;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.BruteForceLoginAttemptsModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.webservicescommons.model.OAuthClientDetailsModel;
import de.hybris.platform.webservicescommons.oauth2.client.ClientDetailsDao;

import org.apache.commons.configuration.Configuration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;


@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AuthFailureListenerTest
{
	@Mock
	private ConfigurationService configurationService;
	@Mock
	private ClientDetailsDao clientDetailsDao;
	@Mock
	private FlexibleSearchService searchService;
	@Mock
	private ModelService modelService;
	@Mock
	private Configuration configuration;
	@Mock
	private OAuthClientDetailsModel client;
	@Mock
	private BruteForceLoginAttemptsModel attempts;
	@Mock
	private SearchResult<BruteForceLoginAttemptsModel> result;
	private AuthFailureListener listener;

	@Before
	public void setUp() throws Exception
	{
		listener = new AuthFailureListener();
		listener.setClientDetailsDao(clientDetailsDao);
		listener.setConfigurationService(configurationService);
		listener.setModelService(modelService);
		listener.setSearchService(searchService);
		doReturn(configuration).when(configurationService).getConfiguration();
	}

	@Test
	public void testDisabled() throws Exception
	{
		when(configuration.getInt(anyString(), anyInt())).thenReturn(-1);
		listener.onApplicationEvent(new AuthenticationFailureBadCredentialsEvent(new UsernamePasswordAuthenticationToken("", ""),
				new AccountExpiredException("")));
		verifyZeroInteractions(clientDetailsDao);
		verifyZeroInteractions(searchService);
		verifyZeroInteractions(modelService);
	}
}
