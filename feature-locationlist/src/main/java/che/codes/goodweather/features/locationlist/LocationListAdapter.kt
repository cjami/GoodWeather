package che.codes.goodweather.features.locationlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import che.codes.goodweather.domain.models.City
import che.codes.goodweather.features.locationlist.LocationListAdapter.*
import com.jakewharton.rxbinding3.view.clicks
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.item_location.view.*

class LocationListAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var data = emptyList<City>()
    private val clickSubject = PublishSubject.create<City>()
    val clickEvent: Observable<City> = clickSubject

    inner class ViewHolder(
        private val view: View,
        private val clickObserver: Observer<City>
    ) : RecyclerView.ViewHolder(view) {

        fun bind(city: City) {
            view.text_location.text = view.context.getString(
                R.string.format_location,
                city.name,
                city.country.shortName
            )

            view.clicks().map { city }.subscribe(clickObserver)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.item_location, parent, false)
        return ViewHolder(layout, clickSubject)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        clickSubject.onComplete()
    }

    fun setData(data: List<City>) {
        this.data = data
        notifyDataSetChanged()
    }
}