package com.tantd.spyzie.util;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.tantd.spyzie.R;
import com.tantd.spyzie.core.BaseFragment;

/**
 * Created by tantd on 8/22/2017.
 */
public class FragmentController {

    public static void addFragmentToActivity(@NonNull FragmentManager fm,
                                             @NonNull Fragment fragment, int frameId) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }

    public static synchronized <T extends BaseFragment> void switchFragment(int containerView, FragmentManager fm, T fragment, boolean addToBackstack) {
        if (fm != null) {
            FragmentTransaction ft = fm.beginTransaction();
            doAddAnimation(ft);
            ft.replace(containerView, fragment);
            if (addToBackstack) {
                ft.addToBackStack(fragment.getClass().getSimpleName());
            }
            try {
                ft.commitAllowingStateLoss();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    public static synchronized void doAddAnimation(FragmentTransaction ft) {
        if (ft != null) {
            ft.setCustomAnimations(R.anim.fragment_slide_left_enter, R.anim.fragment_slide_left_exit,
                    R.anim.fragment_slide_right_enter, R.anim.fragment_slide_right_exit);
        }
    }

    public static synchronized void popToFragmentByTag(FragmentManager fm, String tag) {
        if (fm != null && tag != null) {
            fm.popBackStackImmediate(tag, 0);
        }
    }

    public static synchronized void popBackStack(FragmentManager fm) {
        if (fm != null) {
            fm.popBackStackImmediate();
        }
    }

    public static synchronized void clearFragment(FragmentManager fm) {
        if (fm != null) {
            while (fm.getBackStackEntryCount() > 0) {
                fm.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
        }
    }

    public static synchronized Fragment getCurrentFragment(FragmentManager fm, int containerViewID) {
        Fragment currentFragment = null;
        if (fm != null) {
            currentFragment = fm.findFragmentById(containerViewID);
        }
        return currentFragment;
    }
}
