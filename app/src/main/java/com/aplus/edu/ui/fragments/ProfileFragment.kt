package com.aplus.edu.ui.fragments
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aplus.edu.R
import com.aplus.edu.constants.AppConstants
import com.aplus.edu.preference.Preference
import com.aplus.edu.preference.UserInfo
import com.aplus.edu.ui.activities.Home
import com.aplus.edu.ui.activities.LoginActivity
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_chapters_detailsq_list.back_imv
import kotlinx.android.synthetic.main.activity_profile_view.*

class ProfileFragment : BaseFragment() {

    private fun initiateViews() {

        back_imv.setOnClickListener {

            requireActivity().onBackPressed()

        }

        //menu clicks
        trms.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("testt", "tt")
            (activity as Home).ontermsNav(bundle)
        }
        logout.setOnClickListener {
            Preference.cleanPreferences()
            startActivity(Intent(activity, LoginActivity::class.java))
        }

        //set profile values
        profile_user_tv.text=UserInfo().studname

        profile_standard_tv.text="(studied on " +UserInfo().studclass + " th " + " standard)"

        profile_email_tv.text=UserInfo().emailid
        profile_mobileno_tv.text=UserInfo().mob

        Glide.with(requireActivity())
            .load(AppConstants.IMAGE_URL + UserInfo().photo)
            //.apply(RequestOptions.circleCropTransform())
            //.placeholder(R.drawable.ic_profile_placeholder)
            .into(profileve)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_profile_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initiateViews()
    }


}