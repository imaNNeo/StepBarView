package ir.neo.stepbarview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.os.Parcel
import android.os.Parcelable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.util.*

/**
 * Created by iman.
 * iman.neofight@gmail.com
 */
class StepBarView @JvmOverloads
constructor(mContext : Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(mContext,attrs, defStyleAttr) {

    private var stepsPaint : Paint
    private var stepsLinePaint : Paint
    private var stepsTextPaint : Paint

    private var tmpRect = Rect()

    var onStepChangeListener : OnStepChangeListener? = null

    private val rawHeiht
        get() = Math.max(stepsSize,stepsLineHeight)


    //Lines between steps will drawn based on this variable
    //Note that now width is match_parent
    private val linesWidth : Float
        get() {
            val allStepsSize = (maxCount * stepsSize)
            val allMarginsSize = ((maxCount-1) * (stepsLineMarginLeft + stepsLineMarginRight))
            val width = width - paddingLeft - paddingRight
            val available = (width - allStepsSize - allMarginsSize)
            return available/(maxCount-1)
        }

    //This property used in drawing stuff
    private val yPos
        get() = ((rawHeiht/2) + paddingTop)


    var maxCount : Int = 0
        set(value) {
            if(allowTouchStepTo == field)
                allowTouchStepTo = value
            field = value
            invalidate()
        }

    var reachedStep: Int = 0
        set(value) {
            field = value
            onStepChangeListener?.onStepChanged(field)
            invalidate()
        }

    var stepsReachedColor : Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var stepsUnreachedColor : Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var stepsLineReachedColor : Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var stepsLineUnreachedColor : Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var stepsLineHeight : Float = 0f
        set(value) {
            field = value
            requestLayout()
        }

    var stepsSize : Float = 0f
        set(value) {
            field = value
            requestLayout()
        }

    var stepsTextColor : Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var stepsTextSize : Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    var stepsLineMarginLeft : Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    var stepsLineMarginRight: Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    var allowTouchStepTo : Int = 0

    var showStepIndex: Boolean = true


    init {
        maxCount = 8
        reachedStep = 1

        stepsReachedColor = ContextCompat.getColor(context,R.color.sbv_step_reached_color)
        stepsUnreachedColor = ContextCompat.getColor(context,R.color.sbv_step_unreached_color)

        stepsLineReachedColor = ContextCompat.getColor(context,R.color.sbv_step_line_reached_color)
        stepsLineUnreachedColor = ContextCompat.getColor(context,R.color.sbv_step_line_unreached_color)

        stepsLineHeight = DpHandler.dpToPx(context,4).toFloat()

        stepsSize = DpHandler.dpToPx(context,16).toFloat()
        stepsTextColor = ContextCompat.getColor(context,R.color.sbv_step_text_color)
        stepsTextSize = DpHandler.spToPx(context,14f)

        stepsLineMarginLeft = DpHandler.dpToPx(context,2).toFloat()
        stepsLineMarginRight = DpHandler.dpToPx(context,2).toFloat()

        allowTouchStepTo = maxCount

        showStepIndex = true

        attrs.let {
            val a = mContext.obtainStyledAttributes(attrs, R.styleable.StepBarView)

            maxCount = a.getInt(R.styleable.StepBarView_sbv_max_count,maxCount)

            stepsReachedColor = a.getColor(R.styleable.StepBarView_sbv_steps_reached_colors,stepsReachedColor)
            stepsUnreachedColor = a.getColor(R.styleable.StepBarView_sbv_steps_unreached_colors,stepsUnreachedColor)

            stepsLineReachedColor = a.getColor(R.styleable.StepBarView_sbv_steps_line_reached_colors,stepsLineReachedColor)
            stepsLineUnreachedColor = a.getColor(R.styleable.StepBarView_sbv_steps_line_unreached_colors,stepsLineUnreachedColor)

            stepsLineHeight = a.getDimension(R.styleable.StepBarView_sbv_steps_line_height,stepsLineHeight)

            stepsSize = a.getDimension(R.styleable.StepBarView_sbv_steps_size,stepsSize)
            stepsTextColor = a.getColor(R.styleable.StepBarView_sbv_steps_text_color,stepsTextColor)
            stepsTextSize = a.getDimensionPixelOffset(R.styleable.StepBarView_sbv_steps_text_size, stepsTextSize.toInt()).toFloat()

            stepsLineMarginLeft = a.getDimension(R.styleable.StepBarView_sbv_steps_line_margin_left,stepsLineMarginLeft)
            stepsLineMarginRight = a.getDimension(R.styleable.StepBarView_sbv_steps_line_margin_right,stepsLineMarginRight)

            allowTouchStepTo = a.getInt(R.styleable.StepBarView_sbv_allow_touch_step_to,maxCount)

            showStepIndex = a.getBoolean(R.styleable.StepBarView_sbv_draw_step_index, showStepIndex)

            a.recycle()
        }

        stepsPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }

        stepsLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }

        stepsTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
        }
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec)

        val heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec)



        val mWidth = widthMeasureSize
        val mHeight = calculateDesireHeight()

        setMeasuredDimension(mWidth, mHeight.toInt())
    }


    private fun calculateDesireHeight() = rawHeiht + paddingTop + paddingBottom


    private fun getHorizontalCirclesPosition() : ArrayList<Float> {
        val stepsHorizontalPositions = arrayListOf<Float>()
        val linesSize = linesWidth

        for(i in 0 until maxCount) {
            //This offset contains step circle and right line
            val oneStepOffset = stepsSize + stepsLineMarginLeft + linesSize + stepsLineMarginRight
            val offsetToDraw = paddingLeft + (i * oneStepOffset)
            stepsHorizontalPositions.add(i, offsetToDraw + stepsSize / 2)
        }

        return stepsHorizontalPositions
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val linesSize = linesWidth

        for(i in 0 until maxCount) {
            Log.d("SS","drawing $i step")


            val xPos = getHorizontalCirclesPosition()[i]

            //Draw Steps Circle
            stepsPaint.color = if(i<reachedStep) stepsReachedColor else stepsUnreachedColor

            canvas?.drawCircle(
                    xPos,
                    yPos,
                    (stepsSize/2),
                    stepsPaint)


            //Draw Steps Line
            if(i<maxCount-1){
                stepsLinePaint.strokeWidth = stepsLineHeight
                stepsLinePaint.color =
                        if(i<reachedStep-1) stepsLineReachedColor
                        else stepsLineUnreachedColor

                var startXPoint = xPos + (stepsSize/2) + stepsLineMarginLeft
                canvas?.drawLine(
                        startXPoint,
                        yPos,
                        startXPoint + linesSize,
                        yPos,
                        stepsLinePaint
                )
            }


            //Draw Steps Index
            if(showStepIndex) {
                stepsTextPaint.color = stepsTextColor
                stepsTextPaint.textSize = stepsTextSize
                val drawingText = (i + 1).toString()
                stepsTextPaint.getTextBounds(drawingText, 0, drawingText.length, tmpRect)
                canvas?.drawText(
                        drawingText,
                        xPos,
                        (yPos + (tmpRect.height() / 2)),
                        stepsTextPaint)
            }
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> handleTouchPost(event.x,event.y)
        }
        return true
    }

    private fun handleTouchPost(x: Float, y: Float) {
        val lineSize = linesWidth
        for(i in 0 until getHorizontalCirclesPosition().size){

            //Disallow touch more than allowTouchStepTo
            if(i >= allowTouchStepTo) continue

            val xDotPos = getHorizontalCirclesPosition()[i]
            val yDotPos = yPos

            val stepHalf = stepsSize/2

            //verticalExtraSpace is the extra space for vertical touching
            val verticalExtraSpace = (rawHeiht*2)

            val stepArea = RectF(
                    xDotPos-stepHalf-lineSize-stepsLineMarginRight,
                    yDotPos-stepHalf-verticalExtraSpace,
                    xDotPos+stepHalf+stepsLineMarginLeft,
                    yDotPos+stepHalf+verticalExtraSpace
            )

            if(stepArea.contains(x, y)){
                //Touched Step (i+1)
                reachedStep = (i+1)
            }
        }

    }


    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.maxCount = maxCount
        ss.reachedStep = reachedStep
        ss.stepsReachedColor = stepsReachedColor
        ss.stepsUnreachedColor = stepsUnreachedColor
        ss.stepsLineReachedColor = stepsLineReachedColor
        ss.stepsLineUnreachedColor = stepsLineUnreachedColor
        ss.stepsLineHeight = stepsLineHeight
        ss.stepsSize = stepsSize
        ss.stepsTextColor = stepsTextColor
        ss.stepsTextSize = stepsTextSize
        ss.stepsLineMarginLeft = stepsLineMarginLeft
        ss.stepsLineMarginRight = stepsLineMarginRight
        ss.allowTouchStepTo = allowTouchStepTo
        ss.showStepIndex = showStepIndex
        return ss
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        val savedState = state as SavedState
        super.onRestoreInstanceState(savedState.superState)
        maxCount = savedState.maxCount
        reachedStep = savedState.reachedStep
        stepsReachedColor = savedState.stepsReachedColor
        stepsUnreachedColor = savedState.stepsUnreachedColor
        stepsLineReachedColor = savedState.stepsLineReachedColor
        stepsLineUnreachedColor = savedState.stepsLineUnreachedColor
        stepsLineHeight = savedState.stepsLineHeight
        stepsSize = savedState.stepsSize
        stepsTextColor = savedState.stepsTextColor
        stepsTextSize = savedState.stepsTextSize
        stepsLineMarginLeft = savedState.stepsLineMarginLeft
        stepsLineMarginRight = savedState.stepsLineMarginRight
        allowTouchStepTo = savedState.allowTouchStepTo
        showStepIndex = savedState.showStepIndex
    }

    class SavedState : BaseSavedState{
        companion object CREATOR : Parcelable.Creator<SavedState>{
            override fun createFromParcel(source: Parcel?) = SavedState(source)
            override fun newArray(size: Int) = arrayOfNulls<SavedState?>(size)
        }


        var maxCount : Int = 1
        var reachedStep: Int = 1
        var stepsReachedColor : Int = 1
        var stepsUnreachedColor : Int = 1
        var stepsLineReachedColor: Int = 1
        var stepsLineUnreachedColor: Int = 1
        var stepsLineHeight: Float = 1f
        var stepsSize: Float = 1f
        var stepsTextColor: Int = 1
        var stepsTextSize : Float = 1f
        var stepsLineMarginLeft : Float = 1f
        var stepsLineMarginRight : Float = 1f
        var allowTouchStepTo : Int = 1
        var showStepIndex: Boolean = true

        constructor(parcelable: Parcelable) : super(parcelable)
        constructor(parcel : Parcel?) : super(parcel){
            parcel?.let {
                maxCount = it.readInt()
                reachedStep = it.readInt()
                stepsReachedColor = it.readInt()
                stepsUnreachedColor = it.readInt()
                stepsLineReachedColor = it.readInt()
                stepsLineUnreachedColor = it.readInt()
                stepsLineHeight = it.readFloat()
                stepsSize = it.readFloat()
                stepsTextColor = it.readInt()
                stepsTextSize = it.readFloat()
                stepsLineMarginLeft = it.readFloat()
                stepsLineMarginRight = it.readFloat()
                allowTouchStepTo = it.readInt()
                showStepIndex = it.readInt()==1
            }

        }

        override fun writeToParcel(out: Parcel?, flags: Int) {
            super.writeToParcel(out, flags)

            out?.let {
                it.writeInt(maxCount)
                it.writeInt(reachedStep)
                it.writeInt(stepsReachedColor)
                it.writeInt(stepsUnreachedColor)
                it.writeInt(stepsLineReachedColor)
                it.writeInt(stepsLineUnreachedColor)
                it.writeFloat(stepsLineHeight)
                it.writeFloat(stepsSize)
                it.writeInt(stepsTextColor)
                it.writeFloat(stepsTextSize)
                it.writeFloat(stepsLineMarginLeft)
                it.writeFloat(stepsLineMarginRight)
                it.writeInt(allowTouchStepTo)
                it.writeInt(if(showStepIndex) 1 else 0)
            }
        }
    }

    interface OnStepChangeListener{
        fun onStepChanged(currentStep:Int)
    }

}