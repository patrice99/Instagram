package com.example.instagram.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.instagram.EndlessRecyclerViewScrollListener;
import com.example.instagram.R;
import com.example.instagram.adapters.PostAdapter;
import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private ParseUser user;
    private RecyclerView rvUserPosts;
    private List<Post> userPosts;
    private PostAdapter userAdapter;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private static final int DISPLAY_LIMIT = 20;
    public static final String TAG = ProfileFragment.class.getSimpleName();
    private ImageView ivProfilePic;
    private TextView tvBio;
    private TextView tvUsername;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvBio = view.findViewById(R.id.tvUsername);
        tvUsername = view.findViewById(R.id.tvUsername);

        //Get the bundle to determine user
        Bundle bundle = this.getArguments();
        if (bundle == null){
            user = ParseUser.getCurrentUser();
        } else {
            user = bundle.getParcelable("user");
        }

        tvUsername.setText(user.getUsername());
        ParseFile image = user.getParseFile("profilePic");
        if (image == null){
            Glide.with(getContext())
                    .load(getResources().getString(R.string.DEFAULT_PROFILE_PIC))
                    .circleCrop()
                    .into(ivProfilePic);
        } else {
            Glide.with(getContext())
                    .load(image.getUrl())
                    .circleCrop()
                    .into(ivProfilePic);
        }


        rvUserPosts = view.findViewById(R.id.rvPosts);

        userPosts = new ArrayList<>();
        //instantiate the adapter
        userAdapter = new PostAdapter(getContext(), userPosts, new PostAdapter.onClickListener() {
            @Override
            public void onProfilePicAction(int position) {
                //do nothing
            }

            @Override
            public void onUsernameAction(int position) {
                //do nothing
            }
        }, ProfileFragment.class.getSimpleName());

        //set the adapter on the recycler view
        rvUserPosts.setAdapter(userAdapter);

        //set the layout manager on the recycler view
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 3);
        rvUserPosts.setLayoutManager(gridLayoutManager);
        queryPosts(0);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //get latest 20 Instagram items
                queryPosts(0);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Stop animation (This will be after 2 seconds)
                        swipeContainer.setRefreshing(false);
                    }
                }, 2000); //Delay in millis

            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        scrollListener = new EndlessRecyclerViewScrollListener(gridLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //get the next 20 posts
                queryPosts(page);
            }
        };
        rvUserPosts.addOnScrollListener(scrollListener);
    }

    //Take the posts we have and hand it over to the adapter
    protected void queryPosts(final int page) {
        Post.query(page, DISPLAY_LIMIT, user, new FindCallback<Post>() {
            @Override
            public void done(List<Post> posts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "Issue with getting posts", e);
                    return;

                }
                for(Post post: posts){
                    Log.i(TAG, "Post: " + post.getDescription() + " Username: " + post.getUser().getUsername());
                }
                if(page == 0) {
                    userAdapter.clear();
                }
                userPosts.addAll(posts);
                userAdapter.notifyDataSetChanged();
            }
        });
    }

}

