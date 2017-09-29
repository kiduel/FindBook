package com.example.kidus11.googlebookslist;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by kidus11 on 9/25/17.
 */

public class BooksLoader  extends AsyncTaskLoader<List<Books>> {
    /** Tag for log messages */
    private static final String LOG_TAG = BooksLoader.class.getName();

    /** Query URL */
    private String mUrl;

    public BooksLoader(Context context, String Url) {
        super(context);
        mUrl = Url;
    }


    /*
    forceLoad() which is a required step to actually trigger the loadInBackground() method to execute.
     */
    @Override
    protected void onStartLoading() {
        Log.i("TEST", "onStartLoading: is being called");
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<Books> loadInBackground() {
        Log.i("TEST", "loadInBackground: is being called");
        List<Books> bookListfromBOK = Utils.fetchBooks(mUrl);
        return bookListfromBOK;
    }
}
