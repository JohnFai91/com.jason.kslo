package com.jason.kslo.parseContent.parseItem;

public class DashboardParseItem {
    private String title;
    private String date;
    private String desc;
    private String url;

    public DashboardParseItem(String date, String title){
        this.title = title;
        this.date = date;
    }

    public DashboardParseItem(String date, String title, String desc, String url) {
        this.title = title;
        this.date = date;
        this.desc = desc;
        this.url = url;
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

    public String getDesc() {
        return desc;
    }
    @SuppressWarnings("unused")
    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getUrl() {
        return url;
    }
    @SuppressWarnings("unused")
    public void setUrl(String url) {
        this.url = url;
    }
}
