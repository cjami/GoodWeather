package che.codes.weather.openweathermap.di

import che.codes.goodweather.data.weather.WeatherDataSource
import che.codes.weather.openweathermap.OwmApiService
import che.codes.weather.openweathermap.OwmWeatherDataSource
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named

@Module
class WeatherDataSourceModule {

    @Provides
    fun provideWeatherDataSource(
        okHttpClient: OkHttpClient,
        @Named("openweathermap.id") appId: String,
        @Named("openweathermap.url") baseUrl: String
    ): WeatherDataSource {
        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return OwmWeatherDataSource(retrofit.create(OwmApiService::class.java), appId)
    }
}