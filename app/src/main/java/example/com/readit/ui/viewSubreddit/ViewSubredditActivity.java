package example.com.readit.ui.viewSubreddit;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.analytics.FirebaseAnalytics;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.com.readit.R;
import example.com.readit.R2;
import example.com.readit.data.local.SubredditContract;
import example.com.readit.data.remote.models.Subreddit;
import example.com.readit.ui.home.SubRedditFragment;
import example.com.readit.ui.settings.SettingsActivity;
import example.com.readit.utils.AppUtils;

public class ViewSubredditActivity extends AppCompatActivity implements ViewSubredditContract.ViewSubredditView {

    public static final String SUBREDDIT_TAG = "SUBREDDIT_TAG";
    private static final String COLLAPSED_HEADER = "COLLAPSED_HEADER";

    @BindView(R2.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R2.id.toolbar)
    Toolbar mToolbar;
    @BindView(R2.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout mCollapsingToolbarLayout;
    @BindView(R2.id.header_image)
    ImageView mHeaderImageView;
    @BindView(R2.id.subreddit_title_container)
    LinearLayout subredditTitleContainer;
    @BindView(R2.id.subreddit_title)
    TextView subredditTitle;
    @BindView(R2.id.subreddit_tagline)
    TextView subredditTagline;
    @BindView(R2.id.favorite_fab)
    FloatingActionButton favoriteBtn;

    private Subreddit subreddit;
    private ViewSubredditPresenter viewSubredditPresenter;

    private boolean collapseHeader = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_subreddit);
        ButterKnife.bind(this);

        if (getSupportActionBar()!=null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        viewSubredditPresenter = new ViewSubredditPresenter(this);


        Bundle bundle = getIntent().getExtras();
        if (bundle!=null && bundle.containsKey(SUBREDDIT_TAG)) {
            subreddit = bundle.getParcelable(SUBREDDIT_TAG);

            if (subreddit==null) {
                return;
            }
            mCollapsingToolbarLayout.setTitle(subreddit.getUrl());
            mCollapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppBar);
            subredditTitle.setText(subreddit.getUrl());
            String tagline = AppUtils.formatSubscriber(subreddit.getSubscribers(), 0) +
                    " subscribers, community since " + AppUtils.formatYear(subreddit.getCreated());
            subredditTagline.setText(tagline);


            String image = subreddit.getHeader_img();
            if (!TextUtils.isEmpty(subreddit.getBanner_img())) {
                image = subreddit.getBanner_img();
            }

            Glide.with(this)
                    .load(image)
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            Bitmap bitmap = ((GlideBitmapDrawable) resource.getCurrent()).getBitmap();
                            // Palette palette = Palette.from(bitmap).generate();
                            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    int defaultColor = 0xFF333333;
                                    int color = palette.getDarkMutedColor(defaultColor);
                                    subredditTitleContainer.setBackgroundColor(color);
                                }
                            });
                            return false;
                        }
                    })
                    .into(mHeaderImageView);

            appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
                @Override
                public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                    viewSubredditPresenter.addOnOffsetChanged(appBarLayout, verticalOffset);
                }
            });

            // maintain header collapse state
            if (savedInstanceState != null && savedInstanceState.containsKey(COLLAPSED_HEADER)) {
                collapseHeader = savedInstanceState.getBoolean(COLLAPSED_HEADER);
                appBarLayout.setExpanded(collapseHeader, true);
            }


            if (savedInstanceState != null) {
                return;
            }
            SubRedditFragment ingredientsFragment = SubRedditFragment.newInstance(subreddit.getDisplay_name());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ingredientsFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(COLLAPSED_HEADER, collapseHeader);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.subreddits, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return viewSubredditPresenter.onOptionsItemSelected(item);
    }

    @OnClick(R2.id.favorite_fab)
    public void favoriteClick(View view) {
        viewSubredditPresenter.addFavoriteClick();
    }

    @Override
    public void onAppBarCollapsed() {
        subredditTitleContainer.setVisibility(View.GONE);
        collapseHeader = false;
    }

    @Override
    public void onAppBarExpanded() {
        subredditTitleContainer.setVisibility(View.VISIBLE);
        collapseHeader = true;
    }

    @Override
    public void onFavoriteClicked() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_DISPLAY_NAME, subreddit.getDisplay_name());
        contentValues.put(SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_ID, subreddit.getId());
        contentValues.put(SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_URL, subreddit.getUrl());
        contentValues.put(SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_POSITION, 0);

        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(SubredditContract.SubredditEntry.CONTENT_URI, contentValues);

        // Display the URI that's returned with a Toast
        if (uri != null) {
            Log.d("DATA", uri.getLastPathSegment());

            if (Integer.parseInt(uri.getLastPathSegment()) > 0) {
                Log.d("DATA", "Success");
                Toast.makeText(this, subreddit.getDisplay_name(), Toast.LENGTH_SHORT).show();

                // Firebase Analytics subscribe
                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(getApplicationContext());
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, subreddit.getId());
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, subreddit.getDisplay_name());
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "subscribe");
                mFirebaseAnalytics.logEvent("subscribe", bundle);
            } else {
                Log.d("DATA", "failed");
                Toast.makeText(this, "Already added", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onActionSettingsClicked() {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
