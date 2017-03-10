package com.esod.andelaapp.activity.detail;

import android.content.Context;

import com.esod.andelaapp.model.Developer;

import java.util.List;

/**
 * Created by Jedidiah on 09/03/2017.
 */

interface DetailsContract {

    interface View {

        void showWebView(String url);

        void doShareIntent(String username, String url);
    };

    interface UserActionListener {

        void linkClick(String url);

        void shareButtonClick(String username, String url);
    }
}
