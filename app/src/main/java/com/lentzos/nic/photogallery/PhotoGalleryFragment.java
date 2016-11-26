package com.lentzos.nic.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

/**
 * Created by Nic on 25/11/2016.
 */

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";
    //member variable referencing the recyclerview.
    private RecyclerView mPhotoRecyclerView;

    public static PhotoGalleryFragment newInstance() {
        return new PhotoGalleryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //retain fragment
        setRetainInstance(true);
        // Now call the execute() method to start the AsyncTask thread. This will call doInBackground().
        new FetchItemsTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);

        mPhotoRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_photo_gallery_recycler_view);
        mPhotoRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        return v;
    }

    //Inner class to call networking code in FlickrFetcher.java. Creates a background thread.
    //Utility class called AsyncTask. Override the doInBackground() method to get data from the web site.

    private class FetchItemsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
        //    try {
        //        String result = new FlickrFetchr().getUrlString("https://www.bignerdranch.com");
        //        Log.i(TAG, "Fetched contents of URL: " + result);
        //    } catch (IOException ioe) {
        //        Log.e(TAG, "Failed to fetch URL: ", ioe);
        //    }
            new FlickrFetchr().fetchItems();
            return null;
        }
    }
}
// Now call the execute() method to start the AsyncTask thread. This will call doInBackground().
