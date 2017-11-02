package example.com.readit.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.readit.R;
import example.com.readit.R2;
import example.com.readit.data.local.models.LocalSubreddit;
import example.com.readit.ui.manage.ManageSubredditsActivity;
import example.com.readit.ui.settings.SettingsActivity;
import example.com.readit.ui.subredditPicks.RedditPicksActivity;
import example.com.readit.ui.subredditView.SubredditViewActivity;
import example.com.readit.ui.subreddits.SubredditsListActivity;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeContract.HomeView {

    private static final String LOG_TAG = HomeActivity.class.getSimpleName();
    private static final String SELECTED_TAB = "SELECTED_TAB";
    private static final String SELECTED_TAB_PREF = "SELECTED_TAB_PREF";

    @BindView(R2.id.app_bar_layout)
    AppBarLayout appBarLayout;
    @BindView(R2.id.toolbar)
    Toolbar toolbar;
    @BindView(R2.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R2.id.nav_view)
    NavigationView navigationView;
    @BindView(R2.id.view_pager)
    ViewPager viewPager;
    @BindView(R2.id.tab_layout)
    TabLayout tabLayout;

    @BindView(R2.id.adView)
    AdView mAdView;

    @BindView(R2.id.no_subscription)
    TextView noSubscription;

    private ArrayList<LocalSubreddit> subreddits = new ArrayList<>();
    private HomePresenter mHomePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        tabLayout.setupWithViewPager(viewPager);
        mHomePresenter = new HomePresenter(this);
        mHomePresenter.loadData(getSupportLoaderManager(), this);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mHomePresenter.onOptionsItemSelected(item);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mHomePresenter.onNavigationItemSelected(item);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showShareNavigationClicked() {
        startActivity(new Intent(this, ManageSubredditsActivity.class));
    }

    @Override
    public void onSettingsItemOptionItemClick() {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    @Override
    public void showSubredditsListActivity() {
        startActivity(new Intent(this, SubredditsListActivity.class));
    }

    @Override
    public void onDataLoaded(ArrayList<LocalSubreddit> subredditArrayList) {
        // This is to handle if the order of the db has changed
        SharedPreferences sharedPreferences = getSharedPreferences("ORDER_CHANGED", MODE_PRIVATE);
        boolean order_changed = sharedPreferences.getBoolean("ORDER_HAS_CHANGED", false);

        // Continue only if tab items changes ie first time, adding item, remove item
        if (subreddits.size() != subredditArrayList.size() || order_changed) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("ORDER_HAS_CHANGED", false);
            editor.apply();

            TabsViewPagerAdapter tabsViewPagerAdapter = new TabsViewPagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(tabsViewPagerAdapter);
            subreddits = subredditArrayList;

            if (subreddits.size() > 0) {
                noSubscription.setVisibility(View.GONE);
                tabLayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < subreddits.size(); i++) {
                    tabsViewPagerAdapter.addTitle(subreddits.get(i).getDisplay_name());
                }

                tabsViewPagerAdapter.notifyDataSetChanged();
                // Load previously selected tab
                SharedPreferences prefs = getSharedPreferences(SELECTED_TAB_PREF, MODE_PRIVATE);
                int selectedTab = prefs.getInt(SELECTED_TAB, 0); //0 is the loading value.
                Log.d(LOG_TAG, selectedTab + " selected tab");
                viewPager.setCurrentItem(selectedTab, true);
            } else {
                tabLayout.setVisibility(View.GONE);
                noSubscription.setVisibility(View.VISIBLE);
                Log.d(LOG_TAG, "NO ITEM FOUND");
            }
        }
    }

    @Override
    public void navItemClick(String id) {
        Intent intent = new Intent(this, SubredditViewActivity.class);
        intent.putExtra(SubredditViewActivity.SUBREDDIT_TO_LOAD, id);
        startActivity(intent);
    }

    @Override
    public void navRedditPicksClick() {
        Intent intent = new Intent(this, RedditPicksActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Save selected tab position
        SharedPreferences.Editor editor = getSharedPreferences(SELECTED_TAB_PREF, MODE_PRIVATE).edit();
        editor.putInt(SELECTED_TAB, viewPager.getCurrentItem());
        editor.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy");
        mHomePresenter.onDestroy();
    }
}
