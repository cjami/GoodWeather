package che.codes.androidtesting

import androidx.lifecycle.ViewModel

object ViewModelUtils {

    fun invokeOnCleared(viewModel: ViewModel) {
        val c = ViewModel::class.java
        val onClearedMethod = c.getDeclaredMethod("onCleared")
        onClearedMethod.isAccessible = true
        onClearedMethod.invoke(viewModel)
    }
}