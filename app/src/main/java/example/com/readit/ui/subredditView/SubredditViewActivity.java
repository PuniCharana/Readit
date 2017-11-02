package example.com.readit.ui.subredditView;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import example.com.readit.R;
import example.com.readit.ui.home.SubRedditFragment;
import example.com.readit.ui.settings.SettingsActivity;

public class SubredditViewActivity extends AppCompatActivity implements SubredditViewContract.SubredditViewView {

    public static final String SUBREDDIT_TO_LOAD = "SUBREDDIT_TO_LOAD";
    private static final String LOG_TAG = SubredditViewActivity.class.getSimpleName();

    private SubredditViewPresenter subredditViewPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subreddit_view);

        Bundle bundle = getIntent().getExtras();
        if (bundle!=null && bundle.containsKey(SUBREDDIT_TO_LOAD)) {
            String subreddit_to_load = bundle.getString(SUBREDDIT_TO_LOAD, " ");

            Log.d(LOG_TAG, subreddit_to_load);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle("/r/" + subreddit_to_load);
            }
            subredditViewPresenter = new SubredditViewPresenter(this);

            if (savedInstanceState != null) {
                return;
            }
            SubRedditFragment ingredientsFragment = SubRedditFragment.newInstance(subreddit_to_load);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ingredientsFragment)
                    .commit();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return subredditViewPresenter.onOptionItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public void onNavBackClicked() {
        finish();
    }

    @Override
    public void onActionSettingsClicked() {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
