package com.lentzos.nic.photogallery;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//Set up as singlefragmentactivity, then implement createfragment() and get it to return an instance of photogalleryfragment.
public class PhotoGalleryActivity extends SingleFragmentActivity {

    @Override
    public Fragment createFragment(){
        return PhotoGalleryFragment.newInstance();
    }
}
