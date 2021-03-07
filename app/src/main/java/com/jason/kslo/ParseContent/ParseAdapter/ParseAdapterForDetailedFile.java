package com.jason.kslo.ParseContent.ParseAdapter;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.Activity.MainActivity;
import com.jason.kslo.Intro.SlideActivity;
import com.jason.kslo.ParseContent.Activity.DetailedImageActivity;
import com.jason.kslo.ParseContent.Activity.DetailedLatestNewsActivity;
import com.jason.kslo.ParseContent.ParseItem.ParseItem;
import com.jason.kslo.ParseContent.ParseItem.SecondParseItem;
import com.jason.kslo.PdfView.PdfView;
import com.jason.kslo.R;

import java.util.ArrayList;

import static com.jason.kslo.Activity.MainActivity.getContextOfApplication;

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

            Intent intent = new Intent(MainActivity.getContextOfApplication(), PdfView.class);
            intent.putExtra("title",parseItem.getFileName());
            intent.putExtra("fileUrl",parseItem.getFile());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.getContextOfApplication().startActivity(intent);
        }
    }
}