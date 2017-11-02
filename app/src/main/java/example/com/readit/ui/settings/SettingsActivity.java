package example.com.readit.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RadioGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import example.com.readit.R;
import example.com.readit.R2;

public class SettingsActivity extends AppCompatActivity implements SettingsContract.SettingsView {

    public static final String SELECTED_FILTER_OPTION = "SELECTED_FILTER_OPTION";
    public static final String FILTER_OPTION = "FILTER_OPTION";
    private SettingsPresenter settingsPresenter;

    @BindView(R2.id.filter_option)
    RadioGroup radioButtonGroup;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.title_settings);
        }

        settingsPresenter = new SettingsPresenter(this);

        sharedPreferences = getSharedPreferences(FILTER_OPTION, MODE_PRIVATE);
        int selected = sharedPreferences.getInt(SELECTED_FILTER_OPTION, 0);
        switch(selected){
            case 0:
                radioButtonGroup.check(R.id.filter_option_hot);
                break;
            case 1:
                radioButtonGroup.check(R.id.filter_option_top);
                break;
            case 2:
                radioButtonGroup.check(R.id.filter_option_new);
                break;
            case 3:
                radioButtonGroup.check(R.id.filter_option_rising);
                break;
            case 4:
                radioButtonGroup.check(R.id.filter_option_controversial);
                break;
        }

        radioButtonGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                SharedPreferences.Editor editor = sharedPreferences.edit();
                switch(checkedId){
                    case R.id.filter_option_hot:
                        editor.putInt(SELECTED_FILTER_OPTION, 0);
                        break;
                    case R.id.filter_option_top:
                        editor.putInt(SELECTED_FILTER_OPTION, 1);
                        break;
                    case R.id.filter_option_new:
                        editor.putInt(SELECTED_FILTER_OPTION, 2);
                        break;
                    case R.id.filter_option_rising:
                        editor.putInt(SELECTED_FILTER_OPTION, 3);
                        break;
                    case R.id.filter_option_controversial:
                        editor.putInt(SELECTED_FILTER_OPTION, 4);
                        break;
                }
                editor.apply();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return settingsPresenter.onOptionsItemSelected(item);
    }

    @Override
    public void onBackButtonClick() {
        finish();
    }
}
