package ru.kazakovanet.synoptic.ui.auth

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.yahoo_api_authenticate_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import ru.kazakovanet.synoptic.R
import ru.kazakovanet.synoptic.data.network.api.AUTH_URL
import ru.kazakovanet.synoptic.data.network.api.CLIENT_ID
import ru.kazakovanet.synoptic.data.network.api.REDIRECT_URI
import ru.kazakovanet.synoptic.internal.AuthCodeNotFoundException
import ru.kazakovanet.synoptic.ui.base.ScopedFragment

class YahooAPIAuthenticateFragment : ScopedFragment(), KodeinAware {

    override val kodein by closestKodein()

    private val viewModelFactory: YahooAPIAuthenticateViewModelFactory by instance()

    private lateinit var viewModel: YahooAPIAuthenticateViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.yahoo_api_authenticate_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(YahooAPIAuthenticateViewModel::class.java)

        bindUI()
    }

    private fun bindUI() = launch {
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                webView: WebView,
                request: WebResourceRequest
            ): Boolean {
                val url = request.url

                if (!url.toString().startsWith(REDIRECT_URI)) {
                    webView.loadUrl(url.toString())
                    progressBar.visibility = View.VISIBLE
                    return true
                }

                val code = url.getQueryParameter("code") ?: throw AuthCodeNotFoundException()

                GlobalScope.launch(Dispatchers.IO) {
                    viewModel.changeCodeToAccessToken(code)

                    withContext(Dispatchers.Main) {
                        findNavController().popBackStack()
                    }
                }

                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.GONE
            }
        }

        progressBar.visibility = View.VISIBLE
        webView.loadUrl(getAuthUrl())
    }

    private fun getAuthUrl(): String {
        return "${AUTH_URL}request_auth" +
                "?client_id=$CLIENT_ID" +
                "&redirect_uri=$REDIRECT_URI" +
                "&response_type=code" +
                "&language=en-us"
    }
}