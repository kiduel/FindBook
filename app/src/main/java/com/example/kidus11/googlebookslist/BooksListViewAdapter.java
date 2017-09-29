package com.example.kidus11.googlebookslist;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kidus11 on 9/25/17.
 */

public class BooksListViewAdapter extends ArrayAdapter<Books> {


    public BooksListViewAdapter(Activity context, ArrayList<Books> books) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, books);


    }

    @Override
    //We have to override the getView of the ArrayAdapter since we are creating a custom Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        convertView = null;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.custom_row, parent, false);

            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Books currentBook = getItem(position);

        // Find the TextView with view ID book_title_tv
        viewHolder.bookTitle.setText(currentBook.getTitle());
        // Find the TextView with view author_tv
        viewHolder.authorName.setText(currentBook.getAuthor());
        // Find the TextView with view ID published_tv
        viewHolder.publishedYear.setText(currentBook.getPublishedDate());

        Log.i("test", "The itemView is returned");
        return convertView;


        }

    class ViewHolder {
        private TextView bookTitle;
        private TextView authorName;
        private TextView publishedYear;

        public ViewHolder(@NonNull View view) {
            this.bookTitle = (TextView) view
                    .findViewById(R.id.book_title_tv);
            this.authorName = (TextView) view
                    .findViewById(R.id.author_tv);
            this.publishedYear = (TextView) view
                    .findViewById(R.id.published_tv);
        }

    }
}
