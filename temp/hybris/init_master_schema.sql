
CREATE CACHED TABLE abstractcontactinfo
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_userpos INTEGER,
    p_user BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_phonenumber NVARCHAR(255),
    p_type BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX userRelIDX_26 ON abstractcontactinfo (p_user);

CREATE INDEX userposPosIDX_26 ON abstractcontactinfo (p_userpos);


CREATE CACHED TABLE abstractlinkentries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_area BIGINT,
    p_sortorder INTEGER,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_height INTEGER,
    p_spacer TINYINT,
    p_descriptionicon BIGINT,
    p_url NVARCHAR(255),
    p_extensionname NVARCHAR(255),
    p_parentlinkpos INTEGER,
    p_parentlink BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX code_6002 ON abstractlinkentries (p_code);

CREATE INDEX area_6002 ON abstractlinkentries (p_area);

CREATE INDEX parentlinkRelIDX_6002 ON abstractlinkentries (p_parentlink);

CREATE INDEX parentlinkposPosIDX_6002 ON abstractlinkentries (p_parentlinkpos);


CREATE CACHED TABLE abstractlinkentrieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_title NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE abstrcfgproductinfo
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_productinfostatus BIGINT,
    p_configuratortype BIGINT,
    p_orderentrypos INTEGER,
    p_orderentry BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_configurationlabel NVARCHAR(255),
    p_configurationvalue NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX orderentryRelIDX_131 ON abstrcfgproductinfo (p_orderentry);

CREATE INDEX orderentryposPosIDX_131 ON abstrcfgproductinfo (p_orderentrypos);


CREATE CACHED TABLE aclentries
(
    hjmpTS BIGINT,
    PermissionPK BIGINT NOT NULL,
    Negative TINYINT DEFAULT 0,
    PrincipalPK BIGINT NOT NULL,
    ItemPK BIGINT NOT NULL,
    PRIMARY KEY (PermissionPK, PrincipalPK, ItemPK)
);

CREATE INDEX aclcheckindex_aclentries ON aclentries (ItemPK, PrincipalPK);

CREATE INDEX aclupdateindex_aclentries ON aclentries (ItemPK);


CREATE CACHED TABLE actions4comps
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6226 ON actions4comps (SourcePK);

CREATE INDEX qualifier_6226 ON actions4comps (Qualifier);

CREATE INDEX seqnr_6226 ON actions4comps (SequenceNumber);

CREATE INDEX rseqnr_6226 ON actions4comps (RSequenceNumber);

CREATE INDEX linktarget_6226 ON actions4comps (TargetPK);


CREATE CACHED TABLE addresses
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_original BIGINT,
    p_duplicate TINYINT,
    p_appartment NVARCHAR(255),
    p_building NVARCHAR(255),
    p_cellphone NVARCHAR(255),
    p_company NVARCHAR(255),
    p_country BIGINT,
    p_department NVARCHAR(255),
    p_district NVARCHAR(255),
    p_email NVARCHAR(255),
    p_fax NVARCHAR(255),
    p_firstname NVARCHAR(255),
    p_lastname NVARCHAR(255),
    p_middlename NVARCHAR(255),
    p_middlename2 NVARCHAR(255),
    p_phone1 NVARCHAR(255),
    p_phone2 NVARCHAR(255),
    p_pobox NVARCHAR(255),
    p_postalcode NVARCHAR(255),
    p_region BIGINT,
    p_streetname NVARCHAR(255),
    p_streetnumber NVARCHAR(255),
    p_title BIGINT,
    p_town NVARCHAR(255),
    p_gender BIGINT,
    p_dateofbirth TIMESTAMP,
    p_remarks NVARCHAR(255),
    p_url NVARCHAR(255),
    p_shippingaddress TINYINT,
    p_unloadingaddress TINYINT,
    p_billingaddress TINYINT,
    p_contactaddress TINYINT,
    p_visibleinaddressbook TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX testindex_23 ON addresses (p_email);

CREATE INDEX Address_Owner_23 ON addresses (OwnerPkString);

CREATE INDEX countryPK_idx_23 ON addresses (p_country);


CREATE CACHED TABLE addressprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT NOT NULL,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255) NOT NULL,
    LANGPK BIGINT NOT NULL,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX nameidx_addressprops ON addressprops (NAME);

CREATE INDEX itempk_addressprops ON addressprops (ITEMPK);

CREATE INDEX realnameidx_addressprops ON addressprops (REALNAME);


CREATE CACHED TABLE agent2basestore
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_9006 ON agent2basestore (SourcePK);

CREATE INDEX qualifier_9006 ON agent2basestore (Qualifier);

CREATE INDEX seqnr_9006 ON agent2basestore (SequenceNumber);

CREATE INDEX rseqnr_9006 ON agent2basestore (RSequenceNumber);

CREATE INDEX linktarget_9006 ON agent2basestore (TargetPK);


CREATE CACHED TABLE agreements
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_id NVARCHAR(255),
    p_startdate TIMESTAMP,
    p_enddate TIMESTAMP,
    p_catalogversion BIGINT,
    p_catalog BIGINT,
    p_buyer BIGINT,
    p_supplier BIGINT,
    p_buyercontact BIGINT,
    p_suppliercontact BIGINT,
    p_currency BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE applactntyp4comptyp
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6227 ON applactntyp4comptyp (SourcePK);

CREATE INDEX qualifier_6227 ON applactntyp4comptyp (Qualifier);

CREATE INDEX seqnr_6227 ON applactntyp4comptyp (SequenceNumber);

CREATE INDEX rseqnr_6227 ON applactntyp4comptyp (RSequenceNumber);

CREATE INDEX linktarget_6227 ON applactntyp4comptyp (TargetPK);


CREATE CACHED TABLE applrestrtyp4pgtyp
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1099 ON applrestrtyp4pgtyp (SourcePK);

CREATE INDEX qualifier_1099 ON applrestrtyp4pgtyp (Qualifier);

CREATE INDEX seqnr_1099 ON applrestrtyp4pgtyp (SequenceNumber);

CREATE INDEX rseqnr_1099 ON applrestrtyp4pgtyp (RSequenceNumber);

CREATE INDEX linktarget_1099 ON applrestrtyp4pgtyp (TargetPK);


CREATE CACHED TABLE atomictypes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_extensionname NVARCHAR(255),
    p_autocreate TINYINT,
    p_generate TINYINT,
    InternalCode NVARCHAR(255),
    p_defaultvalue LONGVARBINARY,
    InheritancePathString LONGVARCHAR,
    JavaClassName NVARCHAR(255),
    SuperTypePK BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    InternalCodeLowerCase NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX typecode_81 ON atomictypes (InternalCode);

CREATE INDEX typecodelowercase_81 ON atomictypes (InternalCodeLowerCase);

CREATE INDEX inheritpsi_81 ON atomictypes (InheritancePathString);


CREATE CACHED TABLE atomictypeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE attr2valuerel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_value BIGINT,
    p_attributeassignment BIGINT,
    p_attribute BIGINT,
    p_systemversion BIGINT,
    p_position INTEGER,
    p_externalid NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX sysVer_609 ON attr2valuerel (p_systemversion);

CREATE INDEX catRelIDX_609 ON attr2valuerel (p_attributeassignment);

CREATE INDEX idIDX_609 ON attr2valuerel (p_externalid);

CREATE INDEX valIDX_609 ON attr2valuerel (p_value);

CREATE INDEX attrIDX_609 ON attr2valuerel (p_attribute);


CREATE CACHED TABLE attributedescriptors
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_extensionname NVARCHAR(255),
    p_autocreate TINYINT,
    p_generate TINYINT,
    QualifierInternal NVARCHAR(255),
    AttributeTypePK BIGINT,
    columnName NVARCHAR(255),
    p_defaultvalue LONGVARBINARY,
    p_defaultvaluedefinitionstring NVARCHAR(255),
    EnclosingTypePK BIGINT,
    PersistenceQualifierInternal NVARCHAR(255),
    PersistenceTypePK BIGINT,
    p_attributehandler NVARCHAR(255),
    SelectionDescriptorPK BIGINT,
    modifiers INTEGER DEFAULT 0,
    p_unique TINYINT,
    p_hiddenforui TINYINT,
    p_readonlyforui TINYINT,
    p_dontcopy TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    QualifierLowerCaseInternal NVARCHAR(255),
    isHidden TINYINT DEFAULT 0,
    isProperty TINYINT DEFAULT 0,
    SuperAttributeDescriptorPK BIGINT,
    InheritancePathString LONGVARCHAR,
    p_externalqualifier NVARCHAR(255),
    p_storeindatabase TINYINT,
    p_needrestart TINYINT,
    p_param TINYINT,
    p_position INTEGER,
    p_defaultvalueexpression NVARCHAR(255),
    p_issource TINYINT,
    p_ordered TINYINT,
    p_relationname NVARCHAR(255),
    p_relationtype BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX lcqualifier_87 ON attributedescriptors (QualifierLowerCaseInternal);

CREATE INDEX inheritps_87 ON attributedescriptors (InheritancePathString);

CREATE INDEX enclosing_87 ON attributedescriptors (EnclosingTypePK);

CREATE UNIQUE INDEX qualifier_87 ON attributedescriptors (QualifierInternal, EnclosingTypePK);


CREATE CACHED TABLE attributedescriptorslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE backofficesavedquery
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_queryowner BIGINT,
    p_typecode NVARCHAR(255),
    p_includesubtypes TINYINT,
    p_globaloperatorcode NVARCHAR(255),
    p_sortattribute NVARCHAR(255),
    p_sortasc TINYINT,
    p_tokenizable TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE backofficesavedquerylp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE backofficesearchcond
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_attribute NVARCHAR(255),
    p_value NVARCHAR(255),
    p_valuereference BIGINT,
    p_languagecode NVARCHAR(255),
    p_operatorcode NVARCHAR(255),
    p_selected TINYINT,
    p_editor NVARCHAR(255),
    p_editorparameters LONGVARBINARY,
    p_sortable TINYINT,
    p_disabled TINYINT,
    p_mandatory TINYINT,
    p_savedquery BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX savedqueryRelIDX_6401 ON backofficesearchcond (p_savedquery);


CREATE CACHED TABLE bannforrotcomprels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_3000 ON bannforrotcomprels (SourcePK);

CREATE INDEX qualifier_3000 ON bannforrotcomprels (Qualifier);

CREATE INDEX seqnr_3000 ON bannforrotcomprels (SequenceNumber);

CREATE INDEX rseqnr_3000 ON bannforrotcomprels (RSequenceNumber);

CREATE INDEX linktarget_3000 ON bannforrotcomprels (TargetPK);


CREATE CACHED TABLE basestore
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_storelocatordistanceunit BIGINT,
    p_net TINYINT,
    p_taxgroup BIGINT,
    p_defaultlanguage BIGINT,
    p_defaultcurrency BIGINT,
    p_defaultdeliveryorigin BIGINT,
    p_solrfacetsearchconfiguration BIGINT,
    p_submitorderprocesscode NVARCHAR(255),
    p_createreturnprocesscode NVARCHAR(255),
    p_externaltaxenabled TINYINT,
    p_pickupinstoremode BIGINT,
    p_maxradiusforpossearch DOUBLE,
    p_customerallowedtoignoresugge TINYINT,
    p_paymentprovider NVARCHAR(255),
    p_expresscheckoutenabled TINYINT,
    p_taxestimationenabled TINYINT,
    p_checkoutflowgroup NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE basestore2countryrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6213 ON basestore2countryrel (SourcePK);

CREATE INDEX qualifier_6213 ON basestore2countryrel (Qualifier);

CREATE INDEX seqnr_6213 ON basestore2countryrel (SequenceNumber);

CREATE INDEX rseqnr_6213 ON basestore2countryrel (RSequenceNumber);

CREATE INDEX linktarget_6213 ON basestore2countryrel (TargetPK);


CREATE CACHED TABLE basestore2currencyrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6201 ON basestore2currencyrel (SourcePK);

CREATE INDEX qualifier_6201 ON basestore2currencyrel (Qualifier);

CREATE INDEX seqnr_6201 ON basestore2currencyrel (SequenceNumber);

CREATE INDEX rseqnr_6201 ON basestore2currencyrel (RSequenceNumber);

CREATE INDEX linktarget_6201 ON basestore2currencyrel (TargetPK);


CREATE CACHED TABLE basestore2delmoderel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6220 ON basestore2delmoderel (SourcePK);

CREATE INDEX qualifier_6220 ON basestore2delmoderel (Qualifier);

CREATE INDEX seqnr_6220 ON basestore2delmoderel (SequenceNumber);

CREATE INDEX rseqnr_6220 ON basestore2delmoderel (RSequenceNumber);

CREATE INDEX linktarget_6220 ON basestore2delmoderel (TargetPK);


CREATE CACHED TABLE basestore2languagerel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6212 ON basestore2languagerel (SourcePK);

CREATE INDEX qualifier_6212 ON basestore2languagerel (Qualifier);

CREATE INDEX seqnr_6212 ON basestore2languagerel (SequenceNumber);

CREATE INDEX rseqnr_6212 ON basestore2languagerel (RSequenceNumber);

CREATE INDEX linktarget_6212 ON basestore2languagerel (TargetPK);


CREATE CACHED TABLE basestore2warehouserel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6215 ON basestore2warehouserel (SourcePK);

CREATE INDEX qualifier_6215 ON basestore2warehouserel (Qualifier);

CREATE INDEX seqnr_6215 ON basestore2warehouserel (SequenceNumber);

CREATE INDEX rseqnr_6215 ON basestore2warehouserel (RSequenceNumber);

CREATE INDEX linktarget_6215 ON basestore2warehouserel (TargetPK);


CREATE CACHED TABLE basestorelp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE bruteforceloginattempts
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_attempts INTEGER,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX uidX_9450 ON bruteforceloginattempts (p_uid);


CREATE CACHED TABLE cartentries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_baseprice DECIMAL(30,8),
    p_calculated TINYINT,
    p_discountvaluesinternal LONGVARCHAR,
    p_entrynumber INTEGER,
    p_info LONGVARCHAR,
    p_product BIGINT,
    p_quantity DECIMAL(30,8),
    p_taxvaluesinternal NVARCHAR(255),
    p_totalprice DECIMAL(30,8),
    p_unit BIGINT,
    p_giveaway TINYINT,
    p_rejected TINYINT,
    p_order BIGINT,
    p_chosenvendor BIGINT,
    p_deliveryaddress BIGINT,
    p_deliverymode BIGINT,
    p_nameddeliverydate TIMESTAMP,
    p_quantitystatus BIGINT,
    p_deliverypointofservice BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX oeProd_44 ON cartentries (p_product);

CREATE INDEX oeOrd_44 ON cartentries (p_order);


CREATE CACHED TABLE carts
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_calculated TINYINT,
    p_code NVARCHAR(255),
    p_currency BIGINT,
    p_deliveryaddress BIGINT,
    p_deliverycost DECIMAL(30,8),
    p_deliverymode BIGINT,
    p_deliverystatus BIGINT,
    p_globaldiscountvaluesinternal LONGVARCHAR,
    p_net TINYINT,
    p_paymentaddress BIGINT,
    p_paymentcost DECIMAL(30,8),
    p_paymentinfo BIGINT,
    p_paymentmode BIGINT,
    p_paymentstatus BIGINT,
    p_status BIGINT,
    p_exportstatus BIGINT,
    p_statusinfo NVARCHAR(255),
    p_totalprice DECIMAL(30,8),
    p_totaldiscounts DECIMAL(30,8),
    p_totaltax DECIMAL(30,8),
    p_totaltaxvaluesinternal LONGVARCHAR,
    p_user BIGINT,
    p_subtotal DECIMAL(30,8),
    p_discountsincludedeliverycost TINYINT,
    p_discountsincludepaymentcost TINYINT,
    p_previousdeliverymode BIGINT,
    p_site BIGINT,
    p_store BIGINT,
    p_guid NVARCHAR(255),
    p_appliedcouponcodes LONGVARBINARY,
    p_sessionid NVARCHAR(255),
    p_expirationtime TIMESTAMP,
    p_savetime TIMESTAMP,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    p_savedby BIGINT,
    p_importstatus BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX OrderCode_43 ON carts (p_code);

CREATE INDEX OrderUser_43 ON carts (p_user);

CREATE INDEX guidIndex_43 ON carts (p_guid);

CREATE INDEX cartSelectionIndex_43 ON carts (p_user, p_site, modifiedTS);


CREATE CACHED TABLE cat2attrrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_classificationclass BIGINT,
    p_classificationattribute BIGINT,
    p_systemversion BIGINT,
    p_position INTEGER,
    p_externalid NVARCHAR(255),
    p_unit BIGINT,
    p_mandatory TINYINT,
    p_localized TINYINT,
    p_range TINYINT,
    p_multivalued TINYINT,
    p_searchable TINYINT,
    p_attributetype BIGINT,
    p_formatdefinition NVARCHAR(255),
    p_listable TINYINT,
    p_comparable TINYINT,
    p_visibility BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX sysVer_610 ON cat2attrrel (p_systemversion);

CREATE INDEX relSrcIDX_610 ON cat2attrrel (p_classificationclass);

CREATE INDEX idIDX_610 ON cat2attrrel (p_externalid);

CREATE INDEX relTgtIDX_610 ON cat2attrrel (p_classificationattribute);

CREATE INDEX posIdx_610 ON cat2attrrel (p_position);


CREATE CACHED TABLE cat2attrrellp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cat2catrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_144 ON cat2catrel (SourcePK);

CREATE INDEX qualifier_144 ON cat2catrel (Qualifier);

CREATE INDEX seqnr_144 ON cat2catrel (SequenceNumber);

CREATE INDEX rseqnr_144 ON cat2catrel (RSequenceNumber);

CREATE INDEX linktarget_144 ON cat2catrel (TargetPK);


CREATE CACHED TABLE cat2keywordrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_605 ON cat2keywordrel (SourcePK);

CREATE INDEX qualifier_605 ON cat2keywordrel (Qualifier);

CREATE INDEX seqnr_605 ON cat2keywordrel (SequenceNumber);

CREATE INDEX rseqnr_605 ON cat2keywordrel (RSequenceNumber);

CREATE INDEX linktarget_605 ON cat2keywordrel (TargetPK);


CREATE CACHED TABLE cat2medrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_145 ON cat2medrel (SourcePK);

CREATE INDEX qualifier_145 ON cat2medrel (Qualifier);

CREATE INDEX seqnr_145 ON cat2medrel (SequenceNumber);

CREATE INDEX rseqnr_145 ON cat2medrel (RSequenceNumber);

CREATE INDEX linktarget_145 ON cat2medrel (TargetPK);


CREATE CACHED TABLE cat2princrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_613 ON cat2princrel (SourcePK);

CREATE INDEX qualifier_613 ON cat2princrel (Qualifier);

CREATE INDEX seqnr_613 ON cat2princrel (SequenceNumber);

CREATE INDEX rseqnr_613 ON cat2princrel (RSequenceNumber);

CREATE INDEX linktarget_613 ON cat2princrel (TargetPK);


CREATE CACHED TABLE cat2prodrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_143 ON cat2prodrel (SourcePK);

CREATE INDEX qualifier_143 ON cat2prodrel (Qualifier);

CREATE INDEX seqnr_143 ON cat2prodrel (SequenceNumber);

CREATE INDEX rseqnr_143 ON cat2prodrel (RSequenceNumber);

CREATE INDEX linktarget_143 ON cat2prodrel (TargetPK);


CREATE CACHED TABLE catalogs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_id varchar(200),
    p_activecatalogversion BIGINT,
    p_defaultcatalog TINYINT,
    p_supplier BIGINT,
    p_buyer BIGINT,
    p_previewurltemplate NVARCHAR(255),
    p_urlpatterns LONGVARBINARY,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX idIdx_600 ON catalogs (p_id);


CREATE CACHED TABLE catalogs4basestores
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_2012 ON catalogs4basestores (SourcePK);

CREATE INDEX qualifier_2012 ON catalogs4basestores (Qualifier);

CREATE INDEX seqnr_2012 ON catalogs4basestores (SequenceNumber);

CREATE INDEX rseqnr_2012 ON catalogs4basestores (RSequenceNumber);

CREATE INDEX linktarget_2012 ON catalogs4basestores (TargetPK);


CREATE CACHED TABLE catalogs4restriction
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1079 ON catalogs4restriction (SourcePK);

CREATE INDEX qualifier_1079 ON catalogs4restriction (Qualifier);

CREATE INDEX seqnr_1079 ON catalogs4restriction (SequenceNumber);

CREATE INDEX rseqnr_1079 ON catalogs4restriction (RSequenceNumber);

CREATE INDEX linktarget_1079 ON catalogs4restriction (TargetPK);


CREATE CACHED TABLE catalogs4site
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1063 ON catalogs4site (SourcePK);

CREATE INDEX qualifier_1063 ON catalogs4site (Qualifier);

CREATE INDEX seqnr_1063 ON catalogs4site (SequenceNumber);

CREATE INDEX rseqnr_1063 ON catalogs4site (RSequenceNumber);

CREATE INDEX linktarget_1063 ON catalogs4site (TargetPK);


CREATE CACHED TABLE catalogslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE catalogversions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_active TINYINT,
    p_version NVARCHAR(255),
    p_mimerootdirectory NVARCHAR(255),
    p_generationdate TIMESTAMP,
    p_defaultcurrency BIGINT,
    p_inclfreight TINYINT,
    p_inclpacking TINYINT,
    p_inclassurance TINYINT,
    p_inclduty TINYINT,
    p_territories LONGVARCHAR,
    p_languages LONGVARCHAR,
    p_generatorinfo NVARCHAR(255),
    p_categorysystemid NVARCHAR(255),
    p_previousupdateversion INTEGER,
    p_catalog BIGINT,
    p_mnemonic NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX versionIDX_601 ON catalogversions (p_version);

CREATE INDEX catalogIDX_601 ON catalogversions (p_catalog);

CREATE INDEX visibleIDX_601 ON catalogversions (p_active);


CREATE CACHED TABLE catalogversionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_categorysystemname NVARCHAR(255),
    p_categorysystemdescription NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE catalogversionsyncjob
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_624 ON catalogversionsyncjob (SourcePK);

CREATE INDEX qualifier_624 ON catalogversionsyncjob (Qualifier);

CREATE INDEX seqnr_624 ON catalogversionsyncjob (SequenceNumber);

CREATE INDEX rseqnr_624 ON catalogversionsyncjob (RSequenceNumber);

CREATE INDEX linktarget_624 ON catalogversionsyncjob (TargetPK);


CREATE CACHED TABLE categories
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_order INTEGER,
    p_catalog BIGINT,
    p_catalogversion BIGINT,
    p_normal LONGVARCHAR,
    p_thumbnails LONGVARCHAR,
    p_detail LONGVARCHAR,
    p_logo LONGVARCHAR,
    p_data_sheet LONGVARCHAR,
    p_others LONGVARCHAR,
    p_thumbnail BIGINT,
    p_picture BIGINT,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_externalid NVARCHAR(255),
    p_revision NVARCHAR(255),
    p_showemptyattributes TINYINT,
    p_hasimage TINYINT,
    p_sequence INTEGER,
    PRIMARY KEY (PK)
);

