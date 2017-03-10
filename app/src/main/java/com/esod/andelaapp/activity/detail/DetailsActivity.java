package com.esod.andelaapp.activity.detail;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.esod.andelaapp.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailsActivity extends AppCompatActivity
        implements DetailsContract.View {

    private DetailsContract.UserActionListener mUserActionListener;

    @Bind(R.id.fab)
    FloatingActionButton fab;

    @Bind(R.id.user_dp)
    ImageView user_dp;

    @Bind(R.id.linkText)
    TextView linkText;

    @Bind(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsing_toolbar;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ButterKnife.bind(this);

        mUserActionListener = new DetailsPresenter(this);

        setUpViews();
    }

    private void setUpViews() {
        final String username = getIntent().getStringExtra("username");
        final String url = getIntent().getStringExtra("url");
        final String dp = getIntent().getStringExtra("dp");

        linkText.setText(url);
        collapsing_toolbar.setTitle(username);

        Picasso.with(this)
                .load(dp)
                .placeholder(R.drawable.user_uknown)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        assert user_dp != null;
                        user_dp.setImageBitmap(bitmap);

                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
                                        setToolbarColor(palette);
                                    }
                                });
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserActionListener.shareButtonClick(username, url);
            }
        });
    }

    private void setToolbarColor(Palette palette){
        int defaultDarkColor = ContextCompat.getColor(getApplicationContext(), R.color.colorPrimaryDark);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(palette.getDarkVibrantColor(defaultDarkColor));
        }
    }

    @OnClick(R.id.linkText) void onClickLink() {
        mUserActionListener.linkClick(getIntent().getStringExtra("url"));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showWebView(String url) {
        Uri uri = Uri.parse(url);
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        intentBuilder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark));

        intentBuilder.setStartAnimations(this, R.anim.slide_in_right, R.anim.slide_out_left);
        intentBuilder.setExitAnimations(this, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

        CustomTabsIntent customTabsIntent = intentBuilder.build();

        customTabsIntent.launchUrl(this, uri);
    }

    @Override
    public void doShareIntent(String username, String url) {
        String shareText = String.format(getString(R.string.share_message_format), username, url);

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        startActivity(Intent.createChooser(sharingIntent, getString(R.string.share_using)));
    }
}
