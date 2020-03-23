package com.avinash.expensetracker.activities;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.avinash.expensetracker.R;
import com.avinash.expensetracker.adapters.SectionsPageAdapter;
import com.avinash.expensetracker.fragments.BalanceFragment;
import com.avinash.expensetracker.fragments.CustomBottomSheetDialogFragment;
import com.avinash.expensetracker.fragments.ExpenseFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;


import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.provider.ContactsContract;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnCreateContextMenuListener {


    private ViewPager mViewPager;

    public static FloatingActionButton fab;
    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;
    String EmailHolder;
    TextView Ema;
    FirebaseAuth mAuth;
    private MenuItem Tips;
    private WebView myWebView;

    private DrawerLayout drawerLayout;
    private FrameLayout frameLayout;
    private NavigationView navigationView;


    public static final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b1=(Button)findViewById(R.id.button1);

        Ema = (TextView) findViewById(R.id.emm);


        dl = (DrawerLayout)findViewById(R.id.activity);
        t = new ActionBarDrawerToggle(this, dl,R.string.Open, R.string.Close);

        dl.addDrawerListener(t);
        t.syncState();
           MediaPlayer mp=MediaPlayer.create(getApplicationContext(),R.raw.sound);
        mp.start();



        nv = (NavigationView)findViewById(R.id.nv);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id)
                {

                    case R.id.account:
                        Intent acc=new Intent(MainActivity.this,ProfilePage.class);
                        startActivity(acc);
                        break;

                        case R.id.WebView:
                        Intent web=new Intent(MainActivity.this,Main2Activity.class);
                        startActivity(web);


                        break;

                        case R.id.Shareapp:
                        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                        sharingIntent.setType("text/plain");
                        String shareBody = "https://drive.google.com/file/d/1hnGsQgIYVmzbl84h-vP-a-9mtgQdYDEY/view?usp=sharing";
                        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        Toast.makeText(MainActivity.this, "My Cart",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.Aboutus:
                        Intent about=new Intent(MainActivity.this,About.class);
                        startActivity(about);
                        break;


                    case R.id.logout:

                        FirebaseAuth.getInstance().signOut();
                       finish();


                        Toast.makeText(MainActivity.this,"Log Out Successfull", Toast.LENGTH_LONG).show();
                        Intent intet=new Intent(MainActivity.this,login.class);
                        startActivity(intet);


                    default:
                        return true;
                }


                return true;

            }
        });



        mViewPager=findViewById(R.id.container);
        setupViewPager(mViewPager);

        TabLayout tabLayout=findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CustomBottomSheetDialogFragment().show(getSupportFragmentManager(), "Dialog");

            }
        });

    }




    private void setupViewPager(ViewPager viewPager){
        SectionsPageAdapter adapter=new SectionsPageAdapter(getSupportFragmentManager());
        adapter.addFragment(new ExpenseFragment(),"Expenses");
        adapter.addFragment(new BalanceFragment(),"Balance");
        viewPager.setAdapter(adapter);
    }



}
