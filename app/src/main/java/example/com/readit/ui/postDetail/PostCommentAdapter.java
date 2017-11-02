package example.com.readit.ui.postDetail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.readit.R;
import example.com.readit.R2;
import example.com.readit.data.remote.models.PostComment;
import example.com.readit.utils.TimeUtils;

/**
 * Created by FamilyPC on 10/30/2017.
 */

@SuppressWarnings("ALL")
public class PostCommentAdapter extends RecyclerView.Adapter<PostCommentAdapter.PostCommentViewHolder> {
    private final ArrayList<PostComment> postComments;
    private final Context context;

    public PostCommentAdapter(ArrayList<PostComment> postComments, Context context) {
        this.postComments = postComments;
        this.context = context;
    }

    @Override
    public PostCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post_comment, parent, false);
        return new PostCommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PostCommentViewHolder holder, int position) {
        PostComment postComment = postComments.get(position);
        holder.textView.setText(postComment.getBody());
        holder.commentsMeta.setText(context.getString(R.string.placeholder_comments_meta,
                postComment.getAuthor(),
                postComment.getScore(),
                TimeUtils.getRelativeTime(postComment.getCreated() * 1000)));

    }

    @Override
    public int getItemCount() {
        return postComments.size();
    }

    public class PostCommentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.textView)
        TextView textView;

        @BindView(R2.id.comments_meta)
        TextView commentsMeta;

        public PostCommentViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
