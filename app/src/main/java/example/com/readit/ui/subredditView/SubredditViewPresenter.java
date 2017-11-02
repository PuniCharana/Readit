package example.com.readit.ui.subredditView;

import android.view.MenuItem;

import example.com.readit.R;

/**
 * Created by FamilyPC on 11/1/2017.
 */

@SuppressWarnings("ALL")
public class SubredditViewPresenter implements SubredditViewContract.Presenter {

    private static final String LOG_TAG = SubredditViewPresenter.class.getSimpleName();
    private final SubredditViewContract.SubredditViewView subredditViewView;

    public SubredditViewPresenter(SubredditViewContract.SubredditViewView subredditViewView) {
        this.subredditViewView = subredditViewView;
    }

    @Override
    public boolean onOptionItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                subredditViewView.onNavBackClicked();
                return true;
            case R.id.action_settings:
                subredditViewView.onActionSettingsClicked();
                return true;
        }
        return false;
    }
}
