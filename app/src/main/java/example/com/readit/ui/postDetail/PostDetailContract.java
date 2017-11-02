package example.com.readit.ui.postDetail;

import android.view.MenuItem;

/**
 * Created by FamilyPC on 10/30/2017.
 */

@SuppressWarnings("ALL")
class PostDetailContract {

    interface PostDetailView {

        void onBackPresses();
    }

    interface Presenter {

        boolean onOptionsItemSelected(MenuItem item);
    }
}
