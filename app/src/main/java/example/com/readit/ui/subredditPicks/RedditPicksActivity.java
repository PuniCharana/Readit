package example.com.readit.ui.subredditPicks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.readit.R;
import example.com.readit.R2;
import example.com.readit.ui.subredditView.SubredditViewActivity;

public class RedditPicksActivity extends AppCompatActivity {

    @BindView(R2.id.list)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reddit_picks);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Reddit Picks");
        }

        final List<String> stringList = Arrays.asList(getResources().getStringArray(R.array.reddit_picks));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, stringList);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), SubredditViewActivity.class);
                String subreddit_to_load = stringList.get(position).replace("/r/", "");
                intent.putExtra(SubredditViewActivity.SUBREDDIT_TO_LOAD, subreddit_to_load);
                startActivity(intent);
            }
        });
    }
}
