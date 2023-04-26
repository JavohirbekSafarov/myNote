package com.javohirbekcoder.mynote.models

import android.graphics.Color
import android.os.Parcelable

/*
Created by Javohirbek on 16.04.2023 at 18:17
*/
data class MainRecyclerModel(
    val id : Int,
    val colorIndex: Int,
    val title: String,
    val note : String,
    val time : String
)
