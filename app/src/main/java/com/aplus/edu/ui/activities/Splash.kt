package com.aplus.edu.ui.activities

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.aplus.edu.BuildConfig
import com.aplus.edu.R
import com.aplus.edu.api.manager.APIManager
import com.aplus.edu.api.response.AppVersionMaster
import com.aplus.edu.api.services.TuitionApiService
import com.aplus.edu.preference.UserInfo
import kotlinx.coroutines.launch
import retrofit2.Response


class Splash : AppCompatActivity() {

    private val SPLASH_DISPLAY_LENGTH = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_splash)

        checkAppVersion()
    }

    private fun startOver(){
        Handler().postDelayed({
            ///chek prefs here
            if (UserInfo().uid.isNotEmpty()) {
                startActivity(Intent(this, Home::class.java).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP))
                this.finish()
            }
            else {
                startActivity(Intent(this, LoginActivity::class.java))
                this.finish()
            }
        }, SPLASH_DISPLAY_LENGTH.toLong())
    }

    override fun onResume() {
        super.onResume()
        checkAppVersion()
    }





    private fun getAppVersionName(): String{
        var versionName = "0";
        try {
            val pInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            versionName = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return  versionName;
    }
    private fun promptUpdate(newVersion: String, oldversion: String) {
        AlertDialog.Builder(this)
            .setCancelable(false)
            .setTitle("Update available!")
            .setMessage("You are using an out dated version(v$oldversion) of ${getString(R.string.app_name)}! An updated version(v$newVersion) available in Play Store.")
            .setPositiveButton("Update") { _, _ ->
                val appPackageName = packageName

                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")
                        )
                    )
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                        )
                    )
                }
            }
            .show()
    }

    private fun checkAppVersion(){
        lifecycleScope.launch {
            try {
                val response = APIManager.call<TuitionApiService, Response<AppVersionMaster>> {
                    getAppVersionFromServer()
                }
                if (response.isSuccessful && !response.body()?.data!!.isNullOrEmpty()){
                    val serverAppVersion = response.body()?.data!![0];
                    if (serverAppVersion.version_code.toInt() > BuildConfig.VERSION_CODE && serverAppVersion.version_name != getAppVersionName()){
                        promptUpdate(serverAppVersion.version_name, getAppVersionName())
                    }else{
                        startOver()
                    }
                }
            } catch (e: Exception) {
                startOver()
            }
        }
    }

}