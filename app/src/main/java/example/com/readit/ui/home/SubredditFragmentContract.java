package example.com.readit.ui.home;

import android.support.v7.widget.LinearLayoutManager;

import java.util.ArrayList;

import example.com.readit.data.remote.models.SubredditPost;

/**
 * Created by FamilyPC on 10/25/2017.
 */

@SuppressWarnings("ALL")
class SubredditFragmentContract {

    interface SubredditFragmentView {
        void onDataLoaded(ArrayList<SubredditPost> subredditArrayList);

        void onDataLoadError(String message);

        String getSubredditToLoad();

        String getFilter();

        void showLoading();

        void onPostItemClicked(SubredditPost subredditPost);

        void OnMoreOptionClicked(SubredditPost subredditPost);
    }

    interface SubredditFragmentPresenter {
        void loadData();

        void reloadData();

        void onScrolled(LinearLayoutManager linearLayoutManager, int dx, int dy);

        void onPostItemClick(SubredditPost subredditPost);

        void onMoreOption(SubredditPost subredditPost);
    }
}
