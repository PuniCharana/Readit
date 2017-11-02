package example.com.readit.ui.home;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.readit.R;
import example.com.readit.R2;
import example.com.readit.data.remote.models.SubredditPost;
import example.com.readit.ui.postDetail.PostDetailActivity;
import example.com.readit.ui.postDetail.PostDetailFragment;
import example.com.readit.ui.settings.SettingsActivity;
import example.com.readit.utils.InternetConnectivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubRedditFragment extends Fragment implements SubredditFragmentContract.SubredditFragmentView {
    private static final String ARG_SUB_REDDIT_ID = "ARG_SUB_REDDIT_ID";
    private static final String LOG_TAG = SubRedditFragment.class.getSimpleName();
    private static final String SUBREDDIT_POSTS = "SUBREDDIT_POSTS";
    private static final String FILTER_INDEX = "FILTER_INDEX";
    @BindView(R2.id.post_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R2.id.progress_bar)
    ProgressBar mProgressBar;
    private int filterIndex;

    private String subredditToLoad;
    private SubredditFragmentAdapter fragmentAdapter;
    private ArrayList<SubredditPost> subredditPosts = new ArrayList<>();
    private SubredditFragmentContract.SubredditFragmentPresenter subredditFragmentPresenter;

    public SubRedditFragment() {
        // Required empty public constructor
    }

    public static SubRedditFragment newInstance(String subReddit) {
        Bundle arguments = new Bundle();
        arguments.putString(ARG_SUB_REDDIT_ID, subReddit);
        SubRedditFragment fragment = new SubRedditFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_sub_reddit, container, false);
        ButterKnife.bind(this, rootView);

        if (getArguments().containsKey(ARG_SUB_REDDIT_ID)) {
            Log.d(LOG_TAG, "ARG_SUB_REDDIT_ID");
            this.subredditToLoad = getArguments().getString(ARG_SUB_REDDIT_ID);
        }
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(SettingsActivity.FILTER_OPTION, Context.MODE_PRIVATE);
        filterIndex = sharedPreferences.getInt(SettingsActivity.SELECTED_FILTER_OPTION, 0);

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        subredditFragmentPresenter = new SubredditFragmentPresenter(this);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SUBREDDIT_POSTS)) {
                subredditPosts = savedInstanceState.getParcelableArrayList(SUBREDDIT_POSTS);
            }

            if (savedInstanceState.containsKey(FILTER_INDEX)) {
                filterIndex = savedInstanceState.getInt(FILTER_INDEX);
            }
        } else {

            if (InternetConnectivity.isConnected(getContext())) {
                subredditFragmentPresenter.loadData();
            } else {
                showSnackBar(getString(R.string.error_no_internet_connection));
            }
        }
        fragmentAdapter = new SubredditFragmentAdapter(getContext(), subredditPosts, subredditFragmentPresenter);
        mRecyclerView.setAdapter(fragmentAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                subredditFragmentPresenter.onScrolled(linearLayoutManager, dx, dy);
            }
        });

        return rootView;
    }

    private void showSnackBar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_filter) {

            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
            dialogBuilder.setTitle("Filter By :");
            dialogBuilder.setSingleChoiceItems(R.array.subreddit_filter_options, filterIndex, new DialogInterface
                    .OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    filterIndex = item;
                    changeFilter();
                    dialog.dismiss();
                }
            });
            AlertDialog alert = dialogBuilder.create();
            alert.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LOG_TAG, "onSaveInstanceState");
        outState.putParcelableArrayList(SUBREDDIT_POSTS, subredditPosts);
        outState.putInt(FILTER_INDEX, filterIndex);
    }

    @Override
    public String getSubredditToLoad() {
        return subredditToLoad;
    }

    private void changeFilter() {
        subredditPosts.clear();
        fragmentAdapter.notifyDataSetChanged();

        if (InternetConnectivity.isConnected(getContext())) {
            subredditFragmentPresenter.reloadData();
        } else {
            showSnackBar(getString(R.string.error_no_internet_connection));
        }

    }

    @Override
    public String getFilter() {
        switch(filterIndex){
            case 0:
                return "hot";
            case 1:
                return "top";
            case 2:
                return "new";
            case 3:
                return "rising";
            case 4:
                return "controversial";
            default:
                return "hot";
        }
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPostItemClicked(SubredditPost subredditPostPermalink) {
        Intent intent = new Intent(getContext(), PostDetailActivity.class);
        intent.putExtra(PostDetailFragment.SUBREDDIT_POST_KEY, subredditPostPermalink);
        startActivity(intent);
    }

    @Override
    public void OnMoreOptionClicked(SubredditPost subredditPost) {
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText(subredditPost.getUrl())
                .getIntent(), getString(R.string.action_share)));
    }

    private void hideLoading() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onDataLoaded(ArrayList<SubredditPost> subredditPostArrayList) {
        hideLoading();
        Log.d(LOG_TAG, "data loaded");
        subredditPosts.addAll(subredditPostArrayList);
        fragmentAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDataLoadError(String message) {
        showSnackBar(getString(R.string.error_failed_to_load_data));
        hideLoading();
    }
}