package com.jason.kslo.main.changelog;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.R;

import java.util.ArrayList;

import static com.jason.kslo.main.activity.MainActivity.*;

public class ParseAdapterForChangelog extends RecyclerView.Adapter<ParseAdapterForChangelog.ViewHolder> {

    private static ArrayList<ChangelogParseItem> parseItems = null;
    CardView cardView;
    ChangelogParseItem parseItem;

    @SuppressWarnings("unused")
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

        if (parseItem.getSeparatorVisibility().equals("Visible")){
            holder.separator.setVisibility(View.VISIBLE);
            holder.version.setTextSize(18);
                if (parseItem.getTitle().contains("Stable")) {
                    holder.arrow.setImageResource(R.drawable.selected);
                    holder.separator.setBackgroundColor(getOrange());

                    holder.version.setTextColor(getOrange());
                } else if (parseItem.getTitle().contains("Beta")){
                    holder.separator.setBackgroundColor(getGreen());
                    holder.arrow.setImageResource(R.drawable.green_circle);

                    holder.version.setTextColor(getGreen());
                } else if (parseItem.getTitle().contains("Alpha")){
                    holder.separator.setBackgroundColor(getRed());
                    holder.arrow.setImageResource(R.drawable.red_circle);

                    holder.version.setTextColor(getRed());
                } else if (parseItem.getTitle().contains(getFuture())) {
                    holder.arrow.setImageResource(R.drawable.ic_up_blue);
                    holder.separator.setBackgroundColor(getBlue());

                    holder.version.setTextColor(getBlue());
                    holder.descL.setTextColor(getBlue());
                    holder.descR.setTextColor(getBlue());
                } else {
                    holder.arrow.setImageResource(R.drawable.unselected);
                }
        } else {
            holder.separator.setVisibility(View.GONE);
            holder.version.setTextSize(15);
            if (parseItem.getTitle().contains("-beta")){
                holder.arrow.setImageResource(R.drawable.ic_up_green);

                holder.version.setTextColor(getGreen());
                holder.descL.setTextColor(getGreen());
                holder.descR.setTextColor(getGreen());

            } else if (parseItem.getTitle().contains("alpha")){
                holder.arrow.setImageResource(R.drawable.ic_up_red);

                holder.version.setTextColor(getRed());
                holder.descL.setTextColor(getRed());
                holder.descR.setTextColor(getRed());

            }
            else {

                holder.arrow.setImageResource(R.drawable.ic_up);

                holder.version.setTextColor(getOrange());
                holder.descL.setTextColor(getOrange());
                holder.descR.setTextColor(getOrange());
            }
        }
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView version, descL, descR;
        final View separator;
        final ImageView arrow;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            version = itemView.findViewById(R.id.changelogVersion);
            descL = itemView.findViewById(R.id.leftChangelog);
            descR = itemView.findViewById(R.id.rightChangelog);
            separator = itemView.findViewById(R.id.changelogLineIdentifier);
            arrow = itemView.findViewById(R.id.changelogArrow);
        }
    }
}
