package com.aplus.edu.ui.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aplus.edu.R
import com.kaopiz.kprogresshud.KProgressHUD


open class BaseActivity : AppCompatActivity() {

    private fun setListeners() {

    }

    protected fun moveToScreen(targetActivity: Class<*>?, bundle: Bundle?, isFinish: Boolean) {
        val intent = Intent()
        intent.setClass(this, targetActivity!!)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
        if (isFinish) {
            finish()
        }
    }

    fun setFragment(targetFragment: Fragment, fragmentTag: String?, bundle: Bundle?, addToBackStack: Boolean) {
        val transaction =
            supportFragmentManager.beginTransaction()
        if (bundle != null) {
            targetFragment.arguments = bundle
        }
        transaction.replace(R.id.container, targetFragment, fragmentTag)
        if (addToBackStack) {
            transaction.addToBackStack(fragmentTag)
        } else {
            transaction.addToBackStack(null)
        }
        transaction.commit()
    }

    companion object{
        var hud: KProgressHUD? = null

        fun showProgress(context: Context?, message: String?) {
            hud = KProgressHUD.create(context)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel(message)
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show()
        }

        fun dismissProgress() {
            try {
                hud!!.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        setListeners()
    }

    /*override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.customToolbarBack -> {
                onBackPressed()
            }
            R.id.toolbarCustIcon -> {
            }
            R.id.rerset -> {
            }
        }
    }*/
}