package com.tantd.spyzie.ui.schedule;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.tantd.spyzie.R;
import com.tantd.spyzie.SpyzieApplication;
import com.tantd.spyzie.core.BaseActivity;
import com.tantd.spyzie.util.FragmentController;

/**
 * Created by tantd on 2/7/2020.
 */
public class ScheduleActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpyzieApplication.getInstance().getAppComponent().inject(this);
        setContentView(R.layout.activity_schedule);

        ScheduleFragment scheduleFragment = (ScheduleFragment) getSupportFragmentManager().findFragmentById(R.id.layout_content);
        if (scheduleFragment == null) {
            scheduleFragment = ScheduleFragment.newInstance();
            FragmentController.addFragmentToActivity(getSupportFragmentManager(), scheduleFragment, R.id.layout_content);
        }
    }
}
