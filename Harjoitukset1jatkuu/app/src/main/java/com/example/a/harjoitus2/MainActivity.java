package com.example.a.harjoitus2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void launchPostActivity(View view) {
        Intent intent = new Intent(this, PostActivity.class);
        startActivity(intent);
    }

    public void launchXmlActivity(View view) {
        Intent intent = new Intent(this, EmployeesActivity.class);
        startActivity(intent);
    }

    public void launchPartActivity(View view) {
        Intent intent = new Intent(this, PartActivity.class);
        startActivity(intent);
    }
}
