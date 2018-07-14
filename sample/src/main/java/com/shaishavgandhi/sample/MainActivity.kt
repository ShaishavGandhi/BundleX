package com.shaishavgandhi.sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.shaishavgandhi.navigator.Extra

class MainActivity : AppCompatActivity() {

    @Extra(key = "shaishav")
    lateinit var message: String

    @Extra lateinit var intArray: Array<Int>

    @Extra lateinit var longArray: Array<Long>

    lateinit var list: ArrayList<KotlinParcelable>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bundle = Bundle()
        list = bundle.getKotlinParcelableList(arrayListOf())
    }
}
