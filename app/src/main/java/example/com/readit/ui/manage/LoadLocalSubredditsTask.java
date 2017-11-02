package example.com.readit.ui.manage;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

import example.com.readit.R;
import example.com.readit.data.local.SubredditContract;
import example.com.readit.data.local.models.LocalSubreddit;

/**
 * Created by FamilyPC on 11/3/2017.
 */

public class LoadLocalSubredditsTask extends AsyncTask<String, Integer, Cursor> {

    private static final String LOG_TAG = LoadLocalSubredditsTask.class.getSimpleName();
    private Context mContext;
    private ManageSubredditsContract.ManageSubredditsView mManageSubredditsView;

    public LoadLocalSubredditsTask(Context context, ManageSubredditsContract.ManageSubredditsView manageSubredditsView) {
        mContext = context;
        mManageSubredditsView = manageSubredditsView;
    }

    @Override
    protected Cursor doInBackground(String... strings) {

        try {
            return mContext.getContentResolver().query(SubredditContract.SubredditEntry.CONTENT_URI,
                    null,
                    null,
                    null,
                    SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_POSITION);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to asynchronously load data.");
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Cursor cursor) {
        super.onPostExecute(cursor);

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
            mManageSubredditsView.onDataLoaded(localSubreddits);

        } else {
            mManageSubredditsView.onDataLoadError(mContext.getString(R.string.failed_to_load_data));
        }


    }
}
