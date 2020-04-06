package com.example.iochat.MessageAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iochat.R;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private ArrayList<message> messags;
    private Context mContext;

    public MessageAdapter( Context context) {
        messags = new ArrayList<>();
        mContext = context;
    }

    public void setMessags(ArrayList<message> msgs){
        this.messags = msgs;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new MessageViewHolder(inflater.inflate(R.layout.simple_message_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.profile.setText(messags.get(position).getPro());
        holder.msg.setText(messags.get(position).getMs());
    }

    @Override
    public int getItemCount() {
        return messags.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder{

        private TextView profile, msg;
        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            profile = itemView.findViewById(R.id.message_profile_name_id);
            msg = itemView.findViewById(R.id.message_actual_message_id);
        }
    }
}
