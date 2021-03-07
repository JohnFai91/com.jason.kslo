package com.jason.kslo.ParseContent.ParseAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.Activity.MainActivity;
import com.jason.kslo.ParseContent.Activity.DetailedLatestNewsActivity;
import com.jason.kslo.ParseContent.ParseItem.ParseItem;
import com.jason.kslo.ParseContent.ParseItem.SecondParseItem;
import com.jason.kslo.R;

import java.util.ArrayList;

public class ParseAdapterForLatestNews extends RecyclerView.Adapter<ParseAdapterForLatestNews.ViewHolder> {

    private static ArrayList<SecondParseItem> parseItems = null;

    public ParseAdapterForLatestNews(ArrayList<SecondParseItem> parseItems, Context context) {
        ParseAdapterForLatestNews.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForLatestNews.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_latest_news,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     SecondParseItem parseItem = parseItems.get(position);
     holder.dateTextView.setText(parseItem.getDesc());
     holder.TitleTextView.setText(parseItem.getTitle());
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView TitleTextView, dateTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TitleTextView = itemView.findViewById(R.id.latestNewsTitle);
            dateTextView = itemView.findViewById(R.id.LatestNewsDate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            SecondParseItem parseItem = parseItems.get(position);

            Intent intent = new Intent(MainActivity.getContextOfApplication(), DetailedLatestNewsActivity.class);
            intent.putExtra("title",parseItem.getTitle());
            intent.putExtra("detailUrl",parseItem.getDetailUrl());
            intent.putExtra("imgUrl",parseItem.getImgURL());
            intent.putExtra("video",parseItem.getVideo());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.getContextOfApplication().startActivity(intent);
        }
    }
}
