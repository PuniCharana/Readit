package example.com.readit.ui.subreddits;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MenuItem;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import example.com.readit.R;
import example.com.readit.data.local.SubredditContract;
import example.com.readit.data.remote.ApiClient;
import example.com.readit.data.remote.RedditApiService;
import example.com.readit.data.remote.models.Subreddit;
import example.com.readit.data.remote.models.SubredditResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by FamilyPC on 10/27/2017.
 */

@SuppressWarnings("ALL")
public class SubredditsListPresenter implements SubredditsListContract.SubredditsListPresenter {
    private static final String LOG_TAG = SubredditsListPresenter.class.getSimpleName();
    private final SubredditsListContract.SubredditsListView subredditsListView;


    private boolean mIsLoading = false;

    private static final int mCount = 25;
    private String mAfter;

    public SubredditsListPresenter(SubredditsListContract.SubredditsListView subredditsListView) {
        this.subredditsListView = subredditsListView;
    }

    @Override
    public void loadSubreddits() {
        mIsLoading = true;
        subredditsListView.showLoading();
        RedditApiService redditApiService = ApiClient.getClient().create(RedditApiService.class);
        Call<SubredditResponse> subredditResponseCall = redditApiService.getSubreddits("subreddits", subredditsListView.getFilter(), mCount, mAfter);

        subredditResponseCall.enqueue(new Callback<SubredditResponse>() {
            @Override
            public void onResponse(Call<SubredditResponse> call, Response<SubredditResponse> response) {

                Log.d(LOG_TAG, response.raw().request().url().toString());
                mIsLoading = false;

                SubredditResponse subredditResponse = response.body();
                SubredditResponse.SubredditResponseData subredditResponseData = subredditResponse.getData();
                List<SubredditResponse.SubredditResponseData.SubredditResult> subreddits = subredditResponseData.getChildren();

                ArrayList<Subreddit> subredditArrayList = new ArrayList<>();
                for (int i = 0; i < subreddits.size(); i++) {
                    Subreddit subreddit = subreddits.get(i).getData();
                    subredditArrayList.add(subreddit);
                }
                mAfter = subredditResponseData.getAfter();
                subredditsListView.onSubredditsLoaded(subredditArrayList);
            }

            @Override
            public void onFailure(Call<SubredditResponse> call, Throwable t) {
                mIsLoading = false;
                Log.d(LOG_TAG, "onFailure ", t);
                subredditsListView.onError(t.getMessage());
            }
        });
    }

    @Override
    public void onScrolled(LinearLayoutManager mLayoutManager, int dx, int dy) {
        if (dy > 0) { // on scroll down
            int totalItemCount = mLayoutManager.getItemCount();
            int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

            // continue only if it is not loading already and if last visible item is the last item itself.
            if (!mIsLoading && (lastVisibleItem >= (totalItemCount - 1))) {

                Log.d(LOG_TAG, "totalItemCount " + totalItemCount);
                Log.d(LOG_TAG, "lastVisibleItem " + lastVisibleItem);
                Log.d(LOG_TAG, "" + mIsLoading);
                loadSubreddits();
                subredditsListView.showLoading();
            }
        }
    }

    @Override
    public void onSubscribeClick(Context context, Subreddit subreddit, int position) {

        ContentValues contentValues = new ContentValues();
        contentValues.put(SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_DISPLAY_NAME, subreddit.getDisplay_name());
        contentValues.put(SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_ID, subreddit.getId());
        contentValues.put(SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_URL, subreddit.getUrl());
        contentValues.put(SubredditContract.SubredditEntry.COLUMN_SUBREDDIT_POSITION, position);

        // Insert the content values via a ContentResolver
        Uri uri = context.getContentResolver().insert(SubredditContract.SubredditEntry.CONTENT_URI, contentValues);

        if (uri != null) {
            Log.d("DATA", uri.getLastPathSegment());

            if (Integer.parseInt(uri.getLastPathSegment()) > 0) {
                Log.d("DATA", "Success");
                subredditsListView.onSubscribed(subreddit.getDisplay_name());

                // Firebase Analytics subscribe
                FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, subreddit.getId());
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, subreddit.getDisplay_name());
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "subscribe");
                mFirebaseAnalytics.logEvent("subscribe", bundle);

            } else {
                Log.d("DATA", "failed");
                subredditsListView.onError("Already added");
            }
        }
    }

    @Override
    public void onViewSubreddit(Subreddit subreddit) {
        subredditsListView.goToViewSubredditActivity(subreddit);
    }

    @Override
    public boolean onOptionItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_filter:
                subredditsListView.onFilterOptionClick();
                return true;
        }
        return false;
    }

    @Override
    public void reLoadSubreddits() {
        mAfter = null;
        loadSubreddits();
    }
}
