package example.com.readit.ui.viewSubreddit;

import android.support.design.widget.AppBarLayout;
import android.view.MenuItem;

/**
 * Created by FamilyPC on 10/27/2017.
 */

@SuppressWarnings("ALL")
class ViewSubredditContract {

    interface ViewSubredditView {
        void onAppBarCollapsed();

        void onAppBarExpanded();

        void onFavoriteClicked();

        void onActionSettingsClicked();
    }

    interface Presenter {
        void addOnOffsetChanged(AppBarLayout appBarLayout, int verticalOffset);

        void addFavoriteClick();

        boolean onOptionsItemSelected(MenuItem item);
    }
}
