package com.example.a402_24.day_03_register;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.LogPrinter;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tsongkha.spinnerdatepicker.SpinnerDatePickerDialogBuilder;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements  DatePickerDialog.OnDateSetListener {

    final static String LOG_TAG =  "KNKRegisterDoing";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    EditText input_id;
    EditText input_pw;
    EditText input_name;
    RadioButton radioBtn_Male;
    RadioButton radioBtn_Female;
    RadioGroup radioGroup;
    EditText input_birthday;
    Button btn_submit;
    int day, month, year;
    String buttonGender;
    TextView loginText;
    ImageView imageView;
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    TextView TextPath;
    String filePathRaw;

    public void initRefs(){

        input_id = (EditText) findViewById(R.id.input_id);
        input_pw = (EditText) findViewById(R.id.input_pw);
        input_name = (EditText) findViewById(R.id.input_name);
        input_birthday = (EditText) findViewById(R.id.input_birthday);
        radioBtn_Male = (RadioButton) findViewById(R.id.radioBtn_Male);
        radioBtn_Female = (RadioButton) findViewById(R.id.radioBtn_Female);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        imageView = (ImageView) findViewById(R.id.image_view);
        TextPath = (TextView) findViewById(R.id.TextPath);
    }

    public void setEvents(){

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        input_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar c = Calendar.getInstance();
                year = c.get(Calendar.YEAR);
                month = c.get(Calendar.MONTH);
                day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, MainActivity.this, year, month, day);
                datePickerDialog.show();

            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) findViewById(checkedId);
                buttonGender = button.getText().toString();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String id = input_id.getText().toString();
                            String password = input_pw.getText().toString();
                            String gender;
                            Log.d("현재 진행 과정 1","1번 완료");

                            String lineEnd = "\r\n";
                            String boundary = "androidUpload";
                            String twoHyphens = "--";

                            byte[] buffer;
                            int maxBufferSize = 5 * 1024 * 1024;

                            URL endPoint = new URL("http://192.168.10.208:8080/JS/android/register");
                            HttpURLConnection myConnection = (HttpURLConnection) endPoint.openConnection();
                            myConnection.setUseCaches(false);
                            myConnection.setDoOutput(true);
                            myConnection.setRequestMethod("POST");
                            myConnection.setRequestProperty("ENCTYPE", "multipart/form-data");
                            myConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

                            if(buttonGender.equals("남자")){
                                gender = "M";
                            } else {
                                gender = "F";
                            }

                            String name = input_name.getText().toString();
                            String birthday = input_birthday.getText().toString();

                            String delimiter = twoHyphens + boundary + lineEnd;

                            Log.d("LOG_TAG", "delimiter : " + delimiter);

                            DataOutputStream request = new DataOutputStream(myConnection.getOutputStream());

                            // 아이디 집어 넣는 바운더리
                            request.writeBytes(delimiter);
                            request.writeBytes("Content-Disposition: form-data; name=\"id\"" + lineEnd+lineEnd+input_id.getText().toString()+lineEnd);
                            // 비밀번호 집어넣는 바운더리

                            request.writeBytes(delimiter);
                            request.writeBytes("Content-Disposition: form-data; name=\"password\"" + lineEnd+lineEnd+input_pw.getText().toString()+lineEnd);
                            // 성별 집어 넣는 바운더리

                            request.writeBytes(delimiter);
                            request.writeBytes("Content-Disposition: form-data; name=\"gender\"" + lineEnd+lineEnd+gender+lineEnd);
                            // 이름 집어 넣는 바운더리
                            request.writeBytes(delimiter);
                            request.writeBytes("Content-Disposition: form-data; name=\"name\"" + lineEnd+lineEnd+input_name.getText().toString() +lineEnd);
                            // 생일 집어 넣는 바운더리
                            request.writeBytes(delimiter);
                            request.writeBytes("Content-Disposition: form-data; name=\"birthday\"" + lineEnd+lineEnd+input_birthday.getText().toString() +lineEnd);
                            // 멀티 파트 데이터 집어 넣는 바운더리
                            request.writeBytes(delimiter);
                            request.writeBytes("Content-Disposition: form-data; name=\"image\";filename=\""+ filePathRaw + "\"" + lineEnd);


                            if(filePathRaw!=null){
                                request.writeBytes(lineEnd);
                                FileInputStream fStream = new FileInputStream(filePathRaw);
                                buffer = new byte[maxBufferSize];
                                int length = -1;
                                while((length=fStream.read(buffer)) != -1){
                                    request.write(buffer,0, length);
                                }
                                request.writeBytes(lineEnd);
                                request.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);
                                fStream.close();
                            }

                            request.flush();
                            request.close();

                            if(myConnection.getResponseCode()==200)
                                finish();

                        } catch (Exception e) {
                            Log.d(LOG_TAG, e.getMessage());
                        }
                    }
                });
            }
        });
    }

    private void selectImage(){

        final CharSequence [] list  = {"Camera", "Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Add Image");
        builder.setItems(list, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(list[which].equals("Camera")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if(list[which].equals("Gallery")){
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType(("image/*"));
                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if(list[which].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            if(requestCode==REQUEST_CAMERA){
                Bundle bundle = data.getExtras();
                final Bitmap bmp = (Bitmap) bundle.get("data");
                imageView.setImageBitmap(bmp);
            } else if(requestCode==SELECT_FILE){
                Uri selectedImageUri = data.getData();
                imageView.setImageURI(selectedImageUri);
                getRealPathURI(selectedImageUri);
                filePathRaw = getRealPathURI(selectedImageUri).toString();
                Log.d("selectedImageUri", selectedImageUri.toString());
                Log.d("getRealPathURI", getRealPathURI(selectedImageUri));
            }
        }
    }

    private String getRealPathURI(Uri contentUri){
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        cursor.moveToFirst();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        return cursor.getString(column_index);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initRefs();
        setEvents();



        int permission_internet =
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
        if(permission_internet == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},112);
        }

        int permission_read_external_storage =
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if(permission_read_external_storage == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},113);
        }

        int permission_write_external_storage =
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permission_write_external_storage == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},114);
        }

        int permission_camara =
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
        if(permission_camara == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA},115);
        }

        int permission_media_content_control =
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.MEDIA_CONTENT_CONTROL);
        if(permission_media_content_control == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.MEDIA_CONTENT_CONTROL},116);
        }


    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
      Calendar c = Calendar.getInstance();
      c.set(Calendar.YEAR, year);
      c.set(Calendar.MONTH, month);
      c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

      String currentDateString = DateFormat.getDateInstance().format(c.getTime());

       SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
       String dateFinal =  date.format(new java.util.Date(currentDateString));

       input_birthday.setText(dateFinal);
    }
}
