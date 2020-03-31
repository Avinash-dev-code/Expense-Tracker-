package com.avinash.expensetracker.activities;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.TooltipCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.avinash.expensetracker.R;

public class ProfilePage extends AppCompatActivity {

    TextView Nameid,Emailid;

    String EmailHolder;
    SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);



        final Button b1=(Button)findViewById(R.id.button1);

        Nameid = (TextView) findViewById(R.id.emm);
        Emailid = (TextView) findViewById(R.id.emailid);

        SharedPreferences sharedPreferences = getSharedPreferences("myKey", MODE_PRIVATE);
        final String value = sharedPreferences.getString("firebasekey","");
        Nameid.setText(value);
        SharedPreferences sharedPreferencesS = getSharedPreferences("myKeysecond", MODE_PRIVATE);
        final String values = sharedPreferencesS.getString("firebasekeysecond","");
        Emailid.setText(values);


        b1.setOnClickListener(new View.OnClickListener() {

            @SuppressLint({"NewApi", "ResourceAsColor"})
            @Override
            public void onClick(View v) {



                Intent main=new Intent(ProfilePage.this,MainActivity.class);
                startActivity(main);

                NotificationManagerCompat  mNotificationManager =NotificationManagerCompat.from(ProfilePage.this);
                //Here is FILE_NAME is the name of file that you want to pl

                if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel("YOUR_CHANNEL_ID",
                            "YOUR_CHANNEL_NAME",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    channel.setDescription("YOUR_NOTIFICATION_CHANNEL_DISCRIPTION");
                    mNotificationManager.createNotificationChannel(channel);


                }

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "YOUR_CHANNEL_ID")
                        .setSmallIcon(R.drawable.helpicon);
                mBuilder.setSound(null);

                  mBuilder.setColor(R.color.colorPrimary);// notification icon
                 mBuilder.setContentTitle("Welcome"+" "+value);
                mBuilder.setContentText("Manage your incomes and expense ");// message for notification

                mBuilder.setAutoCancel(true); // clear notification after click
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                PendingIntent pi = PendingIntent.getActivity(ProfilePage.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pi);
                mBuilder.setAutoCancel(true);

                mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL)

                        .setPriority(NotificationCompat.PRIORITY_HIGH);


                mNotificationManager.areNotificationsEnabled();
                mNotificationManager.notify(1,mBuilder.build());

                MediaPlayer mp=MediaPlayer.create(getApplicationContext(),R.raw.sound);
                mp.start();
                finish();




            }




        });

    }
}
