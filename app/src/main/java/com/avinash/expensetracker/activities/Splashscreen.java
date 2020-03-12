package com.avinash.expensetracker.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.avinash.expensetracker.R;

public class Splashscreen extends AppCompatActivity {
    private static int splashTimeOut=1470;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(Splashscreen.this,OnBoardingActivity.class);
                startActivity(i);
                finish();
            }
        },splashTimeOut);
    }

}
