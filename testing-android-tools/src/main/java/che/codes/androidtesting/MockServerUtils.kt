package che.codes.androidtesting

import androidx.test.InstrumentationRegistry
import okhttp3.mockwebserver.MockResponse

object MockServerUtils {

    fun createJsonResponse(code: Int, body: String = "{}"): MockResponse {
        return MockResponse().addHeader("Content-Type", "application/json; charset=utf-8")
            .addHeader("Cache-Control", "no-cache")
            .setBody(body)
            .setResponseCode(code)
    }
}