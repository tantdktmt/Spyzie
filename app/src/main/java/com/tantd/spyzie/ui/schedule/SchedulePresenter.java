package com.tantd.spyzie.ui.schedule;

/**
 * Created by HP on 8/21/2017.
 */
public class SchedulePresenter extends ScheduleContract.Presenter {

    public SchedulePresenter() {
    }

    @Override
    public void loadSchedule() {
        compositeDisposable.add(apiHelper.getSchedules().subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui()).subscribe(schedules -> view.showSchedule(schedules)
                , error -> handleApiError(error)));
    }
}
