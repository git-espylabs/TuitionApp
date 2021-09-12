package com.aplus.edu.ui.fragments
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aplus.edu.R
import com.aplus.edu.adapters.ChaptersdetaillistAdapter
import com.aplus.edu.api.manager.APIManager
import com.aplus.edu.api.response.*
import com.aplus.edu.api.services.TuitionApiService
import com.aplus.edu.preference.UserInfo
import com.aplus.edu.ui.activities.BaseActivity.Companion.dismissProgress
import com.aplus.edu.ui.activities.BaseActivity.Companion.showProgress
import com.aplus.edu.ui.activities.Home
import kotlinx.android.synthetic.main.activity_chapters_detailsq_list.*
import kotlinx.android.synthetic.main.login.*
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response

class ChapterDetailsFragment : BaseFragment(), ChaptersdetaillistAdapter.Chapterdetcommunicator {
    var chapter_Id = ""
    var subject = ""
    var subject_Id=""
    var chapterdetsList: ArrayList<ChapterdetsItem> = ArrayList()
    var chaptersdetadapter: ChaptersdetaillistAdapter?=null
    var chaptersList: ArrayList<ChaptersItem> = ArrayList()
    val chapternames:ArrayList<String>?= ArrayList()
    var spinnerView2_val1=""
    private fun initiateViews() {
        arguments?.let {
            if (it.containsKey("chapter_id")) {
                chapter_Id = it["chapter_id"].toString()
            }
            if (it.containsKey("subject")) {
                subject = it["subject"].toString()
                chapter_title_tv.text=subject
            }
            if (it.containsKey("subject_id")) {
                subject_Id = it["subject_id"].toString()
                //chapter_title_tv.text=subject
                getChapterslist(subject_Id)
            }

        }
        back_imv.setOnClickListener {
            //startActivity(Intent(activity, Home::class.java))
            requireActivity().onBackPressed();
        }
        getChapterDetslist(chapter_Id)
    }

    private fun getChapterDetslist(chapter_Id: String) {
        lifecycleScope.launch {
            showProgress(activity, "Fetching data..")
            try {
                val response = APIManager.call<TuitionApiService, Response<ChapterdetsResp>> {
                    getdetchapters(chapterDetJsonRequest(chapter_Id))
                }
                if (response != null) {
                    chapterdetsList = (response.body()?.data as ArrayList<ChapterdetsItem>?)!!
                    if (chapterdetsList.size>0) {
                        setChaptersdetData(
                            chapterdetsList

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
        return inflater.inflate(R.layout.activity_chapters_detailsq_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initiateViews()
    }

    override fun onRowClick(data: ChapterdetsItem) {
        if (data != null) {
            val bundle = Bundle()
            bundle.putString("video_id", data.videoId.toString())
            bundle.putString("videoTitle", data.videoTitle.toString())
            bundle.putString("lessonId", chapter_Id)
            (activity as Home).onChapterdetOverviewlistNav(bundle)
            //show dialog here and nav to chapters details
            //api here
            getvideocount(data.videoId.toString())

        }
    }

    private fun chapterDetJsonRequest(chapter_Id: String): RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("chapter_id", chapter_Id)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonData.toRequestBody()
    }

    private fun setChaptersdetData(chapterdetsList: ArrayList<ChapterdetsItem>)
    {
        chaptersdetadapter = ChaptersdetaillistAdapter(requireActivity(),chapterdetsList, this)
        val layoutManager = LinearLayoutManager(activity)
        chapters_dtlq_recycler_view.layoutManager = layoutManager
        chapters_dtlq_recycler_view.adapter = chaptersdetadapter

    }

    ////spinnerlist

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

    private fun getChapterslist(subjectid: String) {
        lifecycleScope.launch {
            //showProgress(activity, "Fetching data..")
            try {
                val response = APIManager.call<TuitionApiService, Response<ChaptersResp>> {
                    getchapters(chaptersJsonRequest(subjectid))
                }
                if (response != null) {
                    chaptersList = (response.body()?.data as ArrayList<ChaptersItem>?)!!
                    if (chaptersList.size > 0) {
                        setDatatoSpinner(chaptersList)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                dismissProgress()
            }
            dismissProgress()


        }
    }

    //
    fun setDatatoSpinner(chptr_dta: List<ChaptersItem?>?)
    {
        chapternames!!.clear()
        chapternames!!.add(0,"Select Chapter")
        for (i in chptr_dta!!.indices) {
            chapternames!!.add(chptr_dta[i]!!.chapterTitle!!)
        }

        val spinnerArrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, chapternames)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerView22.adapter = spinnerArrayAdapter

        spinnerView22.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View,
                position: Int,
                id: Long
            ) {

                if(position>0)
                {
                    val chapItem: ChaptersItem = chaptersList[position-1]!!
                    spinnerView2_val1= chapItem.id.toString()
                    //api here
                    getChapterDetslist(spinnerView2_val1)
                    //Toast.makeText(this@LoginActivity,"id"+spinnerView2_val1,Toast.LENGTH_LONG).show()
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // can leave this empty
            }
        }



    }


    ////
    //video count

    private fun getvideocount(videoid: String) {
        lifecycleScope.launch {
            //showProgress(activity, "Fetching data..")
            try {
                val response = APIManager.call<TuitionApiService, Response<ProfileViewResp>> {
                    postvideoclick(videoJsonRequest(videoid))
                }
                if (response != null) {

                }
            } catch (e: Exception) {
                e.printStackTrace()
                dismissProgress()
            }
            dismissProgress()


        }
    }

    ////
    private fun videoJsonRequest(videoid: String): RequestBody {

        var jsonData = ""
        var json: JSONObject? = null
        try {
            json = JSONObject()
            json.put("video_id", videoid)
            json.put("stud_id", UserInfo().uid)
            jsonData = json.toString()
        } catch (e: JSONException) {
            e.printStackTrace()
        }

        return jsonData.toRequestBody()
    }

}