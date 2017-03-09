package com.esod.andelaapp.activity.detail;

import android.content.Context;

import com.esod.andelaapp.model.Developer;

import java.util.List;

/**
 * Created by Jedidiah on 09/03/2017.
 */

public interface DetailsContract {

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
