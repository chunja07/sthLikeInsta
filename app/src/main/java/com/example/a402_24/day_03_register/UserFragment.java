package com.example.a402_24.day_03_register;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
    final static String LOG_TAG =  "KNKUserDoing";
    public static final String MyPREFERENCES = "MyPrefs";

    TextView user_id;
    com.mikhaellopez.circularimageview.CircularImageView user_image;
    Button user_logout;
    TextView follower_num;
    TextView followed_num;
    TextView introduction;

    Gson gson = new Gson();

    public void initRefs(View v){
        user_id = (TextView) v.findViewById(R.id.user_id);
        user_image = (com.mikhaellopez.circularimageview.CircularImageView) v.findViewById(R.id.user_image);
        user_logout = (Button) v.findViewById(R.id.user_logout);
        follower_num = (TextView) v.findViewById(R.id.follower_num);
        followed_num = (TextView) v.findViewById(R.id.followed_num);
        introduction = (TextView) v.findViewById(R.id.introduction);
    }

    public void setEvent(View v) {

        String member_Str = getArguments().getString("member");
        final Member member = gson.fromJson(member_Str, Member.class);

        if (member.getMember_id() != null) {
            user_id.setText(member.getMember_id());

            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL endPoint = new URL("http://192.168.10.208:8080/JS/android/user");
                        HttpURLConnection conn = (HttpURLConnection) endPoint.openConnection();

                        String id = member.getMember_id();
                        String requestParam = String.format("id=%s", id);

                        conn.setDoOutput(true);
                        conn.getOutputStream().write(requestParam.getBytes());

                        conn.setRequestMethod("POST");

                        Log.d(LOG_TAG, "현재 Post까지 실행되었습니다");

                        if(conn.getResponseCode()==200){
                            Log.d(LOG_TAG, "200번, 연결이 성공적으로 되었습니다.");
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            StringBuffer buffer = new StringBuffer();
                            String temp;
                            while((temp=br.readLine()) != null){
                                buffer.append(temp);
                            }
                            String decode = URLDecoder.decode(buffer.toString(),"UTF-8");
                            Log.d(LOG_TAG, "팔로우 숫자가 json으로 넘어옵니다 : " + buffer.toString());
                            try {
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getActivity().getApplication(), "200번 연결 실패", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            final HashMap<String, Object> value = new Gson().fromJson(decode.toString(), HashMap.class);

                            Double followed_num_double = (Double) value.get("followed_num");
                            Double follower_num_double = (Double) value.get("followed_num");

                            final Integer followed_num_int = followed_num_double.intValue();
                            final Integer follower_num_int = follower_num_double.intValue();

                            Log.d("followed_num", value.get("follower_num").toString());
                            Log.d("followed_num", value.get("followed_num").toString());
                            Log.d("followed_num", value.get("intro").toString());

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    followed_num.setText(followed_num_int.toString());
                                    follower_num.setText(follower_num_int.toString());
                                    introduction.setText((String)value.get("intro"));
                                }
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });


            AsyncTask.execute(new Runnable() {
                @Override
                public void run() {
                    final Bitmap bitmap;
                    try {
                        URL endPoint = new URL("http://192.168.10.208:8080");
                        String filePath = member.getMember_profile_pic();
                        URL finalPoint = new URL(endPoint + filePath);

                        HttpURLConnection conn = (HttpURLConnection) finalPoint.openConnection();
                        conn.setDoInput(true);
                        conn.connect();

                        InputStream is = conn.getInputStream();
                        bitmap = BitmapFactory.decodeStream(is);
                        //final RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(), bitmap);
                        //roundedBitmapDrawable.setCircular(true);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //user_image.setImageDrawable(roundedBitmapDrawable);
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

        user_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.commit();

                Intent intent = new Intent(getActivity().getApplicationContext(), Before_Register.class);
                startActivity(intent);
            }
        });

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
