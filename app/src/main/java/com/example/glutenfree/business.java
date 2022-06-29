package com.example.glutenfree;

import java.util.ArrayList;

public class business {
    private String type;
    private String name;
    private String description;
    private String address;
    private String addressCity;
    private String opening_hours;
    private String site_link;
    private String facebook_link;
    private String prices;
    private String delivery_services;
    private String phone;
    private ArrayList<String> pictures;
    private String video;
    private String key;
    private ArrayList<String> reviews;

    public business(String type, String name, String description, String address, String addressCity, String opening_hours, String site_link, String facebook_link, String prices, String delivery_services, String phone, ArrayList<String> pictures, String video, String key, ArrayList<String> reviews) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.address = address;
        this.addressCity = addressCity;
        this.opening_hours = opening_hours;
        this.site_link = site_link;
        this.facebook_link = facebook_link;
        this.prices = prices;
        this.delivery_services = delivery_services;
        this.phone = phone;
        this.pictures = pictures;
        this.video = video;
        this.key = key;
        this.reviews = reviews;
    }

    public business() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddressCity() {
        return addressCity;
    }

    public void setAddressCity(String addressCity) {
        this.addressCity = addressCity;
    }

    public String getOpening_hours() {
        return opening_hours;
    }

    public void setOpening_hours(String opening_hours) {
        this.opening_hours = opening_hours;
    }

    public String getSite_link() {
        return site_link;
    }

    public void setSite_link(String site_link) {
        this.site_link = site_link;
    }

    public String getFacebook_link() {
        return facebook_link;
    }

    public void setFacebook_link(String facebook_link) {
        this.facebook_link = facebook_link;
    }

    public String getPrices() {
        return prices;
    }

    public void setPrices(String prices) {
        this.prices = prices;
    }

    public String getDelivery_services() {
        return delivery_services;
    }

    public void setDelivery_services(String delivery_services) {
        this.delivery_services = delivery_services;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public ArrayList<String> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<String> pictures) {
        this.pictures = pictures;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    @Override
    public String toString() {
        return "business{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", addressCity='" + addressCity + '\'' +
                ", opening_hours='" + opening_hours + '\'' +
                ", site_link='" + site_link + '\'' +
                ", facebook_link='" + facebook_link + '\'' +
                ", prices='" + prices + '\'' +
                ", delivery_services='" + delivery_services + '\'' +
                ", phone='" + phone + '\'' +
                ", pictures=" + pictures +
                ", video='" + video + '\'' +
                ", key='" + key + '\'' +
                ", reviews=" + reviews +
                '}';
    }
}