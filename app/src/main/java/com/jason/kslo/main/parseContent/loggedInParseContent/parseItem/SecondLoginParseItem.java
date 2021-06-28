package com.jason.kslo.main.parseContent.loggedInParseContent.parseItem;

import java.util.Map;

public class SecondLoginParseItem {
    private String title;
    private String bkDetailUrl;
    private String detailUrl;
    private String file;
    private String fileName;
    private String date;
    private String sender;
    private String borrowedDate;
    private String returnDate;
    private Map<String,String> cookies;
    private String content;

    @SuppressWarnings("unused")
    public SecondLoginParseItem(){
    }

    @SuppressWarnings("unused")
    public SecondLoginParseItem(String content) {
        this.content = content;
    }

    public SecondLoginParseItem(String fileName, String fileUrl, Map<String, String> cookies) {
        this.fileName = fileName;
        this.detailUrl = fileUrl;
        this.cookies = cookies;
    }

    public SecondLoginParseItem(String bkTitle, String bkImg, String bkBorrowedDate, String bkReturnDate,
                                String detailUrl, Map<String,String> cookies) {
        this.title = bkTitle;
        this.detailUrl = bkImg;
        this.borrowedDate = bkBorrowedDate;
        this.returnDate = bkReturnDate;
        this.bkDetailUrl = detailUrl;
        this.cookies = cookies;
    }


    public String getTitle() {
        return title;
    }
    @SuppressWarnings("unused")
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailUrl(){
        return  detailUrl;
    }
    @SuppressWarnings("unused")
    public void setDetailUrl(String detailUrl){
        this.detailUrl = detailUrl;
    }

    @SuppressWarnings("unused")
    public String getFile() {
        return file;
    }
    @SuppressWarnings("unused")
    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }
    @SuppressWarnings("unused")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @SuppressWarnings("unused")
    public String getDate() {
        return date;
    }
    @SuppressWarnings("unused")
    public void setDate(String date) {
        this.date = date;
    }

    @SuppressWarnings("unused")
    public String getSender() {
        return sender;
    }
    @SuppressWarnings("unused")
    public void setSender(String sender) {
        this.sender = sender;
    }

    public Map<String,String>getCookies() {
        return cookies;
    }
    @SuppressWarnings("unused")
    public void setCookies (Map<String, String> Cookies) {
        this.cookies =  Cookies;
    }

    @SuppressWarnings("unused")
    public String getContent() {
        return content;
    }
    @SuppressWarnings("unused")
    public void setContent(String content) {
        this.content = content;
    }

    @SuppressWarnings("unused")
    public String getBorrowedDate() {
        return borrowedDate;
    }
    @SuppressWarnings("unused")
    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getReturnDate() {
        return returnDate;
    }
    @SuppressWarnings("unused")
    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getBkDetailUrl() {
        return bkDetailUrl;
    }
    @SuppressWarnings("unused")
    public void setBkDetailUrl(String bkDetailUrl) {
        this.bkDetailUrl = bkDetailUrl;
    }
}
