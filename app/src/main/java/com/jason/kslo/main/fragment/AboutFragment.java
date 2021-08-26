package com.jason.kslo.main.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.jason.kslo.BuildConfig;
import com.jason.kslo.R;
import com.jason.kslo.autoUpdate.AppUtils;
import com.jason.kslo.autoUpdate.UpdateChecker;
import com.jason.kslo.changelog.ChangelogActivity;
import com.jason.kslo.main.parseContent.defaultParseContent.activity.DownloadedFiles;
import com.jason.kslo.main.activity.SettingsActivity;
import com.jason.kslo.main.dialog.InstallUnknownAppsDialog;
import com.jason.kslo.main.pdfView.download.PdfViewFeaturedNotice;
import com.jason.kslo.main.pdfView.download.PdfViewSchedule;
import com.jason.kslo.main.pdfView.PdfViewSchoolCal;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.Random;

import static android.content.Context.MODE_PRIVATE;


public class AboutFragment extends Fragment {
    View view;
    SharedPreferences pref;
    String locale,theme,versionVar;
    TextView version,Locale,ThemeText,desc;
    Button ChangeLogButton,SchoolCal,FeaturedNotice,HalfDaySchedule,Settings,CheckForUpdate,SourceCode,CrashApp,downloadedFiles,
    JoinDevelopment, Website, ShareApp;
    ImageView schoolIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);
        requireContext().getTheme().applyStyle(R.style.AppTheme_MaterialComponents,
                true);
            pref = requireContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            locale = pref.getString("lang","en");
            theme = pref.getString("theme","Follow System");
            versionVar = BuildConfig.APPLICATION_ID + " " + AppUtils.getVersionName(getContext());

            Locale = view.findViewById(R.id.Locale);
            ThemeText = view.findViewById(R.id.Theme);
            version = view.findViewById(R.id.Version);
            ChangeLogButton = view.findViewById(R.id.button_ChangeLog);
            SchoolCal = view.findViewById(R.id.SchoolCal);
            FeaturedNotice = view.findViewById(R.id.Featured_Notice);
            HalfDaySchedule = view.findViewById(R.id.Half_Day_Schedule);
            Settings = view.findViewById(R.id.Settings_button);
            SourceCode = view.findViewById(R.id.button_SourceCode);
            CheckForUpdate = view.findViewById(R.id.CheckForUpdate);
            CrashApp = view.findViewById(R.id.CrashApp);
            desc = view.findViewById(R.id.description);
            schoolIcon = view.findViewById(R.id.School_Logo);
            downloadedFiles = view.findViewById(R.id.showDownloadedFiles);
            JoinDevelopment = view.findViewById(R.id.button_JoinDevelopment);
            Website = view.findViewById(R.id.button_myWebsite);
            ShareApp = view.findViewById(R.id.shareApp);

            Content content = new Content();
            content.run();
        return view;
        }

    void openInstallUnknownDialog(){
        InstallUnknownAppsDialog installUnknownAppsDialog = new InstallUnknownAppsDialog();
        installUnknownAppsDialog.setCancelable(false);
            installUnknownAppsDialog.show(requireActivity().getSupportFragmentManager(), "InstallUnknownAppsDialog");
    }

    class Content implements Runnable{
        @Override
        public void run() {// create an instance of random.
            Random random = new Random();
            String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            alphabet = alphabet + alphabet.toLowerCase(java.util.Locale.ROOT);
            // convert the string to a char array
            char[] alphabetArr = alphabet.toCharArray();
            //set the max number for the int to be the length of the string.

            StringBuilder randomCode = new StringBuilder();
            for (int i = 0; i < 30; i++) {
                int randomInt = random.nextInt(alphabet.length());
                   randomCode.append(alphabetArr[randomInt] + randomInt);
            }

            schoolIcon.setOnClickListener(view -> {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JohnFai91/com.jason.kslo"));
                startActivity(intent);
            });
            Picasso.get().load(R.drawable.school_logo).into(schoolIcon);

            version.setText(versionVar);
            Locale.setText(locale);
            ThemeText.setText(theme);

            ChangeLogButton.setOnClickListener(view1 -> {
                    Intent intent = new Intent(getContext(), ChangelogActivity.class);
                    startActivity(intent);
            });

            SchoolCal.setOnClickListener(view12 -> {
                Intent intent = new Intent(getContext(), PdfViewSchoolCal.class);
                startActivity(intent);

            });

            FeaturedNotice.setOnClickListener(view13 -> {
                Intent intent = new Intent(getContext(), PdfViewFeaturedNotice.class);
                startActivity(intent);
            });

            HalfDaySchedule.setOnClickListener(view14 -> {
                Intent intent = new Intent(getContext(), PdfViewSchedule.class);
                startActivity(intent);
            });

            Settings.setOnClickListener(view15 -> {
                Intent intent = new Intent(getContext(), SettingsActivity.class);
                startActivity(intent);
            });

            SourceCode.setOnClickListener(view17 -> {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JohnFai91/com.jason.kslo"));
                startActivity(browserIntent);
            });

            CheckForUpdate.setOnClickListener(view18 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (!requireContext().getPackageManager().canRequestPackageInstalls()){
                        openInstallUnknownDialog();
                    }
                    else{
                        UpdateChecker.checkForDialog(getContext());
                    }
                }
            });

            CrashApp.setOnClickListener(view19 -> {
                    throw new RuntimeException(getString(R.string.YouPressedTheCrashAppButton));
            });

            downloadedFiles.setOnClickListener(view -> startActivity(new Intent(getActivity(), DownloadedFiles.class)));

            JoinDevelopment.setOnClickListener(view -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://signal.group/#CjQKIOvHZXoDBVldMPF9VqJJAt2JjSRiUptLsto_Rj-0CMR2EhC6SG6a08ubmRndGuP7bqKE"));
                startActivity(i);
            });

            Website.setOnClickListener(view -> {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://johnfai91.github.io/com.jason.kslo/"));
                startActivity(i);
            });

            ShareApp.setOnClickListener(view -> {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "com.jason.kslo");
                String shareMessage= getString(R.string.CheckThisOut) + "\n";
                shareMessage = shareMessage + "https://github.com/JohnFai91/KSLO_Installer/releases/download/latest/latest.apk";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "choose one"));
            });
        }
    }
}