CREATE INDEX codeIDX_142 ON categories (p_code);

CREATE INDEX versionIDX_142 ON categories (p_catalogversion);

CREATE UNIQUE INDEX codeVersionIDX_142 ON categories (p_code, p_catalogversion);

CREATE INDEX extID_142 ON categories (p_externalid);


CREATE CACHED TABLE categories4restriction
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1080 ON categories4restriction (SourcePK);

CREATE INDEX qualifier_1080 ON categories4restriction (Qualifier);

CREATE INDEX seqnr_1080 ON categories4restriction (SequenceNumber);

CREATE INDEX rseqnr_1080 ON categories4restriction (RSequenceNumber);

CREATE INDEX linktarget_1080 ON categories4restriction (TargetPK);


CREATE CACHED TABLE categorieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description LONGVARCHAR,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE catforpromosourcerule
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_categorycode NVARCHAR(255),
    p_rule BIGINT,
    p_promotion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX CatForPromoSourceRule_IDX_5403 ON catforpromosourcerule (p_categorycode, p_rule);


CREATE CACHED TABLE catpromrelations
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_5019 ON catpromrelations (SourcePK);

CREATE INDEX qualifier_5019 ON catpromrelations (Qualifier);

CREATE INDEX seqnr_5019 ON catpromrelations (SequenceNumber);

CREATE INDEX rseqnr_5019 ON catpromrelations (RSequenceNumber);

CREATE INDEX linktarget_5019 ON catpromrelations (TargetPK);


CREATE CACHED TABLE catsforprodcarcomprels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_3003 ON catsforprodcarcomprels (SourcePK);

CREATE INDEX qualifier_3003 ON catsforprodcarcomprels (Qualifier);

CREATE INDEX seqnr_3003 ON catsforprodcarcomprels (SequenceNumber);

CREATE INDEX rseqnr_3003 ON catsforprodcarcomprels (RSequenceNumber);

CREATE INDEX linktarget_3003 ON catsforprodcarcomprels (TargetPK);


CREATE CACHED TABLE catverdiffs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_sourceversion BIGINT,
    p_targetversion BIGINT,
    p_cronjob BIGINT,
    p_differencetext LONGVARCHAR,
    p_differencevalue DOUBLE,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_sourceproduct BIGINT,
    p_targetproduct BIGINT,
    p_mode BIGINT,
    p_sourcecategory BIGINT,
    p_targetcategory BIGINT,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE ccpaysubvalidation
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_subscriptionid NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE changedescriptors
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_cronjob BIGINT,
    p_step BIGINT,
    p_changeditem BIGINT,
    p_sequencenumber INTEGER,
    p_savetimestamp TIMESTAMP,
    p_changetype NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_targetitem BIGINT,
    p_done TINYINT,
    p_copiedimplicitely TINYINT,
    PRIMARY KEY (PK)
);

CREATE INDEX cronjobIDX_505 ON changedescriptors (p_cronjob);

CREATE INDEX stepIDX_505 ON changedescriptors (p_step);

CREATE INDEX changedItemIDX_505 ON changedescriptors (p_changeditem);

CREATE INDEX seqNrIDX_505 ON changedescriptors (p_sequencenumber);

CREATE INDEX doneIDX_505 ON changedescriptors (p_done);


CREATE CACHED TABLE classattrvalues
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_externalid NVARCHAR(255),
    p_systemversion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX sysVer_608 ON classattrvalues (p_systemversion);

CREATE INDEX code_608 ON classattrvalues (p_code);

CREATE INDEX idIDX_608 ON classattrvalues (p_externalid);


CREATE CACHED TABLE classattrvalueslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE classificationattrs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_systemversion BIGINT,
    p_code NVARCHAR(255),
    p_externalid NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX sysVer_607 ON classificationattrs (p_systemversion);

CREATE INDEX code_607 ON classificationattrs (p_code);

CREATE INDEX idIDX_607 ON classificationattrs (p_externalid);


CREATE CACHED TABLE classificationattrslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE clattrunt
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_systemversion BIGINT,
    p_code NVARCHAR(255),
    p_externalid NVARCHAR(255),
    p_symbol NVARCHAR(255),
    p_unittype NVARCHAR(255),
    p_conversionfactor DOUBLE,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX sysVer_612 ON clattrunt (p_systemversion);

CREATE INDEX codeIdx_612 ON clattrunt (p_code);

CREATE INDEX extID_612 ON clattrunt (p_externalid);


CREATE CACHED TABLE clattruntlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cmptype2covgrprels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_978 ON cmptype2covgrprels (SourcePK);

CREATE INDEX qualifier_978 ON cmptype2covgrprels (Qualifier);

CREATE INDEX seqnr_978 ON cmptype2covgrprels (SequenceNumber);

CREATE INDEX rseqnr_978 ON cmptype2covgrprels (RSequenceNumber);

CREATE INDEX linktarget_978 ON cmptype2covgrprels (TargetPK);


CREATE CACHED TABLE cmscomponent
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_name NVARCHAR(255),
    p_catalogversion BIGINT,
    p_visible TINYINT,
    p_onlyonerestrictionmustapply TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_scope BIGINT,
    p_defaultcmscomponent BIGINT,
    p_external TINYINT,
    p_url NVARCHAR(255),
    p_target BIGINT,
    p_contentpagepos INTEGER,
    p_contentpage BIGINT,
    p_productpos INTEGER,
    p_product BIGINT,
    p_categorypos INTEGER,
    p_category BIGINT,
    p_styleattributes NVARCHAR(255),
    p_urllink NVARCHAR(255),
    p_page BIGINT,
    p_play TINYINT,
    p_loop TINYINT,
    p_menu TINYINT,
    p_quality BIGINT,
    p_scale BIGINT,
    p_wmode BIGINT,
    p_salign BIGINT,
    p_bgcolor NVARCHAR(255),
    p_width INTEGER,
    p_height INTEGER,
    p_mediacodepattern NVARCHAR(255),
    p_scroll BIGINT,
    p_searchquery NVARCHAR(255),
    p_categorycode NVARCHAR(255),
    p_popup TINYINT,
    p_timeout INTEGER,
    p_effect BIGINT,
    p_productsfromcontext TINYINT,
    p_pagination TINYINT,
    p_layout BIGINT,
    p_productreferencetypes LONGVARCHAR,
    p_maximumnumberproducts INTEGER,
    p_displayproducttitles TINYINT,
    p_displayproductprices TINYINT,
    p_filterpurchased TINYINT,
    p_totaldisplay BIGINT,
    p_shownproductcount INTEGER,
    p_lightboxbannercomponent BIGINT,
    p_dropdownlayout BIGINT,
    p_navigationnode BIGINT,
    p_wrapafter INTEGER,
    p_link BIGINT,
    p_styleclass NVARCHAR(255),
    p_components LONGVARCHAR,
    p_navigationnodes LONGVARCHAR,
    p_showlanguagecurrency TINYINT,
    p_displaysuggestions TINYINT,
    p_displayproducts TINYINT,
    p_displayproductimages TINYINT,
    p_maxsuggestions INTEGER,
    p_maxproducts INTEGER,
    p_mincharactersbeforerequest INTEGER,
    p_waittimebeforerequest INTEGER,
    p_pag0 NVARCHAR(255),
    p_logo BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX componentIDX_1084 ON cmscomponent (p_uid, p_catalogversion);

CREATE INDEX contentpageRelIDX_1084 ON cmscomponent (p_contentpage);

CREATE INDEX contentpageposPosIDX_1084 ON cmscomponent (p_contentpagepos);

CREATE INDEX productRelIDX_1084 ON cmscomponent (p_product);

CREATE INDEX productposPosIDX_1084 ON cmscomponent (p_productpos);

CREATE INDEX categoryRelIDX_1084 ON cmscomponent (p_category);

CREATE INDEX categoryposPosIDX_1084 ON cmscomponent (p_categorypos);

CREATE INDEX pageRelIDX_1084 ON cmscomponent (p_page);


CREATE CACHED TABLE cmscomponentlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_title NVARCHAR(255),
    p_media BIGINT,
    p_linkname NVARCHAR(255),
    p_headline NVARCHAR(255),
    p_content LONGVARCHAR,
    p_imagemaphtml LONGVARCHAR,
    p_searchquery NVARCHAR(255),
    p_notice NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cmslinksfornavnodes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1089 ON cmslinksfornavnodes (SourcePK);

CREATE INDEX qualifier_1089 ON cmslinksfornavnodes (Qualifier);

CREATE INDEX seqnr_1089 ON cmslinksfornavnodes (SequenceNumber);

CREATE INDEX rseqnr_1089 ON cmslinksfornavnodes (RSequenceNumber);

CREATE INDEX linktarget_1089 ON cmslinksfornavnodes (TargetPK);


CREATE CACHED TABLE cmsnavigationentry
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_name NVARCHAR(255),
    p_catalogversion BIGINT,
    p_item BIGINT,
    p_navigationnodepos INTEGER,
    p_navigationnode BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX cmsNavEntryItemIdx_1096 ON cmsnavigationentry (p_item);

CREATE INDEX navigationnodeRelIDX_1096 ON cmsnavigationentry (p_navigationnode);

CREATE INDEX navigationnodeposPosIDX_1096 ON cmsnavigationentry (p_navigationnodepos);


CREATE CACHED TABLE cmsnavigationnode
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_name NVARCHAR(255),
    p_catalogversion BIGINT,
    p_visible TINYINT,
    p_parentpos INTEGER,
    p_parent BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX parentRelIDX_1088 ON cmsnavigationnode (p_parent);

CREATE INDEX parentposPosIDX_1088 ON cmsnavigationnode (p_parentpos);


CREATE CACHED TABLE cmsnavigationnodelp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_title NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cmspage
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_name NVARCHAR(255),
    p_catalogversion BIGINT,
    p_approvalstatus BIGINT,
    p_mastertemplate BIGINT,
    p_defaultpage TINYINT,
    p_onlyonerestrictionmustapply TINYINT,
    p_previewimage BIGINT,
    p_lockedby BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_label NVARCHAR(255),
    p_homepage TINYINT,
    PRIMARY KEY (PK)
);

CREATE INDEX EmailPageMasterTpl_1072 ON cmspage (p_mastertemplate);

CREATE INDEX lockedbyRelIDX_1072 ON cmspage (p_lockedby);


CREATE CACHED TABLE cmspagelp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_title NVARCHAR(255),
    p_keywords NVARCHAR(255),
    p_description NVARCHAR(255),
    p_fromemail NVARCHAR(255),
    p_fromname NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cmspagesfornavnodes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1090 ON cmspagesfornavnodes (SourcePK);

CREATE INDEX qualifier_1090 ON cmspagesfornavnodes (Qualifier);

CREATE INDEX seqnr_1090 ON cmspagesfornavnodes (SequenceNumber);

CREATE INDEX rseqnr_1090 ON cmspagesfornavnodes (RSequenceNumber);

CREATE INDEX linktarget_1090 ON cmspagesfornavnodes (TargetPK);


CREATE CACHED TABLE cmspreviewticket
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_id NVARCHAR(255),
    p_previewdata BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE cmsrelations
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_catalogversion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE cmssite
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_theme BIGINT,
    p_defaultlanguage BIGINT,
    p_channel BIGINT,
    p_defaultpromotiongroup BIGINT,
    p_solrfacetsearchconfiguration BIGINT,
    p_cartremovalage INTEGER,
    p_anonymouscartremovalage INTEGER,
    p_cartremovalcronjob BIGINT,
    p_uncollectedorderscronjob BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_urlpatterns LONGVARBINARY,
    p_active TINYINT,
    p_activefrom TIMESTAMP,
    p_activeuntil TIMESTAMP,
    p_defaultcatalog BIGINT,
    p_startingpage BIGINT,
    p_redirecturl NVARCHAR(255),
    p_previewurl NVARCHAR(255),
    p_openpreviewinnewtab TINYINT,
    p_defaultpreviewcategory BIGINT,
    p_defaultpreviewproduct BIGINT,
    p_defaultpreviewcatalog BIGINT,
    p_urlencodingattributes LONGVARBINARY,
    p_sitemaps LONGVARCHAR,
    p_sitemapconfig BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX cartremovalcronjobRelIDX_1064 ON cmssite (p_cartremovalcronjob);

CREATE INDEX uncollectedorderscronjobRelIDX_1064 ON cmssite (p_uncollectedorderscronjob);


CREATE CACHED TABLE cmssitelp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_locale NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cockpitcollections
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_qualifier NVARCHAR(255),
    p_user BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_collectiontype BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX userRelIDX_1700 ON cockpitcollections (p_user);


CREATE CACHED TABLE cockpitcollectionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_label NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cockpitcollelements
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_objecttypecode NVARCHAR(255),
    p_collection BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX collectionRelIDX_1701 ON cockpitcollelements (p_collection);


CREATE CACHED TABLE cockpitcollitemrefs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_objecttypecode NVARCHAR(255),
    p_collection BIGINT,
    p_item BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX itemIDX_1702 ON cockpitcollitemrefs (p_item);

CREATE INDEX collectionRelIDX_1702 ON cockpitcollitemrefs (p_collection);


CREATE CACHED TABLE cockpitcompaccessrights
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE cockpitcompconfigs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_factorybean NVARCHAR(255),
    p_code NVARCHAR(255),
    p_objecttemplatecode NVARCHAR(255),
    p_media BIGINT,
    p_principal BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX principalRelIDX_1707 ON cockpitcompconfigs (p_principal);


CREATE CACHED TABLE cockpitfavcategories
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_qualifier NVARCHAR(255),
    p_user BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX userRelIDX_1718 ON cockpitfavcategories (p_user);


CREATE CACHED TABLE cockpitfavcategorieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_label NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cockpititemtemplates
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_relatedtype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX relatedtypeRelIDX_24242 ON cockpititemtemplates (p_relatedtype);


CREATE CACHED TABLE cockpititemtemplateslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cockpitsavedfacvalues
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_facetqualifier NVARCHAR(255),
    p_valuequalifier NVARCHAR(255),
    p_cockpitsavedquery BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX cockpitsavedqueryRelIDX_1704 ON cockpitsavedfacvalues (p_cockpitsavedquery);


CREATE CACHED TABLE cockpitsavedparamvals
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_rawvalue LONGVARCHAR,
    p_operatorqualifier NVARCHAR(255),
    p_languageiso NVARCHAR(255),
    p_parameterqualifier NVARCHAR(255),
    p_casesensitive TINYINT,
    p_reference TINYINT,
    p_cockpitsavedquery BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX cockpitsavedqueryRelIDX_1706 ON cockpitsavedparamvals (p_cockpitsavedquery);


CREATE CACHED TABLE cockpitsavedqueries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_label NVARCHAR(255),
    p_simpletext NVARCHAR(255),
    p_selectedtypecode NVARCHAR(255),
    p_selectedtemplatecode NVARCHAR(255),
    p_code NVARCHAR(255),
    p_defaultviewmode NVARCHAR(255),
    p_user BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX userRelIDX_1703 ON cockpitsavedqueries (p_user);


CREATE CACHED TABLE cockpitsavedquerieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE cockpitsavedsortcrits
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_criterionqualifier NVARCHAR(255),
    p_asc TINYINT,
    p_cockpitsavedquery BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX cockpitsavedqueryRelIDX_1705 ON cockpitsavedsortcrits (p_cockpitsavedquery);


CREATE CACHED TABLE cockpittemplclassifrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1713 ON cockpittemplclassifrels (SourcePK);

CREATE INDEX qualifier_1713 ON cockpittemplclassifrels (Qualifier);

CREATE INDEX seqnr_1713 ON cockpittemplclassifrels (SequenceNumber);

CREATE INDEX rseqnr_1713 ON cockpittemplclassifrels (RSequenceNumber);

CREATE INDEX linktarget_1713 ON cockpittemplclassifrels (TargetPK);


CREATE CACHED TABLE codegenerationconfig
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_codeseparator NVARCHAR(255),
    p_couponpartcount INTEGER,
    p_couponpartlength INTEGER,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX IDX_CodeGenConfig_5405 ON codegenerationconfig (p_codeseparator);


CREATE CACHED TABLE collectiontypes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_extensionname NVARCHAR(255),
    p_autocreate TINYINT,
    p_generate TINYINT,
    InternalCode NVARCHAR(255),
    p_defaultvalue LONGVARBINARY,
    ElementTypePK BIGINT,
    typeOfCollection INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    InternalCodeLowerCase NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX typecode_83 ON collectiontypes (InternalCode);

CREATE INDEX typecodelowercase_83 ON collectiontypes (InternalCodeLowerCase);


CREATE CACHED TABLE collectiontypeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE combinedcatsforrule
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_rule BIGINT,
    p_conditionid INTEGER,
    p_categorycode NVARCHAR(255),
    p_promotion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX CombinedCatsForRule_IDX_5022 ON combinedcatsforrule (p_rule, p_categorycode, p_conditionid);


CREATE CACHED TABLE commentassignrelations
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1148 ON commentassignrelations (SourcePK);

CREATE INDEX qualifier_1148 ON commentassignrelations (Qualifier);

CREATE INDEX seqnr_1148 ON commentassignrelations (SequenceNumber);

CREATE INDEX rseqnr_1148 ON commentassignrelations (RSequenceNumber);

CREATE INDEX linktarget_1148 ON commentassignrelations (TargetPK);


CREATE CACHED TABLE commentattachments
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_item BIGINT,
    p_abstractcomment BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX comm_att_comment_1146 ON commentattachments (p_abstractcomment);


CREATE CACHED TABLE commentcompcreaterels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1152 ON commentcompcreaterels (SourcePK);

CREATE INDEX qualifier_1152 ON commentcompcreaterels (Qualifier);

CREATE INDEX seqnr_1152 ON commentcompcreaterels (SequenceNumber);

CREATE INDEX rseqnr_1152 ON commentcompcreaterels (RSequenceNumber);

CREATE INDEX linktarget_1152 ON commentcompcreaterels (TargetPK);


CREATE CACHED TABLE commentcomponents
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_name NVARCHAR(255),
    p_domain BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX commComponent_code_1142 ON commentcomponents (p_code);

CREATE INDEX domainRelIDX_1142 ON commentcomponents (p_domain);


CREATE CACHED TABLE commentcompreadrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1150 ON commentcompreadrels (SourcePK);

CREATE INDEX qualifier_1150 ON commentcompreadrels (Qualifier);

CREATE INDEX seqnr_1150 ON commentcompreadrels (SequenceNumber);

CREATE INDEX rseqnr_1150 ON commentcompreadrels (RSequenceNumber);

CREATE INDEX linktarget_1150 ON commentcompreadrels (TargetPK);


CREATE CACHED TABLE commentcompremoverels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1153 ON commentcompremoverels (SourcePK);

CREATE INDEX qualifier_1153 ON commentcompremoverels (Qualifier);

CREATE INDEX seqnr_1153 ON commentcompremoverels (SequenceNumber);

CREATE INDEX rseqnr_1153 ON commentcompremoverels (RSequenceNumber);

CREATE INDEX linktarget_1153 ON commentcompremoverels (TargetPK);


CREATE CACHED TABLE commentcompwriterels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1151 ON commentcompwriterels (SourcePK);

CREATE INDEX qualifier_1151 ON commentcompwriterels (Qualifier);

CREATE INDEX seqnr_1151 ON commentcompwriterels (SequenceNumber);

CREATE INDEX rseqnr_1151 ON commentcompwriterels (RSequenceNumber);

CREATE INDEX linktarget_1151 ON commentcompwriterels (TargetPK);


CREATE CACHED TABLE commentdomains
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_name NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX commDomain_code_1141 ON commentdomains (p_code);


CREATE CACHED TABLE commentitemrelations
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1147 ON commentitemrelations (SourcePK);

CREATE INDEX qualifier_1147 ON commentitemrelations (Qualifier);

CREATE INDEX seqnr_1147 ON commentitemrelations (SequenceNumber);

CREATE INDEX rseqnr_1147 ON commentitemrelations (RSequenceNumber);

CREATE INDEX linktarget_1147 ON commentitemrelations (TargetPK);


CREATE CACHED TABLE commentmetadatas
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_x INTEGER,
    p_y INTEGER,
    p_pageindex INTEGER,
    p_item BIGINT,
    p_comment BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX commentRelIDX_1716 ON commentmetadatas (p_comment);


CREATE CACHED TABLE comments
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_subject NVARCHAR(255),
    p_author BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_code NVARCHAR(255),
    p_priority INTEGER,
    p_component BIGINT,
    p_commenttype BIGINT,
    p_startdatetime TIMESTAMP,
    p_enddatetime TIMESTAMP,
    p_interventiontype BIGINT,
    p_reason BIGINT,
    p_resolutiontype BIGINT,
    p_parentpos INTEGER,
    p_parent BIGINT,
    p_commentpos INTEGER,
    p_comment BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX comment_code_1140 ON comments (p_code);

CREATE INDEX comment_component_1140 ON comments (p_component);

CREATE INDEX reply_parent_1140 ON comments (p_parent);

CREATE INDEX reply_comment_1140 ON comments (p_comment);

CREATE INDEX parentposPosIDX_1140 ON comments (p_parentpos);

CREATE INDEX commentposPosIDX_1140 ON comments (p_commentpos);

CREATE INDEX authorRelIDX_1140 ON comments (p_author);

CREATE INDEX commenttypeRelIDX_1140 ON comments (p_commenttype);


CREATE CACHED TABLE commenttypes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_name NVARCHAR(255),
    p_metatype BIGINT,
    p_domain BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX comment_type_code_1145 ON commenttypes (p_code, p_domain);

CREATE INDEX domainRelIDX_1145 ON commenttypes (p_domain);


CREATE CACHED TABLE commentusersettings
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_read TINYINT,
    p_ignored TINYINT,
    p_priority INTEGER,
    p_comment BIGINT,
    p_user BIGINT,
    p_workstatus TINYINT,
    p_hidden TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX cus_user_comment_1144 ON commentusersettings (p_user, p_comment);

CREATE INDEX commentRelIDX_1144 ON commentusersettings (p_comment);

CREATE INDEX userRelIDX_1144 ON commentusersettings (p_user);


CREATE CACHED TABLE commentwatchrelations
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1149 ON commentwatchrelations (SourcePK);

CREATE INDEX qualifier_1149 ON commentwatchrelations (Qualifier);

CREATE INDEX seqnr_1149 ON commentwatchrelations (SequenceNumber);

CREATE INDEX rseqnr_1149 ON commentwatchrelations (RSequenceNumber);

CREATE INDEX linktarget_1149 ON commentwatchrelations (TargetPK);


CREATE CACHED TABLE componenttypegrp
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE componenttypegrplp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE componenttypesforsite
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1093 ON componenttypesforsite (SourcePK);

CREATE INDEX qualifier_1093 ON componenttypesforsite (Qualifier);

CREATE INDEX seqnr_1093 ON componenttypesforsite (SequenceNumber);

