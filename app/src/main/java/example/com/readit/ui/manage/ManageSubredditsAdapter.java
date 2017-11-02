package example.com.readit.ui.manage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.readit.R;
import example.com.readit.R2;
import example.com.readit.data.local.models.LocalSubreddit;

/**
 * Created by FamilyPC on 10/26/2017.
 */

@SuppressWarnings("ALL")
public class ManageSubredditsAdapter extends RecyclerView.Adapter<ManageSubredditsAdapter.ManageSubredditsViewHolder> {

    private final Context mContext;
    private ArrayList<LocalSubreddit> localSubreddits = new ArrayList<>();
    private final ManageSubredditsPresenter mSubredditsPresenter;

    public ManageSubredditsAdapter(Context mContext, ArrayList<LocalSubreddit> localSubreddits, ManageSubredditsPresenter subredditsPresenter) {
        this.mContext = mContext;
        this.localSubreddits = localSubreddits;
        mSubredditsPresenter = subredditsPresenter;
        Log.d("TAG", "ManageSubredditsAdapter");
    }

    @Override
    public ManageSubredditsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_manage_subreddits, parent, false);
        return new ManageSubredditsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ManageSubredditsViewHolder holder, int position) {
        final LocalSubreddit localSubreddit = localSubreddits.get(position);
        holder.subredditTitle.setText(localSubreddit.getDisplay_name());

        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubredditsPresenter.onRemoveItemClick(localSubreddit);
            }
        });
    }


    @Override
    public int getItemCount() {
        return localSubreddits.size();
    }

    public class ManageSubredditsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.subreddit_title)
        TextView subredditTitle;
        @BindView(R2.id.delete)
        ImageView deleteImageView;

        public ManageSubredditsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
