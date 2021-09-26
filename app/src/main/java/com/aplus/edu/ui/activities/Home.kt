package com.aplus.edu.ui.activities
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.aplus.edu.R
import com.aplus.edu.constants.FragmentConstants
import com.aplus.edu.ui.fragments.*


class Home : BaseActivity() {
    private var doubleBackToExitPressedOnce = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility =View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }
        initViews()

    }

    fun initViews()
    {
        Log.e("test","ttt")

            setFragment(HomeFragment(), FragmentConstants.HOME_FRAGMENT, null, false)

    }


    private fun executeAppDoubleTapExit() {
        doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_LONG).show()
        Handler().postDelayed({ doubleBackToExitPressedOnce = false }, 2000)
    }

    override fun onBackPressed() {
        val imm: InputMethodManager = getSystemService(
            INPUT_METHOD_SERVICE
        ) as InputMethodManager
        var view: View? = currentFocus
        if (view == null) {
            view = View(this)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)

        var fragment: Fragment? = supportFragmentManager.findFragmentById(R.id.container)
        if (fragment is ProfileFragment|| fragment is TermsFragment){
            setFragment(HomeFragment(), FragmentConstants.HOME_FRAGMENT, null, false)
        }
        else if (fragment is HomeFragment){
            if (doubleBackToExitPressedOnce) {
                finish()
            } else {
                executeAppDoubleTapExit()
            }
        }else if (supportFragmentManager.backStackEntryCount > 0){
            supportFragmentManager.popBackStack()
        }

    }

    //navigations
    fun onChapterdetlistNav(bundle: Bundle){
        setFragment(
            ChapterDetailsFragment(),
            FragmentConstants.CHAPTER_DETAIL_FRAGMENT, bundle, true
        )

    }
    fun onChapterdetOverviewlistNav(bundle: Bundle){
        setFragment(
            ChapterDetailsOverViewFragment(),
            FragmentConstants.CHAPTER_DETAIL_OVERVIEW_FRAGMENT, bundle, true
        )

    }
//
    fun onProfileNav(bundle: Bundle){
        setFragment(
            ProfileFragment(),
            FragmentConstants.PROFILE_FRAGMENT, bundle, true
        )

    }

    fun ontermsNav(bundle: Bundle){
        setFragment(
            TermsFragment(),
            FragmentConstants.TERMS_FRAGMENT, bundle, true
        )

    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
    }
}