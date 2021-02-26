package com.jason.kslo.Dialog;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class ChangelogDialog extends AppCompatDialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                builder.setTitle("Changelog (Only in English)")
                        .setMessage(    "Future (Beta State) (3 people in state) (Improve UI and performances)\n" +
                                        "Resolve intranet bad UI\n" +
                                        "Give up webview to improve performance in the following order (Login, School Website, Dashboard)\n"+
                                        "Auto internal updater\n" +
                                        "\n" +
                                        "\n" +
                                        "Alpha State (Current) (2 people in state) (Basic features)\n" +
                                        "\n" +
                                        "i = Implemented, v = Version, r = got replaced by, d = Deprecated, c = Concept\n" + "\n" +
                                        "\n" +
                                        "Screens\n" +
                                        "\n" +
                                        "Dashboard (i in v.1.0.1 alpha)\n" +
                                        "Home (i in v.1.0.1 alpha but r \"Login\" and \"School Website\")\n" +
                                        "Library (i in v.1.0.1 alpha but d in v.1.0.3 alpha)\n" +
                                        "\"Login\" and \"School website\" (i in v.1.0.3 alpha)\n"+
                                        "\"About\" (i in v.1.0.3 alpha)\n" +
                                        "\"View\" Pdf (i in v.1.0.3 alpha)\n" +
                                        "\"Offline Pdf view\" (i in v.1.0.3 alpha)\n" +
                                        "\"Settings\" (i in v.1.0.5-alpha)\n" +
                                        "\"Splash screen\" (c in v.1.0.5-alpha but d for wasting time by author)\n" +
                                        "\n" +
                                        "Features\n" +
                                        "\n" +
                                        "Design in \"About\"(i in v.1.0.4 alpha)\n" +
                                        "Support for Chinese (i in v.1.0.4 alpha)\n" +
                                        "Support for dark theme (i on v.1.0.5-alpha)\n" +
                                        "Change language and theme manually (i v.1.0.5-alpha)\n" +
                                        "\n" +
                                        "Versions\n" +
                                        "\n" +
                                        "1.0.5-alpha (Current) (UI update) (End of alpha state)\n"+
                                        "Add \"Settings\" for changing between languages and themes\n" +
                                        "Support for dark theme\n" +
                                        "Improve pdf viewer by adding a scroll bar and page separator\n" +
                                        "\n" +
                                        "1.0.4 alpha (UI update)\n" +
                                        "Add translation for Chinese\n" +
                                        "Improve \"About\" UI" + "\n" +
                                        "\n" +
                                        "1.0.3 alpha (UI and code update)\n" +
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
                                        "1.0.2 alpha (Initial release and start of alpha state) (Code update)\n" +
                                        "Resolve javascript website keep popping up (\"Home\", after login)"
                                )
                        .setPositiveButton("ok", (dialogInterface, i) -> {

                        });
        return builder.create();
    }
}