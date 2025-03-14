package com.example.chapterproject;

import android.graphics.Bitmap;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Contact {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
    private long id;
    private String contactName;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;
    private String cellNumber;
    private String email;
    private Calendar birthday;
    private Bitmap picture;

    public Contact() {
        id = -1;
        birthday = Calendar.getInstance();
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

    public void setHomePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setBirthday(String birthdayStr) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse(birthdayStr));
            this.birthday = cal;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setPicture(Bitmap picture){
        this.picture = picture;
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

    public String getHomePhoneNumber() {
        return phoneNumber;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getBirthday() {
        return dateFormat.format(birthday.getTime());
    }

    public Bitmap getPicture() {return picture;}
}