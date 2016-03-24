package com.vilyever.activityhelper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.vilyever.contextholder.ContextHolder;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * ActivityHelper
 * ESB <com.vilyever.esb>
 * Created by vilyever on 2016/2/19.
 * Feature:
 * Activity帮助类
 */
public class ActivityHelper implements Application.ActivityLifecycleCallbacks {
    final ActivityHelper self = this;

    private static ActivityHelper Instance;

    /* Constructors */
    
    /* Public Methods */
    /**
     * 单例
     * @return 单例
     */
    public synchronized static ActivityHelper getInstance() {
        if (Instance == null) {
            Instance = new ActivityHelper();
        }
        return Instance;
    }

    /**
     * 初始化，必须在Application中调用
     * @param application 当前应用application
     */
    public static void initialize(Application application) {
        if (!getInstance().isInitialized()) {
            getInstance().setApplication(application);
            getInstance().setInitialized(true);
        }
    }

    /**
     * 查找当前resumed的Activity
     * 锁屏和app在后台时将返回null
     * activity在onCreate时将返回null
     * @return 前台resumed的Activity
     * @deprecated try {@link #findTopActivity()} to get top activity even not resumed
     */
    @Nullable
    @Deprecated
    public static Activity findResumedActivity() {
        if (getInstance().isInitialized()
                && findTopActivity() != null) {
            try {
                Field resumedField = Activity.class.getDeclaredField("mResumed");
                resumedField.setAccessible(true);
                boolean resumed = (boolean) resumedField.get(findTopActivity());

                if (resumed) {
                    return findTopActivity();
                }
            }
            catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        }
        else {
            initialize((Application) ContextHolder.getContext());

            try {
                Activity resumedActivity = null;

                Object activityThread = Class.forName("android.app.ActivityThread")
                                             .getMethod("currentActivityThread").invoke(null);

                Field activityMapField = Class.forName("android.app.ActivityThread").getDeclaredField("mActivities");
                activityMapField.setAccessible(true);
                Map activityMap = (Map) activityMapField.get(activityThread);

                Iterator it = activityMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();

                    Field activityField = Class.forName("android.app.ActivityThread$ActivityClientRecord").getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(pair.getValue());

                    Field resumedField = Activity.class.getDeclaredField("mResumed");
                    resumedField.setAccessible(true);
                    boolean resumed = (boolean) resumedField.get(activity);

                    if (resumed) {
                        resumedActivity = activity;
                    }

                    if (!getInstance().getActivities().contains(activity)) {
                        getInstance().getActivities().add(activity);
                    }
                }

                return resumedActivity;
            }
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * 获取当前Activity
     * @return 当前Activity
     */
    public static Activity findTopActivity() {
        getInstance().checkInitialized();
        return getInstance().getTopActivity();
    }

    /**
     * 结束所有activity
     */
    public static void finishAllActivities() {
        getInstance().checkInitialized();

        ArrayList<Activity> activitiesCopy =
                (ArrayList<Activity>) getInstance().getActivities().clone();
        int count = activitiesCopy.size();
        for (int i = 0; i < count; ++i) {
            if (activitiesCopy.get(i) != null) {
                activitiesCopy.get(i).finish();
            }
        }

        getInstance().getActivities().clear();
    }

    public static void registerActivityStateDelegate(ActivityStateDelegate activityStateDelegate) {
        getInstance().checkInitialized();
        if (!getInstance().getActivityStateDelegates().contains(activityStateDelegate)) {
            getInstance().getActivityStateDelegates().add(activityStateDelegate);
        }
    }

    public static void removeActivityStateDelegate(ActivityStateDelegate activityStateDelegate) {
        getInstance().checkInitialized();
        getInstance().getActivityStateDelegates().remove(activityStateDelegate);
    }

    /* Properties */
    private boolean initialized;
    protected ActivityHelper setInitialized(boolean initialized) {
        this.initialized = initialized;
        return this;
    }
    protected boolean isInitialized() {
        return initialized;
    }

    private Application application;
    protected ActivityHelper setApplication(Application application) {
        this.application = application;

        application.registerActivityLifecycleCallbacks(getInstance());
        application.registerActivityLifecycleCallbacks(ActivityRouter.getInstance());

        return this;
    }
    protected Application getApplication() {
        return this.application;
    }

    /**
     * 当前应用中所有activity
     */
    private LinkedList<Activity> activities;
    private LinkedList<Activity> getActivities() {
        if (activities == null) {
            activities = new LinkedList<>();
        }
        return activities;
    }

    /**
     * 当前Activity
     */
    private Activity topActivity;
    protected ActivityHelper setTopActivity(Activity topActivity) {
        this.topActivity = topActivity;
        return this;
    }
    protected Activity getTopActivity() {
        return topActivity;
    }

    private ArrayList<ActivityStateDelegate> activityStateDelegates;
    protected ArrayList<ActivityStateDelegate> getActivityStateDelegates() {
        if (this.activityStateDelegates == null) {
            this.activityStateDelegates = new ArrayList<ActivityStateDelegate>();
        }
        return this.activityStateDelegates;
    }

    /* Overrides */     
     
    /* Delegates */
    /** {@link Application.ActivityLifecycleCallbacks} */
    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        getActivities().add(activity);
        setTopActivity(activity);

        ArrayList<ActivityStateDelegate> delegatesCopy =
                (ArrayList<ActivityStateDelegate>) getActivityStateDelegates().clone();
        int count = delegatesCopy.size();
        for (int i = 0; i < count; ++i) {
            delegatesCopy.get(i).onActivityCreated(activity, savedInstanceState);
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {

        ArrayList<ActivityStateDelegate> delegatesCopy =
                (ArrayList<ActivityStateDelegate>) getActivityStateDelegates().clone();
        int count = delegatesCopy.size();
        for (int i = 0; i < count; ++i) {
            delegatesCopy.get(i).onActivityStarted(activity);
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        setTopActivity(activity);

        ArrayList<ActivityStateDelegate> delegatesCopy =
                (ArrayList<ActivityStateDelegate>) getActivityStateDelegates().clone();
        int count = delegatesCopy.size();
        for (int i = 0; i < count; ++i) {
            delegatesCopy.get(i).onActivityResumed(activity);
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

        ArrayList<ActivityStateDelegate> delegatesCopy =
                (ArrayList<ActivityStateDelegate>) getActivityStateDelegates().clone();
        int count = delegatesCopy.size();
        for (int i = 0; i < count; ++i) {
            delegatesCopy.get(i).onActivityPaused(activity);
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {

        ArrayList<ActivityStateDelegate> delegatesCopy =
                (ArrayList<ActivityStateDelegate>) getActivityStateDelegates().clone();
        int count = delegatesCopy.size();
        for (int i = 0; i < count; ++i) {
            delegatesCopy.get(i).onActivityStopped(activity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        ArrayList<ActivityStateDelegate> delegatesCopy =
                (ArrayList<ActivityStateDelegate>) getActivityStateDelegates().clone();
        int count = delegatesCopy.size();
        for (int i = 0; i < count; ++i) {
            delegatesCopy.get(i).onActivitySaveInstanceState(activity, outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        getActivities().remove(activity);

        ArrayList<ActivityStateDelegate> delegatesCopy =
                (ArrayList<ActivityStateDelegate>) getActivityStateDelegates().clone();
        int count = delegatesCopy.size();
        for (int i = 0; i < count; ++i) {
            delegatesCopy.get(i).onActivityDestroyed(activity);
        }
    }
     
    /* Private Methods */

    /**
     * 检查是否初始化
     */
    private void checkInitialized() {
        if (!isInitialized()) {
            throw new IllegalStateException("ActivityHelper is not initialized, which must initialize with application.");
        }
    }
}