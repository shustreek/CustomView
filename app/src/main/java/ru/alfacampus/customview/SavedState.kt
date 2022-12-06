package ru.alfacampus.customview

import android.os.Parcel
import android.os.Parcelable
import android.view.View

class SavedState : View.BaseSavedState {

    constructor(source: Parcelable) : super(source)

    constructor(parcel: Parcel) : super(parcel) {
        counter = parcel.readInt()
    }

    var counter: Int = 0

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(counter)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SavedState> {
        override fun createFromParcel(parcel: Parcel): SavedState {
            return SavedState(parcel)
        }

        override fun newArray(size: Int): Array<SavedState?> {
            return arrayOfNulls(size)
        }
    }
}