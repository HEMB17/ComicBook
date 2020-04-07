package com.example.comicbook.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DetailBookItem {

    @SerializedName("image")
    private ImageModel image;
    @SerializedName("name")
    private String name;
    @SerializedName("character_credits")
    private List<SubItemModel> character_credits;
    @SerializedName("team_credits")
    private List<SubItemModel> team_credits;
    @SerializedName("location_credits")
    private List<SubItemModel> location_credits;
    @SerializedName("concept_credits")
    private List<SubItemModel> concept_credits;

    public DetailBookItem(){
    }

    public DetailBookItem(ImageModel image, String nName, List<SubItemModel> character_credits, List<SubItemModel> team_credits, List<SubItemModel> location_credits, List<SubItemModel> concept_credits) {
        this.image = image;
        this.name = nName;
        this.character_credits = character_credits;
        this.team_credits = team_credits;
        this.location_credits = location_credits;
        this.concept_credits = concept_credits;
    }

    public ImageModel getImage() {
        return image;
    }

    public void setImage(ImageModel image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubItemModel> getCharacter_credits() {
        return character_credits;
    }

    public void setCharacter_credits(List<SubItemModel> character_credits) {
        this.character_credits = character_credits;
    }

    public List<SubItemModel> getTeam_credits() {
        return team_credits;
    }

    public void setTeam_credits(List<SubItemModel> team_credits) {
        this.team_credits = team_credits;
    }

    public List<SubItemModel> getLocation_credits() {
        return location_credits;
    }

    public void setLocation_credits(List<SubItemModel> location_credits) {
        this.location_credits = location_credits;
    }

    public List<SubItemModel> getConcept_credits() {
        return concept_credits;
    }

    public void setConcept_credits(List<SubItemModel> concept_credits) {
        this.concept_credits = concept_credits;
    }
}
