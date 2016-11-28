package com.lentzos.nic.photogallery;

import android.os.HandlerThread;
import android.util.Log;

/**
 * Created by Nic on 28/11/2016.
 */

public class ThumbnailDownloader<T> extends HandlerThread {
    //background thread that is also a message loop.
    //photogalleryfragment will use this class. It will need some object (T) to identify each download
    //and to determine which UI element to update with the image once it is downloaded.

    private static final String TAG = "ThumbnailDownloader";
    private boolean mHasQuit = false;
    //constructor
    public ThumbnailDownloader() {
        super(TAG);
    }
    //override quit() that signals when the thread has quit.
    @Override
    public boolean quit(){
        mHasQuit = true;
        return super.quit();
    }
    //Needs object T to use as the identifier for the download and a String containing the URL
    //of the file to download.
    public void queueThumbnail(T target, String url) {
        Log.i(TAG, "Got a URL: " + url);
    }
}
