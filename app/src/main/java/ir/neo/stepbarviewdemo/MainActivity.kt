package ir.neo.stepbarviewdemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.*
import ir.neo.stepbarview.DpHandler
import ir.neo.stepbarview.StepBarView

class MainActivity : AppCompatActivity() ,SeekBar.OnSeekBarChangeListener{
    lateinit var myStepBarView : StepBarView
    lateinit var sbAttrsValue: SeekBar
    lateinit var spActions : Spinner
    lateinit var tvValue : TextView

    var actionsList : MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        myStepBarView = findViewById(R.id.my_stepBarView)
        sbAttrsValue = findViewById(R.id.sb_attrsValue)
        spActions = findViewById(R.id.sp_actions)
        tvValue = findViewById(R.id.tv_value)


        initSpinner()
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
        refreshValueText(myStepBarView.maxCount)
    }


    private fun refreshSelected(position : Int) {
        sbAttrsValue.setOnSeekBarChangeListener(null)
        when(actionsList[position]){
            "sbv_max_count" -> {
                sbAttrsValue.max = 20
                sbAttrsValue.progress = myStepBarView.maxCount
                refreshValueText(sbAttrsValue.progress)
            }
            "sbv_steps_line_height" -> {
                sbAttrsValue.max = 20
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, myStepBarView.stepsLineHeight.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
            "sbv_steps_size" -> {
                sbAttrsValue.max = 80
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, myStepBarView.stepsSize.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
            "sbv_steps_text_size" -> {
                sbAttrsValue.max = 25
                sbAttrsValue.progress = DpHandler.pxToSp(this@MainActivity, myStepBarView.stepsSize.toInt()).toInt()
                refreshValueText(sbAttrsValue.progress)
            }
            "sbv_steps_line_margin_left" -> {
                sbAttrsValue.max = 40
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, myStepBarView.stepsLineMarginLeft.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
            "sbv_steps_line_margin_right" -> {
                sbAttrsValue.max = 100
                sbAttrsValue.progress = DpHandler.pxToDp(this@MainActivity, myStepBarView.stepsLineMarginRight.toInt())
                refreshValueText(sbAttrsValue.progress)
            }
            "sbv_allow_touch_step_to" -> {
                sbAttrsValue.max = myStepBarView.maxCount
                sbAttrsValue.progress = myStepBarView.allowTouchStepTo
                refreshValueText(sbAttrsValue.progress)
            }
        }
        sbAttrsValue.setOnSeekBarChangeListener(this)
    }


    private fun refreshValueText(progress: Int) {
        when(actionsList[spActions.selectedItemPosition]){
            "sbv_max_count" -> {
                tvValue.text = "$progress"
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
                myStepBarView.maxCount= progress
            }
            "sbv_steps_line_height" -> {
                myStepBarView.stepsLineHeight = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "sbv_steps_size" -> {
                myStepBarView.stepsSize = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "sbv_steps_text_size" -> {
                myStepBarView.stepsTextSize = DpHandler.spToPx(this,progress.toFloat())
            }
            "sbv_steps_line_margin_left" -> {
                myStepBarView.stepsLineMarginLeft = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "sbv_steps_line_margin_right" -> {
                myStepBarView.stepsLineMarginRight = DpHandler.dpToPx(this@MainActivity,progress).toFloat()
            }
            "sbv_allow_touch_step_to" -> {
                myStepBarView.allowTouchStepTo = progress
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
