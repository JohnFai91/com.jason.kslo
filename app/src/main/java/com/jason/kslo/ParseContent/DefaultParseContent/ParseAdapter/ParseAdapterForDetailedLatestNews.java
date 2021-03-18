package com.jason.kslo.ParseContent.DefaultParseContent.ParseAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.ParseContent.ParseItem.SecondParseItem;
import com.jason.kslo.R;

import java.util.ArrayList;


public class ParseAdapterForDetailedLatestNews extends RecyclerView.Adapter<ParseAdapterForDetailedLatestNews.ViewHolder> {

    private static ArrayList<SecondParseItem> parseItems = null;
    private static final ArrayList<SecondParseItem> parseSecondItems = null;
    CardView cardView;

    public ParseAdapterForDetailedLatestNews(ArrayList<SecondParseItem> parseItems, Context context) {
        ParseAdapterForDetailedLatestNews.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForDetailedLatestNews.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_detailed_latest_news,parent,false);

        cardView = view.findViewById(R.id.cardViewDetailedLatestNews);
        cardView.setCardBackgroundColor(Color.TRANSPARENT);
        cardView.setCardElevation(0);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForDetailedLatestNews.ViewHolder holder, int position) {
        SecondParseItem parseItem = parseItems.get(position);

        holder.bulletPointsTextView.setText(parseItem.getBulletPoints());

    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView bulletPointsTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bulletPointsTextView = itemView.findViewById(R.id.LatestNewsDetailedTextBulletPoints);
        }

    }
}