package com.jason.kslo.parseContent.parseItem;

public class DashboardParseItem {
    private String title;
    private String date;

    public DashboardParseItem(String date, String title){
        this.title = title;
        this.date = date;
    }

    public String getDate() {
        return date;
    }
    @SuppressWarnings("unused")
    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }
    @SuppressWarnings("unused")
    public void setTitle(String title) {
        this.title = title;
    }
}
