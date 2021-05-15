package com.jason.kslo.parseContent.defaultParseContent.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.text.ICalReader;
import biweekly.property.*;
import com.jason.kslo.R;
import com.jason.kslo.parseContent.defaultParseContent.parseAdapter.ParseAdapterForDashboard;
import com.jason.kslo.parseContent.parseItem.DashboardParseItem;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DashboardFragment extends Fragment {
    View v;
    int size;
    File file;
    ICalReader reader;
    ParseAdapterForDashboard parseAdapterForDashboard;
    final static ArrayList<DashboardParseItem> ParseItems = new ArrayList<>();
    final ArrayList<Date> dates = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sharedPreferences;
    String selectedClass;
    Spinner chooseClass;
    static String icsPositionStr;
    static int icsPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dashboard, container, false);

        Content content = new Content();
        content.execute();
        return v;
    }
    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,Void> {
        DateFormat df;
        RecyclerView recyclerView;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            recyclerView = v.findViewById(R.id.DashboardFragmentRecyclerView);
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

            ICalendar ical;
            df = new SimpleDateFormat("dd/MM/yyyy E HH:mm", Locale.ENGLISH);

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

                            summaryStr = finalSummaryStr+ " " + summaryStr + getString(R.string.AssignedBySystem);
                        }

                        String urlStr = null, desc = null, assignmentCode = null, courseCode;
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
                        String finalDescStr;
                        if (description != null) {
                            desc = description.getValue();
                        }

                        if (dateStartStr != null && dateEndStr != null) {
                            dateStartStr = dateStartStr.replace("00:00", "23:59");
                            dateEndStr = dateEndStr.replace("00:00", "23:59");
                            String finalDateStart = dateStartStr.substring(0, 11);
                            String finalDateEnd = dateEndStr.substring(0, 11);

                            try {
                                Date date = df.parse(dateStartStr);
                                dates.add(date);
                                Log.d("Dates", "ArrayList: " + date);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (dateStartStr.equals(dateEndStr)) {
                                if (summaryStr != null) {
                                    ParseItems.add(new DashboardParseItem(dateStartStr, summaryStr, desc, urlStr));
                                    Log.d("Dashboard", "ParseIcs: " + dateStartStr + summaryStr + " Url: " + urlStr +
                                            " desc: " + desc);
                                }
                            } else {
                                if (finalDateStart.equals(finalDateEnd)) {
                                    dateEndStr = dateEndStr.replace(finalDateEnd,"");
                                }
                                ParseItems.add(new DashboardParseItem(dateStartStr + "-" + dateEndStr, summaryStr, desc, urlStr));
                                Log.d("Dashboard", "ParseIcs: " + dateStartStr + "-" + dateEndStr + summaryStr + " Url: " +
                                        urlStr + " desc: " + desc);
                            }
                        }
                    }
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            Collections.reverse(dates);
            try {
                Date finalDate = Calendar.getInstance().getTime();
                String finalDateStr = df.format(finalDate);
                finalDate = df.parse(finalDateStr);
                getDateNearest(dates, finalDate);
                Log.d("Testing", "currentDate: " + finalDate + " getDateNearest: " + "isComparing " + icsPositionStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Collections.reverse(ParseItems);
            parseAdapterForDashboard.notifyDataSetChanged();
            if (getIcsPosition() > 4) {
                recyclerView.scrollToPosition(getIcsPosition() - 4);
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
    private void getDateNearest(List<Date> dates, Date targetDate){
        int position = 0;
        for (Date date : dates) {
            if (date.compareTo(targetDate) <= 0) {
                icsPositionStr = dates.get(position).toString();
                icsPosition = position;
                return;
            }
            position = position + 1;
        }
    }

    public static String getIcsPositionStr() {
        return icsPositionStr;
    }

    public static int getIcsPosition() {
        return icsPosition;
    }
}