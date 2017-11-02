package example.com.readit.ui.subreddits;

import android.util.Log;
import android.view.MenuItem;

import example.com.readit.R;

/**
 * Created by FamilyPC on 2017-10-24.
 */

@SuppressWarnings("ALL")
public class SubredditsPresenter implements SubredditsContract.Presenter {

    private static final String LOG_TAG = SubredditsPresenter.class.getSimpleName();
    private final SubredditsContract.SubredditsView mView;

    public SubredditsPresenter(SubredditsContract.SubredditsView view) {
        this.mView = view;
        Log.d(LOG_TAG, "SubredditsPresenter");
    }

    @Override
    public boolean onOptionItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                mView.onSettingsOptionCLick();
                return true;
        }
        return false;
    }


}
