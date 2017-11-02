package example.com.readit.ui.manage;

import android.view.MenuItem;

import java.util.ArrayList;

import example.com.readit.data.local.models.LocalSubreddit;

/**
 * Created by FamilyPC on 10/26/2017.
 */

@SuppressWarnings("ALL")
class ManageSubredditsContract {

    interface ManageSubredditsView {

        void onDataLoaded(ArrayList<LocalSubreddit> subreddits);

        void onDataLoadError(String message);

        void onSettingsOptionClick();

        void onDescendingOptionClick();

        void onAscendingOptionClick();

        void onAddToWidgetClicked();

        void onItemRemoved(String message);
    }

    interface Presenter {
        void loadData();

        boolean onOptionItemSelected(MenuItem item);

        void onRemoveItemClick(LocalSubreddit localSubreddit);
    }
}
