package com.example.kutuki.view

import android.content.pm.ActivityInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.kutuki.R

open class BaseAct : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.base_act)
//        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

}