package com.example.kidus11.googlebookslist;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by kidus11 on 9/25/17.
 */

public class Utils {


    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = Utils.class.getSimpleName();


    //the fetch books will return ArrayList
    public static ArrayList<Books> fetchBooks(String requestUrl) {
        // Create URL object by using the function createURL
        URL url = createURL(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        //return the extracted
        return extractFeatureFromJson(jsonResponse);

    }

    /**
     * Create a URL request
     * This returns a new URL object from the given string the URL
     */
    private static URL createURL(String stringrl) {
        URL url = null;
        try {
            url = new URL(stringrl);
        } catch (MalformedURLException e) {
            Log.e("Error", "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request from the given URL and returns the string as the response
     * the json response will be returned, a long text
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        //If the URL is null
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            final int READ_TIMEOUT = 1000;
            final int CONNECT_TIMEOUT = 15000;
            final int SUCCESS = 200;

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(READ_TIMEOUT /* millisecond*/);
            urlConnection.setConnectTimeout(CONNECT_TIMEOUT /* millisecond*/);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();


            /**
             * If the request was successful (response code 200)
             * then read the input stream and parse the response.
             */
            if (urlConnection.getResponseCode() == SUCCESS) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e("Error", "Error in response code");

            }
        } catch (IOException e) {
            Log.e("Error", "Problem downloading the book", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect(); //disconnect when done
            }
            if (inputStream != null) {
                inputStream.close();   //close
            }
            return jsonResponse;
        }
    }

    /**
     * the Stream will just be zeros and ones as it downloaded directly fromn the server
     * We need to encode that to something we and understand, this will do that for us
     * This will read the given input stream and make it a sensble string
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output; /* String builder are like string but they can be modified internally
         we need this since we dont know how long the string that  is going to be*/
        output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * This will return a book from the JSON response
     * We want the title, Author, publisher date and a small thumbnail
     */
    private static ArrayList<Books> extractFeatureFromJson(String bookJSON) {
        //If the JSON String is empty or null, then we return null
        if (TextUtils.isEmpty(bookJSON)) {
            return null;
        }
        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<Books> bookslist = new ArrayList<>();
        try {
            JSONObject baseJSONResponse = new JSONObject(bookJSON);
            JSONArray itemsArray = baseJSONResponse.getJSONArray("items");

            String writer = "";
            String publisherDate = "";
            for (int i = 0; i < itemsArray.length(); i++)
            // If there are results in the features array
            {
                JSONObject firstBook = itemsArray.getJSONObject(i); //this is the first member in the array
                JSONObject volume = firstBook.getJSONObject("volumeInfo");

                // Extract out the title, number of people, and perceived strength values
                String bookTitle = volume.getString("title");
                Log.i("test", "extractFeatureFromJson: booktitle is parsed");

                // Extract the publisher data
                if(volume.has("publishedDate")) {
                    publisherDate = volume.getString("publishedDate");
                    Log.i("test", "extractFeatureFromJson: date is parsed");
                }
                // Extract the author,
                if (volume.has("authors")) {
                    JSONArray authors = volume.getJSONArray("authors");
                    writer = authors.toString(); //hopefully converts item to string
                }
                Log.i("test", "extractFeatureFromJson: author is parsed");

                // Extract the thumbNail
                String information = volume.getString("infoLink"); //hopefully converts item to string
                Log.i("test", "extractFeatureFromJson: thumbnail is parsed");

                // Create a new {@link Earthquake} object with the magnitude, location, time,
                // and url from the JSON response.
                Books books = new Books(bookTitle, publisherDate, writer, information);

                // Add the new {@link Earthquake} to the list of earthquakes.
                bookslist.add(books);
                Log.i("test", "The books has been added");


            }

        } catch (JSONException e1) {
            e1.printStackTrace();
        }

        //Extract the title, Author, Publisher data and url
        return bookslist;
    }
}



