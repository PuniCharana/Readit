package example.com.readit.ui.subreddits;

import android.view.MenuItem;

/**
 * Created by FamilyPC on 2017-10-24.
 */

@SuppressWarnings("ALL")
class SubredditsContract {

    interface SubredditsView {
        void onSettingsOptionCLick();
    }

    interface Presenter {
        boolean onOptionItemSelected(MenuItem item);
    }
}
