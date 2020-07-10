package com.example.instagram.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.parse.ParseFile;
import com.parse.ParseUser;

public class EditProfileActivity extends AppCompatActivity {
    private ParseUser user;
    private ImageView ivProfilePic;
    private TextView tvChangePhoto;
    private EditText etName;
    private EditText etUsername;
    private EditText etBio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //get intent
        user = getIntent().getParcelableExtra("user");

        //find views
        ivProfilePic = findViewById(R.id.ivProfilePic);
        tvChangePhoto = findViewById(R.id.tvChangePhoto);
        etName = findViewById(R.id.etName);
        etUsername = findViewById(R.id.etUsername);
        etBio = findViewById(R.id.etBio);

        //bind views with data from user
        ParseFile image = user.getParseFile("profilePic");
        Glide.with(this)
                .load(image.getUrl())
                .circleCrop()
                .into(ivProfilePic);


        String name = user.getString("name");
        if (name != null){
            etName.setText(name);
        }

        etUsername.setText(user.getUsername());

        String bio = user.getString("bio");
        if (bio != null){
            etBio.setText(bio);
        }


    }
}