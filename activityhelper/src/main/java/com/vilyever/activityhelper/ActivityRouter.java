package com.vilyever.activityhelper;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * ActivityRouter
 * ESchoolbag <com.ftet.base>
 * Created by vilyever on 2016/3/1.
 * Feature:
 * Activity注册跳转
 */
public class ActivityRouter implements Application.ActivityLifecycleCallbacks {
    final ActivityRouter self = this;

    /* Constructors */
    private static ActivityRouter instance = null;
    /**
     * 单例
     * @return 单例
     */
    public synchronized static ActivityRouter getInstance() {
        if (instance == null) {
            instance = new ActivityRouter();
        }
        return instance;
    }
    
    
    /* Public Methods */

    /**
     * 结束当前Activity
     */
    public static void back() {
        if (!ActivityHelper.findTopActivity().isFinishing()) {
            ActivityHelper.findTopActivity().finish();
        }
    }

    /**
     * 注册配对Activity和一个标识符
     * @param activityClazz Activity的类
     * @param activityIdentifier 标识符，用于后续跳转功能
     */
    public static void bindActivity(Class<?> activityClazz, String activityIdentifier) {
        getInstance().getActivityMap().put(activityIdentifier, activityClazz);
    }

    /** {@link ActivityRouter#nativeStartActivity(String, Bundle, ActivityIntentDelegate)} */
    public static void startActivity(String activityIdentifier) {
        startActivity(activityIdentifier, null, null);
    }

    /** {@link ActivityRouter#nativeStartActivity(String, Bundle, ActivityIntentDelegate)} */
    public static void startActivity(String activityIdentifier, ActivityIntentDelegate intentDelegate) {
        startActivity(activityIdentifier, null, intentDelegate);
    }

    /** {@link ActivityRouter#nativeStartActivity(String, Bundle, ActivityIntentDelegate)} */
    public static void startActivity(String activityIdentifier, Bundle options, ActivityIntentDelegate intentDelegate) {
        getInstance().nativeStartActivity(activityIdentifier, options, intentDelegate);
    }

    /** {@link ActivityRouter#nativeStartActivityForResult(String, int, Bundle, ActivityIntentDelegate, ActivityResultDelegate)}  */
    public static void startActivityForResult(String activityIdentifier, int requestCode, ActivityResultDelegate resultDelegate) {
        startActivityForResult(activityIdentifier, requestCode, null, null, resultDelegate);
    }

    /** {@link ActivityRouter#nativeStartActivityForResult(String, int, Bundle, ActivityIntentDelegate, ActivityResultDelegate)}  */
    public static void startActivityForResult(String activityIdentifier, int requestCode, ActivityIntentDelegate intentDelegate, ActivityResultDelegate resultDelegate) {
        startActivityForResult(activityIdentifier, requestCode, null, intentDelegate, resultDelegate);
    }

    /** {@link ActivityRouter#nativeStartActivityForResult(String, int, Bundle, ActivityIntentDelegate, ActivityResultDelegate)}  */
    public static void startActivityForResult(String activityIdentifier, int requestCode, Bundle options, ActivityIntentDelegate intentDelegate, ActivityResultDelegate resultDelegate) {
        getInstance().nativeStartActivityForResult(activityIdentifier, requestCode, options, intentDelegate, resultDelegate);
    }


    /* Properties */
    /**
     * activity和标识符的配对
     */
    private Map<String, Class<?>> activityMap;
    protected Map<String, Class<?>> getActivityMap() {
        if (activityMap == null) {
            activityMap = new HashMap<>();
        }
        return activityMap;
    }

    /* Overrides */
     
     
    /* Delegates */
    /** {@link Application.ActivityLifecycleCallbacks} */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {
    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }
     
     
    /* Private Methods */
    /**
     * 根据标识符跳转到某个Activity
     * @param activityIdentifier 标识符
     * @param options options
     * @param intentDelegate 提供intent修改回调, 无需为intent设置activity
     */
    public void nativeStartActivity(String activityIdentifier, Bundle options, ActivityIntentDelegate intentDelegate) {
        Class<?> activityClazz = getInstance().getActivityMap().get(activityIdentifier);
        if (activityClazz != null) {
            Intent intent = new Intent(ActivityHelper.findTopActivity(), activityClazz);

            if (intentDelegate != null) {
                intentDelegate.setupIntentForStartActivity(intent);
            }

            ActivityHelper.findTopActivity().startActivity(intent, options);
        }
    }

    /**
     * 根据标识符跳转到某个Activity ， ForResult
     * 注意：若resultDelegate不为null，此时跳转的Activity将不会接收到{@link Activity#onActivityResult(int, int, Intent)}, result将由resultDelegate处理
     * @param activityIdentifier 标识符
     * @param requestCode requestCode
     * @param options options
     * @param intentDelegate 提供intent修改回调, 无需为intent设置activity
     * @param resultDelegate 提供result返回回调
     */
    private void nativeStartActivityForResult(String activityIdentifier, int requestCode, Bundle options, ActivityIntentDelegate intentDelegate, ActivityResultDelegate resultDelegate) {
        Class<?> activityClazz = getInstance().getActivityMap().get(activityIdentifier);
        if (activityClazz != null) {
            Intent intent = new Intent(ActivityHelper.findTopActivity(), activityClazz);

            if (intentDelegate != null) {
                intentDelegate.setupIntentForStartActivity(intent);
            }

            if (resultDelegate != null) {
                HookResultFragment hookResultFragment = new HookResultFragment().setActivityResultDelegate(resultDelegate);
                ActivityHelper.findTopActivity().getFragmentManager().beginTransaction().add(hookResultFragment, HookResultFragment.class.getSimpleName()).commit();
                ActivityHelper.findTopActivity().getFragmentManager().executePendingTransactions();

                hookResultFragment.startActivityForResult(intent, requestCode, options);
            }
            else {
                ActivityHelper.findTopActivity().startActivityForResult(intent, requestCode, options);
            }
        }
    }

}