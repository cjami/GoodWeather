package che.codes.testing

import com.google.gson.Gson

object FileUtils {
    fun <T : Any> getListFromFile(filename: String, type: Class<Array<T>>): List<T> {
        return Gson().fromJson(getTextFromFile(filename), type).toList()
    }

    fun <T : Any> getObjectFromFile(filename: String, type: Class<T>): T {
        return Gson().fromJson(getTextFromFile(filename), type)
    }

    private fun getTextFromFile(filename: String): String {
        return this::class.java.getResource("/$filename")!!.readText()
    }
}