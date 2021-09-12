package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class VideodetailsResp(

	@field:SerializedName("data")
	val data: List<VideodetailItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)


