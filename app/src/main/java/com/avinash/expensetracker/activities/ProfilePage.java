package com.avinash.expensetracker.activities;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.avinash.expensetracker.R;

public class ProfilePage extends AppCompatActivity {

    TextView Email;

    String EmailHolder;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);



        Button b1=(Button)findViewById(R.id.button1);

        Email = (TextView) findViewById(R.id.emm);

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        String value = sharedPreferences.getString("firebasekey","");
        Email.setText(value);




        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent main=new Intent(ProfilePage.this,MainActivity.class);
                startActivity(main);

            }
        });

    }
}
