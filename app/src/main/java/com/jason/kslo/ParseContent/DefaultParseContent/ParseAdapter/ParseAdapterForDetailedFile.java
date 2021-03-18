package com.jason.kslo.ParseContent.DefaultParseContent.ParseAdapter;

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
import com.jason.kslo.Main.Activity.MainActivity;
import com.jason.kslo.ParseContent.ParseItem.ParseItem;
import com.jason.kslo.Main.PdfView.DownloadPdfView;
import com.jason.kslo.R;

import java.util.ArrayList;

public class ParseAdapterForDetailedFile extends RecyclerView.Adapter<ParseAdapterForDetailedFile.ViewHolder> {

    private static ArrayList<ParseItem> parseItems = null;
    CardView cardView;
    ParseItem parseItem;
    String fileUrl;

    public ParseAdapterForDetailedFile(ArrayList<ParseItem> parseItems, Context context) {
        ParseAdapterForDetailedFile.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForDetailedFile.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_detailed_file,parent,false);

        cardView = view.findViewById(R.id.LatestNewsDetailedFileCardView);
        cardView.setCardBackgroundColor(Color.TRANSPARENT);
        cardView.setCardElevation(0);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForDetailedFile.ViewHolder holder, int position) {
        parseItem = parseItems.get(position);

        holder.file.setText(parseItem.getFileName());
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView file;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            file = itemView.findViewById(R.id.openPdf);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ParseItem parseItem = parseItems.get(position);

            Intent intent = new Intent(MainActivity.getContextOfApplication(), DownloadPdfView.class);
            intent.putExtra("title",parseItem.getFileName());
            intent.putExtra("fileUrl",parseItem.getFile());
            intent.putExtra("origin","DetailedFile");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.getContextOfApplication().startActivity(intent);
        }
    }
}