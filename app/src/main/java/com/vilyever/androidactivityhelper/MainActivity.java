package com.vilyever.androidactivityhelper;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.vilyever.activityhelper.ActivityHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        System.out.println("resumed activity " + ActivityHelper.findResumedActivity());
        System.out.println("top activity " + ActivityHelper.findTopActivity());
    }

    @Override
    protected void onResume() {
        super.onResume();

        System.out.println("onResume resumed activity " + ActivityHelper.findResumedActivity());
        System.out.println("onResume top activity " + ActivityHelper.findTopActivity());
    }
}
