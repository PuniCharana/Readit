package example.com.readit.ui.subreddits;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;

import java.util.ArrayList;

import example.com.readit.data.remote.models.Subreddit;

/**
 * Created by FamilyPC on 10/27/2017.
 */

@SuppressWarnings("ALL")
class SubredditsListContract {

    interface SubredditsListView {
        void onSubredditsLoaded(ArrayList<Subreddit> subreddits);

        void onError(String message);

        void showLoading();

        void goToViewSubredditActivity(Subreddit subreddit);

        void onFilterOptionClick();

        void onSubscribed(String display_name);

        String getFilter();
    }

    interface SubredditsListPresenter {
        void loadSubreddits();

        void onScrolled(LinearLayoutManager mLayoutManager, int dx, int dy);

        void onSubscribeClick(Context context, Subreddit subreddit, int position);

        void onViewSubreddit(Subreddit subreddit);

        boolean onOptionItemSelected(MenuItem item);

        void reLoadSubreddits();

    }
}
