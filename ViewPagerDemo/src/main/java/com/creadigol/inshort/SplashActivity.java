package com.creadigol.inshort;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.creadigol.inshort.Utils.MyApplication;
import com.creadigol.inshort.Utils.PreferenceSettings;
import com.creadigol.verticalviewpager.VerticalViewPager;

/**
 * Created by ravi on 18-03-2017.
 */

public class SplashActivity extends AppCompatActivity {
    public static VerticalViewPager viewPager;
    PreferenceSettings mPreferenceSettings;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.beginingactivity);
        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        if (!mPreferenceSettings.getIsFirst()) {
            MyApplication.isNeeded = false;
            Intent upload = new Intent(SplashActivity.this, MainActivity.class);
            upload.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            overridePendingTransition(0, 0);
            startActivity(upload);
            finish();
        }
        viewPager = (VerticalViewPager) findViewById(R.id.vertical);
        viewPager.setAdapter(new AwesomePagerAdapter());
        viewPager.setCurrentItem(0);
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
                getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

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
//                        mPreferenceSettings.setIsFirst(false);
                        Intent upload = new Intent(SplashActivity.this, MainActivity.class);
                        upload.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivityForResult(upload, 0);
                        overridePendingTransition(R.anim.slide_down_page, R.anim.stable);
                        finish();
                    }
                });
                view = layout;
                break;
        }
        return view;
    }
}
