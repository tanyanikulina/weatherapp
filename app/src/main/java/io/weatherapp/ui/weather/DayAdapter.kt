package io.weatherapp.ui.weather

import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import io.weatherapp.R
import io.weatherapp.models.DayModel
import io.weatherapp.utils.DF_DAY_OF_WEEK
import io.weatherapp.utils.DateUtils
import io.weatherapp.utils.ImageUtils

class DayAdapter(val onDayClick: (position: Int) -> Unit) :
    RecyclerView.Adapter<DayAdapter.DayViewHolder>() {

    val days = mutableListOf<DayModel>()

    fun setNewData(newDays: List<DayModel>) {
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

        val day = days[position].dayWeather
        val isSelected = days[position].isSelected

        holder.tvDay.text = DateUtils.formatDateFromMillis(day.receiveDateMillis(), DF_DAY_OF_WEEK)
        holder.tvTemp.text = day.receiveTemp()
        holder.ivWeather.setImageDrawable(
            holder.itemView.context.getDrawable(ImageUtils.getWeatherRes(day.receiveWeather()))
        )

        if (isSelected) {
            setViewColor(
                holder,
                holder.itemView.context.resources.getColor(R.color.blue_light),
                holder.itemView.context.resources.getColor(R.color.blue_secondary)
            )
        } else {
            setViewColor(
                holder,
                holder.itemView.context.resources.getColor(R.color.white),
                holder.itemView.context.resources.getColor(R.color.black)
            )
        }

        holder.itemView.setOnClickListener {
            onDayClick(position)
        }

    }

    private fun setViewColor(holder: DayViewHolder, viewColor: Int, textColor: Int) {
        holder.clMain.background.setTint(viewColor)
        holder.tvDay.setTextColor(textColor)
        holder.tvTemp.setTextColor(textColor)
        holder.ivWeather.setColorFilter(textColor, PorterDuff.Mode.SRC_IN);
    }

    class DayViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clMain: ConstraintLayout = itemView.findViewById(R.id.clMain) as ConstraintLayout
        val tvDay: TextView = itemView.findViewById(R.id.tvDay) as TextView
        val tvTemp: TextView = itemView.findViewById(R.id.tvTemp) as TextView
        val ivWeather: ImageView = itemView.findViewById(R.id.ivWeather) as ImageView
    }

}