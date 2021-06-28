package com.jason.kslo.main.parseContent.loggedInParseContent.parseAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.main.DownloadView;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseItem.SecondLoginParseItem;
import com.jason.kslo.R;

import java.io.Serializable;
import java.util.ArrayList;

public class ParseAdapterForDetailedIntranetFile extends RecyclerView.Adapter<ParseAdapterForDetailedIntranetFile.ViewHolder> {

    private static ArrayList<SecondLoginParseItem> parseItems = null;
    CardView cardView;
    SecondLoginParseItem parseItem;

    @SuppressWarnings("unused")
    public ParseAdapterForDetailedIntranetFile(ArrayList<SecondLoginParseItem> parseItems, Context context) {
        ParseAdapterForDetailedIntranetFile.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForDetailedIntranetFile.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_detailed_intranet_file, parent, false);

        cardView = view.findViewById(R.id.IntranetDetailedFileCardView);
        cardView.setCardBackgroundColor(Color.TRANSPARENT);
        cardView.setCardElevation(0);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForDetailedIntranetFile.ViewHolder holder, int position) {
        parseItem = parseItems.get(position);

        holder.file.setText(parseItem.getFileName());
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView file;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            file = itemView.findViewById(R.id.openFile);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            SecondLoginParseItem parseItem = parseItems.get(position);

                Intent intent = new Intent(MainActivity.getContextOfApplication(), DownloadView.class);
                intent.putExtra("title", parseItem.getFileName());
                intent.putExtra("fileUrl", parseItem.getDetailUrl());
                intent.putExtra("cookies", (Serializable) parseItem.getCookies());
                intent.putExtra("origin", "detailedIntranetFile");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.getContextOfApplication().startActivity(intent);
        }
    }
}