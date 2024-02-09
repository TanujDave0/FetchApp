package com.example.fetch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        findViewById(R.id.back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                go_back(view);
            }

        });
    }

    public void go_back(View view) {
//        Intent myIntent = new Intent(this, MainActivity.class);
//        startActivity(myIntent);
        finish();
    }
}