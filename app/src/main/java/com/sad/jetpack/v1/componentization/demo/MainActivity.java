package com.sad.jetpack.v1.componentization.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sad.jetpack.v1.componentization.api.SCore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SCore.registerParasiticComponentFromHost(this);
    }
}