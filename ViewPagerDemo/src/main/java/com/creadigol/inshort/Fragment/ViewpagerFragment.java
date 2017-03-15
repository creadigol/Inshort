package com.creadigol.inshort.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.creadigol.verticalviewpager.VerticalViewPager;
import com.creadigol.inshort.ContentFragment;
import com.creadigol.inshort.ContentFragmentAdapter;
import com.creadigol.inshort.DataBase.DatabaseHelper;
import com.creadigol.inshort.FullScreenAdsActivity;
import com.creadigol.inshort.MainActivity;
import com.creadigol.inshort.Model.NewsModel;
import com.creadigol.inshort.R;
import com.creadigol.inshort.Utils.MyApplication;
import com.creadigol.inshort.Utils.PreferenceSettings;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * Created by ADMIN on 06-Jan-17.
 */

public class ViewpagerFragment extends Fragment implements ViewPager.OnPageChangeListener {
    DatabaseHelper mDatabaseHelper;
    public static ArrayList<NewsModel> newsModelsArrayList;
    public static VerticalViewPager viewPager;
    PreferenceSettings mPreferenceSettings;
    private AdView mAdMobAdView;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_three, container,
                false);
        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        mDatabaseHelper = new DatabaseHelper(getActivity());
        if (mPreferenceSettings.getBookmark()) {
            Toast.makeText(getActivity(), "Bookmarked", Toast.LENGTH_LONG).show();
            newsModelsArrayList = mDatabaseHelper.getBookmarkData();
        } else {
            newsModelsArrayList = mDatabaseHelper.getAllNews();
        }
        getActivity().setTitle("");
        initViewPager(rootView);

        return rootView;
    }

    /*old*/
    private void initViewPager(View rootView) {
        viewPager = (VerticalViewPager) rootView.findViewById(R.id.vertical_viewpager);
        String title = "Content";
        if (mPreferenceSettings.getIsFirst()) {
            viewPager.setAdapter(new AwesomePagerAdapter());
            viewPager.setCurrentItem(0);
        } else if (!mPreferenceSettings.getIsFirst()) {
            viewPager.setAdapter(new ContentFragmentAdapter.Holder(getActivity().getSupportFragmentManager())
                    .set(newsModelsArrayList));
            //If you setting other scroll mode, the scrolled fade is shown from either side of display.
            viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
            viewPager.addOnPageChangeListener(this);

            mAdMobAdView = (AdView) rootView.findViewById(R.id.adview);
            mAdMobAdView.setVisibility(View.VISIBLE);
            AdRequest adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("9CB6B5D5AFF1A744DF27540C55E11266")// Add your real device id here
                    .build();

            mAdMobAdView.loadAd(adRequest);

            mAdMobAdView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), FullScreenAdsActivity.class));
                }
            });

        }

    }

    private class AwesomePagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(View collection, int position, Object view) {
            ((ViewPager) collection).removeView((View) view);
        }

        @Override
        public void finishUpdate(View arg0) {
            //setPageTitles(getPageNumber());
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public Object instantiateItem(View collection, int position) {
            /*  TextView tv = new TextView(MyPagerActivity.this);
        tv.setText("Bonjour PAUG " + position);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(30);

        ((ViewPager) collection).addView(tv,0);

        return tv;*/

            View view = getViewToShow(position);
            ((ViewPager) collection).addView(view, 0);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((View) object);
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public Parcelable saveState() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void startUpdate(View arg0) {
            // TODO Auto-generated method stub

        }

    }

    private View getViewToShow(int position) {
        View view = null;
        View layout;
        LayoutInflater mInflater = (LayoutInflater)
                getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        switch (position) {

            case 0:
                layout = mInflater.inflate(R.layout.instructionpage1, null);
                view = layout;
                break;
            case 1:
                view = layout = mInflater.inflate(R.layout.instructionpage2, null);
                TextView gotit = (TextView) view.findViewById(R.id.gotid);

                gotit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPreferenceSettings.setIsFirst(false);
                        Intent upload = new Intent(getActivity(), MainActivity.class);
                        getActivity().overridePendingTransition(0, 0);
                        upload.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().overridePendingTransition(0, 0);
                        getActivity().startActivity(upload);
                        getActivity().finish();
                    }
                });
                view = layout;
                break;
        }
        return view;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        MyApplication.pageChnage = position;
        if (position == 0) {
            ContentFragment.btnSync.setVisibility(View.VISIBLE);
            ContentFragment.upload.setVisibility(View.GONE);
        } else if (position > 0) {
            ContentFragment.btnSync.setVisibility(View.GONE);
            ContentFragment.upload.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }
}