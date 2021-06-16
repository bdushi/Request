package com.example.twitter.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.twitter.BuildConfig
import com.example.twitter.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 *
 * https://developer.twitter.com/en/docs/authentication/guides/log-in-with-twitter
 *
 */

class TwitterLogInBottomSheet : BottomSheetDialogFragment() {
    private lateinit var url: String

    class Builder {
        private var url: String? = null
        fun setFirebaseUser(url: String?): Builder {
            this.url = url
            return this
        }

        fun build(): TwitterLogInBottomSheet {
            return newInstance(url)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(url: String?) =
            TwitterLogInBottomSheet().apply {
                arguments = Bundle().apply {
                    putString(URL, url)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_dialog_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView: WebView = view.findViewById(R.id.twitter_log_in)
        webView.isVerticalScrollBarEnabled = false
        webView.isHorizontalScrollBarEnabled = false
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                if (request?.url.toString().startsWith(BuildConfig.callBackUtl)) {
                    Log.d("Authorization URL: ", request?.url.toString())
                    handleUrl(request?.url.toString())

                    // Close the dialog after getting the oauth_verifier
                    if (request?.url.toString().contains(BuildConfig.callBackUtl)) {
                        dismiss()
                    }
                    return true
                }
                return false
            }
        }
        webView.settings.javaScriptEnabled = true
        webView.loadUrl(url)
    }

    // Get the oauth_verifier
    private fun handleUrl(url: String) {
//        val uri = Uri.parse(url)
//        val oauthVerifier = uri.getQueryParameter("oauth_verifier") ?: ""
//        GlobalScope.launch(Dispatchers.Main) {
//            accToken =
//                withContext(Dispatchers.IO) { twitter.getOAuthAccessToken(oauthVerifier) }
//            getUserProfile()
//        }
    }
}

const val URL = "URL"