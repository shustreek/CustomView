package ru.alfacampus.customview

import android.content.Context
import android.os.Parcelable
import android.util.AttributeSet
import android.util.SparseArray
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ru.alfacampus.customview.databinding.CounterViewBinding

private const val MAX_VALUE = 99
private const val MIN_VALUE = 0

class CounterView
@JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRs: Int = 0) :
    LinearLayout(context, attrs, defStyleAttr, defStyleRs) {

    private var counter = 0
    private var binding: CounterViewBinding

    init {
        gravity = Gravity.CENTER
        binding = CounterViewBinding
            .inflate(LayoutInflater.from(context), this)

        binding.cvUpBtn.setOnClickListener { onUpBtnClick() }
        binding.cvDownBtn.setOnClickListener { onDownBtnClick() }
        setCounter(counter)
        isSaveEnabled = true
        isSaveFromParentEnabled = true
    }

    private fun onDownBtnClick() {
        setCounter(counter - 1)
    }

    private fun onUpBtnClick() {
        setCounter(counter + 1)
    }

    fun setCounter(value: Int) {
        counter = value.coerceIn(MIN_VALUE, MAX_VALUE)
        /*
        counter = when {
            value < MIN_VALUE -> MIN_VALUE
            value > MAX_VALUE -> MAX_VALUE
            else -> value
        }
        */
        binding.cvCounter.text = counter.toString()
    }

    fun getCounter(): Int {
        return counter
    }

    override fun onSaveInstanceState(): Parcelable {
        val state = super.onSaveInstanceState()

        return SavedState(
            counter,
            state
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        state as SavedState
        super.onRestoreInstanceState(state.superState)
        setCounter(state.counter)
    }

    @Parcelize
    class SavedState(
        val counter: Int,
        @IgnoredOnParcel val source: Parcelable? = null
    ) : BaseSavedState(source)
}