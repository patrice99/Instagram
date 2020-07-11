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
    private ImageView ivProfilePic;
    private ImageView ivLike;
    private ImageView ivComment;
    private boolean isliked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        //get the intent and post passed in
        post = getIntent().getParcelableExtra("post");
        isliked = getIntent().getBooleanExtra("isLiked", false);


        //define Views
        ivPostPic = findViewById(R.id.ivPostPic);
        tvUsername = findViewById(R.id.tvUsername);
        tvUsername2 = findViewById(R.id.tvUsername2);
        tvDescription= findViewById(R.id.tvDescription);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        ivLike = findViewById(R.id.ivLike);
        ivComment = findViewById(R.id.ivComment);

        tvUsername.setText(post.getUser().getUsername());
        tvUsername2.setText(post.getUser().getUsername());
        tvDescription.setText(post.getDescription());
        tvTimestamp.setText(post.getTimeStamp());



        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(post.getImage().getUrl()).into(ivPostPic);
        }

        //check if the user has a valid profilePic
        ParseFile image2 = post.getUser().getParseFile("profilePic");
        if (image2 != null) {
            Glide.with(this)
                    .load(post.getUser().getParseFile("profilePic").getUrl())
                    .circleCrop()
                    .into(ivProfilePic);
        } else {
            Glide.with(this)
                    .load(getResources().getString(R.string.DEFAULT_PROFILE_PIC))
                    .circleCrop()
                    .into(ivProfilePic);
        }

        if (isliked == true){
            Glide.with(this)
                    .load(R.drawable.ufi_heart_active)
                    .into(ivLike);
        }





    }
}