package com.tantd.spyzie.core;

import android.content.Context;

import androidx.fragment.app.Fragment;

/**
 * Created by tantd on 2/7/2020.
 */
public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
            mActivity.onFragmentAttached();
        }
    }

    public interface Callback {
        void onFragmentAttached();

        void onFragmentDetached(String tag);
    }
}