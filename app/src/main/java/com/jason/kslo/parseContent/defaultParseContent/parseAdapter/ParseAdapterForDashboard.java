package com.jason.kslo.parseContent.defaultParseContent.parseAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    private static ArrayList<DashboardParseItem> parseItems;
    static DashboardParseItem parseItem;

    @SuppressWarnings("unused")
    public ParseAdapterForDashboard(ArrayList<DashboardParseItem> parseItems, Context context) {
        ParseAdapterForDashboard.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForDashboard.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_dashboard,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForDashboard.ViewHolder holder, int position) {
        parseItem = parseItems.get(position);

        holder.title.setText(parseItem.getTitle());
        holder.date.setText(parseItem.getDate());
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView title, date;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.DashboardCardViewTitle);
                date = itemView.findViewById(R.id.DashboardCardViewDate);

                itemView.setOnClickListener(this);
        }
        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle(parseItems.get(position).getTitle())
                    .setMessage(parseItems.get(position).getDesc())
                    .setNegativeButton(R.string.Confirm, (dialogInterface, i) -> dialogInterface.dismiss())
                    .setCancelable(true);
            if (parseItems.get(position).getUrl() != null) {
                builder.setNeutralButton(view.getContext().getString(R.string.GoTo) + "VLE", (dialogInterface, i) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(parseItems.get(position).getUrl()));
                    view.getContext().startActivity(intent);
                });
            }
            builder.show();
        }
    }
}