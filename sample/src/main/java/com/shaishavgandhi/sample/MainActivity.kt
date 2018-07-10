package com.shaishavgandhi.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.shaishavgandhi.navigator.Extra

class MainActivity : AppCompatActivity() {

    @Extra(key = "shaishav")
    lateinit var message: String

    @Extra
    lateinit var intArray: Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
