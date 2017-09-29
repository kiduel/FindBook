package com.example.kidus11.googlebookslist;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Books>> {
    private static final String STATE_SCROLL_POSITION = "scrollPosition";
    private String bookQuery1 = "";
    private int load_id = 1;
    private BooksListViewAdapter myadapter;
    private String bookTitle;
    private ListView booksListView;
    private boolean isConnected;
    private RelativeLayout emptyLayout;
    private EditText mEdit;
    private int savedPosition = 0;

    private static String prepareString(String bookFromUser) {
        String googleFormat = "https://www.googleapis.com/books/v1/volumes?q=";
        String searchableString = googleFormat + bookFromUser + "&maxResults=30";
        return searchableString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Button search;
        final RelativeLayout no_book;
        super.onCreate(savedInstanceState);

        //To check the connection, we created isConnected
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        setContentView(R.layout.activity_main);

        mEdit = (EditText) findViewById(R.id.user_edit_text);
        myadapter = new BooksListViewAdapter(this, new ArrayList<Books>());
        booksListView = (ListView) findViewById(R.id.list);
        booksListView.setAdapter(myadapter);
        emptyLayout = (RelativeLayout) findViewById(R.id.empty_layout_relative1);
        no_book = (RelativeLayout) findViewById(R.id.no_book_check);
        final TextView no_intr = (TextView) findViewById(R.id.no_internet_connecton);


        // We initialize the loader
        getLoaderManager().initLoader(load_id, null, MainActivity.this).forceLoad();

        //Check the internet when the activity is created
        if (!isConnected) {
            no_book.setVisibility(View.INVISIBLE);
        } else {
            no_book.setVisibility(View.VISIBLE);
            no_intr.setVisibility(View.INVISIBLE);
        }

        //Search - setOnClickListener
        search = (Button) findViewById(R.id.search_button);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bookTitle = mEdit.getText().toString();
                String input = prepareString(bookTitle);

                bookQuery1 = input;

                getLoaderManager().restartLoader(load_id, null, MainActivity.this);

                //Check the internet connection when the button is pressed.
                if (!isConnected) {
                    no_book.setVisibility(View.INVISIBLE);
                }
            }
        });

        //Each list
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current earthquake that was clicked on
                Books currentBook = myadapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri currentBookUri = Uri.parse(currentBook.getmInformation());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, currentBookUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
                savedPosition = position; //We save the position of the scroll

            }
        });

        //we add the savedPosition  to the savedInstanceState
        if (savedInstanceState != null && savedInstanceState.containsKey(STATE_SCROLL_POSITION)) {
            savedPosition = savedInstanceState.getInt(STATE_SCROLL_POSITION);
        }

        //If the list is Empty
        booksListView.setEmptyView(emptyLayout);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        // save currnet positon
        int currentPosition = booksListView.getFirstVisiblePosition();
        outState.putInt(STATE_SCROLL_POSITION,
                currentPosition);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<List<Books>> onCreateLoader(int i, Bundle bundle) {
        Log.i("teddy", "onCreateLoader: is called");

        return new BooksLoader(this, bookQuery1); //The BookLoader runs in the worker thread


    }

    @Override
    public void onLoadFinished(Loader<List<Books>> loader, List<Books> bookses) {
        Log.i("TEST", "onLoadFinished: is called");



        // Clear the adapter of previous earthquake data
        myadapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (bookses != null && !bookses.isEmpty()) {
            myadapter.addAll(bookses);
        }
        booksListView.setSelection(savedPosition);


    }

    @Override
    public void onLoaderReset(Loader<List<Books>> loader) {
        Log.i("Log", "onLoaderReset: is called");
        myadapter.clear();

    }

}
