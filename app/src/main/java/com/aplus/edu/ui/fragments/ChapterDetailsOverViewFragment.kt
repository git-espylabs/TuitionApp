package com.aplus.edu.ui.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
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
import com.aplus.edu.extensions.setSelectedMode
import com.aplus.edu.preference.UserInfo
import com.aplus.edu.ui.activities.BaseActivity.Companion.dismissProgress
import com.aplus.edu.ui.activities.BaseActivity.Companion.showProgress
import com.aplus.edu.ui.activities.Home
import com.aplus.edu.utils.CommonUtils
import com.aplus.edu.utils.MultiPartRequestHelper
import com.aplus.edu.utils.ScalableVideoView
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
import android.util.DisplayMetrics





class ChapterDetailsOverViewFragment : BaseFragment(), StudymaterialsAdapter.Subjectcommunicator,
    FaqsAdapter.Faqcommunicator, ExampdfAdapter.Exampdfcommunicator, View.OnClickListener {
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
    var selectedTab = 1


    var cameraLaucher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            setCameraPicToImageView()
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

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.back_imv ->{
                requireActivity().onBackPressed();
            }

            R.id.askadoubtfaqs ->{
                showaskqueriessDialog()
            }

            R.id.overview_tv ->{
                selectTab(1)
            }

            R.id.studymaterials_tv ->{
                selectTab(2)
            }

            R.id.faq_tv ->{
                selectTab(3)
            }

            R.id.exam_tv ->{
                selectTab(4)
            }
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

        back_imv.setOnClickListener(this)
        askadoubtfaqs.setOnClickListener(this)
        overview_tv.setOnClickListener(this)
        studymaterials_tv.setOnClickListener(this)
        faq_tv.setOnClickListener(this)
        exam_tv.setOnClickListener(this)

        askadoubtfaqs.visibility=View.VISIBLE

        overview_tv.setSelectedMode(true)
        getChapterOverviewlist(video_id)

    }

    private fun selectTab(tabNumber: Int){
        when(tabNumber){
            1 -> {
                selectedTab = 1

                over_linear.visibility=View.VISIBLE
                overview_tv.setSelectedMode(true)

                studymaterials_tv.setSelectedMode(false)
                faq_tv.setSelectedMode(false)
                exam_tv.setSelectedMode(false)

                study_materials_Linear.visibility=View.GONE
                faqsLinear.visibility=View.GONE
                examsLinear.visibility=View.GONE
                askadoubtfaqs.visibility=View.VISIBLE
            }

            2 ->{
                selectedTab = 2

                getstudymaterialslist(video_id)

                study_materials_Linear.visibility=View.VISIBLE
                studymaterials_tv.setSelectedMode(true)

                overview_tv.setSelectedMode(false)
                faq_tv.setSelectedMode(false)
                exam_tv.setSelectedMode(false)

                over_linear.visibility=View.GONE
                faqsLinear.visibility=View.GONE
                examsLinear.visibility=View.GONE
                askadoubtfaqs.visibility=View.VISIBLE
            }

            3 ->{
                selectedTab = 3

                getfaqslist(video_id)

                faqsLinear.visibility=View.VISIBLE
                faq_tv.setSelectedMode(true)

                overview_tv.setSelectedMode(false)
                studymaterials_tv.setSelectedMode(false)
                exam_tv.setSelectedMode(false)

                over_linear.visibility=View.GONE
                study_materials_Linear.visibility=View.GONE
                examsLinear.visibility=View.GONE
                askadoubtfaqs.visibility=View.VISIBLE
            }

            4 ->{
                selectedTab = 4

                getExamslist(chapter_Ids)

                examsLinear.visibility=View.VISIBLE
                exam_tv.setSelectedMode(true)

                overview_tv.setSelectedMode(false)
                studymaterials_tv.setSelectedMode(false)
                faq_tv.setSelectedMode(false)


                over_linear.visibility=View.GONE
                study_materials_Linear.visibility=View.GONE
                faqsLinear.visibility=View.GONE
                askadoubtfaqs.visibility=View.VISIBLE
            }
        }
    }

    /* Chapter Overview*/

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
        videovvv.setDisplayMode(ScalableVideoView.DisplayMode.FULL_SCREEN)
        videovvv.setOnPreparedListener { mp ->
            mp.isLooping = true
            videovvv.setBackgroundColor(Color.TRANSPARENT);
            videovvv.start()
        }
        videovvv.visibility=View.VISIBLE

        overvw_tv.text=overviewitems.overView
        overviewtext= overviewitems.overView!!

    }


    /* Study Materials*/
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


    /* FAQs*/
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


    /* Exams*/
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

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setLandscapeMode()
        }else{
            setPortraitMode()
        }
    }

    private fun setLandscapeMode(){
        headerLay.visibility = View.GONE
        subHeaderLay.visibility = View.GONE
        askadoubtfaqs.visibility=View.GONE
        over_linear.visibility=View.GONE
        study_materials_Linear.visibility=View.GONE
        faqsLinear.visibility=View.GONE
        examsLinear.visibility=View.GONE

        val displayMetrics = resources.displayMetrics
        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        val params: ViewGroup.LayoutParams = videovvv.layoutParams
        params.height = height
        videovvv.layoutParams = params
    }

    private fun setPortraitMode(){
        headerLay.visibility = View.VISIBLE
        subHeaderLay.visibility = View.VISIBLE
        askadoubtfaqs.visibility=View.VISIBLE

        selectTab(selectedTab)

        val scale = resources.displayMetrics.density
        val height = (230 * scale + 0.5f).toInt()

        val params: ViewGroup.LayoutParams = videovvv.layoutParams
        params.height = height
        videovvv.layoutParams = params
    }

}