package com.hackathon2018.udaan.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hackathon2018.udaan.Models.Answers;
import com.hackathon2018.udaan.Models.Message;
import com.hackathon2018.udaan.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by aniketvishal on 30/03/18.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public List<Message> MessageList;
    private Context context;

    public MessageAdapter(List<Message> messageList, Context context) {
        MessageList = messageList;
        this.context = context;
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_msg_layout, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {

        String type = MessageList.get(position).getType();

        if (type.equals("sent")){

            holder.mCmlMsg.setText(MessageList.get(position).getMsg());
            holder.mCmlName.setText(MessageList.get(position).getName());
            holder.mCmlMsg.setGravity(Gravity.RIGHT);
            holder.mCmlMsg.setBackgroundColor(Color.parseColor("#eefff4"));

        }else {

            holder.mCmlMsg.setText(MessageList.get(position).getMsg());
            holder.mCmlName.setText(MessageList.get(position).getName());
            holder.mCmlMsg.setGravity(Gravity.RIGHT);
            holder.mCmlMsg.setBackgroundColor(Color.parseColor("#FFEEF9"));

        }

    }

    @Override
    public int getItemCount() {
        return MessageList.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public TextView mCmlMsg,mCmlName;

        public MessageViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mCmlMsg = (TextView) mView.findViewById(R.id.cml_msg_txt);
            mCmlName = (TextView) mView.findViewById(R.id.cml_name_txt);

        }
    }

}
