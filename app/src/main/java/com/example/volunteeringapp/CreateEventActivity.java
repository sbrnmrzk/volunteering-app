package com.example.volunteeringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity {

    EditText ET_EventTitle, ET_Description, ET_Location, ET_Capacity, ET_StartDate, ET_StartTime, ET_EndTime;
    Button btn_createEvent, btn_uploadPhoto;
    DBHelper DB;
    TimePickerDialog picker_start, picker_end;
    ImageView cover_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

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
        cover_photo.setVisibility(View.GONE);

        Toolbar createEvent_toolbar = (Toolbar) findViewById(R.id.createEvent_toolbar);
        setSupportActionBar(createEvent_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        btn_uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
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
                picker_start = new TimePickerDialog(CreateEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                ET_StartTime.setText(sHour + ":" + sMinute);
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
                picker_end = new TimePickerDialog(CreateEventActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker tp, int sHour, int sMinute) {
                                ET_EndTime.setText(sHour + ":" + sMinute);
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

                SessionManagement sessionManagement = new SessionManagement(CreateEventActivity.this);
                String organizer_id = Integer.toString(sessionManagement.getSession());

                Bitmap b_cover_photo = ((BitmapDrawable)cover_photo.getDrawable()).getBitmap();

                // String location_lat
                // String location_long
                // String rewards?

                if(event_title.equals("")||description.equals(""))
                    Toast.makeText(CreateEventActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else {
                    Boolean createEvent = DB.createEvent(event_title, description, capacity, location, start_date, start_time, end_time, organizer_id, b_cover_photo);
                    if (createEvent == true) {
                        Toast.makeText(CreateEventActivity.this, "Registered successfully.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), ManageEventsActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(CreateEventActivity.this, "Event creation failed.", Toast.LENGTH_SHORT).show();
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
                new DatePickerDialog(CreateEventActivity.this, date, myCalendar
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
            ActivityCompat.requestPermissions(CreateEventActivity.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);
        }
        final CharSequence[] options = {"Choose from Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateEventActivity.this);
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
        Toast.makeText(CreateEventActivity.this, "Rewards are not available yet.", Toast.LENGTH_SHORT).show();
        return true;
    }
}