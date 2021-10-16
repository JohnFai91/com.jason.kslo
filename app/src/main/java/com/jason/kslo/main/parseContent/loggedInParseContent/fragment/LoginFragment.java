package com.jason.kslo.main.parseContent.loggedInParseContent.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.jason.kslo.R;
import com.jason.kslo.main.activity.MainActivity;
import com.jason.kslo.main.parseContent.loggedInParseContent.activity.LoginActivity;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseAdapter.ParseAdapterForBooks;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseAdapter.ParseAdapterForIntranet;
import com.jason.kslo.main.parseContent.loggedInParseContent.parseItem.LoginParseItem;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoginFragment extends Fragment {

    static ParseAdapterForBooks parseAdapterForBooks;
    static ParseAdapterForIntranet parseAdapterForIntranet;
    static ArrayList<LoginParseItem> loginParseItems = new ArrayList<>(), secondLoginParseItems = new ArrayList<>();
    static int pageNo, totalPageNo;
    static String Username, originalPw, finalUsername, eClassPassword;
    static Map<String, String> Cookies = new HashMap<>();

    SharedPreferences pref;
    View view;
    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView borrowedBooksRecyclerView, IntranetRecyclerView;
    TextView borrowedBooksTitle, IntranetTitle, IntranetReachedBottom;
    Button loginButton;
    ProgressBar borrowedBooksProgressBar, IntranetProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_login, container, false);

        pref = requireActivity().getSharedPreferences("MyPref", Context.MODE_PRIVATE);

        originalPw = pref.getString("Password","");
        Username = pref.getString("Username","");
        eClassPassword = pref.getString("eClassPassword","");
        finalUsername = Username.replaceAll("s","");

        borrowedBooksProgressBar = view.findViewById(R.id.BorrowedBooksProgressBar);
        IntranetProgressBar = view.findViewById(R.id.IntranetProgressBar);
        IntranetTitle = view.findViewById(R.id.IntranetTitle);
        borrowedBooksTitle = view.findViewById(R.id.BorrowedBooksFragmentTitle);
        IntranetRecyclerView = view.findViewById(R.id.IntranetRecyclerView);
        RecyclerView borrowedBooksRecyclerView = view.findViewById(R.id.BorrowedBooksRecyclerView);

        pageNo = 1;

        LinearLayoutManager layoutHorizontalManager
                = new LinearLayoutManager(borrowedBooksRecyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        borrowedBooksRecyclerView.setLayoutManager(layoutHorizontalManager);
        LinearLayoutManager layoutVerticalManager
                = new LinearLayoutManager(IntranetRecyclerView.getContext(),
                LinearLayoutManager.VERTICAL, false);
        IntranetRecyclerView.setLayoutManager(layoutVerticalManager);

        parseAdapterForBooks = new ParseAdapterForBooks(secondLoginParseItems, borrowedBooksRecyclerView.getContext());
        borrowedBooksRecyclerView.setAdapter(parseAdapterForBooks);
        parseAdapterForIntranet = new ParseAdapterForIntranet(loginParseItems, requireActivity());
        IntranetRecyclerView.setAdapter(parseAdapterForIntranet);

        swipeRefreshLayout = view.findViewById(R.id.BorrowedBooksSwipeRefresh);
        loginButton = view.findViewById(R.id.LoginButton);
        loginButton.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), LoginActivity.class);
            startActivity(intent);
        });
        if (Username.isEmpty()||originalPw.isEmpty()||eClassPassword.isEmpty()) {
            swipeRefreshLayout.setEnabled(false);
            loginButton.setVisibility(View.VISIBLE);
            borrowedBooksProgressBar.setVisibility(View.GONE);
            IntranetProgressBar.setVisibility(View.GONE);
        } else {
            loginButton.setVisibility(View.VISIBLE);
            borrowedBooksProgressBar.setVisibility(View.VISIBLE);
            IntranetProgressBar.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setColorSchemeColors(
                    requireActivity().getResources().getColor(android.R.color.holo_blue_dark),
                    requireActivity().getResources().getColor(android.R.color.holo_orange_dark),
                    requireActivity().getResources().getColor(android.R.color.holo_green_dark),
                    requireActivity().getResources().getColor(android.R.color.holo_red_dark));

            loginButton.setVisibility(View.GONE);

            Content content = new Content();
            content.execute();
            swipeRefreshLayout.setOnRefreshListener(() -> {
                pageNo = 1;
                Content con = new Content();
                con.execute();
            });
        }

        IntranetReachedBottom = view.findViewById(R.id.IntranetTouchedBottom);
        IntranetRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!IntranetRecyclerView.canScrollVertically(1)) {
                    ReloadIntranet reloadIntranet = new ReloadIntranet();
                    reloadIntranet.execute();
                }
            }
        });
        return view;
    }

    @SuppressLint("StaticFieldLeak")
    private class ReloadIntranet extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            IntranetProgressBar.setVisibility(View.VISIBLE);
            pageNo = pageNo + 1;
            if (pageNo < totalPageNo) {
                IntranetReachedBottom.setVisibility(View.GONE);
            } else {
                IntranetRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (!IntranetRecyclerView.canScrollVertically(1)) {
                            IntranetReachedBottom.setVisibility(View.VISIBLE);
                        } else {
                            IntranetReachedBottom.setVisibility(View.GONE);
                        }
                    }
                });
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (pageNo < totalPageNo) {
                parseIntranet();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);
            IntranetProgressBar.setVisibility(View.GONE);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class Content extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            IntranetProgressBar.setVisibility(View.VISIBLE);
            IntranetTitle.setText(getString(R.string.Intranet));
            borrowedBooksProgressBar.setVisibility(View.VISIBLE);
            borrowedBooksTitle.setText(getString(R.string.BorrowedBooks));

            SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.BorrowedBooksSwipeRefresh);
            swipeRefreshLayout.setRefreshing(true);

            secondLoginParseItems.clear();
            loginParseItems.clear();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            parseLm();
            parseIntranet();
            return null;
        }

        @Override
        protected void onPostExecute(Void unused) {
            super.onPostExecute(unused);

            parseAdapterForIntranet.notifyDataSetChanged();
            parseAdapterForBooks.notifyDataSetChanged();
            SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.BorrowedBooksSwipeRefresh);
            swipeRefreshLayout.setRefreshing(false);

            IntranetProgressBar.setVisibility(View.GONE);
            borrowedBooksProgressBar.setVisibility(View.GONE);

            borrowedBooksTitle.setText(getString(R.string.BorrowedBooksNumber, String.valueOf(secondLoginParseItems.size())));


            }
        }

    public void parseLm(){
        String originalPw, Username, finalUsername;
        Map<String,String> cookies;
        int BookSize;
        try {
            SharedPreferences pref = MainActivity.getContextOfApplication()
                    .getSharedPreferences("MyPref", Context.MODE_PRIVATE);

            originalPw = pref.getString("Password", "");
            Username = pref.getString("Username", "");
            finalUsername = Username.replaceAll("s", "");
            Connection.Response loginForm = Jsoup.connect("https://lm.hkmakslo.edu.hk/Handlers/UserLogin.ashx?&sno=" +
                            finalUsername + "&pass=" + originalPw)
                    .method(Connection.Method.GET)
                    .execute();

            if (loginForm.body().contains("Warn")) {
                requireActivity().runOnUiThread(() -> {
                    TextView booksErrorMsg = view.findViewById(R.id.books_error_msg);
                    booksErrorMsg.setVisibility(View.VISIBLE);
                });
            } else {
                requireActivity().runOnUiThread(() -> {
                    TextView booksErrorMsg = view.findViewById(R.id.books_error_msg);
                    booksErrorMsg.setVisibility(View.GONE);
                });


                cookies = loginForm.cookies();

                Document doc = Jsoup.connect("https://lm.hkmakslo.edu.hk/PrivatePages/_book_table.aspx?")
                        .cookies(cookies)
                        .get();

                Log.d("Parse Lm", "Cookies: " + loginForm.cookies());

                Elements bk = doc.select("a");

                BookSize = bk.size();
                Log.d("ParseLM", "Size: " + BookSize);

                for (int i = 0; i < BookSize; i++) {

                    String bkTitle = bk
                            .eq(i)
                            .text();

                    String bkImg = doc
                            .select("img.book-cover-tbumb")
                            .eq(i)
                            .attr("src");

                    bkImg = bkImg.replace("..", "");
                    bkImg = "https://lm.hkmakslo.edu.hk" + bkImg;

                    String bkBorrowedDate = doc
                            .select("td.borrowed-date")
                            .eq(i)
                            .text();

                    String bkReturnDate = doc
                            .select("td.return-date")
                            .eq(i)
                            .text();

                    String countReturn = doc.select("span.due")
                            .eq(i)
                            .text();

                    String detailUrl = doc
                            .select("span.row")
                            .select("a.book-name")
                            .eq(i)
                            .attr("href");
                    String baseDetailUrl = "https://lm.hkmakslo.edu.hk/Pages/";
                    detailUrl = baseDetailUrl + detailUrl;

                    countReturn = countReturn.replace("天後到期", requireContext().getString(R.string.DaysToDueDate));

                    bkReturnDate = bkReturnDate + " (" + countReturn + ")";
                    secondLoginParseItems.add(new LoginParseItem(bkTitle, bkImg, bkBorrowedDate, bkReturnDate, detailUrl, loginForm.cookies()));
                    Log.d("Parse Lm", "Bk: " + " title: " + bkTitle + " imgUrl: " + bkImg +
                            " borrowed date: " + bkBorrowedDate + " return date: " + bkReturnDate);


                    requireActivity().runOnUiThread(() -> parseAdapterForBooks.notifyDataSetChanged());
                }
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void parseIntranet() {
        try {
            Username = pref.getString("Username", "");
            eClassPassword = pref.getString("eClassPassword", "");

            Connection.Response templates = Jsoup.connect("https://eclass.hkmakslo.edu.hk/login.php/")
                    .method(Connection.Method.POST)
                    .execute();
            String securetoken = templates.body();
            securetoken = securetoken.substring(securetoken.indexOf("<input type=\"hidden\" name=\"securetoken\" value=\""))
                    .replace("<input type=\"hidden\" name=\"securetoken\" value=\"", "");
            securetoken = securetoken.replace(securetoken.substring(securetoken.indexOf("\"/></form>")), "");
            securetoken = securetoken.replace("\"/></form>", "");

            Connection.Response response = Jsoup.connect("https://eclass.hkmakslo.edu.hk/login.php")
                    .data("UserLogin", Username)
                    .data("UserPassword", eClassPassword)
                    .data("url", "/templates/index.php?err=1&DirectLink=")
                    .data("securetoken", securetoken)
                    .cookies(templates.cookies())
                    .method(Connection.Method.POST)
                    .followRedirects(true)
                    .referrer("https://eclass.hkmakslo.edu.hk/login.php")
                    .execute();

            if (response.body().contains("<div class=\"error_msg\">")) {
                String errorMsgStr = response.body().substring(response.body().lastIndexOf("<div class=\"error_msg\">"))
                        .replace("<div class=\"error_msg\">","");
                errorMsgStr = errorMsgStr.replace(errorMsgStr
                                .substring(errorMsgStr.indexOf("</div>")),"");
                String finalErrorMsgStr = errorMsgStr;
                requireActivity().runOnUiThread(() -> {
                    TextView errorMsg = view.findViewById(R.id.intranet_error_msg);
                    errorMsg.setText(finalErrorMsgStr);
                    errorMsg.setVisibility(View.VISIBLE);
                    loginButton.setVisibility(View.VISIBLE);
                });
            } else if (response.body()
                    .contains("As you have already attempted logins but failed for too many times, please try again later")) {
                String errorMsgStr = response.body();
                requireActivity().runOnUiThread(() -> {
                    TextView errorMsg = view.findViewById(R.id.intranet_error_msg);
                    errorMsg.setText(errorMsgStr);
                    errorMsg.setVisibility(View.VISIBLE);
                });
            } else {
                requireActivity().runOnUiThread(() -> {
                    TextView errorMsg = view.findViewById(R.id.intranet_error_msg);
                    errorMsg.setVisibility(View.GONE);
                    loginButton.setVisibility(View.GONE);
                });

                Cookies.put("PHPSESSID", Objects.requireNonNull(templates.cookie("PHPSESSID")));
                Cookies.put("ck_intranet_justlogin", Objects.requireNonNull(response.cookie("ck_intranet_justlogin")));

                Document document = Jsoup.connect("https://eclass.hkmakslo.edu.hk/home/imail_gamma/viewfolder.php?Folder=INBOX&" +
                                "num_per_page=20&pageNo=" + pageNo)
                        .cookies(Cookies)
                        .referrer("https://eclass.hkmakslo.edu.hk/templates/")
                        .get();

                Elements tableParent = document.select("table#MailList");
                Elements table = tableParent.select("tbody");

                totalPageNo = document.select("select.formtextbox").eq(0).select("option").size();
                Log.d("Testing", "parseIntranet: " + totalPageNo);

                String title, size, sender, date, detailUrl;
                boolean filePresent, read;
                int trNo = table
                        .select("tr")
                        .size();
                for (int i = 1; i < trNo - 2; i++) {
                    Elements tr = table
                            .select("tr")
                            .eq(i);

                    title = tr
                            .select("td.iMailrow.tabletext")
                            .eq(6)
                            .select("span")
                            .select("a.iMailSubject")
                            .text();

                    if (title.isEmpty()) {
                        read = false;
                        title = tr.select("td.iMailrowunread.tabletext")
                                .eq(6)
                                .text();

                        filePresent = tr
                                .select("td.iMailrowunread.tabletext")
                                .eq(6)
                                .select("span")
                                .select("a.iMailSubjectunread")
                                .select("img")
                                .attr("src").equals("/images/2020a/iMail/icon_attachment.gif");

                        detailUrl = tr
                                .select("td.iMailrowunread.tabletext")
                                .eq(6)
                                .select("span")
                                .select("a")
                                .attr("href");

                        sender = tr
                                .select("td.iMailrowunread.tabletext.iMailsenderunread")
                                .select("span")
                                .select("span")
                                .eq(1)
                                .text();

                        date = tr
                                .select("td.iMailrowunread.tabletext")
                                .eq(7)
                                .text();

                        size = tr
                                .select("td.iMailrowunread.tabletext")
                                .eq(8)
                                .text();
                    } else {
                        read = true;

                        filePresent = tr
                                .select("td.iMailrow.tabletext")
                                .eq(6)
                                .select("span")
                                .select("a.iMailSubject")
                                .select("img")
                                .attr("src").equals("/images/2020a/iMail/icon_attachment.gif");

                        detailUrl = tr
                                .select("td.iMailrow.tabletext")
                                .eq(6)
                                .select("span")
                                .select("a")
                                .attr("href");

                        sender = tr
                                .select("td.iMailrow.tabletext.iMailsender")
                                .select("span")
                                .select("span")
                                .eq(1)
                                .text();

                        date = tr
                                .select("td.iMailrow.tabletext")
                                .eq(7)
                                .text();

                        size = tr
                                .select("td.iMailrow.tabletext")
                                .eq(8)
                                .text();
                    }
                    size = size + " Kb";
                    loginParseItems.add(new LoginParseItem(title, sender, date, detailUrl, filePresent, read, size));

                    requireActivity().runOnUiThread(() -> parseAdapterForBooks.notifyDataSetChanged());
                }
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }


    public static Map<String, String> getCookies() {
        return Cookies;
    }
}