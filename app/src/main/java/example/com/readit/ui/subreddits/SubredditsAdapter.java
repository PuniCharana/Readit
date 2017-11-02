package example.com.readit.ui.subreddits;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.readit.R;
import example.com.readit.R2;
import example.com.readit.data.remote.models.Subreddit;
import example.com.readit.utils.AppUtils;

/**
 * Created by FamilyPC on 2017-10-24.
 */

@SuppressWarnings("ALL")
public class SubredditsAdapter extends RecyclerView.Adapter<SubredditsAdapter.SubredditsViewHolder> {

    private final Context mContext;
    private final ArrayList<Subreddit> subreddits;
    private final SubredditsListPresenter mSubredditsPresenter;

    public SubredditsAdapter(Context context, ArrayList<Subreddit> arrayList, SubredditsListPresenter presenter) {
        mContext = context;
        subreddits = arrayList;
        mSubredditsPresenter = presenter;
    }

    @Override
    public SubredditsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_subreddits, parent, false);
        return new SubredditsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SubredditsViewHolder holder, final int position) {
        final Subreddit subreddit = subreddits.get(position);

        holder.subredditTitle.setText(subreddit.getUrl());
        holder.subredditDescription.setText(subreddit.getPublic_description());
        holder.subredditSubscriber.setText(AppUtils.formatSubscriber(subreddit.getSubscribers(), 0) + " subscribers,");
        holder.subredditCreated.setText("community since " + AppUtils.formatYear(subreddit.getCreated()));

        holder.subredditSubscribeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubredditsPresenter.onSubscribeClick(mContext, subreddit, position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSubredditsPresenter.onViewSubreddit(subreddit);
            }
        });
    }

    @Override
    public int getItemCount() {
        return subreddits.size();
    }

    class SubredditsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R2.id.subreddit_title)
        TextView subredditTitle;
        @BindView(R2.id.subreddit_description)
        TextView subredditDescription;
        @BindView(R2.id.subreddit_subscriber)
        TextView subredditSubscriber;
        @BindView(R2.id.subreddit_created)
        TextView subredditCreated;
        @BindView(R2.id.subreddit_subscribe_btn)
        Button subredditSubscribeBtn;

        public SubredditsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
