package com.lentzos.nic.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by Nic on 28/11/2016.
 */

public class ThumbnailDownloader<T> extends HandlerThread {
    //background thread that is also a message loop.
    //photogalleryfragment will use this class. It will need some object (T) to identify each download
    //and to determine which UI element to update with the image once it is downloaded.

    private static final String TAG = "ThumbnailDownloader";
    //message handler variable.
    private static final int MESSAGE_DOWNLOAD = 0;

    private boolean mHasQuit = false;
    //message handler variable.
    private Handler mRequestHandler;
    private ConcurrentMap<T, String> mRequestMap = new ConcurrentHashMap<>();
    //concurrent hash map = thread safe version of hashmap. Download request's identifying object T
    //is used as a key and you can store and retrieve a URL associated with the request.

    //constructor
    public ThumbnailDownloader() {
        super(TAG);
    }
    //initialise mRequestHandler and define what that handler will do when downloaded messages are
    //pulled off the queue and passed to it.
    @Override
    protected void onLooperPrepared(){
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    T target = (T) msg.obj;
                    Log.i(TAG, "Got a request for URL: " + mRequestMap.get(target));
                    handleRequest(target);
                }
            }
        };


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

        //add code to update mRequestMap and post a new message to the background thread's message queue.
        if (url == null) {
            mRequestMap.remove(target);
        } else {
            mRequestMap.put(target, url);
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget();
        }
    }

    private void handleRequest(final T target) {
        try {
            final String url = mRequestMap.get(target);
            if (url == null) {
                return;
            }
            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created");
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }
}
