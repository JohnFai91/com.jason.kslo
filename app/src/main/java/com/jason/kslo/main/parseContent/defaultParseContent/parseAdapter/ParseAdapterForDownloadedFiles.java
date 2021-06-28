package com.jason.kslo.main.parseContent.defaultParseContent.parseAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ShareCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.jason.kslo.BuildConfig;
import com.jason.kslo.R;
import com.jason.kslo.main.DownloadView;
import com.jason.kslo.main.parseContent.parseItem.ParseItem;
import com.jason.kslo.parseContent.defaultParseContent.activity.DownloadedFiles;

import java.io.File;
import java.util.ArrayList;

public class ParseAdapterForDownloadedFiles extends RecyclerView.Adapter<ParseAdapterForDownloadedFiles.ViewHolder> {

    private static ArrayList<ParseItem> parseItems = new ArrayList<>();
    static ParseItem parseItem;
    @SuppressLint("StaticFieldLeak")
    static Context context;

    public ParseAdapterForDownloadedFiles(ArrayList<ParseItem> parseItems, Context applicationContext) {
        ParseAdapterForDownloadedFiles.parseItems = parseItems;
        context = applicationContext;
    }

    @NonNull
    @Override
    public ParseAdapterForDownloadedFiles.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.parse_item_for_downloaded_files,parent,false);

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ParseAdapterForDownloadedFiles.ViewHolder holder, int position) {
        parseItem = parseItems.get(position);

        holder.fileName.setText(parseItem.getFileName());

        if (parseItem.getFileSize() > 1024) {
            long fileSizeInKB = parseItem.getFileSize() / 1024;
            if (fileSizeInKB > 1024) {
                long fileSizeInMB = parseItem.getFileSize() / 1024;
                holder.fileSize.setText(fileSizeInMB + "MB");
            } else {
                holder.fileSize.setText(fileSizeInKB + "KB");
            }
        } else {
            holder.fileSize.setText(parseItem.getSize() + "B");
        }

        holder.fileDate.setText(parseItem.getDate() + "");

        holder.fileMore.setOnClickListener(view -> showPopup(view, position));

    }

    @SuppressLint("NonConstantResourceId")
    private void showPopup(View view, int position) {

        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.downloaded_files_popup_menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            popupMenu.setForceShowIcon(true);
        }
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            String fileName = parseItems.get(position).getFileName();

            switch (menuItem.getItemId()) {

                case R.id.downloaded_files_action_popup_delete:
                    showDeleteDialog(fileName);
                    return true;

                case R.id.downloaded_files_action_popup_share:

                    Log.d("Intranet", "delete: " + fileName + " selectedPosition: " + position);

                    String fileType;

                    if (fileName.contains(".doc") || fileName.contains(".docx")) {
                        // Word document
                        fileType = "application/msword";
                    } else if (fileName.contains(".ppt") || fileName.contains(".pptx")) {
                        // Powerpoint file
                        fileType = "application/vnd.ms-powerpoint";
                    } else if (fileName.contains(".xls") || fileName.contains(".xlsx")) {
                        // Excel file
                        fileType = "application/vnd.ms-excel";
                    } else if (fileName.contains(".wav") || fileName.contains(".mp3")) {
                        // WAV audio file
                        fileType = "audio/x-wav";
                    } else if (fileName.contains(".gif")) {
                        // GIF file
                        fileType = "image/gif";
                    } else if (fileName.contains(".jpg") || fileName.contains(".jpeg") || fileName.contains(".png")) {
                        // JPG file
                        fileType = "image/jpeg";
                    } else if (fileName.contains(".txt")) {
                        // Text file
                        fileType = "text/plain";
                    } else if (fileName.contains(".3gp") || fileName.contains(".mpg") ||
                            fileName.contains(".mpeg") || fileName.contains(".mpe") || fileName.contains(".mp4") ||
                            fileName.contains(".avi")) {
                        // Video files
                        fileType = "video/*";
                    } else {
                        fileType = "*/*";
                    }

                    Uri data = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", new File(
                            context.getCacheDir() + "/" + fileName));
                    Intent intent = ShareCompat.IntentBuilder.from((Activity) context)
                            .setStream(data)
                            .setType(fileType)
                            .setChooserTitle("Choose bar")
                            .createChooserIntent()
                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(intent);

                    return true;

                default:
                    return false;
            }
        });
        popupMenu.show();
    }

    @Override
    public int getItemCount() {
        return parseItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView fileName, fileSize, fileDate;
        final ImageButton fileMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fileName = itemView.findViewById(R.id.DownloadedFileName);
            fileSize = itemView.findViewById(R.id.DownloadedFileSize);
            fileDate = itemView.findViewById(R.id.DownloadedFileDate);
            fileMore = itemView.findViewById(R.id.DownloadedFileMore);

            itemView.setOnClickListener(this);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Intent intent = new Intent(context, DownloadView.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("title", parseItems.get(position).getFileName());

            intent.putExtra("origin", "OpenFile");

            context.startActivity(intent);
        }
    }
        private void showDeleteDialog(String fileName) {
            String recyclingBin =
                    context.getString(R.string.RecyclingBin);
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.Delete);
            builder.setMessage(builder.getContext().getString(R.string.ConfirmDelete) + fileName + builder.getContext()
                    .getString(R.string.Irreversible));
            builder.setIcon(R.drawable.ic_delete);
            builder.setPositiveButton(android.R.string.yes, (dialogInterface, i) -> {

                File file = new File(context.getCacheDir() + "/" + fileName);
                if (file.exists()) {
                    if (file.delete()) {
                        DownloadedFiles.ReloadPage();
                    }
                }
            });
            builder.setNegativeButton(R.string.Cancel, (dialogInterface, i) -> {

            });
            builder.setCancelable(false);
            builder.setOnCancelListener(dialogInterface -> {

            });
            builder.show();
        }
}