package com.jason.kslo.changelog;

public class ChangelogParseItem {
    public String descR;
    public String descL;
    public String title;
    public String separatorVisibility;

    @SuppressWarnings("unused")
    public ChangelogParseItem() {
    }

    public ChangelogParseItem(String descL, String descR, String version, String separatorVisibility) {
        this.descL = descL;
        this.descR = descR;
        this.title = version;
        this.separatorVisibility = separatorVisibility;
    }

    public String getTitle() {
        return title;
    }
    @SuppressWarnings("unused")
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescL() {
        return descL;
    }
    @SuppressWarnings("unused")
    public void setDescL(String descL) {
        this.descL = descL;
    }

    public String getDescR() {
        return descR;
    }
    @SuppressWarnings("unused")
    public void setDescR(String descR) {
        this.descR = descR;
    }

    public String getSeparatorVisibility() {
        return separatorVisibility;
    }
    @SuppressWarnings("unused")
    public void setSeparatorVisibility(String separatorVisibility) {
        this.separatorVisibility = separatorVisibility;
    }
}
