package com.lentzos.nic.photogallery;

import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Nic on 26/11/2016.
 */

public class FlickrFetchr {
    //Add some Flickr constants.
    private static final String TAG = "FlickrFetchr";
    private static final String API_KEY = "5ed92ceb17fe8fc5058197d126735bfb";

    //geturlbytes() fetches raw data from a URL and returns it as an array of bytes.
    public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();

        try{
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);

            }

            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }}
        //geturlstring() converts the result of geturlbytes() to a string.
        public String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }
    //Method to build a request URL and fetch the URL contents.
    //Use uri.builder to build the complete URL for flickr api request. This method automatically escapes query strings etc for you.
    //the extras parameter url_s tells flickr to supply the URL for the small version of the picture if it is available.
    public void fetchItems() {
        try {
            String url = Uri.parse("https://api.flickr.com/services/rest/").buildUpon()
                    .appendQueryParameter("method", "flickr.photos.getRecent")
                    .appendQueryParameter("api_key", API_KEY)
                    .appendQueryParameter("format", "json")
                    .appendQueryParameter("nojsoncallback", "1")
                    .appendQueryParameter("extras", "url_s")
                    .build().toString();
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received json: " + jsonString);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }
    }
}
// 1) Create a URL object from a string.
// 2) call openConnection() to create a connection object pointed at the URL.
// 3) URL.openConnection() returns a URLConnection. Cast it to HttpURLConnection.
// This gives you HTTP specific interfaces to work with.

// HttpURLConnection represents a connection, but it will not connect to the endpoint until you
// call getInputStream().

// Then call read() repeatedly until connection runs out of data.
// The InputStream will yield bytes as they become available.
// When finished, you close it and spit out your ByteArrayOutputStream byte array.
