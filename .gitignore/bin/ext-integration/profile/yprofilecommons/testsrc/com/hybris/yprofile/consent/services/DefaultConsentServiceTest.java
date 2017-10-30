package com.hybris.yprofile.consent.services;

import com.hybris.charon.exp.HttpException;
import com.hybris.yprofile.consent.cookie.ConsentReferenceCookieGenerator;
import com.hybris.yprofile.services.ProfileCharonFactory;
import com.hybris.yprofile.services.ProfileConfigurationService;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.Observable;
import rx.observers.TestSubscriber;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

@UnitTest
public class DefaultConsentServiceTest {

    private static final String APP_ID = "test";
    private static final String TENANT_ID = "tenant";

    private DefaultConsentService defaultConsentService;

    @Mock
    private ConsentServiceClient client;

    @Mock
    private ConsentResponse consentResponse;

    @Mock
    private ConsentReferenceCookieGenerator cookieGenerator;

    @Mock
    private SessionService sessionService;

    @Mock
    private UserService userService;

    @Mock
    private ProfileConfigurationService profileConfigurationService;

    @Mock
    private ProfileCharonFactory charonFactory;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        defaultConsentService = new DefaultConsentService();
        defaultConsentService.setSessionService(sessionService);
        defaultConsentService.setCookieGenerator(cookieGenerator);
        defaultConsentService.setUserService(userService);
        defaultConsentService.setProfileConfigurationService(profileConfigurationService);
        defaultConsentService.setCharonFactory(charonFactory);

        when(consentResponse.getId()).thenReturn("consent-reference-id");
        when(consentResponse.getLink()).thenReturn("consent-reference-link");

        when(charonFactory.client(APP_ID, ConsentServiceClient.class)).thenReturn(client);
        when(profileConfigurationService.getApplicationId()).thenReturn(APP_ID);
        when(profileConfigurationService.getYaaSTenant()).thenReturn(TENANT_ID);
    }


    @Test
    public void getConsentReferenceIdWhenClientReturnValidConsentResponse(){

        when(client.getConsentReference(TENANT_ID,"userId")).thenReturn(Observable.just(consentResponse));
        when(profileConfigurationService.isYaaSConfigurationPresent()).thenReturn(true);

        TestSubscriber<ConsentResponse> testSubscriber = new TestSubscriber<>();
        defaultConsentService.generateConsentReferenceForUser("userId").subscribe(testSubscriber);

        ConsentResponse expectedResponse = new ConsentResponse();
        expectedResponse.setId("consent-reference-id");
        expectedResponse.setLink("consent-reference-link");

        testSubscriber.assertNoErrors();
        testSubscriber.assertValue(consentResponse);
    }



    @Test(expected = HttpException.class)
    public void assertErrorWhenClientReturnException(){

        when(client.getConsentReference(TENANT_ID,"userId")).thenThrow(new HttpException(403, "Forbidden"));
        when(profileConfigurationService.isYaaSConfigurationPresent()).thenReturn(true);

        TestSubscriber<ConsentResponse> testSubscriber = new TestSubscriber<>();
        defaultConsentService.generateConsentReferenceForUser("userId").subscribe(testSubscriber);

        testSubscriber.assertError(HttpException.class);
    }

    @Test
    public void verifyGenerateConsentReferenceWhenNoCookieIsDefined(){

        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[]{new Cookie("some-cookie", "some value")};
        when(request.getCookies()).thenReturn(cookies);

        HttpServletResponse response = mock(HttpServletResponse.class);

        ConsentResponse consentResponse = mock(ConsentResponse.class);
        when(consentResponse.getId()).thenReturn("consent-reference-id");
        when(client.getConsentReference(TENANT_ID,"userId")).thenReturn(Observable.just(consentResponse));
        when(profileConfigurationService.isYaaSConfigurationPresent()).thenReturn(true);
        when(cookieGenerator.getCookieName()).thenReturn("baseSiteUid-consentReference");
        UserModel userModel = mock(UserModel.class);
        when(userModel.getUid()).thenReturn("userId");
        when(userService.getCurrentUser()).thenReturn(userModel);

        defaultConsentService.generateConsentReference(request, response);

        verify(cookieGenerator, times(1)).getCookieName();
        verify(client, times(1)).getConsentReference(anyString(),anyString());
        verify(cookieGenerator, times(1)).addCookie(anyObject(), anyString());
        verifyNoMoreInteractions(cookieGenerator);
        verifyNoMoreInteractions(client);
    }

    @Test
    public void verifyDoNotGenerateConsentReferenceWhenCookieIsDefined(){

        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[]{new Cookie("some-cookie", "some value"), new Cookie("baseSiteUid-consentReference", "consent-reference-id")};
        when(request.getCookies()).thenReturn(cookies);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(cookieGenerator.getCookieName()).thenReturn("baseSiteUid-consentReference");
        defaultConsentService.generateConsentReference(request, response);

        verify(cookieGenerator, times(1)).getCookieName();
        verifyNoMoreInteractions(cookieGenerator);
        verifyNoMoreInteractions(client);
    }

    @Test
    public void verifyDoNotSetInvalidConsentReferenceInCookie(){

        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[]{new Cookie("some-cookie", "some value")};
        when(request.getCookies()).thenReturn(cookies);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(client.getConsentReference(TENANT_ID,"userId")).thenReturn(Observable.just(null));
        when(profileConfigurationService.isYaaSConfigurationPresent()).thenReturn(true);
        when(cookieGenerator.getCookieName()).thenReturn("baseSiteUid-consentReference");
        UserModel userModel = mock(UserModel.class);
        when(userModel.getUid()).thenReturn("userId");
        when(userService.getCurrentUser()).thenReturn(userModel);

        defaultConsentService.generateConsentReference(request, response);

        verify(cookieGenerator, times(1)).getCookieName();
        verify(client, times(1)).getConsentReference(anyString(),anyString());
        verify(cookieGenerator, times(0)).addCookie(anyObject(), anyString());
        verifyNoMoreInteractions(cookieGenerator);
        verifyNoMoreInteractions(client);
    }

    @Test
    public void verifyDoNotRequestConsentReferenceWithInvalidYaaSConfiguration(){

        HttpServletRequest request = mock(HttpServletRequest.class);
        Cookie[] cookies = new Cookie[]{new Cookie("some-cookie", "some value")};
        when(request.getCookies()).thenReturn(cookies);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(profileConfigurationService.isYaaSConfigurationPresent()).thenReturn(false);
        when(cookieGenerator.getCookieName()).thenReturn("baseSiteUid-consentReference");
        UserModel userModel = mock(UserModel.class);
        when(userModel.getUid()).thenReturn("userId");
        when(userService.getCurrentUser()).thenReturn(userModel);

        defaultConsentService.generateConsentReference(request, response);

        verify(client, times(0)).getConsentReference(anyString(),anyString());
    }
}