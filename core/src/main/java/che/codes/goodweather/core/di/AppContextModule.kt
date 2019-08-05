package che.codes.goodweather.core.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppContextModule(private val appContext: Context) {

    @Provides
    fun provideContext(): Context {
        return appContext
    }
}