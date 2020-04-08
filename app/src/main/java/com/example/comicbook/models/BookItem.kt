package com.example.comicbook.models

import com.google.gson.annotations.SerializedName

class BookItem {
    @SerializedName("image")
    var image: ImageModel? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("issue_number")
    var issue_number: String? = null
    @SerializedName("date_added")
    var date_added: String? = null
    @SerializedName("api_detail_url")
    var api_detail_url: String? = null

    constructor() {}
    constructor(image: ImageModel?, nName: String?, nIssue_number: String?, nDate_added: String?, nApi_detail_url: String?) {
        this.image = image
        name = nName
        issue_number = nIssue_number
        date_added = nDate_added
        api_detail_url = nApi_detail_url
    }

}