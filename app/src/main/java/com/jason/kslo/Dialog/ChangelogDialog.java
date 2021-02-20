package com.jason.kslo.Dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ChangelogDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Changelog")
                        .setMessage("Alpha State (Current) (Internal release to 2 people)\n" +
                                        "\n" +
                                        "Future\n" +
                                        "Resolve intranet bad UI\n" +
                                        "Give up webview to improve performance in the following order (Login, School Website, Dashboard)"+
                                        "\n" +
                                        "\n" +
                                        "1.0.5-alpha (Current) (Code update)\n"+
                                        "Auto internal updater\n" +
                                        "Settings\n" +
                                        "Support for dark theme(only for \"About\" and \"Settings\" screens because others use webview, will eventually support all after giving uo webview)\n" +
                                        "Add \"Splash Screen\" to cover loading time"+
                                        "\n" +
                                        "\n" +
                                        "1.0.4 alpha (Code update)\n" +
                                        "Resolve internal code warnings\n" +
                                        "Translation for Chinese\n" +
                                        "Improve \"About\" UI" + "\n" +
                                        "\n" +
                                        "1.0.3 alpha (UI update)\n" +
                                        "Improve dashboard performance\n" +
                                        "Resolve fragments not inflating for larger screens\n" +
                                        "Resolve elements not always blocked\n" +
                                        "Resolve blank documents to be able to be opened\n" +
                                        "Implement \"About\" tab\n" +
                                        "Divide \"Home\" into \"Login\" and \"School Website\" tabs\n" +
                                        "Resolve calendar wrong time problem (new calendar)\n" +
                                        "Remove unused permissions\n" +
                                        "Support orientation\n" +
                                        "Support offline Pdf Viewer (only for School Calendar, 3C Half Day Schedule and The Notice of the month)\n" +
                                        "\n" +
                                        "1.0.2 alpha (Initial) (UI update)\n" +
                                        "Resolve javascript website keep popping up (Home, login)"
                                )
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
        return builder.create();
    }
}