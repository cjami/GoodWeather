package che.codes.goodweather.core.ui

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    protected fun setActionBarTitle(resId: Int) {
        setActionBarTitle(getString(resId))
    }

    protected fun setActionBarTitle(text: String) {
        (activity as AppCompatActivity).supportActionBar?.title = text
    }
}