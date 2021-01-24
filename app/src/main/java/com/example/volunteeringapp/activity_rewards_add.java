package com.example.volunteeringapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class activity_rewards_add extends AppCompatActivity {

    Button saverewardbtn, cancelrewardbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_add);

        //Bundle bundle = getIntent().getExtras();

        saverewardbtn = findViewById(R.id.BTN_NewRewardSave);
        saverewardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent savereward = new Intent(activity_rewards_add.this, activity_rewards_page.class);
                startActivity(savereward);
            }
        });

        cancelrewardbtn = findViewById(R.id.BTN_NewRewardCancel);
        cancelrewardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cancelreward = new Intent(activity_rewards_add.this, activity_rewards_page.class);
                startActivity(cancelreward);
            }
        });
    }
}