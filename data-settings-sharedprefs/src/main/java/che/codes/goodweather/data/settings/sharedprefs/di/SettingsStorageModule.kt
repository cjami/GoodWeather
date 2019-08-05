package che.codes.goodweather.data.settings.sharedprefs.di

import android.content.Context
import che.codes.goodweather.data.settings.SettingsStorage
import che.codes.goodweather.data.settings.sharedprefs.SharedPrefsSettingsStorage
import dagger.Module
import dagger.Provides

@Module
class SettingsStorageModule {

    @Provides
    fun provideSettingsStorage(appContext: Context): SettingsStorage {
        return SharedPrefsSettingsStorage(appContext)
    }
}