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
package com.hybris.yprofile.dto;

import de.hybris.platform.core.model.user.AddressModel;

/**
 * Transfer object for correct yaas format.
 */
public class Address {

    private String firstName;
    private String lastName;
    private String addition;
    private String street;
    private String number;
    private String zip;
    private String city;
    private String country;

    public Address(AddressModel deliveryAddress) {
        if(deliveryAddress!=null) {
            this.firstName = deliveryAddress.getFirstname() != null ? deliveryAddress.getFirstname() : "";
            this.lastName = deliveryAddress.getLastname() != null? deliveryAddress.getLastname() : "";

            this.street = deliveryAddress.getStreetname();
            this.number = "0";

            int lastIndexOf = deliveryAddress.getStreetname().lastIndexOf(" ");
            if (lastIndexOf > 0) {
                this.street = deliveryAddress.getStreetname().substring(0, lastIndexOf);
                this.number = deliveryAddress.getStreetname().substring(lastIndexOf);
            }


            this.addition = deliveryAddress.getLine2() != null ? deliveryAddress.getLine2() : "";

            this.zip = deliveryAddress.getPostalcode() != null ? deliveryAddress.getPostalcode() : "";
            this.city = deliveryAddress.getTown() != null ? deliveryAddress.getTown() : "";
            this.country = (deliveryAddress.getCountry() != null && deliveryAddress.getCountry().getIsocode() != null) ? deliveryAddress.getCountry().getIsocode() : "";
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddition() {
        return addition;
    }

    public void setAddition(String addition) {
        this.addition = addition;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Address{");
        sb.append("firstName='").append(firstName).append('\'');
        sb.append(", lastName='").append(lastName).append('\'');
        sb.append(", addition='").append(addition).append('\'');
        sb.append(", street='").append(street).append('\'');
        sb.append(", number='").append(number).append('\'');
        sb.append(", zip='").append(zip).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", country='").append(country).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
