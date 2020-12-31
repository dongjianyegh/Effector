package com.example.effector;

import android.os.Bundle;

import com.dongjianye.effector.OutlineEffector;
import com.dongjianye.effector.internal.BlendManager;

import androidx.appcompat.app.AppCompatActivity;

import android.view.MotionEvent;
import android.view.View;

import android.view.ViewGroup;

public class MainActivity extends AppCompatActivity {

    private KeyView mKeyA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mKeyA = findViewById(R.id.textA);
    }


}