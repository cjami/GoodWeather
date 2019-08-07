package che.codes.goodweather.features.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import che.codes.goodweather.core.di.CoreComponent
import che.codes.goodweather.core.ui.BaseFragment
import che.codes.goodweather.domain.SETTINGS_TEMP_UNIT
import che.codes.goodweather.domain.models.TempUnit
import che.codes.goodweather.features.settings.SettingsViewModel.Result
import che.codes.goodweather.features.settings.di.DaggerSettingsComponent
import com.jakewharton.rxbinding3.widget.checkedChanges
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

class SettingsFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: SettingsViewModelFactory

    private lateinit var viewModel: SettingsViewModel

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerSettingsComponent.builder()
            .coreComponent(CoreComponent.getInstance(activity!!.applicationContext))
            .build().inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SettingsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.result.observe(this, Observer<Result> { handleResult(it) })
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
        setActionBarTitle(R.string.title_settings)
    }

    private fun handleResult(result: Result) {
        when (result) {
            is Result.Success -> {
                when (result.settings[SETTINGS_TEMP_UNIT] as TempUnit) {
                    TempUnit.Fahrenheit -> switch_setting_temp.isChecked = true
                    TempUnit.Celsius -> switch_setting_temp.isChecked = false
                }
                disposables.add(switch_setting_temp.checkedChanges().subscribe { handleSwitchCheck(it) })
            }
            is Result.Error -> {
                Toast.makeText(context, result.exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleSwitchCheck(checked: Boolean) {
        viewModel.set(SETTINGS_TEMP_UNIT, checked)
    }
}