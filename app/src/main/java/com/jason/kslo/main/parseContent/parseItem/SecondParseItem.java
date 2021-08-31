package com.jason.kslo.main.parseContent.parseItem;

public class SecondParseItem {
    private String imgURL;
    private String title;
    private String detailUrl;
    private String Desc;
    private String date;
    private String bulletPoints;
    private String video;
    private String file;
    private String count;
    private String fileName;
    private String sender;

    public SecondParseItem(String sender,String title, String date, String desc, String detailUrl){
        this.sender = sender;
        this.title = title;
        this.date = date;
        this.Desc = desc;
        this.detailUrl = detailUrl;
    }

    public SecondParseItem(String title, String desc, String video){
        this.title = title;
        this.Desc = desc;
        this.video = video;
    }

    public SecondParseItem(String src){
            this.bulletPoints = src;
    }

    public SecondParseItem(String imgUrl, String detailImgUrl) {
        this.imgURL = imgUrl;
        this.detailUrl = detailImgUrl;
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

    public String getDesc() {
        return Desc;
    }
    @SuppressWarnings("unused")
    public void setDesc(String desc) {
        this.Desc = desc;
    }

    public String getBulletPoints() {
        return bulletPoints;
    }
    @SuppressWarnings("unused")
    public void setBulletPoints(String bulletPoints) {
        this.bulletPoints = bulletPoints;
    }

    public String getVideo() {
        return video;
    }
    @SuppressWarnings("unused")
    public void setVideo(String pdf) {
        this.video = pdf;
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
    public String getCount() {
        return count;
    }
    @SuppressWarnings("unused")
    public void setCount(String count) {
        this.count = count;
    }

    @SuppressWarnings("unused")
    public String getFileName() {
        return fileName;
    }
    @SuppressWarnings("unused")
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @SuppressWarnings("unused")
    public String getSender() {
        return sender;
    }
    @SuppressWarnings("unused")
    public void setSender(String sender) {
        this.sender = sender;
    }

    @SuppressWarnings("unused")
    public String getDate() {
        return date;
    }
    @SuppressWarnings("unused")
    public void setDate(String date) {
        this.date = date;
    }

}
