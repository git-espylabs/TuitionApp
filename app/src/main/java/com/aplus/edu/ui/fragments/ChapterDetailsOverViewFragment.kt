package com.aplus.edu.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.aplus.edu.R
import com.aplus.edu.adapters.ExampdfAdapter
import com.aplus.edu.adapters.FaqsAdapter
import com.aplus.edu.adapters.StudymaterialsAdapter
import com.aplus.edu.api.manager.APIManager
import com.aplus.edu.api.response.*
import com.aplus.edu.api.services.TuitionApiService
import com.aplus.edu.constants.AppConstants
import com.aplus.edu.preference.UserInfo
import com.aplus.edu.ui.activities.BaseActivity.Companion.dismissProgress
import com.aplus.edu.ui.activities.BaseActivity.Companion.showProgress
import com.aplus.edu.ui.activities.Home
import com.aplus.edu.utils.CommonUtils
import com.aplus.edu.utils.MultiPartRequestHelper
import kotlinx.android.synthetic.main.activity_chapters_main_tabs_view.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException


class ChapterDetailsOverViewFragment : BaseFragment(), StudymaterialsAdapter.Subjectcommunicator,
    FaqsAdapter.Faqcommunicator, ExampdfAdapter.Exampdfcommunicator {
    var video_id = ""
    var videoTitle = ""
    var overviewtext=""
    var chapter_Ids=""
    var overviewItemList: ArrayList<OverviewItem> = ArrayList()
    var studymaterialItemList: ArrayList<StudymaterialItem> = ArrayList()
    var faqsItemList: ArrayList<FaqsItem> = ArrayList()
    var studymaterialsAdapter: StudymaterialsAdapter?=null
    var faqsAdapter: FaqsAdapter?=null
    var exampdfAdapter:ExampdfAdapter?=null
    var examsItemList: ArrayList<ExmPdfDataItem> = ArrayList()

    private var currentPhotoPath: String? = null
    private var absolutePhotoPathe: String? = null
    private var photoFile: File? = null
    var queriesDialog: Dialog? = null


    var cameraLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            setCameraPicToImageView()
        }
    }

    private fun initiateViews() {
        arguments?.let {
            if (it.containsKey("video_id")) {
                video_id = it["video_id"].toString()
            }
            if (it.containsKey("videoTitle")) {
                videoTitle = it["videoTitle"].toString()
                chapter_title_tv.text=videoTitle
            }
            if (it.containsKey("lessonId")) {
                chapter_Ids = it["lessonId"].toString()
                chapter_title_tv.text=videoTitle
            }

        }
        back_imv.setOnClickListener {
            //startActivity(Intent(activity, Home::class.java))
            requireActivity().onBackPressed();
        }

        askadoubtfaqs.setOnClickListener {
            //show dialog fragment
            //ask
            showaskqueriessDialog()
        }

        ///tabs click listner
        overview_tv .setBackgroundDrawable(resources.getDrawable(R.drawable.rect_bg_white_tabs_vw));
        val typeface = ResourcesCompat.getFont(requireActivity(), R.font.poppins_regular)
        val typeface1 = ResourcesCompat.getFont(requireActivity(), R.font.poppins_medium)
        overview_tv.typeface = typeface1
        overview_tv.setTextColor(resources.getColor(R.color.btn_clr1))
        overview_tv.setOnClickListener {
            overview_tv.typeface = typeface1
            studymaterials_tv.typeface = typeface
            faq_tv.typeface = typeface
            overview_tv.setTextColor(resources.getColor(R.color.btn_clr1))
            studymaterials_tv.setTextColor(resources.getColor(R.color.white))
            faq_tv.setTextColor(resources.getColor(R.color.white))
            /////
            examsLinear.visibility=View.GONE
            overvw_tv.text=overviewtext
            over_linear.visibility=View.VISIBLE
            study_materials_Linear.visibility=View.GONE
            faqsLinear.visibility=View.GONE
            askadoubtfaqs.visibility=View.VISIBLE

            //
            overview_tv .setBackgroundDrawable(resources.getDrawable(R.drawable.rect_bg_white_tabs_vw));
            studymaterials_tv .setBackgroundDrawable(null)
            faq_tv .setBackgroundDrawable(null);
            exam_tv.setBackgroundDrawable(null);
        }

        studymaterials_tv.setOnClickListener {
        //call api here

            overview_tv.typeface = typeface
            studymaterials_tv.typeface = typeface1
            faq_tv.typeface = typeface

            overview_tv.setTextColor(resources.getColor(R.color.white))
            studymaterials_tv.setTextColor(resources.getColor(R.color.btn_clr1))
            faq_tv.setTextColor(resources.getColor(R.color.white))
            /////
            examsLinear.visibility=View.GONE
            over_linear.visibility=View.GONE
            askadoubtfaqs.visibility=View.VISIBLE
            study_materials_Linear.visibility=View.VISIBLE
            faqsLinear.visibility=View.GONE
            getstudymaterialslist(video_id)

            overview_tv .setBackgroundDrawable(null);
            studymaterials_tv .setBackgroundDrawable(resources.getDrawable(R.drawable.rect_bg_white_tabs_vw))
            faq_tv .setBackgroundDrawable(null);
            exam_tv.setBackgroundDrawable(null);

        }

        faq_tv.setOnClickListener {

            overview_tv.typeface = typeface
            studymaterials_tv.typeface = typeface
            faq_tv.typeface = typeface1

            overview_tv.setTextColor(resources.getColor(R.color.white))
            studymaterials_tv.setTextColor(resources.getColor(R.color.white))
            faq_tv.setTextColor(resources.getColor(R.color.btn_clr1))
            /////
            examsLinear.visibility=View.GONE
            over_linear.visibility=View.GONE
            study_materials_Linear.visibility=View.GONE
            faqsLinear.visibility=View.VISIBLE
            askadoubtfaqs.visibility=View.VISIBLE
            getfaqslist(video_id)

            overview_tv .setBackgroundDrawable(null);
            studymaterials_tv .setBackgroundDrawable(null)
            faq_tv .setBackgroundDrawable(resources.getDrawable(R.drawable.rect_bg_white_tabs_vw));
            exam_tv.setBackgroundDrawable(null);

        }
        exam_tv.setOnClickListener {
            overview_tv.typeface = typeface
            studymaterials_tv.typeface = typeface
            faq_tv.typeface = typeface
            exam_tv.typeface = typeface1

            askadoubtfaqs.visibility=View.VISIBLE

            overview_tv.setTextColor(resources.getColor(R.color.white))
            studymaterials_tv.setTextColor(resources.getColor(R.color.white))
            faq_tv.setTextColor(resources.getColor(R.color.white))
            exam_tv.setTextColor(resources.getColor(R.color.btn_clr1))

            overview_tv .setBackgroundDrawable(null);
            studymaterials_tv .setBackgroundDrawable(null)
            faq_tv .setBackgroundDrawable(null);
            exam_tv .setBackgroundDrawable(resources.getDrawable(R.drawable.rect_bg_white_tabs_vw));

            examsLinear.visibility=View.VISIBLE
            over_linear.visibility=View.GONE
            study_materials_Linear.visibility=View.GONE
            faqsLinear.visibility=View.GONE

            getExamslist(chapter_Ids)
        }
        askadoubtfaqs.visibility=View.VISIBLE
        getChapterOverviewlist(video_id)

    }

    private fun getChapterOverviewlist(video_id: String) {
        lifecycleScope.launch {
            showProgress(activity, "Fetching data..")
            try {
                val response = APIManager.call<TuitionApiService, Response<ChapterdetoverviewResp>> {
                    chaptersoverview(chapteroverviewJsonRequest(video_id))
                }
                if (response != null) {
                    overviewItemList = (response.body()?.data as ArrayList<OverviewItem>?)!!
                    if (overviewItemList.size>0) {
                        setChaptersoverviewData(
                            overviewItemList

                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                dismissProgress()
            }
            dismissProgress()


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_chapters_main_tabs_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initiateViews()
    }

    private fun chapteroverviewJsonRequest(chapter_Id: String): RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("video_id", chapter_Id)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonData.toRequestBody()
    }

    private fun setChaptersoverviewData(overviewItem: ArrayList<OverviewItem>)
    {

    var overviewitems=overviewItem[0]
        val video: Uri =
            Uri.parse(AppConstants.VIDEO_URL+overviewitems.videoFile)
        val mc = MediaController(activity)
        mc.setAnchorView(videovvv)
        mc.setMediaPlayer(videovvv)
        videovvv.setMediaController(mc)
        videovvv.setVideoURI(video)
        videovvv.setOnPreparedListener { mp ->
            mp.isLooping = true
            videovvv.start()
        }
        videovvv.visibility=View.VISIBLE
        ///

        ////
        overvw_tv.text=overviewitems.overView
        overviewtext= overviewitems.overView!!

    }

    //////////////////////////////////////////////////////////////tabs api code here

    private fun getstudymaterialslist(video_Id: String) {
        lifecycleScope.launch {
            showProgress(activity, "Fetching data..")
            try {
                val response = APIManager.call<TuitionApiService, Response<StudymaterialsResp>> {
                    getStudymaterials(studymaterialsJsonRequest(video_Id))
                }
                if (response != null) {
                    studymaterialItemList = (response.body()?.data as ArrayList<StudymaterialItem>?)!!
                    if (studymaterialItemList.size>0) {
                        setstudymaterialsData(
                            studymaterialItemList

                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                dismissProgress()
            }
            dismissProgress()


        }
    }


    private fun studymaterialsJsonRequest(video_Id: String): RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("video_id", video_Id)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonData.toRequestBody()
    }

    private fun setstudymaterialsData(studymaterialItemList: ArrayList<StudymaterialItem>)
    {
        studymaterialsAdapter = StudymaterialsAdapter(requireActivity(),studymaterialItemList, this)
        val layoutManager = LinearLayoutManager(activity)
        study_materials_recycler_view.layoutManager = layoutManager
        study_materials_recycler_view.adapter = studymaterialsAdapter

    }

    override fun onRowClick(data: StudymaterialItem) {

        if(data!=null)
        {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.VIDEO_URL+data.file))
                startActivity(browserIntent)
            }
            catch (e:java.lang.Exception)
            {

            }

        }

    }

    ////faqs///////////////////////////////////////////////////////

    private fun getfaqslist(video_Id: String) {
        lifecycleScope.launch {
            showProgress(activity, "Fetching data..")
            try {
                val response = APIManager.call<TuitionApiService, Response<FaqsResp>> {
                    getfaqslist(faqsJsonRequest(video_Id))
                }
                if (response != null) {
                    faqsItemList = (response.body()?.data as ArrayList<FaqsItem>?)!!
                    if (faqsItemList.size>0) {
                        setfaqsData(
                            faqsItemList

                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                dismissProgress()
            }
            dismissProgress()


        }
    }


    private fun faqsJsonRequest(video_Id: String): RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("video_id", video_Id)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonData.toRequestBody()
    }

    private fun setfaqsData(FaqsItemList: ArrayList<FaqsItem>)
    {
        faqsAdapter = FaqsAdapter(requireActivity(),FaqsItemList, this)
        val layoutManager = LinearLayoutManager(activity)
        faqs_recycler_view.layoutManager = layoutManager
        faqs_recycler_view.adapter = faqsAdapter

    }

    override fun onRowClick(data: FaqsItem) {
        TODO("Not yet implemented")
    }

    ////////queries

    //askqueries
    private fun showaskqueriessDialog() {
        queriesDialog = Dialog(requireActivity(), R.style.DialogTheme)
        queriesDialog?.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            setCancelable(false)
            window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.help_dialog_box_common)
            val edt_queries = findViewById<EditText>(R.id.edt_queries)
            val edt_subject = findViewById<EditText>(R.id.edt_sbjt)
            val btnquery_smt = findViewById<TextView>(R.id.btnquery_smt)
            val close = findViewById<ImageView>(R.id.close)
            val capture = findViewById<ImageView>(R.id.capture)
            val userImg = findViewById<ImageView>(R.id.userImg)

            close.setOnClickListener { dismiss() }

            btnquery_smt.setOnClickListener {

                if (edt_queries.text.toString().isNotEmpty()) {
                    queriestofaqDb(edt_queries.text.toString(),this, edt_subject.text.toString())
                } else {
                    Toast.makeText(activity, "Please enter queries!!!", Toast.LENGTH_LONG).show()
                }

            }

            capture.setOnClickListener {
                dispatchTakePictureIntent()
            }

            userImg.setOnClickListener {
                dispatchTakePictureIntent()
            }

            show()
        }

    }


    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            try {
                photoFile = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireActivity(),
                        CommonUtils.getFileProviderName(requireContext()),
                        it
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    cameraLaucher.launch(takePictureIntent)
                }
            } catch (_: Exception) {

            }
        }
    }

    private fun createImageFile(): File {
        return CommonUtils.createImageFile(requireContext(),
            "USER"
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    private fun setCameraPicToImageView() {
        scaleDownImage(BitmapFactory.decodeFile(currentPhotoPath))
        deleteOriginalCameraImage()
    }

    private fun scaleDownImage(image: Bitmap) {
        with(CommonUtils.scaleDownImage(image)) {
            CommonUtils.compressAndSaveImage(requireContext(), this, "USER").also {
                absolutePhotoPathe = it.absolutePath
                setImage(it.absolutePath)
            }
        }
    }

    private fun deleteOriginalCameraImage() {
        try {
            photoFile?.delete()
        } catch (_: Exception) {
        }
        photoFile = null
    }

    private fun setImage(path: String){
        val image = BitmapFactory.decodeFile(path)
        image?.let {
            if (queriesDialog !=null && queriesDialog?.isShowing == true){
                val userImg = queriesDialog?.findViewById<ImageView>(R.id.userImg)
                userImg?.apply {
                    setImageBitmap(image)
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    setPadding(0, 0, 0, 0)
                }
            }
        }
    }

    private fun queriestofaqDb(queries: String, dialog: Dialog, subject: String) {
        lifecycleScope.launch {
            showProgress(activity, "Fetching data..")
            try {

                val partLogin_id = MultiPartRequestHelper.createRequestBody("login_id", UserInfo().uid)
                val partQuiery = MultiPartRequestHelper.createRequestBody("quiery", queries)
                val partSubject = MultiPartRequestHelper.createRequestBody("subject", subject)
                val partFile = MultiPartRequestHelper.createFileRequestBody(absolutePhotoPathe?:"", "image", requireContext())

                val response = APIManager.call<TuitionApiService, Response<AskadoubthomeResp>> {
                    addQueries(partLogin_id, partQuiery, partSubject, partFile)
                }
                if (response != null) {
                    if (response.body()!!.error == false) {
                        Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                    }
                    else
                    {

                        Toast.makeText(activity, response.body()!!.message, Toast.LENGTH_LONG).show()
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
                dismissProgress()
            }
            dismissProgress()


        }
    }

    private fun queriesJsonRequest(queries: String): RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("quiery", queries)
            json.put("video_id", video_id)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

    //exams

    private fun getExamslist(chapter_Id: String) {
        lifecycleScope.launch {
            showProgress(activity, "Fetching data..")
            try {
                val response = APIManager.call<TuitionApiService, Response<ExampdfResp>> {
                    exampdf(examsJsonRequest(chapter_Id))
                }
                if (response != null) {
                    examsItemList = (response.body()?.data as ArrayList<ExmPdfDataItem>?)!!
                    if (examsItemList.size>0) {
                        setexamsData(
                            examsItemList

                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                dismissProgress()
            }
            dismissProgress()


        }
    }

    private fun examsJsonRequest(chapter_Id: String): RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("lessonid", chapter_Id)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonData.toRequestBody()
    }

    private fun setexamsData(exmsItemList: ArrayList<ExmPdfDataItem>)
    {
        exampdfAdapter = ExampdfAdapter(requireActivity(),exmsItemList, this)
        val layoutManager = LinearLayoutManager(activity)
        exams_recycler_view.layoutManager = layoutManager
        exams_recycler_view.adapter = exampdfAdapter

    }

    override fun onRowClick(data: ExmPdfDataItem) {
        if(data!=null)
        {
            try {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(AppConstants.VIDEO_URL+data.pdf))
                startActivity(browserIntent)
            }
            catch (e:java.lang.Exception)
            {

            }

        }
    }
}