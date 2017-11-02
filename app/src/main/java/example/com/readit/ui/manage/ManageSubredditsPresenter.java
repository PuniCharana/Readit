package example.com.readit.ui.manage;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;

import example.com.readit.R;
import example.com.readit.data.local.SubredditContract;
import example.com.readit.data.local.models.LocalSubreddit;

/**
 * Created by FamilyPC on 10/26/2017.
 */

@SuppressWarnings("ALL")
public class ManageSubredditsPresenter implements ManageSubredditsContract.Presenter {

    private static final String LOG_TAG = ManageSubredditsPresenter.class.getCanonicalName();
    private final ManageSubredditsContract.ManageSubredditsView manageSubredditsView;

    private static final int TASK_LOADER_ID = 1001;
    private LoaderManager mLoaderManager;

    private final Context mContext;

    public ManageSubredditsPresenter(ManageSubredditsContract.ManageSubredditsView subredditsView, Context context) {
        manageSubredditsView = subredditsView;
        mContext = context;
    }

    @Override
    public void loadData(LoaderManager loaderManager) {
        mLoaderManager = loaderManager;
        mLoaderManager.initLoader(TASK_LOADER_ID, null, new ManageSubredditsPresenter.LoadData(mContext));
    }

    @Override
    public boolean onOptionItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_add_to_widget:
                manageSubredditsView.onAddToWidgetClicked();
                return true;
            case R.id.action_settings:
                manageSubredditsView.onSettingsOptionClick();
                return true;
            case R.id.action_help:
                manageSubredditsView.onHelpOptionClick();
                return true;
            case R.id.action_ascending:
                manageSubredditsView.onAscendingOptionClick();
                return true;
            case R.id.action_descending:
                manageSubredditsView.onDescendingOptionClick();
                return true;
        }
        return false;
    }

    @Override
    public void onRemoveItemClick(LocalSubreddit localSubreddit) {
        Uri uri = SubredditContract.SubredditEntry.CONTENT_URI;

        String where = SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_ID + "= ?";
        String[] whereArgs = new String[]{localSubreddit.getId()};

        mContext.getContentResolver().delete(uri, where, whereArgs);
        Toast.makeText(mContext, "Deleted " + localSubreddit.getDisplay_name(), Toast.LENGTH_SHORT).show();

        mLoaderManager.restartLoader(TASK_LOADER_ID, null, new ManageSubredditsPresenter.LoadData(mContext));

        // Firebase Analytics - unsubscribe
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, localSubreddit.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, localSubreddit.getDisplay_name());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "unsubscribe");
        mFirebaseAnalytics.logEvent("unsubscribe", bundle);
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
            Log.d(LOG_TAG, "onLoadFinished");

            if (cursor != null) {
                Log.d(LOG_TAG, cursor.getCount() + "");
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
                manageSubredditsView.onDataLoaded(localSubreddits);

            } else {
                manageSubredditsView.onDataLoadError("Failed to load data");
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            Log.d(LOG_TAG, "onLoaderReset");
        }
    }
}
