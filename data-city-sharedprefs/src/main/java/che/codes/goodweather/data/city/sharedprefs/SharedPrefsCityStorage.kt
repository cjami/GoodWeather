package che.codes.goodweather.data.city.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import che.codes.goodweather.data.city.CityStorage
import che.codes.goodweather.domain.models.City
import com.google.gson.Gson
import io.reactivex.Single

const val SHARED_PREFS_KEY = "CITIES"
const val CITIES_KEY = "CITIES"

class SharedPrefsCityStorage(context: Context) : CityStorage {

    private val sharedPrefs: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }

    private val gson: Gson by lazy { Gson() }

    override fun store(city: City) {
        with(sharedPrefs.edit()) {
            val cityList = loadAll().toMutableList()

            if (!cityList.contains(city)) {
                cityList.add(city)
            }

            putString(CITIES_KEY, gson.toJson(cityList))
            apply()
        }
    }

    override fun retrieve(): Single<List<City>> {
        return Single.just(loadAll())
    }

    private fun loadAll(): List<City> {
        return gson.fromJson(sharedPrefs.getString(CITIES_KEY, "[]"), Array<City>::class.java).toList()
    }
}