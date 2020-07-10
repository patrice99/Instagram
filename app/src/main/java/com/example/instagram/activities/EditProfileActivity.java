package com.example.instagram.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditProfileActivity extends AppCompatActivity {
    private ParseUser user;
    private ImageView ivProfilePic;
    private TextView tvChangePhoto;
    private EditText etName;
    private EditText etUsername;
    private EditText etBio;
    private Button btnDone;


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
        btnDone = findViewById(R.id.btnDone);

        //bind views with data from user
        //check to see if user has image
        ParseFile image = user.getParseFile("profilePic");
        if (image != null){
            Glide.with(this)
                    .load(image.getUrl())
                    .circleCrop()
                    .into(ivProfilePic);
        } else {
            tvChangePhoto.setText("Add Profile Pic");
            Glide.with(this)
                    .load(getResources().getString(R.string.DEFAULT_PROFILE_PIC))
                    .circleCrop()
                    .into(ivProfilePic);
        }


        String name = user.getString("name");
        if (name != null){
            etName.setText(name);
        }

        etUsername.setText(user.getUsername());

        String bio = user.getString("bio");
        if (bio != null){
            etBio.setText(bio);
        }

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //save all new attributes of user to Parse
                user.setUsername(etUsername.getText().toString());
                user.put("name", etName.getText().toString());
                user.put("bio", etBio.getText().toString());
                user.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        finish();
                    }
                });

            }
        });

        ivProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePic();
            }
        });

        tvChangePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePic();
            }
        });




    }

    private void changePic() {
        //code for user to select a photo from their gallery or to take a picture

    }
}