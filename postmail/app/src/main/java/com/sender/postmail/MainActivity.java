package com.sender.postmail;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sp;
    String userid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp= PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                userid=sp.getString("post-mail-userid","");
                if (userid.length()>0) {
                    if (userid.equals("logout")) {
                        startActivity(new Intent(MainActivity.this, loginpage.class));
                        finish();
                    }
                    else
                    {
                        startActivity(new Intent(MainActivity.this,home.class));
                        finish();
                    }
                }
                else
                {
                    startActivity(new Intent(MainActivity.this, loginpage.class));
                    finish();
                }
            }
        },1000);
    }
}
