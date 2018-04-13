[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![APK](https://img.shields.io/badge/APK-Demo-brightgreen.svg)](https://github.com/imaNNeoFighT/ArcChartView/raw/master/repo_files/AcvDemo-1-0-1.apk)
[![](https://jitpack.io/v/imaNNeoFighT/ArcChartView.svg)](https://jitpack.io/#imaNNeoFighT/ArcChartView)
[![Android Arsenal]( https://img.shields.io/badge/Android%20Arsenal-ArcChartView-green.svg?style=flat )]( https://android-arsenal.com/details/1/6599 )

# Step Bar View

You can use this library to have a step bar

can be used in pages that you have some steps to reach

you can download the [Demo apk file](./repo_files/AcvDemo-1-0-0.apk) (you can first adjust your Chart in the app and then implement it in code)

<img src="./repo_files/images/demoo.gif" width="300">



## 1 - Getting Started

By this instructions you can add this library and i will explain how use it.



### Add Maven to your root build.gradle

First of all Add it in your root build.gradle at the end of repositories:

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

### Add Dependency

Add the dependency to your app build.gradle file

```
dependencies
{
    compile 'com.github.imaNNeoFighT:ArcChartView:1.0.1'
    // Or in new versions : 
    // implementation 'com.github.imaNNeoFighT:ArcChartView:1.0.1'
}
```

And then sync your gradle and take a tea.


## 2 - About The View
You can simply use this View like other Views in android,
just add ``StepBarView`` in your java code or xml.

## View Properties 

### * in XML
you can use this properties for make everything you want,
and all of them is arbitary,and can change via xml or java code/


|Attribute|Type|Kotlin|Description|
|:---:|:---:|:---:|:---:|
|sbv_max_count|Integer|`maxCount`|by this property you can specify your steps count (max to reach), default value is `8`|
|sbv_steps_reached_colors|Color|`stepsReachedColor`|by this property you can specify steps reached color (steps circle reached color)|
|sbv_steps_unreached_colors|Color|`stepsUnreachedColor`|by this property you can specify steps unReached color (steps circle default color)|
|sbv_steps_line_reached_colors|Color|`stepsLineReachedColor`|by this property you can specify steps line reached color|
|sbv_steps_line_unreached_colors|Color|`stepsLineUnreachedColor`|by this property you can specify steps line uReached color|
|sbv_steps_line_height|Dimensions|`stepsLineHeight`|by this property you can specify steps line height, default value is `4dp`|
|sbv_steps_size|Dimensions|`stepsSize`|by this property you can specify steps circle size, default value is `16dp`|
|sbv_steps_text_color|Color|`stepsTextColor`|by this property you can specify steps text color (number that drawn on steps circle)|
|sbv_steps_text_size|Dimensions|`stepsTextSize`|by this property you can specify steps text size, default is `14sp`|
|sbv_steps_line_margin_left|Dimensions|`stepsLineMarginLeft`|by this property you can specify steps line margin left (gap in left of lines), default value is `2dp`|
|sbv_steps_line_margin_right|Dimensions|`stepsLineMarginRight`|by this property you can specify steps line margin right (gap in right of lines), default value is `2dp`|
|sbv_allow_touch_step_to|Integer|`allowTouchStepTo`|by this property you can allow touch to reach step (for example if you set 3 you can touch to reach step to 3 and not more), default value is `8`|


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