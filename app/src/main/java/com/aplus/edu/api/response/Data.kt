package com.aplus.edu.api.response

import com.google.gson.annotations.SerializedName

data class Data(

	@field:SerializedName("stud_phonenumber")
	val studPhonenumber: String? = null,

	@field:SerializedName("stud_address")
	val studAddress: Any? = null,

	@field:SerializedName("stud_login_id")
	val studLoginId: Int? = null,

	@field:SerializedName("stud_name")
	val studName: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("stud_blocked_status")
	val studBlockedStatus: Int? = null,

	@field:SerializedName("stud_class")
	val studClass: Int? = null,

	@field:SerializedName("stud_photo")
	val studPhoto: String? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("stud_parent_phnum")
	val studParentPhnum: Any? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("stud_parent_name")
	val studParentName: Any? = null,

	@field:SerializedName("stud_age")
	val studAge: Any? = null,

	@field:SerializedName("email")
	val emailid: String? = null,

	@field:SerializedName("class_name")
	val class_name: String? = null
)