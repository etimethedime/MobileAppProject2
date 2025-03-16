package com.example.chapterproject;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Contact implements Parcelable {
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

    public void setPicture(Bitmap picture) {
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

    public Bitmap getPicture() {
        return picture;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(contactName);
        dest.writeString(streetAddress);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(zipCode);
        dest.writeString(phoneNumber);
        dest.writeString(cellNumber);
        dest.writeString(email);
        dest.writeSerializable(birthday);
        dest.writeParcelable(picture, flags);
    }

    protected Contact(Parcel in) {
        id = in.readLong();
        contactName = in.readString();
        streetAddress = in.readString();
        city = in.readString();
        state = in.readString();
        zipCode = in.readString();
        phoneNumber = in.readString();
        cellNumber = in.readString();
        email = in.readString();
        birthday = (Calendar) in.readSerializable();
        picture = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };
}
