package example.com.readit.ui.home;

import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import example.com.readit.data.remote.ApiClient;
import example.com.readit.data.remote.RedditApiService;
import example.com.readit.data.remote.models.SubredditPost;
import example.com.readit.data.remote.models.SubredditPostResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by FamilyPC on 10/25/2017.
 */

@SuppressWarnings("ALL")
public class SubredditFragmentPresenter implements SubredditFragmentContract.SubredditFragmentPresenter {

    private static final String LOG_TAG = SubredditFragmentPresenter.class.getSimpleName();
    private final SubredditFragmentContract.SubredditFragmentView subredditFragmentView;

    private boolean mIsLoading = false;
    private static final int mCount = 25;
    private String mAfter;

    public SubredditFragmentPresenter(SubredditFragmentContract.SubredditFragmentView fragmentView) {
        this.subredditFragmentView = fragmentView;
    }

    @Override
    public void loadData() {
        mIsLoading = true;
        subredditFragmentView.showLoading();
        RedditApiService redditApiService = ApiClient.getClient().create(RedditApiService.class);
        Call<SubredditPostResponse> subredditResponseCall = redditApiService.getSubredditPosts(
                subredditFragmentView.getSubredditToLoad(),
                subredditFragmentView.getFilter(),
                mCount,
                mAfter);

        subredditResponseCall.enqueue(new Callback<SubredditPostResponse>() {
            @Override
            public void onResponse(Call<SubredditPostResponse> call, Response<SubredditPostResponse> response) {

                Log.d(LOG_TAG, response.raw().request().url().toString());
                mIsLoading = false;

                SubredditPostResponse subredditResponse = response.body();
                SubredditPostResponse.SubredditResponseData subredditResponseData = subredditResponse.getData();
                List<SubredditPostResponse.SubredditResponseData.SubredditResult> subreddits = subredditResponseData.getChildren();

                ArrayList<SubredditPost> subredditArrayList = new ArrayList<>();
                for (int i = 0; i < subreddits.size(); i++) {
                    SubredditPost subredditPost = subreddits.get(i).getData();
                    subredditArrayList.add(subredditPost);
                }
                mAfter = subredditResponseData.getAfter();
                subredditFragmentView.onDataLoaded(subredditArrayList);
            }

            @Override
            public void onFailure(Call<SubredditPostResponse> call, Throwable t) {
                mIsLoading = false;
                Log.d(LOG_TAG, "onFailure ", t);
                subredditFragmentView.onDataLoadError(t.getMessage());
            }
        });
    }

    @Override
    public void reloadData() {
        this.mAfter = null;
        loadData();
    }

    @Override
    public void onScrolled(LinearLayoutManager linearLayoutManager, int dx, int dy) {
        if (dy > 0) { // on scroll down
            int totalItemCount = linearLayoutManager.getItemCount();
            int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();

            // continue only if it is not loading already and if last visible item is the last item itself.
            if (!mIsLoading && (lastVisibleItem >= (totalItemCount - 1))) {

                Log.d(LOG_TAG, "totalItemCount " + totalItemCount);
                Log.d(LOG_TAG, "lastVisibleItem " + lastVisibleItem);
                Log.d(LOG_TAG, "" + mIsLoading);
                loadData();
                subredditFragmentView.showLoading();
            }
        }
    }

    @Override
    public void onPostItemClick(SubredditPost subredditPost) {
        subredditFragmentView.onPostItemClicked(subredditPost);
    }

    @Override
    public void onMoreOption(SubredditPost subredditPost) {
        subredditFragmentView.OnMoreOptionClicked(subredditPost);
    }
}
