package com.example.instagram.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.example.instagram.R;
import com.example.instagram.models.Post;

public class PostDetailsActivity extends AppCompatActivity {
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        //get the intent and post passed in
        post = getIntent().getParcelableExtra("post");

        //define Views

    }
}