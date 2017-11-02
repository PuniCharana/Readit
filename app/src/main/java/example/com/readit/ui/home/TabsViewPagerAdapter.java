package example.com.readit.ui.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FamilyPC on 10/19/2017.
 */

@SuppressWarnings("ALL")
class TabsViewPagerAdapter extends FragmentStatePagerAdapter {

    private final List<String> mFragmentTitleList = new ArrayList<>();

    public TabsViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return SubRedditFragment.newInstance(mFragmentTitleList.get(position));
    }

    @Override
    public int getCount() {
        return mFragmentTitleList.size();
    }

    public void addTitle(String title) {
        mFragmentTitleList.add(title);
    }

    public void reset() {
        mFragmentTitleList.clear();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
