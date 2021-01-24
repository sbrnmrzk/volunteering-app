package com.example.volunteeringapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.Cursor;
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

public class DisplayRewardsActivity extends AppCompatActivity implements View.OnClickListener {

    TextView claimNow1, claimNow2, TV_NoBadge;

    ImageView badge1, badge2, badge3, badge4;
    Button addrewardbtn;
    DBHelper DB;
    Integer userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rewards_page);

        badge1 = (ImageView) findViewById(R.id.IV_VerifiedBadge);
        badge2 = (ImageView) findViewById(R.id.IV_GiverBadge);
        badge3 = (ImageView) findViewById(R.id.IV_BloodDBadge);
        TV_NoBadge = (TextView) findViewById(R.id.TV_NoBadge);

        badge1.setVisibility(View.INVISIBLE);
        badge2.setVisibility(View.INVISIBLE);
        badge3.setVisibility(View.INVISIBLE);
        TV_NoBadge.setVisibility(View.INVISIBLE);



        SessionManagement sessionManagement = new SessionManagement(DisplayRewardsActivity.this);
        userId = sessionManagement.getSession();
        DB = new DBHelper(this);

        Cursor res = DB.getEventHistory(userId, "JOINED");
        Integer totalEvents = res.getCount();

        if (totalEvents == 0) {
            TV_NoBadge.setVisibility(View.VISIBLE);
        } else {
            if (totalEvents > 0) {
                badge1.setVisibility(View.VISIBLE);
            }
            if (totalEvents > 1) {
                badge2.setVisibility(View.VISIBLE);
            }
            if (totalEvents > 2) {
                badge3.setVisibility(View.VISIBLE);
            }
        }

        //Bundle bundle = getIntent().getExtras();
        TextView claimNow1 = (TextView) findViewById(R.id.TV_R1ClaimBtn);
        TextView claimNow2 = (TextView) findViewById(R.id.TV_R2ClaimBtn);
        claimNow1.setOnClickListener(this);
        claimNow2.setOnClickListener(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.rewardsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    //when user click "claim now" the textview change to "claimed"

    public void onClick(View v) {

            TextView claimNow1 = (TextView)findViewById(R.id.TV_R1ClaimBtn);

            claimNow1.setText("Claimed!");
            claimNow1.setTextColor(Color.GRAY);

        /**if (v.equals(claimNow2)){
            TextView claimNow2 = (TextView)findViewById(R.id.TV_R2ClaimBtn);

            claimNow2.setText("Claimed!");
            claimNow2.setTextColor(Color.GRAY);
        }*/
}

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
