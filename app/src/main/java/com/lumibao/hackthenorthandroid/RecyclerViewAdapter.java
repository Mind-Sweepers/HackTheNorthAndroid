package com.lumibao.hackthenorthandroid;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by micha on 2018-09-23.
 */

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{


    Context context;
    List<Message> messageList;

    public RecyclerViewAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messageList = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View row = inflater.inflate(R.layout.message_layout, parent, false);
        MessageItem message = new MessageItem(row);
        return message;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);

        ((MessageItem) holder).txtLanguage.setText(message.getLanguage());
        ((MessageItem) holder).txtDate.setText(message.getTime());
        ((MessageItem) holder).txtMessage.setText(message.getMessage());

        Log.d("test", "hi");
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class MessageItem extends RecyclerView.ViewHolder {
        TextView txtLanguage;
        TextView txtDate;
        TextView txtMessage;

        public MessageItem(View view) {
            super(view);
            txtLanguage = view.findViewById(R.id.txtLanguage);
            txtDate = view.findViewById(R.id.txtDate);
            txtMessage = view.findViewById(R.id.txtMessage);
        }
    }
}
