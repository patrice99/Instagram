package com.example.instagram.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.instagram.utils.EndlessRecyclerViewScrollListener;
import com.example.instagram.R;
import com.example.instagram.adapters.PostAdapter;
import com.example.instagram.models.Post;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class PostFragment extends Fragment {
    private static final String TAG = "PostFragment";
    private RecyclerView rvPosts;
    protected PostAdapter adapter;
    protected List<Post> allPosts;
    private SwipeRefreshLayout swipeContainer;
    private EndlessRecyclerViewScrollListener scrollListener;
    private static final int DISPLAY_LIMIT = 20;
    protected ParseUser filterForUser;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_post, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvPosts = view.findViewById(R.id.rvPosts);

        allPosts = new ArrayList<>();
        //instantiate the adapter
        adapter = new PostAdapter(getContext(), allPosts, onClickListener, PostFragment.class.getSimpleName());

        //set the adapter on the recycler view
        rvPosts.setAdapter(adapter);

        //set the layout manager on the recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvPosts.setLayoutManager(linearLayoutManager);
        queryPosts(0);

        swipeContainer = view.findViewById(R.id.swipeContainer);
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


        scrollListener = new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //get the next 20 posts
                queryPosts(page);
            }
        };
        rvPosts.addOnScrollListener(scrollListener);
    }

    //Take the posts we have and hand it over to the adapter
    protected void queryPosts(final int page) {
        Post.query(page, DISPLAY_LIMIT, filterForUser, new FindCallback<Post>() {
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
                    adapter.clear();
                }
                allPosts.addAll(posts);
                adapter.notifyDataSetChanged();
            }
        });
    }

    PostAdapter.onClickListener onClickListener = new PostAdapter.onClickListener() {
        @Override
        public void onProfilePicAction(int position) {
            //get user of that specific post
            ParseUser user = allPosts.get(position).getUser();

            //pass this info to profile fragment
            Fragment fragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            fragment.setArguments(bundle);

            //Go from this fragment to profile fragment
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();


        }

        @Override
        public void onUsernameAction(int position) {
            //get user of that specific post
            ParseUser user = allPosts.get(position).getUser();

            //pass this info to profile fragment
            Fragment fragment = new ProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("user", user);
            fragment.setArguments(bundle);

            //Go from this fragment to profile fragment
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.flContainer, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        @Override
        public void onLikeAction(int position) {
            //update likes count in Post class in Parse
            Post post = allPosts.get(position);
            int likes = (int) post.getNumber("likes");
            likes++;
            post.put("likes", likes);
            post.saveInBackground();
        }

        @Override
        public void onCommentAction(int position) {

        }
    };
}