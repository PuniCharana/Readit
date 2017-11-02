package example.com.readit.ui.postDetail;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.readit.R;
import example.com.readit.R2;
import example.com.readit.data.remote.models.SubredditPost;

public class PostDetailActivity extends AppCompatActivity implements PostDetailContract.PostDetailView {

    private PostDetailPresenter postDetailPresenter;

    @BindView(R2.id.adView)
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        postDetailPresenter = new PostDetailPresenter(this);

        Bundle bundle = getIntent().getExtras();

        if (bundle!= null && bundle.containsKey(PostDetailFragment.SUBREDDIT_POST_KEY)) {

            if (getSupportActionBar() != null) {
                SubredditPost subredditPost = bundle.getParcelable(PostDetailFragment.SUBREDDIT_POST_KEY);
                if (subredditPost!=null) {
                    getSupportActionBar().setTitle(getString(R.string.placeholder_subreddit_title, subredditPost.getSubreddit()));
                }
            }

            if (savedInstanceState != null) {
                return;
            }

            // init fragment
            PostDetailFragment postDetailFragment = new PostDetailFragment();
            postDetailFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, postDetailFragment)
                    .commit();
        }

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return postDetailPresenter.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPresses() {
        finish();
    }
}
