package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class FaqsItem(

	@field:SerializedName("qu_answer")
	val quAnswer: String? = null,

	@field:SerializedName("qu_title")
	val quTitle: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("video_id")
	val videoId: Int? = null
)