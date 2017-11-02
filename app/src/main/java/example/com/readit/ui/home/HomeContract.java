package example.com.readit.ui.home;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.view.MenuItem;

import java.util.ArrayList;

import example.com.readit.data.local.models.LocalSubreddit;

/**
 * Created by FamilyPC on 10/24/2017.
 */

@SuppressWarnings("ALL")
public interface HomeContract {

    interface HomeView {
        void showShareNavigationClicked();

        void onSettingsItemOptionItemClick();

        void showSubredditsListActivity();

        void onDataLoaded(ArrayList<LocalSubreddit> localSubreddits);

        void navItemClick(String id);

        void navRedditPicksClick();
    }

    interface Presenter {
        void onNavigationItemSelected(MenuItem item);

        void onOptionsItemSelected(MenuItem item);

        void loadData(LoaderManager loaderManager, Context context);

        void onDestroy();
    }
}
