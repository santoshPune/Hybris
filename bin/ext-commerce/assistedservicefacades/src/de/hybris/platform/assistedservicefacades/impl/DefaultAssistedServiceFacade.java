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
 *
 */
package de.hybris.platform.assistedservicefacades.impl;

import de.hybris.platform.assistedservicefacades.AssistedServiceFacade;
import de.hybris.platform.assistedservicefacades.constants.AssistedservicefacadesConstants;
import de.hybris.platform.assistedservicefacades.util.AssistedServiceUtils;
import de.hybris.platform.assistedserviceservices.AssistedServiceService;
import de.hybris.platform.assistedserviceservices.constants.AssistedserviceservicesConstants;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentBadCredentialsException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentBlockedException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentNoCustomerAndCartIdException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceAgentNotLoggedInException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceCartBindException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceWrongCartIdException;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceWrongCustomerIdException;
import de.hybris.platform.assistedserviceservices.utils.AssistedServiceSession;
import de.hybris.platform.assistedserviceservices.utils.CustomerEmulationParams;
import de.hybris.platform.commercefacades.order.data.CartData;
import de.hybris.platform.commercefacades.user.data.CustomerData;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.commerceservices.i18n.CommerceCommonI18NService;
import de.hybris.platform.commerceservices.model.user.StoreEmployeeGroupModel;
import de.hybris.platform.converters.Converters;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.EmployeeModel;
import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.jalo.user.CookieBasedLoginToken;
import de.hybris.platform.jalo.user.LoginToken;
import de.hybris.platform.order.CartService;
import de.hybris.platform.order.impl.DefaultCartService;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.event.EventService;
import de.hybris.platform.servicelayer.exceptions.ClassMismatchException;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.PasswordEncoderService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.ticket.enums.EventType;
import de.hybris.platform.ticketsystem.events.SessionEvent;
import de.hybris.platform.util.Config;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;


/**
 * Default implementation of the {@link AssistedServiceFacade} interface.
 */
public class DefaultAssistedServiceFacade implements AssistedServiceFacade
{

	// Abbreviation 'AS' in this class means Assisted Service
	private static final Logger LOG = Logger.getLogger(AssistedServiceFacade.class);

	// Hashmap for storing number of bad password attempts for each agent
	private final ConcurrentHashMap<String, Integer> bruteForceAttackCache = new ConcurrentHashMap();

	// Session key for storing customer which was logged in before AS agent
	private static final String AS_SAVED_ON_AGENTLOGIN_CUSTOMER = AssistedServiceFacade.class.getName() + "_saved_customer";

	/* Default parameters values */
	private static final int DEFAULT_SESSION_TIMEOUT = 660;
	private static final int DEFAULT_SESSION_TIMER = 600;
	private static final int DEFAULT_MAX_BAD_ATTEMPTS = 3;

	private static final String SESSION_CART_KEY = "cart";
	private static final String AGENT_TIMER_KEY = "agentTimer";
	private static final String CREATE_DISABLED_KEY = "createDisabled";
	private static final String EMULATE_BY_ORDER_KEY = "emulateByOrder";
	private static final String ASM_ERROR_MESSAGE_KEY = "asm_message";
	private static final String ASM_ERROR_MESSAGE_ARGS_KEY = "asm_message_args";
	private static final String ASM_AGENT_STORE = "asm_agent_store";

	private CartService cartService;
	private SessionService sessionService;
	private UserService userService;
	private PasswordEncoderService passwordEncoderService;
	private ModelService modelService;
	private EventService eventService;
	private CommerceCommonI18NService commerceCommonI18NService;
	private AssistedServiceService assistedServiceService;
	private Converter<UserModel, CustomerData> customerConverter;
	private Converter<CartModel, CartData> cartConverter;

