package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class SubjectItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("class_id")
	val classId: Int? = null,

	@field:SerializedName("subject_image")
	val subjectImage: String? = null,

	@field:SerializedName("subject_name")
	val subjectName: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null
)