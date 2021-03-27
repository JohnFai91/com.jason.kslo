package com.jason.kslo.parseContent.loggedInParseContent.parseAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.parseContent.loggedInParseContent.parseItem.LoginParseItem;
import com.jason.kslo.R;

import java.util.ArrayList;


public class ParseAdapterForDetailedIntranetActivity extends RecyclerView.Adapter<ParseAdapterForDetailedIntranetActivity.ViewHolder> {

    private static ArrayList<LoginParseItem> parseItems = null;
    CardView cardView;

    @SuppressWarnings("unused")
    public ParseAdapterForDetailedIntranetActivity(ArrayList<LoginParseItem> parseItems, Context context) {
        ParseAdapterForDetailedIntranetActivity.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForDetailedIntranetActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_detailed_intranet,parent,false);

        cardView = view.findViewById(R.id.cardView_detailed_intranet);
        cardView.setCardBackgroundColor(Color.TRANSPARENT);
        cardView.setCardElevation(0);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForDetailedIntranetActivity.ViewHolder holder, int position) {
        LoginParseItem parseItem = parseItems.get(position);

        holder.contentTextView.setText(parseItem.getContent());
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView contentTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.detailedIntranetContent);
        }

    }
}