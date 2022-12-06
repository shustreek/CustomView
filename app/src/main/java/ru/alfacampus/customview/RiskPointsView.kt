package ru.alfacampus.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt

private const val TAG = "CustomView"

private const val POINT_DEFAULT_RADIUS = 10f
private const val POINTS_DEFAULT_DISTANCE = 10f
private const val POINTS_DEFAULT_COUNT = 5
private const val PAINT_BRUSH_STROKE_WIDTH = 3f

class RiskPointsView
@JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.riskPointsViewStyle,
    defStyleRs: Int = R.style.RiskPointsViewStyle
) : View(context, attrs, defStyleAttr, defStyleRs) {

    enum class Shape {
        Circle,
        Rectangle
    }

    private var riskPointsCount = 0
    private var pointsCount = 0
    private var pointRadius = 0f
    private var pointsDistance = 0f
    private lateinit var pointsShape: Shape
    private var points: Array<Paint> = emptyArray()

    private val paintStrokeBrush: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = context.toDp(PAINT_BRUSH_STROKE_WIDTH)
//            color = Color.BLACK
        }
    }
    private val paintFilledBrush: Paint by lazy {
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            strokeWidth = context.toDp(PAINT_BRUSH_STROKE_WIDTH)
//            color = Color.BLACK
        }
    }

    init {
        attrs?.let { initAttrs(it, defStyleAttr, defStyleRs) }
        initPoints()
    }

    private fun initPoints() {
        points = Array(pointsCount) {
            if (it < riskPointsCount) {
                paintFilledBrush
            } else {
                paintStrokeBrush
            }
        }
    }

    private fun initAttrs(attrs: AttributeSet, defStyleAttr: Int, defStyleRs: Int) {
        context.theme
            .obtainStyledAttributes(attrs, R.styleable.RiskPointsView, defStyleAttr, defStyleRs)
            .apply {
                try {
                    pointsCount = getInteger(R.styleable.RiskPointsView_pointsCount, POINTS_DEFAULT_COUNT)
                    pointRadius = getDimension(R.styleable.RiskPointsView_pointsRadius, POINT_DEFAULT_RADIUS)
                    pointsDistance = getDimension(R.styleable.RiskPointsView_pointsDistance, POINTS_DEFAULT_DISTANCE)

                    paintFilledBrush.color = getColor(R.styleable.RiskPointsView_pointsFillColor, Color.BLACK)
                    paintStrokeBrush.color = getColor(R.styleable.RiskPointsView_pointsStrokeColor, Color.BLACK)

                    val shape = getInt(R.styleable.RiskPointsView_pointsShape, Shape.Circle.ordinal)
                    pointsShape = Shape.values()[shape]
                } finally {
                    recycle()
                }
            }
    }

    fun setCount(count: Int) {
        pointsCount = count
        initPoints()
        requestLayout()
        invalidate()
    }

    fun setRiskCount(count: Int) {
        riskPointsCount = count.coerceIn(0, pointsCount)
        initPoints()
        invalidate()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Log.d(TAG, "onAttachedToWindow")
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        Log.d(TAG, "onMeasure")
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        var wSize = MeasureSpec.getSize(widthMeasureSpec)
        var hSize = MeasureSpec.getSize(heightMeasureSpec)
        /*when (mode) {
            MeasureSpec.EXACTLY -> {
                wSize = 200
                hSize = 200
            }
            MeasureSpec.AT_MOST -> {
                wSize = 400
                hSize = 400
            }
        }
        setMeasuredDimension(wSize, hSize)*/
        val desiredWidth = pointsCount * (pointRadius * 2 + pointsDistance) - pointsDistance
        val desiredHeight = pointRadius * 2
        setMeasuredDimension(
            resolveSize(desiredWidth.toInt(), widthMeasureSpec) + paddingLeft + paddingRight,
            resolveSize(desiredHeight.toInt(), heightMeasureSpec) + paddingTop + paddingBottom
        )
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG, "onLayout")
    }

    override fun onDraw(canvas: Canvas?) {
        Log.d(TAG, "onDraw")
        super.onDraw(canvas)
        canvas ?: return
        canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
        when (pointsShape) {
            Shape.Circle -> {
                points.forEach { paint ->
                    canvas.drawCircle(
                        /*cx*/ pointRadius,
                        /*cy*/ pointRadius,
                        /*radius*/ pointRadius - paint.strokeWidth / 2,
                        paint
                    )
                    canvas.translate(pointRadius * 2 + pointsDistance, 0f)
                }
            }
            Shape.Rectangle -> points.forEach { paint ->
                canvas.drawRect(
                    0f,
                    0f,
                    pointRadius * 2,
                    pointRadius * 2,
                    paint
                )
                canvas.translate(pointRadius * 2 + pointsDistance, 0f)
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Log.d(TAG, "onDetachedFromWindow")
    }

    fun Context.toDp(value: Float): Float {
        return resources.displayMetrics.density * value
    }
}