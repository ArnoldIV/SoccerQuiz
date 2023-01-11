package com.example.soccerquiz.ui.fragment.game.webview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController

import com.example.soccerquiz.R
import com.example.soccerquiz.WebViewViewModel
import com.example.soccerquiz.databinding.FragmentWebviewBinding
import com.example.soccerquiz.util.Constants.Companion.GOOGLE_URL


class WebViewFragment : Fragment(R.layout.fragment_webview) {
    private lateinit var binding: FragmentWebviewBinding



    private lateinit var webViewViewModel: WebViewViewModel

    private var lastUrl = GOOGLE_URL
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        webViewViewModel = ViewModelProvider(requireActivity())[WebViewViewModel::class.java]

        checkForInternet(requireContext())

        if (!checkForInternet(requireContext())) {
            findNavController().navigate(R.id.action_webViewFragment_to_welcomeScreenFragment)
        }
    }


    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentWebviewBinding.bind(view)

        if (savedInstanceState != null) {
            binding.webView.restoreState(savedInstanceState)
        }

        webViewViewModel = ViewModelProvider(requireActivity())[WebViewViewModel::class.java]


        webViewViewModel.readLastUrl.asLiveData().observe(viewLifecycleOwner) { value ->
            lastUrl = value.firstUrl
            loadPreviousURL(value.firstUrl)

            Log.d("readlasturl", lastUrl)
        }



        binding.webView.apply {

            settings.javaScriptEnabled = true
            settings.builtInZoomControls = true
            settings.useWideViewPort = true
            settings.loadWithOverviewMode = true
            settings.domStorageEnabled = true


            url?.let { loadUrl(it) }

            Log.d("urlforloading", lastUrl)


            webViewClient = object : WebViewClient() {

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                }


                override fun onPageFinished(view: WebView?, url: String?) {
                    if (url != null) {
                        webViewViewModel.saveLastUrl(url)
                    }
                    webViewViewModel.applyQueries()
                    if (url != null) {
                        Log.d("lastUrlsaved", url)
                    }
                }

                override fun onReceivedError(
                    view: WebView?, request: WebResourceRequest?, error: WebResourceError?
                ) {
                    val errorMessage = "Got error! $error"
                    val errorDescription = error?.description.toString()
                    val errorCode = error?.errorCode
                    Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                    Log.d("got error", errorDescription)
                    Log.d("got error", errorCode.toString())

                    super.onReceivedError(view, request, error)
                }
            }



            activity?.onBackPressedDispatcher?.addCallback(
                viewLifecycleOwner,
                object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        if (binding.webView.canGoBack()) {
                            binding.webView.goBack()
                        } else {
                            super.setEnabled(false)
                        }

                    }
                })

        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.webView.saveState(outState)
    }

    override fun onResume() {
        super.onResume()
        binding.webView.resumeTimers()
    }

    override fun onPause() {
        super.onPause()
        binding.webView.pauseTimers()
    }

    private fun loadPreviousURL(firstUrl: String) {
        if (firstUrl != null) {
            try {
                binding.webView.loadUrl(lastUrl)
            } catch (e: Exception) {
                Log.d("loadUrlError", e.message.toString())
            }
        }

    }

    fun checkForInternet(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork ?: return false

        val activeNetwork =
            connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            else -> false
        }
    }
}
