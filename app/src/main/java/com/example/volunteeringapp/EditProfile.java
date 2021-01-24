package com.example.volunteeringapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.regex.Pattern;

public class EditProfile extends AppCompatActivity {

    ImageView cover_photo;
    DBHelper DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        TextView name = (TextView) findViewById(R.id.EP_name);
        TextView email = (TextView) findViewById(R.id.EP_emailAddress);
        TextView password = (TextView) findViewById(R.id.EP_password);
        Button btn_uploadPhoto = (Button) findViewById(R.id.btn_uploadPhoto2);
        cover_photo = (ImageView) findViewById(R.id.IV_CoverPhoto2);

        SessionManagement sessionManagement = new SessionManagement(EditProfile.this);
        String current_user = Integer.toString(sessionManagement.getSession());
        int userID = sessionManagement.getSession();

        DB = new DBHelper(this);

        btn_uploadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Cursor GetUserByID = DB.getUserById(current_user);
                Boolean CheckPicture = DB.checkProfilePicture(current_user);

                if(!CheckPicture){
                    if (GetUserByID != null && GetUserByID.getCount() > 0) {
                        GetUserByID.moveToFirst();
                        name.setText(GetUserByID.getString(1));
                        email.setText(GetUserByID.getString(2));
                        password.setText(GetUserByID.getString(3));
                        cover_photo.setImageResource(R.drawable.defaulticon);
                    }
                } else {
                    if (GetUserByID != null && GetUserByID.getCount() > 0) {
                        GetUserByID.moveToFirst();
                        byte[] blob = GetUserByID.getBlob(GetUserByID.getColumnIndex("profilePicture"));
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(blob);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                        name.setText(GetUserByID.getString(1));
                        email.setText(GetUserByID.getString(2));
                        password.setText(GetUserByID.getString(3));
                        cover_photo.setImageBitmap(bitmap);
                    }
                }
            }
        });
    }

    public boolean discard(View view){
        onBackPressed();
        return true;
    }

    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public void saveChanges(View view) {
        TextView name = (TextView) findViewById(R.id.EP_name);
        TextView email = (TextView) findViewById(R.id.EP_emailAddress);
        TextView password = (TextView) findViewById(R.id.EP_password);
        Bitmap b_cover_photo = ((BitmapDrawable) cover_photo.getDrawable()).getBitmap();
        String nama = name.getText().toString();
        String emel = email.getText().toString();
        String kunci = password.getText().toString();

        SessionManagement sessionManagement = new SessionManagement(EditProfile.this);
        String current_user = Integer.toString(sessionManagement.getSession());
        int userID = sessionManagement.getSession();

        if (!isValid(emel)) {
            Toast.makeText(EditProfile.this, "Please enter correct email syntax.", Toast.LENGTH_SHORT).show();
        } else {
            if (nama.equals("") || emel.equals("") || kunci.equals(""))
                Toast.makeText(EditProfile.this, "Please enter all the fields", Toast.LENGTH_SHORT).show();
            else {
                Boolean updateProfile = DB.updateProfile(userID, nama, emel, kunci, b_cover_photo);
                Boolean updateUser = DB.updateUser(userID, nama, emel, kunci);
                if (!updateProfile && !updateUser) {
                    Toast.makeText(EditProfile.this, "Successfully saved!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditProfile.this, "Failed to save!", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    private void selectImage() {
        if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(EditProfile.this,new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE
            },1);
        }
        final CharSequence[] options = {"Choose from Gallery","Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(EditProfile.this);
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
}
