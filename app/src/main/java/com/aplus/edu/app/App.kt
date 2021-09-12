package com.aplus.edu.app
import android.app.Application
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import com.aplus.edu.constants.SharedPrefereceConstants.Companion.PREF_NAME
import com.aplus.edu.log.AppLogger
import com.aplus.edu.preference.Preference
import org.jetbrains.anko.getStackTraceString


class App : Application() {


    companion object {
        lateinit var mApp: App
    }

    override fun onCreate() {
        mApp = this
        super.onCreate()
        Preference.init(this, PREF_NAME)
        Thread.setDefaultUncaughtExceptionHandler { thread, e ->
            AppLogger.error(e.getStackTraceString())
        }
    }

    override fun onTerminate() {

        super.onTerminate()
    }
    fun getAppVersion(): String{
        val manager: PackageManager = mApp.packageManager
        val info: PackageInfo = manager.getPackageInfo(
            mApp.packageName, 0
        )
        return info.versionName
    }
}