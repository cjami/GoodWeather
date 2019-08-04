package che.codes.goodweather.data.settings.sharedprefs

import android.content.Context
import android.content.SharedPreferences
import che.codes.goodweather.data.settings.SettingsStorage
import io.reactivex.Single

const val SHARED_PREFS_KEY = "SETTINGS"

class SharedPrefsSettingsStorage(context: Context) : SettingsStorage {

    private val sharedPrefs: SharedPreferences by lazy {
        context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }

    override fun store(key: String, value: Boolean) {
        with(sharedPrefs.edit()) {
            putBoolean(key, value)
            commit()
        }
    }

    override fun retrieveBoolean(key: String): Single<Boolean> {
        return Single.just(sharedPrefs.getBoolean(key, false))
    }
}