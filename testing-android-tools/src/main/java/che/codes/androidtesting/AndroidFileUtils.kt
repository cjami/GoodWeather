package che.codes.androidtesting

import androidx.test.InstrumentationRegistry
import com.google.gson.Gson

object AndroidFileUtils {

    fun getJsonFromFile(filename: String): String {
        val inputStream = InstrumentationRegistry.getInstrumentation().context.assets.open(filename)
        return inputStream.bufferedReader().use { it.readText() }
    }

    fun <T : Any> getListFromFile(filename: String, type: Class<Array<T>>): List<T> {
        return Gson().fromJson(getJsonFromFile(filename), type).toList()
    }

    fun <T : Any> getObjectFromFile(filename: String, type: Class<T>): T {
        return Gson().fromJson(getJsonFromFile(filename), type)
    }
}