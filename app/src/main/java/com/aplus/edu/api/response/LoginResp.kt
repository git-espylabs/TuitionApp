package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class LoginResp(

    @field:SerializedName("data")
    val data: Int? = null,

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("classid")
    val classid: Int? = null

)
