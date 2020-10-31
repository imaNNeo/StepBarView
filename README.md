[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![APK](https://img.shields.io/badge/APK-Demo-brightgreen.svg)](https://github.com/imaNNeoFighT/StepBarView/raw/master/repo_files/SbvDemo1-1-0.apk)
[![](https://jitpack.io/v/imaNNeoFighT/StepBarView.svg)](https://jitpack.io/#imaNNeoFighT/StepBarView)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-StepBarView-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/6931)

# Step Bar View

You can use this library to have a step bar.

It can be used in pages that you have some steps to reach.

You can download the [Demo apk file](https://github.com/imaNNeoFighT/StepBarView/raw/master/repo_files/SbvDemo1-1-0.apk) (you can first adjust your StepBar in the demoApp and then implement it in code).

<img src="./repo_files/images/demoo.gif" width="300">



## 1 - Getting Started

By these instructions you can add this library and I will explain how to use it.



### Add Maven to your root build.gradle

First of all add it in your root build.gradle at the end of the repositories:

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

### Add Dependency

Add the dependency to your app build.gradle file.

```
dependencies
{
    implementation  'com.github.imaNNeoFighT:StepBarView:1.1.0'
    // Or in older versions : 
    // compile'com.github.imaNNeoFighT:StepBarView:1.1.0'
}
```

And then sync your gradle and have a cup of tea.


## 2 - About The View
You can simply use this View like other Views in android,
just add ``StepBarView`` in your java code or xml.

## View Properties 

You can customize StepBarView. All of this attributes can be changed via xml or code (runtime).


|Attribute|Type|Kotlin|Description|
|:---:|:---:|:---:|:---:|
|sbv_max_count|Integer|`maxCount`|your steps count (max to reach), default value is `8`|
|sbv_steps_reached_colors|Color|`stepsReachedColor`|steps reached color (steps circle reached color)|
|sbv_steps_unreached_colors|Color|`stepsUnreachedColor`|steps unReached color (steps circle default color)|
|sbv_steps_line_reached_colors|Color|`stepsLineReachedColor`|steps line reached color|
|sbv_steps_line_unreached_colors|Color|`stepsLineUnreachedColor`|steps line uReached color|
|sbv_steps_line_height|Dimensions|`stepsLineHeight`|steps line height, default value is `4dp`|
|sbv_steps_size|Dimensions|`stepsSize`|steps circle size, default value is `16dp`|
|sbv_steps_text_color|Color|`stepsTextColor`|steps text color (number that drawn on steps circle)|
|sbv_steps_text_size|Dimensions|`stepsTextSize`|steps text size, default is `14sp`|
|sbv_steps_line_margin_left|Dimensions|`stepsLineMarginLeft`|steps line margin left (gap in left of lines), default value is `2dp`|
|sbv_steps_line_margin_right|Dimensions|`stepsLineMarginRight`|steps line margin right (gap in right of lines), default value is `2dp`|
|sbv_allow_touch_step_to|Integer|`allowTouchStepTo`|allow touch to reach step (for example if you set 3 you can touch to reach step to 3 and not more), default value is `8`|
|sbv_show_step_index|Boolean|`showStepIndex`|you can set this property false to prevent showing indexes (then just a solid circle will be drawn), default value is `true`|
|sbv_steps_stroke_size|Dimensions|`stepsStrokeSize`| Stroke Size of steps , default value is `2dp`|
|sbv_steps_stroke_reached_color|Color|`stepsStrokeReachedColor`| Stroke color of reached steps|
|sbv_steps_stroke_unreached_color|Color|`stepsStrokeUnReachedColor`| Stroke color of unReached steps|
|sbv_steps_stroke_current_color|Color|`stepsStrokeCurrentColor`| Stroke color of current steps|
|sbv_show_step_stroke|Boolean|`showStepStroke`| flag to showing the Stroke or not!, default is false|
|sbv_is_rtl|Boolean|`isRtl`| flag to showing steps in RTL (Right to left), default is false|
|sbv_show_step_name|Boolean|`showStepName`| flag to show title below the steps, default is false|
|sbv_is_fixed_steps_line_width|Boolean|`isFixedStepsLineWidth`| flag to specify that the line widths is fixed or it should calculate depends on View width (use it when you want to achieve scrollable view)|
|sbv_steps_line_width|Dimensions|`stepsLineWidth`|steps line width, default value is `24dp`, it will ignored when isFixedStepsLineWidth is false|


## 3 - Some Samples

### Scrollable Sample
To achieve scrollable View just set `isFixedStepsLineWidth : true`, and put this view inside a `HorizontalScrollView`,

just like this:

<img src="./repo_files/images/scrollable_sample.gif" width="300">

```
<HorizontalScrollView
        android:id="@+id/stepBar3Container"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toBottomOf="@id/my_stepBarView2"
        app:layout_constraintBottom_toTopOf="@+id/my_stepBarView4"
        android:scrollbarSize="0dp"
        >

        <ir.neo.stepbarview.StepBarView
            android:id="@+id/my_stepBarView3"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            app:sbv_steps_size="28dp"
            app:sbv_steps_reached_colors="#fff"
            app:sbv_steps_line_reached_colors="#fff"
            app:sbv_steps_line_unreached_colors="#dbcecece"
            app:sbv_steps_unreached_colors="#dbcecece"
            app:sbv_steps_text_color="#f0f"
            app:sbv_max_count="15"
            android:paddingLeft="24dp"
            android:paddingRight="24dp"
            android:background="#f0f"
            android:paddingTop="8dp"
            android:paddingBottom="8dp"
            app:sbv_is_fixed_steps_line_width="true"
            app:sbv_steps_line_width="80dp"
            />

</HorizontalScrollView>
```


# License
```
Copyright 2018 Iman Khoshabi

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
