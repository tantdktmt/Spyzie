package com.tantd.spyzie.ui.schedule;

import com.tantd.spyzie.core.BasePresenter;
import com.tantd.spyzie.core.IView;
import com.tantd.spyzie.data.network.model.Event;

import java.util.List;

/**
 * Created by HP on 8/21/2017.
 */
public interface ScheduleContract {
    interface View extends IView<Presenter> {
        void showSchedule(List<Event> lstEvent);
    }

    abstract class Presenter extends BasePresenter<View> {
        public abstract void loadSchedule();
    }
}
