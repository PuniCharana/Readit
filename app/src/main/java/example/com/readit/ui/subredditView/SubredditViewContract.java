package example.com.readit.ui.subredditView;

import android.view.MenuItem;

/**
 * Created by FamilyPC on 11/1/2017.
 */

@SuppressWarnings("ALL")
class SubredditViewContract {

    interface SubredditViewView {
        void onNavBackClicked();

        void onActionSettingsClicked();
    }

    interface Presenter {
        boolean onOptionItemSelected(MenuItem item);
    }
}
