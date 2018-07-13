package com.shaishavgandhi.sample

import android.os.Parcel
import android.os.Parcelable

class KotlinParcelable(
    val someProperty: String,
    val intProperty: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(someProperty)
        writeString(intProperty)
    }

    companion object {
        fun createFromParcel(parcel: Parcel): KotlinParcelable {
            return KotlinParcelable(parcel)
        }

        fun newArray(size: Int): Array<KotlinParcelable?> {
            return arrayOfNulls(size)
        }

        @JvmField
        val CREATOR: Parcelable.Creator<KotlinParcelable> =
            object : Parcelable.Creator<KotlinParcelable> {
                override fun createFromParcel(source: Parcel): KotlinParcelable =
                    KotlinParcelable(source)

                override fun newArray(size: Int): Array<KotlinParcelable?> = arrayOfNulls(size)
            }
    }
}