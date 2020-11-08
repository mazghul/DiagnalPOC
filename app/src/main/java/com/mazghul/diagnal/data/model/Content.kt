package com.mazghul.diagnal.data.model

import com.google.gson.annotations.SerializedName

data class Content(
    val name: String,
    @SerializedName("poster-image")
    val poserImage: String
)