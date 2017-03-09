package com.esod.andelaapp.activity.home;

import android.content.Context;
import android.support.annotation.NonNull;

import com.esod.andelaapp.R;
import com.esod.andelaapp.model.APIResponse;
import com.esod.andelaapp.model.Developer;
import com.esod.andelaapp.rest.APIClient;
import com.esod.andelaapp.rest.ApiInterface;
import com.esod.andelaapp.util.AppTags;
import com.esod.andelaapp.util.CommonUtil;
import com.esod.andelaapp.util.SessionManager;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Jedidiah on 07/03/2017.
 */

class HomePresenter implements HomeContract.UserActionListener {

    private HomeContract.View mHomeScreenView;

    HomePresenter(@NonNull HomeContract.View mHomeScreenView) {
        this.mHomeScreenView = mHomeScreenView;
    }

    @Override
    public void getDataFromServer(final Context context) {
        final int page_no = SessionManager.getPageNo(context);
        String url = String.format(context.getString(R.string.url_format), page_no, 20);

        if(CommonUtil.isNetworkAvailable(context)){
            ApiInterface apiService = APIClient.getClient().create(ApiInterface.class);
            Call<APIResponse> call = apiService.getDevelopers(url);
            call.enqueue(new Callback<APIResponse>() {
                @Override
                public void onResponse(Call<APIResponse> call, Response<APIResponse> response) {
                    try {
                        List<Developer> developersList = new LinkedList<>();
                        JSONArray itemArray = new JSONArray(new Gson().toJson(response.body().getItems()));

                        for(int i = 0;i<itemArray.length();i++) {
                            JSONObject myObject = itemArray.getJSONObject(i);

                            int id  =  myObject.getInt(AppTags.DEVELOPER_ID);
                            String name  =  myObject.getString(AppTags.DEVELOPER_NAME);
                            String thumbnail  =  myObject.getString(AppTags.DEVELOPER_THUMBNAIL);
                            String url  =  myObject.getString(AppTags.DEVELOPER_URL);
                            int bg_color = CommonUtil.getRandomMaterialColor(context, "400");

                            Developer developer = new Developer(id, name, thumbnail, url, bg_color);
                            developersList.add(developer);
                        }

                        int counter = page_no;
                        SessionManager.setPageNo(context, counter++);
                        mHomeScreenView.hideProgress();
                        mHomeScreenView.showDevelopersList(developersList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<APIResponse> call, Throwable t) {
                    mHomeScreenView.showNetworkErrorView();
                }
            });
        } else {
            mHomeScreenView.showNetworkErrorView();
        }
    }

    @Override
    public void listItemClick(Developer developer) {
        mHomeScreenView.showDetailsScreen(developer);
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void refreshClick() {

    }

    @Override
    public void retryButtonClick() {
        mHomeScreenView.hideNetworkErrorView();
    }
}
