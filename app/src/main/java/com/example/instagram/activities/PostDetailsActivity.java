package com.example.instagram.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.models.Post;
import com.parse.ParseFile;

public class PostDetailsActivity extends AppCompatActivity {
    private Post post;
    private ImageView ivPostPic;
    private TextView tvUsername;
    private TextView tvUsername2;
    private TextView tvDescription;
    private TextView tvTimestamp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        //get the intent and post passed in
        post = getIntent().getParcelableExtra("post");

        //define Views
        ivPostPic = findViewById(R.id.ivPostPic);
        tvUsername = findViewById(R.id.tvUsername);
        tvUsername2 = findViewById(R.id.tvUsername2);
        tvDescription= findViewById(R.id.tvDescription);
        tvTimestamp = findViewById(R.id.tvTimestamp);

        tvUsername.setText(post.getUser().getUsername());
        tvUsername2.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        tvTimestamp.setText(post.getCreatedAt().toString());

        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(post.getImage().getUrl()).into(ivPostPic);
        }


    }
}