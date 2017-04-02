package com.example.arto.googlemaps;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class RouteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);

        Intent intent = getIntent();

        String uri = intent.getStringExtra("URI");

        //WebView webview = (WebView) this.findViewById(R.id.webview);
        //webview.loadUrl("https://drive.google.com/open?id=134QWrL6_qiR2etC3gZSkBtY0_p8&usp=sharing");


    }
}
