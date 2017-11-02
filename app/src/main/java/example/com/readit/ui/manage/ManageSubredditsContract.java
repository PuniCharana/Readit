package example.com.readit.ui.manage;

import android.support.v4.app.LoaderManager;
import android.view.MenuItem;

import java.util.ArrayList;

import example.com.readit.data.local.models.LocalSubreddit;

/**
 * Created by FamilyPC on 10/26/2017.
 */

@SuppressWarnings("ALL")
class ManageSubredditsContract {

    interface ManageSubredditsView {
        void onDataLoaded(ArrayList<LocalSubreddit> localSubreddits);

        void onDataLoadError(String message);

        void onSettingsOptionClick();

        void onHelpOptionClick();

        void onDescendingOptionClick();

        void onAscendingOptionClick();

        void onAddToWidgetClicked();
    }

    interface Presenter {
        void loadData(LoaderManager loaderManager);

        boolean onOptionItemSelected(MenuItem item);

        void onRemoveItemClick(LocalSubreddit localSubreddit);
    }
}
