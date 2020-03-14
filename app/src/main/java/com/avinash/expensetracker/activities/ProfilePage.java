package com.avinash.expensetracker.activities;


import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

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
        final String value = sharedPreferences.getString("firebasekey","");
        Email.setText(value);




        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent main=new Intent(ProfilePage.this,MainActivity.class);
                startActivity(main);
                NotificationManagerCompat managerCompat=NotificationManagerCompat.from(ProfilePage.this);
                NotificationCompat.Builder noti=new NotificationCompat.Builder(ProfilePage.this);
                noti.setContentTitle("Welcome"+" "+value);
                noti.setContentText("Manage your incomes and expense ");
                noti.setSmallIcon(android.R.drawable.star_big_on);

                Intent i1=new Intent(ProfilePage.this,MainActivity.class);
                PendingIntent pd=PendingIntent.getActivities(ProfilePage.this,1, new Intent[]{i1},0);
                noti.setContentIntent(pd);
                noti.setAutoCancel(true);
                noti.setDefaults(NotificationCompat.DEFAULT_ALL)

                        .setPriority(NotificationCompat.PRIORITY_HIGH);

                managerCompat.areNotificationsEnabled();
                managerCompat.notify(1,noti.build());
                finish();


            }
        });

    }
}
