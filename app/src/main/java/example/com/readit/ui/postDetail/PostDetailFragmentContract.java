package example.com.readit.ui.postDetail;

import java.util.ArrayList;

import example.com.readit.data.remote.models.PostComment;

/**
 * Created by FamilyPC on 10/30/2017.
 */

@SuppressWarnings("ALL")
class PostDetailFragmentContract {

    interface PostDetailFragmentView {

        void onDataLoaded(ArrayList<PostComment> postComments);

        void showLoading();

        void onDataLoadError(String s);

        void onShareClicked();
    }

    interface FragmentPresenter {

        void LoadData(String string);

        void onShareClick();
    }
}
