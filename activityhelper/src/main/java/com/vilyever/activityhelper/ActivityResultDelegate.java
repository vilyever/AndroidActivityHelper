package com.vilyever.activityhelper;

import android.content.Intent;

/**
 * ActivityResultDelegate
 * ESchoolbag <com.ftet.base.ActivityHelper>
 * Created by vilyever on 2016/3/1.
 * Feature:
 */
public interface ActivityResultDelegate {
    /**
     * while using {@link ActivityRouter} to start activity for result, this is the time to hold the result
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data data
     */
    void onActivityResult(int requestCode, int resultCode, Intent data);
}
