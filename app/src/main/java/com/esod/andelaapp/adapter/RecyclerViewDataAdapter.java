package com.esod.andelaapp.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.esod.andelaapp.R;
import com.esod.andelaapp.event.DeveloperItemClickEvent;
import com.esod.andelaapp.model.Developer;
import com.esod.andelaapp.util.AppTags;
import com.esod.andelaapp.util.OnLoadMoreListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by Jedidiah on 10/03/2017.
 */

public class RecyclerViewDataAdapter extends RecyclerView.Adapter {

    private List<Developer> developerList;

    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;

    private Context context;
    private OnLoadMoreListener onLoadMoreListener;

    public RecyclerViewDataAdapter(Context context, List<Developer> developerList, RecyclerView recyclerView) {
        this.developerList = developerList;
        this.context = context;

        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        return developerList.get(position) != null ? AppTags.CARD_DEVELOPER : AppTags.CARD_LOADING;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == AppTags.CARD_DEVELOPER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.developer_item, parent, false);

            vh = new DeveloperViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.progress_item, parent, false);

            vh = new DeveloperViewHolder.ProgressViewHolder(v);
        }
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof DeveloperViewHolder) {

            final Developer developer = developerList.get(position);

            String text = String.format(context.getString(R.string.username_format), developer.get_login());
            ((DeveloperViewHolder) holder).developerName.setText(text);
            Picasso.with(context)
                    .load(developer.get_avatar_url())
                    .placeholder(R.drawable.user_uknown)
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(((DeveloperViewHolder) holder).developerDp, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(context)
                                    .load(developer.get_avatar_url())
                                    .placeholder(R.drawable.user_uknown)
                                    .into(((DeveloperViewHolder) holder).developerDp);
                        }
                    });
            ((DeveloperViewHolder) holder).bg.setBackgroundColor(developer.get_bg_color());

            ((DeveloperViewHolder) holder).item_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBus.getDefault().post(new DeveloperItemClickEvent(developer));
                }
            });

        }
    }

    public void setLoaded() {
        loading = false;
    }

    @Override
    public int getItemCount() {
        return developerList.size();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }


    private static class DeveloperViewHolder extends RecyclerView.ViewHolder {
        LinearLayout item_content;

        CircularImageView developerDp;

        TextView developerName;

        TextView locationText;

        View bg;

        DeveloperViewHolder(View v) {
            super(v);
            item_content = (LinearLayout) v.findViewById(R.id.item_content);
            developerDp = (CircularImageView) v.findViewById(R.id.developerDp);
            developerName = (TextView) v.findViewById(R.id.developerName);
            locationText = (TextView) v.findViewById(R.id.locationText);
            bg = v.findViewById(R.id.bg);
        }

        static class ProgressViewHolder extends RecyclerView.ViewHolder {
            ProgressBar progressBar;

            ProgressViewHolder(View v) {
                super(v);
                progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
            }
        }
    }
}
