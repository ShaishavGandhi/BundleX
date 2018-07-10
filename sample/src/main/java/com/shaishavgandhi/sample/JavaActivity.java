package com.shaishavgandhi.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.shaishavgandhi.navigator.Extra;

public class JavaActivity extends AppCompatActivity {

    @Extra String javaMessage;
    @Extra int points;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
