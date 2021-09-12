package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class ForgotpassResp(

	@field:SerializedName("data")
	val data: Int? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null

)