CREATE INDEX rseqnr_1093 ON componenttypesforsite (RSequenceNumber);

CREATE INDEX linktarget_1093 ON componenttypesforsite (TargetPK);


CREATE CACHED TABLE componenttypesforslots
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1094 ON componenttypesforslots (SourcePK);

CREATE INDEX qualifier_1094 ON componenttypesforslots (Qualifier);

CREATE INDEX seqnr_1094 ON componenttypesforslots (SequenceNumber);

CREATE INDEX rseqnr_1094 ON componenttypesforslots (RSequenceNumber);

CREATE INDEX linktarget_1094 ON componenttypesforslots (TargetPK);


CREATE CACHED TABLE composedindexdtypes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE composedtypes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_extensionname NVARCHAR(255),
    p_autocreate TINYINT,
    p_generate TINYINT,
    InternalCode NVARCHAR(255),
    p_defaultvalue LONGVARBINARY,
    InheritancePathString LONGVARCHAR,
    jaloClassName NVARCHAR(255),
    ItemJNDIName NVARCHAR(255),
    Singleton TINYINT DEFAULT 0,
    p_jaloonly TINYINT,
    p_dynamic TINYINT,
    SuperTypePK BIGINT,
    p_legacypersistence TINYINT,
    p_systemtype TINYINT,
    p_catalogitemtype TINYINT,
    p_catalogversionattributequali NVARCHAR(255),
    p_uniquekeyattributequalifier NVARCHAR(255),
    p_hmcicon BIGINT,
    p_loghmcchanges TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    InternalCodeLowerCase NVARCHAR(255),
    removable TINYINT DEFAULT 0,
    propertyTableStatus TINYINT DEFAULT 0,
    ItemTypeCode INTEGER DEFAULT 0,
    p_comparationattribute BIGINT,
    p_localized TINYINT,
    p_sourceattribute BIGINT,
    p_targetattribute BIGINT,
    p_sourcetype BIGINT,
    p_targettype BIGINT,
    p_orderingattribute BIGINT,
    p_localizationattribute BIGINT,
    p_previewdisabled TINYINT,
    PRIMARY KEY (PK)
);

CREATE INDEX ComposedTypeSuperTypePKIDX_82 ON composedtypes (SuperTypePK);

CREATE INDEX typecode_82 ON composedtypes (InternalCode);

CREATE INDEX typecodelowercase_82 ON composedtypes (InternalCodeLowerCase);

CREATE INDEX inheritpsi_82 ON composedtypes (InheritancePathString);


CREATE CACHED TABLE composedtypeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE compositeentries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_executablecronjob BIGINT,
    p_triggerablejob BIGINT,
    p_compositecronjobpos INTEGER,
    p_compositecronjob BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX compositecronjobRelIDX_510 ON compositeentries (p_compositecronjob);

CREATE INDEX compositecronjobposPosIDX_510 ON compositeentries (p_compositecronjobpos);


CREATE CACHED TABLE comptypegrp2comptype
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1097 ON comptypegrp2comptype (SourcePK);

CREATE INDEX qualifier_1097 ON comptypegrp2comptype (Qualifier);

CREATE INDEX seqnr_1097 ON comptypegrp2comptype (SequenceNumber);

CREATE INDEX rseqnr_1097 ON comptypegrp2comptype (RSequenceNumber);

CREATE INDEX linktarget_1097 ON comptypegrp2comptype (TargetPK);


CREATE CACHED TABLE configitems
(
    hjmpTS BIGINT,
    PK BIGINT NOT NULL,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    aCLTS BIGINT DEFAULT 0,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE configuratorsettings
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_id NVARCHAR(255),
    p_catalogversion BIGINT,
    p_configuratortype BIGINT,
    p_qualifier NVARCHAR(255),
    p_configurationcategorypos INTEGER,
    p_configurationcategory BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX versionIdx_130 ON configuratorsettings (p_catalogversion);

CREATE UNIQUE INDEX idVersionIdx_130 ON configuratorsettings (p_id, p_catalogversion);

CREATE INDEX configurationcategoryRelIDX_130 ON configuratorsettings (p_configurationcategory);

CREATE INDEX configurationcategoryposPosIDX_130 ON configuratorsettings (p_configurationcategorypos);


CREATE CACHED TABLE configuratorsettingslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_textfieldlabel NVARCHAR(255),
    p_textfielddefaultvalue NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE consignmententries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_quantity BIGINT,
    p_shippedquantity BIGINT,
    p_orderentry BIGINT,
    p_consignment BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX orderentryRelIDX_2004 ON consignmententries (p_orderentry);

CREATE INDEX consignmentRelIDX_2004 ON consignmententries (p_consignment);


CREATE CACHED TABLE consignments
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_shippingaddress BIGINT,
    p_deliverymode BIGINT,
    p_nameddeliverydate TIMESTAMP,
    p_shippingdate TIMESTAMP,
    p_trackingid NVARCHAR(255),
    p_carrier NVARCHAR(255),
    p_status BIGINT,
    p_warehouse BIGINT,
    p_order BIGINT,
    p_deliverypointofservice BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX warehouseRelIDX_2003 ON consignments (p_warehouse);

CREATE INDEX orderRelIDX_2003 ON consignments (p_order);


CREATE CACHED TABLE constraintgroup
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_id NVARCHAR(255),
    p_interfacename NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_coveragedomainid NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX CronstraintGroup_idx_982 ON constraintgroup (p_id);


CREATE CACHED TABLE contentslot
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_name NVARCHAR(255),
    p_catalogversion BIGINT,
    p_active TINYINT,
    p_activefrom TIMESTAMP,
    p_activeuntil TIMESTAMP,
    p_locked TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE contentslotname
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_comptypegroup BIGINT,
    p_templatepos INTEGER,
    p_template BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX templateRelIDX_1078 ON contentslotname (p_template);

CREATE INDEX templateposPosIDX_1078 ON contentslotname (p_templatepos);


CREATE CACHED TABLE countries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_active TINYINT,
    p_isocode NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX ISOCode_34 ON countries (p_isocode);


CREATE CACHED TABLE countrieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE coupon
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_couponid NVARCHAR(255),
    p_startdate TIMESTAMP,
    p_enddate TIMESTAMP,
    p_active TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_maxredemptionspercustomer INTEGER,
    p_maxtotalredemptions INTEGER,
    p_codegenerationconfiguration BIGINT,
    p_generatedcodes LONGVARCHAR,
    p_alphabet NVARCHAR(255),
    p_signature NVARCHAR(255),
    p_couponcodenumber BIGINT,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX UC_Coupon_5400 ON coupon (p_couponid);


CREATE CACHED TABLE couponlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE couponredemption
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_couponcode NVARCHAR(255),
    p_coupon BIGINT,
    p_order BIGINT,
    p_user BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX IDX_CouponRedemption_5401 ON couponredemption (p_couponcode);


CREATE CACHED TABLE cronjobs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_errormode BIGINT,
    p_logtofile TINYINT,
    p_logtodatabase TINYINT,
    p_loglevelfile BIGINT,
    p_logleveldatabase BIGINT,
    p_sessionuser BIGINT,
    p_sessionlanguage BIGINT,
    p_sessioncurrency BIGINT,
    p_active TINYINT,
    p_retry TINYINT,
    p_singleexecutable TINYINT,
    p_emailaddress NVARCHAR(255),
    p_sendemail TINYINT,
    p_starttime TIMESTAMP,
    p_endtime TIMESTAMP,
    p_status BIGINT,
    p_result BIGINT,
    p_nodeid INTEGER,
    p_nodegroup NVARCHAR(255),
    p_runningonclusternode INTEGER,
    p_currentstep BIGINT,
    p_changerecordingenabled TINYINT,
    p_requestabort TINYINT,
    p_requestabortstep TINYINT,
    p_priority INTEGER,
    p_removeonexit TINYINT,
    p_emailnotificationtemplate BIGINT,
    p_alternativedatasourceid NVARCHAR(255),
    p_logsdaysold INTEGER,
    p_logscount INTEGER,
    p_logsoperator BIGINT,
    p_filesdaysold INTEGER,
    p_filescount INTEGER,
    p_filesoperator BIGINT,
    p_job BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_jobmedia BIGINT,
    p_currentline INTEGER,
    p_lastsuccessfulline INTEGER,
    p_url NVARCHAR(255),
    p_query NVARCHAR(255),
    p_failonunknown TINYINT,
    p_dontneedtotal TINYINT,
    p_rangestart INTEGER,
    p_count INTEGER,
    p_searchbase NVARCHAR(255),
    p_ldapquery NVARCHAR(255),
    p_resultfilter NVARCHAR(255),
    p_importmode BIGINT,
    p_ldiffile BIGINT,
    p_configfile BIGINT,
    p_destmedia BIGINT,
    p_dumpmedia BIGINT,
    p_usersearchfieldqualifier NVARCHAR(255),
    p_userrootdn NVARCHAR(255),
    p_userresultfilter NVARCHAR(255),
    p_importusers TINYINT,
    p_codeexecution TINYINT,
    p_itempks BIGINT,
    p_itemsfound INTEGER,
    p_itemsdeleted INTEGER,
    p_itemsrefused INTEGER,
    p_createsavedvalues TINYINT,
    p_medias LONGVARCHAR,
    p_targetfolder BIGINT,
    p_movedmediascount INTEGER,
    p_mediafolder BIGINT,
    p_timethreshold INTEGER,
    p_versionthreshold INTEGER,
    p_xdaysold INTEGER,
    p_excludecronjobs LONGVARCHAR,
    p_resultcoll LONGVARCHAR,
    p_statuscoll LONGVARCHAR,
    p_encoding BIGINT,
    p_mode BIGINT,
    p_dataexporttarget BIGINT,
    p_mediasexporttarget BIGINT,
    p_exporttemplate BIGINT,
    p_export BIGINT,
    p_itemsexported INTEGER,
    p_itemsmaxcount INTEGER,
    p_itemsskipped INTEGER,
    p_fieldseparator SMALLINT,
    p_quotecharacter SMALLINT,
    p_commentcharacter SMALLINT,
    p_dataexportmediacode NVARCHAR(255),
    p_mediasexportmediacode NVARCHAR(255),
    p_report BIGINT,
    p_converterclass BIGINT,
    p_singlefile TINYINT,
    p_workmedia BIGINT,
    p_mediasmedia BIGINT,
    p_externaldatacollection LONGVARCHAR,
    p_locale NVARCHAR(255),
    p_dumpfileencoding BIGINT,
    p_enablecodeexecution TINYINT,
    p_enableexternalcodeexecution TINYINT,
    p_enableexternalsyntaxparsing TINYINT,
    p_enablehmcsavedvalues TINYINT,
    p_mediastarget NVARCHAR(255),
    p_valuecount INTEGER,
    p_unresolveddatastore BIGINT,
    p_dumpingallowed TINYINT,
    p_unzipmediasmedia TINYINT,
    p_maxthreads INTEGER,
    p_legacymode TINYINT,
    p_processeditemscount INTEGER,
    p_sourceversion BIGINT,
    p_targetversion BIGINT,
    p_missingproducts INTEGER,
    p_newproducts INTEGER,
    p_maxpricetolerance DOUBLE,
    p_searchmissingproducts TINYINT,
    p_searchmissingcategories TINYINT,
    p_searchnewproducts TINYINT,
    p_searchnewcategories TINYINT,
    p_searchpricedifferences TINYINT,
    p_overwriteproductapprovalstat TINYINT,
    p_pricecomparecustomer BIGINT,
    p_catalog BIGINT,
    p_catalogversion BIGINT,
    p_dontremoveobjects TINYINT,
    p_notremoveditems BIGINT,
    p_totaldeleteitemcount INTEGER,
    p_currentprocessingitemcount INTEGER,
    p_forceupdate TINYINT,
    p_statusmessage NVARCHAR(255),
    p_paging INTEGER,
    p_indexeroperation BIGINT,
    p_indexerhints LONGVARBINARY,
    p_facetsearchconfigpos INTEGER,
    p_facetsearchconfig BIGINT,
    p_indextypename NVARCHAR(255),
    p_items LONGVARCHAR,
    p_indexedtype NVARCHAR(255),
    p_indexedproperties LONGVARBINARY,
    p_queryparameterprovider NVARCHAR(255),
    p_language BIGINT,
    p_solrfacetsearchconfigpos INTEGER,
    p_solrfacetsearchconfig BIGINT,
    p_cart BIGINT,
    p_deliveryaddress BIGINT,
    p_paymentaddress BIGINT,
    p_paymentinfo BIGINT,
    p_ordertemplate BIGINT,
    p_order BIGINT,
    p_batchsize INTEGER,
    p_internaldelay INTEGER,
    p_basestore BIGINT,
    p_cmssite BIGINT,
    p_currency BIGINT,
    p_user BIGINT,
    p_thirdpartyhost NVARCHAR(255),
    p_thirdpartyusername NVARCHAR(255),
    p_thirdpartypassword NVARCHAR(255),
    p_datagenerationpipeline NVARCHAR(255),
    p_contentsite BIGINT,
    p_sitemapurllimitperfile INTEGER,
    p_age INTEGER,
    p_queuetimelimit INTEGER,
    p_sites LONGVARCHAR,
    p_cartremovalage INTEGER,
    p_anonymouscartremovalage INTEGER,
    PRIMARY KEY (PK)
);

CREATE INDEX IdxJob_501 ON cronjobs (p_job);

CREATE INDEX IdxNode_501 ON cronjobs (p_nodeid);

CREATE INDEX IdxActive_501 ON cronjobs (p_active);

CREATE INDEX facetsearchconfigRelIDX_501 ON cronjobs (p_facetsearchconfig);

CREATE INDEX facetsearchconfigposPosIDX_501 ON cronjobs (p_facetsearchconfigpos);

CREATE INDEX solrfacetsearchconfigRelIDX_501 ON cronjobs (p_solrfacetsearchconfig);

CREATE INDEX solrfacetsearchconfigposPosIDX_501 ON cronjobs (p_solrfacetsearchconfigpos);

CREATE INDEX ordertemplateRelIDX_501 ON cronjobs (p_ordertemplate);

CREATE INDEX cartRelIDX_501 ON cronjobs (p_cart);

CREATE INDEX deliveryaddressRelIDX_501 ON cronjobs (p_deliveryaddress);

CREATE INDEX paymentaddressRelIDX_501 ON cronjobs (p_paymentaddress);

CREATE INDEX paymentinfoRelIDX_501 ON cronjobs (p_paymentinfo);

CREATE INDEX orderRelIDX_501 ON cronjobs (p_order);


CREATE CACHED TABLE cronjobslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE csagentgroup2basestore
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_9007 ON csagentgroup2basestore (SourcePK);

CREATE INDEX qualifier_9007 ON csagentgroup2basestore (Qualifier);

CREATE INDEX seqnr_9007 ON csagentgroup2basestore (SequenceNumber);

CREATE INDEX rseqnr_9007 ON csagentgroup2basestore (RSequenceNumber);

CREATE INDEX linktarget_9007 ON csagentgroup2basestore (TargetPK);


CREATE CACHED TABLE cschangeevententry
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_alteredattribute BIGINT,
    p_oldstringvalue NVARCHAR(255),
    p_newstringvalue NVARCHAR(255),
    p_oldbinaryvalue LONGVARBINARY,
    p_newbinaryvalue LONGVARBINARY,
    p_event BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_oldvalue NVARCHAR(255),
    p_newvalue NVARCHAR(255),
    p_oldvalu0 BIGINT,
    p_newvalu0 BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX eventRelIDX_9004 ON cschangeevententry (p_event);


CREATE CACHED TABLE csticketemail
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_messageid NVARCHAR(255),
    p_from NVARCHAR(255),
    p_to NVARCHAR(255),
    p_subject NVARCHAR(255),
    p_body LONGVARCHAR,
    p_ticketpos INTEGER,
    p_ticket BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX ticketRelIDX_9003 ON csticketemail (p_ticket);

CREATE INDEX ticketposPosIDX_9003 ON csticketemail (p_ticketpos);


CREATE CACHED TABLE csticketeventemailconfig
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_plaintexttemplate BIGINT,
    p_htmltemplate BIGINT,
    p_subject NVARCHAR(255),
    p_eventtype BIGINT,
    p_alteredattributes LONGVARCHAR,
    p_recipienttype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE cstickets
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_ticketid NVARCHAR(255),
    p_customer BIGINT,
    p_order BIGINT,
    p_headline NVARCHAR(255),
    p_category BIGINT,
    p_priority BIGINT,
    p_state BIGINT,
    p_assignedagent BIGINT,
    p_assignedgroup BIGINT,
    p_resolution BIGINT,
    p_basesite BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX ticketIdIdx_9001 ON cstickets (p_ticketid);

CREATE INDEX ticketPoolIdx_9001 ON cstickets (p_assignedagent, p_assignedgroup, p_state);


CREATE CACHED TABLE cstrgr2abscstrrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_979 ON cstrgr2abscstrrel (SourcePK);

CREATE INDEX qualifier_979 ON cstrgr2abscstrrel (Qualifier);

CREATE INDEX seqnr_979 ON cstrgr2abscstrrel (SequenceNumber);

CREATE INDEX rseqnr_979 ON cstrgr2abscstrrel (RSequenceNumber);

CREATE INDEX linktarget_979 ON cstrgr2abscstrrel (TargetPK);


CREATE CACHED TABLE currencies
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_active TINYINT,
    p_isocode NVARCHAR(255),
    p_base TINYINT,
    p_conversion DOUBLE,
    p_digits INTEGER,
    p_symbol NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX ISOCode_33 ON currencies (p_isocode);

CREATE INDEX conversionFactor_idx_33 ON currencies (p_conversion);


CREATE CACHED TABLE currencieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE customerreviews
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_headline NVARCHAR(255),
    p_comment LONGVARCHAR,
    p_rating DOUBLE,
    p_blocked TINYINT,
    p_alias NVARCHAR(255),
    p_approvalstatus BIGINT,
    p_language BIGINT,
    p_user BIGINT,
    p_product BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX userRelIDX_49 ON customerreviews (p_user);

CREATE INDEX productRelIDX_49 ON customerreviews (p_product);


CREATE CACHED TABLE cxabstractaction
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_type BIGINT,
    p_target NVARCHAR(255),
    p_catalogversion BIGINT,
    p_variation BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_componentid NVARCHAR(255),
    p_containerid NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX codeVariationCatalogVersionIdx_3604 ON cxabstractaction (p_code, p_variation, p_catalogversion);

CREATE INDEX variationCatalogVersionIdx_3604 ON cxabstractaction (p_variation, p_catalogversion);

CREATE INDEX variationRelIDX_3604 ON cxabstractaction (p_variation);


CREATE CACHED TABLE cxabstracttrigger
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_catalogversion BIGINT,
    p_variation BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_groupby BIGINT,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX codeVariationCatalogVersionIdx_3609 ON cxabstracttrigger (p_code, p_variation, p_catalogversion);

CREATE INDEX variationCatalogVersionIdx_3609 ON cxabstracttrigger (p_variation, p_catalogversion);

CREATE INDEX variationRelIDX_3609 ON cxabstracttrigger (p_variation);


CREATE CACHED TABLE cxcustomization
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_catalogversion BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    p_enabledstartdate TIMESTAMP,
    p_enabledenddate TIMESTAMP,
    p_grouppos INTEGER,
    p_group BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX codeCatalogVersionIdx_3602 ON cxcustomization (p_code, p_catalogversion);

CREATE INDEX nameCatalogVersionIdx_3602 ON cxcustomization (p_name, p_catalogversion);

CREATE INDEX catalogVersionIdx_3602 ON cxcustomization (p_catalogversion);

CREATE INDEX groupRelIDX_3602 ON cxcustomization (p_group);

CREATE INDEX groupposPosIDX_3602 ON cxcustomization (p_grouppos);


CREATE CACHED TABLE cxcustomizationsgroup
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_catalogversion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX catalogVersionIdx_3608 ON cxcustomizationsgroup (p_catalogversion);


CREATE CACHED TABLE cxsegment
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX codeIdx_3603 ON cxsegment (p_code);


CREATE CACHED TABLE cxsegmenttotrigger
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_3610 ON cxsegmenttotrigger (SourcePK);

CREATE INDEX qualifier_3610 ON cxsegmenttotrigger (Qualifier);

CREATE INDEX seqnr_3610 ON cxsegmenttotrigger (SequenceNumber);

CREATE INDEX rseqnr_3610 ON cxsegmenttotrigger (RSequenceNumber);

CREATE INDEX linktarget_3610 ON cxsegmenttotrigger (TargetPK);


CREATE CACHED TABLE cxusertosegment
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_affinity DECIMAL(30,8),
    p_segment BIGINT,
    p_user BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX segmentRelIDX_3607 ON cxusertosegment (p_segment);

CREATE INDEX userRelIDX_3607 ON cxusertosegment (p_user);


CREATE CACHED TABLE cxvariation
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_catalogversion BIGINT,
    p_name NVARCHAR(255),
    p_enabled TINYINT,
    p_customizationpos INTEGER,
    p_customization BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX codeCustomizationCatalogVersionIdx_3601 ON cxvariation (p_code, p_customization, p_catalogversion);

CREATE INDEX nameCustomizationCatalogVersionIdx_3601 ON cxvariation (p_name, p_customization, p_catalogversion);

CREATE INDEX customizationCatalogVersionIdx_3601 ON cxvariation (p_customization, p_catalogversion);

CREATE INDEX customizationRelIDX_3601 ON cxvariation (p_customization);

CREATE INDEX customizationposPosIDX_3601 ON cxvariation (p_customizationpos);


CREATE CACHED TABLE deeplinkurlrules
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_baseurlpattern NVARCHAR(255),
    p_desturltemplate NVARCHAR(255),
    p_applicabletype BIGINT,
    p_useforward TINYINT,
    p_priority INTEGER,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE deeplinkurls
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_name NVARCHAR(255),
    p_baseurl NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE deliverymodes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_active TINYINT,
    p_code NVARCHAR(255),
    p_supportedpaymentmodesinterna NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_propertyname NVARCHAR(255),
    p_net TINYINT,
    p_supportedmode BIGINT,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE deliverymodeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description LONGVARCHAR,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE derivedmedias
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_mime NVARCHAR(255),
    p_size BIGINT,
    p_datapk BIGINT,
    p_location LONGVARCHAR,
    p_locationhash NVARCHAR(255),
    p_realfilename NVARCHAR(255),
    p_version NVARCHAR(255),
    p_media BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX dataPK_idx_31 ON derivedmedias (p_datapk);

CREATE UNIQUE INDEX version_idx_31 ON derivedmedias (p_media, p_version);

CREATE INDEX mediaRelIDX_31 ON derivedmedias (p_media);


CREATE CACHED TABLE devicesupport
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_width INTEGER,
    p_height INTEGER,
    p_orientation TINYINT,
    p_supporteduiexperience BIGINT,
    p_default TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX IdxDeviceSupportItem_3500 ON devicesupport (p_code, p_supporteduiexperience);


