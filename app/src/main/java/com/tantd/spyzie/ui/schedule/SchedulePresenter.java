package com.tantd.spyzie.ui.schedule;

import com.tantd.spyzie.data.network.ApiHelper;
import com.tantd.spyzie.util.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by HP on 8/21/2017.
 */
public class SchedulePresenter extends ScheduleContract.Presenter {

    @Inject
    public SchedulePresenter(SchedulerProvider schedulerProvider, ApiHelper apiHelper, CompositeDisposable compositeDisposable) {
        super(schedulerProvider, apiHelper, compositeDisposable);
    }

    @Override
    public void loadSchedule() {
        compositeDisposable.add(apiHelper.getSchedules().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(schedules -> view.showSchedule(schedules)
                , error -> handleApiError(error)));
    }
}
