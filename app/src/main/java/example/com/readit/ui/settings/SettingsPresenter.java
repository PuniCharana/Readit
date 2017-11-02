package example.com.readit.ui.settings;

import android.view.MenuItem;

/**
 * Created by FamilyPC on 10/26/2017.
 */

@SuppressWarnings("ALL")
public class SettingsPresenter implements SettingsContract.SettingsPresenter {
    private final SettingsContract.SettingsView settingsView;

    public SettingsPresenter(SettingsContract.SettingsView settingsView) {
        this.settingsView = settingsView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            settingsView.onBackButtonClick();
            return true;
        }
        return false;
    }
}
