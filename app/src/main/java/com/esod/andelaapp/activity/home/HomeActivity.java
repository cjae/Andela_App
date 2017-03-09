package com.esod.andelaapp.activity.home;

import android.content.Intent;
import android.os.Bundle;
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
import com.esod.andelaapp.event.DeveloperItemClickEvent;
import com.esod.andelaapp.model.Developer;
import com.esod.andelaapp.renderer.DeveloperRenderer;
import com.pedrogomez.renderers.AdapteeCollection;
import com.pedrogomez.renderers.ListAdapteeCollection;
import com.pedrogomez.renderers.RVRendererAdapter;
import com.pedrogomez.renderers.Renderer;
import com.pedrogomez.renderers.RendererBuilder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);

        mUserActionListener = new HomePresenter(this);
        mUserActionListener.getDataFromServer(getApplicationContext());
    }

    @Override
    public void showDevelopersList(List<Developer> developerList) {
        AdapteeCollection<Developer> developersCollection = new ListAdapteeCollection<>(developerList);
        Renderer<Developer> renderer = new DeveloperRenderer();
        RendererBuilder<Developer> rendererBuilder = new RendererBuilder<>(renderer);
        RVRendererAdapter<Developer> adapter = new RVRendererAdapter<>(rendererBuilder, developersCollection);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        developersRecyclerView.setLayoutManager(mLayoutManager);
        developersRecyclerView.setItemAnimator(new DefaultItemAnimator());
        developersRecyclerView.setAdapter(adapter);
    }

    @OnClick(R.id.retryButton) void doRetry(){
        mUserActionListener.retryButtonClick();
        mUserActionListener.getDataFromServer(getApplicationContext());
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