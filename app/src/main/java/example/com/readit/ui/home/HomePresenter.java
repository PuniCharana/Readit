package example.com.readit.ui.home;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;

import example.com.readit.R;
import example.com.readit.data.local.SubredditContract;
import example.com.readit.data.local.models.LocalSubreddit;

/**
 * Created by FamilyPC on 10/24/2017.
 */

@SuppressWarnings("ALL")
public class HomePresenter implements HomeContract.Presenter {

    private static final String LOG_TAG = HomePresenter.class.getSimpleName();
    private final HomeContract.HomeView homeView;
    private static final int TASK_LOADER_ID = 1000;
    private LoaderManager mLoaderManager;


    public HomePresenter(HomeContract.HomeView homeView) {
        this.homeView = homeView;
    }

    @Override
    public void onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_all:
                homeView.navItemClick("all");
                break;
            case R.id.nav_popular:
                homeView.navItemClick("popular");
                break;
            case R.id.nav_random:
                homeView.navItemClick("random");
                break;
            case R.id.nav_reddit_picks:
                homeView.navRedditPicksClick();
                break;
            case R.id.nav_manage:
                homeView.showShareNavigationClicked();
                break;
            case R.id.nav_subreddits:
                homeView.showSubredditsListActivity();
                break;
        }
    }

    @Override
    public void onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            homeView.onSettingsItemOptionItemClick();
        }
    }

    @Override
    public void loadData(LoaderManager loaderManager, Context context) {
        Log.d(LOG_TAG, "constructor loadData");
        mLoaderManager = loaderManager;
        mLoaderManager.initLoader(TASK_LOADER_ID, null, new LoadData(context));
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy loader");
        mLoaderManager.destroyLoader(TASK_LOADER_ID);
    }


    public class LoadData implements LoaderManager.LoaderCallbacks<Cursor> {


        private final Context context;

        public LoadData(Context context) {
            this.context = context;
            Log.d(LOG_TAG, "LoadData");
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            Log.d(LOG_TAG, "onCreateLoader");
            return new AsyncTaskLoader<Cursor>(context) {

                @Override
                protected void onStartLoading() {
                    // Force a new load
                    forceLoad();
                }

                @Override
                public Cursor loadInBackground() {
                    try {
                        return context.getContentResolver().query(SubredditContract.SubredditEntry.CONTENT_URI,
                                null,
                                null,
                                null,
                                SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_POSITION);

                    } catch (Exception e) {
                        Log.e("LO", "Failed to asynchronously load data.");
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(Cursor data) {
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            Log.d(LOG_TAG, "onLoadFinished what");

            if (cursor != null) {
                Log.d(LOG_TAG, cursor.getCount() + " cursor count");
                ArrayList<LocalSubreddit> localSubreddits = new ArrayList<>();
                while (cursor.moveToNext()) {

                    int display_name_index = cursor.getColumnIndex(SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_DISPLAY_NAME);
                    int id_index = cursor.getColumnIndex(SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_ID);
                    int url_index = cursor.getColumnIndex(SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_URL);
                    int position_index = cursor.getColumnIndex(SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_POSITION);

                    String display_name = cursor.getString(display_name_index);
                    String id = cursor.getString(id_index);
                    String url = cursor.getString(url_index);
                    int position = cursor.getInt(position_index);

                    LocalSubreddit localSubreddit = new LocalSubreddit(display_name, id, url, position);

                    localSubreddits.add(localSubreddit);

                }
                homeView.onDataLoaded(localSubreddits);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            Log.d(LOG_TAG, "onLoaderReset");
        }
    }
}
