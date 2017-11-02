package example.com.readit.ui.postDetail;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import example.com.readit.R;
import example.com.readit.R2;
import example.com.readit.data.remote.models.PostComment;
import example.com.readit.data.remote.models.SubredditPost;
import example.com.readit.utils.AppUtils;
import example.com.readit.utils.InternetConnectivity;
import example.com.readit.utils.TimeUtils;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailFragment extends Fragment implements PostDetailFragmentContract.PostDetailFragmentView {

    public static final String SUBREDDIT_POST_KEY = "SUBREDDIT_POST_KEY";
    private static final String LOG_TAG = PostDetailFragment.class.getSimpleName();

    private View rootView;

    @BindView(R2.id.post_content)
    NestedScrollView postContent;
    @BindView(R2.id.post_title)
    TextView postTitle;
    @BindView(R2.id.post_meta_date_user)
    TextView postMetaDateUser;
    @BindView(R2.id.post_image)
    ImageView postImageView;
    @BindView(R2.id.post_body)
    TextView postBody;
    @BindView(R2.id.post_url)
    TextView postUrl;
    @BindView(R2.id.progress_bar)
    ProgressBar progressBar;
    @BindView(R2.id.post_comments_recycler_view)
    RecyclerView recyclerView;

    @BindView(R2.id.post_score)
    TextView postScore;
    @BindView(R2.id.post_num_comment)
    TextView postNumComment;

    @BindView(R2.id.post_vote_up)
    ImageButton postVoteUp;
    @BindView(R2.id.post_vote_down)
    ImageButton postVoteDown;

    @BindView(R2.id.share_button)
    ImageButton shareButton;

    private ArrayList<PostComment> postComments = new ArrayList<>();
    private PostCommentAdapter postCommentAdapter;

    private SubredditPost subredditPost;
    private PostDetailFragmentPresenter fragmentPresenter;

    public PostDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_post_detail, container, false);
        ButterKnife.bind(this, rootView);
        fragmentPresenter = new PostDetailFragmentPresenter(this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        if (getArguments().containsKey(SUBREDDIT_POST_KEY)) {
            subredditPost = getArguments().getParcelable(SUBREDDIT_POST_KEY);
            Log.d(LOG_TAG, subredditPost != null ? subredditPost.getPermalink() : null);
            updatePost(subredditPost);
        }

        if (savedInstanceState != null && savedInstanceState.containsKey("COMMENTS")) {
            postComments = savedInstanceState.getParcelableArrayList("COMMENTS");
            Log.d(LOG_TAG, "savedInstanceState" + (postComments != null ? postComments.size() : 0));
        } else {

            if (InternetConnectivity.isConnected(getContext())) {
                fragmentPresenter.LoadData(subredditPost.getPermalink());
            } else {
                showSnackBar(getString(R.string.error_no_internet_connection));
            }

        }

        Log.d(LOG_TAG, "whay" + postComments.size());
        postCommentAdapter = new PostCommentAdapter(postComments, getContext());
        recyclerView.setAdapter(postCommentAdapter);
        recyclerView.setNestedScrollingEnabled(false);

        return rootView;
    }

    private void showSnackBar(String message) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    private void updatePost(SubredditPost subredditPost) {
        postTitle.setText(subredditPost.getTitle());

        String relativeDate = String.valueOf(
                TimeUtils.getRelativeTime(1000L * subredditPost.getCreated()));
        postMetaDateUser.setText(getString(R.string.placeholder_date_author,
                relativeDate, subredditPost.getAuthor()));
        postScore.setText(AppUtils.formatSubscriber(subredditPost.getScore(), 0));
        postNumComment.setText(AppUtils.formatSubscriber(subredditPost.getNum_comments(), 0));

        Log.d(LOG_TAG, "domain "+subredditPost.getDomain());


        if (subredditPost.getUrl() != null && !TextUtils.isEmpty(subredditPost.getUrl())) {
            String url = subredditPost.getUrl();
            Log.d("IPSUM", "found url " + url);

            if (url.endsWith(".gif") || url.endsWith(".gifv")) {
                Log.d("IPSUM", "GIF " + url);

                String newUrl = url;

                if (url.endsWith(".gifv")) {
                    newUrl = url.replaceFirst(".$", "");
                }

                GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(postImageView);
                Glide.with(this)
                        .load(newUrl)
                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(imageViewTarget);


            } else {
                if (url.endsWith(".jpg") ||
                        url.endsWith("jpeg") ||
                        url.endsWith("png")) {

                    Log.d("IPSUM", "jpg " + url);

                    postBody.setVisibility(View.GONE);
                    Glide.with(getContext())
                            .load(url)
                            .placeholder(R.drawable.placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .into(postImageView);
                } else {

                    if (subredditPost.getThumbnail() != null && !TextUtils.isEmpty(subredditPost.getThumbnail())) {
                        Log.d("IPSUM", "thumbnail" + subredditPost.getThumbnail());
                        postBody.setVisibility(View.GONE);
                        Glide.with(getContext())
                                .load(subredditPost.getThumbnail())
                                .placeholder(R.drawable.placeholder)
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .dontAnimate()
                                .into(postImageView);
                    } else {
                        Log.d("IPSUM", "NO Image foud");
                        if (subredditPost.getSelftext() != null && !TextUtils.isEmpty(subredditPost.getSelftext())) {
                            postBody.setVisibility(View.VISIBLE);
                            postImageView.setVisibility(View.GONE);
                            postBody.setText(subredditPost.getSelftext());
                        } else {
                            postBody.setVisibility(View.GONE);
                            postImageView.setVisibility(View.GONE);
                        }
                    }

                }
            }
        } else if (subredditPost.getThumbnail() != null && !TextUtils.isEmpty(subredditPost.getThumbnail())) {
            Log.d("IPSUM", "found thumbnail" + subredditPost.getThumbnail());
            postBody.setVisibility(View.GONE);
            Glide.with(getContext())
                    .load(subredditPost.getThumbnail())
                    .placeholder(R.drawable.placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .into(postImageView);
        } else {
            Log.d("IPSUM", "NO Image foud");
            if (subredditPost.getSelftext() != null && !TextUtils.isEmpty(subredditPost.getSelftext())) {
                postBody.setVisibility(View.VISIBLE);
                postImageView.setVisibility(View.GONE);
                postBody.setText(subredditPost.getSelftext());
            } else {
                postBody.setVisibility(View.GONE);
                postImageView.setVisibility(View.GONE);
            }
        }

        if (subredditPost.getUrl() != null) {
            postUrl.setText(subredditPost.getUrl());
        } else {
            postUrl.setVisibility(View.GONE);
        }

    }

    @Override
    public void onDataLoaded(ArrayList<PostComment> postCommentArrayList) {
        progressBar.setVisibility(View.GONE);

        if (postCommentArrayList.size() > 0) {
            Snackbar.make(rootView, "Comments loaded", Snackbar.LENGTH_LONG).show();

            postComments.addAll(postCommentArrayList);
            postCommentAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getContext(), "No comment found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDataLoadError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShareClicked() {
        startActivity(Intent.createChooser(ShareCompat.IntentBuilder.from(getActivity())
                .setType("text/plain")
                .setText(subredditPost.getUrl())
                .getIntent(), getString(R.string.action_share)));
    }

    @OnClick(R2.id.share_button)
    public void onShareClick(View view){
        fragmentPresenter.onShareClick();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("COMMENTS", postComments);
    }
}
