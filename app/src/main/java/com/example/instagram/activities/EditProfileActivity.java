package com.example.instagram.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.instagram.R;
import com.parse.ParseUser;

public class EditProfileActivity extends AppCompatActivity {
    ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //get intent
        user = getIntent().getParcelableExtra("user");
    }
}