package com.example.instagram.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class ProfileFragment extends PostFragment {

    public static final String TAG = "ProfileFragment";

    // DRY
    // dont repeat yourself

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        filterForUser = ParseUser.getCurrentUser();
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void queryPosts(int page) {
        super.queryPosts(page);
    }
}
