package example.com.readit.ui.subreddits;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import example.com.readit.R;
import example.com.readit.ui.settings.SettingsActivity;

public class SubredditsListActivity extends AppCompatActivity implements SubredditsContract.SubredditsView {

    private SubredditsPresenter mSubredditsPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subreddits_list);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_subreddits);
        }

        mSubredditsPresenter = new SubredditsPresenter(this);

        // do not create the fragment again if already created
        // in case of screen rotation
        if (savedInstanceState != null)
            return;

        SubredditsListFragment ingredientsFragment = new SubredditsListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ingredientsFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.subreddits, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mSubredditsPresenter.onOptionItemSelected(item);
    }

    @Override
    public void onSettingsOptionCLick() {
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
