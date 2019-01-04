package com.example.a402_24.day_03_register;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    final static String LOG_TAG =  "KNKUserDoing";

    TextView user_id;
    ImageView user_image;
    Button user_logout;


    Gson gson = new Gson();

    public void initRefs(View v){
        user_id = (TextView) v.findViewById(R.id.user_id);
        user_image = (ImageView) v.findViewById(R.id.user_image);
        user_logout = (Button) v.findViewById(R.id.user_logout);
    }

    public void setEvent(View v) {

        String member_Str = getArguments().getString("member");
        final Member member = gson.fromJson(member_Str, Member.class);


        if (member.getMember_id() != null) {
            user_id.setText(member.getMember_id());

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap;
                    try {
                        URL endPoint = new URL("http://192.168.0.19:8080");
                        String filePath = member.getMember_profile_pic();
                        URL finalPoint = new URL(endPoint + filePath);

                        HttpURLConnection conn = (HttpURLConnection) finalPoint.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                user_image.setImageBitmap(bitmap);
                            }
                        });
                        is.close();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }

            });

        } else {
            return;
        }
    }

    public UserFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         View v =  inflater.inflate(R.layout.fragment_user, container, false);
         initRefs(v);
         setEvent(v);
         return v;
    }

}