CREATE CACHED TABLE devicesupportlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE discountrows
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_product BIGINT,
    p_pg BIGINT,
    p_productmatchqualifier BIGINT,
    p_starttime TIMESTAMP,
    p_endtime TIMESTAMP,
    p_user BIGINT,
    p_ug BIGINT,
    p_usermatchqualifier BIGINT,
    p_productid NVARCHAR(255),
    p_currency BIGINT,
    p_discount BIGINT,
    p_value DOUBLE,
    p_catalogversion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX MatchIndexP_1052 ON discountrows (p_productmatchqualifier);

CREATE INDEX MatchIndexU_1052 ON discountrows (p_usermatchqualifier);

CREATE INDEX PIdx_1052 ON discountrows (p_product);

CREATE INDEX UIdx_1052 ON discountrows (p_user);

CREATE INDEX PGIdx_1052 ON discountrows (p_pg);

CREATE INDEX UGIdx_1052 ON discountrows (p_ug);

CREATE INDEX ProductIdIdx_1052 ON discountrows (p_productid);

CREATE INDEX versionIDX_1052 ON discountrows (p_catalogversion);


CREATE CACHED TABLE discounts
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_currency BIGINT,
    p_global TINYINT,
    p_priority INTEGER,
    p_value DOUBLE,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_freeshipping TINYINT,
    p_codes LONGVARCHAR,
    p_vouchercode NVARCHAR(255),
    p_redemptionquantitylimit INTEGER,
    p_redemptionquantitylimitperus INTEGER,
    PRIMARY KEY (PK)
);

CREATE INDEX codeIDX_48 ON discounts (p_code);


CREATE CACHED TABLE discountslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE distributedbatches
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_id NVARCHAR(255),
    p_executionid NVARCHAR(255),
    p_type BIGINT,
    p_remainingworkload BIGINT,
    p_process BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_resultbatchid NVARCHAR(255),
    p_retries INTEGER,
    p_scriptcode NVARCHAR(255),
    p_context LONGVARBINARY,
    p_group INTEGER,
    p_metadata NVARCHAR(255),
    p_importcontentcode NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX BatchUnqIdx_112 ON distributedbatches (p_executionid, p_process, p_id, p_type);

CREATE INDEX processRelIDX_112 ON distributedbatches (p_process);


CREATE CACHED TABLE distributedprocesses
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_handlerbeanid NVARCHAR(255),
    p_currentexecutionid NVARCHAR(255),
    p_state BIGINT,
    p_stoprequested TINYINT,
    p_nodegroup NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_batchsize INTEGER,
    p_sessionuser NVARCHAR(255),
    p_sessionlanguage NVARCHAR(255),
    p_sessioncurrency NVARCHAR(255),
    p_indexoperationid BIGINT,
    p_indexoperation BIGINT,
    p_externalindexoperation TINYINT,
    p_facetsearchconfig NVARCHAR(255),
    p_indexedtype NVARCHAR(255),
    p_indexedproperties LONGVARBINARY,
    p_index NVARCHAR(255),
    p_indexerhints LONGVARBINARY,
    p_impeximportcronjob BIGINT,
    p_metadata NVARCHAR(255),
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE droolskiebase
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_equalitybehavior BIGINT,
    p_eventprocessingmode BIGINT,
    p_defaultkiesession BIGINT,
    p_kiemodule BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX kiemoduleRelIDX_5200 ON droolskiebase (p_kiemodule);


CREATE CACHED TABLE droolskiesession
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_sessiontype BIGINT,
    p_kiebase BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX kiebaseRelIDX_5201 ON droolskiesession (p_kiebase);


CREATE CACHED TABLE dynamiccontent
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_active TINYINT,
    p_checksum NVARCHAR(255),
    p_content LONGVARCHAR,
    p_version BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX codeVersionActiveIDX_101 ON dynamiccontent (p_code, p_version, p_active);


CREATE CACHED TABLE elements4container
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1077 ON elements4container (SourcePK);

CREATE INDEX qualifier_1077 ON elements4container (Qualifier);

CREATE INDEX seqnr_1077 ON elements4container (SequenceNumber);

CREATE INDEX rseqnr_1077 ON elements4container (RSequenceNumber);

CREATE INDEX linktarget_1077 ON elements4container (TargetPK);


CREATE CACHED TABLE elements4slots
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1085 ON elements4slots (SourcePK);

CREATE INDEX qualifier_1085 ON elements4slots (Qualifier);

CREATE INDEX seqnr_1085 ON elements4slots (SequenceNumber);

CREATE INDEX rseqnr_1085 ON elements4slots (RSequenceNumber);

CREATE INDEX linktarget_1085 ON elements4slots (TargetPK);


CREATE CACHED TABLE emailaddress
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_emailaddress NVARCHAR(255),
    p_displayname NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX EmailAddress_6207 ON emailaddress (p_emailaddress, p_displayname);


CREATE CACHED TABLE emailmessage
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_sent TINYINT,
    p_replytoaddress NVARCHAR(255),
    p_subject LONGVARCHAR,
    p_body LONGVARCHAR,
    p_bodymedia BIGINT,
    p_sentdate TIMESTAMP,
    p_sentmessageid NVARCHAR(255),
    p_fromaddress BIGINT,
    p_process BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX EmailMessageSent_6208 ON emailmessage (p_sent);

CREATE INDEX fromaddressRelIDX_6208 ON emailmessage (p_fromaddress);

CREATE INDEX processRelIDX_6208 ON emailmessage (p_process);


CREATE CACHED TABLE emailmsg2bccaddrrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6204 ON emailmsg2bccaddrrel (SourcePK);

CREATE INDEX qualifier_6204 ON emailmsg2bccaddrrel (Qualifier);

CREATE INDEX seqnr_6204 ON emailmsg2bccaddrrel (SequenceNumber);

CREATE INDEX rseqnr_6204 ON emailmsg2bccaddrrel (RSequenceNumber);

CREATE INDEX linktarget_6204 ON emailmsg2bccaddrrel (TargetPK);


CREATE CACHED TABLE emailmsg2ccaddrrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6203 ON emailmsg2ccaddrrel (SourcePK);

CREATE INDEX qualifier_6203 ON emailmsg2ccaddrrel (Qualifier);

CREATE INDEX seqnr_6203 ON emailmsg2ccaddrrel (SequenceNumber);

CREATE INDEX rseqnr_6203 ON emailmsg2ccaddrrel (RSequenceNumber);

CREATE INDEX linktarget_6203 ON emailmsg2ccaddrrel (TargetPK);


CREATE CACHED TABLE emailmsg2toaddrrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6202 ON emailmsg2toaddrrel (SourcePK);

CREATE INDEX qualifier_6202 ON emailmsg2toaddrrel (Qualifier);

CREATE INDEX seqnr_6202 ON emailmsg2toaddrrel (SequenceNumber);

CREATE INDEX rseqnr_6202 ON emailmsg2toaddrrel (RSequenceNumber);

CREATE INDEX linktarget_6202 ON emailmsg2toaddrrel (TargetPK);


CREATE CACHED TABLE enginerules
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uuid NVARCHAR(255),
    p_code NVARCHAR(255),
    p_active TINYINT,
    p_rulecontent LONGVARCHAR,
    p_ruletype BIGINT,
    p_checksum NVARCHAR(255),
    p_currentversion TINYINT,
    p_version BIGINT,
    p_ruleparameters LONGVARCHAR,
    p_maxallowedruns INTEGER,
    p_rulegroupcode NVARCHAR(255),
    p_promotion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_rulepackage NVARCHAR(255),
    p_globals LONGVARBINARY,
    p_kiebase BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX IdxEngineRuleActive_5100 ON enginerules (p_active);

CREATE UNIQUE INDEX IdxCodeVersionActive_5100 ON enginerules (p_code, p_version, p_active, p_currentversion);

CREATE INDEX IdxEngineRulePkg_5100 ON enginerules (p_rulepackage);

CREATE INDEX kiebaseRelIDX_5100 ON enginerules (p_kiebase);


CREATE CACHED TABLE engineruleslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_messagefired LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE enumerationvalues
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    Code NVARCHAR(255),
    codeLowerCase NVARCHAR(255),
    SequenceNumber INTEGER DEFAULT 0,
    p_extensionname NVARCHAR(255),
    p_icon BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    Editable TINYINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX uniqueCodeIdx_91 ON enumerationvalues (TypePkString, codeLowerCase);

CREATE INDEX codeidx_91 ON enumerationvalues (Code);

CREATE INDEX seqnridx_91 ON enumerationvalues (SequenceNumber);

CREATE INDEX code2idx_91 ON enumerationvalues (codeLowerCase);


CREATE CACHED TABLE enumerationvalueslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE excludedcatforrule
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_categorycode NVARCHAR(255),
    p_rule BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX ExcludedCatForRule_IDX_5020 ON excludedcatforrule (p_rule, p_categorycode);


CREATE CACHED TABLE excludedproductforrule
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_productcode NVARCHAR(255),
    p_rule BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX ExcludedProductForRule_IDX_5021 ON excludedproductforrule (p_rule, p_productcode);


CREATE CACHED TABLE exportdatahistoryentry
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_status BIGINT,
    p_starttime TIMESTAMP,
    p_finishtime TIMESTAMP,
    p_processedresultcount INTEGER,
    p_failuremessage NVARCHAR(255),
    p_exportdatacronjob BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX exportdatacronjobRelIDX_6211 ON exportdatahistoryentry (p_exportdatacronjob);


CREATE CACHED TABLE exports
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_exportedmedias BIGINT,
    p_exporteddata BIGINT,
    p_exportscript BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE exportslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE externalimportkey
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_sourcesystemid NVARCHAR(255),
    p_sourcekey NVARCHAR(255),
    p_targetpk BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX sourceSystemIDSourceKeyIDX_110 ON externalimportkey (p_sourcesystemid, p_sourcekey);


CREATE CACHED TABLE facetsearchconf
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    p_document BIGINT,
    p_solrsearchconfig BIGINT,
    p_solrindexconfig BIGINT,
    p_solrserverconfig BIGINT,
    p_indexnameprefix NVARCHAR(255),
    p_enabledlanguagefallbackmecha TINYINT,
    p_listeners LONGVARBINARY,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE facsearchconfcatverrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_2201 ON facsearchconfcatverrel (SourcePK);

CREATE INDEX qualifier_2201 ON facsearchconfcatverrel (Qualifier);

CREATE INDEX seqnr_2201 ON facsearchconfcatverrel (SequenceNumber);

CREATE INDEX rseqnr_2201 ON facsearchconfcatverrel (RSequenceNumber);

CREATE INDEX linktarget_2201 ON facsearchconfcatverrel (TargetPK);


CREATE CACHED TABLE facsearchconfcurrrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_2202 ON facsearchconfcurrrel (SourcePK);

CREATE INDEX qualifier_2202 ON facsearchconfcurrrel (Qualifier);

CREATE INDEX seqnr_2202 ON facsearchconfcurrrel (SequenceNumber);

CREATE INDEX rseqnr_2202 ON facsearchconfcurrrel (RSequenceNumber);

CREATE INDEX linktarget_2202 ON facsearchconfcurrrel (TargetPK);


CREATE CACHED TABLE facsearchconflangrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_2203 ON facsearchconflangrel (SourcePK);

CREATE INDEX qualifier_2203 ON facsearchconflangrel (Qualifier);

CREATE INDEX seqnr_2203 ON facsearchconflangrel (SequenceNumber);

CREATE INDEX rseqnr_2203 ON facsearchconflangrel (RSequenceNumber);

CREATE INDEX linktarget_2203 ON facsearchconflangrel (TargetPK);


CREATE CACHED TABLE format
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_initial BIGINT,
    p_documenttype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE format2comtyprel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_13102 ON format2comtyprel (SourcePK);

CREATE INDEX qualifier_13102 ON format2comtyprel (Qualifier);

CREATE INDEX seqnr_13102 ON format2comtyprel (SequenceNumber);

CREATE INDEX rseqnr_13102 ON format2comtyprel (RSequenceNumber);

CREATE INDEX linktarget_13102 ON format2comtyprel (TargetPK);


CREATE CACHED TABLE format2medforrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_13101 ON format2medforrel (SourcePK);

CREATE INDEX qualifier_13101 ON format2medforrel (Qualifier);

CREATE INDEX seqnr_13101 ON format2medforrel (SequenceNumber);

CREATE INDEX rseqnr_13101 ON format2medforrel (RSequenceNumber);

CREATE INDEX linktarget_13101 ON format2medforrel (TargetPK);


CREATE CACHED TABLE formatlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE fraudreports
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_provider NVARCHAR(255),
    p_timestamp TIMESTAMP,
    p_status BIGINT,
    p_explanation NVARCHAR(255),
    p_order BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX orderRelIDX_2001 ON fraudreports (p_order);


CREATE CACHED TABLE fraudsymptomscoring
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_score DOUBLE,
    p_explanation NVARCHAR(255),
    p_fraudreport BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX fraudreportRelIDX_2014 ON fraudsymptomscoring (p_fraudreport);


CREATE CACHED TABLE genericitems
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_comparator BIGINT,
    p_emptyhandling BIGINT,
    p_valuetype BIGINT,
    p_searchparametername NVARCHAR(255),
    p_joinalias NVARCHAR(255),
    p_lower TINYINT,
    p_wherepart BIGINT,
    p_typedsearchparameter BIGINT,
    p_enclosingtype BIGINT,
    p_code NVARCHAR(255),
    p_actiontemplate BIGINT,
    p_action BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX wherepartRelIDX_99 ON genericitems (p_wherepart);

CREATE INDEX actiontemplateRelIDX_99 ON genericitems (p_actiontemplate);

CREATE INDEX actionRelIDX_99 ON genericitems (p_action);


CREATE CACHED TABLE genericitemslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE gentestitems
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE globaldiscountrows
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_product BIGINT,
    p_pg BIGINT,
    p_productmatchqualifier BIGINT,
    p_starttime TIMESTAMP,
    p_endtime TIMESTAMP,
    p_user BIGINT,
    p_ug BIGINT,
    p_usermatchqualifier BIGINT,
    p_productid NVARCHAR(255),
    p_currency BIGINT,
    p_discount BIGINT,
    p_value DOUBLE,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX MatchIndexP_1053 ON globaldiscountrows (p_productmatchqualifier);

CREATE INDEX MatchIndexU_1053 ON globaldiscountrows (p_usermatchqualifier);

CREATE INDEX PIdx_1053 ON globaldiscountrows (p_product);

CREATE INDEX UIdx_1053 ON globaldiscountrows (p_user);

CREATE INDEX PGIdx_1053 ON globaldiscountrows (p_pg);

CREATE INDEX UGIdx_1053 ON globaldiscountrows (p_ug);

CREATE INDEX ProductIdIdx_1053 ON globaldiscountrows (p_productid);


CREATE CACHED TABLE groups4restriction
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1083 ON groups4restriction (SourcePK);

CREATE INDEX qualifier_1083 ON groups4restriction (Qualifier);

CREATE INDEX seqnr_1083 ON groups4restriction (SequenceNumber);

CREATE INDEX rseqnr_1083 ON groups4restriction (RSequenceNumber);

CREATE INDEX linktarget_1083 ON groups4restriction (TargetPK);


CREATE CACHED TABLE historydocrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_2009 ON historydocrel (SourcePK);

CREATE INDEX qualifier_2009 ON historydocrel (Qualifier);

CREATE INDEX seqnr_2009 ON historydocrel (SequenceNumber);

CREATE INDEX rseqnr_2009 ON historydocrel (RSequenceNumber);

CREATE INDEX linktarget_2009 ON historydocrel (TargetPK);


CREATE CACHED TABLE hmchistoryentries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_user BIGINT,
    p_timestamp TIMESTAMP,
    p_actiontype BIGINT,
    p_comment NVARCHAR(255),
    p_referenceditem BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE impbatchcontent
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_content LONGVARCHAR,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_processcode NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX codeIDX_113 ON impbatchcontent (p_code);


CREATE CACHED TABLE impexdocumentids
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_processcode NVARCHAR(255),
    p_docid NVARCHAR(255),
    p_itemqualifier NVARCHAR(255),
    p_itempk BIGINT,
    p_resolved TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX ImpexDocumentIdsIdx_114 ON impexdocumentids (p_processcode, p_docid, p_itemqualifier, p_itempk, p_resolved);


CREATE CACHED TABLE indextestitem
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_column1 SMALLINT,
    p_column2 SMALLINT,
    p_column3 SMALLINT,
    p_column4 SMALLINT,
    p_column5 SMALLINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX OrderIndex_7777 ON indextestitem (p_column3, p_column4, p_column1, p_column2, p_column5);


CREATE CACHED TABLE itemcockpittemplrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1712 ON itemcockpittemplrels (SourcePK);

CREATE INDEX qualifier_1712 ON itemcockpittemplrels (Qualifier);

CREATE INDEX seqnr_1712 ON itemcockpittemplrels (SequenceNumber);

CREATE INDEX rseqnr_1712 ON itemcockpittemplrels (RSequenceNumber);

CREATE INDEX linktarget_1712 ON itemcockpittemplrels (TargetPK);


CREATE CACHED TABLE itemsynctimestamps
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_syncjob BIGINT,
    p_sourceitem BIGINT,
    p_targetitem BIGINT,
    p_sourceversion BIGINT,
    p_targetversion BIGINT,
    p_lastsyncsourcemodifiedtime TIMESTAMP,
    p_lastsynctime TIMESTAMP,
    p_pendingattributesownerjob BIGINT,
    p_pendingattributesscheduledtu INTEGER,
    p_pendingattributequalifiers LONGVARCHAR,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX syncIDX_619 ON itemsynctimestamps (p_sourceitem, p_targetversion, p_syncjob);

CREATE INDEX srcIDX_619 ON itemsynctimestamps (p_sourceitem);

CREATE INDEX tgtIDX_619 ON itemsynctimestamps (p_targetitem);

CREATE INDEX jobIDX_619 ON itemsynctimestamps (p_syncjob);

CREATE INDEX srcVerIDX_619 ON itemsynctimestamps (p_sourceversion);

CREATE INDEX tgtVerIDX_619 ON itemsynctimestamps (p_targetversion);


CREATE CACHED TABLE jalotranslatorconfig
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE jalovelocityrenderer
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_template LONGVARCHAR,
    p_translatorconfigurationpos INTEGER,
    p_translatorconfiguration BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX translatorconfigurationRelIDX_13211 ON jalovelocityrenderer (p_translatorconfiguration);

CREATE INDEX translatorconfigurationposPosIDX_13211 ON jalovelocityrenderer (p_translatorconfigurationpos);


CREATE CACHED TABLE joblogs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_step BIGINT,
    p_level BIGINT,
    p_cronjob BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX cronjobIDX_504 ON joblogs (p_cronjob);


CREATE CACHED TABLE jobs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_nodeid INTEGER,
    p_nodegroup NVARCHAR(255),
    p_errormode BIGINT,
    p_logtofile TINYINT,
    p_logtodatabase TINYINT,
    p_loglevelfile BIGINT,
    p_logleveldatabase BIGINT,
    p_sessionuser BIGINT,
    p_sessionlanguage BIGINT,
    p_sessioncurrency BIGINT,
    p_active TINYINT,
    p_retry TINYINT,
    p_singleexecutable TINYINT,
    p_emailaddress NVARCHAR(255),
    p_sendemail TINYINT,
    p_changerecordingenabled TINYINT,
    p_requestabort TINYINT,
    p_requestabortstep TINYINT,
    p_priority INTEGER,
    p_removeonexit TINYINT,
    p_emailnotificationtemplate BIGINT,
    p_alternativedatasourceid NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_springid NVARCHAR(255),
    p_springidcronjobfactory NVARCHAR(255),
    p_scripturi NVARCHAR(255),
    p_threshold INTEGER,
    p_searchtype BIGINT,
    p_searchscript NVARCHAR(255),
    p_processscript NVARCHAR(255),
    p_maxthreads INTEGER,
    p_sourceversion BIGINT,
    p_targetversion BIGINT,
    p_exclusivemode TINYINT,
    p_syncprincipalsonly TINYINT,
    p_createnewitems TINYINT,
    p_removemissingitems TINYINT,
    p_syncorder INTEGER,
    p_copycachesize INTEGER,
    p_enabletransactions TINYINT,
    p_maxschedulerthreads INTEGER,
    p_activationscript LONGVARCHAR,
    PRIMARY KEY (PK)
);

CREATE INDEX IdxCode_500 ON jobs (p_code);


CREATE CACHED TABLE jobsearchrestriction
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_type BIGINT,
    p_jobpos INTEGER,
    p_job BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX jobRelIDX_508 ON jobsearchrestriction (p_job);

CREATE INDEX jobposPosIDX_508 ON jobsearchrestriction (p_jobpos);


CREATE CACHED TABLE jobslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE keywords
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_keyword NVARCHAR(255),
    p_language BIGINT,
    p_catalog BIGINT,
    p_catalogversion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_externalid NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX keywordIDX_602 ON keywords (p_keyword);

CREATE INDEX versionIDX_602 ON keywords (p_catalogversion);

CREATE UNIQUE INDEX codeVersionIDX_602 ON keywords (p_keyword, p_catalogversion);

CREATE INDEX extIDX_602 ON keywords (p_externalid);


CREATE CACHED TABLE languages
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_active TINYINT,
    p_isocode NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX ISOCode_32 ON languages (p_isocode);


CREATE CACHED TABLE languageslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE links
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_7 ON links (SourcePK);

CREATE INDEX qualifier_7 ON links (Qualifier);

CREATE INDEX seqnr_7 ON links (SequenceNumber);

CREATE INDEX rseqnr_7 ON links (RSequenceNumber);

CREATE INDEX linktarget_7 ON links (TargetPK);


CREATE CACHED TABLE maptypes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_extensionname NVARCHAR(255),
    p_autocreate TINYINT,
    p_generate TINYINT,
    InternalCode NVARCHAR(255),
    p_defaultvalue LONGVARBINARY,
    ArgumentTypePK BIGINT,
    ReturnTypePK BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    InternalCodeLowerCase NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX typecode_84 ON maptypes (InternalCode);

CREATE INDEX typecodelowercase_84 ON maptypes (InternalCodeLowerCase);


CREATE CACHED TABLE maptypeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE mediacontainer
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_qualifier NVARCHAR(255),
    p_catalogversion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX versionIDX_50 ON mediacontainer (p_catalogversion);

CREATE UNIQUE INDEX codeVersionIDX_50 ON mediacontainer (p_qualifier, p_catalogversion);


CREATE CACHED TABLE mediacontainerlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE mediacontext
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_qualifier NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX qualifierIDX_52 ON mediacontext (p_qualifier);


CREATE CACHED TABLE mediacontextlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE mediafolders
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_qualifier NVARCHAR(255),
    p_path NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX qualifierIdx_54 ON mediafolders (p_qualifier);


CREATE CACHED TABLE mediaformat
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_qualifier NVARCHAR(255),
    p_externalid NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX qualifierIDX_51 ON mediaformat (p_qualifier);


CREATE CACHED TABLE mediaformatlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE mediaformatmapping
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_source BIGINT,
    p_target BIGINT,
    p_mediacontext BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX mediacontextRelIDX_53 ON mediaformatmapping (p_mediacontext);


