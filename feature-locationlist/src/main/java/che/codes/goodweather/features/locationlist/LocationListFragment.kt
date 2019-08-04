package che.codes.goodweather.features.locationlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import che.codes.goodweather.domain.models.City
import che.codes.goodweather.features.locationlist.LocationListViewModel.*
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_location_list.*
import javax.inject.Inject

class LocationListFragment : Fragment() {

    private lateinit var locationListAdapter: LocationListAdapter

    @Inject
    lateinit var viewModelFactory: LocationListViewModelFactory

    private lateinit var viewModel: LocationListViewModel

    private val disposables = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LocationListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_location_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationListAdapter = LocationListAdapter()
        disposables.add(locationListAdapter.clickEvent.subscribe { handleLocationClick(it) })

        list_locations.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = locationListAdapter
        }

        viewModel.result.observe(this, Observer<Result> { handleResult(it) })
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

    private fun handleLocationClick(city: City) {
        //GOTO VIEW LOCATION
    }
}