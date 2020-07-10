package com.example.instagram.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.R;
import com.example.instagram.activities.PostDetailsActivity;
import com.example.instagram.fragments.PostFragment;
import com.example.instagram.fragments.ProfileFragment;
import com.example.instagram.models.Post;
import com.parse.ParseFile;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private Context context;
    private List<Post> posts;
    private onClickListener clickListener;
    String fromWhere;

    public interface onClickListener {
        void onProfilePicAction(int position);
        void onUsernameAction(int position);
    }


    public PostAdapter(Context context, List<Post> posts, onClickListener clickListener, String fromWhere) {
        this.context = context;
        this.posts = posts;
        this.clickListener = clickListener;
        this.fromWhere = fromWhere;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        if (fromWhere.equals(PostFragment.class.getSimpleName())){
            view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        } else if (fromWhere.equals(ProfileFragment.class.getSimpleName())){
            view = LayoutInflater.from(context).inflate(R.layout.item_user_post, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvUsername;
        private TextView tvUsername2;
        private ImageView ivImage;
        private TextView tvDescription;
        private ImageView ivProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //find the views
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvUsername2 = itemView.findViewById(R.id.tvUsername2);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            itemView.setOnClickListener(this);
        }

        public void bind(final Post post) {
            //bind the post data to the view elements
            if (tvDescription != null) {
                tvDescription.setText(post.getDescription());
            }
            if (tvUsername != null) {
                tvUsername.setText(post.getUser().getUsername());
                tvUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onUsernameAction(getAdapterPosition());
                    }
                });
            }

            if (tvUsername2 != null){
                tvUsername2.setText(post.getUser().getUsername());
                tvUsername2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onUsernameAction(getAdapterPosition());
                    }
                });
            }
            if (ivImage != null){
                //check if the post has a valid image
                ParseFile image = post.getImage();
                if (image != null) {
                    Glide.with(context).load(post.getImage().getUrl()).into(ivImage);
                }
            }

            if (ivProfilePic != null) {
                //check if the user has a valid profilePic
                ParseFile image2 = post.getUser().getParseFile("profilePic");
                if (image2 != null) {
                    Glide.with(context)
                            .load(post.getUser().getParseFile("profilePic").getUrl())
                            .circleCrop()
                            .into(ivProfilePic);
                } else {
                    Glide.with(context)
                            .load(context.getResources().getString(R.string.DEFAULT_PROFILE_PIC))
                            .circleCrop()
                            .into(ivProfilePic);
                }

                ivProfilePic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickListener.onProfilePicAction(getAdapterPosition());
                    }
                });

            }

        }

        @Override
        public void onClick(View view) {
            //get post position
            int position = getAdapterPosition();

            //get post at that position
            Post post = posts.get(position);
            Log.i(PostAdapter.class.getSimpleName(), "Post at Position " + position + "clicked.");
            //go to PostDetails Activity
            Intent intent = new Intent(context, PostDetailsActivity.class);
            //pass info from that post into Details Activity
            intent.putExtra("post", post);
            context.startActivity(intent);
        }
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }
}
