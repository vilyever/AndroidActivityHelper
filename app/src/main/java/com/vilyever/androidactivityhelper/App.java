package com.vilyever.androidactivityhelper;

import android.app.Application;

import com.vilyever.activityhelper.ActivityHelper;

/**
 * App
 * AndroidActivityHelper <com.vilyever.androidactivityhelper>
 * Created by vilyever on 2016/3/24.
 * Feature:
 */
public class App extends Application {
    final App self = this;

    
    /* Constructors */

    /* Public Methods */


    /* Properties */


    /* Overrides */
    @Override
    public void onCreate() {
        super.onCreate();

        ActivityHelper.initialize(this);
    }

     
    /* Delegates */
     
     
    /* Private Methods */
    
}