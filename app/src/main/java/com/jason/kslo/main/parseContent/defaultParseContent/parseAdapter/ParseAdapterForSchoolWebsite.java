package com.jason.kslo.main.parseContent.defaultParseContent.parseAdapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.R;
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.main.parseContent.defaultParseContent.activity.DetailedImageActivity;
import com.jason.kslo.main.parseContent.parseItem.ThirdParseItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ParseAdapterForSchoolWebsite extends RecyclerView.Adapter<ParseAdapterForSchoolWebsite.ViewHolder> {

    private static ArrayList<ThirdParseItem> parseItems = null;
    static ThirdParseItem parseItem;
    Activity activity;

    public ParseAdapterForSchoolWebsite(ArrayList<ThirdParseItem> parseItems, Activity activity) {
        ParseAdapterForSchoolWebsite.parseItems = parseItems;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ParseAdapterForSchoolWebsite.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_school_website,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     parseItem = parseItems.get(position);
     holder.textView.setText(parseItem.getTitle());

         Picasso.get().load(parseItem.getImgUrl())
                 .resize(300,200)
                 .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView imageView;
        final TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.GalleryImageView);
            textView = itemView.findViewById(R.id.imageViewDescription);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            ThirdParseItem parseItem = parseItems.get(position);

            Intent intent = new Intent(MainActivity.getContextOfApplication(), DetailedImageActivity.class);
            intent.putExtra("title",parseItem.getTitle());
            intent.putExtra("image",parseItem.getImgUrl());
            intent.putExtra("detailUrl",parseItem.getUrl());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.getContextOfApplication().startActivity(intent);
        }
    }
}
