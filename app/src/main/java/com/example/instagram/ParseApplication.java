package com.example.instagram;

import android.app.Application;

import com.example.instagram.models.Post;
import com.parse.Parse;
import com.parse.ParseObject;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //Register your parse models
        ParseObject.registerSubclass(Post.class);

        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.APP_ID)) // should correspond to APP_ID env variable
                .clientKey(getString(R.string.MASTER_KEY))  // set explicitly unless clientKey is explicitly configured on Parse server
                .server(getString(R.string.SERVER_URL)).build());
    }
}
