package com.jason.kslo.main.parseContent.defaultParseContent.parseAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.R;
import com.jason.kslo.main.parseContent.defaultParseContent.fragment.DashboardFragment;
import com.jason.kslo.main.parseContent.parseItem.DashboardParseItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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

        if (position == DashboardFragment.getIcsPosition() - 1) {
            holder.title.setText(parseItem.getTitle());
            holder.date.setText(parseItem.getDate());
            holder.todayView.setVisibility(View.VISIBLE);
            Log.d("AdapterForDashboard", "position: " + DashboardFragment.getIcsPositionStr());
        } else {
            holder.title.setText(parseItem.getTitle());
            holder.date.setText(parseItem.getDate());
            holder.todayView.setVisibility(View.GONE);
        }


        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy E HH:mm", Locale.ENGLISH);
        String todayStr = df.format(Calendar.getInstance().getTime());

        String date = parseItem.getDate();
        Log.d("Dashboard", "Date(today): " + todayStr + " Date(item): " + parseItem.getDate());
        if (date.substring(0,11).equals(todayStr.substring(0,11))) {
            holder.dateSample.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.red));
        } else {
            holder.dateSample.setTextColor(holder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
        }
        holder.dateSample.setText(parseItem.getDate().substring(0,2));
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView title, date, dateSample;
        final RelativeLayout todayView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.DashboardCardViewTitle);
            date = itemView.findViewById(R.id.DashboardCardViewDate);
            todayView = itemView.findViewById(R.id.DashboardCardViewTodayView);
            dateSample = itemView.findViewById(R.id.Dashboard_Simple_Date);

            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();

            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle(parseItems.get(position).getTitle())
                    .setNegativeButton(R.string.Confirm, (dialogInterface, i) -> dialogInterface.dismiss())
                    .setCancelable(true);
            if (parseItems.get(position).getUrl() != null) {
                builder.setNeutralButton(view.getContext().getString(R.string.GoTo) + " VLE", (dialogInterface, i) -> {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(parseItems.get(position).getUrl()));
                    view.getContext().startActivity(intent);
                });
            }
                if (parseItems.get(position).getDesc() != null &&
                        parseItems.get(position).getDesc().contains("HwUrl: https://hkedcity.instructure.com/courses/")) {

                    String desc = parseItems.get(position).getDesc(), url;
                    url = desc.substring(desc.indexOf("HwUrl: "));
                    url = url.replace("HwUrl: ","");
                    desc = desc.replace("HwUrl: " + url, "");

                    String finalUrl = url;
                    builder.setMessage(desc)
                           .setNeutralButton(view.getContext().getString(R.string.GoTo) + " VLE", (dialogInterface, i) -> {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
                                view.getContext().startActivity(intent);
                    });
                } else {
                    builder.setMessage(parseItems.get(position).getDesc());
                }
                builder.show();
        }
    }
}