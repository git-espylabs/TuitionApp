package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class ChaptersResp(

	@field:SerializedName("data")
	val data: List<ChaptersItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)


