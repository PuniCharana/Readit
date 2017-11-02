package example.com.readit.ui.settings;

import android.view.MenuItem;

/**
 * Created by FamilyPC on 10/26/2017.
 */

@SuppressWarnings("ALL")
class SettingsContract {
    interface SettingsView {
        void onBackButtonClick();
    }

    interface SettingsPresenter {

        boolean onOptionsItemSelected(MenuItem item);
    }
}
