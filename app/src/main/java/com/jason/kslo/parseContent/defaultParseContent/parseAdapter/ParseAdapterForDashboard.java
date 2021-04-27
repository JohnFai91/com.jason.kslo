package com.jason.kslo.parseContent.defaultParseContent.parseAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.R;
import com.jason.kslo.parseContent.parseItem.DashboardParseItem;

import java.util.ArrayList;

public class ParseAdapterForDashboard extends RecyclerView.Adapter<ParseAdapterForDashboard.ViewHolder> {

    private final ArrayList<DashboardParseItem> parseItems;

    @SuppressWarnings("unused")
    public ParseAdapterForDashboard(ArrayList<DashboardParseItem> parseItems, Context context) {
        this.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForDashboard.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_dashboard,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForDashboard.ViewHolder holder, int position) {
        DashboardParseItem parseItem = parseItems.get(position);

        holder.title.setText(parseItem.getTitle());
        holder.date.setText(parseItem.getDate());
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView title, date;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.DashboardCardViewTitle);
                date = itemView.findViewById(R.id.DashboardCardViewDate);

        }
    }
}