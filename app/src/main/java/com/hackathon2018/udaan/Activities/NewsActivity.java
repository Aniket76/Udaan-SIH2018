package com.hackathon2018.udaan.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackathon2018.udaan.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class NewsActivity extends AppCompatActivity {

    private ImageView mImage;
    private TextView mNews,mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        String news = getIntent().getStringExtra("news");
        final String image = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");

        mImage = (ImageView)findViewById(R.id.na_news_image);
        mNews = (TextView) findViewById(R.id.na_news_news);
        mTitle = (TextView) findViewById(R.id.na_news_title);

        mNews.setText(news);
        mTitle.setText(title);

        Picasso.with(NewsActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                .placeholder(R.drawable.news_icon).into(mImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(NewsActivity.this).load(image).placeholder(R.drawable.news_icon).into(mImage);
            }
        });

    }
}
