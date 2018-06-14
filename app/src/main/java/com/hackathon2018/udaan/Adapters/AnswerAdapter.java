package com.hackathon2018.udaan.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackathon2018.udaan.Models.Answers;
import com.hackathon2018.udaan.Models.Questions;
import com.hackathon2018.udaan.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aniketvishal on 24/03/18.
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

//    private static final int STATIC_CARD = 0;
//    private static final int DYNAMIC_CARD = 1;

    public List<Answers> AnswerList;
    private Context context;

    private String QName,QImage,QQues;

    public AnswerAdapter(List<Answers> answerList, Context context,String QName,String QImage,String QQues) {
        AnswerList = answerList;
        this.context = context;
        this.QName = QName;
        this.QImage = QImage;
        this.QQues = QQues;
    }

//    @Override
//    public int getItemViewType(int position) {
//        if(position == 0) {
//            return STATIC_CARD;
//        } else {
//            return DYNAMIC_CARD;
//        }
//    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forum_list_layout, parent, false);
            return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AnswerViewHolder holder, int position) {

        if (position%2 == 0){
            holder.mFAllChildCardLayout.setBackgroundColor(Color.parseColor("#f9fffe"));
        }else {
            holder.mFAllChildCardLayout.setBackgroundColor(Color.parseColor("#fafff9"));
        }

        if (position == 0){

            holder.setSearchDetails(
                    QName,
                    QImage,
                    QQues);

        }else {

            holder.setSearchDetails(
                    AnswerList.get(position).getName(),
                    AnswerList.get(position).getImage(),
                    AnswerList.get(position).getAnswer(),
                    "answered the above question");

        }

    }

    @Override
    public int getItemCount() {
        return AnswerList.size();
    }



    public class AnswerViewHolder extends RecyclerView.ViewHolder {

        View mView;

        private TextView mFllName, mFllQuestion,mFllAdded;
        private CircleImageView mFllImage;

        public LinearLayout mFAllChildCardLayout;

        public AnswerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mFllName = (TextView) mView.findViewById(R.id.fll_name_txt);
            mFllQuestion = (TextView) mView.findViewById(R.id.fll_question_txt);
            mFllAdded = (TextView) mView.findViewById(R.id.fll_asked_txt);

            mFllImage = (CircleImageView) mView.findViewById(R.id.fll_profile_image);

            mFAllChildCardLayout = (LinearLayout)mView.findViewById(R.id.fll_childcard_layout);

        }

        public void setSearchDetails(String name, final String image, String answer, String asked) {
            mFllName.setText(name);
            mFllQuestion.setText(answer);
            mFllAdded.setText(asked);

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

        public void setSearchDetails(String name, final String image, String answer) {
            mFllName.setText(name);
            mFllQuestion.setText(answer);
            mFllQuestion.setTextSize(22);
            mFllQuestion.setTextColor(Color.parseColor("#424242"));

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
