package example.com.readit.ui.postDetail;

import android.view.MenuItem;

/**
 * Created by FamilyPC on 10/30/2017.
 */

@SuppressWarnings("ALL")
public class PostDetailPresenter implements PostDetailContract.Presenter {

    private final PostDetailContract.PostDetailView postDetailView;

    public PostDetailPresenter(PostDetailContract.PostDetailView postDetailView) {
        this.postDetailView = postDetailView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            postDetailView.onBackPresses();
            return true;
        }

        return false;
    }
}
