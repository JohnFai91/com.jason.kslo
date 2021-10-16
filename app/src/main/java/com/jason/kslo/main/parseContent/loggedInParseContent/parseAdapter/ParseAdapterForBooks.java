package com.jason.kslo.main.parseContent.loggedInParseContent.parseAdapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.main.parseContent.loggedInParseContent.activity.detailedBooksActivity;
import com.jason.kslo.R;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseItem.LoginParseItem;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class ParseAdapterForBooks extends RecyclerView.Adapter<ParseAdapterForBooks.ViewHolder> {

    private static ArrayList<LoginParseItem> parseItems = null;
    CardView cardView;
    LoginParseItem parseItem;

    @SuppressWarnings("unused")
    public ParseAdapterForBooks(ArrayList<LoginParseItem> parseItems, Context context) {
        ParseAdapterForBooks.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForBooks.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_books,parent,false);

        cardView = view.findViewById(R.id.horizontalBooksCardView);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForBooks.ViewHolder holder, int position) {
        parseItem = parseItems.get(position);

        holder.title.setText(parseItem.getTitle());
        holder.dueDate.setText(parseItem.getReturnDate());

        Picasso.get().load(parseItem.getDetailUrl())
                .memoryPolicy(MemoryPolicy.NO_STORE,MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE,NetworkPolicy.NO_STORE)
                .into(holder.bookImg);

    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView title;
        @SuppressWarnings("unused")
        final TextView borrowedDate;
        final TextView dueDate ;
        final ImageView bookImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.BooksName);
            borrowedDate = itemView.findViewById(R.id.BooksBorrowedDate);
            dueDate = itemView.findViewById(R.id.BooksReturnDate);
            bookImg = itemView.findViewById(R.id.BookImage);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            LoginParseItem parseItem = parseItems.get(position);

            Intent intent = new Intent(MainActivity.getContextOfApplication(), detailedBooksActivity.class);
            intent.putExtra("title",parseItem.getTitle());
            intent.putExtra("image",parseItem.getDetailUrl());
            intent.putExtra("detailUrl",parseItem.getBkDetailUrl());
            intent.putExtra("cookies", (Serializable) parseItem.getCookies());
            intent.putExtra("dueDate",parseItem.getReturnDate());
            intent.putExtra("borrowedDate",parseItem.getBorrowedDate());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            MainActivity.getContextOfApplication().startActivity(intent);
        }
    }
}