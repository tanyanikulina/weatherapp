package io.weatherapp.models

import android.os.Parcel
import android.os.Parcelable

class CityModel(val name: String?, val lat: String?, val lon: String?) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(lat)
        parcel.writeString(lon)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CityModel> {
        override fun createFromParcel(parcel: Parcel): CityModel {
            return CityModel(parcel)
        }

        override fun newArray(size: Int): Array<CityModel?> {
            return arrayOfNulls(size)
        }
    }


}