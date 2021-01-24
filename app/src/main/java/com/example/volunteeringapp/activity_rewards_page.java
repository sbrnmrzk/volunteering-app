package com.example.volunteeringapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import org.w3c.dom.Text;

public class activity_rewards_page extends AppCompatActivity implements View.OnClickListener {

    TextView claimNow1, claimNow2;
    ImageView badge1, badge2, badge3;
    Button addrewardbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_page);

        //ImageView badge1 = findViewById(R.id.IV_VerifiedBadge);

        //Bundle bundle = getIntent().getExtras();
        TextView claimNow1 = (TextView) findViewById(R.id.TV_R1ClaimBtn);
        TextView claimNow2 = (TextView) findViewById(R.id.TV_R2ClaimBtn);
        claimNow1.setOnClickListener(this);
        claimNow2.setOnClickListener(this);

        addrewardbtn = findViewById(R.id.BTN_addReward);
        addrewardbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addreward = new Intent(activity_rewards_page.this, activity_rewards_add.class);
                startActivity(addreward);
            }
        });
    }

    //when user click "claim now" the textview change to "claimed"

    public void onClick(View v){

        //if(v.equals(claimNow1)){
            TextView claimNow1 = (TextView)findViewById(R.id.TV_R1ClaimBtn);

            claimNow1.setText("Claimed!");
            claimNow1.setTextColor(Color.GRAY);
        //}
        /**if (v.equals(claimNow2)){
            TextView claimNow2 = (TextView)findViewById(R.id.TV_R2ClaimBtn);

            claimNow2.setText("Claimed!");
            claimNow2.setTextColor(Color.GRAY);
        }*/
}
}