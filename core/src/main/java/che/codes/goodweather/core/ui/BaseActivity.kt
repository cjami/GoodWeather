package che.codes.goodweather.core.ui

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    abstract fun expandToolbar()
    abstract fun collapseToolbar()
}