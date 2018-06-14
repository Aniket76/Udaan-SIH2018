package com.hackathon2018.udaan.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackathon2018.udaan.Activities.OthersProfileActivity;
import com.hackathon2018.udaan.Models.SearchUsers;
import com.hackathon2018.udaan.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aniketvishal on 22/03/18.
 */

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    public List<SearchUsers> searchUserList;
    private Context context;

    public SearchListAdapter(Context context, List<SearchUsers> searchUserList) {
        this.searchUserList = searchUserList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        if (position%2 == 0){
            holder.mSlMainCardLayout.setBackgroundColor(Color.parseColor("#f9fffe"));
        }else {
            holder.mSlMainCardLayout.setBackgroundColor(Color.parseColor("#fafff9"));
        }

        holder.setSearchDetails(searchUserList.get(position).getDisplayName(),
                searchUserList.get(position).getStatus(),
                searchUserList.get(position).getImage());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Activity activity = (Activity)context;
                final String quesId = searchUserList.get(position).otherUserId;

                Intent myintent = new Intent(activity, OthersProfileActivity.class);
                myintent.putExtra("UsersId", quesId);
                context.startActivity(myintent);

                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


            }
        });

    }

    @Override
    public int getItemCount() {
        return searchUserList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        View mView;

        String img;

        private TextView mSlNameTv, mSlStatusTv;
        private CircleImageView mSlImageCiv;

        public LinearLayout mSlMainCardLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mSlNameTv = (TextView) mView.findViewById(R.id.sl_name_txt);
            mSlStatusTv = (TextView) mView.findViewById(R.id.sl_status_txt);
            mSlImageCiv = (CircleImageView) mView.findViewById(R.id.sl_profile_image);

            mSlMainCardLayout = (LinearLayout)mView.findViewById(R.id.sl_maincard_layout);

        }

        public void setSearchDetails(String name, String status, String image) {
            mSlNameTv.setText(name);
            mSlStatusTv.setText(status);
            img = image;

            Picasso.with(mView.getContext()).load(img).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.profile_icon).into(mSlImageCiv, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(mView.getContext()).load(img).placeholder(R.drawable.profile_icon).into(mSlImageCiv);
                }
            });

        }
    }

}
