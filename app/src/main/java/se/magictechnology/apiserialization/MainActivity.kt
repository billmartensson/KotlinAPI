package se.magictechnology.apiserialization

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

@Serializable
class WeatherData()
{
    var type : String? = null
    var properties : WeatherProperties? = null

}
@Serializable
class WeatherProperties()
{
    var timeseries : List<WeatherTimeseries>? = null

}
@Serializable
class WeatherTimeseries()
{
    var time : String? = null
    var data : WeatherTimeseriesData? = null

}
@Serializable
class WeatherTimeseriesData()
{
    var instant : WeatherTimeseriesDataInstant? = null

}
@Serializable
class WeatherTimeseriesDataInstant()
{
    var details : WeatherTimeseriesDataInstantDetails? = null

}
@Serializable
class WeatherTimeseriesDataInstantDetails()
{
    var air_temperature : Double? = null
    var wind_speed : Double? = null
}



class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        GlobalScope.launch(Dispatchers.IO) {

            val apiURL = URL("https://api.met.no/weatherapi/locationforecast/2.0/compact?lat=55.6112171&lon=12.9933074")

            val httpRequest = apiURL.openConnection() as HttpURLConnection
            httpRequest.setRequestProperty("User-Agent", "MYapp")

            httpRequest.inputStream.bufferedReader(Charsets.UTF_8).use {
                val apiResponse = it.readText()
                Log.i("BILLDEBUG", apiResponse)


                val weaterObject = Json {
                    ignoreUnknownKeys = true
                }.decodeFromString<WeatherData>(apiResponse)

                Log.i("BILLDEBUG", weaterObject.type!!)

                GlobalScope.launch(Dispatchers.Main) {
                    theTextview.text = weaterObject.type!!
                }

                for (timeser in weaterObject.properties!!.timeseries!!)
                {
                    Log.i("BILLDEBUG", timeser.time!!)
                    Log.i("BILLDEBUG", timeser.data!!.instant!!.details!!.air_temperature!!.toString())
                    Log.i("BILLDEBUG", timeser.data!!.instant!!.details!!.wind_speed!!.toString())
                }

            }


        }





    }
}