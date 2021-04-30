package com.jason.kslo.parseContent.defaultParseContent.fragment;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.text.ICalReader;
import biweekly.property.*;
import com.jason.kslo.R;
import com.jason.kslo.autoUpdate.UpdateChecker;
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.main.activity.SettingsActivity;
import com.jason.kslo.main.dialog.InstallUnknownAppsDialog;
import com.jason.kslo.parseContent.defaultParseContent.parseAdapter.ParseAdapterForDashboard;
import com.jason.kslo.parseContent.parseItem.DashboardParseItem;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DashboardFragment extends Fragment {
    View v;
    int size;
    File file;
    ICalReader reader;
    ParseAdapterForDashboard parseAdapterForDashboard;
    final ArrayList<DashboardParseItem> ParseItems = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sharedPreferences;
    String selectedClass;
    Spinner chooseClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dashboard, container, false);
        checkUpdate();

        Content content = new Content();
        content.execute();
        return v;
    }
    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            RecyclerView recyclerView = v.findViewById(R.id.DashboardFragmentRecyclerView);
            recyclerView.setHasFixedSize(true);
            LinearLayoutManager layoutVerticalManager
                    = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutVerticalManager);

            parseAdapterForDashboard = new ParseAdapterForDashboard(ParseItems, getContext());
            recyclerView.setAdapter(parseAdapterForDashboard);

            swipeRefreshLayout = v.findViewById(R.id.DashboardRefresh);
            swipeRefreshLayout.setOnRefreshListener(() -> {
                Content content = new Content();
                content.execute();
            });
            swipeRefreshLayout.setColorSchemeColors(
                    requireActivity().getResources().getColor(android.R.color.holo_blue_dark),
                    requireActivity().getResources().getColor(android.R.color.holo_orange_dark),
                    requireActivity().getResources().getColor(android.R.color.holo_green_dark),
                    requireActivity().getResources().getColor(android.R.color.holo_red_dark)
            );
            sharedPreferences = requireContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
            selectedClass = sharedPreferences.getString("Class", "3C");
            chooseClass = v.findViewById(R.id.DashboardFragmentTitle);
        String[] classes = {
                requireActivity().getString(R.string.Dashboard) + " (" + selectedClass + ")","2D", "3C"
        };
        ArrayAdapter<? extends String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, classes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseClass.setAdapter(adapter);

            chooseClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                 @Override
                 public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                     switch (position) {
                         case 1:
                             sharedPreferences.edit().putString("Class","2D").apply();
                             Content content = new Content();
                             content.execute();
                             break;
                         case 2:
                             sharedPreferences.edit().putString("Class","3C").apply();
                             content = new Content();
                             content.execute();
                             break;
                     }
                 }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            swipeRefreshLayout.setRefreshing(true);
            ParseItems.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            downloadIcs();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);


            ICalendar ical;
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

            try {
                reader = new ICalReader(new File(requireActivity().getCacheDir() + "/" + "Hw.ics"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
            while ((ical = reader.readNext()) != null) {

                    for (VEvent event : ical.getEvents()) {
                        DateStart dateStart = event.getDateStart();
                        DateEnd dateEnd = event.getDateEnd();
                        String dateStartStr = (dateStart == null) ? null : df.format(dateStart.getValue());
                        String dateEndStr = (dateEnd == null) ? null : df.format(dateEnd.getValue());

                        Summary summary = event.getSummary();
                        String summaryStr = (summary == null) ? null : summary.getValue();
                        if (summaryStr != null && summaryStr.contains("[2020-21 ")) {
                            String finalSummaryStr = summaryStr.substring(summaryStr.indexOf("[2020-21 "));
                            summaryStr = summaryStr.replace(finalSummaryStr,"");

                            finalSummaryStr = finalSummaryStr.replace("2020-21 ","");
                            if (finalSummaryStr.contains(selectedClass)) {
                                finalSummaryStr = finalSummaryStr.replace(selectedClass + " ", "");
                            }

                            if (finalSummaryStr.contains("Science (")) {
                                finalSummaryStr = finalSummaryStr.replace("Science (","");
                                finalSummaryStr = finalSummaryStr.replace(finalSummaryStr.substring(finalSummaryStr.lastIndexOf(")"))
                                        ,"");
                                finalSummaryStr = finalSummaryStr + "]";
                            }

                            summaryStr = finalSummaryStr+ " " + summaryStr + getString(R.string.AssignedBySystem) ;
                        }

                        String urlStr = null, desc = null, assignmentCode = null, courseCode = null;
                        Url url = event.getUrl();
                        Description description = event.getDescription();
                        if (url != null) {
                            urlStr = url.getValue();
                            if (urlStr.contains("#calendar_event_")){
                                urlStr = null;
                            } else if (urlStr.contains("course")) {
                                courseCode = urlStr.substring(urlStr.indexOf("course"));
                                courseCode = courseCode.replace(courseCode.substring(courseCode.indexOf("&month=")),"");

                                if (urlStr.contains("#assignment_")) {
                                    assignmentCode = urlStr.substring(urlStr.indexOf("#assignment_"));
                                    assignmentCode = assignmentCode.replace("#assignment_","");
                                }
                                if (courseCode.contains("course_")) {
                                    courseCode = courseCode.replace("course_", "");
                                }

                                if (assignmentCode.contains("#assignment_")) {
                                    assignmentCode = assignmentCode.replace("#assignment_", "");
                                }

                                urlStr = "https://hkedcity.instructure.com/courses/" + courseCode + "/assignments/" + assignmentCode;
                            }
                        }
                        if (description != null) {
                            desc = description.getValue();
                        }

                        if (dateStartStr != null && dateEndStr != null) {
                            dateStartStr = dateStartStr.replace("00:00", "23:59");
                            dateEndStr = dateEndStr.replace("00:00", "23:59");
                            String finalDateStart = dateStartStr.substring(0, 11);
                            String finalDateEnd = dateEndStr.substring(0, 11);

                            if (dateStartStr.equals(dateEndStr)) {
                                if (summaryStr != null) {
                                    ParseItems.add(new DashboardParseItem(dateEndStr, summaryStr, desc, urlStr));
                                    Log.d("Dashboard", "ParseIcs: " + dateEndStr + summaryStr + " Url: " + urlStr +
                                            " desc: " + desc);
                                }
                            } else {
                                ParseItems.add(new DashboardParseItem(dateStartStr + "-" +dateEndStr, summaryStr, desc, urlStr));
                                Log.d("Dashboard", "ParseIcs: " + dateStartStr + "-" +dateEndStr + summaryStr + " Url: " +
                                        urlStr + " desc: " + desc);
                            }
                        }
                    }
                }
                if (reader != null) {
                    reader.close();
                }

                Collections.reverse(ParseItems);
                parseAdapterForDashboard.notifyDataSetChanged();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            swipeRefreshLayout.setRefreshing(false);
        }

        void downloadIcs() {
            String url;
            try {
                if (selectedClass.equals("3C")) {
                    //3C
                    url = "https://hkedcity.instructure.com/feeds/calendars/user_r3rBBJB2zCiooISGiBIEalWRDfnn4xTBdKxgEPr9.ics";
                } else {
                    //2D
                    url = "https://hkedcity.instructure.com/feeds/calendars/user_mbha2FngAAEJI9IzszNwngot4tcJzkNRKp2hgCaz.ics";
                }
                Connection.Response resultImageResponse;
                resultImageResponse = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .execute();
                // output here
                FileOutputStream out = (new FileOutputStream(requireActivity().getCacheDir() + "/" + "Hw.ics"));
                out.write(resultImageResponse.bodyAsBytes());
                out.close();
                Log.d("Dashboard", "downloadIcs: " + "finish");
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }
    }

    public void checkUpdate() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (!requireActivity().getPackageManager().canRequestPackageInstalls()) {
                InstallUnknownAppsDialog installUnknownAppsDialog = new InstallUnknownAppsDialog();
                installUnknownAppsDialog.setCancelable(false);
                installUnknownAppsDialog.show(requireActivity().getSupportFragmentManager(), "ChangelogDialog");
            } else {
                UpdateChecker.checkForDialog(getContext());
            }
        }
    }
}