# GoodWeather
 
GoodWeather is an Android app that displays a four day weather forecast for a given city. It makes use of [OpenWeatherMap](https://openweathermap.org/api) and [GeocoderAPI](https://developers.google.com/maps/documentation/geocoding/start).

| Location List | Add Location | View Location | Settings
| --- | --- | --- | --- |
|![Location List](/screenshots/screenshot_1.png)|![Add Location](/screenshots/screenshot_2.png)|![View Location](/screenshots/screenshot_3.png)|![Settings](/screenshots/screenshot_4.png)

### Setup:
The app requires **GeocoderAPI**/**OpenWeatherMap** API keys. To do this add a `keys.properties` file to the root of the project.

The inside of this file should look like this (replacing placeholders with your keys):

```
# Geocoder API Key
geocoderapi.key = "YOUR_API_KEY"

# OpenWeatherMap App ID
openweathermap.id = "YOUR_APP_ID"
```

### Architecture:
The architecture of this app is based on **MVVM** (without data-binding) and **Clean Architecture**. Logic that would traditionally sit in View Models now exists in the form of Use Cases. It also has a dedicated Domain layer allowing easier swapping of Data and Presentation layers.

### Libraries:
* RxJava
* RxIdler
* RxBinding
* Dagger
* Retrofit
* Gson
* Picasso
* Mockito
* Espresso
* MockWebServer
* ViewModel + LiveData
* Material Design Components
* Navigation Components
