package com.pabloangelveliz.ejemplos.kotlin.climaapp

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.EditText

class AgregarCiudad : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agregar_ciudad)

        val toolbar = findViewById(R.id.addcity_toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.title = resources.getString(R.string.AddCity_Toolbar_Title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

    }

    fun clicBotonAgregarCiudad(v : View) {

        val cityName = findViewById(R.id.editTextCityName) as EditText

        val i = Intent()

        i.putExtra("nombreCiudad", cityName.text.toString())

        if(cityName.text.isNotEmpty())
            setResult(Activity.RESULT_OK, i)
        else
            setResult(Activity.RESULT_CANCELED, i)
        finish()
    }

}
