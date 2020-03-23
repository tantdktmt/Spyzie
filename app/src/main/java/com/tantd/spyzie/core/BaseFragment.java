package com.tantd.spyzie.core;

import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * Created by tantd on 2/7/2020.
 */
public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;

    private SingleClick mSingleClick;
    private SingleItemClick mSingleItemClick;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
            mActivity.onFragmentAttached();
        }
    }

    public SingleClick getSingleClick(SingleClick.SingleClickListener listener) {
        if (mSingleClick == null) {
            mSingleClick = new SingleClick();
            mSingleClick.setListener(listener);
        }
        return mSingleClick;
    }

    public SingleItemClick getSingleItemClick(SingleItemClick.SingleItemClickListener listener) {
        if (mSingleItemClick == null) {
            mSingleItemClick = new SingleItemClick();
            mSingleItemClick.setListener(listener);
        }
        return mSingleItemClick;
    }

    public interface Callback {
        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }
}