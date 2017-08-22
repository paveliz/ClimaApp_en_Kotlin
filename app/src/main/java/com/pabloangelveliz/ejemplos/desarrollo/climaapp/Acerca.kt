package com.pabloangelveliz.ejemplos.desarrollo.climaapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar

class Acerca : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_acerca)

        val toolbar = findViewById(R.id.about_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.About_Toolbar_Title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