CREATE CACHED TABLE mediaprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT NOT NULL,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255) NOT NULL,
    LANGPK BIGINT NOT NULL,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX itempk_mediaprops ON mediaprops (ITEMPK);

CREATE INDEX nameidx_mediaprops ON mediaprops (NAME);

CREATE INDEX realnameidx_mediaprops ON mediaprops (REALNAME);


CREATE CACHED TABLE medias
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_mime NVARCHAR(255),
    p_size BIGINT,
    p_datapk BIGINT,
    p_location LONGVARCHAR,
    p_locationhash NVARCHAR(255),
    p_realfilename NVARCHAR(255),
    p_code NVARCHAR(255),
    p_internalurl LONGVARCHAR,
    p_description NVARCHAR(255),
    p_alttext NVARCHAR(255),
    p_removable TINYINT,
    p_mediaformat BIGINT,
    p_folder BIGINT,
    p_subfolderpath NVARCHAR(255),
    p_mediacontainer BIGINT,
    p_catalog BIGINT,
    p_catalogversion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_outputmimetype NVARCHAR(255),
    p_inputmimetype NVARCHAR(255),
    p_itemtimestamp TIMESTAMP,
    p_format BIGINT,
    p_sourceitem BIGINT,
    p_fieldseparator SMALLINT,
    p_quotecharacter SMALLINT,
    p_commentcharacter SMALLINT,
    p_encoding BIGINT,
    p_linestoskip INTEGER,
    p_removeonsuccess TINYINT,
    p_zipentry NVARCHAR(255),
    p_allowscriptevaluation TINYINT,
    p_scheduledcount INTEGER,
    p_cronjobpos INTEGER,
    p_cronjob BIGINT,
    p_icon BIGINT,
    p_compiledreport BIGINT,
    p_barcodetext NVARCHAR(255),
    p_barcodetype BIGINT,
    p_contextitem BIGINT,
    p_deeplinkurl BIGINT,
    p_message BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX dataPK_idx_30 ON medias (p_datapk);

CREATE INDEX Media_Code_30 ON medias (p_code);

CREATE INDEX versionIDX_30 ON medias (p_catalogversion);

CREATE UNIQUE INDEX codeVersionIDX_30 ON medias (p_code, p_catalogversion);

CREATE INDEX mediacontainerRelIDX_30 ON medias (p_mediacontainer);

CREATE INDEX sourceitemRelIDX_30 ON medias (p_sourceitem);

CREATE INDEX cronjobRelIDX_30 ON medias (p_cronjob);

CREATE INDEX cronjobposPosIDX_30 ON medias (p_cronjobpos);

CREATE INDEX deeplinkurlRelIDX_30 ON medias (p_deeplinkurl);

CREATE INDEX messageRelIDX_30 ON medias (p_message);


CREATE CACHED TABLE mediaslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_title NVARCHAR(255),
    p_reportdescription NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE metainformations
(
    hjmpTS BIGINT,
    PK BIGINT NOT NULL,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    aCLTS BIGINT DEFAULT 0,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    propTS BIGINT DEFAULT 0,
    SystemPK NVARCHAR(255),
    SystemName NVARCHAR(255),
    isInitialized TINYINT DEFAULT 0,
    LicenceID NVARCHAR(255),
    LicenceName NVARCHAR(255),
    LicenceEdition NVARCHAR(255),
    AdminFactor INTEGER DEFAULT 0,
    LicenceExpirationDate TIMESTAMP,
    LicenceSignature NVARCHAR(255),
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE numberseries
(
    hjmpTS BIGINT,
    serieskey NVARCHAR(255) NOT NULL,
    seriestype INTEGER DEFAULT 0,
    currentValue BIGINT,
    template NVARCHAR(255),
    PRIMARY KEY (serieskey)
);


CREATE CACHED TABLE oauthaccesstoken
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_tokenid NVARCHAR(255),
    p_token LONGVARBINARY,
    p_authenticationid NVARCHAR(255),
    p_client BIGINT,
    p_authentication LONGVARBINARY,
    p_refreshtoken BIGINT,
    p_user BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX tokenIdIdx_6228 ON oauthaccesstoken (p_tokenid);

CREATE UNIQUE INDEX authenticationIdIdx_6228 ON oauthaccesstoken (p_authenticationid);

CREATE INDEX refreshTokenIdx_6228 ON oauthaccesstoken (p_refreshtoken);

CREATE INDEX userRelIDX_6228 ON oauthaccesstoken (p_user);


CREATE CACHED TABLE oauthauthorizationcode
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_authentication LONGVARBINARY,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX codeIdx_6231 ON oauthauthorizationcode (p_code);


CREATE CACHED TABLE oauthclientdetails
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_clientid NVARCHAR(255),
    p_resourceids LONGVARBINARY,
    p_clientsecret NVARCHAR(255),
    p_scope LONGVARBINARY,
    p_authorizedgranttypes LONGVARBINARY,
    p_registeredredirecturi LONGVARBINARY,
    p_authorities LONGVARBINARY,
    p_accesstokenvalidityseconds INTEGER,
    p_refreshtokenvalidityseconds INTEGER,
    p_autoapprove LONGVARBINARY,
    p_disabled TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX clientIdIdx_6500 ON oauthclientdetails (p_clientid);


CREATE CACHED TABLE oauthrefreshtoken
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_tokenid NVARCHAR(255),
    p_token LONGVARBINARY,
    p_authentication LONGVARBINARY,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX tokenIdIdx_6229 ON oauthrefreshtoken (p_tokenid);


CREATE CACHED TABLE openingdays
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_openingtime TIMESTAMP,
    p_closingtime TIMESTAMP,
    p_openingschedule BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_date TIMESTAMP,
    p_closed TINYINT,
    p_dayofweek BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX openingscheduleRelIDX_2021 ON openingdays (p_openingschedule);


CREATE CACHED TABLE openingdayslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_message NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE openingschedules
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE openingscheduleslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE ordercancelconfigs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_ordercancelallowed TINYINT,
    p_cancelafterwarehouseallowed TINYINT,
    p_completecancelaftershippings TINYINT,
    p_partialcancelallowed TINYINT,
    p_partialorderentrycancelallow TINYINT,
    p_queuedorderwaitingtime INTEGER,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE orderdiscrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_202 ON orderdiscrels (SourcePK);

CREATE INDEX qualifier_202 ON orderdiscrels (Qualifier);

CREATE INDEX seqnr_202 ON orderdiscrels (SequenceNumber);

CREATE INDEX rseqnr_202 ON orderdiscrels (RSequenceNumber);

CREATE INDEX linktarget_202 ON orderdiscrels (TargetPK);


CREATE CACHED TABLE orderentmodrecentrs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_notes NVARCHAR(255),
    p_originalorderentry BIGINT,
    p_orderentry BIGINT,
    p_modificationrecordentry BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_cancelrequestquantity INTEGER,
    p_cancelledquantity INTEGER,
    p_cancelreason BIGINT,
    p_expectedquantity BIGINT,
    p_returnedquantity BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX modificationrecordentryRelIDX_2018 ON orderentmodrecentrs (p_modificationrecordentry);


CREATE CACHED TABLE orderentries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_baseprice DECIMAL(30,8),
    p_calculated TINYINT,
    p_discountvaluesinternal LONGVARCHAR,
    p_entrynumber INTEGER,
    p_info LONGVARCHAR,
    p_product BIGINT,
    p_quantity DECIMAL(30,8),
    p_taxvaluesinternal NVARCHAR(255),
    p_totalprice DECIMAL(30,8),
    p_unit BIGINT,
    p_giveaway TINYINT,
    p_rejected TINYINT,
    p_order BIGINT,
    p_chosenvendor BIGINT,
    p_deliveryaddress BIGINT,
    p_deliverymode BIGINT,
    p_nameddeliverydate TIMESTAMP,
    p_quantitystatus BIGINT,
    p_deliverypointofservice BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX oeProd_46 ON orderentries (p_product);

CREATE INDEX oeOrd_46 ON orderentries (p_order);


CREATE CACHED TABLE orderentryconsumed
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_promotionresult BIGINT,
    p_quantity BIGINT,
    p_adjustedunitprice DOUBLE,
    p_orderentry BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX promotionResultIDX_5014 ON orderentryconsumed (p_promotionresult);


CREATE CACHED TABLE orderentryprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT NOT NULL,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255) NOT NULL,
    LANGPK BIGINT NOT NULL,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX itempk_orderentryprops ON orderentryprops (ITEMPK);

CREATE INDEX realnameidx_orderentryprops ON orderentryprops (REALNAME);

CREATE INDEX nameidx_orderentryprops ON orderentryprops (NAME);


CREATE CACHED TABLE orderhistoryentries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_timestamp TIMESTAMP,
    p_employee BIGINT,
    p_description NVARCHAR(255),
    p_previousorderversion BIGINT,
    p_orderpos INTEGER,
    p_order BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX orderRelIDX_2002 ON orderhistoryentries (p_order);

CREATE INDEX orderposPosIDX_2002 ON orderhistoryentries (p_orderpos);


CREATE CACHED TABLE ordermodifirecentrs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_timestamp TIMESTAMP,
    p_status BIGINT,
    p_originalversion BIGINT,
    p_principal BIGINT,
    p_failedmessage NVARCHAR(255),
    p_notes NVARCHAR(255),
    p_modificationrecord BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_refusedmessage NVARCHAR(255),
    p_cancelresult BIGINT,
    p_cancelreason BIGINT,
    p_returnstatus BIGINT,
    p_expectedquantity BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX modificationrecordRelIDX_2017 ON ordermodifirecentrs (p_modificationrecord);


CREATE CACHED TABLE ordermodifirecords
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_inprogress TINYINT,
    p_identifier NVARCHAR(255),
    p_order BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX orderRelIDX_2016 ON ordermodifirecords (p_order);


CREATE CACHED TABLE orderprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT NOT NULL,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255) NOT NULL,
    LANGPK BIGINT NOT NULL,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX itempk_orderprops ON orderprops (ITEMPK);

CREATE INDEX nameidx_orderprops ON orderprops (NAME);

CREATE INDEX realnameidx_orderprops ON orderprops (REALNAME);


CREATE CACHED TABLE orders
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_calculated TINYINT,
    p_code NVARCHAR(255),
    p_currency BIGINT,
    p_deliveryaddress BIGINT,
    p_deliverycost DECIMAL(30,8),
    p_deliverymode BIGINT,
    p_deliverystatus BIGINT,
    p_globaldiscountvaluesinternal LONGVARCHAR,
    p_net TINYINT,
    p_paymentaddress BIGINT,
    p_paymentcost DECIMAL(30,8),
    p_paymentinfo BIGINT,
    p_paymentmode BIGINT,
    p_paymentstatus BIGINT,
    p_status BIGINT,
    p_exportstatus BIGINT,
    p_statusinfo NVARCHAR(255),
    p_totalprice DECIMAL(30,8),
    p_totaldiscounts DECIMAL(30,8),
    p_totaltax DECIMAL(30,8),
    p_totaltaxvaluesinternal LONGVARCHAR,
    p_user BIGINT,
    p_subtotal DECIMAL(30,8),
    p_discountsincludedeliverycost TINYINT,
    p_discountsincludepaymentcost TINYINT,
    p_previousdeliverymode BIGINT,
    p_site BIGINT,
    p_store BIGINT,
    p_guid NVARCHAR(255),
    p_appliedcouponcodes LONGVARBINARY,
    p_versionid NVARCHAR(255),
    p_originalversion BIGINT,
    p_fraudulent TINYINT,
    p_potentiallyfraudulent TINYINT,
    p_salesapplication BIGINT,
    p_language BIGINT,
    p_placedby BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_fulfilmentstatus BIGINT,
    p_notes NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX OrderCode_45 ON orders (p_code);

CREATE INDEX OrderUser_45 ON orders (p_user);

CREATE INDEX guidIndex_45 ON orders (p_guid);

CREATE INDEX currencyPK_idx_45 ON orders (p_currency);

CREATE INDEX paymentAddressPK_idx_45 ON orders (p_paymentaddress);

CREATE INDEX placedByIndex_45 ON orders (p_placedby);


CREATE CACHED TABLE pagetemplate
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_name NVARCHAR(255),
    p_catalogversion BIGINT,
    p_active TINYINT,
    p_frontendtemplatename NVARCHAR(255),
    p_previewicon BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_htmltemplate BIGINT,
    p_subject BIGINT,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE pagetypesfortemplates
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1095 ON pagetypesfortemplates (SourcePK);

CREATE INDEX qualifier_1095 ON pagetypesfortemplates (Qualifier);

CREATE INDEX seqnr_1095 ON pagetypesfortemplates (SequenceNumber);

CREATE INDEX rseqnr_1095 ON pagetypesfortemplates (RSequenceNumber);

CREATE INDEX linktarget_1095 ON pagetypesfortemplates (TargetPK);


CREATE CACHED TABLE parserproperty
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_startexp NVARCHAR(255),
    p_endexp NVARCHAR(255),
    p_parserclass NVARCHAR(255),
    p_translatorconfigurationpos INTEGER,
    p_translatorconfiguration BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX translatorconfigurationRelIDX_13213 ON parserproperty (p_translatorconfiguration);

CREATE INDEX translatorconfigurationposPosIDX_13213 ON parserproperty (p_translatorconfigurationpos);


CREATE CACHED TABLE paymentinfos
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_original BIGINT,
    p_code NVARCHAR(255),
    p_user BIGINT,
    p_duplicate TINYINT,
    p_billingaddress BIGINT,
    p_saved TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_ccowner NVARCHAR(255),
    p_number NVARCHAR(255),
    p_type BIGINT,
    p_validtomonth NVARCHAR(255),
    p_validtoyear NVARCHAR(255),
    p_validfrommonth NVARCHAR(255),
    p_validfromyear NVARCHAR(255),
    p_subscriptionid NVARCHAR(255),
    p_issuenumber INTEGER,
    p_subscriptionvalidated TINYINT,
    p_bankidnumber NVARCHAR(255),
    p_bank NVARCHAR(255),
    p_accountnumber NVARCHAR(255),
    p_baowner NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX PaymentInfo_User_42 ON paymentinfos (p_user);

CREATE INDEX Subscription_42 ON paymentinfos (p_subscriptionid);


CREATE CACHED TABLE paymentmodes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_active TINYINT,
    p_code NVARCHAR(255),
    p_paymentinfotype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_net TINYINT,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE paymentmodeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description LONGVARCHAR,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE paymentsubscresult
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_cartid NVARCHAR(255),
    p_success TINYINT,
    p_result LONGVARBINARY,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX cartIdIdx_6230 ON paymentsubscresult (p_cartid);


CREATE CACHED TABLE paymenttransactions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_requestid NVARCHAR(255),
    p_requesttoken NVARCHAR(255),
    p_paymentprovider NVARCHAR(255),
    p_plannedamount DECIMAL(30,8),
    p_currency BIGINT,
    p_info BIGINT,
    p_versionid NVARCHAR(255),
    p_order BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX transUniqueIdx_2100 ON paymenttransactions (p_code, p_order, p_versionid);

CREATE INDEX orderRelIDX_2100 ON paymenttransactions (p_order);


CREATE CACHED TABLE paymnttrnsctentries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_type BIGINT,
    p_amount DECIMAL(30,8),
    p_currency BIGINT,
    p_time TIMESTAMP,
    p_transactionstatus NVARCHAR(255),
    p_transactionstatusdetails NVARCHAR(255),
    p_requesttoken NVARCHAR(255),
    p_requestid NVARCHAR(255),
    p_subscriptionid NVARCHAR(255),
    p_code NVARCHAR(255),
    p_versionid NVARCHAR(255),
    p_paymenttransaction BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX transcodeIdx_2101 ON paymnttrnsctentries (p_code);

CREATE UNIQUE INDEX transEntryUniqueIdx_2101 ON paymnttrnsctentries (p_code, p_paymenttransaction, p_type, p_versionid);

CREATE INDEX paymenttransactionRelIDX_2101 ON paymnttrnsctentries (p_paymenttransaction);


CREATE CACHED TABLE pcp2wrtblecvrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_617 ON pcp2wrtblecvrel (SourcePK);

CREATE INDEX qualifier_617 ON pcp2wrtblecvrel (Qualifier);

CREATE INDEX seqnr_617 ON pcp2wrtblecvrel (SequenceNumber);

CREATE INDEX rseqnr_617 ON pcp2wrtblecvrel (RSequenceNumber);

CREATE INDEX linktarget_617 ON pcp2wrtblecvrel (TargetPK);


CREATE CACHED TABLE pcpl2rdblecvrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_618 ON pcpl2rdblecvrel (SourcePK);

CREATE INDEX qualifier_618 ON pcpl2rdblecvrel (Qualifier);

CREATE INDEX seqnr_618 ON pcpl2rdblecvrel (SequenceNumber);

CREATE INDEX rseqnr_618 ON pcpl2rdblecvrel (RSequenceNumber);

CREATE INDEX linktarget_618 ON pcpl2rdblecvrel (TargetPK);


CREATE CACHED TABLE pendingstepsrelation
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_507 ON pendingstepsrelation (SourcePK);

CREATE INDEX qualifier_507 ON pendingstepsrelation (Qualifier);

CREATE INDEX seqnr_507 ON pendingstepsrelation (SequenceNumber);

CREATE INDEX rseqnr_507 ON pendingstepsrelation (RSequenceNumber);

CREATE INDEX linktarget_507 ON pendingstepsrelation (TargetPK);


CREATE CACHED TABLE pgrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_201 ON pgrels (SourcePK);

CREATE INDEX qualifier_201 ON pgrels (Qualifier);

CREATE INDEX seqnr_201 ON pgrels (SequenceNumber);

CREATE INDEX rseqnr_201 ON pgrels (RSequenceNumber);

CREATE INDEX linktarget_201 ON pgrels (TargetPK);


CREATE CACHED TABLE pointofservice
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_address BIGINT,
    p_description NVARCHAR(255),
    p_type BIGINT,
    p_mapicon BIGINT,
    p_latitude DOUBLE,
    p_longitude DOUBLE,
    p_geocodetimestamp TIMESTAMP,
    p_openingschedule BIGINT,
    p_storeimage BIGINT,
    p_basestore BIGINT,
    p_displayname NVARCHAR(255),
    p_nearbystoreradius DOUBLE,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX basestoreRelIDX_2040 ON pointofservice (p_basestore);


CREATE CACHED TABLE pointofservicelp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_storecontent LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE pos2warehouserel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6217 ON pos2warehouserel (SourcePK);

CREATE INDEX qualifier_6217 ON pos2warehouserel (Qualifier);

CREATE INDEX seqnr_6217 ON pos2warehouserel (SequenceNumber);

CREATE INDEX rseqnr_6217 ON pos2warehouserel (RSequenceNumber);

CREATE INDEX linktarget_6217 ON pos2warehouserel (TargetPK);


CREATE CACHED TABLE previewdata
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_user BIGINT,
    p_usergroup BIGINT,
    p_country BIGINT,
    p_language BIGINT,
    p_time TIMESTAMP,
    p_liveedit TINYINT,
    p_editmode TINYINT,
    p_resourcepath LONGVARCHAR,
    p_page BIGINT,
    p_previewcategory BIGINT,
    p_previewproduct BIGINT,
    p_previewcatalog BIGINT,
    p_activesite BIGINT,
    p_activecatalogversion BIGINT,
    p_liveeditvariant BIGINT,
    p_uiexperience BIGINT,
    p_viewport NVARCHAR(255),
    p_devicesupport BIGINT,
    p_deviceorientation BIGINT,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE previewdatatocatvers
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1101 ON previewdatatocatvers (SourcePK);

CREATE INDEX qualifier_1101 ON previewdatatocatvers (Qualifier);

CREATE INDEX seqnr_1101 ON previewdatatocatvers (SequenceNumber);

CREATE INDEX rseqnr_1101 ON previewdatatocatvers (RSequenceNumber);

CREATE INDEX linktarget_1101 ON previewdatatocatvers (TargetPK);


CREATE CACHED TABLE previewtickets
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_previewcatalogversion BIGINT,
    p_validto TIMESTAMP,
    p_createdby BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE previewtovariation
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_3700 ON previewtovariation (SourcePK);

CREATE INDEX qualifier_3700 ON previewtovariation (Qualifier);

CREATE INDEX seqnr_3700 ON previewtovariation (SequenceNumber);

CREATE INDEX rseqnr_3700 ON previewtovariation (RSequenceNumber);

CREATE INDEX linktarget_3700 ON previewtovariation (TargetPK);


CREATE CACHED TABLE pricerows
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_product BIGINT,
    p_pg BIGINT,
    p_productmatchqualifier BIGINT,
    p_starttime TIMESTAMP,
    p_endtime TIMESTAMP,
    p_user BIGINT,
    p_ug BIGINT,
    p_usermatchqualifier BIGINT,
    p_productid NVARCHAR(255),
    p_catalogversion BIGINT,
    p_matchvalue INTEGER,
    p_currency BIGINT,
    p_minqtd BIGINT,
    p_net TINYINT,
    p_price DOUBLE,
    p_unit BIGINT,
    p_unitfactor INTEGER,
    p_giveawayprice TINYINT,
    p_channel BIGINT,
    p_sequenceid BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX MatchIndexP_1055 ON pricerows (p_productmatchqualifier);

CREATE INDEX MatchIndexU_1055 ON pricerows (p_usermatchqualifier);

CREATE INDEX PIdx_1055 ON pricerows (p_product);

CREATE INDEX UIdx_1055 ON pricerows (p_user);

CREATE INDEX PGIdx_1055 ON pricerows (p_pg);

CREATE INDEX UGIdx_1055 ON pricerows (p_ug);

CREATE INDEX ProductIdIdx_1055 ON pricerows (p_productid);

CREATE INDEX versionIDX_1055 ON pricerows (p_catalogversion);


CREATE CACHED TABLE principcockpitreadrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1714 ON principcockpitreadrels (SourcePK);

CREATE INDEX qualifier_1714 ON principcockpitreadrels (Qualifier);

CREATE INDEX seqnr_1714 ON principcockpitreadrels (SequenceNumber);

CREATE INDEX rseqnr_1714 ON principcockpitreadrels (RSequenceNumber);

CREATE INDEX linktarget_1714 ON principcockpitreadrels (TargetPK);


CREATE CACHED TABLE principcockpitwriterels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1715 ON principcockpitwriterels (SourcePK);

CREATE INDEX qualifier_1715 ON principcockpitwriterels (Qualifier);

CREATE INDEX seqnr_1715 ON principcockpitwriterels (SequenceNumber);

CREATE INDEX rseqnr_1715 ON principcockpitwriterels (RSequenceNumber);

CREATE INDEX linktarget_1715 ON principcockpitwriterels (TargetPK);


CREATE CACHED TABLE princtolinkrelations
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6001 ON princtolinkrelations (SourcePK);

CREATE INDEX qualifier_6001 ON princtolinkrelations (Qualifier);

CREATE INDEX seqnr_6001 ON princtolinkrelations (SequenceNumber);

