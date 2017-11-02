package example.com.readit.ui.manage;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.firebase.analytics.FirebaseAnalytics;

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
    private final Context mContext;

    public ManageSubredditsPresenter(ManageSubredditsContract.ManageSubredditsView subredditsView, Context context) {
        manageSubredditsView = subredditsView;
        mContext = context;
    }

    @Override
    public void loadData() {
        new LoadLocalSubredditsTask(mContext, manageSubredditsView).execute();
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

        manageSubredditsView.onItemRemoved("Deleted " + localSubreddit.getDisplay_name());

        new LoadLocalSubredditsTask(mContext, manageSubredditsView).execute();

        // Firebase Analytics - unsubscribe
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(mContext);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, localSubreddit.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, localSubreddit.getDisplay_name());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "unsubscribe");
        mFirebaseAnalytics.logEvent("unsubscribe", bundle);
    }
}
