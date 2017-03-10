package com.esod.andelaapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.esod.andelaapp.R;
import com.esod.andelaapp.activity.detail.DetailsActivity;
import com.esod.andelaapp.adapter.RecyclerViewDataAdapter;
import com.esod.andelaapp.event.DeveloperItemClickEvent;
import com.esod.andelaapp.model.Developer;
import com.esod.andelaapp.util.AppTags;
import com.esod.andelaapp.util.OnLoadMoreListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity
        implements HomeContract.View {

    private HomeContract.UserActionListener mUserActionListener;

    @Bind(R.id.progressLoader)
    ProgressBar progressLoader;

    @Bind(R.id.network_layout)
    View network_layout;

    @Bind(R.id.developersRecyclerView)
    RecyclerView developersRecyclerView;

    LinearLayoutManager mLayoutManager;

    private RecyclerViewDataAdapter mAdapter;

    private List<Developer> developerList;

    protected Handler handler;

    int page_no = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!= null) {
            getSupportActionBar().setTitle(getString(R.string.title_activity_home));
        }

        ButterKnife.bind(this);

        mLayoutManager = new LinearLayoutManager(this);
        developerList = new ArrayList<>();
        handler = new Handler();

        mUserActionListener = new HomePresenter(this);
        mUserActionListener.getDataFromServer(getApplicationContext(), page_no);
    }

    @Override
    public void showDevelopersList(final List<Developer> devList) {
        developerList = devList;
        page_no++;

        developersRecyclerView.setLayoutManager(mLayoutManager);
        developersRecyclerView.setHasFixedSize(true);
        developersRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new RecyclerViewDataAdapter(this, developerList, developersRecyclerView);
        developersRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //add null , so the adapter will check view_type and show progress bar at bottom
                        developerList.add(null);
                        mAdapter.notifyItemInserted(developerList.size() - 1);

                        mUserActionListener.loadMore(getApplicationContext(), page_no);
                    }
                }, 500);
            }
        });
    }

    @Override
    public void updateDeveloperList(final List<Developer> devList) {
        if(devList.isEmpty()){
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //   remove progress item
                    developerList.remove(developerList.size() - 1);
                    mAdapter.notifyItemRemoved(developerList.size());

                    mAdapter.setLoaded();
                }
            }, 2000);
        } else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //   remove progress item
                    developerList.remove(developerList.size() - 1);
                    mAdapter.notifyItemRemoved(developerList.size());

                    developerList.addAll(devList);
                    mAdapter.notifyItemInserted(developerList.size());

                    page_no++;
                    mAdapter.setLoaded();
                }
            }, 2000);
        }
    }

    @OnClick(R.id.retryButton) void doRetry(){
        mUserActionListener.retryButtonClick();
        mUserActionListener.getDataFromServer(getApplicationContext(), page_no);
    }

    @Override
    public void showNetworkErrorView() {
        progressLoader.setVisibility(View.GONE);
        network_layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNetworkErrorView() {
        progressLoader.setVisibility(View.VISIBLE);
        network_layout.setVisibility(View.GONE);
    }

    @Override
    public void displayTotalDevelopers(int total) {
        String text = String.format(getString(R.string.total_format), total);
        if(getSupportActionBar()!= null) {
            getSupportActionBar().setSubtitle(text);
        }
    }

    @Override
    public void showProgress() {
        progressLoader.setVisibility(View.VISIBLE);
        developersRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void hideProgress() {
        progressLoader.setVisibility(View.GONE);
        developersRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showDetailsScreen(Developer developer) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("username", developer.get_login());
        intent.putExtra("dp", developer.get_avatar_url());
        intent.putExtra("url", developer.get_html_url());
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeveloperItemClickEvent(DeveloperItemClickEvent event) {
        mUserActionListener.listItemClick(event.developer);
    }

    @Override
    public void showMessageToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }
}