package com.jason.kslo.ParseContent.ParseItem;

public class SecondParseItem {
    private String imgURL;
    private String title;
    private String detailUrl;
    private String Desc;
    private String bulletPoints;
    private String video;
    private String file;
    private String count;
    private String fileName;

    public SecondParseItem(){
    }

    public SecondParseItem(String imgURL,String title, String desc, String detailUrl){
        this.imgURL = imgURL;
        this.title = title;
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

    public String getDesc() {
        return Desc;
    }
    public void setDesc(String desc) {
        this.Desc = desc;
    }

    public String getBulletPoints() {
        return bulletPoints;
    }
    public void setBulletPoints(String bulletPoints) {
        this.bulletPoints = bulletPoints;
    }

    public String getVideo() {
        return video;
    }
    public void setVideo(String pdf) {
        this.video = pdf;
    }

    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }

    public String getCount() {
        return count;
    }
    public void setCount(String count) {
        this.count = count;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

}
