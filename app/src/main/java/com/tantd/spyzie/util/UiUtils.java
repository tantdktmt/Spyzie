package com.tantd.spyzie.util;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.EditText;

/**
 * Created by tantd on 8/22/2017.
 */
public class UiUtils {

    public static void setBackground(View view, Drawable drawable) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    public static void setEditTextEnable(EditText editText, boolean enabled, int enabledResId, int paddingLeft) {
        editText.setEnabled(enabled);
        if (enabled) {
            editText.setBackgroundResource(enabledResId);
            editText.setPadding(paddingLeft, editText.getPaddingTop(), editText.getPaddingRight(), editText.getPaddingBottom());
        } else {
            setBackground(editText, null);
            editText.setPadding(0, editText.getPaddingTop(), editText.getPaddingRight(), editText.getPaddingBottom());
        }
    }
}
