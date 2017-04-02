package com.example.a.harjoitus2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class EmployeesActivity extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employees);

        listView = (ListView) findViewById(R.id.employees_listView);

        List<Employee> employees = null;

        try {
            XMLPullParserHandler parser = new XMLPullParserHandler();
            employees = parser.parse(getAssets().open("employees.xml"));

            ArrayAdapter<Employee> adapter = new ArrayAdapter<Employee>(this, R.layout.employee_list_item, employees);
            listView.setAdapter(adapter);

        } catch (Exception e) {
            Log.e("EmployeesActivity", e.getMessage());
        }
    }
}
