package che.codes.goodweather.features.addlocation

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import che.codes.goodweather.core.di.CoreComponent
import che.codes.goodweather.core.ui.BaseFragment
import che.codes.goodweather.domain.models.City
import che.codes.goodweather.features.addlocation.AddLocationViewModel.Result
import che.codes.goodweather.features.addlocation.di.DaggerAddLocationComponent
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_add_location.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

private const val TEXT_CHANGE_DEBOUNCE = 500L

class AddLocationFragment : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: AddLocationViewModelFactory

    private lateinit var viewModel: AddLocationViewModel

    private val disposables = CompositeDisposable()

    private var currentCity: City? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerAddLocationComponent.builder()
            .coreComponent(CoreComponent.getInstance(activity!!.applicationContext))
            .build().inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddLocationViewModel::class.java)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_location, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disposables.add(edit_city_name.textChanges()
            .debounce(TEXT_CHANGE_DEBOUNCE, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { text -> handleTextChange(text.toString()) })

        viewModel.result.observe(this, Observer<Result> { handleResult(it) })
    }

    override fun onResume() {
        super.onResume()
        setActionBarTitle(R.string.title_add_location)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        inflater.inflate(R.menu.add_location, menu)

        menu.findItem(R.id.action_add_location).isVisible = currentCity != null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_add_location) {
            currentCity?.let {
                viewModel.add(it)
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        disposables.clear()
        super.onDestroyView()
    }

    private fun handleTextChange(text: String) {
        viewModel.geocode(text)
    }

    private fun handleResult(result: Result) {
        when (result) {
            is Result.Success -> {
                currentCity = result.city
                text_city_country.text = result.city.country.longName
            }
            is Result.Error, Result.Processing -> {
                currentCity = null
                text_city_country.text = null
            }
        }

        activity?.invalidateOptionsMenu()
    }
}