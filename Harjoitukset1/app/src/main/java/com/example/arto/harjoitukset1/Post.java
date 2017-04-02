package com.example.arto.harjoitukset1;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Post {
    @SerializedName("id")
    long ID;

    @SerializedName("date")
    Date dateCreated;

    String title;
    String author;
    String url;
    String body;
}
