package com.hybris.yprofile.eventtracking.services;


import com.hybris.yprofile.consent.services.ConsentService;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@UnitTest
public class DefaultRawEventEnricherTest {

    private DefaultRawEventEnricher defaultRawEventEnricher;

    @Mock
    private UserService userService;

    @Mock
    private ConsentService consentService;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        defaultRawEventEnricher = new DefaultRawEventEnricher();
        defaultRawEventEnricher.setConsentService(consentService);
        defaultRawEventEnricher.setUserService(userService);

    }

    @Test
    public void verifyRawEventIsEnrichedLoggedInUser(){

        HttpServletRequest request = setupHttpRequest();

        CustomerModel user = mock(CustomerModel.class);
        when(user.getUid()).thenReturn("userId");
        when(user.getContactEmail()).thenReturn("contact@email.com");
        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.isAnonymousUser(user)).thenReturn(false);
        when(consentService.getConsentReferenceFromCookie(anyObject())).thenReturn("consent-reference-id");

        String result = defaultRawEventEnricher.enrich(getJson(), request);

        assertTrue(result.contains("\"session_id\":\"sessionId\""));
        assertTrue(result.contains("\"user_id\":\"userId\""));
        assertTrue(result.contains("\"user_email\":\"contact@email.com\""));
        assertTrue(result.contains("\"consent_reference\":\"consent-reference-id\""));
        assertTrue(result.contains("\"user_agent\":\"User-Agent\""));
    }

    @Test
    public void verifyRawEventIsEnrichedAnonymousUser(){

        HttpServletRequest request = setupHttpRequest();

        CustomerModel user = mock(CustomerModel.class);
        when(userService.getCurrentUser()).thenReturn(user);
        when(userService.isAnonymousUser(user)).thenReturn(true);
        when(consentService.getConsentReferenceFromCookie(anyObject())).thenReturn("consent-reference-id");

        String result = defaultRawEventEnricher.enrich(getJson(), request);

        assertTrue(result.contains("\"session_id\":\"sessionId\""));
        assertFalse(result.contains("\"user_id\":\"userId\""));
        assertFalse(result.contains("\"user_email\":\"contact@email.com\""));
        assertTrue(result.contains("\"consent_reference\":\"consent-reference-id\""));
        assertTrue(result.contains("\"user_agent\":\"User-Agent\""));
    }

    private HttpServletRequest setupHttpRequest(){

        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getId()).thenReturn("sessionId");

        Cookie[] cookies = new Cookie[]{new Cookie("some-cookie", "some value"),
                new Cookie("baseSiteUid-consentReference", "consent-reference-id")};

        when(request.getSession()).thenReturn(httpSession);
        when(request.getCookies()).thenReturn(cookies);
        when(request.getHeader("User-Agent")).thenReturn("User-Agent");

        return request;
    }

    private String getJson(){
        return "{\"_viewts\":\"1461149581\",\"idsite\":\"electronics\",\"_refts\":\"0\",\"wma\":\"0\"," +
                "\"cvar\":\"{\\\"1\\\":[\\\"ec_id\\\",\\\"00001000\\\"],\\\"2\\\":[\\\"_pkp\\\",\\\"1\\\"],\\\"3\\\":[\\\"_pks\\\",\\\"1382080\\\"],\\\"4\\\":[\\\"_pkn\\\",\\\"EOS450D + 18-55 IS Kit\\\"],\\\"5\\\":[\\\"_pkc\\\",\\\"\\\"]}\"," +
                "\"_idvc\":\"2\",\"dir\":\"0\",\"rec\":\"1\",\"revenue\":\"0\"," +
                "\"_idts\":\"1461149460\",\"java\":\"0\",\"_ects\":\"1461149557\"," +
                "\"_idn\":\"0\",\"gt_ms\":\"7130\",\"fla\":\"1\",\"gears\":\"0\"," +
                "\"res\":\"1920x1200\",\"qt\":\"0\"," +
                "\"urlref\":\"https:\\/\\/electronics.local:9002\\/yacceleratorstorefront\\/\"," +
                "\"cookie\":\"1\"," +
                "\"ec_items\":\"[[\\\"1382080\\\",\\\"EOS450D + 18-55 IS Kit\\\",[],\\\"574.88\\\",\\\"1\\\"]]\",\"ag\":\"0\"," +
                "\"realp\":\"0\",\"h\":\"14\",\"m\":\"8\"," +
                "\"url\":\"https:\\/\\/electronics.local:9002\\/yacceleratorstorefront\\/electronics\\/en\\/Open-Catalogue\\/Cameras\\/Digital-Cameras\\/Digital-SLR\\/EOS450D-%2B-18-55-IS-Kit\\/p\\/1382080\"," +
                "\"idgoal\":\"0\",\"r\":\"306416\",\"s\":\"51\",\"pdf\":\"1\"," +
                "\"eventtype\":\"ecommerce\",\"_id\":\"c35e7323191132e6\"}";
    }
}