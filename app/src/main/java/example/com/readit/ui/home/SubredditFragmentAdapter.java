package example.com.readit.ui.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.readit.R;
import example.com.readit.R2;
import example.com.readit.data.remote.models.SubredditPost;
import example.com.readit.utils.AppUtils;
import example.com.readit.utils.TimeUtils;

/**
 * Created by FamilyPC on 10/25/2017.
 */

@SuppressWarnings("ALL")
public class SubredditFragmentAdapter extends RecyclerView.Adapter<SubredditFragmentAdapter.SubredditFragmentViewHolder> {

    private final Context mContext;
    private final ArrayList<SubredditPost> mSubredditPosts;
    private final SubredditFragmentContract.SubredditFragmentPresenter subredditFragmentPresenter;

    public SubredditFragmentAdapter(Context context, ArrayList<SubredditPost> subredditPosts, SubredditFragmentContract.SubredditFragmentPresenter subredditPresenter) {
        this.mContext = context;
        this.mSubredditPosts = subredditPosts;
        subredditFragmentPresenter = subredditPresenter;
    }

    @Override
    public SubredditFragmentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_subreddit_post, parent, false);
        return new SubredditFragmentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubredditFragmentViewHolder holder, int position) {
        final SubredditPost subredditPost = mSubredditPosts.get(position);

        holder.postTitleTextView.setText(subredditPost.getTitle());

        String relativeDate = String.valueOf(
                TimeUtils.getRelativeTime(1000L * subredditPost.getCreated()));

        holder.postMetaDateUser.setText(mContext.getString(R.string.placeholder_date_author,
                relativeDate, subredditPost.getAuthor()));

        holder.postScore.setText(AppUtils.formatSubscriber(subredditPost.getScore(), 0));
        holder.postNumComment.setText(AppUtils.formatSubscriber(subredditPost.getNum_comments(), 0));

        if ((subredditPost.getThumbnail() != null && !TextUtils.isEmpty(subredditPost.getThumbnail())) ||
                (subredditPost.getUrl() != null && !TextUtils.isEmpty(subredditPost.getUrl()))) {

            if (subredditPost.getThumbnail() != null && !TextUtils.isEmpty(subredditPost.getThumbnail())) {

                holder.postBody.setVisibility(View.GONE);
                Glide.with(mContext)
                        .load(subredditPost.getThumbnail())
                        .placeholder(R.drawable.placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(holder.postThumbnail);
            } else {
                String url = subredditPost.getUrl();

                if (url.endsWith(".jpg") ||
                        url.endsWith("jpeg") ||
                        url.endsWith("png")) {

                    Log.d("IPSUM", "jpg " + url);

                    holder.postBody.setVisibility(View.GONE);
                    Glide.with(mContext)
                            .load(url)
                            .placeholder(R.drawable.placeholder)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .dontAnimate()
                            .into(holder.postThumbnail);
                } else {
                    if (subredditPost.getSelftext() != null && !TextUtils.isEmpty(subredditPost.getSelftext())) {
                        holder.postBody.setVisibility(View.VISIBLE);
                        holder.postThumbnail.setVisibility(View.GONE);
                        holder.postBody.setText(subredditPost.getSelftext());
                    } else {
                        holder.postBody.setVisibility(View.GONE);
                        holder.postThumbnail.setVisibility(View.GONE);
                    }
                }
            }

        } else {

            if (subredditPost.getSelftext() != null && !TextUtils.isEmpty(subredditPost.getSelftext())) {
                holder.postBody.setVisibility(View.VISIBLE);
                holder.postThumbnail.setVisibility(View.GONE);
                holder.postBody.setText(subredditPost.getSelftext());
            } else {
                holder.postBody.setVisibility(View.GONE);
                holder.postThumbnail.setVisibility(View.GONE);
            }
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subredditFragmentPresenter.onPostItemClick(subredditPost);
            }
        });

        holder.postOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subredditFragmentPresenter.onMoreOption(subredditPost);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSubredditPosts.size();
    }

    public class SubredditFragmentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.post_title)
        TextView postTitleTextView;
        @BindView(R2.id.post_thumbnail)
        ImageView postThumbnail;
        @BindView(R2.id.post_body)
        TextView postBody;
        @BindView(R2.id.post_meta_date_user)
        TextView postMetaDateUser;
        @BindView(R2.id.post_score)
        TextView postScore;
        @BindView(R2.id.post_num_comment)
        TextView postNumComment;
        @BindView(R2.id.post_option)
        ImageButton postOption;

        public SubredditFragmentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
