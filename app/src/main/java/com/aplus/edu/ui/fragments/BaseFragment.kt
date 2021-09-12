package com.aplus.edu.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.aplus.edu.R


open class BaseFragment : Fragment() {

    protected fun moveToScreen(targetActivity: Class<*>?, bundle: Bundle?, isFinish: Boolean) {
        val intent = Intent()
        intent.setClass(requireActivity(), targetActivity!!)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        startActivity(intent)
        if (isFinish) {
            requireActivity().finish()
        }
    }

    fun moveToScreen(targetFragment: Fragment, fragmentTag: String?, bundle: Bundle?) {
        val transaction =
            requireActivity().supportFragmentManager.beginTransaction()
        if (bundle != null) {
            targetFragment.arguments = bundle
        }
        transaction.replace(R.id.container, targetFragment, fragmentTag)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}