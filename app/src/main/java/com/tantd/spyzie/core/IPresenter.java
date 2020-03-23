package com.tantd.spyzie.core;

/**
 * Created by tantd on 2/7/2020.
 */
public interface IPresenter<V extends IView> {

    void onAttach(V view);

    void onDetach();

    void handleApiError(Throwable error);
}