CREATE INDEX rseqnr_6001 ON princtolinkrelations (RSequenceNumber);

CREATE INDEX linktarget_6001 ON princtolinkrelations (TargetPK);


CREATE CACHED TABLE processedstepsrelation
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_506 ON processedstepsrelation (SourcePK);

CREATE INDEX qualifier_506 ON processedstepsrelation (Qualifier);

CREATE INDEX seqnr_506 ON processedstepsrelation (SequenceNumber);

CREATE INDEX rseqnr_506 ON processedstepsrelation (RSequenceNumber);

CREATE INDEX linktarget_506 ON processedstepsrelation (TargetPK);


CREATE CACHED TABLE processes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_processdefinitionname NVARCHAR(255),
    p_processdefinitionversion NVARCHAR(255),
    p_state BIGINT,
    p_endmessage LONGVARCHAR,
    p_user BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_consignment BIGINT,
    p_parentprocess BIGINT,
    p_done TINYINT,
    p_waitingforconsignment TINYINT,
    p_warehouseconsignmentstate BIGINT,
    p_order BIGINT,
    p_ordermodificationrecordentry BIGINT,
    p_returnrequest BIGINT,
    p_site BIGINT,
    p_store BIGINT,
    p_customer BIGINT,
    p_language BIGINT,
    p_currency BIGINT,
    p_token NVARCHAR(255),
    p_uploadedfile BIGINT,
    p_savedcart BIGINT,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX ProcessengineProcess_name_idx_32766 ON processes (p_code);

CREATE INDEX consignmentRelIDX_32766 ON processes (p_consignment);

CREATE INDEX orderRelIDX_32766 ON processes (p_order);

CREATE INDEX parentprocessRelIDX_32766 ON processes (p_parentprocess);

CREATE INDEX returnrequestRelIDX_32766 ON processes (p_returnrequest);


CREATE CACHED TABLE processparameters
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_value LONGVARBINARY,
    p_process BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX BusinessProcessParameter_idx_32764 ON processparameters (p_process, p_name);

CREATE INDEX processRelIDX_32764 ON processparameters (p_process);


CREATE CACHED TABLE prod2keywordrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_604 ON prod2keywordrel (SourcePK);

CREATE INDEX qualifier_604 ON prod2keywordrel (Qualifier);

CREATE INDEX seqnr_604 ON prod2keywordrel (SequenceNumber);

CREATE INDEX rseqnr_604 ON prod2keywordrel (RSequenceNumber);

CREATE INDEX linktarget_604 ON prod2keywordrel (TargetPK);


CREATE CACHED TABLE prodforpromosourcerule
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_productcode NVARCHAR(255),
    p_rule BIGINT,
    p_promotion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX ProdForPromoSourceRule_IDX_5402 ON prodforpromosourcerule (p_productcode, p_rule);


CREATE CACHED TABLE prodpromrelations
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_5018 ON prodpromrelations (SourcePK);

CREATE INDEX qualifier_5018 ON prodpromrelations (Qualifier);

CREATE INDEX seqnr_5018 ON prodpromrelations (SequenceNumber);

CREATE INDEX rseqnr_5018 ON prodpromrelations (RSequenceNumber);

CREATE INDEX linktarget_5018 ON prodpromrelations (TargetPK);


CREATE CACHED TABLE prodsforprodcarcomprels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_3002 ON prodsforprodcarcomprels (SourcePK);

CREATE INDEX qualifier_3002 ON prodsforprodcarcomprels (Qualifier);

CREATE INDEX seqnr_3002 ON prodsforprodcarcomprels (SequenceNumber);

CREATE INDEX rseqnr_3002 ON prodsforprodcarcomprels (RSequenceNumber);

CREATE INDEX linktarget_3002 ON prodsforprodcarcomprels (TargetPK);


CREATE CACHED TABLE prodsforprodlistcomprels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_3001 ON prodsforprodlistcomprels (SourcePK);

CREATE INDEX qualifier_3001 ON prodsforprodlistcomprels (Qualifier);

CREATE INDEX seqnr_3001 ON prodsforprodlistcomprels (SequenceNumber);

CREATE INDEX rseqnr_3001 ON prodsforprodlistcomprels (RSequenceNumber);

CREATE INDEX linktarget_3001 ON prodsforprodlistcomprels (TargetPK);


CREATE CACHED TABLE productdelimoderel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_2011 ON productdelimoderel (SourcePK);

CREATE INDEX qualifier_2011 ON productdelimoderel (Qualifier);

CREATE INDEX seqnr_2011 ON productdelimoderel (SequenceNumber);

CREATE INDEX rseqnr_2011 ON productdelimoderel (RSequenceNumber);

CREATE INDEX linktarget_2011 ON productdelimoderel (TargetPK);


CREATE CACHED TABLE productfeatures
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_qualifier NVARCHAR(255),
    p_classificationattributeassig BIGINT,
    p_language BIGINT,
    p_valueposition INTEGER,
    p_featureposition INTEGER,
    p_valuetype INTEGER,
    p_stringvalue LONGVARCHAR,
    p_booleanvalue TINYINT,
    p_numbervalue DECIMAL(30,8),
    p_rawvalue LONGVARBINARY,
    p_unit BIGINT,
    p_valuedetails NVARCHAR(255),
    p_description NVARCHAR(255),
    p_productpos INTEGER,
    p_product BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX featureIDX_611 ON productfeatures (p_product);

CREATE INDEX featureIDX2_611 ON productfeatures (p_qualifier);

CREATE INDEX featureIDX3_611 ON productfeatures (p_classificationattributeassig);

CREATE INDEX featureIDX4_611 ON productfeatures (p_product, p_classificationattributeassig, p_featureposition, p_language, p_valueposition);

CREATE INDEX productposPosIDX_611 ON productfeatures (p_productpos);


CREATE CACHED TABLE productorderlimits
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_intervalresolution BIGINT,
    p_intervalvalue INTEGER,
    p_intervalmaxordersnumber INTEGER,
    p_maxnumberperorder INTEGER,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE productprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT NOT NULL,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255) NOT NULL,
    LANGPK BIGINT NOT NULL,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX realnameidx_productprops ON productprops (REALNAME);

CREATE INDEX nameidx_productprops ON productprops (NAME);

CREATE INDEX itempk_productprops ON productprops (ITEMPK);


CREATE CACHED TABLE productreferences
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_qualifier NVARCHAR(255),
    p_target BIGINT,
    p_quantity INTEGER,
    p_referencetype BIGINT,
    p_icon BIGINT,
    p_preselected TINYINT,
    p_active TINYINT,
    p_sourcepos INTEGER,
    p_source BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX targetIDX_606 ON productreferences (p_target);

CREATE INDEX qualifierIDX_606 ON productreferences (p_qualifier);

CREATE INDEX sourceRelIDX_606 ON productreferences (p_source);

CREATE INDEX sourceposPosIDX_606 ON productreferences (p_sourcepos);


CREATE CACHED TABLE productreferenceslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE products
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_unit BIGINT,
    p_thumbnail BIGINT,
    p_picture BIGINT,
    p_catalog BIGINT,
    p_catalogversion BIGINT,
    p_onlinedate TIMESTAMP,
    p_offlinedate TIMESTAMP,
    p_ean NVARCHAR(255),
    p_supplieralternativeaid NVARCHAR(255),
    p_buyerids LONGVARBINARY,
    p_manufactureraid NVARCHAR(255),
    p_manufacturername NVARCHAR(255),
    p_erpgroupbuyer NVARCHAR(255),
    p_erpgroupsupplier NVARCHAR(255),
    p_deliverytime DOUBLE,
    p_specialtreatmentclasses LONGVARBINARY,
    p_order INTEGER,
    p_approvalstatus BIGINT,
    p_contentunit BIGINT,
    p_numbercontentunits DOUBLE,
    p_minorderquantity INTEGER,
    p_maxorderquantity INTEGER,
    p_orderquantityinterval INTEGER,
    p_pricequantity DOUBLE,
    p_normal LONGVARCHAR,
    p_thumbnails LONGVARCHAR,
    p_detail LONGVARCHAR,
    p_logo LONGVARCHAR,
    p_data_sheet LONGVARCHAR,
    p_others LONGVARCHAR,
    p_startlinenumber INTEGER,
    p_endlinenumber INTEGER,
    p_varianttype BIGINT,
    p_europe1pricefactory_ppg BIGINT,
    p_europe1pricefactory_ptg BIGINT,
    p_europe1pricefactory_pdg BIGINT,
    p_productorderlimit BIGINT,
    p_galleryimages LONGVARCHAR,
    p_sequenceid BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_baseproduct BIGINT,
    p_swatchcolors LONGVARCHAR,
    p_genders LONGVARCHAR,
    PRIMARY KEY (PK)
);

CREATE INDEX Product_Code_1 ON products (p_code);

CREATE INDEX catalogIDX_1 ON products (p_catalog);

CREATE INDEX visibilityIDX_1 ON products (p_approvalstatus, p_onlinedate, p_offlinedate);

CREATE UNIQUE INDEX codeVersionIDX_1 ON products (p_code, p_catalogversion);

CREATE INDEX versionIDX_1 ON products (p_catalogversion);

CREATE INDEX baseproductRelIDX_1 ON products (p_baseproduct);

CREATE INDEX productorderlimitRelIDX_1 ON products (p_productorderlimit);


CREATE CACHED TABLE products4restriction
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1081 ON products4restriction (SourcePK);

CREATE INDEX qualifier_1081 ON products4restriction (Qualifier);

CREATE INDEX seqnr_1081 ON products4restriction (SequenceNumber);

CREATE INDEX rseqnr_1081 ON products4restriction (RSequenceNumber);

CREATE INDEX linktarget_1081 ON products4restriction (TargetPK);


CREATE CACHED TABLE productslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    p_manufacturertypedescription NVARCHAR(255),
    p_segment NVARCHAR(255),
    p_articlestatus LONGVARBINARY,
    p_summary LONGVARCHAR,
    p_style NVARCHAR(255),
    p_size NVARCHAR(255),
    p_color NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE producttaxcodes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_productcode NVARCHAR(255),
    p_taxarea NVARCHAR(255),
    p_taxcode NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX taxCodeLookup_2026 ON producttaxcodes (p_productcode, p_taxarea);

CREATE INDEX taxCode4Product_2026 ON producttaxcodes (p_productcode);


CREATE CACHED TABLE productvendorrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_2010 ON productvendorrel (SourcePK);

CREATE INDEX qualifier_2010 ON productvendorrel (Qualifier);

CREATE INDEX seqnr_2010 ON productvendorrel (SequenceNumber);

CREATE INDEX rseqnr_2010 ON productvendorrel (RSequenceNumber);

CREATE INDEX linktarget_2010 ON productvendorrel (TargetPK);


CREATE CACHED TABLE promotion
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_title NVARCHAR(255),
    p_description LONGVARCHAR,
    p_startdate TIMESTAMP,
    p_enddate TIMESTAMP,
    p_detailsurl NVARCHAR(255),
    p_enabled TINYINT,
    p_priority INTEGER,
    p_immutablekeyhash NVARCHAR(255),
    p_immutablekey LONGVARCHAR,
    p_promotiongroup BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_productbanner BIGINT,
    p_productfixedunitprice LONGVARCHAR,
    p_percentagediscount DOUBLE,
    p_qualifyingcount INTEGER,
    p_freecount INTEGER,
    p_bundleprices LONGVARCHAR,
    p_qualifyingcountsandbundlepri LONGVARCHAR,
    p_partnerproducts LONGVARCHAR,
    p_partnerprices LONGVARCHAR,
    p_partnerproduct BIGINT,
    p_thresholdtotals LONGVARCHAR,
    p_discountproduct BIGINT,
    p_productprices LONGVARCHAR,
    p_includediscountedpriceinthre TINYINT,
    p_discountprices LONGVARCHAR,
    p_giftproduct BIGINT,
    p_freevoucher BIGINT,
    p_deliverymode BIGINT,
    p_rule BIGINT,
    p_ruleversion BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX codeIDX_5010 ON promotion (p_code);

CREATE INDEX enabledIDX_5010 ON promotion (p_enabled);

CREATE INDEX immutableKeyHashIDX_5010 ON promotion (p_immutablekeyhash);

CREATE INDEX promotiongroupRelIDX_5010 ON promotion (p_promotiongroup);


CREATE CACHED TABLE promotionaction
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_markedapplied TINYINT,
    p_guid NVARCHAR(255),
    p_promotionresult BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_amount DOUBLE,
    p_orderentryproduct BIGINT,
    p_orderentryquantity BIGINT,
    p_orderentrynumber INTEGER,
    p_freeproduct BIGINT,
    p_deliverymode BIGINT,
    p_usedcouponcodes LONGVARBINARY,
    p_rule BIGINT,
    p_strategyid NVARCHAR(255),
    p_metadatahandlers LONGVARBINARY,
    p_amoun0 DECIMAL(30,8),
    p_product BIGINT,
    p_quantity BIGINT,
    p_deliverycost DECIMAL(30,8),
    p_replaceddeliverymode BIGINT,
    p_replaceddeliverycost DECIMAL(30,8),
    PRIMARY KEY (PK)
);

CREATE INDEX promotionResultIDX_5015 ON promotionaction (p_promotionresult);


CREATE CACHED TABLE promotiongroup
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_identifier NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX PromotionGroup_IdentifierIDX_5017 ON promotiongroup (p_identifier);


CREATE CACHED TABLE promotionlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_messagefired LONGVARCHAR,
    p_messagecouldhavefired LONGVARCHAR,
    p_messageproductnothreshold LONGVARCHAR,
    p_messagethresholdnoproduct LONGVARCHAR,
    p_promotiondescription LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE promotionpricerow
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_currency BIGINT,
    p_price DOUBLE,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE promotionrestriction
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_promotion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_products LONGVARCHAR,
    p_positive TINYINT,
    p_users LONGVARCHAR,
    PRIMARY KEY (PK)
);

CREATE INDEX promotionIDX_5011 ON promotionrestriction (p_promotion);


CREATE CACHED TABLE promotionrestrictionlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_descriptionpattern LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE promotionresult
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_promotion BIGINT,
    p_certainty FLOAT,
    p_custom NVARCHAR(255),
    p_order BIGINT,
    p_moduleversion BIGINT,
    p_ruleversion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX orderIDX_5013 ON promotionresult (p_order);

CREATE INDEX promotionIDX_5013 ON promotionresult (p_promotion);


CREATE CACHED TABLE promrestr2orderrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6221 ON promrestr2orderrel (SourcePK);

CREATE INDEX qualifier_6221 ON promrestr2orderrel (Qualifier);

CREATE INDEX seqnr_6221 ON promrestr2orderrel (SequenceNumber);

CREATE INDEX rseqnr_6221 ON promrestr2orderrel (RSequenceNumber);

CREATE INDEX linktarget_6221 ON promrestr2orderrel (TargetPK);


CREATE CACHED TABLE props
(
    hjmpTS BIGINT,
    ITEMPK BIGINT NOT NULL,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255) NOT NULL,
    LANGPK BIGINT NOT NULL,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX realnameidx_props ON props (REALNAME);

CREATE INDEX itempk_props ON props (ITEMPK);

CREATE INDEX nameidx_props ON props (NAME);


CREATE CACHED TABLE quantityandpricesrow
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_quantity BIGINT,
    p_prices LONGVARCHAR,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE readcockpitcollrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1710 ON readcockpitcollrels (SourcePK);

CREATE INDEX qualifier_1710 ON readcockpitcollrels (Qualifier);

CREATE INDEX seqnr_1710 ON readcockpitcollrels (SequenceNumber);

CREATE INDEX rseqnr_1710 ON readcockpitcollrels (RSequenceNumber);

CREATE INDEX linktarget_1710 ON readcockpitcollrels (TargetPK);


CREATE CACHED TABLE readsavedqueryrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1717 ON readsavedqueryrels (SourcePK);

CREATE INDEX qualifier_1717 ON readsavedqueryrels (Qualifier);

CREATE INDEX seqnr_1717 ON readsavedqueryrels (SequenceNumber);

CREATE INDEX rseqnr_1717 ON readsavedqueryrels (RSequenceNumber);

CREATE INDEX linktarget_1717 ON readsavedqueryrels (TargetPK);


CREATE CACHED TABLE regions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_active TINYINT,
    p_isocode NVARCHAR(255),
    p_country BIGINT,
    p_isocodeshort NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX ISOCode_35 ON regions (p_isocode);

CREATE INDEX Region_Country_35 ON regions (p_country);


CREATE CACHED TABLE regionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE renderersproperty
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_key NVARCHAR(255),
    p_value NVARCHAR(255),
    p_translatorconfigurationpos INTEGER,
    p_translatorconfiguration BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX translatorconfigurationRelIDX_13212 ON renderersproperty (p_translatorconfiguration);

CREATE INDEX translatorconfigurationposPosIDX_13212 ON renderersproperty (p_translatorconfigurationpos);


CREATE CACHED TABLE renderertemplate
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_contextclass NVARCHAR(255),
    p_outputmimetype NVARCHAR(255),
    p_renderertype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE renderertemplatelp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    p_content BIGINT,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE restriction
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_name NVARCHAR(255),
    p_catalogversion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_originalrestriction BIGINT,
    p_recursive TINYINT,
    p_activefrom TIMESTAMP,
    p_activeuntil TIMESTAMP,
    p_includesubgroups TINYINT,
    p_uiexperience BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX originalrestrictionRelIDX_1074 ON restriction (p_originalrestriction);


CREATE CACHED TABLE restriction4pages
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1087 ON restriction4pages (SourcePK);

CREATE INDEX qualifier_1087 ON restriction4pages (Qualifier);

CREATE INDEX seqnr_1087 ON restriction4pages (SequenceNumber);

CREATE INDEX rseqnr_1087 ON restriction4pages (RSequenceNumber);

CREATE INDEX linktarget_1087 ON restriction4pages (TargetPK);


CREATE CACHED TABLE restrictions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_positive TINYINT,
    p_voucherpos INTEGER,
    p_voucher BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_products LONGVARCHAR,
    p_quantity BIGINT,
    p_unit BIGINT,
    p_categories LONGVARCHAR,
    p_total DOUBLE,
    p_currency BIGINT,
    p_net TINYINT,
    p_valueofgoodsonly TINYINT,
    p_startdate TIMESTAMP,
    p_enddate TIMESTAMP,
    p_users LONGVARCHAR,
    p_orderquantity INTEGER,
    p_allorderstotal DOUBLE,
    PRIMARY KEY (PK)
);

CREATE INDEX voucherRelIDX_1601 ON restrictions (p_voucher);

CREATE INDEX voucherposPosIDX_1601 ON restrictions (p_voucherpos);


CREATE CACHED TABLE restrictions4comps
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1091 ON restrictions4comps (SourcePK);

CREATE INDEX qualifier_1091 ON restrictions4comps (Qualifier);

CREATE INDEX seqnr_1091 ON restrictions4comps (SequenceNumber);

CREATE INDEX rseqnr_1091 ON restrictions4comps (RSequenceNumber);

CREATE INDEX linktarget_1091 ON restrictions4comps (TargetPK);


CREATE CACHED TABLE restrictionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    p_violationmessage NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE returnentry
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_orderentry BIGINT,
    p_expectedquantity BIGINT,
    p_receivedquantity BIGINT,
    p_reacheddate TIMESTAMP,
    p_status BIGINT,
    p_action BIGINT,
    p_notes NVARCHAR(255),
    p_returnrequestpos INTEGER,
    p_returnrequest BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_reason BIGINT,
    p_amount DECIMAL(30,8),
    p_refundeddate TIMESTAMP,
    PRIMARY KEY (PK)
);

CREATE INDEX returnrequestRelIDX_2052 ON returnentry (p_returnrequest);

CREATE INDEX returnrequestposPosIDX_2052 ON returnentry (p_returnrequestpos);


CREATE CACHED TABLE returnrequest
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_rma NVARCHAR(255),
    p_replacementorder BIGINT,
    p_currency BIGINT,
    p_status BIGINT,
    p_orderpos INTEGER,
    p_order BIGINT,
    p_refunddeliverycost TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX rmaIdx_2051 ON returnrequest (p_rma);

CREATE INDEX orderRelIDX_2051 ON returnrequest (p_order);

CREATE INDEX orderposPosIDX_2051 ON returnrequest (p_orderpos);


CREATE CACHED TABLE ruleactdefcatrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_5307 ON ruleactdefcatrel (SourcePK);

CREATE INDEX qualifier_5307 ON ruleactdefcatrel (Qualifier);

CREATE INDEX seqnr_5307 ON ruleactdefcatrel (SequenceNumber);

CREATE INDEX rseqnr_5307 ON ruleactdefcatrel (RSequenceNumber);

CREATE INDEX linktarget_5307 ON ruleactdefcatrel (TargetPK);


CREATE CACHED TABLE ruleactdefruletypemap
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_ruletype BIGINT,
    p_definition BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX definitionRelIDX_5309 ON ruleactdefruletypemap (p_definition);


CREATE CACHED TABLE ruleactiondef
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_id NVARCHAR(255),
    p_priority INTEGER,
    p_translatorid NVARCHAR(255),
    p_translatorparameters LONGVARBINARY,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE ruleactiondefcat
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_id NVARCHAR(255),
    p_priority INTEGER,
    p_icon BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE ruleactiondefcatlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE ruleactiondeflp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_breadcrumb NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE ruleactiondefparam
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_id NVARCHAR(255),
    p_priority INTEGER,
    p_type NVARCHAR(255),
    p_value NVARCHAR(255),
    p_required TINYINT,
    p_validators LONGVARBINARY,
    p_definitionpos INTEGER,
    p_definition BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX definitionRelIDX_5304 ON ruleactiondefparam (p_definition);

CREATE INDEX definitionposPosIDX_5304 ON ruleactiondefparam (p_definitionpos);


CREATE CACHED TABLE ruleactiondefparamlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE ruleconddef
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_id NVARCHAR(255),
    p_priority INTEGER,
    p_allowschildren TINYINT,
    p_translatorid NVARCHAR(255),
    p_translatorparameters LONGVARBINARY,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE ruleconddefcat
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_id NVARCHAR(255),
    p_priority INTEGER,
    p_icon BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE ruleconddefcatlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE ruleconddefcatrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_5306 ON ruleconddefcatrel (SourcePK);

CREATE INDEX qualifier_5306 ON ruleconddefcatrel (Qualifier);

CREATE INDEX seqnr_5306 ON ruleconddefcatrel (SequenceNumber);

CREATE INDEX rseqnr_5306 ON ruleconddefcatrel (RSequenceNumber);

CREATE INDEX linktarget_5306 ON ruleconddefcatrel (TargetPK);


CREATE CACHED TABLE ruleconddeflp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_breadcrumb NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE ruleconddefparam
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_id NVARCHAR(255),
    p_priority INTEGER,
    p_type NVARCHAR(255),
    p_value NVARCHAR(255),
    p_required TINYINT,
    p_validators LONGVARBINARY,
    p_definitionpos INTEGER,
    p_definition BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX definitionRelIDX_5301 ON ruleconddefparam (p_definition);

