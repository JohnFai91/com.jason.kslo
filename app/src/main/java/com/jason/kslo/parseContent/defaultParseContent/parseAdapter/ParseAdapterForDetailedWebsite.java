package com.jason.kslo.parseContent.defaultParseContent.parseAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.parseContent.parseItem.ParseItem;
import com.jason.kslo.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ParseAdapterForDetailedWebsite extends RecyclerView.Adapter<ParseAdapterForDetailedWebsite.ViewHolder> {

    private final ArrayList<ParseItem> parseItems;

    @SuppressWarnings("unused")
    public ParseAdapterForDetailedWebsite(ArrayList<ParseItem> parseItems, Context context) {
        this.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForDetailedWebsite.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_detailed_website,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForDetailedWebsite.ViewHolder holder, int position) {
        ParseItem parseItem = parseItems.get(position);

                Picasso.get().load(parseItem.getImgURL())
                        .memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE)
                        .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                        .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        final ImageView imageView;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.ImageViewDetailed);

        }
    }
}