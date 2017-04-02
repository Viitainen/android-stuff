package com.example.arto.harjoitukset1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class KingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_king);

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String years = intent.getStringExtra("years");

        TextView nameTv = (TextView) findViewById(R.id.king_name);
        TextView yearsTv = (TextView) findViewById(R.id.king_years);

        nameTv.setText(name);
        yearsTv.setText(years);

    }
}
