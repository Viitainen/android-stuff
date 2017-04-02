package com.example.arto.harjoitukset1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class JSONFileActivity extends AppCompatActivity {

    private String TAG = JSONFileActivity.class.getSimpleName();
    private ListView lv;
    private ArrayList<HashMap<String, String>> kingsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonfile);

        lv = (ListView) findViewById(R.id.kings_list);
        kingsList = new ArrayList<HashMap<String, String>>();
        Toast.makeText(JSONFileActivity.this, "Loading kings, hold up!", Toast.LENGTH_SHORT).show();
        GetKings();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> king = kingsList.get(position);

                Intent intent = new Intent(JSONFileActivity.this, KingActivity.class);
                intent.putExtra("name", king.get("name"));
                intent.putExtra("years", king.get("years"));
                //Toast.makeText(JSONFileActivity.this, kingsList.get(position).get("name"), Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

    }



    private void GetKings() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray kings = obj.getJSONArray("kings");

            for(int i = 0; i < kings.length(); i++) {
                JSONObject king = kings.optJSONObject(i);
                String name = king.getString("nm");
                String years = king.getString("yrs");
                HashMap<String, String> k = new HashMap<>();
                k.put("name", name);
                k.put("years", years);
                kingsList.add(k);
            }

            ListAdapter adapter = new SimpleAdapter(JSONFileActivity.this, kingsList, R.layout.king_list_item, new String[] { "name", "years" }, new int[] { R.id.name, R.id.years});
            lv.setAdapter(adapter);

        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
            Toast.makeText(JSONFileActivity.this, "Could not load data.", Toast.LENGTH_LONG).show();
        }
    }

    private String loadJSONFromAsset() {
        String json = null;

        try {
            InputStream is = getAssets().open("kings.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);

            is.close();
            json = new String(buffer, "UTF-8");

        } catch(Exception e) {
            Log.e(TAG, e.getMessage());
            return null;
        }

        return json;

    }
}
