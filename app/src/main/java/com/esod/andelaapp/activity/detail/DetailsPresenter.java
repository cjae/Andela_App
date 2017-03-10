package com.esod.andelaapp.activity.detail;

import android.support.annotation.NonNull;


/**
 * Created by Jedidiah on 09/03/2017.
 */

class DetailsPresenter implements DetailsContract.UserActionListener {

    private DetailsContract.View mDetailScreenView;

    DetailsPresenter(@NonNull DetailsContract.View mDetailScreenView) {
        this.mDetailScreenView = mDetailScreenView;
    }

    @Override
    public void linkClick(String url) {
        mDetailScreenView.showWebView(url);
    }

    @Override
    public void shareButtonClick(String username, String url) {
        mDetailScreenView.doShareIntent(username, url);
    }
}
