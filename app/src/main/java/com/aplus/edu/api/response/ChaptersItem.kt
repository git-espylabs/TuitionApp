package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class ChaptersItem(

	@field:SerializedName("lesson_name")
	val lessonName: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("subject")
	val subject: Int? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("chapter_title")
	val chapterTitle: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("videocount")
	val videocount: Int? = null,

	@field:SerializedName("subject_name")
	val subject_name: String? = null
)