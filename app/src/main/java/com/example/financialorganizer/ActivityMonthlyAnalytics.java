package com.example.financialorganizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.anychart.AnyChartView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityMonthlyAnalytics extends AppCompatActivity {

    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private String onlineUserId = "";
    private DatabaseReference expensesRef, personalRef;

    private TextView totalAmountSpentOn, analyticsTransportationAmount, analyticsFoodAmount, analyticsRentAmount;
    private TextView analyticsEntertainmentAmount, analyticsHealthAmount, analyticsOtherAmount, monthSpentAmount, monthRatioSpending;

    private RelativeLayout relativeLayoutTransport, relativeLayoutFood, relativeLayoutRent, relativeLayoutEntertainment;
    private RelativeLayout relativeLayoutHealth, relativeLayoutOther, linearLayoutAnalysis;

    private AnyChartView anyChartView;
    private ImageView transportation_status, food_status, rent_status, entertainment_status, health_status, other_status, monthRatioSpending_Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_analytics);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("This Months Analytics");

        mAuth = FirebaseAuth.getInstance();
        onlineUserId = mAuth.getCurrentUser().getUid();
        expensesRef = FirebaseDatabase.getInstance().getReference("expenses").child(onlineUserId);
        personalRef = FirebaseDatabase.getInstance().getReference("personal").child(onlineUserId);

        //TextView initialization
        totalAmountSpentOn = findViewById(R.id.totalAmountSpentOn);
        analyticsTransportationAmount = findViewById(R.id.analyticsTransportationAmount);
        analyticsFoodAmount = findViewById(R.id.analyticsFoodAmount);
        analyticsRentAmount = findViewById(R.id.analyticsRentAmount);
        analyticsEntertainmentAmount = findViewById(R.id.analyticsEntertainmentAmount);
        analyticsHealthAmount = findViewById(R.id.analyticsHealthAmount);
        analyticsOtherAmount = findViewById(R.id.analyticsOtherAmount);
        monthSpentAmount = findViewById(R.id.monthSpentAmount);
        monthRatioSpending = findViewById(R.id.monthRatioSpending);

        //RelativeLayout initialization
        relativeLayoutTransport = findViewById(R.id.relativeLayoutTransport);
        relativeLayoutFood = findViewById(R.id.relativeLayoutFood);
        relativeLayoutRent = findViewById(R.id.relativeLayoutRent);
        relativeLayoutEntertainment = findViewById(R.id.relativeLayoutEntertainment);
        relativeLayoutHealth = findViewById(R.id.relativeLayoutHealth);
        relativeLayoutOther = findViewById(R.id.relativeLayoutOther);
        linearLayoutAnalysis = findViewById(R.id.linearLayoutAnalysis);

        //AnyChartView initialization
        anyChartView = findViewById(R.id.anyChartView);

        //ImageView initialization
        transportation_status = findViewById(R.id.transportation_status);
        food_status = findViewById(R.id.food_status);
        rent_status = findViewById(R.id.rent_status);
        entertainment_status = findViewById(R.id.entertainment_status);
        health_status = findViewById(R.id.health_status);
        other_status = findViewById(R.id.other_status);
        monthRatioSpending_Image = findViewById(R.id.monthRatioSpending_Image);
    }
}