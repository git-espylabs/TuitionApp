package com.aplus.edu.ui.fragments
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.aplus.edu.R
import com.aplus.edu.ui.activities.Home
import kotlinx.android.synthetic.main.activity_chapters_detailsq_list.back_imv
import kotlinx.android.synthetic.main.fragment_terms.*


class TermsFragment : BaseFragment() {

    private fun initiateViews() {

        back_imv.setOnClickListener {
            //startActivity(Intent(activity, Home::class.java))
            requireActivity().onBackPressed();
        }

        webView.loadUrl("https://aplusmytutionapp.com/terms")
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        webView.isHorizontalScrollBarEnabled = false;
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_terms, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initiateViews()
    }

}