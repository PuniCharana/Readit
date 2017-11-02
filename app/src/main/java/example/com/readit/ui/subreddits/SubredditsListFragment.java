package example.com.readit.ui.subreddits;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
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
import example.com.readit.data.remote.models.Subreddit;
import example.com.readit.ui.viewSubreddit.ViewSubredditActivity;
import example.com.readit.utils.InternetConnectivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubredditsListFragment extends Fragment implements SubredditsListContract.SubredditsListView {

    private static final String SUBREDDITS = "SUBREDDITS";
    private static final String LOG_TAG = SubredditsListFragment.class.getSimpleName();
    private LinearLayoutManager mLayoutManager;
    @BindView(R2.id.recycler_view)
    RecyclerView mRecyclerView;
    private SubredditsAdapter mSubredditsAdapter;
    private ArrayList<Subreddit> mSubreddits = new ArrayList<>();
    @BindView(R2.id.progress_bar)
    ProgressBar mProgressBar;
    private int filterIndex = 0;

    private SubredditsListPresenter subredditsListPresenter;
    private String filterText = "popular";

    public SubredditsListFragment() {
        // Required empty public constructor
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
        View rootView = inflater.inflate(R.layout.fragment_subreddits_list, container, false);
        ButterKnife.bind(this, rootView);

        subredditsListPresenter = new SubredditsListPresenter(this);

        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        if (savedInstanceState != null && savedInstanceState.containsKey(SUBREDDITS)) {
            mSubreddits = savedInstanceState.getParcelableArrayList(SUBREDDITS);
        } else {

            if (InternetConnectivity.isConnected(getContext())) {
                subredditsListPresenter.loadSubreddits();
            } else {
                showSnackBar(getString(R.string.error_no_internet_connection));
            }

        }

        mSubredditsAdapter = new SubredditsAdapter(getContext(), mSubreddits, subredditsListPresenter);
        mRecyclerView.setAdapter(mSubredditsAdapter);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                subredditsListPresenter.onScrolled(mLayoutManager, dx, dy);
            }
        });

        return rootView;
    }

    private void showSnackBar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onSubredditsLoaded(ArrayList<Subreddit> subreddits) {
        Log.d(LOG_TAG, "onSubredditsLoaded");
        mProgressBar.setVisibility(View.INVISIBLE);
        mSubreddits.addAll(subreddits);
        mSubredditsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onError(String message) {
        Log.d(LOG_TAG, message);
        showSnackBar(getString(R.string.error_failed_to_load_data));
    }

    @Override
    public void showLoading() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return subredditsListPresenter.onOptionItemSelected(item);
    }

    @Override
    public void goToViewSubredditActivity(Subreddit subreddit) {
        Intent intent = new Intent(getContext(), ViewSubredditActivity.class);
        intent.putExtra(ViewSubredditActivity.SUBREDDIT_TAG, subreddit);
        startActivity(intent);
    }

    @Override
    public void onFilterOptionClick() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
        dialogBuilder.setTitle("Filter By :");
        dialogBuilder.setSingleChoiceItems(R.array.subreddits_filter_options, filterIndex, new DialogInterface
                .OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                filterIndex = item;
                String selectedFIlter = getResources().getStringArray(R.array.subreddits_filter_options)[item];
                changeFilter(selectedFIlter.toLowerCase());
                dialog.dismiss();
            }
        });
        AlertDialog alert = dialogBuilder.create();
        alert.show();
    }

    private void changeFilter(String filter) {
        filterText = filter;

        if (InternetConnectivity.isConnected(getContext())) {
            subredditsListPresenter.reLoadSubreddits();
            mSubreddits.clear();
            mSubredditsAdapter.notifyDataSetChanged();
        } else {
            showSnackBar(getString(R.string.error_no_internet_connection));
        }
    }

    @Override
    public void onSubscribed(String message) {
        Log.d(LOG_TAG, message);
        showSnackBar(message);
    }

    @Override
    public String getFilter() {
        return this.filterText;
    }
}
