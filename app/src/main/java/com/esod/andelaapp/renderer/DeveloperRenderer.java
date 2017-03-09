package com.esod.andelaapp.renderer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esod.andelaapp.R;
import com.esod.andelaapp.event.DeveloperItemClickEvent;
import com.esod.andelaapp.model.Developer;
import com.esod.andelaapp.util.CommonUtil;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.pedrogomez.renderers.Renderer;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Jedidiah on 08/03/2017.
 */

public class DeveloperRenderer extends Renderer<Developer> {

    private Context context;

    @Bind(R.id.developerDp)
    CircularImageView developerDp;

    @Bind(R.id.developerName)
    TextView developerName;

    @Bind(R.id.bg)
    View bg;

    @Override
    protected void setUpView(View rootView) {

    }

    @Override
    protected void hookListeners(View rootView) {

    }

    @Override
    protected View inflate(LayoutInflater inflater, ViewGroup parent) {
        View inflatedView = inflater.inflate(R.layout.developer_item, parent, false);

        this.context = parent.getContext();
        ButterKnife.bind(this, inflatedView);

        return inflatedView;
    }

    @Override
    public void render() {
        Developer developer = getContent();
        renderDeveloperItemUI(developer);
    }

    private void renderDeveloperItemUI(final Developer developer) {
        String text = String.format(context.getString(R.string.username_format), developer.get_login());
        developerName.setText(text);

        Picasso.with(getContext())
                .load(developer.get_avatar_url())
                .placeholder(R.drawable.user_uknown)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(developerDp, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(getContext())
                                .load(developer.get_avatar_url())
                                .placeholder(R.drawable.user_uknown)
                                .into(developerDp);
                    }
                });

        developerDp.setBorderColor(developer.get_bg_color());
        bg.setBackgroundColor(developer.get_bg_color());
    }

    @OnClick(R.id.item_content) void onClickItem(){
        Developer developer = getContent();
        EventBus.getDefault().post(new DeveloperItemClickEvent(developer));
    }
}
