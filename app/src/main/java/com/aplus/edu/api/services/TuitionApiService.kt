package com.aplus.edu.api.services


import com.aplus.edu.api.response.*
import com.aplus.edu.api.response.AppVersionMaster
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface TuitionApiService {


    @POST("stud_reg")
    suspend fun signupUser(@Body jsonRequest : RequestBody): Response<RegisterResp>

    @POST("login")
    suspend fun loginUser(@Body jsonRequest : RequestBody): Response<LoginResp>

    @GET("classes")
    suspend fun classes(): Response<ClassResp>

///home
    @POST("subjects")
    suspend fun getsubjects(@Body jsonRequest : RequestBody): Response<SubjectResp>

    @POST("chapter")
    suspend fun getchapters(@Body jsonRequest : RequestBody): Response<ChaptersResp>

    @POST("chapter_videos")
    suspend fun getdetchapters(@Body jsonRequest : RequestBody): Response<ChapterdetsResp>

    @POST("video_details")
    suspend fun chaptersoverview(@Body jsonRequest : RequestBody): Response<ChapterdetoverviewResp>


    @POST("materials")
    suspend fun getStudymaterials(@Body jsonRequest : RequestBody): Response<StudymaterialsResp>

    @POST("faqlist")
    suspend fun getfaqslist(@Body jsonRequest : RequestBody): Response<FaqsResp>

    @POST("quiery")
    suspend fun askadoubthome(@Body jsonRequest : RequestBody): Response<AskadoubthomeResp>

    @POST("faqinsert")
    suspend fun askadoubthomefaq(@Body jsonRequest : RequestBody): Response<AskadoubthomeResp>

    @POST("profile")
    suspend fun getProfile(@Body jsonRequest : RequestBody): Response<ProfileViewResp>

////
    @POST("forget_pwd")
    suspend fun forgotPassword(@Body jsonRequest : RequestBody): Response<ForgotpassResp>

    @POST("refer_insert")
    suspend fun referData(@Body jsonRequest : RequestBody): Response<ReferResp>

    @GET("app_version")
    suspend fun getAppVersionFromServer(): Response<AppVersionMaster>

    @POST("videoview_insert")
    suspend fun postvideoclick(@Body jsonRequest : RequestBody): Response<ProfileViewResp>

    @POST("exampdf")
    suspend fun exampdf(@Body jsonRequest : RequestBody): Response<ExampdfResp>

    @Multipart
    @POST("quiery")
    suspend fun addQueries(
        @Part login_id: MultipartBody.Part,
        @Part quiery: MultipartBody.Part,
        @Part subject: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ): Response<AskadoubthomeResp>

    @POST("book_counsel")
    suspend fun bookCouncil(@Body jsonRequest : RequestBody): Response<CommonResponse>

}