package com.tantd.spyzie.ui.schedule;

import com.tantd.spyzie.core.BasePresenter;
import com.tantd.spyzie.core.IView;
import com.tantd.spyzie.data.network.ApiManager;
import com.tantd.spyzie.data.model.Event;
import com.tantd.spyzie.util.rx.SchedulerProvider;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by HP on 8/21/2017.
 */
public interface ScheduleContract {
    interface View extends IView<Presenter> {
        void showSchedule(List<Event> lstEvent);
    }

    abstract class Presenter extends BasePresenter<View> {

        public Presenter(SchedulerProvider schedulerProvider, ApiManager apiManager, CompositeDisposable compositeDisposable) {
            super(schedulerProvider, apiManager, compositeDisposable);
        }

        public abstract void loadSchedule();
    }
}
