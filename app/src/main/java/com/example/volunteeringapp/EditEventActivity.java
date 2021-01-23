package com.example.volunteeringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditEventActivity extends AppCompatActivity {

    EditText ET_EventTitle, ET_Description, ET_Location, ET_Capacity, ET_StartDate, ET_StartTime, ET_EndTime;
    Button btn_createEvent, btn_uploadPhoto;
    DBHelper DB;
    TimePickerDialog picker_start, picker_end;
    ImageView cover_photo;
    final Fragment mapF = new MapsFragment();
    Fragment active = mapF;
    final FragmentManager fm = getSupportFragmentManager();
    String lat;
    String lat2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        DB = new DBHelper(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        fm.beginTransaction().add(R.id.frameFragment, mapF, "1").commit();

        ET_EventTitle = (EditText) findViewById(R.id.ET_EventTitle);
        ET_Description = (EditText) findViewById(R.id.ET_Description);
        ET_Location = (EditText) findViewById(R.id.ET_Location);
        ET_Capacity = (EditText) findViewById(R.id.ET_Capacity);
        ET_StartDate = (EditText) findViewById(R.id.ET_StartDate);
        ET_StartTime = (EditText) findViewById(R.id.ET_StartTime);
        ET_EndTime = (EditText) findViewById(R.id.ET_EndTime);
        // photo not available yet
        // rewards not available yet
        btn_uploadPhoto = (Button) findViewById(R.id.btn_uploadPhoto);
        btn_createEvent = (Button) findViewById(R.id.btn_createEvent);
        cover_photo = (ImageView) findViewById(R.id.IV_CoverPhoto);
        DB = new DBHelper(this);
//        cover_photo.setVisibility(View.GONE);

        Toolbar editEvent_toolbar = (Toolbar) findViewById(R.id.editEvent_toolbar);
        setSupportActionBar(editEvent_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        String event_id = getIntent().getStringExtra("event_id");

        Cursor res = DB.getEventById(Integer.valueOf(event_id));
        if(res.getCount()==0){
            Toast.makeText(EditEventActivity.this, "No Entry Exists", Toast.LENGTH_SHORT).show();
            return;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
//            Toast.makeText(EditEventActivity.this, res.getString(res.getColumnIndex("location")), Toast.LENGTH_SHORT).show();
            ET_EventTitle.setText(res.getString(res.getColumnIndex("event_title")));
            ET_Description.setText(res.getString(res.getColumnIndex("description")));
            ET_Capacity.setText(res.getString(res.getColumnIndex("capacity")));
            ET_StartDate.setText(res.getString(res.getColumnIndex("start_date")));
            ET_StartTime.setText(res.getString(res.getColumnIndex("start_time")));
            ET_EndTime.setText(res.getString(res.getColumnIndex("end_time")));
            ET_Location.setText(res.getString(res.getColumnIndex("location")));

//            byte[] image = res.getBlob(res.getColumnIndex("cover_photo"));
//            Bitmap bmp= BitmapFactory.decodeByteArray(image, 0 , image.length);
//            cover_photo.setImageBitmap(bmp);

            byte[] blob = res.getBlob(res.getColumnIndex("cover_photo"));
            ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            cover_photo.setImageBitmap(bitmap);

            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    MapsFragment fragment = (MapsFragment) getSupportFragmentManager().findFragmentById(R.id.frameFragment);
                    String address = ET_Location.getText().toString();
                    fragment.locationSearch(address);


                }
            }, 1000);
        }


        btn_uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        ET_Location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {
                    String address = ET_Location.getText().toString();
                    MapsFragment fragment = (MapsFragment) getSupportFragmentManager().findFragmentById(R.id.frameFragment);
                    fragment.locationSearch(address);
                    lat = fragment.latGet();
                    lat2 = fragment.longGet();
                }
            }
        });

        ET_StartTime.setInputType(InputType.TYPE_NULL);
        ET_StartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);
                // time picker dialog
                picker_start = new TimePickerDialog(EditEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                int pm = sHour % 12;
                                ET_StartTime.setText(String.format("%02d:%02d %s", pm == 0 ? 12 : pm,
                                        sMinute, sHour < 12 ? "AM" : "PM"));
                            }
                        }, hour, minutes, false);
                picker_start.show();
            }
        });

        ET_EndTime.setInputType(InputType.TYPE_NULL);
        ET_EndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int hour = cldr.get(Calendar.HOUR_OF_DAY);
                int minutes = cldr.get(Calendar.MINUTE);

                // time picker dialog
                picker_end = new TimePickerDialog(EditEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                int pm = sHour % 12;
                                ET_EndTime.setText(String.format("%02d:%02d %s", pm == 0 ? 12 : pm,
                                        sMinute, sHour < 12 ? "AM" : "PM"));
                            }
                        }, hour, minutes, false);
                picker_end.show();
            }
        });

        btn_createEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(CreateEventActivity.this, ET_StartTime.getText().toString() + " + " + ET_EndTime.getText().toString(), Toast.LENGTH_SHORT).show();

                String event_title = ET_EventTitle.getText().toString();
                String description = ET_Description.getText().toString();
                String capacity = ET_Capacity.getText().toString();
                String location = ET_Location.getText().toString();
                String start_date = ET_StartDate.getText().toString();
                String start_time = ET_StartTime.getText().toString();
                String end_time = ET_EndTime.getText().toString();

                SessionManagement sessionManagement = new SessionManagement(EditEventActivity.this);
                Integer organizer_id = sessionManagement.getSession();

                Bitmap b_cover_photo = ((BitmapDrawable)cover_photo.getDrawable()).getBitmap();
                // String rewards?

                if(event_title.equals("")||description.equals(""))
                    Toast.makeText(EditEventActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else {
                    Boolean createEvent = DB.createEvent(event_title, description, capacity, location, start_date, start_time, end_time, organizer_id, b_cover_photo, lat, lat2);
                    if (createEvent == true) {
                        Toast.makeText(EditEventActivity.this, "Registered successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ManageEventsActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(EditEventActivity.this, "Event creation failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        final Calendar myCalendar = Calendar.getInstance();

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "dd MMM yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                ET_StartDate.setText(sdf.format(myCalendar.getTime()));
            }
        };

        ET_StartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(EditEventActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds options to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    private void selectImage() {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditEventActivity.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);
        }
        final CharSequence[] options = {"Choose from Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditEventActivity.this);
        builder.setTitle("Upload Cover Photo");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
//                if (options[item].equals("Take Photo"))
//                {
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
//                    startActivityForResult(intent, 1);
//                }
                if (options[item].equals("Choose from Gallery"))
                {
                    Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();




    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==1){
            if(grantResults[0]==PackageManager.PERMISSION_GRANTED){

            }
            else{
                Toast.makeText(getApplicationContext(), "Permission Denied", Toast.LENGTH_LONG).show();
            }
        }
    }
    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
//                for (File temp : f.listFiles()) {
//                    if (temp.getName().equals("temp.jpg")) {
//                        f = temp;
//                        break;
//                    }
//                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            bitmapOptions);
                    cover_photo.setImageBitmap(bitmap);
                    String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage,filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                Log.w("path of image from gallery......******************.........", picturePath+"");
                cover_photo.setImageBitmap(thumbnail);
                cover_photo.setVisibility(View.VISIBLE);

            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public boolean rewards(View view){
        Toast.makeText(EditEventActivity.this, "Rewards are not available yet.", Toast.LENGTH_SHORT).show();
        return true;
    }
}