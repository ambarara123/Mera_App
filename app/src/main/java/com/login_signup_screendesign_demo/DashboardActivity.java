package com.login_signup_screendesign_demo;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Toast;

public class DashboardActivity extends BaseActivity {

    CardView cardDashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        cardDashboard = (CardView) findViewById(R.id.cardView);


        //card view onclick Listener
        cardDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(DashboardActivity.this,DetailedDashboardActivity.class));
                Toast.makeText(DashboardActivity.this, "Welcome to Detailed Dashboard activity", Toast.LENGTH_SHORT).show();

            }
        });

    }

}