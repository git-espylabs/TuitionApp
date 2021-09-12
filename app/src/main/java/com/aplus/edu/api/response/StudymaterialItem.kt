package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class StudymaterialItem(

	@field:SerializedName("file")
	val file: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("file_size")
	val fileSize: String? = null,

	@field:SerializedName("video_id")
	val videoId: Int? = null
)