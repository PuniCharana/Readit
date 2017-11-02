package example.com.readit.ui.viewSubreddit;

import android.support.design.widget.AppBarLayout;
import android.view.MenuItem;

import example.com.readit.R;

/**
 * Created by FamilyPC on 10/27/2017.
 */

@SuppressWarnings("ALL")
public class ViewSubredditPresenter implements ViewSubredditContract.Presenter {

    private static final String LOG_TAG = ViewSubredditPresenter.class.getSimpleName();
    private final ViewSubredditContract.ViewSubredditView viewSubredditView;

    public ViewSubredditPresenter(ViewSubredditContract.ViewSubredditView viewSubredditView) {
        this.viewSubredditView = viewSubredditView;
    }

    @Override
    public void addOnOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if (Math.abs(verticalOffset) - appBarLayout.getTotalScrollRange() == 0) {
            viewSubredditView.onAppBarCollapsed();
        } else {
            viewSubredditView.onAppBarExpanded();
        }
    }

    @Override
    public void addFavoriteClick() {
        viewSubredditView.onFavoriteClicked();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                viewSubredditView.onActionSettingsClicked();
                return true;
        }
        return false;
    }
}
