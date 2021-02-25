package com.jason.kslo.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import com.jason.kslo.Dialog.ChangelogDialog;
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


public class AboutFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        SharedPreferences pref = Objects.requireNonNull(getContext()).getSharedPreferences("MyPref",Context.MODE_PRIVATE);
        String locale = pref.getString("lang","zh");

        TextView Locale = view.findViewById(R.id.Locale);
        Locale.setText(locale);

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

        return view;
    }
    void openDialog(){
        ChangelogDialog changelogDialog = new ChangelogDialog();
            changelogDialog.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "ChangelogDialog");
    }

}
