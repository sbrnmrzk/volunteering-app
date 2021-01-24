package com.example.volunteeringapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CreateRewardsActivity extends AppCompatActivity {

    Button saverewardbtn, cancelrewardbtn;
    EditText ET_RewardName, ET_RewardDescription, ET_RewardCodeNumber;
    ImageView cover_photo;
    Button BTN_NewRewardPhoto;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_add);

        Toolbar createEvent_toolbar = (Toolbar) findViewById(R.id.addrewardToolbar);
        setSupportActionBar(createEvent_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ET_RewardName = (EditText) findViewById(R.id.ET_RewardName);
        ET_RewardDescription = (EditText) findViewById(R.id.ET_RewardDescription);
        ET_RewardCodeNumber = (EditText) findViewById(R.id.ET_RewardCodeNumber);
        BTN_NewRewardPhoto = (Button) findViewById(R.id.BTN_NewRewardPhoto);
        cover_photo = (ImageView) findViewById(R.id.iv_rewardsphoto);
        DB = new DBHelper(this);
        cover_photo.setVisibility(View.GONE);
        //Bundle bundle = getIntent().getExtras();

        saverewardbtn = findViewById(R.id.BTN_NewRewardSave);
        saverewardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String rewardName = ET_RewardName.getText().toString();
                String rewardDescription = ET_RewardDescription.getText().toString();
                String rewardCodeNumber = ET_RewardCodeNumber.getText().toString();

                // String rewards?

                if( rewardName.equals("")||rewardDescription.equals("")||rewardCodeNumber.equals(""))
                    Toast.makeText(CreateRewardsActivity.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
                else {
                    Bitmap b_cover_photo = ((BitmapDrawable)cover_photo.getDrawable()).getBitmap();
                    Boolean createReward = DB.createReward(rewardName, rewardDescription, rewardCodeNumber, b_cover_photo);
                    if (createReward) {
                        Cursor getIdOfLastReward = DB.getAllRewards();
                        getIdOfLastReward.moveToLast();
                        String rewardId = getIdOfLastReward.getString(getIdOfLastReward.getColumnIndex("ID"));

                        Toast.makeText(CreateRewardsActivity.this, "Created reward successfully.", Toast.LENGTH_SHORT).show();
                        Intent data = new Intent();

                        data.putExtra("rewardId", rewardId);
                        data.putExtra("requestCode", 3);
                        setResult(RESULT_OK, data);
                        finish();
                    } else {
                        Toast.makeText(CreateRewardsActivity.this, "Event creation failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        cancelrewardbtn = findViewById(R.id.BTN_NewRewardCancel);
        cancelrewardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        BTN_NewRewardPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



    private void selectImage() {

        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(CreateRewardsActivity.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);
        }
        final CharSequence[] options = {"Choose from Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(CreateRewardsActivity.this);
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
        if(data.getStringArrayExtra("requestCode") != null ){
            requestCode = Integer.parseInt(data.getStringExtra("requestCode"));
        }
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
}