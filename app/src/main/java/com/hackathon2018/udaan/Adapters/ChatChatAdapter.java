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

import com.google.firebase.auth.FirebaseAuth;
import com.hackathon2018.udaan.Activities.ChatMsgActivity;
import com.hackathon2018.udaan.Activities.ForumAnswerActivity;
import com.hackathon2018.udaan.Models.ChatChat;
import com.hackathon2018.udaan.Models.ChatRequest;
import com.hackathon2018.udaan.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aniketvishal on 29/03/18.
 */

public class ChatChatAdapter extends RecyclerView.Adapter<ChatChatAdapter.ChatChatViewHolder>{

    private String currentUserUid;
    private String uid;

    public List<ChatChat> ChatChatList;
    private Context context;

    public ChatChatAdapter(List<ChatChat> chatChatList, Context context) {
        ChatChatList = chatChatList;
        this.context = context;
    }

    @Override
    public ChatChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_chat_layout, parent, false);
        return new ChatChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChatChatViewHolder holder, int position) {

        uid = ChatChatList.get(position).chatOtherUserId;
        final Activity activity = (Activity)context;

        currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (position%2 == 0){
            holder.mCclChildCardLayout.setBackgroundColor(Color.parseColor("#f9fffe"));
        }else {
            holder.mCclChildCardLayout.setBackgroundColor(Color.parseColor("#fafff9"));
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myintent = new Intent(activity, ChatMsgActivity.class);
                myintent.putExtra("otherUserId", uid);
                context.startActivity(myintent);

            }
        });

        String imageUrl= ChatChatList.get(position).getImageurl();
        Picasso.with(context)
                .load(ChatChatList.get(position).getImageurl())
                .fit().into(holder.mCclImage);

        holder.mCclName.setText(ChatChatList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return ChatChatList.size();
    }

    public class ChatChatViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TextView mCclName,mCclMsg;
        public CircleImageView mCclImage;

        public LinearLayout mCclChildCardLayout;

        public ChatChatViewHolder(View itemView) {
            super(itemView);


            mView = itemView;

            mCclName = (TextView) mView.findViewById(R.id.ccl_name_txt);
            mCclMsg = (TextView) mView.findViewById(R.id.ccl_msg_txt);

            mCclImage = (CircleImageView) mView.findViewById(R.id.ccl_profile_image);

            mCclChildCardLayout = (LinearLayout)mView.findViewById(R.id.ccl_maincard_layout);


        }
    }

}
