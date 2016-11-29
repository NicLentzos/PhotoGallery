package com.lentzos.nic.photogallery;

import android.net.Uri;
import android.renderscript.RSInvalidStateException;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nic on 26/11/2016.
 */

public class FlickrFetchr {
    //Add some Flickr constants.
    private static final String TAG = "FlickrFetchr";
    private static final String API_KEY = "5ed92ceb17fe8fc5058197d126735bfb";
    private static final String FETCH_RECENTS_METHOD = "flickr.photos.getRecent";
    private static final String SEARCH_METHOD = "flickr.photos.search";
    private static final Uri ENDPOINT = Uri.parse("https://api.flickr.com/services/rest/").buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .build();

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

    public List<GalleryItem> fetchRecentPhotos(){
        String url = buildUrl(FETCH_RECENTS_METHOD, null);
        return downloadGalleryItems(url);
    }

    public List<GalleryItem> searchPhotos(String query) {
        String url = buildUrl(SEARCH_METHOD, query);
        return downloadGalleryItems(url);
    }
    //Method to build a request URL and fetch the URL contents.
    //Use uri.builder to build the complete URL for flickr api request. This method automatically escapes query strings etc for you.
    //the extras parameter url_s tells flickr to supply the URL for the small version of the picture if it is available.
    public List<GalleryItem> downloadGalleryItems(String url) {
        //List of GalleryItems
        List<GalleryItem> items = new ArrayList<>();
        try {
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received json: " + jsonString);
            //Parse JSON text into corresponding Java objects using JSONObject() constructor.
            JSONObject jsonBody = new JSONObject(jsonString);
            //call parseItems() and return a list of GalleryItems.
            parseItems(items, jsonBody);
        } catch (JSONException je) {
            Log.e(TAG,"Failed to parse JSON", je);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch items", ioe);
        }

        return items;
    }

    private String buildUrl(String method, String query) {
        Uri.Builder uriBuilder = ENDPOINT.buildUpon().appendQueryParameter("method", method);
        if (method.equals(SEARCH_METHOD)) {
            uriBuilder.appendQueryParameter("text", query);
        }
        //The buildUrl method appends the necessary parameters, just as fetchItems used to
        //but it dynamically fills the method parameter value. It also appends a value for the
        //text parameter only if the value specified for the method parameter is search.
        return uriBuilder.build().toString();
    }

    //You have a JSON top level object (maps to outermost curly braces of JSON file). This contains a nested JSON
    //object named photos. Within this is a JSONArray named photo. The array contains a collection of JSON objects
    //that represent metadata for a single photo.
    //Need to make a GalleryItem for each photo and add it to a list.


    private void parseItems(List<GalleryItem> items, JSONObject jsonBody) throws IOException, JSONException{
        JSONObject photosJsonObject = jsonBody.getJSONObject("photos");
        JSONArray photoJsonArray = photosJsonObject.getJSONArray("photo");

        for (int i = 0; i< photoJsonArray.length(); i++) {
            JSONObject photoJsonObject = photoJsonArray.getJSONObject(i);

            GalleryItem item = new GalleryItem();
            item.setId(photoJsonObject.getString("id"));
            item.setCaption(photoJsonObject.getString("title"));

            //ignore images that do not have an image URL.
            if(!photoJsonObject.has("url_s")) {
                continue;
            }

            item.setUrl(photoJsonObject.getString("url_s"));
            items.add(item);
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
