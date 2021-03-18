package com.jason.kslo.ParseContent.ParseItem;

public class ParseItem {
    private String imgURL;
    private String title;
    private String detailUrl;
    private String count;
    private String file;
    private String fileName;

    public ParseItem(){
    }

    public ParseItem(String imgURL, String title, String count, String detailUrl){
        this.imgURL = imgURL;
        this.title = title;
        this.detailUrl = detailUrl;
        this.count = count;
    }

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
    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
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
    public void setDetailUrl(String deatailUrl){
        this.detailUrl = deatailUrl;
    }

    public String getCount() {
        return count;
    }
    public void getCount(String desc) {
        this.count = count;
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

}
