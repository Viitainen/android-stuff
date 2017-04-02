package com.example.arto.harjoitukset1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void startContactsActivity(View view) {
        Intent intent = new Intent(this, ContactsActivity.class);
        startActivity(intent);

    }

    public void startJSONFileActivity(View view) {
        Intent intent = new Intent(this, JSONFileActivity.class);
        startActivity(intent);
    }

    public void startGSONActivity(View view) {
        Intent intent = new Intent(this, GSONActivity.class);
        startActivity(intent);
    }
}
