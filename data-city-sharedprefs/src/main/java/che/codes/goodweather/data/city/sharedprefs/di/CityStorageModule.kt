package che.codes.goodweather.data.city.sharedprefs.di

import android.content.Context
import che.codes.goodweather.data.city.CityStorage
import che.codes.goodweather.data.city.sharedprefs.SharedPrefsCityStorage
import dagger.Module
import dagger.Provides

@Module
class CityStorageModule {

    @Provides
    fun provideCityStorage(appContext: Context): CityStorage {
        return SharedPrefsCityStorage(appContext)
    }
}