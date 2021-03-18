package com.jason.kslo.Main.Changelog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.ParseContent.ParseItem.ParseItem;
import com.jason.kslo.R;

import java.util.ArrayList;

public class ParseAdapterForChangelog extends RecyclerView.Adapter<ParseAdapterForChangelog.ViewHolder> {

    private static ArrayList<ChangelogParseItem> parseItems = null;
    CardView cardView;
    ChangelogParseItem parseItem;

    public ParseAdapterForChangelog(ArrayList<ChangelogParseItem> parseItems, Context context) {
        ParseAdapterForChangelog.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForChangelog.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_changelog, parent, false);

        cardView = view.findViewById(R.id.changelogCardView);
        cardView.setCardBackgroundColor(Color.TRANSPARENT);
        cardView.setCardElevation(0);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForChangelog.ViewHolder holder, int position) {
        parseItem = parseItems.get(position);

        holder.version.setText(parseItem.getTitle());
        holder.descL.setText(parseItem.getDescL());
        holder.descR.setText(parseItem.getDescR());
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView version, descL, descR;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            version = itemView.findViewById(R.id.changelogVersion);
            descL = itemView.findViewById(R.id.leftChangelog);
            descR = itemView.findViewById(R.id.rightChangelog);
        }
    }
}
