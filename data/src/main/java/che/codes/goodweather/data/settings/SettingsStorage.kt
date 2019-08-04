package che.codes.goodweather.data.settings

import io.reactivex.Single

interface SettingsStorage {

    fun store(key: String, value: Boolean)

    fun retrieveBoolean(key: String) : Single<Boolean>
}