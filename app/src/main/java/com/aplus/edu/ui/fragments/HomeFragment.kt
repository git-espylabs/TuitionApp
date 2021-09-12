package com.aplus.edu.ui.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplus.edu.R
import com.aplus.edu.adapters.ChaptersAdapter
import com.aplus.edu.adapters.SubjectAdapter
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
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.home.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.io.IOException


class HomeFragment : BaseFragment(), SubjectAdapter.Subjectcommunicator,
    ChaptersAdapter.Chaptercommunicator {

    var sublectList: ArrayList<SubjectItem> = ArrayList()
    var chaptersList: ArrayList<ChaptersItem> = ArrayList()
    var adapter: SubjectAdapter? = null
    var chaptersadapter: ChaptersAdapter? = null
    lateinit var dialog: Dialog


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
        askadoubthome11.setOnClickListener {
            //show dialog fragment
            //ask
            showaskqueriessDialog()
        }
        getSubjectlist()
        getProfile()
        ///
        profile.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("test", "t")
            (activity as Home).onProfileNav(bundle)
        }

        refer_a_friend.setOnClickListener {

            showreferafriendDialog()
        }

        counciling.setOnClickListener {
            showBookCouncilConfirmation()
        }

    }

    private fun showBookCouncilConfirmation(){
        AlertDialog.Builder(requireContext())
            .setTitle("Confirm!")
            .setMessage("Do you need counseling support?")
            .setPositiveButton("Yes") { dialog, _ ->
                bookACouncil()
                dialog.dismiss()
            }
            .setNegativeButton("Dismiss") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun bookACouncil(){
        lifecycleScope.launch {
            showProgress(activity, "Processing..")
            try {
                val response = APIManager.call<TuitionApiService, Response<CommonResponse>> {
                    bookCouncil(bookCouncilJsonRequest())
                }
                if (response != null) {
                    if(response.body()?.error.equals("false", true)) {
                        Toast.makeText(activity,"Your request has been processed successfully.",Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        Toast.makeText(activity,response.body()!!.message,Toast.LENGTH_LONG).show()
                    }

                }
            } catch (e: Exception) {
                e.printStackTrace()
                dismissProgress()
            }
            dismissProgress()
        }
    }

    private fun bookCouncilJsonRequest(): RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("login_id", UserInfo().uid)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

    private fun getSubjectlist() {
        lifecycleScope.launch {
            showProgress(activity, "Fetching data..")
            try {
                val response = APIManager.call<TuitionApiService, Response<SubjectResp>> {
                    getsubjects(subjectJsonRequest())
                }
                if (response != null) {
                    if(response.body()!!.error==false) {
                        sublectList = (response.body()?.data as ArrayList<SubjectItem>?)!!

                        if (sublectList.size > 0) {
                            setData(sublectList)
                        }
                    }
                    else
                    {
                        Toast.makeText(activity,response.body()!!.message,Toast.LENGTH_LONG).show()
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
        return inflater.inflate(R.layout.home, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initiateViews()
    }

    override fun onRowClick(data: SubjectItem) {
        if (data != null) {
            val bundle = Bundle()
            bundle.putString("id", data.id.toString())
            //(activity as Home).onChapterlistNav(bundle, data.subjectName!!)
            //show dialog here and nav to chapters details
            showChaptersDialog(data.id.toString(), data.subjectName)

        }
    }

    private fun subjectJsonRequest(): RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("user_id", UserInfo().uid)
            json.put("classes_id", UserInfo().classid)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

    private fun showChaptersDialog(subjectid: String, title: String?) {
        dialog = Dialog(requireActivity(), R.style.DialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_chapters_list)
        val chapter_title = dialog.findViewById(R.id.chapter_title) as TextView
        chapter_title.text = "$title Chapters "
        val close = dialog.findViewById(R.id.close) as ImageView
        val chapters_recycler_view =
            dialog.findViewById(R.id.chapters_recycler_view) as RecyclerView
        close.setOnClickListener { dialog.dismiss() }
        //load chapterlist
        loadChapterslist(subjectid, chapters_recycler_view)
        dialog.show()

    }

    private fun setData(sublectList: ArrayList<SubjectItem>) {
        adapter = SubjectAdapter(requireActivity(), sublectList, this)
        val layoutManager = GridLayoutManager(activity, 3)
        subjects_recycler_view.layoutManager = layoutManager
        subjects_recycler_view.adapter = adapter
        subjects_recycler_view.isNestedScrollingEnabled = false;

    }

    private fun setChaptersData(
        chaptersList: ArrayList<ChaptersItem>,
        chapters_recycler_view: RecyclerView
    ) {
        chaptersadapter = ChaptersAdapter(requireActivity(), chaptersList, this)
        val layoutManager = LinearLayoutManager(activity)
        chapters_recycler_view.layoutManager = layoutManager
        chapters_recycler_view.adapter = chaptersadapter

    }

    //
    fun loadChapterslist(subjectid: String, chapters_recycler_view: RecyclerView) {
        getChapterslist(subjectid, chapters_recycler_view)
    }


    private fun chaptersJsonRequest(subjectid: String): RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("subject", subjectid)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

    private fun getChapterslist(subjectid: String, chapters_recycler_view: RecyclerView) {
        lifecycleScope.launch {
            showProgress(activity, "Fetching data..")
            try {
                val response = APIManager.call<TuitionApiService, Response<ChaptersResp>> {
                    getchapters(chaptersJsonRequest(subjectid))
                }
                if (response != null) {
                    chaptersList = (response.body()?.data as ArrayList<ChaptersItem>?)!!
                    if (chaptersList.size > 0) {
                        setChaptersData(chaptersList, chapters_recycler_view)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                dismissProgress()
            }
            dismissProgress()


        }
    }

    override fun onRowClick(data: ChaptersItem) {

        if (data != null) {
            dialog.dismiss()
            val bundle = Bundle()
            bundle.putString("chapter_id", data.id.toString())
            bundle.putString("subject", data.subject_name)
            bundle.putString("subject_id", data.subject.toString())
            (activity as Home).onChapterdetlistNav(bundle)

        }

    }

    ///get profile
    private fun getProfile() {
        lifecycleScope.launch {
            //showProgress(activity, "Fetching data..")
            try {
                val response = APIManager.call<TuitionApiService, Response<ProfileViewResp>> {
                    getProfile(profileJsonRequest())
                }
                if (response != null) {
                    var data = response.body()?.data
                    if (data != null) {
                        setProfileData(data)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                dismissProgress()
            }
            dismissProgress()


        }
    }

    private fun profileJsonRequest(): RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("login_id", UserInfo().uid)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

    fun setProfileData(data: Data) {
        //set profile image
        if (data.studPhoto != null) {
            Glide.with(requireActivity())
                .load(AppConstants.IMAGE_URL + data.studPhoto)
                //.apply(RequestOptions.circleCropTransform())
                //.placeholder(R.drawable.ic_profile_placeholder)
                .into(profile)
        }
        //add name ,class,email,mob,image url
        saveUserInfos(data.emailid!!,data.studPhonenumber,data.studPhoto,data.studName,data.class_name)
    }
    private fun saveUserInfos(
        email: String,
        studPhonenumber: String?,
        studPhoto: String?,
        studName: String?,
        studClass: String?
    ) {
        UserInfo().emailid = email
        UserInfo().mob = studPhonenumber!!
        UserInfo().photo = studPhoto!!
        UserInfo().studname = studName!!
        UserInfo().studclass = studClass.toString()
    }

    ////

    //askqueries
    private fun showaskqueriessDialog2() {
        var dialog = Dialog(requireActivity(), R.style.DialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.help_dialog_box_common)
        val edt_queries = dialog.findViewById(R.id.edt_queries) as EditText
        val btnquery_smt = dialog.findViewById(R.id.btnquery_smt) as TextView
        val close = dialog.findViewById(R.id.close) as ImageView

        close.setOnClickListener { dialog.dismiss() }
        //loadChapterslist(subjectid,chapters_recycler_view)
        btnquery_smt.setOnClickListener {

            if (edt_queries.text.toString().isNotEmpty()) {
                queriestoDb(edt_queries.text.toString(),dialog)
            } else {
                Toast.makeText(activity, "Please enter queries!!!", Toast.LENGTH_LONG).show()
            }

        }
        dialog.show()

    }

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


    private fun queriestoDb(queries: String, dialog: Dialog) {
        lifecycleScope.launch {
            showProgress(activity, "Fetching data..")
            try {
                val response = APIManager.call<TuitionApiService, Response<AskadoubthomeResp>> {
                    askadoubthome(queriesJsonRequest(queries))
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
            json.put("login_id", UserInfo().uid)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }


    ///refer a friend
    private fun showreferafriendDialog() {
        var dialog = Dialog(requireActivity(), R.style.DialogTheme)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.help_dialog_box_refer_friend)
        val ref_edt_name = dialog.findViewById(R.id.ref_edt_name) as EditText
        val ref_edt_email = dialog.findViewById(R.id.ref_edt_email) as EditText
        val ref_edt_mob = dialog.findViewById(R.id.ref_edt_mob) as EditText
        val refbtnquery_smt = dialog.findViewById(R.id.refbtnquery_smt) as TextView
        val close = dialog.findViewById(R.id.close) as ImageView

        close.setOnClickListener { dialog.dismiss() }

        refbtnquery_smt.setOnClickListener {

            if (ref_edt_name.text.toString().isEmpty()) {

                Toast.makeText(activity, "Please enter name!!!", Toast.LENGTH_LONG).show()
            }
            else if(ref_edt_email.text.toString().isEmpty())
            {
                Toast.makeText(activity, "Please enter email!!!", Toast.LENGTH_LONG).show()
            }
            else if(!isValidEmail(ref_edt_email.text.toString())){
                Toast.makeText(activity, "Please enter valid emailId" , Toast.LENGTH_LONG).show()
            }
            else if(ref_edt_mob.text.toString().isEmpty())
            {
                Toast.makeText(activity, "Please enter mobile number!!!", Toast.LENGTH_LONG).show()
            }
            else {

                referfrndtoDb(ref_edt_name.text.toString(),
                    ref_edt_email.text.toString(),
                    ref_edt_mob.text.toString(),
                    dialog
                )
            }

        }
        dialog.show()

    }

    private fun referfrndtoDb(
        name: String,
        email: String,
        mob: String,
        dialog: Dialog

        ) {
        lifecycleScope.launch {
            showProgress(activity, "Fetching data..")
            try {
                val response = APIManager.call<TuitionApiService, Response<ReferResp>> {
                    referData(referfrndJsonRequest(name,email,mob))
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

    private fun referfrndJsonRequest(name: String, email: String, mob: String): RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("name", name)
            json.put("mobnum", email)
            json.put("gmail", mob)
            json.put("referby", UserInfo().uid)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }



}