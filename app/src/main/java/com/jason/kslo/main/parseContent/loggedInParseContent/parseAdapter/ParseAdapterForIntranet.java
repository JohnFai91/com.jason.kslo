package com.jason.kslo.main.parseContent.loggedInParseContent.parseAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.R;
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.main.parseContent.loggedInParseContent.activity.DetailedIntranetActivity;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseItem.LoginParseItem;

import java.util.ArrayList;

public class ParseAdapterForIntranet extends RecyclerView.Adapter<ParseAdapterForIntranet.ViewHolder> {

    private static ArrayList<LoginParseItem> parseItems = null;
    CardView cardView;
    LoginParseItem parseItem;

    public ParseAdapterForIntranet(ArrayList<LoginParseItem> parseItems, Context context) {
        ParseAdapterForIntranet.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForIntranet.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_intranet,parent,false);

        cardView = view.findViewById(R.id.horizontalBooksCardView);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForIntranet.ViewHolder holder, int position) {
        parseItem = parseItems.get(position);

        holder.title.setText(parseItem.getTitle());
        holder.date.setText(parseItem.getDate());
        holder.size.setText(parseItem.getSize());
        holder.sender.setText(parseItem.getSender());

        if (parseItem.getFilePresent()) {
            holder.attachmentIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.attachmentIndicator.setVisibility(View.GONE);
        }

        if (parseItem.getRead()) {
            holder.readIndicator.setVisibility(View.GONE);
        } else {
            holder.readIndicator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView title, borrowedDate,date, size, sender;
        final ImageView readIndicator, attachmentIndicator;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.parse_item_for_intranet_title);
            borrowedDate = itemView.findViewById(R.id.BooksBorrowedDate);
            date = itemView.findViewById(R.id.parse_item_for_intranet_date);
            size = itemView.findViewById(R.id.parse_item_for_intranet_size);
            attachmentIndicator = itemView.findViewById(R.id.parse_item_for_intranet_attachment_indicator);
            readIndicator = itemView.findViewById(R.id.parse_item_for_intranet_read_indicator);
            sender = itemView.findViewById(R.id.parse_item_for_intranet_sender);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            LoginParseItem parseItem = parseItems.get(position);
            Intent intent = new Intent(MainActivity.getContextOfApplication(), DetailedIntranetActivity.class);
            intent.putExtra("title", parseItem.getTitle());
            intent.putExtra("sender", parseItem.getSender());
            intent.putExtra("time", parseItem.getDate());
            intent.putExtra("detailUrl", parseItem.getDetailUrl());
            MainActivity.getContextOfApplication().startActivity(intent);
        }
    }
}