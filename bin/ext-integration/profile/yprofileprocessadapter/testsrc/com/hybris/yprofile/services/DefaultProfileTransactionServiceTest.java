package com.hybris.yprofile.services;

import com.hybris.yprofile.clients.ProfileClient;
import com.hybris.yprofile.clients.ProfileResponse;
import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.basecommerce.enums.ConsignmentStatus;
import de.hybris.platform.basecommerce.enums.ReturnStatus;
import de.hybris.platform.core.enums.OrderStatus;
import de.hybris.platform.core.model.c2l.CountryModel;
import de.hybris.platform.core.model.c2l.CurrencyModel;
import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.order.OrderEntryModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.order.payment.DebitPaymentInfoModel;
import de.hybris.platform.core.model.product.ProductModel;
import de.hybris.platform.core.model.product.UnitModel;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.ordersplitting.model.ConsignmentEntryModel;
import de.hybris.platform.ordersplitting.model.ConsignmentModel;
import de.hybris.platform.returns.model.ReturnEntryModel;
import de.hybris.platform.returns.model.ReturnRequestModel;
import de.hybris.platform.store.BaseStoreModel;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import rx.Observable;

import java.util.*;

import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;


@UnitTest
public class DefaultProfileTransactionServiceTest {

    private static final String APP_ID = "test";
    private static final String TENANT_ID = "tenant";
    private static final String CONSENT_REFERENCE = "consent-reference-id";

    private DefaultProfileTransactionService defaultProfileService;

    @Mock
    private ProfileClient client;

    @Mock
    private ProfileResponse response;

    @Mock
    private ProfileConfigurationService profileConfigurationService;

    @Mock
    private ProfileCharonFactory charonFactory;

    private UserModel user;
    private AddressModel address;
    private DebitPaymentInfoModel paymentInfo;
    private LanguageModel language;
    private CurrencyModel currency;
    private CountryModel country;
    private ProductModel product0, product1;
    private UnitModel unit;
    private BaseStoreModel store;
    private OrderModel order;
    private ConsignmentModel consignment0, consignment1;
    private ReturnRequestModel returnRequest;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        defaultProfileService = new DefaultProfileTransactionService();
        defaultProfileService.setCharonFactory(charonFactory);
        defaultProfileService.setProfileConfigurationService(profileConfigurationService);

        createCoreData();
        createProductData();
        createUserData();
        createOrderData();

