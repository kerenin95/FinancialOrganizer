package com.example.financialorganizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView weeklyBtnImageView, dailyBtnImageView, budgetBtnImageView, monthlyBtnImageView, analyticsBtnImageView;
    private CardView dayCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dayCardView = findViewById(R.id.dayCardView);

        weeklyBtnImageView = findViewById(R.id.weeklyBtnImageView);
        dailyBtnImageView = findViewById(R.id.dailyBtnImageView);
        budgetBtnImageView = findViewById(R.id.budgetBtnImageView);
        monthlyBtnImageView = findViewById(R.id.monthlyBtnImageView);
        analyticsBtnImageView = findViewById(R.id.analyticsBtnImageView);

        dayCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TodaysSpending.class);
            }
        });


        budgetBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Budget.class);
                startActivity(intent);
            }
        });

        dailyBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TodaysSpending.class);
                startActivity(intent);
            }
        });

        weeklyBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WeeklySpendingActivity.class);
                intent.putExtra("type", "week");
                startActivity(intent);
            }
        });

        monthlyBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WeeklySpendingActivity.class);
                intent.putExtra("type", "month");
                startActivity(intent);
            }
        });

        analyticsBtnImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AnalyticsChoiceActivity.class);
                startActivity(intent);
            }
        });
    }
}