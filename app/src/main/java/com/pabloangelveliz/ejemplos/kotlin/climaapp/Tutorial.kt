package com.pabloangelveliz.ejemplos.kotlin.climaapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.webkit.WebView.SCROLLBARS_OUTSIDE_OVERLAY
import android.webkit.WebViewClient
import java.io.BufferedReader
import java.io.InputStreamReader


class Tutorial : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)

        val nombreArchivo = intent.getStringExtra("codeFile")

        var webviewTutorial = findViewById(R.id.webviewTutorial) as WebView


        webviewTutorial.settings.setSupportZoom(false)
        webviewTutorial.settings.builtInZoomControls = false
        webviewTutorial.scrollBarStyle = SCROLLBARS_OUTSIDE_OVERLAY
        webviewTutorial.settings.loadsImagesAutomatically = true
        webviewTutorial.settings.javaScriptEnabled = true

        webviewTutorial.webViewClient = WebViewClient()

        webviewTutorial.loadDataWithBaseURL(null, loadHtml(nombreArchivo), "text/html", "utf-8",null)

    }

    fun loadHtml(archivo : String): String {

        var result = ""

        try {
            val buffer = BufferedReader (InputStreamReader(assets.open(archivo), "utf-8"))
            val stringBuilder = StringBuilder()
            buffer.forEachLine { stringBuilder.append(it) }

            result = stringBuilder.toString()

        } catch (e : Exception) {
            Log.d("InputStream", e.localizedMessage)
        }

        return result
    }
}
