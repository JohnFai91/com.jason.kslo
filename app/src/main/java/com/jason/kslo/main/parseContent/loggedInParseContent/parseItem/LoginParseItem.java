package com.jason.kslo.main.parseContent.loggedInParseContent.parseItem;

import java.util.Map;

public class LoginParseItem {
    private String title;
    private String detailUrl;
    private String file;
    private String fileName;
    private String date;
    private String sender;
    private Map<String,String> cookies;
    private String content;
    private Boolean filePresent;
    private Boolean read;
    private String size;
    private  String borrowedDate;
    private String returnDate;
    private String bkDetailUrl;

    public LoginParseItem(String title, String sender, String date, String detailUrl, Boolean filePresent, Boolean read, String size) {
        this.title = title;
        this.sender = sender;
        this. date = date;
        this.detailUrl = detailUrl;
        this.filePresent = filePresent;
        this.read = read;
        this.size = size;
    }

    public LoginParseItem(String fileName, String fileUrl, Map<String, String> cookies) {
        this.fileName = fileName;
        this.detailUrl = fileUrl;
        this.cookies = cookies;
    }

    public LoginParseItem(String bkTitle, String bkImg, String bkBorrowedDate, String bkReturnDate,
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

    @SuppressWarnings("unused")
    public String getFileName() {
        return fileName;
    }
    @SuppressWarnings("unused")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getDate() {
        return date;
    }
    @SuppressWarnings("unused")
    public void setDate(String date) {
        this.date = date;
    }

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
    public Map<String, String> setCookies() {
        return cookies;
    }

    public String getContent() {
        return content;
    }
    @SuppressWarnings("unused")
    public void setContent(String content) {
        this.content = content;
    }

    public Boolean getFilePresent() {
        return filePresent;
    }
    @SuppressWarnings("unused")
    public void setFilePresent(Boolean filePresent) {
        this.filePresent = filePresent;
    }

    public Boolean getRead() {
        return read;
    }
    @SuppressWarnings("unused")
    public void setRead(Boolean read) {
        this.read = read;
    }

    public String getSize() {
        return size;
    }
    @SuppressWarnings("unused")
    public void setSize(String size) {
        this.size = size;
    }

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
        this.size = returnDate;
    }

    public String getBkDetailUrl() {
        return bkDetailUrl;
    }
    @SuppressWarnings("unused")
    public void setBkDetailUrl(String bkDetailUrl) {
        this.bkDetailUrl = bkDetailUrl;
    }
}
