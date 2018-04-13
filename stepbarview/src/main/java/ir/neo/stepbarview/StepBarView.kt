package ir.neo.stepbarview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
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

    init {
        maxCount = 8
        reachedStep = 3

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


    private fun calculateDesireHeight() = Math.max(stepsSize,stepsLineHeight)

    private fun getStepsLineSize() : Float {
        val allStepsSize = (maxCount * stepsSize)
        val allMarginsSize = ((maxCount-1) * (stepsLineMarginLeft + stepsLineMarginRight))
        val width = measuredWidth
        val available = (width - allStepsSize - allMarginsSize);
        return available/(maxCount-1)
    }


    private fun getHorizontalCirclesPosition() : ArrayList<Float> {
        val stepsHorizontalPositions = arrayListOf<Float>()
        val linesSize = getStepsLineSize()

        for(i in 0 until maxCount)
            stepsHorizontalPositions.add(i,(i * (stepsSize + stepsLineMarginLeft + linesSize + stepsLineMarginRight)) + stepsSize / 2)

        return stepsHorizontalPositions
    }
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)



        val linesSize = getStepsLineSize()

        for(i in 0 until maxCount) {
            Log.d("SS","drawing $i step")

            //Draw Steps Circle
            if(i<reachedStep){
                stepsPaint.color = stepsReachedColor
            }else{
                stepsPaint.color = stepsUnreachedColor
            }

            canvas?.drawCircle(
                    getHorizontalCirclesPosition()[i].toFloat(),
                    (height/2).toFloat(),
                    (stepsSize/2).toFloat(),
                    stepsPaint)


            //Draw Steps Line
            if(i<maxCount-1){
                stepsLinePaint.strokeWidth = stepsLineHeight.toFloat()
                stepsLinePaint.color =
                        if(i<reachedStep-1) stepsLineReachedColor
                        else stepsLineUnreachedColor

                var startXPoint = ((i*(stepsSize + stepsLineMarginLeft + linesSize + stepsLineMarginRight)) + stepsSize+stepsLineMarginLeft).toFloat()
                canvas?.drawLine(
                        startXPoint,
                        (height/2).toFloat(),
                        startXPoint + linesSize,
                        (height/2).toFloat(),
                        stepsLinePaint
                )
            }


            //Draw Steps Text
            stepsTextPaint.color = stepsTextColor
            stepsTextPaint.textSize = stepsTextSize
            val drawingText = (i+1).toString()
            stepsTextPaint.getTextBounds(drawingText,0,drawingText.length,tmpRect)
            canvas?.drawText(
                    drawingText,
                    (getHorizontalCirclesPosition()[i]).toFloat(),
                    ((height/2) + (tmpRect.height()/2)).toFloat(),
                    stepsTextPaint)
        }

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event?.action){
            MotionEvent.ACTION_DOWN -> {
                handleTouchPost(event.x,event.y)
            }

            MotionEvent.ACTION_MOVE -> {
                handleTouchPost(event.x,event.y)
            }

            MotionEvent.ACTION_UP -> {

            }
        }
        return true
    }

    private fun handleTouchPost(x: Float, y: Float) {
        val lineSize = getStepsLineSize()
        for(i in 0 until getHorizontalCirclesPosition().size){

            //Disallow touch more than allowTouchStepTo
            if(i >= allowTouchStepTo) continue

            val xDotPos = getHorizontalCirclesPosition()[i]
            val yDotPos = height/2

            val stepHalf = stepsSize/2

            //verticalExtraSpace is the extra space for vertical touching
            val verticalExtraSpace = (height*2)

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


    interface OnStepChangeListener{
        fun onStepChanged(currentStep:Int)
    }

}