package com.avinash.expensetracker.activities;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.avinash.expensetracker.R;

public class About extends AppCompatActivity {

    ImageView fb,yt,twt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        fb=(ImageView)findViewById(R.id.facebook);
        yt=(ImageView)findViewById(R.id.youtube);
        twt=(ImageView)findViewById(R.id.twitter);
        TextView scrollingtext=(TextView)findViewById(R.id.scrollingtext);
        scrollingtext.setSelected(true);
        scrollingtext.startAnimation((Animation) AnimationUtils.loadAnimation(this,R.anim.scrollingtext));



        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.facebook.com/profile.php?id=100008953200181";
                Intent fblink = new Intent(Intent.ACTION_VIEW);
                fblink.setData(Uri.parse(url));
                startActivity(fblink);
            }
        });
        yt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.youtube.com/watch?v=bxTPjGmi31Q";
                Intent ytlink = new Intent(Intent.ACTION_VIEW);
                ytlink.setData(Uri.parse(url));
                startActivity(ytlink);

            }
        });
        twt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://twitter.com/Google?ref_src=twsrc%5Egoogle%7Ctwcamp%5Eserp%7Ctwgr%5Eauthor";
                Intent twtlink = new Intent(Intent.ACTION_VIEW);
                twtlink.setData(Uri.parse(url));
                startActivity(twtlink);

            }
        });
    }
}
