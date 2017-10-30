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
package com.hybris.yprofile.populators;

import com.hybris.yprofile.dto.TrackingEvent;
import de.hybris.eventtracking.publisher.csv.model.TrackingEventCsvData;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.apache.commons.lang.StringUtils;

public class TrackingEventPopulator implements Populator<TrackingEventCsvData , TrackingEvent> {

    private static final String DEFAULT_INT_VALUE = "0";

    @Override
    public void populate(TrackingEventCsvData source, TrackingEvent target) throws ConversionException {

        target.setAction_name(source.getEventType());

        target.setUrl(source.getUrl());
        target.set_id(source.getPiwikId());
        target.set_idts(source.getTimestamp());
        target.set_viewts(source.getTimestamp());
        target.setDate(Long.toString(System.currentTimeMillis()));
        target.setPiwik_id(source.getPiwikId());
        target.setCvar(getCvar(source));
        target.setIdsite(source.getIdsite());
        target.setRes(source.getRes());
        target.setUserId(source.getUserId());

        target.setUserAgent(source.getUserAgent());
        target.setAccept(source.getAccept());
        target.setAcceptLanguage(source.getAcceptLanguage());
        target.setReferer(source.getReferer());

        populateSearchValues(source, target);
        populateCommerceValues(source, target);
        populateWithDefault(target);
    }

    private void populateSearchValues(TrackingEventCsvData source, TrackingEvent target) {
        target.setData(StringUtils.isBlank(source.getData()) ? null : source.getData());
        target.setSearch(StringUtils.isBlank(source.getSearchTerm())? null: source.getSearchTerm());
        target.setSearch_cat(StringUtils.isBlank(source.getSearch_cat()) ? null : source.getSearch_cat());
        target.setSearch_count(StringUtils.isBlank(source.getSearch_count())? null: source.getSearch_count());
    }

    private void populateCommerceValues(TrackingEventCsvData source, TrackingEvent target) {
        target.setEc_id(StringUtils.isBlank(source.getEc_id())? null: source.getEc_id());
        target.setEc_items(StringUtils.isBlank(source.getEc_items())? null: source.getEc_items());
        target.setEc_st(StringUtils.isBlank(source.getEc_st())? null: source.getEc_st());
        target.setEc_tx(StringUtils.isBlank(source.getEc_tx())? null: source.getEc_tx());
        target.setEc_dt(StringUtils.isBlank(source.getEc_dt())? null: source.getEc_dt());
    }

    private String getCvar(TrackingEventCsvData source) {

        if (StringUtils.isNotBlank(source.getCvar())){
            return source.getCvar();
        }

        StringBuilder sb = new StringBuilder();
        sb.append("{");

        //The cvar has the following structure:
        //"cvar": "{\"2\":[\"_pkp\",110],\"3\":[\"_pks\",\"lumia920\"],\"4\":[\"_pkn\",\"Nokia Lumia 920\"],\"5\":[\"_pkc\",\"Phones\"]}",

        if (StringUtils.isNotBlank(source.getProductId())){
            sb.append("\"2\":");
            sb.append("[\"_pkp\",\"" + source.getProductPrice() + "\"],"); // product price
            sb.append("\"3\":");
            sb.append("[\"_pks\",\"" + source.getProductId() + "\"],"); // product id
            sb.append("\"4\":");
            sb.append("[\"_pkn\",\"" + source.getProductName() + "\"],"); // product name
        }

        if (StringUtils.isNotBlank(source.getCategoryName())) {
            sb.append("\"5\":");
            sb.append("[\"_pkc\",\"" + source.getCategoryName() + "\"]"); // categories
        }

        sb.append("}");

        return sb.toString();
    }

    private void populateWithDefault(TrackingEvent target) {
        target.setRec(DEFAULT_INT_VALUE);
        target.setR(DEFAULT_INT_VALUE);
        target.setH(DEFAULT_INT_VALUE);
        target.setM(DEFAULT_INT_VALUE);
        target.setS(DEFAULT_INT_VALUE);
        target.set_idvc(DEFAULT_INT_VALUE);
        target.set_idn(DEFAULT_INT_VALUE);
        target.set_refts(DEFAULT_INT_VALUE);
        target.setSend_image(DEFAULT_INT_VALUE);
        target.setPdf(DEFAULT_INT_VALUE);
        target.setQt(DEFAULT_INT_VALUE);
        target.setRealp(DEFAULT_INT_VALUE);
        target.setWma(DEFAULT_INT_VALUE);
        target.setDir(DEFAULT_INT_VALUE);
        target.setFla(DEFAULT_INT_VALUE);
        target.setJava(DEFAULT_INT_VALUE);
        target.setGears(DEFAULT_INT_VALUE);
        target.setAg(DEFAULT_INT_VALUE);
        target.setCookie(DEFAULT_INT_VALUE);
        target.setGt_ms(DEFAULT_INT_VALUE);
    }

}
