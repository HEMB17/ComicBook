package com.example.comicbook.models;

import java.util.List;

public class ImageModel {

    private String original_url;

    public ImageModel(String original_url) {
        this.original_url = original_url;
    }

    public String getOriginal_url() {
        return original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }
}
