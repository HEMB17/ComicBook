package com.example.comicbook.models;

import com.google.gson.annotations.SerializedName;


public class BookItem {

    @SerializedName("image")
    private ImageModel image;
    @SerializedName("name")
    private String name;
    @SerializedName("issue_number")
    private String issue_number;
    @SerializedName("date_added")
    private String date_added;
    @SerializedName("api_detail_url")
    private String api_detail_url;

    public BookItem(){
    }

    public BookItem(ImageModel image, String nName, String nIssue_number, String nDate_added, String nApi_detail_url) {
        this.image = image;
        this.name = nName;
        this.issue_number = nIssue_number;
        this.date_added = nDate_added;
        this.api_detail_url = nApi_detail_url;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

    public ImageModel getImage() {
        return image;
    }

    public String getIssue_number() {
        return issue_number;
    }

    public void setIssue_number(String issue_number) {
        this.issue_number = issue_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate_added() {
        return date_added;
    }

    public void setDate_added(String nDate_added) {
        this.date_added = nDate_added;
    }

    public String getApi_detail_url() {
        return api_detail_url;
    }

    public void setApi_detail_url(String api_detail_url) {
        this.api_detail_url = api_detail_url;
    }
}
