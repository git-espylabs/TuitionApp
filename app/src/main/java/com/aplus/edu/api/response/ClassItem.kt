package com.aplus.edu.api.response
import com.google.gson.annotations.SerializedName

data class ClassItem(

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("class_name")
	val className: String? = null
)