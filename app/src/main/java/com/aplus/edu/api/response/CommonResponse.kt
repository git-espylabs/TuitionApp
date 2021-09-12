package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class CommonResponse(
    @SerializedName("error") val error: String,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: String
)
