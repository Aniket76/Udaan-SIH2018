package com.hackathon2018.udaan.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackathon2018.udaan.Activities.ForumAnswerActivity;
import com.hackathon2018.udaan.Models.Events;
import com.hackathon2018.udaan.Models.Questions;
import com.hackathon2018.udaan.QuestionId;
import com.hackathon2018.udaan.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aniketvishal on 24/03/18.
 */

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.ForumViewHolder> {

    public List<Questions> QuestionList;
    private Context context;
    private int pos;

    public ForumAdapter(List<Questions> questionList, Context context) {
        QuestionList = questionList;
        this.context = context;
    }

    @Override
    public ForumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_list_layout, parent, false);
        return new ForumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForumViewHolder holder, int position) {

        pos = position;

        final Activity activity = (Activity)context;
        final String quesId = QuestionList.get(position).questionId;

        if (position%2 == 0){
            holder.mFllChildCardLayout.setBackgroundColor(Color.parseColor("#f9fffe"));
        }else {
            holder.mFllChildCardLayout.setBackgroundColor(Color.parseColor("#fafff9"));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myintent = new Intent(activity, ForumAnswerActivity.class);
                myintent.putExtra("questionId", quesId);
                myintent.putExtra("QName", QuestionList.get(pos).getName());
                myintent.putExtra("QImage", QuestionList.get(pos).getImage());
                myintent.putExtra("QQues", QuestionList.get(pos).getQuestion());
                context.startActivity(myintent);

            }
        });

        holder.setSearchDetails(
                QuestionList.get(position).getName(),
                QuestionList.get(position).getImage(),
                QuestionList.get(position).getQuestion());

    }

    @Override
    public int getItemCount() {
        return QuestionList.size();
    }

    public class ForumViewHolder extends RecyclerView.ViewHolder {

        View mView;

        private TextView mFllName, mFllQuestion;
        private CircleImageView mFllImage;

        public LinearLayout mFllChildCardLayout;

        public ForumViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mFllName = (TextView) mView.findViewById(R.id.fll_name_txt);
            mFllQuestion = (TextView) mView.findViewById(R.id.fll_question_txt);

            mFllChildCardLayout = (LinearLayout)mView.findViewById(R.id.fll_childcard_layout);

            mFllImage = (CircleImageView) mView.findViewById(R.id.fll_profile_image);

        }

        public void setSearchDetails(String name, final String image, String question) {
            mFllName.setText(name);
            mFllQuestion.setText(question);

            Picasso.with(mView.getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.profile_icon).into(mFllImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(mView.getContext()).load(image).placeholder(R.drawable.profile_icon).into(mFllImage);
                }
            });

        }
    }

}
