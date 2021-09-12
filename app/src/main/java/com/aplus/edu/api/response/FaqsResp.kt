package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class FaqsResp(

	@field:SerializedName("data")
	val data: List<FaqsItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)


