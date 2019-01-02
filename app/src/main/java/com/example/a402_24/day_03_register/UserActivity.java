package com.example.a402_24.day_03_register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UserActivity extends AppCompatActivity {
    final static String LOG_TAG =  "KNKUserDoing";
    public static final String MyPREFERENCES = "MyPrefs";

    TextView output_id;
    ImageView user_ImageView;
    ImageView user_ImageView2;
    Button btn_logout;


    public void initRefs(){
        Gson gson = new Gson();

        output_id = (TextView) findViewById(R.id.output_id);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        user_ImageView = (ImageView) findViewById(R.id.user_ImageView);
        user_ImageView2 = (ImageView) findViewById(R.id.user_ImageView2);


        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        String buffer = sharedPreferences.getString("info", null);
        final Member member = gson.fromJson(buffer, Member.class);

        try {
            if (member.getMember_id() != null) {
                output_id.setText(member.getMember_id());


                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {

                            Bitmap bitmap;

                            URL endPoint = new URL("http://192.168.0.26:8080");
                            String filePath = new String(member.getMember_profile_pic());
                            URL filePoint = new URL(endPoint.toString() + filePath);
                            Log.d(LOG_TAG, "filePoint" + filePoint);

                            HttpURLConnection conn = (HttpURLConnection) filePoint.openConnection();
                            conn.setDoInput(true);
                            conn.connect();

                            InputStream is = conn.getInputStream();

                            bitmap = BitmapFactory.decodeStream(is);

                            user_ImageView.setImageBitmap(bitmap);

                            is.close();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                });


            } else {
                output_id.setText(null);

            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setEvents(){

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(getApplicationContext(), Before_Register.class);
                startActivity(intent);

            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        initRefs();
        setEvents();

    }
}
