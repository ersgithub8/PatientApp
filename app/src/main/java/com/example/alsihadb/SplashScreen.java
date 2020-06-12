package com.example.alsihadb;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class SplashScreen extends AppCompatActivity {


    SessionManagment managment;
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        managment=new SessionManagment();
        /////////////////////

        handler=new Handler();
        final Runnable runnable= new Runnable() {
            @Override
            public void run() {

                try {
                    String status=managment.getPreferences(SplashScreen.this,"status");


                    if (status.equals("1") ){

                        SharedPreferences sharedPreferences = getSharedPreferences("userdata", MODE_PRIVATE);
                        if(sharedPreferences.getString("login","null").equals("pat"))
                        {
                            Intent intent=new Intent(SplashScreen.this,PateintMain.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Intent intent=new Intent(SplashScreen.this,DocMain.class);
                            startActivity(intent);
                        }



                    }
                    else
                    {

                        Intent i=new Intent(SplashScreen.this,PateintMain.class);
                        startActivity(i);

                    }
                    finish();
                }catch (Exception e){

                    Toast.makeText(SplashScreen.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        };
        new Thread()
        {
            @Override
            public void run()
            {
                SystemClock.sleep(3000);
                handler.post(runnable);
            }

        }.start();
    }
}

