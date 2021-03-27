package com.jason.kslo.parseContent.parseItem;

public class ParseItem {
    private String imgURL;
    private String title;
    private String detailUrl;
    private String count;
    private String file;
    private String fileName;

    @SuppressWarnings("unused")
    public ParseItem(){
    }

    public ParseItem(String imgURL, String title, String count, String detailUrl){
        this.imgURL = imgURL;
        this.title = title;
        this.detailUrl = detailUrl;
        this.count = count;
    }

    @SuppressWarnings("unused")
    public ParseItem(String imgURL, String title, String detailUrl){
        this.imgURL = imgURL;
        this.title = title;
        this.detailUrl = detailUrl;
    }

    public ParseItem(String imgURL){
        this.imgURL =imgURL;
    }

    public ParseItem(String fileUrl, String fileName) {
        this.file = fileUrl;
        this.fileName = fileName;
    }

    public String getImgURL(){
        return imgURL;
    }
    @SuppressWarnings("unused")
    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
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

    public String getCount() {
        return count;
    }
    @SuppressWarnings("unused")
    public void getCount(String count) {
        this.count = count;
    }

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

}
