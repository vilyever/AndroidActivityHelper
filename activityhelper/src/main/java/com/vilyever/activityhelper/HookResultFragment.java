package com.vilyever.activityhelper;

import android.app.Fragment;
import android.content.Intent;

/**
 * HookResultFragment
 * ESchoolbag <com.ftet.base.ActivityHelper>
 * Created by vilyever on 2016/3/1.
 * Feature:
 * 抓取onActivityResult
 */
public class HookResultFragment extends Fragment {
    final HookResultFragment self = this;

    
    /* Constructors */
    public HookResultFragment() {
    }
    
    
    /* Public Methods */

    
    
    /* Properties */
    /**
     * result 回调
     */
    private ActivityResultDelegate activityResultDelegate;
    protected HookResultFragment setActivityResultDelegate(ActivityResultDelegate activityResultDelegate) {
        this.activityResultDelegate = activityResultDelegate;
        return this;
    }
    protected ActivityResultDelegate getActivityResultDelegate() {
        return activityResultDelegate;
    }
    
    
    /* Overrides */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        getActivityResultDelegate().onActivityResult(requestCode, resultCode, data);

        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
    }
     
     
    /* Delegates */
     
     
    /* Private Methods */
    
}