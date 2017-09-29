package com.example.kidus11.googlebookslist;

/**
 * Created by kidus11 on 9/18/17.
 */

public class Books {
    private String mTitle;
    private String mAuthor;
    private String mPublishedDate;
    private String mInformation;


    public Books(String mTitle, String mAuthor, String mPublishedDate, String mInformation) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mPublishedDate = mPublishedDate;
        this.mInformation = mInformation;
    }

    public String getPublishedDate() {
        return mPublishedDate;
    }

    public String getmInformation() {
        return mInformation;
    }

    public void setmInformation(String mInformation) {
        this.mInformation = mInformation;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public void setmPublishedDate(String mPublishedDate) {
        this.mPublishedDate = mPublishedDate;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }
}
