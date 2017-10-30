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
package com.hybris.yprofile.common;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by d064321 on 01.12.15.
 *
 * Formatting utility to create correct data format for yaas.
 */
public class Utils {

    public static String formatDouble(Double d){
        if(d == null){
            return "";
        }
        DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
        decimalFormatSymbols.setDecimalSeparator('.');
        decimalFormatSymbols.setGroupingSeparator(',');
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", decimalFormatSymbols);
        return decimalFormat.format(d);
    }

    public static String formatDate(Date d){
        DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        return df2.format(d);
    }
}
