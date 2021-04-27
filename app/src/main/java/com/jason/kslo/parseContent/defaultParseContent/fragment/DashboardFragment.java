package com.jason.kslo.parseContent.defaultParseContent.fragment;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import biweekly.ICalendar;
import biweekly.component.VEvent;
import biweekly.io.text.ICalReader;
import biweekly.property.DateEnd;
import biweekly.property.DateStart;
import biweekly.property.Summary;
import com.jason.kslo.R;
import com.jason.kslo.parseContent.defaultParseContent.parseAdapter.ParseAdapterForDashboard;
import com.jason.kslo.parseContent.loggedInParseContent.fragment.IntranetFragment;
import com.jason.kslo.parseContent.parseItem.DashboardParseItem;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.*;
import java.net.*;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_dashboard, container, false);

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

        Content content = new Content();
        content.execute();
        return v;
    }
    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
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
                reader = new ICalReader(new File(Objects.requireNonNull(getActivity()).getCacheDir() + "/" + "Hw.txt"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Log.d("Crashing", "Here: 1 " + reader);
            try {
            while ((ical = reader.readNext()) != null) {

                    Log.d("Crashing", "Here: 2");
                    for (VEvent event : ical.getEvents()) {
                        Log.d("Crashing", "Here: 3");
                        DateStart dateStart = event.getDateStart();
                        DateEnd dateEnd = event.getDateEnd();
                        String dateStartStr = (dateStart == null) ? null : df.format(dateStart.getValue());
                        String dateEndStr = (dateEnd == null) ? null : df.format(dateEnd.getValue());

                        Summary summary = event.getSummary();
                        String summaryStr = (summary == null) ? null : summary.getValue();
                        if (summaryStr != null && summaryStr.contains("[2020-21 3C ")) {
                            summaryStr = summaryStr.replace(summaryStr.substring(summaryStr.indexOf("[2020-21 3C ")),
                                    getString(R.string.AssignedBySystem));
                        }

                        if (dateStartStr != null && dateEndStr != null) {
                            dateStartStr = dateStartStr.replace("00:00", "23:59");
                            dateEndStr = dateEndStr.replace("00:00", "23:59");
                            String finalDateStart = dateStartStr.substring(0, 11);
                            String finalDateEnd = dateEndStr.substring(0, 11);

                            if (dateStartStr.equals(dateEndStr)) {
                                if (summaryStr != null) {
                                    ParseItems.add(new DashboardParseItem(dateEndStr, summaryStr));
                                }
                            } else if (finalDateStart.equals(finalDateEnd)) {
                                dateEndStr = dateEndStr.replace(finalDateEnd, "");
                                ParseItems.add(new DashboardParseItem(dateStartStr + "-" +dateEndStr, summaryStr));
                            } else {
                                ParseItems.add(new DashboardParseItem(dateStartStr + "-" +dateEndStr, summaryStr));
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
                url = "https://hkedcity.instructure.com/feeds/calendars/user_r3rBBJB2zCiooISGiBIEalWRDfnn4xTBdKxgEPr9.ics";
                Connection.Response resultImageResponse;
                resultImageResponse = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .execute();
                // output here
                FileOutputStream out = (new FileOutputStream(Objects.requireNonNull(getActivity()).getCacheDir() + "/" + "Hw.txt"));
                out.write(resultImageResponse.bodyAsBytes());
                out.close();
                Log.d("Dashboard", "downloadIcs: " + "finish");
            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
        }
    }
}