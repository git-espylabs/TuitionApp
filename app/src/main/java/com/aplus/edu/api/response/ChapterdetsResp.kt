package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class ChapterdetsResp(

	@field:SerializedName("data")
	val data: List<ChapterdetsItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)


