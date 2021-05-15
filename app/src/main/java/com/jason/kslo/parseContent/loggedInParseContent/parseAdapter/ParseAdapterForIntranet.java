package com.jason.kslo.parseContent.loggedInParseContent.parseAdapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.snackbar.Snackbar;
import com.jason.kslo.R;
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.parseContent.loggedInParseContent.activity.DetailedIntranetActivity;
import com.jason.kslo.parseContent.loggedInParseContent.fragment.IntranetFragment;
import com.jason.kslo.parseContent.loggedInParseContent.parseItem.LoginParseItem;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ParseAdapterForIntranet extends RecyclerView.Adapter<ParseAdapterForIntranet.ViewHolder> {

    private static ArrayList<LoginParseItem> parseItems = null;
    CardView cardView;
    LoginParseItem parseItem;
    static String mailId, mailTitle;
    static int selectedPosition;
    static Boolean delete;
    @SuppressLint("StaticFieldLeak")
    static View view;
    @SuppressLint("StaticFieldLeak")
    static ParseAdapterForIntranet.ViewHolder viewHolder;

    public ParseAdapterForIntranet(ArrayList<LoginParseItem> parseItems, Context context) {
        ParseAdapterForIntranet.parseItems = parseItems;
    }

    @NonNull
    @Override
    public ParseAdapterForIntranet.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_intranet,parent,false);

        cardView = view.findViewById(R.id.cardView_intranet);
        cardView.setCardElevation(0);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForIntranet.ViewHolder holder, int position) {
        parseItem = parseItems.get(position);

        holder.title.setText(parseItem.getTitle());
        holder.sender.setText(parseItem.getSender());
        holder.date.setText(parseItem.getDate());
        holder.size.setText(parseItem.getSize());
        holder.more.setOnClickListener(view -> showPopup(view,position));

         viewHolder = holder;
         view = holder.itemView;

        if (parseItem.getRead().equals("true")) {
            holder.readIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.readIndicator.setVisibility(View.GONE);
        }

        if (parseItem.getFilePresent().equals("true")) {
            holder.file.setVisibility(View.VISIBLE);
        } else {
            holder.file.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView title, date, sender, size;
        final ImageView file, readIndicator;
        final ImageButton more;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.intranetTitle);
            date = itemView.findViewById(R.id.intranetDate);
            sender = itemView.findViewById(R.id.intranetSender);
            file = itemView.findViewById(R.id.attachmentIndicator);
            more = itemView.findViewById(R.id.IntranetMoreInfo);
            readIndicator = itemView.findViewById(R.id.IntranetReadIndicator);
            size = itemView.findViewById(R.id.intranetSize);

            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            LoginParseItem parseItem = parseItems.get(position);
                Intent intent = new Intent(MainActivity.getContextOfApplication(), DetailedIntranetActivity.class);
                intent.putExtra("title", parseItem.getTitle());
                intent.putExtra("sender", parseItem.getSender());
                intent.putExtra("time", parseItem.getDate());
                intent.putExtra("detailUrl", parseItem.getDetailUrl());
                intent.putExtra("cookies", (Serializable) parseItem.getCookies());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MainActivity.getContextOfApplication().startActivity(intent);
        }
    }
    @SuppressLint("NonConstantResourceId")
    public void showPopup(View view, int position) {

        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.intranet_popup_menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            mailId = "[" + parseItems.get(position).getDetailUrl() + "]";
            mailTitle = parseItems.get(position).getTitle();
            selectedPosition = viewHolder.getAdapterPosition();

                switch (menuItem.getItemId()) {
                    case R.id.intranet_action_popup_delete:

                        Log.d("Intranet", "delete: " + mailId + mailTitle + " selectedPosition: " + selectedPosition);
                        showDeleteDialog();
                        return true;

                    case R.id.intranet_action_popup_move:

                        Log.d("Intranet", "star: " + parseItem.getDetailUrl());
                        showMoveDialog();
                        return true;

                    default:
                        return false;
                }
            });
                popupMenu.show();
    }
    private static class DeleteIntranet extends AsyncTask<Void, Void, Void> {
        Map<String, String> cookies;
        String url;
        Snackbar snackbar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            url = "https://www.hkmakslo.edu.hk/it-school//php/intra/index.php3";
            cookies = IntranetFragment.getCookies();
        }

        @Override
        protected Void doInBackground(Void... voids) {
                try {
                    Log.d("deleteIntranet", "selected_msgid" + mailId + "true");

                    if (IntranetFragment.getFolder().equals("inbox")) {
                        Document moveToBin = Jsoup.connect(url)
                                .cookies(cookies)
                                .method(Connection.Method.POST)
                                .data("folder", "inbox")
                                .data("order_by", "date")
                                .data("warned", "")
                                .data("formaction", "move")
                                .data("sorting_method", "desc")
                                .data("mail_id", "")
                                .data("postURL", "")
                                .data("selectfolder", "inbox")
                                .data("keywords", "")
                                .data("page_msg", "20")
                                .data("selected_msgid" + mailId, "true")
                                .data("move_folder", "trash")
                                .data("page", "0")
                                .post();
                    }

                    if (delete) {
                        Document delete = Jsoup.connect(url)
                                .cookies(cookies)
                                .method(Connection.Method.POST)
                                .data("folder", "inbox")
                                .data("order_by", "date")
                                .data("warned", "")
                                .data("formaction", "cleantrash")
                                .data("sorting_method", "desc")
                                .data("mail_id", "")
                                .data("postURL", "")
                                .data("selectfolder", "inbox")
                                .data("keywords", "")
                                .data("page_msg", "20")
                                .data("selected_msgid" + mailId, "true")
                                .data("move_folder", "trash")
                                .data("page", "0")
                                .post();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }
    private static class MoveIntranet extends AsyncTask<Void, Void, Void> {
        Map<String, String> cookies;
        String url;
        Snackbar snackbar;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            url = "https://www.hkmakslo.edu.hk/it-school//php/intra/index.php3";
            cookies = IntranetFragment.getCookies();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {

                String folder = "trash";
                String currentFolder = IntranetFragment.getFolder();
                if (currentFolder.equals("inbox")) {
                    folder = "trash";
                } else if (currentFolder.equals("trash")) {
                    folder = "inbox";
                }
                Log.d("moveIntranet", "selected_msgid" + mailId + " from " + currentFolder + " to " + folder);
                    Document moveToBin = Jsoup.connect(url)
                            .cookies(cookies)
                            .method(Connection.Method.POST)
                            .data("folder", currentFolder)
                            .data("order_by", "date")
                            .data("warned", "")
                            .data("formaction", "move")
                            .data("sorting_method", "desc")
                            .data("mail_id", "")
                            .data("postURL", "")
                            .data("selectfolder", currentFolder)
                            .data("keywords", "")
                            .data("page_msg", "20")
                            .data("selected_msgid" + mailId, "true")
                            .data("move_folder", folder)
                            .data("page", "0")
                            .post();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
        }
    }
    private static void showDeleteDialog() {
        String recyclingBin =
                view.getRootView().getContext().getString(R.string.RecyclingBin);
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
        builder.setTitle(R.string.Delete);
        builder.setMessage(view.getContext().getString(R.string.ConfirmDelete) + mailTitle + " & " + recyclingBin + view.getContext()
                .getString(R.string.Irreversible));
        builder.setIcon(R.drawable.ic_delete);
        builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
            delete = true;
            DeleteIntranet deleteIntranet = new DeleteIntranet();
            deleteIntranet.execute();
            Snackbar.make(view.getRootView(), view.getRootView().getContext().getString(R.string.DeletedSuccessfully) +
                    view.getRootView().getContext().getString(R.string.PleaseReload),
                    Snackbar.LENGTH_LONG).show();
        });
        builder.setNegativeButton(R.string.Cancel, (dialogInterface, i) -> {

        });
        builder.setCancelable(false);
        builder.setOnCancelListener(dialogInterface -> {

        });
        if (IntranetFragment.getFolder().equals("inbox")) {
            builder.setNeutralButton(view.getRootView().getContext().getString(R.string.MoveTo) + recyclingBin,
                    (dialogInterface, i) -> {
                        delete = false;
                        DeleteIntranet deleteIntranet = new DeleteIntranet();
                        deleteIntranet.execute();
                            Snackbar.make(view.getRootView(),
                                    view.getRootView().getContext().getString(R.string.ActionDone)
                                            + view.getRootView().getContext().getString(R.string.PleaseReload),
                                    Snackbar.LENGTH_LONG).show();
                    });
        }
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private static void showMoveDialog() {
        String folder = view.getRootView().getContext().getString(R.string.RecyclingBin);
        if (IntranetFragment.getFolder().equals("inbox")) {
            folder = view.getRootView().getContext().getString(R.string.RecyclingBin);
        } else if (IntranetFragment.getFolder().equals("trash")) {
            folder = view.getRootView().getContext().getString(R.string.Inbox);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getRootView().getContext());
        builder.setTitle(R.string.Move);
        builder.setMessage(view.getContext().getString(R.string.ConfirmMove) + mailTitle  + view.getContext().getString(R.string.To)
                + folder+ view.getContext().getString(R.string.ConfirmQuery));
        builder.setIcon(R.drawable.ic_move);
        builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {
            MoveIntranet moveIntranet = new MoveIntranet();
            moveIntranet.execute();
            Snackbar.make(view.getRootView(),
                    view.getRootView().getContext().getString(R.string.ActionDone) +
                            view.getRootView().getContext().getString(R.string.PleaseReload),
                    Snackbar.LENGTH_LONG).show();
        });
        builder.setNegativeButton(R.string.Cancel, (dialogInterface, i) -> {

        });
        builder.setCancelable(false);
        builder.setOnCancelListener(dialogInterface -> {

        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void filterList(ArrayList<LoginParseItem> filteredList) {
        parseItems = filteredList;
        notifyDataSetChanged();
    }
}