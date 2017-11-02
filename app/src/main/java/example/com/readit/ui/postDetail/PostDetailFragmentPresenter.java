package example.com.readit.ui.postDetail;

import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import example.com.readit.data.remote.ApiClient;
import example.com.readit.data.remote.RedditApiService;
import example.com.readit.data.remote.models.PostComment;
import example.com.readit.data.remote.models.PostResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by FamilyPC on 10/30/2017.
 */

@SuppressWarnings("ALL")
public class PostDetailFragmentPresenter implements PostDetailFragmentContract.FragmentPresenter {

    private static final String LOG_TAG = PostDetailFragmentPresenter.class.getSimpleName();
    private final PostDetailFragmentContract.PostDetailFragmentView postDetailFragmentView;

    public PostDetailFragmentPresenter(PostDetailFragmentContract.PostDetailFragmentView postDetailFragmentView) {
        this.postDetailFragmentView = postDetailFragmentView;
    }

    @Override
    public void LoadData(String permalink) {
        postDetailFragmentView.showLoading();

        RedditApiService redditApiService = ApiClient.getClient().create(RedditApiService.class);
        Call<String> detailResponseCall = redditApiService.getSubredditPostDetail(permalink);
        detailResponseCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d(LOG_TAG, call.request().url().toString());
//                Log.d(LOG_TAG, response.body());
                String body = response.body();
                ArrayList<PostComment> postComments = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(body);
                    JSONObject jsonObject = jsonArray.getJSONObject(1);

                    String json = jsonObject.toString();
//                    Log.d(LOG_TAG, json);

                    PostResponse postResponse = new Gson().fromJson(json, PostResponse.class);
                    PostResponse.PostResponseData postResponseData = postResponse.getData();
                    List<PostResponse.PostResponseData.PostResponseResult> postResponseResults = postResponseData.getChildren();


                    for (int i = 0; i < postResponseResults.size(); i++) {
                        PostComment postComment = postResponseResults.get(i).getData();
                        postComments.add(postComment);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                postDetailFragmentView.onDataLoaded(postComments);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(LOG_TAG, "error" + call.request().url().toString(), t);
                postDetailFragmentView.onDataLoadError(call.request().url().toString());
            }
        });
    }

    @Override
    public void onShareClick() {
        postDetailFragmentView.onShareClicked();
    }
}
