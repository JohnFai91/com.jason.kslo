package com.jason.kslo.ParseContent.LoggedInParseContent.ParseItem;

import java.util.Map;

public class SecondLoginParseItem {
    private String title;
    private String detailUrl;
    private String file;
    private String fileName;
    private String date;
    private String sender;
    private String borrowedDate;
    private String returnDate;
    private Map<String,String> cookies;
    private String content;

    public SecondLoginParseItem(){
    }

    public SecondLoginParseItem(String content) {
        this.content = content;
    }

    public SecondLoginParseItem(String fileName, String fileUrl, Map<String, String> cookies) {
        this.fileName = fileName;
        this.detailUrl = fileUrl;
        this.cookies = cookies;
    }

    public SecondLoginParseItem(String bkTitle, String bkImg, String bkBorrowedDate, String bkReturnDate) {
        this.title = bkTitle;
        this.detailUrl = bkImg;
        this.borrowedDate = bkBorrowedDate;
        this.returnDate = bkReturnDate;
    }


    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailUrl(){
        return  detailUrl;
    }
    public void setDetailUrl(String detailUrl){
        this.detailUrl = detailUrl;
    }

    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String file) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getSender() {
        return sender;
    }
    public void setSender(String sender) {
        this.sender = sender;
    }

    public Map<String,String>getCookies() {
        return cookies;
    }
    public Map<String, String> setCookies() {
        return cookies;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getBorrowedDate() {
        return borrowedDate;
    }
    public void setBorrowedDate(String borrowedDate) {
        this.borrowedDate = borrowedDate;
    }

    public String getReturnDate() {
        return returnDate;
    }
    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }
}
