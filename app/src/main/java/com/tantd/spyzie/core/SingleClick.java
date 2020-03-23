package com.tantd.spyzie.core;

import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;

import com.tantd.spyzie.util.Constants;
import com.tantd.spyzie.util.LogUtils;

/**
 *
 */

/**
 * <b>Class Overview</b> <br>
 * <br>
 * Represents a class for preventing multiple clicking events of a single
 * component by implementing <b><code>SingleClickListener</code></b>. All
 * components need to prevent multiple clicking should apply <b>
 * <code>SingleClick</code></b> to <b><code>OnClickListener</code></b><br>
 * <br>
 * <b>Summary</b>
 */
public class SingleClick implements OnClickListener, AdapterView.OnItemClickListener {


    /**
     * public interface<br>
     * <b>SingleClickListener</b><br>
     * <br>
     * <b>Class Overview</b> <br>
     * <br>
     * Used for receiving notifications from the <code>SingleClick</code> when
     * event click of a single component is fired.<br>
     * <br>
     * <b>Summary</b>
     */
    public interface SingleClickListener {
        /**
         * <b>Specified by:</b> onSingleClick(...) in SingleClickListener <br>
         * <br>
         * This is called immediately after the click event is being fired
         * within the pre-defined minimum interval time.
         *
         * @param v The view is being clicked
         */
        void onClick(View v);
    }

    private SingleClickListener listener;
    private long minInterval;
    private LastClick lastClick;

    public SingleClick() {
        this.minInterval = Constants.SINGLE_CLICK_THRESHOLD;
    }

    public SingleClick(SingleClickListener listener) {
        this.minInterval = Constants.SINGLE_CLICK_THRESHOLD;
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        LogUtils.e("SingleClick click: " + v.getId());
        long currentTimestamp = SystemClock.uptimeMillis();
        if (lastClick != null) {
            if (listener != null && !(currentTimestamp - lastClick.getTimeStamp() <= minInterval)) {
                listener.onClick(v);
                lastClick = new LastClick(currentTimestamp);
            }

        } else if (listener != null) {
            listener.onClick(v);
            lastClick = new LastClick(currentTimestamp);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public SingleClickListener getListener() {
        return listener;
    }

    public void setListener(SingleClickListener listener) {
        this.listener = listener;
    }

    public long getMinInterval() {
        return minInterval;
    }

    public SingleClick setMinInterval(long minInterval) {
        this.minInterval = minInterval;
        return this;
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
