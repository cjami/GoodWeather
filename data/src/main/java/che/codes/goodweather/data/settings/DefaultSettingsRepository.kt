package che.codes.goodweather.data.settings

import che.codes.goodweather.domain.SettingsRepository
import io.reactivex.Single

class DefaultSettingsRepository(private val storage: SettingsStorage) : SettingsRepository{

    override fun put(key: String, value: Boolean) {
        storage.store(key, value)
    }

    override fun getBoolean(key: String): Single<Boolean> {
        return storage.retrieveBoolean(key)
    }
}