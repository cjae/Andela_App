package com.esod.andelaapp.activity.home;

import android.content.Context;

import com.esod.andelaapp.model.Developer;

import java.util.List;

/**
 * Created by Jedidiah on 07/03/2017.
 */

interface HomeContract {

    interface View {
        void showDevelopersList(List<Developer> developerList);

        void showNetworkErrorView();

        void hideNetworkErrorView();

        void showProgress();

        void hideProgress();

        void showDetailsScreen(Developer developer);

        void showMessageToast(String message);
    };

    interface UserActionListener {
        void getDataFromServer(Context context);

        void listItemClick(Developer developer);

        void loadMore();

        void refreshClick();

        void retryButtonClick();
    }
}
