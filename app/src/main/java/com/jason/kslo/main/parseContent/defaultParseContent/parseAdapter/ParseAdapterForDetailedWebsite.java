package com.jason.kslo.main.parseContent.defaultParseContent.parseAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.main.DownloadView;
import com.jason.kslo.main.parseContent.defaultParseContent.activity.DetailedImageActivity;
import com.jason.kslo.R;
import com.jason.kslo.main.parseContent.parseItem.SecondParseItem;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ParseAdapterForDetailedWebsite extends RecyclerView.Adapter<ParseAdapterForDetailedWebsite.ViewHolder> {

    private static ArrayList<SecondParseItem> parseItems = null;

    @SuppressWarnings("unused")
    public ParseAdapterForDetailedWebsite(ArrayList<SecondParseItem> parseItems, Context context) {
        ParseAdapterForDetailedWebsite.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForDetailedWebsite.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_detailed_website,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForDetailedWebsite.ViewHolder holder, int position) {
        SecondParseItem parseItem = parseItems.get(position);

                Picasso.get().load(parseItem.getImgURL())
                        .memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                        .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ImageViewDetailed);

                itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(view.getContext(), DownloadView.class);
            intent.putExtra("origin","detailedWebsite");
            intent.putExtra("fileUrl", parseItems.get(getAdapterPosition()).getDetailUrl());
            intent.putExtra("title", DetailedImageActivity.title);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            view.getContext().startActivity(intent);
        }
    }

}