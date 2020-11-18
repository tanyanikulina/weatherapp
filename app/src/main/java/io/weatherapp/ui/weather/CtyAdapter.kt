package io.weatherapp.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import io.weatherapp.R
import io.weatherapp.models.CityModel

class CityAdapter(var cities: List<CityModel>) : BaseAdapter(), Filterable {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        var cv = convertView
        if (cv == null) {
            cv = LayoutInflater.from(parent!!.context).inflate(R.layout.item_city, parent, false)
        }

        (cv as TextView).text = cities[position].name

        return cv
    }

    override fun getItem(position: Int): CityModel {
        return cities[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return cities.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    val resList = cities.filter { it.name!!.contains(constraint, true) }
                    filterResults.values = resList
                    filterResults.count = resList.size
                }
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                if (results != null && results.count > 0) {
                    cities = results.values as List<CityModel>
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }
    }


}