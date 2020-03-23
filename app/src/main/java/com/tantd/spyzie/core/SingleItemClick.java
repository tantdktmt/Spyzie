package com.tantd.spyzie.core;

import android.os.SystemClock;
import android.view.View;
import android.widget.AdapterView;

import com.tantd.spyzie.util.Constants;

public class SingleItemClick implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        long currentTimestamp = SystemClock.uptimeMillis();
        if (lastClick != null) {
            if (listener != null && !(currentTimestamp - lastClick.getTimeStamp() <= minInterval)) {
                listener.onItemClick(parent, view, position, id);
                lastClick = new LastClick(currentTimestamp);
            }

        } else if (listener != null) {
            listener.onItemClick(parent, view, position, id);
            lastClick = new LastClick(currentTimestamp);
        }
    }

    public interface SingleItemClickListener {

        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }

    private SingleItemClickListener listener;
    private long minInterval;
    private LastClick lastClick;

    public SingleItemClick() {
        this.minInterval = Constants.SINGLE_CLICK_THRESHOLD;
    }

    public SingleItemClick(SingleItemClickListener listener) {
        this.minInterval = Constants.SINGLE_CLICK_THRESHOLD;
        this.listener = listener;
    }


    public SingleItemClickListener getListener() {
        return listener;
    }

    public void setListener(SingleItemClickListener listener) {
        this.listener = listener;
    }

    public long getMinInterval() {
        return minInterval;
    }

    public void setMinInterval(long minInterval) {
        this.minInterval = minInterval;
    }

    private class LastClick {
        private long timeStamp;

        /**
         * @return the timeStamp
         */
        public long getTimeStamp() {
            return timeStamp;
        }

        public LastClick(long timeStamp) {
            super();
            this.timeStamp = timeStamp;
        }

    }

}
