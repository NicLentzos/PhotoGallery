package com.lentzos.nic.photogallery;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nic on 25/11/2016.
 */

public class PhotoGalleryFragment extends Fragment {
    private static final String TAG = "PhotoGalleryFragment";
    //member variable referencing the recyclerview.
    private RecyclerView mPhotoRecyclerView;
    //Implementing setupAdapter()
    private List<GalleryItem> mItems = new ArrayList<>();

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
        //Impleneting setupAdapter()
        setupAdapter();

        return v;
    }
    //Implementing setupAdapter()
    private void setupAdapter() {
        //check whether isAdded() is true before setting adapter. This ensures that the fragment has
        //been added to an activity and that getActivity() will not be null.
        //We are using an AsyncTask therefore cannot assume that the fragment is attached to an activity.
        if (isAdded()) {
            mPhotoRecyclerView.setAdapter(new PhotoAdapter(mItems));
        }
    }

    //Inner class to call networking code in FlickrFetcher.java. Creates a background thread.
    //Utility class called AsyncTask. Override the doInBackground() method to get data from the web site.

    private class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {
        @Override
        protected List<GalleryItem> doInBackground(Void... params) {
        //    try {
        //        String result = new FlickrFetchr().getUrlString("https://www.bignerdranch.com");
        //        Log.i(TAG, "Fetched contents of URL: " + result);
        //    } catch (IOException ioe) {
        //        Log.e(TAG, "Failed to fetch URL: ", ioe);
        //    }

            return new FlickrFetchr().fetchItems();
        }
        //Override onpostexecute() which runs after doinbackground() completes.
        //runs on main thread.
        //update mItems and call setupAdapter() after fetching photos to update the RecyclerViews data source.
        @Override
        protected void onPostExecute(List<GalleryItem> items) {
            mItems = items;
            setupAdapter();
        }
    }

    //Get photogalleryfragment's recyclerview to display some captions.
    //First define a viewholder as an inner class.

    private class PhotoHolder extends RecyclerView.ViewHolder{
        //Update PhotoHolder to hold an imageview instead of a textview.
        private ImageView mItemImageView;

        public PhotoHolder(View itemView) {
            super (itemView);

            mItemImageView = (ImageView) itemView;
        }
        //Create bindDrawable to set the imageview's drawable.
        public void bindDrawable(Drawable drawable){
            mItemImageView.setImageDrawable(drawable);
        }
    }
    // Now, add a recyclerview.adaptor to provide photoholders as needed based on a list of galleryitems.
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoHolder> {

        private List<GalleryItem> mGalleryItems;

        public PhotoAdapter(List<GalleryItem> galleryItems) {
            mGalleryItems = galleryItems;
        }
        //onCreateViewHolder now needs to inflate the gallery_view file and pass it to photoholder's constructor.
        @Override
        public PhotoHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            //TextView textView = new TextView(getActivity());
            //return new PhotoHolder(textView);
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.gallery_item, viewGroup, false);
            return new PhotoHolder(view);
        }
        //Placeholder image for each ImageView.
        @Override
        public void onBindViewHolder(PhotoHolder photoHolder, int position) {
            GalleryItem galleryItem = mGalleryItems.get(position);
            //photoHolder.bindGalleryItem(galleryItem);
            Drawable placeholder = getResources().getDrawable(R.drawable.worm);
            photoHolder.bindDrawable(placeholder); 
        }

        @Override
        public int getItemCount(){
            return mGalleryItems.size();
        }
    }
}

