package com.hackathon2018.udaan.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackathon2018.udaan.Activities.ForumAnswerActivity;
import com.hackathon2018.udaan.Activities.NewsActivity;
import com.hackathon2018.udaan.Models.Answers;
import com.hackathon2018.udaan.Models.News;
import com.hackathon2018.udaan.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aniketvishal on 30/03/18.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder>{

    public List<News> NewsList;
    private Context context;

    public NewsAdapter(List<News> newsList, Context context) {
        NewsList = newsList;
        this.context = context;
    }

    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_layout, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NewsViewHolder holder, final int position) {

        final Activity activity = (Activity)context;

        holder.getNewsData(NewsList.get(position).getTitle(),NewsList.get(position).getImage());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myintent = new Intent(activity, NewsActivity.class);
                myintent.putExtra("news", NewsList.get(position).getNews());
                myintent.putExtra("image", NewsList.get(position).getImage());
                myintent.putExtra("title", NewsList.get(position).getTitle());
                context.startActivity(myintent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return NewsList.size();
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder{

        View mView;

        private TextView mNlTitle;
        private ImageView mNlImage;

        public LinearLayout mFAllChildCardLayout;

        public NewsViewHolder(View itemView) {
            super(itemView);

            mView = itemView;

            mNlTitle = (TextView) mView.findViewById(R.id.nl_news_title);
            mNlImage = (ImageView) mView.findViewById(R.id.nl_news_image);

        }

        public void getNewsData(String title, final String image){

            mNlTitle.setText(title);

            Picasso.with(mView.getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.news_icon).into(mNlImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(mView.getContext()).load(image).placeholder(R.drawable.news_icon).into(mNlImage);
                }
            });

        }
    }
}
