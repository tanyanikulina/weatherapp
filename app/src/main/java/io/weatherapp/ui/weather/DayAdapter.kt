package io.weatherapp.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import io.weatherapp.R
import io.weatherapp.data.DailyWeather
import io.weatherapp.utils.DateUtils
import io.weatherapp.utils.DateUtils.DF_DAY_OF_WEEK
import io.weatherapp.utils.ImageUtils

class DayAdapter() : RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    val days = mutableListOf<DailyWeather>()

    fun setNewData(newDays: List<DailyWeather>) {
        days.clear()
        days.addAll(newDays)
        notifyDataSetChanged()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return days.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayViewHolder {
        return DayViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_day, parent, false)
        )
    }


    override fun onBindViewHolder(holder: DayViewHolder, position: Int) {

        val day = days[position]

        holder.tvDay.text = DateUtils.formatDateFromMillis((day.date ?: 0L) * 1000L, DF_DAY_OF_WEEK)
        holder.tvTemp.text = day.temp?.day.toString() + "°/" + day.temp?.night.toString() + "°"
        holder.ivWeather.setImageDrawable(
            holder.itemView.context.getDrawable(
                ImageUtils.getWeatherRes(day.weather?.get(0)?.main ?: "")
            )
        )

//        holder.itemView.setOnClickListener {
//
//        }

    }

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDay: TextView = itemView.findViewById(R.id.tvDay) as TextView
        val tvTemp: TextView = itemView.findViewById(R.id.tvTemp) as TextView
        val ivWeather: ImageView = itemView.findViewById(R.id.ivWeather) as ImageView
    }

}