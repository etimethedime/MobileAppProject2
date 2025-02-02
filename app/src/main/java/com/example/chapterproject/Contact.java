package com.example.chapterproject;

import java.util.Calendar;

public class Contact {
    private long id;
    private String contactName;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;
    private String cellNumber;
    private String email;
    private String birthday;

    public Contact() {
        id=-1;
        birthday = String.valueOf(Calendar.getInstance());
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }


    public long getId() {
        return id;
    }

    public String getContactName() {
        return contactName;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return birthday;
    }
}