package com.jason.kslo.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import com.jason.kslo.AutoUpdate.AppUtils;
import com.jason.kslo.AutoUpdate.UpdateChecker;
import com.jason.kslo.Dialog.ChangelogDialog;
import com.jason.kslo.Intro.SlideActivity;
import com.jason.kslo.PdfView.PdfViewFeaturedNotice;
import com.jason.kslo.PdfView.PdfViewHalfDaySchedule;
import com.jason.kslo.PdfView.PdfViewSchoolCal;
import com.jason.kslo.Activity.SettingsActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.jason.kslo.*;

import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("MyPref", MODE_PRIVATE);
        String locale = pref.getString("lang","zh");

        TextView Locale = view.findViewById(R.id.Locale);
        Locale.setText(locale);

        String Version = AppUtils.getVersionName(getContext());
        TextView version = view.findViewById(R.id.Version);
        version.setText(Version);

        String theme = pref.getString("theme","Follow System");

        TextView ThemeText = view.findViewById(R.id.Theme);
        ThemeText.setText(theme);

        Button ChangeLogButton = view.findViewById(R.id.button_ChangeLog);
        ChangeLogButton.setOnClickListener(view1 -> openDialog());

        Button SchoolCal = view.findViewById(R.id.SchoolCal);
        SchoolCal.setOnClickListener(view12 -> {
            Intent intent = new Intent(getContext(), PdfViewSchoolCal.class);
            startActivity(intent);

        });

        Button FeaturedNotice = view.findViewById(R.id.Featured_Notice);
        FeaturedNotice.setOnClickListener(view13 -> {
            Intent intent = new Intent(getContext(), PdfViewFeaturedNotice.class);
            startActivity(intent);
        });

        Button HalfDaySchedule = view.findViewById(R.id.Half_Day_Schedule);
        HalfDaySchedule.setOnClickListener(view14 -> {
            Intent intent = new Intent(getContext(), PdfViewHalfDaySchedule.class);
            startActivity(intent);
        });

        Button Settings = view.findViewById(R.id.Settings_button);
        Settings.setOnClickListener(view15 -> {
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);
        });

        Button Intro = view.findViewById(R.id.Intro_Button);
        Intro.setOnClickListener(view16 -> {
            SharedPreferences.Editor editor = Objects.requireNonNull(getActivity()).getSharedPreferences("slide",MODE_PRIVATE).edit();
            editor.putBoolean("slide", false);
            editor.commit();
            Intent intent = new Intent(getContext(), SlideActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        Button SourceCode = view.findViewById(R.id.button_SourceCode);
        SourceCode.setOnClickListener(view17 -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/JohnFai91/com.jason.kslo"));
            startActivity(browserIntent);
        });

        Button CheckForUpdate = view.findViewById(R.id.CheckForUpdate);
        CheckForUpdate.setOnClickListener(view17 -> {
            UpdateChecker.checkForDialog(getContext());
        });

        return view;
    }
    void openDialog(){
        ChangelogDialog changelogDialog = new ChangelogDialog();
            changelogDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "ChangelogDialog");
    }

}