CREATE INDEX definitionposPosIDX_5301 ON ruleconddefparam (p_definitionpos);


CREATE CACHED TABLE ruleconddefparamlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE rulecondefruletypemap
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_ruletype BIGINT,
    p_definition BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX definitionRelIDX_5308 ON rulecondefruletypemap (p_definition);


CREATE CACHED TABLE ruleenginecontext
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_kiesession BIGINT,
    p_rulefiringlimit BIGINT,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE rulegroup
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE rulegrouplp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE rules
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uuid NVARCHAR(255),
    p_code NVARCHAR(255),
    p_startdate TIMESTAMP,
    p_enddate TIMESTAMP,
    p_priority INTEGER,
    p_maxallowedruns INTEGER,
    p_stackable TINYINT,
    p_status BIGINT,
    p_rulegroup BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_conditions LONGVARCHAR,
    p_actions LONGVARCHAR,
    p_website BIGINT,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX UC_Abstract_Rule_5103 ON rules (p_code);

CREATE INDEX rulegroupRelIDX_5103 ON rules (p_rulegroup);


CREATE CACHED TABLE ruleslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    p_messagefired LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE rulesmodule
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_ruletype BIGINT,
    p_active TINYINT,
    p_version BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_mvngroupid NVARCHAR(255),
    p_mvnartifactid NVARCHAR(255),
    p_mvnversion NVARCHAR(255),
    p_defaultkiebase BIGINT,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE ruletemplates
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_maxallowedruns INTEGER,
    p_rulegroup BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_conditions LONGVARCHAR,
    p_actions LONGVARCHAR,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX UC_Abstract_Rule_Template_5106 ON ruletemplates (p_code);

CREATE INDEX rulegroupRelIDX_5106 ON ruletemplates (p_rulegroup);


CREATE CACHED TABLE ruletemplateslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    p_messagefired LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE ruletoengineruletypemap
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_ruletype BIGINT,
    p_engineruletype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE savedqueries
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_paramtypes LONGVARCHAR,
    p_params LONGVARBINARY,
    p_query LONGVARCHAR,
    p_resulttype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE savedquerieslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE savedquery2usergrouprel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6402 ON savedquery2usergrouprel (SourcePK);

CREATE INDEX qualifier_6402 ON savedquery2usergrouprel (Qualifier);

CREATE INDEX seqnr_6402 ON savedquery2usergrouprel (SequenceNumber);

CREATE INDEX rseqnr_6402 ON savedquery2usergrouprel (RSequenceNumber);

CREATE INDEX linktarget_6402 ON savedquery2usergrouprel (TargetPK);


CREATE CACHED TABLE savedvalueentry
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_modifiedattribute NVARCHAR(255),
    p_oldvalueattributedescriptor BIGINT,
    p_parent BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX parentRelIDX_335 ON savedvalueentry (p_parent);


CREATE CACHED TABLE savedvalues
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_modifieditemtype BIGINT,
    p_timestamp TIMESTAMP,
    p_user BIGINT,
    p_modificationtype BIGINT,
    p_modifieditempos INTEGER,
    p_modifieditem BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX savedvalmoditem_334 ON savedvalues (p_modifieditem);

CREATE INDEX modifieditemposPosIDX_334 ON savedvalues (p_modifieditempos);


CREATE CACHED TABLE scripts
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_active TINYINT,
    p_checksum NVARCHAR(255),
    p_content LONGVARCHAR,
    p_version BIGINT,
    p_scripttype BIGINT,
    p_autodisabling TINYINT,
    p_disabled TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX codeVersionActiveIDX_100 ON scripts (p_code, p_version, p_active);


CREATE CACHED TABLE scriptslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE searchrestrictions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_extensionname NVARCHAR(255),
    p_autocreate TINYINT,
    p_generate TINYINT,
    p_code NVARCHAR(255),
    p_active TINYINT,
    principal BIGINT,
    query LONGVARCHAR,
    RestrictedType BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX restrtype_90 ON searchrestrictions (RestrictedType);

CREATE INDEX principal_90 ON searchrestrictions (principal);


CREATE CACHED TABLE searchrestrictionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE sessionevent
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_eventtime TIMESTAMP,
    p_agent BIGINT,
    p_sessionid NVARCHAR(255),
    p_basesite BIGINT,
    p_groups LONGVARCHAR,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_customer BIGINT,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE sitemapconfig
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_configid NVARCHAR(255),
    p_sitemaplanguagecurrencies LONGVARCHAR,
    p_sitemappages LONGVARCHAR,
    p_sitemaptemplate BIGINT,
    p_customurls LONGVARBINARY,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE sitemaplanguagecurrency
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_language BIGINT,
    p_currency BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE sitemappage
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code BIGINT,
    p_frequency BIGINT,
    p_priority DOUBLE,
    p_active TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE slactions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_type BIGINT,
    p_target NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE slotsforpage
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_catalogversion BIGINT,
    p_position NVARCHAR(255),
    p_page BIGINT,
    p_contentslot BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE slotsfortemplate
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_catalogversion BIGINT,
    p_allowoverwrite TINYINT,
    p_position NVARCHAR(255),
    p_pagetemplate BIGINT,
    p_contentslot BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE slrcnf2valrngsetrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_2214 ON slrcnf2valrngsetrel (SourcePK);

CREATE INDEX qualifier_2214 ON slrcnf2valrngsetrel (Qualifier);

CREATE INDEX seqnr_2214 ON slrcnf2valrngsetrel (SequenceNumber);

CREATE INDEX rseqnr_2214 ON slrcnf2valrngsetrel (RSequenceNumber);

CREATE INDEX linktarget_2214 ON slrcnf2valrngsetrel (TargetPK);


CREATE CACHED TABLE slridprp2valrnstrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_2215 ON slridprp2valrnstrel (SourcePK);

CREATE INDEX qualifier_2215 ON slridprp2valrnstrel (Qualifier);

CREATE INDEX seqnr_2215 ON slridprp2valrnstrel (SequenceNumber);

CREATE INDEX rseqnr_2215 ON slridprp2valrnstrel (RSequenceNumber);

CREATE INDEX linktarget_2215 ON slridprp2valrnstrel (TargetPK);


CREATE CACHED TABLE smarteditconfiguration
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_key NVARCHAR(255),
    p_value NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE solrabskwrdredir
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_url NVARCHAR(255),
    p_redirectitem BIGINT,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE solrboostrule
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_operator BIGINT,
    p_propertyvalue NVARCHAR(255),
    p_boostfactor INTEGER,
    p_solrindexedpropertypos INTEGER,
    p_solrindexedproperty BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX solrindexedpropertyRelIDX_9257 ON solrboostrule (p_solrindexedproperty);

CREATE INDEX solrindexedpropertyposPosIDX_9257 ON solrboostrule (p_solrindexedpropertypos);


CREATE CACHED TABLE solrboostrule2srchprof
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_9258 ON solrboostrule2srchprof (SourcePK);

CREATE INDEX qualifier_9258 ON solrboostrule2srchprof (Qualifier);

CREATE INDEX seqnr_9258 ON solrboostrule2srchprof (SequenceNumber);

CREATE INDEX rseqnr_9258 ON solrboostrule2srchprof (RSequenceNumber);

CREATE INDEX linktarget_9258 ON solrboostrule2srchprof (TargetPK);


CREATE CACHED TABLE solrendpointurl
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_url NVARCHAR(255),
    p_master TINYINT,
    p_solrserverconfig BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX solrserverconfigRelIDX_2213 ON solrendpointurl (p_solrserverconfig);


CREATE CACHED TABLE solrfacetreconf2srchprof
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_9254 ON solrfacetreconf2srchprof (SourcePK);

CREATE INDEX qualifier_9254 ON solrfacetreconf2srchprof (Qualifier);

CREATE INDEX seqnr_9254 ON solrfacetreconf2srchprof (SequenceNumber);

CREATE INDEX rseqnr_9254 ON solrfacetreconf2srchprof (RSequenceNumber);

CREATE INDEX linktarget_9254 ON solrfacetreconf2srchprof (TargetPK);


CREATE CACHED TABLE solrfacetreconfig
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_customfacetsortprovider NVARCHAR(255),
    p_facettype BIGINT,
    p_priority INTEGER,
    p_visible TINYINT,
    p_facetpos INTEGER,
    p_facet BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX facetRelIDX_9256 ON solrfacetreconfig (p_facet);

CREATE INDEX facetposPosIDX_9256 ON solrfacetreconfig (p_facetpos);


CREATE CACHED TABLE solrfacetvisiblerule
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_name NVARCHAR(255),
    p_condition BIGINT,
    p_facetpos INTEGER,
    p_facet BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_operator BIGINT,
    p_count INTEGER,
    p_selectedstates LONGVARCHAR,
    p_otherfacet BIGINT,
    p_categories LONGVARCHAR,
    p_coveragepercentage INTEGER,
    PRIMARY KEY (PK)
);

CREATE INDEX facetRelIDX_9251 ON solrfacetvisiblerule (p_facet);

CREATE INDEX facetposPosIDX_9251 ON solrfacetvisiblerule (p_facetpos);


CREATE CACHED TABLE solrheroproddef2prodrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_9253 ON solrheroproddef2prodrel (SourcePK);

CREATE INDEX qualifier_9253 ON solrheroproddef2prodrel (Qualifier);

CREATE INDEX seqnr_9253 ON solrheroproddef2prodrel (SequenceNumber);

CREATE INDEX rseqnr_9253 ON solrheroproddef2prodrel (RSequenceNumber);

CREATE INDEX linktarget_9253 ON solrheroproddef2prodrel (TargetPK);


CREATE CACHED TABLE solrheroproductdef
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_category BIGINT,
    p_indexedtype BIGINT,
    p_code NVARCHAR(255),
    p_catalogversion BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX versionIDX_9252 ON solrheroproductdef (p_catalogversion);

CREATE INDEX codeVersionIDX_9252 ON solrheroproductdef (p_code, p_catalogversion);


CREATE CACHED TABLE solrindexconfig
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_batchsize INTEGER,
    p_exportpath NVARCHAR(255),
    p_numberofthreads INTEGER,
    p_indexmode BIGINT,
    p_commitmode BIGINT,
    p_optimizemode BIGINT,
    p_ignoreerrors TINYINT,
    p_legacymode TINYINT,
    p_maxretries INTEGER,
    p_maxbatchretries INTEGER,
    p_distributedindexing TINYINT,
    p_nodegroup NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE solrindexedcoresrecord
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_corename NVARCHAR(255),
    p_indexname NVARCHAR(255),
    p_indextime TIMESTAMP,
    p_currentindexdatasubdirectory NVARCHAR(255),
    p_servermode BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE solrindexedproperty
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_type BIGINT,
    p_sortabletype BIGINT,
    p_facet TINYINT,
    p_localized TINYINT,
    p_currency TINYINT,
    p_multivalue TINYINT,
    p_rangeset BIGINT,
    p_facetdisplaynameprovider NVARCHAR(255),
    p_facettype BIGINT,
    p_fieldvalueprovider NVARCHAR(255),
    p_valueproviderparameter NVARCHAR(255),
    p_valueproviderparameters LONGVARBINARY,
    p_exportid NVARCHAR(255),
    p_useforspellchecking TINYINT,
    p_useforautocomplete TINYINT,
    p_priority INTEGER,
    p_includeinresponse TINYINT,
    p_customfacetsortprovider NVARCHAR(255),
    p_ftsquery TINYINT,
    p_ftsquerymintermlength INTEGER,
    p_ftsqueryboost FLOAT,
    p_ftsfuzzyquery TINYINT,
    p_ftsfuzzyquerymintermlength INTEGER,
    p_ftsfuzzyqueryfuzziness INTEGER,
    p_ftsfuzzyqueryboost FLOAT,
    p_ftswildcardquery TINYINT,
    p_ftswildcardquerymintermlengt INTEGER,
    p_ftswildcardquerytype BIGINT,
    p_ftswildcardqueryboost FLOAT,
    p_ftsphrasequery TINYINT,
    p_ftsphrasequeryslop FLOAT,
    p_ftsphrasequeryboost FLOAT,
    p_solrindexedtypepos INTEGER,
    p_solrindexedtype BIGINT,
    p_classattributeassignment BIGINT,
    p_categoryfield TINYINT,
    p_facetsort BIGINT,
    p_topvaluesprovider NVARCHAR(255),
    p_visible TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX solrindexedtypeRelIDX_2208 ON solrindexedproperty (p_solrindexedtype);

CREATE INDEX solrindexedtypeposPosIDX_2208 ON solrindexedproperty (p_solrindexedtypepos);


CREATE CACHED TABLE solrindexedpropertylp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_displayname NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE solrindexedtype
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_identifier NVARCHAR(255),
    p_type BIGINT,
    p_variant TINYINT,
    p_identityprovider NVARCHAR(255),
    p_modelloader NVARCHAR(255),
    p_defaultfieldvalueprovider NVARCHAR(255),
    p_valuesprovider NVARCHAR(255),
    p_indexname NVARCHAR(255),
    p_solrresultconverter NVARCHAR(255),
    p_group TINYINT,
    p_groupfieldname NVARCHAR(255),
    p_grouplimit INTEGER,
    p_groupfacets TINYINT,
    p_listeners LONGVARBINARY,
    p_configset NVARCHAR(255),
    p_ftsquerybuilder NVARCHAR(255),
    p_ftsquerybuilderparameters LONGVARBINARY,
    p_solrfacetsearchconfigpos INTEGER,
    p_solrfacetsearchconfig BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX solrfacetsearchconfigRelIDX_2207 ON solrindexedtype (p_solrfacetsearchconfig);

CREATE INDEX solrfacetsearchconfigposPosIDX_2207 ON solrindexedtype (p_solrfacetsearchconfigpos);


CREATE CACHED TABLE solrindexerquery
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_identifier NVARCHAR(255),
    p_type BIGINT,
    p_query LONGVARCHAR,
    p_injectlastindextime TINYINT,
    p_injectcurrenttime TINYINT,
    p_injectcurrentdate TINYINT,
    p_user BIGINT,
    p_parameterprovider NVARCHAR(255),
    p_solrindexedtypepos INTEGER,
    p_solrindexedtype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX solrindexedtypeRelIDX_2209 ON solrindexerquery (p_solrindexedtype);

CREATE INDEX solrindexedtypeposPosIDX_2209 ON solrindexerquery (p_solrindexedtypepos);


CREATE CACHED TABLE solrindexerqueryparam
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_value NVARCHAR(255),
    p_solrindexerquerypos INTEGER,
    p_solrindexerquery BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX solrindexerqueryRelIDX_2210 ON solrindexerqueryparam (p_solrindexerquery);

CREATE INDEX solrindexerqueryposPosIDX_2210 ON solrindexerqueryparam (p_solrindexerquerypos);


CREATE CACHED TABLE solrindexoperationrecord
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_starttime TIMESTAMP,
    p_finishtime TIMESTAMP,
    p_mode BIGINT,
    p_status BIGINT,
    p_threadid NVARCHAR(255),
    p_clusterid INTEGER,
    p_failedreason NVARCHAR(255),
    p_solrindexcorerecordpos INTEGER,
    p_solrindexcorerecord BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX SolrIndexOperationRecord_2224 ON solrindexoperationrecord (p_threadid, p_clusterid, p_mode, p_status, p_solrindexcorerecord);

CREATE INDEX solrindexcorerecordRelIDX_2224 ON solrindexoperationrecord (p_solrindexcorerecord);

CREATE INDEX solrindexcorerecordposPosIDX_2224 ON solrindexoperationrecord (p_solrindexcorerecordpos);


CREATE CACHED TABLE solrkeywredirects
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_language BIGINT,
    p_keyword NVARCHAR(255),
    p_matchtype BIGINT,
    p_ignorecase TINYINT,
    p_redirect BIGINT,
    p_facetsearchconfig BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX facetsearchconfigRelIDX_2223 ON solrkeywredirects (p_facetsearchconfig);


CREATE CACHED TABLE solrqueryaggregatedstats
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_time TIMESTAMP,
    p_indexconfig BIGINT,
    p_language BIGINT,
    p_query NVARCHAR(255),
    p_count BIGINT,
    p_avgnumberofresults DOUBLE,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE solrsearchconfig
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_pagesize INTEGER,
    p_defaultsortorder LONGVARBINARY,
    p_description NVARCHAR(255),
    p_restrictfieldsinresponse TINYINT,
    p_allfacetvaluesinresponse TINYINT,
    p_legacymode TINYINT,
    p_groupingproperty BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE solrsearchprofile
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_indexedtype BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_categorycode NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX categoryIndexTypeIdx_9255 ON solrsearchprofile (p_categorycode, p_indexedtype);


CREATE CACHED TABLE solrserverconfig
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_mode BIGINT,
    p_embeddedmaster TINYINT,
    p_alivecheckinterval INTEGER,
    p_connectiontimeout INTEGER,
    p_sockettimeout INTEGER,
    p_tcpnodelay TINYINT,
    p_maxtotalconnections INTEGER,
    p_maxtotalconnectionsperhostco INTEGER,
    p_readtimeout INTEGER,
    p_usemasternodeexclusivelyfori TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE solrsort
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_useboost TINYINT,
    p_indexedtypepos INTEGER,
    p_indexedtype BIGINT,
    p_visible TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_conditions LONGVARCHAR,
    PRIMARY KEY (PK)
);

CREATE INDEX indexedtypeRelIDX_6205 ON solrsort (p_indexedtype);

CREATE INDEX indexedtypeposPosIDX_6205 ON solrsort (p_indexedtypepos);


CREATE CACHED TABLE solrsortcondition
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_inverse TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_rootcategory BIGINT,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE solrsortfield
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_fieldname NVARCHAR(255),
    p_ascending TINYINT,
    p_sortpos INTEGER,
    p_sort BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX sortRelIDX_6206 ON solrsortfield (p_sort);

CREATE INDEX sortposPosIDX_6206 ON solrsortfield (p_sortpos);


CREATE CACHED TABLE solrsortlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE solrstopword
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_stopword NVARCHAR(255),
    p_languagepos INTEGER,
    p_language BIGINT,
    p_facetsearchconfigpos INTEGER,
    p_facetsearchconfig BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX languageRelIDX_2222 ON solrstopword (p_language);

CREATE INDEX languageposPosIDX_2222 ON solrstopword (p_languagepos);

CREATE INDEX facetsearchconfigRelIDX_2222 ON solrstopword (p_facetsearchconfig);

CREATE INDEX facetsearchconfigposPosIDX_2222 ON solrstopword (p_facetsearchconfigpos);


CREATE CACHED TABLE solrsynonymconfig
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_synonymfrom NVARCHAR(255),
    p_synonymto NVARCHAR(255),
    p_languagepos INTEGER,
    p_language BIGINT,
    p_facetsearchconfigpos INTEGER,
    p_facetsearchconfig BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX languageRelIDX_2218 ON solrsynonymconfig (p_language);

CREATE INDEX languageposPosIDX_2218 ON solrsynonymconfig (p_languagepos);

CREATE INDEX facetsearchconfigRelIDX_2218 ON solrsynonymconfig (p_facetsearchconfig);

CREATE INDEX facetsearchconfigposPosIDX_2218 ON solrsynonymconfig (p_facetsearchconfigpos);


CREATE CACHED TABLE solrvaluerange
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_from NVARCHAR(255),
    p_to NVARCHAR(255),
    p_solrvaluerangesetpos INTEGER,
    p_solrvaluerangeset BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX solrvaluerangesetRelIDX_2205 ON solrvaluerange (p_solrvaluerangeset);

CREATE INDEX solrvaluerangesetposPosIDX_2205 ON solrvaluerange (p_solrvaluerangesetpos);


CREATE CACHED TABLE solrvaluerangeset
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_type NVARCHAR(255),
    p_qualifier NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE stdpaymmodevals
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_currency BIGINT,
    p_value DOUBLE,
    p_paymentmode BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX paymentmodeRelIDX_1022 ON stdpaymmodevals (p_paymentmode);


CREATE CACHED TABLE steps
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_batchjob BIGINT,
    p_code NVARCHAR(255),
    p_sequencenumber INTEGER,
    p_synchronous TINYINT,
    p_errormode BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX IdxBatchJob_503 ON steps (p_batchjob);

CREATE INDEX seqNrIDX_503 ON steps (p_sequencenumber);


CREATE CACHED TABLE stocklevelhistoryentry
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_updatedate TIMESTAMP,
    p_actual INTEGER,
    p_reserved INTEGER,
    p_updatetype BIGINT,
    p_comment NVARCHAR(255),
    p_stocklevelpos INTEGER,
    p_stocklevel BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX stocklevelRelIDX_2020 ON stocklevelhistoryentry (p_stocklevel);

CREATE INDEX stocklevelposPosIDX_2020 ON stocklevelhistoryentry (p_stocklevelpos);


CREATE CACHED TABLE stocklevels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_available INTEGER,
    p_releasedate TIMESTAMP,
    p_nextdeliverytime TIMESTAMP,
    p_productcode NVARCHAR(255),
    p_reserved INTEGER,
    p_overselling INTEGER,
    p_preorder INTEGER,
    p_maxpreorder INTEGER,
    p_treatnegativeaszero TINYINT,
    p_instockstatus BIGINT,
    p_maxstocklevelhistorycount INTEGER,
    p_warehouse BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX productCodeIdx_2007 ON stocklevels (p_productcode);

CREATE INDEX warehouseIndex_2007 ON stocklevels (p_warehouse);

CREATE INDEX productCodeWarehouseIndex_2007 ON stocklevels (p_productcode, p_warehouse);


CREATE CACHED TABLE stocklevproductrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_2015 ON stocklevproductrel (SourcePK);

CREATE INDEX qualifier_2015 ON stocklevproductrel (Qualifier);

CREATE INDEX seqnr_2015 ON stocklevproductrel (SequenceNumber);

CREATE INDEX rseqnr_2015 ON stocklevproductrel (RSequenceNumber);

CREATE INDEX linktarget_2015 ON stocklevproductrel (TargetPK);


CREATE CACHED TABLE storelocation2locator
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_6209 ON storelocation2locator (SourcePK);

CREATE INDEX qualifier_6209 ON storelocation2locator (Qualifier);

CREATE INDEX seqnr_6209 ON storelocation2locator (SequenceNumber);

CREATE INDEX rseqnr_6209 ON storelocation2locator (RSequenceNumber);

CREATE INDEX linktarget_6209 ON storelocation2locator (TargetPK);


CREATE CACHED TABLE storelocatorfeature
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_icon BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE storelocatorfeaturelp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE stores4site
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1092 ON stores4site (SourcePK);

CREATE INDEX qualifier_1092 ON stores4site (Qualifier);

CREATE INDEX seqnr_1092 ON stores4site (SequenceNumber);

CREATE INDEX rseqnr_1092 ON stores4site (RSequenceNumber);

CREATE INDEX linktarget_1092 ON stores4site (TargetPK);


