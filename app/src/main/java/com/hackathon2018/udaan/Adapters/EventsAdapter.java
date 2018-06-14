package com.hackathon2018.udaan.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hackathon2018.udaan.Models.Events;
import com.hackathon2018.udaan.Models.SearchUsers;
import com.hackathon2018.udaan.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aniketvishal on 23/03/18.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.EventsViewHolder> {

    public List<Events> EventsList;
    private Context context;

    public EventsAdapter(List<Events> eventsList, Context context) {
        EventsList = eventsList;
        this.context = context;
    }

    @Override
    public EventsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_list_layout, parent, false);
        return new EventsViewHolder(view);
    }

//    @Override
//    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
//    }

    @Override
    public void onBindViewHolder(EventsViewHolder holder, int position) {

        if (position%2 == 0){
            holder.mEllChildCardLayout.setBackgroundColor(Color.parseColor("#f9fffe"));
        }else {
            holder.mEllChildCardLayout.setBackgroundColor(Color.parseColor("#fafff9"));
        }

        holder.setSearchDetails(
                EventsList.get(position).getName(),
                EventsList.get(position).getImage(),
                EventsList.get(position).getPostedBy(),
                EventsList.get(position).getCity(),
                EventsList.get(position).getTitle(),
                EventsList.get(position).getDesc(),
                EventsList.get(position).getDate(),
                EventsList.get(position).getVenue(),
                EventsList.get(position).getTime());

    }

    @Override
    public int getItemCount() {
        return EventsList.size();
    }

    public class EventsViewHolder extends RecyclerView.ViewHolder {

        View mView;

        private TextView mEllName, mEllCity, mEllTitle, mEllDesc, mEllDate, mEllVenue, mEllTime;
        private CircleImageView mEllImage;

        public LinearLayout mEllChildCardLayout;

        public EventsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mEllName = (TextView) mView.findViewById(R.id.ell_name_txt);
            mEllCity = (TextView) mView.findViewById(R.id.ell_city_txt);
            mEllTitle = (TextView) mView.findViewById(R.id.ell_title_txt);
            mEllDesc = (TextView) mView.findViewById(R.id.ell_desc_txt);
            mEllDate = (TextView) mView.findViewById(R.id.ell_date_txt);
            mEllVenue = (TextView) mView.findViewById(R.id.ell_venue_txt);
            mEllTime = (TextView) mView.findViewById(R.id.ell_time_txt);

            mEllChildCardLayout = (LinearLayout)mView.findViewById(R.id.ell_childcard_layout);

            mEllImage = (CircleImageView) mView.findViewById(R.id.ell_profile_image);

        }

        public void setSearchDetails(String name, final String image, String postedBy, String city, String title, String desc, String date, String venue, String time) {
            mEllName.setText(name);
            mEllCity.setText(city);
            mEllTitle.setText(title);
            mEllDesc.setText(desc);
            mEllDate.setText(date);
            mEllVenue.setText(venue);
            mEllTime.setText(time);

            Picasso.with(mView.getContext()).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                    .placeholder(R.drawable.profile_icon).into(mEllImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(mView.getContext()).load(image).placeholder(R.drawable.profile_icon).into(mEllImage);
                }
            });

        }
    }

}
