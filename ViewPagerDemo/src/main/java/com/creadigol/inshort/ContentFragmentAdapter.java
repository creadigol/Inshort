package com.creadigol.inshort;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.creadigol.inshort.Fragment.ReadBookmarsFragment;
import com.creadigol.inshort.Fragment.NodataFragment;
import com.creadigol.inshort.Fragment.RealAllNewStoryFragment;
import com.creadigol.inshort.Model.NewsModel;
import com.creadigol.inshort.Utils.CommonUtils;
import com.creadigol.inshort.Utils.MyApplication;
import com.creadigol.inshort.Utils.PreferenceSettings;

import java.util.ArrayList;
import java.util.List;

public class ContentFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();

    public ContentFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public int getItemPosition(Object object) {

        return super.getItemPosition(object);

    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return ((ContentFragment) fragments.get(position)).getTitle();
    }


    public static class Holder {
        private final List<Fragment> fragments = new ArrayList<>();
        private FragmentManager manager;


        public Holder(FragmentManager manager) {
            this.manager = manager;
        }

        public Holder add(Fragment f) {
            fragments.add(f);
            return this;
        }

        public ContentFragmentAdapter set(ArrayList<NewsModel> newsModelArrayList) {
            PreferenceSettings mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
            if (newsModelArrayList.size() == 0 && !CommonUtils.isNetworkAvailable() && mPreferenceSettings.getBookmark() == false) {
                fragments.add(new NodataFragment());
            }
            for (int i = 0; i < newsModelArrayList.size(); i++) {
                fragments.add(ContentFragment.newInstance("test" + i, newsModelArrayList.get(i), i, "i"));
            }
            if (newsModelArrayList.size() > 0 && mPreferenceSettings.getBookmark() == false) {
                fragments.add(new RealAllNewStoryFragment());
            }
            if (mPreferenceSettings.getBookmark() == true)
                fragments.add(new ReadBookmarsFragment());
            return new ContentFragmentAdapter(manager, fragments);
        }
    }
}
