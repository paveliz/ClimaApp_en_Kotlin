package com.pabloangelveliz.ejemplos.kotlin.climaapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.net.URL

class MainActivity : AppCompatActivity() {

    //region ### LAMBDA EXPRESSION : onItemClickListener ###
    /* Manejo de Nulos

    En la siguiente sintáxis:

    private var mAdapter : ArrayAdapter<String>? = null

    El signo ? antes del igual, está avisando que la variable admite nulos.

    Cuando me encuentro con temas como el siguiente:

    supportActionBar!!.setDisplayShowTitleEnabled(false)

    Los dos signos de admiración juntos !! antes del punto de la propiedad o método del objeto
    están avisando que NO se adminite nulos. Esto SOLO debemos usarlo si sabemos que el valor
    a ser asignado a la variable o método NUNCA será nulo, de lo contrario vamos a caer en el
    mismo error de Java y tendremos el famoso NullPointerException.

    Ver más detalles sobre este punto en el artículo:

    http://paveliz.blogspot.com.ar/2017/06/android-comenzando-con-koltin-classes-safety-null.html
    */
    //endregion

    private var mAdapter : ArrayAdapter<String>? = null
    private var mCityArray : ArrayList<String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        val mListView = findViewById(R.id.listViewCities) as ListView
        val txtTemp = findViewById(R.id.textViewTemperatura) as TextView
        val txtDesc = findViewById(R.id.textViewDescription) as TextView
        val imgWeahter = findViewById(R.id.imageViewWeather) as ImageView
        val txtCityName = findViewById(R.id.textViewCityName) as TextView

        //region ### INICIALIZACION del CLIMA ###

        GetWeatherTask(this, txtTemp, txtDesc, imgWeahter)
                .execute(resources.getString(R.string.JsonURL)
                        .replace("#CITY#", resources.getString(R.string.ciudadInicial), false))

        txtCityName.text = resources.getString(R.string.ciudadInicial)

        //endregion

        val iarr : Array<String> = resources.getStringArray(R.array.cities_array)

        mCityArray = ArrayList<String>(iarr.asList())

        mAdapter = ArrayAdapter<String>(this,
                                R.layout.list_row,
                                R.id.lblCityName,
                                mCityArray)
        mListView.adapter = mAdapter

        //region ### LAMBDA EXPRESSION : onItemClickListener ###
        /*
        VERSION "onItemClickListener" NO lambda

        Esta es la clásica implementación de un onItemClickListener en Android con Java, dónde
        hacemos uso de una "inner class" a los fines de simplificar código.

        mListView.setOnItemClickListener(new OnItemClickListener() {
			  @Override
			  public void onItemClick(AdapterView<?> parent, View view,
			    int position, long id) {

				Log.d("onItemClick", "Se hizo clic")

			  }
			});

        VERSION "onItemClickListener" lambda expression

        Kotlin incorpora Lambda expressions, lo que permite simplificar aún más implementaciones
        de este tipo.

        mListView.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            Log.d("onItemClick", "Se hizo clic")
        }

        view y id no se usan, entonces pueden reemplazarse por un underscore "_"

        mListView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->
            Log.d("onItemClick", "Se hizo clic")
        }

        */
        //endregion

        mListView.onItemClickListener = AdapterView.OnItemClickListener { parent, _, position, _ ->

            val sCityName = parent.getItemAtPosition(position) as String

            txtCityName.text = sCityName

            GetWeatherTask(parent.context, txtTemp, txtDesc, imgWeahter).execute(resources.getString(R.string.JsonURL).replace("#CITY#", sCityName, false))
        }


    }

    //region ### METODOS vs FUN y OVERRIDE ###
    /*

    Lo que teníamos como "métodos" en Java para darle cierta funcionalidad a nuestras clases,
    en Kotlin se llaman Functions (Funciones) y se precede por la palabra reservada "fun":

    fun [nombre de la función]([variable]: [tipo de dato]): [tipo de dato de retorno] {

    Ejemplo:
            kelvinConversion(kelvin_temperature : Double): String {

    En esta función llamada "kelvinConversion", la misma recibe un tipo Double en una variable
    que se llamará "kelvin_temperature" y se retorna un String.

    De no necesitar retornar nada, no es necesario usar "void", sino simplemente obviamos el
    tipo de dato de retorno.

    Ejemplo:
            fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

    Ver: http://paveliz.blogspot.com.ar/2017/06/android-comenzando-con-koltin-classes-safety-null.html
    */
    //endregion

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val menu_id = item.itemId

        when (menu_id){

            R.id.menuCodigoMainActivity -> {
                val i = Intent(this, Tutorial::class.java)
                i.putExtra("codeFile","MainActivity.html")
                startActivity(i)
            }

            R.id.menuCodigoAgregarCiudad -> {
                val i = Intent(this, Tutorial::class.java)
                i.putExtra("codeFile","AgregarCiudad.html")
                startActivity(i)
            }

            R.id.menuCodigoTutorial -> {
                val i = Intent(this, Tutorial::class.java)
                i.putExtra("codeFile","Tutorial.html")
                startActivity(i)
            }

            R.id.menuCodigoAbout -> {
                val i = Intent(this, Tutorial::class.java)
                i.putExtra("codeFile","Acerca.html")
                startActivity(i)
            }

            R.id.menuAbout -> {
                val i = Intent(this, Acerca::class.java)
                startActivity(i)
            }

            R.id.menuAgregarCiudad -> {
                val i = Intent(this, AgregarCiudad::class.java)
                startActivityForResult(i, 100)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)  {

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {

            if(data != null) {

                mCityArray?.add(data.getStringExtra("nombreCiudad"))
            }

            mAdapter!!.notifyDataSetChanged()
        }
    }

    class GetWeatherTask(var context : Context, var tvTemperature: TextView,
                         var tvDescription: TextView,
                         var imgWeather: ImageView) : AsyncTask<String, Unit, String>() {

        override fun doInBackground(vararg params: String?): String? {

            val inputStream : InputStream
            var result = ""

            try {
                inputStream = URL(params[0]).openStream()

                if(inputStream != null) {
                    val buffer = BufferedReader (InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    buffer.forEachLine { stringBuilder.append(it) }

                    result = stringBuilder.toString()
                }

            } catch (e : Exception) {
                Log.d("InputStream", e.localizedMessage)
            }

            return result
        }

        override fun onPostExecute(result: String?) {

            super.onPostExecute(result)

            val json = JSONObject(result)

            val json_main = json.getJSONObject("main")
            val json_array_weather = json.getJSONArray("weather")

            val temp_kelvin = json_main.getDouble("temp")
            val pressure = json_main.getLong("pressure")
            val humidity = json_main.getLong("humidity")

            var temp_description = ""
            var temp_icon = ""

            for (i in 0 .. json_array_weather.length()-1) {

                val json_weather = json_array_weather.getJSONObject(i)

                temp_description = json_weather.getString("description")
                temp_icon = json_weather.getString("icon")
            }

            tvTemperature.text = kelvinConversion(temp_kelvin)
            tvDescription.text = temp_description
            imgWeather.setImageResource( context.resources.getIdentifier("weather_"+temp_icon, "drawable", context.packageName) )
                                                                                                                                                        }

        fun kelvinConversion(kelvin_temperature : Double): String {

            val temperaturaKFloat = "%.2f".format((kelvin_temperature.toFloat()) - 273.15)

            return temperaturaKFloat
        }
    }
}
