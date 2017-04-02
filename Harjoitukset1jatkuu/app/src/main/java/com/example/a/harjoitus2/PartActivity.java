package com.example.a.harjoitus2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class PartActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_part);

        listView = (ListView) findViewById(R.id.parts_listView);

        List<Part> parts = null;

        try {
            XMLPartsParser parser = new XMLPartsParser();
            parts = parser.parse(getAssets().open("parts.xml"));

            ArrayAdapter<Part> adapter = new ArrayAdapter<Part>(this, R.layout.list_item_part, parts);
            listView.setAdapter(adapter);

        } catch (Exception e) {
            Log.e("EmployeesActivity", e.getMessage());
        }
    }


}
