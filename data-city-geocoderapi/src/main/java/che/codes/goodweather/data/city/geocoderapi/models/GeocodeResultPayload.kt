package che.codes.goodweather.data.city.geocoderapi.models

import com.google.gson.annotations.SerializedName

data class GeocodeResultPayload(
    val results: List<Result>,
    val status: String
) {

    data class Result(
        @SerializedName("address_components")
        val addressComponents: List<AddressComponent>,
        @SerializedName("formatted_address")
        val formattedAddress: String,
        val geometry: Geometry,
        @SerializedName("place_id")
        val placeID: String,
        val types: List<String>
    ) {

        data class AddressComponent(
            @SerializedName("long_name")
            val longName: String,
            @SerializedName("short_name")
            val shortName: String,
            val types: List<String>
        )

        data class Geometry(
            val location: Location,
            @SerializedName("location_type")
            val locationType: String,
            val viewport: ViewPort
        ) {
            data class Location(val lat: Double, val lng: Double)
            data class ViewPort(val northeast: Location, val southwest: Location)
        }
    }
}