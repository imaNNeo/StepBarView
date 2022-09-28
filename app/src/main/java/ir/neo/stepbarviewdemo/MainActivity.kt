package ir.neo.stepbarviewdemo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.*
import ir.neo.stepbarview.DpHandler
import ir.neo.stepbarview.StepBarView
import ir.neo.stepbarview.StepBarView.StepsTitleSetter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() ,SeekBar.OnSeekBarChangeListener{
    lateinit var sbAttrsValue: SeekBar
    lateinit var spActions : Spinner
    lateinit var tvValue : TextView

    var actionsList : MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        my_stepBarView4.allowSelectStep = object : StepBarView.AllowSelectStep{
            override fun allowSelectStep(step: Int) = step != 2
        }

        sbAttrsValue = findViewById(R.id.sb_attrsValue)
        spActions = findViewById(R.id.sp_actions)
        tvValue = findViewById(R.id.tv_value)


        initSpinner()
        initStepNames()
    }

    private fun initStepNames() {
        my_stepBarView2.stepsTitleSetter = object : StepsTitleSetter {
            override fun getStepTitle(step: Int): String {
                return when (step) {
                    1 -> "Fist"
                    2 -> "2nd"
                    3 -> "Third"
                    4 -> "4th"
                    5 -> "Fifth"
                    6 -> "6th"
                    7 -> "Seventh"
                    8 -> "8th"
                    9 -> "Ninth"
                    10 -> "10th"
                    else -> "Non"
                }
            }
        }
    }

    private fun initSpinner() {
        actionsList.add("sbv_max_count")
        actionsList.add("sbv_steps_line_height")
        actionsList.add("sbv_steps_size")
        actionsList.add("sbv_steps_text_size")
        actionsList.add("sbv_steps_line_margin_left")
        actionsList.add("sbv_steps_line_margin_right")
        actionsList.add("sbv_allow_touch_step_to")

        val spinnerArrayAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,
                actionsList)
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item)
        spActions.adapter = spinnerArrayAdapter


        spActions.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                refreshSelected(position)
            }

        }

        spActions.setSelection(0)
        refreshValueText(my_stepBarView4.maxCount)
    }


    private fun refreshSelected(position : Int) {
        sbAttrsValue.setOnSeekBarChangeListener(null)
        when(actionsList[position]){
            "sbv_max_count" -> {
                sbAttrsValue.max = 20
                sbAttrsValue.progress = my_stepBarView4.maxCount-2
                refreshValueText(sbAttrsValue.progress)
            }
            "sbv_steps_line_height" -> {
                sbAttrsValue.max = 20
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, my_stepBarView4.stepsLineHeight.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
            "sbv_steps_size" -> {
                sbAttrsValue.max = 80
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, my_stepBarView4.stepsSize.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
            "sbv_steps_text_size" -> {
                sbAttrsValue.max = 25
                sbAttrsValue.progress = DpHandler.pxToSp(this@MainActivity, my_stepBarView4.stepsSize.toInt()).toInt()
                refreshValueText(sbAttrsValue.progress)
            }
            "sbv_steps_line_margin_left" -> {
                sbAttrsValue.max = 40
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, my_stepBarView4.stepsLineMarginLeft.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
            "sbv_steps_line_margin_right" -> {
                sbAttrsValue.max = 100
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, my_stepBarView4.stepsLineMarginRight.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
            "sbv_allow_touch_step_to" -> {
                sbAttrsValue.max = my_stepBarView4.maxCount
                sbAttrsValue.progress = my_stepBarView4.allowTouchStepTo
                refreshValueText(sbAttrsValue.progress)
            }
        }
        sbAttrsValue.setOnSeekBarChangeListener(this)
    }


    private fun refreshValueText(progress: Int) {
        when(actionsList[spActions.selectedItemPosition]){
            "sbv_max_count" -> {
                tvValue.text = "${progress+2}"
            }
            "sbv_steps_line_height" -> {
                tvValue.text = "$progress dp"
            }
            "sbv_steps_size" -> {
                tvValue.text = "$progress sp"
            }
            "sbv_steps_text_size" -> {
                tvValue.text = "$progress sp"
            }
            "sbv_steps_line_margin_left" -> {
                tvValue.text = "$progress dp"
            }
            "sbv_steps_line_margin_right" -> {
                tvValue.text = "$progress dp"
            }
            "sbv_allow_touch_step_to" -> {
                tvValue.text = "$progress"
            }
        }
    }

    private fun refreshProgress(progress: Int) {
        when(actionsList[spActions.selectedItemPosition]){
            "sbv_max_count" -> {
                my_stepBarView4.maxCount= progress+2
            }
            "sbv_steps_line_height" -> {
                my_stepBarView4.stepsLineHeight = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "sbv_steps_size" -> {
                my_stepBarView4.stepsSize = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "sbv_steps_text_size" -> {
                my_stepBarView4.stepsTextSize = DpHandler.spToPx(this,progress.toFloat())
            }
            "sbv_steps_line_margin_left" -> {
                my_stepBarView4.stepsLineMarginLeft = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "sbv_steps_line_margin_right" -> {
                my_stepBarView4.stepsLineMarginRight = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "sbv_allow_touch_step_to" -> {
                my_stepBarView4.allowTouchStepTo = progress
            }
        }
    }


    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        refreshValueText(progress)
        refreshProgress(progress)
    }
    override fun onStartTrackingTouch(seekBar: SeekBar?){}
    override fun onStopTrackingTouch(seekBar: SeekBar?){}
}
