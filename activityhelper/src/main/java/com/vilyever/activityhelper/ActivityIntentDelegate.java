package com.vilyever.activityhelper;

import android.content.Intent;

/**
 * ActivityIntentDelegate
 * ESchoolbag <com.ftet.base.ActivityHelper>
 * Created by vilyever on 2016/3/1.
 * Feature:
 */
public interface ActivityIntentDelegate {
    /**
     * while using {@link ActivityRouter} to start activity, this is the time to put data into intent
     * @param intent 依照当前Activity和欲跳转的Activity生成的intent
     */
    void setupIntentForStartActivity(Intent intent);
}
