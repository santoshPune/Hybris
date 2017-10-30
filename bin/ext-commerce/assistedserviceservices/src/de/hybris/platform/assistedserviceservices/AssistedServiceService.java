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
package de.hybris.platform.assistedserviceservices;

import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import de.hybris.platform.assistedserviceservices.utils.AssistedServiceSession;
import de.hybris.platform.commerceservices.customer.DuplicateUidException;
import de.hybris.platform.core.model.order.AbstractOrderModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.order.OrderModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;

import java.util.Collection;
import java.util.List;


public interface AssistedServiceService {

    /**
     * Get list of customers which username or email starts with provided value.
     *
     * @param username uid or customer's name
     * @return suggested customers
     */
    List<CustomerModel> getSuggestedCustomers(final String username);

    /**
     * Binds customer with provided id to cart if it's anonymous cart.
     */
    void bindCustomerToCart(final String customerId, final String cartId)  throws AssistedServiceException;

    /**
     * Creates a new customer by it email and name.
     *
     * @param customerId   email of to be newly created customer that is used as uid
     * @param customerName name of to be newly created customer (firstname and surname separated by space symbol)
     */
    CustomerModel createNewCustomer(final String customerId, final String customerName) throws DuplicateUidException;

    /**
     * Returns collection of a customer's carts
     *
     * @param customer customer model whose carts to be returned
     * @return collection of the customer's cart models
     */
    Collection<CartModel> getCartsForCustomer(final CustomerModel customer);

    /**
     * returns customer
     *
     * @param customerId
     * @return
     */
    UserModel getCustomer(final String customerId);

    /**
     * search for a cart with most resent CartModel::getModifiedtime
     *
     * @param customer
     * @return
     */
    CartModel getLatestModifiedCart(final UserModel customer);

    /**
     * search for order
     *
     * @param orderCode
     * @param customer
     * @return
     */
    OrderModel gerOrderByCode(final String orderCode, final UserModel customer);

    /**
     * Returns true when provided abstractOrderModel relates to current base site
     *
     * @param abstractOrderModel
     */
    boolean isAbstractOrderMatchBaseSite(final AbstractOrderModel abstractOrderModel);

    /**
     * search cart by code and customer
     *
     * @param cartCode
     * @param customer
     * @return
     */
    CartModel getCartByCode(final String cartCode, final UserModel customer);

    /**
     * Returns ASM session object with all information about current asm session.
     *
     * @return asm session object
     */
    AssistedServiceSession getAsmSession();

    /**
     * Restore cart to provided user
     *
     * @param cart
     * @param user
     */
    void restoreCartToUser(final CartModel cart, final UserModel user);
}