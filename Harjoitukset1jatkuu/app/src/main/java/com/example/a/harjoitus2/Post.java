package com.example.a.harjoitus2;

import java.util.Date;
import com.google.gson.annotations.SerializedName;


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
