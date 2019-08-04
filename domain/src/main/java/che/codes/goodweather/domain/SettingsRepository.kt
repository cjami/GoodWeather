package che.codes.goodweather.domain

import io.reactivex.Single

interface SettingsRepository {

    fun put(key: String, value: Boolean)

    fun getBoolean(key: String): Single<Boolean>
}