	@Override
	public Map<String, Object> getAssistedServiceSessionAttributes()
	{
		// Populate model with params to be displayed
		final Map<String, Object> attributes = new HashMap();
		attributes.putAll(getAsmSession().getAsmSessionParametersMap());
		attributes.put(SESSION_CART_KEY,
				getSessionService().getCurrentSession().getAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME));
		attributes.put(AGENT_TIMER_KEY, String.valueOf(getAssistedServiceSessionTimerValue()));
		attributes.put(CREATE_DISABLED_KEY, Config.getParameter(AssistedservicefacadesConstants.CREATE_DISABLED_PROPERTY));
		attributes.put(EMULATE_BY_ORDER_KEY, Config.getParameter(AssistedservicefacadesConstants.EMULATE_BY_ORDER_PROPERTY));
		attributes.put(ASM_AGENT_STORE, getAssistedServiceAgentStore(getAsmSession().getAgent()));

		final String errorMessage = getAsmSession().getFlashErrorMessage();
		if (errorMessage != null)
		{
			attributes.put(ASM_ERROR_MESSAGE_KEY, errorMessage);
			attributes.put(ASM_ERROR_MESSAGE_ARGS_KEY, getAsmSession().getFlashErrorMessageArgs());
		}
		return attributes;
	}

	@Override
	public boolean isAssistedServiceModeLaunched()
	{
		return getAsmSession() != null;
	}

	@Override
	public void emulateCustomer(final String customerId, final String cartId, final String orderId)
			throws AssistedServiceException
	{
		try
		{
			// validate session at first
			validateSession();
		}
		catch (final AssistedServiceAgentNotLoggedInException e)
		{
			// in case AS agent isn't logged in yet - save parameters in session
			getAsmSession().setSavedEmulationData(new CustomerEmulationParams(customerId, cartId, orderId));
			throw e;
		}

		// Both parameters can't be blank at the same time
		if (StringUtils.isBlank(customerId) && StringUtils.isBlank(cartId))
		{
			throw new AssistedServiceAgentNoCustomerAndCartIdException(
					Localization.getLocalizedString("asm.emulate.error.no_customer_or_cart_id_provided"));
		}

		try
		{
			UserModel customer = assistedServiceService.getCustomer(customerId);
			// emulate only customers and not agents
			if ((customer instanceof CustomerModel) && !isAssistedServiceAgent(customer))
			{
				// attach cart to the session in case it's been provided
				if (StringUtils.isNotBlank(cartId))
				{
					final CartModel cart = assistedServiceService.getCartByCode(cartId, customer);

					if (cart == null)
					{
						final OrderModel order = assistedServiceService.gerOrderByCode(cartId, customer);
						if (order == null)
						{
							throw new AssistedServiceWrongCartIdException("Cart ID/Order ID not found");
						}
						if (getUserService().isAnonymousUser(customer))
						{
							customer = order.getUser();
						}
						getSessionService().setAttribute(AssistedservicefacadesConstants.ASM_ORDER_ID_TO_REDIRECT, order.getCode());
						attachLatestSessionCart(customer);
					}
					else
					{
						// Fix for BAD-1100
						cart.setCurrency(commerceCommonI18NService.getCurrentCurrency());
						getCartService().setSessionCart(cart);
					}
				}
				else
				{
					// in case no cartId has been provided get the latest modified one
					attachLatestSessionCart(customer);
				}

				if (StringUtils.isNotBlank(orderId))
				{
					final OrderModel order = assistedServiceService.gerOrderByCode(orderId, customer);
					if (order == null)
					{
						getAsmSession().setFlashErrorMessage("asm.emulate.error.order_not_found");
						getAsmSession().setFlashErrorMessageArgs(orderId);
					}
					else
					{
						getSessionService().setAttribute(AssistedservicefacadesConstants.ASM_ORDER_ID_TO_REDIRECT, order.getCode());
					}
				}
				getUserService().setCurrentUser(customer);
				getAsmSession().setEmulatedCustomer(customer);

				final SessionEvent asmStartSessionEventData = new SessionEvent();
				asmStartSessionEventData.setAgent(getAsmSession().getAgent());
				asmStartSessionEventData.setCustomer(customer);
				asmStartSessionEventData.setCreatedAt(new Date());
				asmStartSessionEventData.setAgentGroups(new ArrayList<>(getAsmSession().getAgent().getGroups()));
				asmStartSessionEventData.setEventType(EventType.START_SESSION_EVENT);
				eventService.publishEvent(asmStartSessionEventData);
			}
			else
			{
				throw new AssistedServiceWrongCustomerIdException(
						"This id belongs to non-customer person. Will not add customer and/or cart to the session.");
			}

		}
		catch (final UnknownIdentifierException e)
		{
			e.printStackTrace();
			throw new AssistedServiceWrongCustomerIdException(
					"Unknown customer id. Will not add customer and/or cart to the session.", e);
		}
	}

	@Override
	public void emulateCustomer(final String customerId, final String cartId) throws AssistedServiceException
	{
		emulateCustomer(customerId, cartId, null);
	}

	@Override
	public void stopEmulateCustomer()
	{
		final SessionEvent asmStopSessionEventData = new SessionEvent();
		asmStopSessionEventData.setAgent(getAsmSession().getAgent());
		asmStopSessionEventData.setCustomer(getAsmSession().getEmulatedCustomer());
		asmStopSessionEventData.setCreatedAt(new Date());
		asmStopSessionEventData.setAgentGroups(new ArrayList<>(getAsmSession().getAgent().getGroups()));
		asmStopSessionEventData.setEventType(EventType.END_SESSION_EVENT);

		getAsmSession().setEmulatedCustomer(null);
		detachSessionCart();
		getUserService().setCurrentUser(getUserService().getAnonymousUser());

		eventService.publishEvent(asmStopSessionEventData);
	}

	/**
	 * Detaches current cart from session and attaches newly created empty one.
	 */
	protected void detachSessionCart()
	{
		getSessionService().removeAttribute(DefaultCartService.SESSION_CART_PARAMETER_NAME);
		// At first look this method doesn't make any sense, but that's the simplest way
		//    to create new empty cart
		getCartService().setSessionCart(getCartService().getSessionCart());
	}

	protected void attachLatestSessionCart(final UserModel customer)
	{
		final CartModel cart = assistedServiceService.getLatestModifiedCart(customer);
		if (cart != null)
		{
			// Fix for BAD-1100
			cart.setCurrency(commerceCommonI18NService.getCurrentCurrency());

			getCartService().setSessionCart(cart);
		}
		else
		{
			detachSessionCart();
		}
	}

	@Override
	public void launchAssistedServiceMode()
	{
		cleanAsmSession();
	}

	@Override
	public void quitAssistedServiceMode()
	{
		try
		{
			logoutAssistedServiceAgent();
		}
		catch (final AssistedServiceException e)
		{
			// AssistedServiceException here means that AS agent isn't logged in.
			// That's ok. Just do nothing after that
			LOG.debug(e.getMessage(), e);
		}
		quitAsmSession();
	}

	@Override
	public void loginAssistedServiceAgent(final String username, final String password) throws AssistedServiceException
	{
		verifyAssistedServiceMode();
		verifyFormLoginAbility();
		try
		{
			final UserModel agent = getUserService().getUserForUID(username, EmployeeModel.class);

			if (agent.isLoginDisabled())
			{
				throw new AssistedServiceAgentBlockedException("Account was blocked.");
			}

			if (!getPasswordEncoderService().isValid(agent, password))
			{
				// check for a possible brute force
				if (isBruteForce(agent))
				{
					agent.setLoginDisabled(true);
					getModelService().save(agent);
					resetBruteForceCounter(agent);
					throw new AssistedServiceAgentBlockedException("Account has been blocked.");
				}
				throw new AssistedServiceAgentBadCredentialsException("User Name and Password doesn't match.");
			}
			// reset brute force counter after successful login
			resetBruteForceCounter(agent);
			loginAssistedServiceAgent(agent);
			LOG.info(String.format("Agent [%s] has been loged in using login form", agent.getUid()));
		}
		catch (final UnknownIdentifierException | ClassMismatchException e)
		{
			LOG.debug(e.getMessage(), e);
			throw new AssistedServiceAgentBadCredentialsException("Unknown user id.");
		}
	}

	@Override
	public void loginAssistedServiceAgent(final HttpServletRequest request) throws AssistedServiceException
	{
		// get token from request (cookie)
		final Cookie samlTokenCookie = AssistedServiceUtils.getSamlCookie(request);
		if (null != samlTokenCookie)
		{
			launchAssistedServiceMode();
			final LoginToken token = new CookieBasedLoginToken(samlTokenCookie);
			try
			{
				// grab user model
				final UserModel agent = getUserService().getUserForUID(token.getUser().getUid());

				// ensure credentials (password didn't change)
				if (agent.getEncodedPassword().equals(token.getPassword()))
				{
					// success --> do 'soft login'
					// optional: remove cookie ?
					loginAssistedServiceAgent(agent);
					LOG.info(String.format("Agent [%s] has been loged in using saml", agent.getUid()));
				}
			}
			catch (final UnknownIdentifierException | ClassMismatchException e)
			{
				LOG.debug(e.getMessage(), e);
				throw new AssistedServiceAgentBadCredentialsException("Unknown user id.");
			}
		}
	}

	protected void loginAssistedServiceAgent(final UserModel agent) throws AssistedServiceException
	{
		verifyAssistedServiceAgent(agent);

		// there is a little hack over there - we change current user to 'anonymous', so it's not going to be removed on authentication step
		getCartService().changeCurrentCartUser(getUserService().getAnonymousUser());

		getAsmSession().setAgent(agent);

		// save previously logged in customer
		if (!isAssistedServiceAgent(getUserService().getCurrentUser()))
		{
			getSessionService().getCurrentSession().setAttribute(AS_SAVED_ON_AGENTLOGIN_CUSTOMER, getUserService().getCurrentUser());
		}
	}

	@Override
	public void logoutAssistedServiceAgent() throws AssistedServiceException
	{
		verifyAssistedServiceMode();
		if (getAsmSession().getAgent() == null)
		{
			throw new AssistedServiceException(Localization.getLocalizedString("asm.logout.error.no_agent"));
		}

		final SessionEvent agentLogoutEvent = new SessionEvent();
		agentLogoutEvent.setAgent(getAsmSession().getAgent());
		agentLogoutEvent.setCreatedAt(new Date());
		agentLogoutEvent.setEventType(EventType.AGENT_LOGOUT);
		eventService.publishEvent(agentLogoutEvent);

		cleanAsmSession();

		if (isAssistedServiceAgent(getUserService().getCurrentUser()))
		{
			getUserService().setCurrentUser(getUserService().getAnonymousUser());
			getCartService().getSessionCart().setUser(getUserService().getAnonymousUser());
		}
	}

	@Override
	public int getAssistedServiceSessionTimeout()
	{
		final String timeout = Config.getParameter(AssistedservicefacadesConstants.ASM_AGENT_SESSION_TIMEOUT);
		if (StringUtils.isNotBlank(timeout))
		{
			try
			{
				final int timeoutInt = Integer.parseInt(timeout);
				if (timeoutInt > 0)
				{
					return timeoutInt;
				}
			}
			catch (final NumberFormatException e)
			{
				LOG.warn(String.format("Timeout value from config file - [%s] can not be cast to integer.", timeout));
			}
		}
		LOG.warn(String.format("Bad or missing property [%s]. Using [%s] as default value.",
				AssistedservicefacadesConstants.ASM_AGENT_SESSION_TIMEOUT, String.valueOf(DEFAULT_SESSION_TIMEOUT)));
		return DEFAULT_SESSION_TIMEOUT;
	}

	@Override
	public int getAssistedServiceSessionTimerValue()
	{
		final String timer = Config.getParameter(AssistedservicefacadesConstants.ASM_AGENT_SESSION_TIMER);
		if (StringUtils.isNotBlank(timer))
		{
			try
			{
				final int timerInt = Integer.parseInt(timer);
				if (timerInt > 0)
				{
					return timerInt;
				}
			}
			catch (final NumberFormatException e)
			{
				LOG.warn(String.format("Timer value from config file - [%s] can not be cast to integer.", timer));
			}
		}
		LOG.warn(String.format("Bad or missing property [%s]. Using [%s] as default value.",
				AssistedservicefacadesConstants.ASM_AGENT_SESSION_TIMER, String.valueOf(DEFAULT_SESSION_TIMER)));
		return DEFAULT_SESSION_TIMER;
	}

	@Override
	@Deprecated
	public List<CustomerModel> getSuggestedCustomers(final String username) throws AssistedServiceException
	{
		validateSession();

		return assistedServiceService.getSuggestedCustomers(username);
	}

	@Override
	public List<CustomerData> getSuggestedCustomerList(final String username) throws AssistedServiceException
	{
		validateSession();

		return Converters.convertAll(assistedServiceService.getSuggestedCustomers(username), getCustomerConverter());
	}

	@Override
	public void emulateAfterLogin() throws AssistedServiceException
	{
		// try to emulate using previous emulation call parameters
		final CustomerEmulationParams savedEmulationParams = getAsmSession().getSavedEmulationData();

		final CartModel cart = getCartService().getSessionCart();
		final UserModel savedUser = getSessionService().getCurrentSession().getAttribute(AS_SAVED_ON_AGENTLOGIN_CUSTOMER);

		// in case we saved params after Deep Link were called without logging in first
		if (savedEmulationParams != null)
		{
			assistedServiceService.restoreCartToUser(cart, savedUser);
			emulateCustomer(savedEmulationParams.getUserId(), savedEmulationParams.getCartId(), savedEmulationParams.getOrderId());
			getAsmSession().setSavedEmulationData(null);
			return;
		}

		// in case there were a user before we signed in
		if (savedUser != null)
		{
			getSessionService().getCurrentSession().removeAttribute(AS_SAVED_ON_AGENTLOGIN_CUSTOMER);
			if (CollectionUtils.isNotEmpty(cart.getEntries()))
			{
				assistedServiceService.restoreCartToUser(cart, savedUser);
				if (!getUserService().isAnonymousUser(savedUser))
				{
					emulateCustomer(savedUser.getUid(), cart.getCode());
				}
				else
				{
					emulateCustomer(null, cart.getCode());
				}
			}
			else
			{
				if (!getUserService().isAnonymousUser(savedUser))
				{
					emulateCustomer(savedUser.getUid(), null);
				}
			}

			if (isAssistedServiceAgent(getUserService().getCurrentUser()))
			{
				getUserService().setCurrentUser(getUserService().getAnonymousUser());
			}
		}
		else
		{
			getUserService().setCurrentUser(getUserService().getAnonymousUser());
		}
	}

	@Override
	public void bindCustomerToCart(final String customerId, final String cartId) throws AssistedServiceException
	{
		validateSession();
		try
		{
			final UserModel customer = customerId == null ? getUserService().getCurrentUser() : getUserService().getUserForUID(
					customerId);
			final CartModel cart = cartId == null ? getCartService().getSessionCart() : assistedServiceService.getCartByCode(cartId,
					getUserService().getAnonymousUser());
			if (cart == null || !getUserService().isAnonymousUser(cart.getUser()))
			{
				throw new AssistedServiceCartBindException(Localization.getLocalizedString("asm.bindCart.error.not_anonymous_cart"));
			}
			getUserService().setCurrentUser(customer);
			getCartService().setSessionCart(cart);
			getCartService().changeCurrentCartUser(customer);
			getAsmSession().setEmulatedCustomer(customer);
		}
		catch (final UnknownIdentifierException e)
		{
			throw new AssistedServiceCartBindException(Localization.getLocalizedString("asm.bindCart.error.unknown_cart_id"), e);
		}
	}

	/**
	 * Checks current session for ASM requirements
	 *
	 * @throws AssistedServiceException
	 *            in case ASM not launched or AS agent isn't logged in
	 */
	protected void validateSession() throws AssistedServiceException
	{
		verifyAssistedServiceMode();
		if (!isAssistedServiceAgentLoggedIn())
		{
			throw new AssistedServiceAgentNotLoggedInException("Assisted Service Agent is not logged in.");
		}
	}

	/**
	 * Checks current session for ASM mode launched.
	 *
	 * @throws AssistedServiceException
	 *            in case it's not
	 */
	protected void verifyAssistedServiceMode() throws AssistedServiceException
	{
		if (!isAssistedServiceModeLaunched())
		{
			throw new AssistedServiceException("Assisted Service mode inactive.");
		}
	}

	/**
	 * Verify whether AS agent can be logged in via https login form.
	 *
	 * @throws AssistedServiceException
	 *            in case it can't
	 */
	protected void verifyFormLoginAbility() throws AssistedServiceException
	{
		if (!Config.getBoolean(AssistedservicefacadesConstants.AS_ENABLE_FORM_LOGIN, true))
		{
			throw new AssistedServiceException("asm.login.disabled");
		}
	}

	/**
	 * Verify that provided user participate in a parent AS agent group.
	 *
	 * @param asmAgent
	 * @throws AssistedServiceException
	 *            in case he's not
	 */
	protected void verifyAssistedServiceAgent(final UserModel asmAgent) throws AssistedServiceException
	{
		if (!isAssistedServiceAgent(asmAgent))
		{
			throw new AssistedServiceException(String.format("User does not belong to the correct user group - [%s].",
					AssistedserviceservicesConstants.AS_AGENT_GROUP_UID));
		}
	}

	/**
	 * Verify that provided user participate in a parent AS agent group.
	 *
	 * @param asmAgent
	 * @return true when he is
	 */
	protected boolean isAssistedServiceAgent(final UserModel asmAgent)
	{
		final Set<UserGroupModel> userGroups = getUserService().getAllUserGroupsForUser(asmAgent);
		for (final UserGroupModel userGroup : userGroups)
		{
			if (AssistedserviceservicesConstants.AS_AGENT_GROUP_UID.equals(userGroup.getUid()))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isAssistedServiceAgentLoggedIn()
	{
		return getAsmSession() != null && getAsmSession().getAgent() != null;
	}

	@Override
	@Deprecated
	public CustomerModel createNewCustomer(final String customerId, final String customerName) throws AssistedServiceException
	{
		final boolean createDisabled = BooleanUtils.toBoolean(Config
				.getParameter(AssistedservicefacadesConstants.CREATE_DISABLED_PROPERTY));
		if (createDisabled)
		{
			throw new AssistedServiceException("Customer creation not enabled.");
		}
		try
		{
			return assistedServiceService.createNewCustomer(customerId, customerName);
		}
		catch (final DuplicateUidException ex)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(String.format("Trying to create user with already existed uid [%s]", customerId));
			}
			throw new AssistedServiceException("Duplicate User id", ex);
		}
	}

	@Override
	public CustomerData createCustomer(final String customerId, final String customerName) throws AssistedServiceException
	{
		final boolean createDisabled = BooleanUtils.toBoolean(Config
				.getParameter(AssistedservicefacadesConstants.CREATE_DISABLED_PROPERTY));
		if (createDisabled)
		{
			throw new AssistedServiceException("Customer creation not enabled.");
		}
		try
		{
			return getCustomerConverter().convert(assistedServiceService.createNewCustomer(customerId, customerName));
		}
		catch (final DuplicateUidException ex)
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(String.format("Trying to create user with already existed uid [%s]", customerId));
			}
			throw new AssistedServiceException("Duplicate User id", ex);
		}
	}

	@Override
	public AssistedServiceSession getAsmSession()
	{
		return assistedServiceService.getAsmSession();
	}

	protected void cleanAsmSession()
	{
		getSessionService().setAttribute(AssistedservicefacadesConstants.ASM_SESSION_PARAMETER, new AssistedServiceSession());
	}

	protected void quitAsmSession()
	{
		getSessionService().removeAttribute(AssistedservicefacadesConstants.ASM_SESSION_PARAMETER);
	}

	protected boolean isBruteForce(final UserModel asmAgent)
	{
		final int badAttemptNumber = bruteForceAttackCache.get(asmAgent.getUid()) != null ? bruteForceAttackCache.get(
				asmAgent.getUid()).intValue() + 1 : 1;
		int maxBadAttempts;
		try
		{
			maxBadAttempts = Config.getInt(AssistedservicefacadesConstants.AS_BAD_ATTEMPTS_BEFORE_DISABLE, DEFAULT_MAX_BAD_ATTEMPTS);
		}
		catch (final NumberFormatException e)
		{
			LOG.warn(String.format("Bad or missing property [%s] value, using [" + DEFAULT_MAX_BAD_ATTEMPTS + "]",
					AssistedservicefacadesConstants.AS_BAD_ATTEMPTS_BEFORE_DISABLE));
			maxBadAttempts = DEFAULT_MAX_BAD_ATTEMPTS;
		}

		if (badAttemptNumber >= maxBadAttempts)
		{
			LOG.info(String.format("Wrong password has been provided for agent [%s]. Attempt #%d. Agent has been blocked.",
					asmAgent.getUid(), Integer.valueOf(badAttemptNumber)));
			return true;
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(String.format("Wrong password has been provided for agent [%s]. Attempt #%d", asmAgent.getUid(),
						Integer.valueOf(badAttemptNumber)));
			}
			bruteForceAttackCache.put(asmAgent.getUid(), Integer.valueOf(badAttemptNumber));
		}
		return false;
	}

	protected void resetBruteForceCounter(final UserModel asmAgent)
	{
		bruteForceAttackCache.remove(asmAgent.getUid());
	}

	protected String getAssistedServiceAgentStore(final UserModel agent)
	{
		if (agent != null && CollectionUtils.isNotEmpty(agent.getAllGroups()))
		{
			final List<StoreEmployeeGroupModel> storeEmployeeGroups = getUserService()
					.getAllUserGroupsForUser(agent, StoreEmployeeGroupModel.class).stream().filter(group -> group.getStore() != null)
					.collect(Collectors.toList());
			if (storeEmployeeGroups.size() == 1)
			{
				return storeEmployeeGroups.get(0).getStore().getName();
			}
			else if (storeEmployeeGroups.size() > 1)
			{
				return "asm.emulate.multiple_stores";
			}
		}
		return "asm.emulate.no_stores";
	}

	@Override
	public Collection<CartModel> getCartsForCustomer(final CustomerModel customer)
	{
		return assistedServiceService.getCartsForCustomer(customer);
	}

	@Override
	public Collection<CartData> getCartListForCustomer(final CustomerModel customer)
	{
		return Converters.convertAll(assistedServiceService.getCartsForCustomer(customer), getCartConverter());
	}

	protected AssistedServiceService getAssistedServiceService()
	{
		return assistedServiceService;
	}

	@Required
	public void setAssistedServiceService(final AssistedServiceService assistedServiceService)
	{
		this.assistedServiceService = assistedServiceService;
	}

	protected CartService getCartService()
	{
		return cartService;
	}

	@Required
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	protected CommerceCommonI18NService getCommerceCommonI18NService()
	{
		return commerceCommonI18NService;
	}

	@Required
	public void setCommerceCommonI18NService(final CommerceCommonI18NService commerceCommonI18NService)
	{
		this.commerceCommonI18NService = commerceCommonI18NService;
	}

	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected PasswordEncoderService getPasswordEncoderService()
	{
		return passwordEncoderService;
	}

	@Required
	public void setPasswordEncoderService(final PasswordEncoderService passwordEncoderService)
	{
		this.passwordEncoderService = passwordEncoderService;
	}

	/**
	 * @return the modelService
	 */
	protected ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	protected EventService getEventService()
	{
		return eventService;
	}

	@Required
	public void setEventService(final EventService eventService)
	{
		this.eventService = eventService;
	}

	protected Converter<CartModel, CartData> getCartConverter()
	{
		return cartConverter;
	}

	@Required
	public void setCartConverter(final Converter<CartModel, CartData> cartConverter)
	{
		this.cartConverter = cartConverter;
	}

	protected Converter<UserModel, CustomerData> getCustomerConverter()
	{
		return customerConverter;
	}

	@Required
	public void setCustomerConverter(final Converter<UserModel, CustomerData> customerConverter)
	{
		this.customerConverter = customerConverter;
	}
}