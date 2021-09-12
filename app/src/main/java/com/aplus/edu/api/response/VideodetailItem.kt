package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class VideodetailItem(

	@field:SerializedName("video_duration")
	val videoDuration: String? = null,

	@field:SerializedName("video_title")
	val videoTitle: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("over_view")
	val overView: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("video_file")
	val videoFile: String? = null,

	@field:SerializedName("short_desc")
	val shortDesc: String? = null,

	@field:SerializedName("lesson_id")
	val lessonId: Int? = null,

	@field:SerializedName("video_thumb")
	val videoThumb: String? = null
)