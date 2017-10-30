/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 30 Oct, 2017 11:21:07 AM                    ---
 * ----------------------------------------------------------------
 *  
 * [y] hybris Platform
 *  
 * Copyright (c) 2000-2016 SAP SE
 * All rights reserved.
 *  
 * This software is the confidential and proprietary information of SAP
 * Hybris ("Confidential Information"). You shall not disclose such
 * Confidential Information and shall use it only in accordance with the
 * terms of the license agreement you entered into with SAP Hybris.
 *  
 */
package de.hybris.platform.commerceservices.constants;

/**
 * @deprecated use constants in Model classes instead
 */
@Deprecated
@SuppressWarnings({"unused","cast","PMD"})
public class GeneratedCommerceServicesConstants
{
	public static final String EXTENSIONNAME = "commerceservices";
	public static class TC
	{
		public static final String CUSTOMERLIST = "CustomerList".intern();
		public static final String CUSTOMERTYPE = "CustomerType".intern();
		public static final String FORGOTTENPASSWORDPROCESS = "ForgottenPasswordProcess".intern();
		public static final String ORGUNIT = "OrgUnit".intern();
		public static final String PICKUPDELIVERYMODE = "PickUpDeliveryMode".intern();
		public static final String PICKUPINSTOREMODE = "PickupInStoreMode".intern();
		public static final String PROMOTIONORDERRESTRICTION = "PromotionOrderRestriction".intern();
		public static final String SALESAPPLICATION = "SalesApplication".intern();
		public static final String SITECHANNEL = "SiteChannel".intern();
		public static final String SITETHEME = "SiteTheme".intern();
		public static final String SOLRINDEXEDPROPERTYFACETSORT = "SolrIndexedPropertyFacetSort".intern();
		public static final String SOLRSORT = "SolrSort".intern();
		public static final String SOLRSORTFIELD = "SolrSortField".intern();
		public static final String STOREEMPLOYEEGROUP = "StoreEmployeeGroup".intern();
		public static final String STOREFRONTCUSTOMERPROCESS = "StoreFrontCustomerProcess".intern();
		public static final String STOREFRONTPROCESS = "StoreFrontProcess".intern();
		public static final String STORELOCATORFEATURE = "StoreLocatorFeature".intern();
		public static final String UIEXPERIENCELEVEL = "UiExperienceLevel".intern();
		public static final String WAREHOUSECONSIGNMENTSTATE = "WarehouseConsignmentState".intern();
	}
	public static class Attributes
	{
		public static class AbstractOrder
		{
			public static final String GUID = "guid".intern();
			public static final String PROMOTIONORDERRESTRICTIONS = "promotionOrderRestrictions".intern();
			public static final String SITE = "site".intern();
			public static final String STORE = "store".intern();
		}
		public static class AbstractOrderEntry
		{
			public static final String DELIVERYPOINTOFSERVICE = "deliveryPointOfService".intern();
		}
		public static class Address
		{
			public static final String VISIBLEINADDRESSBOOK = "visibleInAddressBook".intern();
		}
		public static class BaseSite
		{
			public static final String CHANNEL = "channel".intern();
			public static final String DEFAULTLANGUAGE = "defaultLanguage".intern();
			public static final String DEFAULTPROMOTIONGROUP = "defaultPromotionGroup".intern();
			public static final String LOCALE = "locale".intern();
			public static final String SOLRFACETSEARCHCONFIGURATION = "solrFacetSearchConfiguration".intern();
			public static final String THEME = "theme".intern();
		}
		public static class BaseStore
		{
			public static final String CREATERETURNPROCESSCODE = "createReturnProcessCode".intern();
			public static final String CURRENCIES = "currencies".intern();
			public static final String CUSTOMERALLOWEDTOIGNORESUGGESTIONS = "customerAllowedToIgnoreSuggestions".intern();
			public static final String DEFAULTCURRENCY = "defaultCurrency".intern();
			public static final String DEFAULTDELIVERYORIGIN = "defaultDeliveryOrigin".intern();
			public static final String DEFAULTLANGUAGE = "defaultLanguage".intern();
			public static final String DELIVERYCOUNTRIES = "deliveryCountries".intern();
			public static final String DELIVERYMODES = "deliveryModes".intern();
			public static final String EXTERNALTAXENABLED = "externalTaxEnabled".intern();
			public static final String LANGUAGES = "languages".intern();
			public static final String MAXRADIUSFORPOSSEARCH = "maxRadiusForPoSSearch".intern();
			public static final String NET = "net".intern();
			public static final String PAYMENTPROVIDER = "paymentProvider".intern();
			public static final String PICKUPINSTOREMODE = "pickupInStoreMode".intern();
			public static final String SOLRFACETSEARCHCONFIGURATION = "solrFacetSearchConfiguration".intern();
			public static final String SUBMITORDERPROCESSCODE = "submitOrderProcessCode".intern();
			public static final String TAXGROUP = "taxGroup".intern();
			public static final String WAREHOUSES = "warehouses".intern();
		}
		public static class Cart
		{
			public static final String DESCRIPTION = "description".intern();
			public static final String EXPIRATIONTIME = "expirationTime".intern();
			public static final String NAME = "name".intern();
			public static final String SAVEDBY = "savedBy".intern();
			public static final String SAVETIME = "saveTime".intern();
		}
		public static class Consignment
		{
			public static final String DELIVERYPOINTOFSERVICE = "deliveryPointOfService".intern();
		}
		public static class Country
		{
			public static final String BASESTORES = "baseStores".intern();
		}
		public static class CreditCardPaymentInfo
		{
			public static final String ISSUENUMBER = "issueNumber".intern();
		}
		public static class Currency
		{
			public static final String BASESTORES = "baseStores".intern();
		}
		public static class Customer
		{
			public static final String CONTACTEMAIL = "contactEmail".intern();
			public static final String DEFAULTPAYMENTINFO = "defaultPaymentInfo".intern();
			public static final String ORIGINALUID = "originalUid".intern();
			public static final String TITLE = "title".intern();
			public static final String TOKEN = "token".intern();
			public static final String TYPE = "type".intern();
		}
		public static class DeliveryMode
		{
			public static final String STORES = "stores".intern();
		}
		public static class Language
		{
			public static final String BASESTORES = "baseStores".intern();
		}
		public static class Order
		{
			public static final String LANGUAGE = "language".intern();
			public static final String PLACEDBY = "placedBy".intern();
			public static final String SALESAPPLICATION = "salesApplication".intern();
		}
		public static class PaymentInfo
		{
			public static final String SAVED = "saved".intern();
		}
		public static class PointOfService
		{
			public static final String DISPLAYNAME = "displayName".intern();
			public static final String FEATURES = "features".intern();
			public static final String NEARBYSTORERADIUS = "nearbyStoreRadius".intern();
			public static final String STOREEMPLOYEEGROUPS = "storeEmployeeGroups".intern();
			public static final String WAREHOUSES = "warehouses".intern();
		}
		public static class Product
		{
			public static final String GALLERYIMAGES = "galleryImages".intern();
			public static final String SUMMARY = "summary".intern();
		}
		public static class Region
		{
			public static final String ISOCODESHORT = "isocodeShort".intern();
		}
		public static class ReturnRequest
		{
			public static final String REFUNDDELIVERYCOST = "refundDeliveryCost".intern();
		}
		public static class SolrIndexedProperty
		{
			public static final String CATEGORYFIELD = "categoryField".intern();
			public static final String CLASSATTRIBUTEASSIGNMENT = "classAttributeAssignment".intern();
			public static final String DISPLAYNAME = "displayName".intern();
			public static final String FACETSORT = "facetSort".intern();
			public static final String TOPVALUESPROVIDER = "topValuesProvider".intern();
			public static final String VISIBLE = "visible".intern();
		}
		public static class SolrIndexedType
		{
			public static final String SORTS = "sorts".intern();
		}
		public static class Warehouse
		{
			public static final String BASESTORES = "baseStores".intern();
			public static final String POINTSOFSERVICE = "pointsOfService".intern();
		}
	}
	public static class Enumerations
	{
		public static class ConsignmentStatus
		{
			public static final String READY_FOR_PICKUP = "READY_FOR_PICKUP".intern();
			public static final String PICKUP_COMPLETE = "PICKUP_COMPLETE".intern();
		}
		public static class CustomerType
		{
			public static final String GUEST = "GUEST".intern();
			public static final String REGISTERED = "REGISTERED".intern();
		}
		public static class OrderStatus
		{
			public static final String CHECKED_VALID = "CHECKED_VALID".intern();
			public static final String CHECKED_INVALID = "CHECKED_INVALID".intern();
			public static final String PAYMENT_AUTHORIZED = "PAYMENT_AUTHORIZED".intern();
			public static final String PAYMENT_NOT_AUTHORIZED = "PAYMENT_NOT_AUTHORIZED".intern();
			public static final String PAYMENT_AMOUNT_RESERVED = "PAYMENT_AMOUNT_RESERVED".intern();
			public static final String PAYMENT_AMOUNT_NOT_RESERVED = "PAYMENT_AMOUNT_NOT_RESERVED".intern();
			public static final String PAYMENT_CAPTURED = "PAYMENT_CAPTURED".intern();
			public static final String PAYMENT_NOT_CAPTURED = "PAYMENT_NOT_CAPTURED".intern();
			public static final String FRAUD_CHECKED = "FRAUD_CHECKED".intern();
			public static final String ORDER_SPLIT = "ORDER_SPLIT".intern();
			public static final String PROCESSING_ERROR = "PROCESSING_ERROR".intern();
			public static final String WAIT_FRAUD_MANUAL_CHECK = "WAIT_FRAUD_MANUAL_CHECK".intern();
		}
		public static class PaymentTransactionType
		{
			public static final String REVIEW_DECISION = "REVIEW_DECISION".intern();
		}
		public static class PickupInStoreMode
		{
			public static final String DISABLED = "DISABLED".intern();
			public static final String BUY_AND_COLLECT = "BUY_AND_COLLECT".intern();
			public static final String RESERVE_AND_COLLECT = "RESERVE_AND_COLLECT".intern();
		}
		public static class SalesApplication
		{
			public static final String WEB = "Web".intern();
			public static final String WEBMOBILE = "WebMobile".intern();
			public static final String CALLCENTER = "CallCenter".intern();
		}
		public static class SiteChannel
		{
			public static final String B2B = "B2B".intern();
			public static final String B2C = "B2C".intern();
		}
		public static class SiteTheme
		{
			// no values defined
		}
		public static class SolrIndexedPropertyFacetSort
		{
			public static final String COUNT = "Count".intern();
			public static final String ALPHA = "Alpha".intern();
			public static final String CUSTOM = "Custom".intern();
		}
		public static class StockLevelStatus
		{
			public static final String LOWSTOCK = "lowStock".intern();
		}
		public static class UiExperienceLevel
		{
			public static final String MOBILE = "Mobile".intern();
			public static final String DESKTOP = "Desktop".intern();
		}
		public static class WarehouseConsignmentState
		{
			public static final String COMPLETE = "COMPLETE".intern();
			public static final String CANCEL = "CANCEL".intern();
			public static final String PARTIAL = "PARTIAL".intern();
		}
	}
	public static class Relations
	{
		public static final String BASESTORE2COUNTRYREL = "BaseStore2CountryRel".intern();
		public static final String BASESTORE2CURRENCYREL = "BaseStore2CurrencyRel".intern();
		public static final String BASESTORE2DELIVERYMODEREL = "BaseStore2DeliveryModeRel".intern();
		public static final String BASESTORE2LANGUAGEREL = "BaseStore2LanguageRel".intern();
		public static final String BASESTORE2WAREHOUSEREL = "BaseStore2WarehouseRel".intern();
		public static final String POS2WAREHOUSEREL = "PoS2WarehouseRel".intern();
		public static final String PROMOTIONRESTRICTION2ORDERREL = "PromotionRestriction2OrderRel".intern();
		public static final String SOLRINDEXEDTYPE2SOLRSORTREL = "SolrIndexedType2SolrSortRel".intern();
		public static final String SOLRSORT2SOLRSORTFIELDREL = "SolrSort2SolrSortFieldRel".intern();
		public static final String STOREEMPLGROUP2POSREL = "StoreEmplGroup2POSRel".intern();
		public static final String STORELOCATION2STORELOCATORFEATURE = "StoreLocation2StoreLocatorFeature".intern();
	}
	
	protected GeneratedCommerceServicesConstants()
	{
		// private constructor
	}
	
	
}
