package com.jason.kslo.ParseContent.LoggedInParseContent.ParseAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.Main.Activity.MainActivity;
import com.jason.kslo.ParseContent.LoggedInParseContent.Activity.DetailedIntranetActivity;
import com.jason.kslo.ParseContent.LoggedInParseContent.ParseItem.LoginParseItem;
import com.jason.kslo.R;

import java.io.Serializable;
import java.util.ArrayList;

public class ParseAdapterForLoginFragment extends RecyclerView.Adapter<ParseAdapterForLoginFragment.ViewHolder> {

    private static ArrayList<LoginParseItem> parseItems = null;
    CardView cardView;
    LoginParseItem parseItem;

    public ParseAdapterForLoginFragment(ArrayList<LoginParseItem> parseItems, Context context) {
        ParseAdapterForLoginFragment.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForLoginFragment.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_intranet,parent,false);

        cardView = view.findViewById(R.id.cardView_intranet);
        cardView.setCardElevation(0);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForLoginFragment.ViewHolder holder, int position) {
        parseItem = parseItems.get(position);

        holder.title.setText(parseItem.getTitle());
        holder.sender.setText(parseItem.getSender());
        holder.date.setText(parseItem.getDate());
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title, date, sender;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.intranetTitle);
            date = itemView.findViewById(R.id.intranetDate);
            sender = itemView.findViewById(R.id.intranetSender);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            LoginParseItem parseItem = parseItems.get(position);

            Intent intent = new Intent(MainActivity.getContextOfApplication(), DetailedIntranetActivity.class);
            intent.putExtra("title",parseItem.getTitle());
            intent.putExtra("sender",parseItem.getSender());
            intent.putExtra("time",parseItem.getDate());
            intent.putExtra("detailUrl",parseItem.getDetailUrl());
            intent.putExtra("cookies", (Serializable) parseItem.getCookies());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.getContextOfApplication().startActivity(intent);
        }
    }
}