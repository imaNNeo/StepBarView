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

/**
 * Created by iman.
 * iman.neofight@gmail.com
 */
class StepBarView @JvmOverloads
constructor(mContext : Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) :
        View(mContext,attrs, defStyleAttr) {

    companion object {
        private val IS_DEBUG = false

        // This is a fixed distance to add space between the circle and the step name
        private val NAME_STEP_SEPARATION_PX = 10
    }

    private var stepsPaint : Paint
    private var stepsStrokePaint : Paint
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
        set(value) {
            field = value
            invalidate()
        }

    var showStepName: Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    var stepsStrokeSize : Float = 0f
        set(value) {
            field = value
            invalidate()
        }

    var stepsStrokeReachedColor : Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var stepsStrokeUnReachedColor : Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var stepsStrokeCurrentColor : Int = 0
        set(value) {
            field = value
            invalidate()
        }

    var showStepStroke : Boolean = true
        set(value) {
            field = value
            invalidate()
        }

    var isRtl : Boolean = false
        set(value) {
            field = value
            invalidate()
        }

    //To include the steps name height when onMeasure is called
    //if showStepName is false, this is 0
    private var namesHeight = 0

    var allowSelectStep = object : AllowSelectStep{
        override fun allowSelectStep(step: Int) = true
    }

    private lateinit var stepsNames: Array<String?>

    /**
     * To add the name of the steps under the circle step.
     * This must be called from the app implementing this library
     *
     * @param step The step number to set the name
     * @param name The name of the step
     */
    fun setStepName(step: Int, name: String) {
        if (this::stepsNames.isInitialized) {
            if(step <= maxCount) {
                stepsNames[step] = name
            } else {
                Log.e("SBV", "setting stepname greater than maxcount")
            }
        }
    }

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
        showStepName = false

        stepsStrokeSize = DpHandler.dpToPx(context,2).toFloat()

        stepsStrokeReachedColor = ContextCompat.getColor(context,R.color.sbv_step_stroke_reached_color)
        stepsStrokeUnReachedColor = ContextCompat.getColor(context,R.color.sbv_step_stroke_unreached_color)
        stepsStrokeCurrentColor = ContextCompat.getColor(context,R.color.sbv_step_stroke_current_color)

        showStepStroke = false

        isRtl = false

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

            showStepIndex = a.getBoolean(R.styleable.StepBarView_sbv_show_step_index, showStepIndex)

            showStepName = a.getBoolean(R.styleable.StepBarView_sbv_show_step_name, showStepName)
            if (showStepName) {
                stepsNames = arrayOfNulls<String>(maxCount)
            }

            stepsStrokeSize = a.getDimension(R.styleable.StepBarView_sbv_steps_stroke_size,stepsStrokeSize)

            stepsStrokeReachedColor = a.getColor(R.styleable.StepBarView_sbv_steps_stroke_reached_color,stepsStrokeReachedColor)
            stepsStrokeUnReachedColor = a.getColor(R.styleable.StepBarView_sbv_steps_stroke_unreached_color,stepsStrokeUnReachedColor)
            stepsStrokeCurrentColor = a.getColor(R.styleable.StepBarView_sbv_steps_stroke_current_color,stepsStrokeCurrentColor)

            showStepStroke = a.getBoolean(R.styleable.StepBarView_sbv_show_step_stroke, showStepStroke)

            isRtl = a.getBoolean(R.styleable.StepBarView_sbv_is_rtl, isRtl)

            a.recycle()
        }

        stepsPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }

        stepsStrokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
        }

        stepsLinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
        }

        stepsTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            textAlign = Paint.Align.CENTER
        }

        //If showStepName is true we get the text height with a sample text
        //to be considered when onMeasure() is called
        if (showStepName) {
            stepsTextPaint.color = stepsTextColor
            stepsTextPaint.textSize = stepsTextSize
            stepsTextPaint.getTextBounds("sample", 0, "sample".length, tmpRect)
            namesHeight = tmpRect.height() + NAME_STEP_SEPARATION_PX * 2
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


    private fun calculateDesireHeight() = rawHeiht + paddingTop + paddingBottom + namesHeight

    private fun getHorizontalCirclesPosition() : FloatArray {
        val stepsHorizontalPositions = FloatArray(maxCount)
        val linesSize = linesWidth


        val range = when(isRtl) {
            true -> maxCount-1 downTo 0
            false -> 0 until maxCount
        }
        for(i in range) {
            //This offset contains step circle and right line
            val oneStepOffset = stepsSize + stepsLineMarginLeft + linesSize + stepsLineMarginRight
            val offsetToDraw = paddingLeft + (i * oneStepOffset)

            val pos = if(isRtl) (maxCount-1)-i else i
            stepsHorizontalPositions[pos] = offsetToDraw + stepsSize / 2
        }

        return stepsHorizontalPositions
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val linesSize = linesWidth

        for(i in 0 until maxCount) {
            if (IS_DEBUG) {
                Log.d("SS", "drawing $i step")
            }

            val xPos = getHorizontalCirclesPosition()[i]

            //Draw Steps Line
            if(i<maxCount-1){
                stepsLinePaint.strokeWidth = stepsLineHeight
                stepsLinePaint.color =
                        if(i<reachedStep-1) stepsLineReachedColor
                        else stepsLineUnreachedColor

                val startXPoint =
                        when(isRtl){
                            true -> xPos - (stepsSize/2) - stepsLineMarginRight
                            false -> xPos + (stepsSize/2) + stepsLineMarginLeft
                        }

                val endXPoint =
                        when(isRtl){
                            false -> startXPoint + linesSize
                            true -> startXPoint - linesSize
                        }

                canvas?.drawLine(
                        startXPoint,
                        yPos,
                        endXPoint,
                        yPos,
                        stepsLinePaint
                )
            }

            //Draw Steps Circle
            stepsPaint.color = if(i<reachedStep) stepsReachedColor else stepsUnreachedColor
            canvas?.drawCircle(
                    xPos,
                    yPos,
                    (stepsSize/2),
                    stepsPaint)

            //Draw Step Stroke
            if(showStepStroke) {
                stepsStrokePaint.strokeWidth = stepsStrokeSize
                stepsStrokePaint.color = when {
                    i + 1 < reachedStep -> stepsStrokeReachedColor
                    i + 1 == reachedStep -> stepsStrokeCurrentColor
                    else -> stepsStrokeUnReachedColor
                }
                canvas?.drawCircle(
                        xPos,
                        yPos,
                        (stepsSize / 2) - (stepsStrokePaint.strokeWidth / 2),
                        stepsStrokePaint)
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

            //Draw Steps Names
            if(showStepName) {
                stepsNames[i]?.let { name ->
                    stepsTextPaint.getTextBounds(name, 0, name.length, tmpRect)
                    canvas?.drawText(
                        name,
                        xPos,
                        yPos*2 + tmpRect.height() + NAME_STEP_SEPARATION_PX,
                        stepsTextPaint)
                    namesHeight = tmpRect.height()
                }
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

            //Disallow touch user prevent steps
            if(!allowSelectStep.allowSelectStep(i+1)) continue

            val xDotPos = getHorizontalCirclesPosition()[i]
            val yDotPos = yPos

            val stepHalf = stepsSize/2

            //verticalExtraSpace is the extra space for vertical touching
            val verticalExtraSpace = (rawHeiht*2)

            val leftArea =
                    when(isRtl){
                        false -> xDotPos-stepHalf-lineSize-stepsLineMarginRight
                        true -> xDotPos-stepHalf-stepsLineMarginRight
                    }

            val rightArea =
                    when(isRtl){
                        false -> xDotPos+stepHalf+stepsLineMarginLeft
                        true -> xDotPos+stepHalf+stepsLineMarginLeft+lineSize
                    }

            val stepArea = RectF(
                    leftArea,
                    yDotPos-stepHalf-verticalExtraSpace,
                    rightArea,
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
        ss.stepsStrokeSize = stepsStrokeSize
        ss.stepsStrokeReachedColor = stepsStrokeReachedColor
        ss.stepsStrokeUnReachedColor = stepsStrokeUnReachedColor
        ss.stepsStrokeCurrentColor = stepsStrokeCurrentColor
        ss.showStepStroke = showStepStroke
        ss.isRtl = isRtl
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
        stepsStrokeSize = savedState.stepsStrokeSize
        stepsStrokeReachedColor = savedState.stepsStrokeReachedColor
        stepsStrokeUnReachedColor = savedState.stepsStrokeUnReachedColor
        stepsStrokeCurrentColor = savedState.stepsStrokeCurrentColor
        showStepStroke = savedState.showStepStroke
        isRtl = savedState.isRtl
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
        var stepsStrokeSize : Float = 1f
        var stepsStrokeReachedColor : Int = 1
        var stepsStrokeUnReachedColor : Int = 1
        var stepsStrokeCurrentColor : Int = 1
        var showStepStroke : Boolean = false
        var isRtl : Boolean = false

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
                stepsStrokeSize = it.readFloat()
                stepsStrokeReachedColor = it.readInt()
                stepsStrokeUnReachedColor = it.readInt()
                stepsStrokeCurrentColor = it.readInt()
                showStepStroke = it.readInt()==1
                isRtl = it.readInt()==1
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
                it.writeFloat(stepsStrokeSize)
                it.writeInt(stepsStrokeReachedColor)
                it.writeInt(stepsStrokeUnReachedColor)
                it.writeInt(stepsStrokeCurrentColor)
                it.writeInt(if(showStepStroke) 1 else 0)
                it.writeInt(if(isRtl) 1 else 0)
            }
        }
    }

    interface OnStepChangeListener{
        fun onStepChanged(currentStep:Int)
    }

    interface AllowSelectStep{
        fun allowSelectStep(step: Int) : Boolean
    }

}