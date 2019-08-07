package che.codes.goodweather.core.ui

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    protected fun setActionBarTitle(resId: Int) {
        setActionBarTitle(getString(resId))
    }

    protected fun setActionBarTitle(text: String) {
        (activity as? AppCompatActivity)?.supportActionBar?.title = text
    }

    override fun onDetach() {
        super.onDetach()
        hideSoftwareKeyboard()
        collapseToolbar()
    }

    protected fun hideSoftwareKeyboard() {
        val imm = (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        imm.hideSoftInputFromWindow(activity?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    protected fun expandToolbar(){
        (activity as? BaseActivity)?.expandToolbar()
    }

    protected fun collapseToolbar(){
        (activity as? BaseActivity)?.collapseToolbar()
    }
}