package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class ChapterdetoverviewResp(

	@field:SerializedName("data")
	val data: List<OverviewItem?>? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)


