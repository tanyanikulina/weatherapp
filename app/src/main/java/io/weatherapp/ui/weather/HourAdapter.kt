package io.weatherapp.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.weatherapp.R
import io.weatherapp.data.HourlyWeather
import io.weatherapp.utils.DateUtils
import io.weatherapp.utils.DateUtils.DF_HOUR
import io.weatherapp.utils.ImageUtils

class HourAdapter() : RecyclerView.Adapter<HourAdapter.HourViewHolder>() {

    val hours = mutableListOf<HourlyWeather>()

    fun setNewData(newHours: List<HourlyWeather>) {
        hours.clear()
        hours.addAll(newHours)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return hours.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourViewHolder {
        return HourViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_hour, parent, false)
        )
    }

    override fun onBindViewHolder(holder: HourViewHolder, position: Int) {
        val hour = hours[position]

        holder.tvHour.text = DateUtils.formatDateFromMillis((hour.date ?: 0L) * 1000L, DF_HOUR)
        holder.tvTemp.text = hour.temp.toString() + "°"
        holder.ivWeather.setImageDrawable(
            holder.itemView.context.getDrawable(
                ImageUtils.getWeatherRes(hour.weather?.get(0)?.main ?: "")
            )
        )

    }

    class HourViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHour: TextView = itemView.findViewById(R.id.tvHour) as TextView
        val tvTemp: TextView = itemView.findViewById(R.id.tvTemp) as TextView
        val ivWeather: ImageView = itemView.findViewById(R.id.ivWeather) as ImageView
    }

}