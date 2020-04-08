package com.example.comicbook.models

import com.example.comicbook.services.SubItemModel
import com.google.gson.annotations.SerializedName

class DetailBookItem {
    @SerializedName("image")
    var image: ImageModel? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("character_credits")
    var character_credits: List<SubItemModel>? = null
    @SerializedName("team_credits")
    var team_credits: List<SubItemModel>? = null
    @SerializedName("location_credits")
    var location_credits: List<SubItemModel>? = null
    @SerializedName("concept_credits")
    var concept_credits: List<SubItemModel>? = null

    constructor() {}
    constructor(image: ImageModel?, nName: String?, character_credits: List<SubItemModel>?, team_credits: List<SubItemModel>?, location_credits: List<SubItemModel>?, concept_credits: List<SubItemModel>?) {
        this.image = image
        name = nName
        this.character_credits = character_credits
        this.team_credits = team_credits
        this.location_credits = location_credits
        this.concept_credits = concept_credits
    }

}