package com.example.volunteeringapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
                    Boolean createReward = DB.createReward(rewardName, rewardDescription, rewardCodeNumber, null);
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
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}