package com.tantd.spyzie.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

/**
 * Created by tantd on 8/22/2017
 */
public abstract class BaseCustomLayout extends RelativeLayout {
    public BaseCustomLayout(Context context) {
        super(context);
        setLayout();
        initCompoundView();
        initData();
        setListener();
    }

    public BaseCustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
        setLayout();
        initCompoundView();
        initData();
        setListener();
    }

    protected int[] getStyleableId() {
        return null;
    }

    protected void initDataFromStyleable(TypedArray a) {
    }

    protected abstract int getLayoutId();

    protected abstract void initCompoundView();

    protected abstract void initData();

    protected abstract void setListener();

    private void init(AttributeSet attrs) {
        if (getStyleableId() != null && getStyleableId().length > 0) {
            TypedArray a = getContext().getTheme().obtainStyledAttributes(attrs, getStyleableId(), 0, 0);
            try {
                initDataFromStyleable(a);
            } catch (Exception e) {
                Log.e("BaseCustomLayout", "Cannot init view");
            } finally {
                a.recycle();
            }
        }
    }

    private void setLayout() {
        if (getLayoutId() != 0) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            inflater.inflate(getLayoutId(), this, true);
        }
    }
}