        when(charonFactory.client(APP_ID, ProfileClient.class)).thenReturn(client);
        when(profileConfigurationService.getApplicationId()).thenReturn(APP_ID);
        when(profileConfigurationService.getYaaSTenant()).thenReturn(TENANT_ID);
    }

    @Test
    public void verifyOrderWithValidConsentReferenceIsSentToYProfile(){

        when(client.sendOrderEvent(anyString(), anyString(), anyObject())).thenReturn(Observable.just(response));
        when(profileConfigurationService.isYaaSConfigurationPresent()).thenReturn(true);

        defaultProfileService.sendSubmitOrderEvent(order);

        verify(client, times(1)).sendOrderEvent(anyString(), anyString(), anyObject());
        verifyNoMoreInteractions(client);
    }

    @Test
    public void verifyPartialDeliveryWithValidConsentReferenceIsSentToYProfile(){

        createConsignments();
        when(client.sendOrderEvent(anyString(), anyString(), anyObject())).thenReturn(Observable.just(response));
        when(profileConfigurationService.isYaaSConfigurationPresent()).thenReturn(true);

        defaultProfileService.sendConsignmentEvent(consignment0);

        verify(client, times(1)).sendOrderEvent(anyString(), anyString(), anyObject());
        verifyNoMoreInteractions(client);
    }

    @Test
    public void verifyCompleteDeliveryWithValidConsentReferenceIsSentToYProfile(){

        createConsignments();
        consignment1.setStatus(ConsignmentStatus.SHIPPED);
        when(client.sendOrderEvent(anyString(), anyString(), anyObject())).thenReturn(Observable.just(response));
        when(profileConfigurationService.isYaaSConfigurationPresent()).thenReturn(true);

        defaultProfileService.sendConsignmentEvent(consignment1);

        verify(client, times(1)).sendOrderEvent(anyString(), anyString(), anyObject());
        verifyNoMoreInteractions(client);
    }

    @Test
    public void verifyPartialReturnOrderWithValidConsentReferenceIsSentToYProfile(){

        createPartialReturn();
        when(client.sendOrderEvent(anyString(), anyString(), anyObject())).thenReturn(Observable.just(response));
        when(profileConfigurationService.isYaaSConfigurationPresent()).thenReturn(true);

        defaultProfileService.sendReturnOrderEvent(returnRequest);

        verify(client, times(1)).sendOrderEvent(anyString(), anyString(), anyObject());
        verifyNoMoreInteractions(client);
    }

    @Test
    public void verifyCompleteReturnOrderWithValidConsentReferenceIsSentToYProfile(){

        createCompleteReturn();
        when(client.sendOrderEvent(anyString(), anyString(), anyObject())).thenReturn(Observable.just(response));
        when(profileConfigurationService.isYaaSConfigurationPresent()).thenReturn(true);

        defaultProfileService.sendReturnOrderEvent(returnRequest);

        verify(client, times(1)).sendOrderEvent(anyString(), anyString(), anyObject());
        verifyNoMoreInteractions(client);
    }

    @Test
    public void verifyDoNotTransactionToYaasWithInvalidYaaSConfiguration(){

        when(client.sendOrderEvent(anyString(), anyString(), anyObject())).thenReturn(Observable.just(response));
        when(profileConfigurationService.isYaaSConfigurationPresent()).thenReturn(false);

        defaultProfileService.sendSubmitOrderEvent(order);

        verify(client, times(0)).sendOrderEvent(anyString(), anyString(), anyObject());
    }

    private void createPartialReturn(){

        returnRequest = new ReturnRequestModel();
        returnRequest.setCode("returnId0");
        returnRequest.setOrder(order);
        returnRequest.setStatus(ReturnStatus.COMPLETED);
        returnRequest.setCreationtime(Calendar.getInstance().getTime());

        ReturnEntryModel entry0 = new ReturnEntryModel();
        entry0.setOrderEntry(order.getEntries().get(0));
        entry0.setStatus(ReturnStatus.COMPLETED);
        entry0.setReceivedQuantity(Long.valueOf(1));

        returnRequest.setReturnEntries(Arrays.asList(entry0));
    }

    private void createCompleteReturn(){

        returnRequest = new ReturnRequestModel();
        returnRequest.setCode("returnId0");
        returnRequest.setOrder(order);
        returnRequest.setStatus(ReturnStatus.COMPLETED);
        returnRequest.setCreationtime(Calendar.getInstance().getTime());

        ReturnEntryModel entry0 = new ReturnEntryModel();
        entry0.setOrderEntry(order.getEntries().get(0));
        entry0.setStatus(ReturnStatus.COMPLETED);
        entry0.setReceivedQuantity(Long.valueOf(1));

        ReturnEntryModel entry1 = new ReturnEntryModel();
        entry1.setOrderEntry(order.getEntries().get(1));
        entry1.setStatus(ReturnStatus.COMPLETED);
        entry1.setReceivedQuantity(Long.valueOf(2));

        returnRequest.setReturnEntries(Arrays.asList(entry0, entry1));
    }

    private void createConsignments(){

        consignment0 = new ConsignmentModel();
        consignment0.setCode("consignment0");
        consignment0.setOrder(order);
        consignment0.setShippingAddress(address);
        consignment0.setCarrier("carrier0");
        consignment0.setTrackingID("trackingId0");
        consignment0.setStatus(ConsignmentStatus.SHIPPED);
        consignment0.setCreationtime(Calendar.getInstance().getTime());

        ConsignmentEntryModel entry0 = new ConsignmentEntryModel();
        entry0.setOrderEntry(order.getEntries().get(0));
        Set<ConsignmentEntryModel> entriesConsignment0 = new HashSet<ConsignmentEntryModel>();
        entriesConsignment0.add(entry0);
        consignment0.setConsignmentEntries(entriesConsignment0);

        consignment1 = new ConsignmentModel();
        consignment1.setCode("consignment1");
        consignment1.setOrder(order);
        consignment1.setShippingAddress(address);
        consignment1.setCarrier("carrier1");
        consignment1.setTrackingID("trackingId1");
        consignment1.setStatus(ConsignmentStatus.READY);
        consignment1.setCreationtime(Calendar.getInstance().getTime());

        ConsignmentEntryModel entry1 = new ConsignmentEntryModel();
        entry1.setOrderEntry(order.getEntries().get(1));

        Set<ConsignmentEntryModel> entriesConsignment1 = new HashSet<ConsignmentEntryModel>();
        entriesConsignment1.add(entry1);
        consignment1.setConsignmentEntries(entriesConsignment1);

        Set<ConsignmentModel> consignments = new HashSet<ConsignmentModel>();
        consignments.add(consignment0);
        consignments.add(consignment1);
        order.setConsignments(consignments);

    }

    private void createOrderData(){

        order = new OrderModel();
        order.setCode("orderId");
        order.setUser(user);
        order.setConsentReference(CONSENT_REFERENCE);
        order.setCreationtime(Calendar.getInstance().getTime());
        order.setCurrency(currency);
        order.setStatus(OrderStatus.CREATED);
        order.setPaymentInfo(paymentInfo);
        order.setDeliveryAddress(address);
        order.setStore(store);

        OrderEntryModel entry0 = new OrderEntryModel();
        entry0.setProduct(product0);
        entry0.setQuantity(Long.valueOf(1));
        entry0.setUnit(unit);
        entry0.setBasePrice(Double.valueOf(10));
        entry0.setTotalPrice(Double.valueOf(10));
        entry0.setEntryNumber(Integer.valueOf(0));
        entry0.setOrder(order);
        entry0.setTaxValues(Collections.emptySet());

        OrderEntryModel entry1 = new OrderEntryModel();
        entry1.setProduct(product1);
        entry1.setQuantity(Long.valueOf(2));
        entry1.setUnit(unit);
        entry1.setBasePrice(Double.valueOf(5));
        entry1.setTotalPrice(Double.valueOf(5));
        entry1.setEntryNumber(Integer.valueOf(1));
        entry1.setOrder(order);
        entry1.setTaxValues(Collections.emptySet());


        order.setEntries(Arrays.asList(entry0, entry1));
        order.setAllPromotionResults(Collections.emptySet());
    }

    private void createCoreData(){
        language = new LanguageModel();
        language.setIsocode("en");

        country = new CountryModel();
        country.setIsocode("DE");

        currency = new CurrencyModel();
        currency.setIsocode("EUR");

        unit = new UnitModel();
        unit.setCode("myUnit");

        store = new BaseStoreModel();
        store.setUid("baseStoreId");
    }

    private void createProductData(){
        product0 = new ProductModel();
        product0.setCode("product0");

        product1 = new ProductModel();
        product1.setCode("product1");
    }

    private void createUserData(){

        user = new CustomerModel();
        user.setUid("customerId");
        user.setCreationtime(Calendar.getInstance().getTime());

        address = new AddressModel();
        address.setFirstname("firstName");
        address.setLastname("lastName");
        address.setStreetname("Nymphenburger Straße 86");
        address.setLine2("hybris");
        address.setPostalcode("80636");
        address.setTown("München");
        address.setCountry(country);

        address.setOwner(user);

        paymentInfo = new DebitPaymentInfoModel();
        paymentInfo.setOwner(user);
        paymentInfo.setBank("MeineBank");
        paymentInfo.setUser(user);
        paymentInfo.setAccountNumber("34434");
        paymentInfo.setBankIDNumber("1111112");
        paymentInfo.setBaOwner("Ich");
        paymentInfo.setCode("testPayment");
    }
}