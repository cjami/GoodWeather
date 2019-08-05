package che.codes.goodweather.features.locationlist

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import che.codes.goodweather.core.ui.BaseFragment
import che.codes.goodweather.core.di.CoreComponent
import che.codes.goodweather.domain.models.City
import che.codes.goodweather.features.locationlist.LocationListViewModel.Result
import che.codes.goodweather.features.locationlist.di.DaggerLocationListComponent
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_location_list.*
import javax.inject.Inject

class LocationListFragment : BaseFragment() {

    private lateinit var locationListAdapter: LocationListAdapter

    @Inject
    lateinit var viewModelFactory: LocationListViewModelFactory

    private lateinit var viewModel: LocationListViewModel

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DaggerLocationListComponent.builder()
            .coreComponent(CoreComponent.getInstance(activity!!.applicationContext))
            .build().inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LocationListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location_list, container, false)
    }

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationListAdapter = LocationListAdapter()
        disposables.add(locationListAdapter.clickEvent.subscribe { handleLocationItemClick(it) })

        list_locations.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = locationListAdapter
        }

        button_add.clicks().subscribe { handleAddClick() }

        viewModel.result.observe(this, Observer<Result> { handleResult(it) })
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
        setActionBarTitle(R.string.title_location_list)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposables.clear()
    }

    private fun handleResult(result: Result) {
        when (result) {
            is Result.Success -> {
                locationListAdapter.setData(result.cities)
            }
            is Result.Error -> {
                Toast.makeText(context, result.exception.localizedMessage, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun handleLocationItemClick(city: City) {
        findNavController().navigate(R.id.action_view_location)
    }

    private fun handleAddClick() {
        findNavController().navigate(R.id.action_add_location)
    }
}