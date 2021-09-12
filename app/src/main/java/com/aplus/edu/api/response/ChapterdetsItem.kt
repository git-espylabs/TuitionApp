package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class ChapterdetsItem(

	@field:SerializedName("video_duration")
	val videoDuration: String? = null,

	@field:SerializedName("chapter")
	val chapter: String? = null,

	@field:SerializedName("video_title")
	val videoTitle: String? = null,

	@field:SerializedName("short_desc")
	val shortDesc: String? = null,

	@field:SerializedName("video_thumb")
	val videoThumb: String? = null,

	@field:SerializedName("video_id")
	val videoId: Int? = null
)