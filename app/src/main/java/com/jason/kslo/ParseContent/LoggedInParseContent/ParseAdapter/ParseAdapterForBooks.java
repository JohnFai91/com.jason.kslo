package com.jason.kslo.ParseContent.LoggedInParseContent.ParseAdapter;

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
import com.jason.kslo.Main.Activity.MainActivity;
import com.jason.kslo.ParseContent.LoggedInParseContent.Activity.DetailedIntranetActivity;
import com.jason.kslo.ParseContent.LoggedInParseContent.ParseItem.LoginParseItem;
import com.jason.kslo.ParseContent.LoggedInParseContent.ParseItem.SecondLoginParseItem;
import com.jason.kslo.R;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ArrayList;

public class ParseAdapterForBooks extends RecyclerView.Adapter<ParseAdapterForBooks.ViewHolder> {

    private static ArrayList<SecondLoginParseItem> parseItems = null;
    CardView cardView;
    SecondLoginParseItem parseItem;

    public ParseAdapterForBooks(ArrayList<SecondLoginParseItem> parseItems, Context context) {
        ParseAdapterForBooks.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForBooks.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_books,parent,false);

        cardView = view.findViewById(R.id.horizontalBooksCardView);
        cardView.setCardElevation(0);
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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, borrowedDate, dueDate ;
        ImageView bookImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.BooksName);
            borrowedDate = itemView.findViewById(R.id.BooksBorrowedDate);
            dueDate = itemView.findViewById(R.id.BooksReturnDate);
            bookImg = itemView.findViewById(R.id.BookImage);
        }
    }
}