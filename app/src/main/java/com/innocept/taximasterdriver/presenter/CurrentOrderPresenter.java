package com.innocept.taximasterdriver.presenter;

import com.innocept.taximasterdriver.ui.activity.CurrentOrderActivity;
import com.innocept.taximasterdriver.ui.activity.NewOrderActivity;

/**
 * Created by dulaj on 5/28/16.
 */
public class CurrentOrderPresenter {

    private static CurrentOrderPresenter instance = null;
    private CurrentOrderActivity view;

    private CurrentOrderPresenter() {
    }

    public static CurrentOrderPresenter getInstance(){
        if(instance==null){
            instance = new CurrentOrderPresenter();
        }
        return instance;
    }

    public void setView(CurrentOrderActivity view) {
        this.view = view;
    }

}
