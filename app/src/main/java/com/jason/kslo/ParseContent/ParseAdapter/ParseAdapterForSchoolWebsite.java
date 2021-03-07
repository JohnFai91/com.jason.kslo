package com.jason.kslo.ParseContent.ParseAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.Activity.MainActivity;
import com.jason.kslo.ParseContent.Activity.DetailedImageActivity;
import com.jason.kslo.ParseContent.ParseItem.ParseItem;
import com.jason.kslo.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ParseAdapterForSchoolWebsite extends RecyclerView.Adapter<ParseAdapterForSchoolWebsite.ViewHolder> {

    private static ArrayList<ParseItem> parseItems = null;

    public ParseAdapterForSchoolWebsite(ArrayList<ParseItem> parseItems, Context context) {
        ParseAdapterForSchoolWebsite.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForSchoolWebsite.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_school_website,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     ParseItem parseItem = parseItems.get(position);
     holder.textView.setText(parseItem.getTitle());
     holder.countTextView.setText(parseItem.getCount());

     Picasso.get().load(parseItem.getImgURL())
             .memoryPolicy(MemoryPolicy.NO_CACHE,MemoryPolicy.NO_STORE)
             .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView textView, countTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            countTextView = itemView.findViewById(R.id.imageCount);
            imageView = itemView.findViewById(R.id.ImageView);
            textView = itemView.findViewById(R.id.imageViewDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ParseItem parseItem = parseItems.get(position);

            Intent intent = new Intent(MainActivity.getContextOfApplication(), DetailedImageActivity.class);
            intent.putExtra("title",parseItem.getTitle());
            intent.putExtra("image",parseItem.getImgURL());
            intent.putExtra("detailUrl",parseItem.getDetailUrl());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.getContextOfApplication().startActivity(intent);
        }
    }
}
