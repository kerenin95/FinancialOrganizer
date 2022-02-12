package com.example.financialorganizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AnalyticsChoiceActivity extends AppCompatActivity {
    private CardView todaysCardView, weeksCardView, monthsCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics_choice);

        todaysCardView = findViewById(R.id.todaysCardView);
        weeksCardView = findViewById(R.id.weeksCardView);
        monthsCardView = findViewById(R.id.monthsCardView);

        todaysCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalyticsChoiceActivity.this, DailyAnalyticsActivity.class);
                startActivity(intent);
            }
        });

        weeksCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalyticsChoiceActivity.this, ActivityWeeklyAnalytics.class);
                startActivity(intent);
            }
        });

        monthsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AnalyticsChoiceActivity.this, ActivityMonthlyAnalytics.class);
                startActivity(intent);
            }
        });
    }
}