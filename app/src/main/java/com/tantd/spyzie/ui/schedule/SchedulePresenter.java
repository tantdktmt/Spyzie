package com.tantd.spyzie.ui.schedule;

import com.tantd.spyzie.data.network.ApiManager;
import com.tantd.spyzie.util.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by HP on 8/21/2017.
 */
public class SchedulePresenter extends ScheduleContract.Presenter {

    @Inject
    public SchedulePresenter(SchedulerProvider schedulerProvider, ApiManager apiManager, CompositeDisposable compositeDisposable) {
        super(schedulerProvider, apiManager, compositeDisposable);
    }

    @Override
    public void loadSchedule() {
        compositeDisposable.add(mApiManager.getSchedules().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(schedules -> view.showSchedule(schedules)
                , error -> handleApiError(error)));
    }
}
