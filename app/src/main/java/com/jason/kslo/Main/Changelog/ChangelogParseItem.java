package com.jason.kslo.Main.Changelog;

import com.jason.kslo.ParseContent.ParseItem.SecondParseItem;

public class ChangelogParseItem {
    public String descR;
    public String descL;
    public String title;

    public ChangelogParseItem() {
    }
    public ChangelogParseItem(String descL, String descR, String title){
        this.descL = descL;
        this.descR = descR;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescL() {
        return descL;
    }
    public void setDescL(String descL) {
        this.descL = descL;
    }

    public String getDescR() {
        return descR;
    }
    public void setDescR(String descR) {
        this.descR = descR;
    }
}