CREATE CACHED TABLE synattcfg
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_syncjob BIGINT,
    p_attributedescriptor BIGINT,
    p_includedinsync TINYINT,
    p_copybyvalue TINYINT,
    p_untranslatable TINYINT,
    p_translatevalue TINYINT,
    p_presetvalue LONGVARBINARY,
    p_partiallytranslatable TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX jobIdx_620 ON synattcfg (p_syncjob);

CREATE INDEX attrIdx_620 ON synattcfg (p_attributedescriptor);


CREATE CACHED TABLE syncjob2langrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_622 ON syncjob2langrel (SourcePK);

CREATE INDEX qualifier_622 ON syncjob2langrel (Qualifier);

CREATE INDEX seqnr_622 ON syncjob2langrel (SequenceNumber);

CREATE INDEX rseqnr_622 ON syncjob2langrel (RSequenceNumber);

CREATE INDEX linktarget_622 ON syncjob2langrel (TargetPK);


CREATE CACHED TABLE syncjob2pcplrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_623 ON syncjob2pcplrel (SourcePK);

CREATE INDEX qualifier_623 ON syncjob2pcplrel (Qualifier);

CREATE INDEX seqnr_623 ON syncjob2pcplrel (SequenceNumber);

CREATE INDEX rseqnr_623 ON syncjob2pcplrel (RSequenceNumber);

CREATE INDEX linktarget_623 ON syncjob2pcplrel (TargetPK);


CREATE CACHED TABLE syncjob2typerel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_621 ON syncjob2typerel (SourcePK);

CREATE INDEX qualifier_621 ON syncjob2typerel (Qualifier);

CREATE INDEX seqnr_621 ON syncjob2typerel (SequenceNumber);

CREATE INDEX rseqnr_621 ON syncjob2typerel (RSequenceNumber);

CREATE INDEX linktarget_621 ON syncjob2typerel (TargetPK);


CREATE CACHED TABLE taskconditions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uniqueid NVARCHAR(255),
    p_expirationtimemillis BIGINT,
    p_processeddate TIMESTAMP,
    p_fulfilled TINYINT,
    p_consumed TINYINT,
    p_choice NVARCHAR(255),
    p_counter INTEGER,
    p_task BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX Cond_idx_951 ON taskconditions (p_uniqueid, p_consumed);

CREATE INDEX Cond_match_idx_951 ON taskconditions (p_task, p_fulfilled);

CREATE INDEX taskRelIDX_951 ON taskconditions (p_task);


CREATE CACHED TABLE tasklogs
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_returncode NVARCHAR(255),
    p_startdate TIMESTAMP,
    p_enddate TIMESTAMP,
    p_actionid NVARCHAR(255),
    p_clusterid INTEGER,
    p_logmessages LONGVARCHAR,
    p_process BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX processRelIDX_32767 ON tasklogs (p_process);


CREATE CACHED TABLE tasks
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_runnerbean NVARCHAR(255),
    p_executiontimemillis BIGINT,
    p_failed TINYINT,
    p_expirationtimemillis BIGINT,
    p_context LONGVARBINARY,
    p_contextitem BIGINT,
    p_nodeid INTEGER,
    p_nodegroup NVARCHAR(255),
    p_retry INTEGER,
    p_runningonclusternode INTEGER,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_action NVARCHAR(255),
    p_process BIGINT,
    p_scripturi NVARCHAR(255),
    p_trigger BIGINT,
    p_state BIGINT,
    p_conditionid NVARCHAR(255),
    PRIMARY KEY (PK)
);

CREATE INDEX Task_dr_idx_950 ON tasks (p_runningonclusternode, p_expirationtimemillis, p_nodeid);

CREATE INDEX processRelIDX_950 ON tasks (p_process);


CREATE CACHED TABLE taxes
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_value DOUBLE,
    p_currency BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE taxeslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE taxrows
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_product BIGINT,
    p_pg BIGINT,
    p_productmatchqualifier BIGINT,
    p_starttime TIMESTAMP,
    p_endtime TIMESTAMP,
    p_user BIGINT,
    p_ug BIGINT,
    p_usermatchqualifier BIGINT,
    p_productid NVARCHAR(255),
    p_catalogversion BIGINT,
    p_currency BIGINT,
    p_tax BIGINT,
    p_value DOUBLE,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX MatchIndexP_1054 ON taxrows (p_productmatchqualifier);

CREATE INDEX MatchIndexU_1054 ON taxrows (p_usermatchqualifier);

CREATE INDEX PIdx_1054 ON taxrows (p_product);

CREATE INDEX UIdx_1054 ON taxrows (p_user);

CREATE INDEX PGIdx_1054 ON taxrows (p_pg);

CREATE INDEX UGIdx_1054 ON taxrows (p_ug);

CREATE INDEX ProductIdIdx_1054 ON taxrows (p_productid);

CREATE INDEX versionIDX_1054 ON taxrows (p_catalogversion);


CREATE CACHED TABLE testitem
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    fieldA NVARCHAR(255),
    fieldB NVARCHAR(255),
    fieldBoolean TINYINT,
    fieldByte SMALLINT,
    fieldCharacter SMALLINT,
    fieldDate TIMESTAMP,
    fieldDouble DOUBLE,
    fieldFloat FLOAT,
    fieldInteger INTEGER,
    fieldLong BIGINT,
    fieldPrimitiveBoolean TINYINT DEFAULT 0,
    fieldPrimitiveByte SMALLINT DEFAULT 0,
    fieldPrimitiveChar SMALLINT,
    fieldPrimitiveDouble DOUBLE DEFAULT 0,
    fieldPrimitiveFloat FLOAT DEFAULT 0,
    fieldPrimitiveInteger INTEGER DEFAULT 0,
    fieldPrimitiveLong BIGINT DEFAULT 0,
    fieldPrimitiveShort SMALLINT,
    fieldSerializable LONGVARBINARY,
    fieldString NVARCHAR(255),
    fieldLongString LONGVARCHAR,
    p_testproperty0 NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    fieldBigDecimal DECIMAL(30,8),
    p_testproperty1 INTEGER,
    p_foo NVARCHAR(255),
    p_bar NVARCHAR(255),
    p_xxx NVARCHAR(255),
    p_itemtypetwo BIGINT,
    p_itemstypetwo LONGVARCHAR,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE testitemlp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_testproperty2 NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE titles
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX codeIdx_24 ON titles (p_code);


CREATE CACHED TABLE titleslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE triggerscj
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_active TINYINT,
    p_second INTEGER,
    p_minute INTEGER,
    p_hour INTEGER,
    p_day INTEGER,
    p_month INTEGER,
    p_year INTEGER,
    p_relative TINYINT,
    p_daysofweek LONGVARCHAR,
    p_weekinterval INTEGER,
    p_daterange LONGVARBINARY,
    p_activationtime TIMESTAMP,
    p_cronexpression NVARCHAR(255),
    p_maxacceptabledelay INTEGER,
    p_job BIGINT,
    p_cronjob BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX IdxCronJob_502 ON triggerscj (p_cronjob);

CREATE INDEX IdxActive_502 ON triggerscj (p_active);

CREATE INDEX jobRelIDX_502 ON triggerscj (p_job);


CREATE CACHED TABLE typesystemprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT NOT NULL,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255) NOT NULL,
    LANGPK BIGINT NOT NULL,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX nameidx_typesystemprops ON typesystemprops (NAME);

CREATE INDEX itempk_typesystemprops ON typesystemprops (ITEMPK);

CREATE INDEX realnameidx_typesystemprops ON typesystemprops (REALNAME);


CREATE CACHED TABLE units
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_conversion DOUBLE,
    p_unittype NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE unitslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE useraudit
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_uid NVARCHAR(255),
    p_userpk BIGINT,
    p_changinguser NVARCHAR(255),
    p_changingapplication NVARCHAR(255),
    p_ipaddress NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    passwd LONGVARCHAR,
    p_passwordencoding NVARCHAR(255),
    p_failedlogins INTEGER,
    p_failedoauthauthorizations INTEGER,
    PRIMARY KEY (PK)
);

CREATE INDEX UID_6 ON useraudit (p_uid);

CREATE INDEX userPK_6 ON useraudit (p_userpk);


CREATE CACHED TABLE usergroupprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT NOT NULL,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255) NOT NULL,
    LANGPK BIGINT NOT NULL,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX realnameidx_usergroupprops ON usergroupprops (REALNAME);

CREATE INDEX itempk_usergroupprops ON usergroupprops (ITEMPK);

CREATE INDEX nameidx_usergroupprops ON usergroupprops (NAME);


CREATE CACHED TABLE usergroups
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_description NVARCHAR(255),
    p_name NVARCHAR(255),
    p_uid NVARCHAR(255),
    p_profilepicture BIGINT,
    p_ldapsearchbase NVARCHAR(255),
    p_dn LONGVARCHAR,
    p_cn NVARCHAR(255),
    p_backofficelogindisabled TINYINT,
    p_maxbruteforceloginattempts INTEGER,
    p_writeablelanguages LONGVARCHAR,
    p_readablelanguages LONGVARCHAR,
    p_userdiscountgroup BIGINT,
    p_userpricegroup BIGINT,
    p_usertaxgroup BIGINT,
    p_hmclogindisabled TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_dunsid NVARCHAR(255),
    p_ilnid NVARCHAR(255),
    p_buyerspecificid NVARCHAR(255),
    p_id NVARCHAR(255),
    p_supplierspecificid NVARCHAR(255),
    p_medias LONGVARCHAR,
    p_shippingaddress BIGINT,
    p_unloadingaddress BIGINT,
    p_billingaddress BIGINT,
    p_contactaddress BIGINT,
    p_contact BIGINT,
    p_vatid NVARCHAR(255),
    p_responsiblecompany BIGINT,
    p_country BIGINT,
    p_lineofbuisness BIGINT,
    p_buyer TINYINT,
    p_supplier TINYINT,
    p_manufacturer TINYINT,
    p_carrier TINYINT,
    p_active TINYINT,
    p_implementationtype NVARCHAR(255),
    p_priority INTEGER,
    p_store BIGINT,
    p_emaildistributionlist NVARCHAR(255),
    p_defaultassignee BIGINT,
    p_authorities LONGVARBINARY,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX UID_5 ON usergroups (p_uid);

CREATE INDEX storeRelIDX_5 ON usergroups (p_store);


CREATE CACHED TABLE usergroupslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_locname NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE userprofiles
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_readablelanguages LONGVARCHAR,
    p_writablelanguages LONGVARCHAR,
    p_expandinitial TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE userprops
(
    hjmpTS BIGINT,
    ITEMPK BIGINT NOT NULL,
    ITEMTYPEPK BIGINT,
    NAME NVARCHAR(255) NOT NULL,
    LANGPK BIGINT NOT NULL,
    REALNAME NVARCHAR(255),
    TYPE1 INTEGER DEFAULT 0,
    VALUESTRING1 LONGVARCHAR,
    VALUE1 LONGVARBINARY,
    PRIMARY KEY (ITEMPK, NAME, LANGPK)
);

CREATE INDEX nameidx_userprops ON userprops (NAME);

CREATE INDEX realnameidx_userprops ON userprops (REALNAME);

CREATE INDEX itempk_userprops ON userprops (ITEMPK);


CREATE CACHED TABLE userrights
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE userrightslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE users
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_description NVARCHAR(255),
    p_name NVARCHAR(255),
    p_uid NVARCHAR(255),
    p_profilepicture BIGINT,
    p_ldapsearchbase NVARCHAR(255),
    p_dn LONGVARCHAR,
    p_cn NVARCHAR(255),
    p_backofficelogindisabled TINYINT,
    p_defaultpaymentaddress BIGINT,
    p_defaultshipmentaddress BIGINT,
    p_passwordencoding NVARCHAR(255),
    passwd LONGVARCHAR,
    p_passwordanswer LONGVARCHAR,
    p_passwordquestion LONGVARCHAR,
    p_sessionlanguage BIGINT,
    p_sessioncurrency BIGINT,
    p_logindisabled TINYINT,
    p_lastlogin TIMESTAMP,
    p_hmclogindisabled TINYINT,
    p_userprofile BIGINT,
    p_europe1pricefactory_udg BIGINT,
    p_europe1pricefactory_upg BIGINT,
    p_europe1pricefactory_utg BIGINT,
    p_ldapaccount TINYINT,
    p_domain NVARCHAR(255),
    p_ldaplogin NVARCHAR(255),
    p_authorizedtounlockpages TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_customerid NVARCHAR(255),
    p_previewcatalogversions LONGVARCHAR,
    p_title BIGINT,
    p_defaultpaymentinfo BIGINT,
    p_token LONGVARCHAR,
    p_originaluid NVARCHAR(255),
    p_type BIGINT,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX UID_4 ON users (p_uid);


CREATE CACHED TABLE users4restriction
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1082 ON users4restriction (SourcePK);

CREATE INDEX qualifier_1082 ON users4restriction (Qualifier);

CREATE INDEX seqnr_1082 ON users4restriction (SequenceNumber);

CREATE INDEX rseqnr_1082 ON users4restriction (RSequenceNumber);

CREATE INDEX linktarget_1082 ON users4restriction (TargetPK);


CREATE CACHED TABLE validationconstraints
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_id NVARCHAR(255),
    p_active TINYINT,
    p_annotation LONGVARCHAR,
    p_severity BIGINT,
    p_target LONGVARCHAR,
    p_type BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_language BIGINT,
    p_expression LONGVARCHAR,
    p_firstfieldname NVARCHAR(255),
    p_secondfieldname NVARCHAR(255),
    p_qualifier NVARCHAR(255),
    p_languages LONGVARCHAR,
    p_descriptor BIGINT,
    p_regexp NVARCHAR(255),
    p_flags LONGVARCHAR,
    p_integer INTEGER,
    p_fraction INTEGER,
    p_value BIGINT,
    p_valu0 DECIMAL(30,8),
    p_min BIGINT,
    p_max BIGINT,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX AbstractConstraint_idx_980 ON validationconstraints (p_id);

CREATE INDEX typeRelIDX_980 ON validationconstraints (p_type);

CREATE INDEX descriptorRelIDX_980 ON validationconstraints (p_descriptor);


CREATE CACHED TABLE validationconstraintslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_message LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE vendors
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);


CREATE CACHED TABLE vendorslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE voucherinvalidations
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_user BIGINT,
    p_order BIGINT,
    p_status NVARCHAR(255),
    p_voucher BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX codeIdx_1600 ON voucherinvalidations (p_code);

CREATE INDEX codeUserIdx_1600 ON voucherinvalidations (p_code, p_user);

CREATE INDEX codeOrderIdx_1600 ON voucherinvalidations (p_code, p_order);

CREATE INDEX voucherRelIDX_1600 ON voucherinvalidations (p_voucher);


CREATE CACHED TABLE warehouses
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_default TINYINT,
    p_vendorpos INTEGER,
    p_vendor BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX vendorRelIDX_2005 ON warehouses (p_vendor);

CREATE INDEX vendorposPosIDX_2005 ON warehouses (p_vendorpos);


CREATE CACHED TABLE warehouseslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE whereparts
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_and TINYINT,
    p_replacepattern NVARCHAR(255),
    p_savedquery BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX savedqueryRelIDX_1300 ON whereparts (p_savedquery);


CREATE CACHED TABLE widgetparameter
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_name NVARCHAR(255),
    p_type BIGINT,
    p_description NVARCHAR(255),
    p_defaultvalueexpression NVARCHAR(255),
    p_targettype NVARCHAR(255),
    p_value LONGVARBINARY,
    p_widgetpreferences BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX widgetpreferencesRelIDX_2072 ON widgetparameter (p_widgetpreferences);


CREATE CACHED TABLE widgetpreferences
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_owneruser BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_report BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX owneruserRelIDX_2071 ON widgetpreferences (p_owneruser);


CREATE CACHED TABLE widgetpreferenceslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_title NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE workflowactioncomments
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_comment NVARCHAR(255),
    p_user BIGINT,
    p_workflowaction BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX workflowactionRelIDX_1118 ON workflowactioncomments (p_workflowaction);


CREATE CACHED TABLE workflowactionitemsrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1116 ON workflowactionitemsrel (SourcePK);

CREATE INDEX qualifier_1116 ON workflowactionitemsrel (Qualifier);

CREATE INDEX seqnr_1116 ON workflowactionitemsrel (SequenceNumber);

CREATE INDEX rseqnr_1116 ON workflowactionitemsrel (RSequenceNumber);

CREATE INDEX linktarget_1116 ON workflowactionitemsrel (TargetPK);


CREATE CACHED TABLE workflowactionlinkrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    p_active TINYINT,
    p_andconnection TINYINT,
    p_template BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1124 ON workflowactionlinkrel (SourcePK);

CREATE INDEX qualifier_1124 ON workflowactionlinkrel (Qualifier);

CREATE INDEX seqnr_1124 ON workflowactionlinkrel (SequenceNumber);

CREATE INDEX rseqnr_1124 ON workflowactionlinkrel (RSequenceNumber);

CREATE INDEX linktarget_1124 ON workflowactionlinkrel (TargetPK);


CREATE CACHED TABLE workflowactions
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_actiontype BIGINT,
    p_code NVARCHAR(255),
    p_principalassigned BIGINT,
    p_sendemail TINYINT,
    p_emailaddress NVARCHAR(255),
    p_renderertemplate BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    p_workflowpos INTEGER,
    p_workflow BIGINT,
    p_jobclass LONGVARCHAR,
    p_jobhandler NVARCHAR(255),
    p_selecteddecision BIGINT,
    p_firstactivated TIMESTAMP,
    p_activated TIMESTAMP,
    p_comment NVARCHAR(255),
    p_status BIGINT,
    p_template BIGINT,
    PRIMARY KEY (PK)
);

CREATE INDEX codeIDX_1113 ON workflowactions (p_code);

CREATE INDEX workflowRelIDX_1113 ON workflowactions (p_workflow);

CREATE INDEX workflowposPosIDX_1113 ON workflowactions (p_workflowpos);


CREATE CACHED TABLE workflowactionslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    p_description LONGVARCHAR,
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE workflowactionsrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1115 ON workflowactionsrel (SourcePK);

CREATE INDEX qualifier_1115 ON workflowactionsrel (Qualifier);

CREATE INDEX seqnr_1115 ON workflowactionsrel (SequenceNumber);

CREATE INDEX rseqnr_1115 ON workflowactionsrel (RSequenceNumber);

CREATE INDEX linktarget_1115 ON workflowactionsrel (TargetPK);


CREATE CACHED TABLE workflowitematts
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    p_comment NVARCHAR(255),
    p_item BIGINT,
    p_typeofitem BIGINT,
    p_workflowpos INTEGER,
    p_workflow BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX codeIDX_1114 ON workflowitematts (p_code);

CREATE INDEX workflowRelIDX_1114 ON workflowitematts (p_workflow);

CREATE INDEX workflowposPosIDX_1114 ON workflowitematts (p_workflowpos);


CREATE CACHED TABLE workflowitemattslp
(
    ITEMPK BIGINT,
    ITEMTYPEPK BIGINT,
    LANGPK BIGINT,
    p_name NVARCHAR(255),
    PRIMARY KEY (ITEMPK, LANGPK)
);


CREATE CACHED TABLE workflowtemplatelinkrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    p_andconnectiontemplate TINYINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1125 ON workflowtemplatelinkrel (SourcePK);

CREATE INDEX qualifier_1125 ON workflowtemplatelinkrel (Qualifier);

CREATE INDEX seqnr_1125 ON workflowtemplatelinkrel (SequenceNumber);

CREATE INDEX rseqnr_1125 ON workflowtemplatelinkrel (RSequenceNumber);

CREATE INDEX linktarget_1125 ON workflowtemplatelinkrel (TargetPK);


CREATE CACHED TABLE workflowtemplprincrel
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1117 ON workflowtemplprincrel (SourcePK);

CREATE INDEX qualifier_1117 ON workflowtemplprincrel (Qualifier);

CREATE INDEX seqnr_1117 ON workflowtemplprincrel (SequenceNumber);

CREATE INDEX rseqnr_1117 ON workflowtemplprincrel (RSequenceNumber);

CREATE INDEX linktarget_1117 ON workflowtemplprincrel (TargetPK);


CREATE CACHED TABLE writecockpitcollrels
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1711 ON writecockpitcollrels (SourcePK);

CREATE INDEX qualifier_1711 ON writecockpitcollrels (Qualifier);

CREATE INDEX seqnr_1711 ON writecockpitcollrels (SequenceNumber);

CREATE INDEX rseqnr_1711 ON writecockpitcollrels (RSequenceNumber);

CREATE INDEX linktarget_1711 ON writecockpitcollrels (TargetPK);


CREATE CACHED TABLE ydeployments
(
    hjmpTS BIGINT,
    Typecode INTEGER DEFAULT 0,
    TableName NVARCHAR(255),
    PropsTableName NVARCHAR(255),
    Name NVARCHAR(255) NOT NULL,
    PackageName NVARCHAR(255),
    SuperName NVARCHAR(255),
    ExtensionName NVARCHAR(255),
    Modifiers INTEGER DEFAULT 0,
    TypeSystemName NVARCHAR(255) NOT NULL,
    PRIMARY KEY (Name, TypeSystemName)
);

CREATE INDEX deplselect2_ydeployments ON ydeployments (Typecode);

CREATE INDEX deplselect_ydeployments ON ydeployments (ExtensionName);

CREATE INDEX tsnameidx_ydeployments ON ydeployments (TypeSystemName);


CREATE CACHED TABLE zone2country
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    languagepk BIGINT,
    Qualifier NVARCHAR(255),
    SourcePK BIGINT,
    TargetPK BIGINT,
    SequenceNumber INTEGER DEFAULT 0,
    RSequenceNumber INTEGER DEFAULT 0,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE INDEX linksource_1204 ON zone2country (SourcePK);

CREATE INDEX qualifier_1204 ON zone2country (Qualifier);

CREATE INDEX seqnr_1204 ON zone2country (SequenceNumber);

CREATE INDEX rseqnr_1204 ON zone2country (RSequenceNumber);

CREATE INDEX linktarget_1204 ON zone2country (TargetPK);


CREATE CACHED TABLE zonedeliverymodevalues
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_currency BIGINT,
    p_minimum DOUBLE,
    p_value DOUBLE,
    p_zone BIGINT,
    p_deliverymode BIGINT,
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX IdentityIDX_1202 ON zonedeliverymodevalues (p_deliverymode, p_zone, p_currency, p_minimum);

CREATE INDEX ModeIDX_1202 ON zonedeliverymodevalues (p_deliverymode);

CREATE INDEX ZoneIDX_1202 ON zonedeliverymodevalues (p_zone);


CREATE CACHED TABLE zones
(
    hjmpTS BIGINT,
    createdTS TIMESTAMP,
    modifiedTS TIMESTAMP,
    TypePkString BIGINT,
    OwnerPkString BIGINT,
    PK BIGINT NOT NULL,
    p_code NVARCHAR(255),
    aCLTS BIGINT DEFAULT 0,
    propTS BIGINT DEFAULT 0,
    PRIMARY KEY (PK)
);

CREATE UNIQUE INDEX IdentityIDX_1203 ON zones (p_code);

