package com.creadigol.inshort.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.creadigol.inshort.MainActivity;
import com.creadigol.inshort.R;
import com.creadigol.inshort.Utils.MyApplication;
import com.creadigol.inshort.Utils.PreferenceSettings;

/**
 * Created by ADMIN on 06-Jan-17.
 */

public class LeftFragment extends Fragment implements View.OnClickListener {
    ImageView ll_Allnews, ll_Bookmark;
    TextView tvBookmark, tvAllNews;
    PreferenceSettings mPreferenceSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container,
                false);
        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        ll_Allnews = (ImageView) rootView.findViewById(R.id.allnews);
        ll_Bookmark = (ImageView) rootView.findViewById(R.id.bookmark);
        tvBookmark = (TextView) rootView.findViewById(R.id.tvbookmark);
        tvAllNews = (TextView) rootView.findViewById(R.id.tvallnew);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_right);
        ImageView ivRight = (ImageView) toolbar.findViewById(R.id.ivRight);
        TextView tvToolbar = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        tvToolbar.setText("Setting");

        ivRight.setOnClickListener(this);
        ll_Allnews.setOnClickListener(this);
        ll_Bookmark.setOnClickListener(this);
        if (mPreferenceSettings.getBookmark()) {
            ll_Bookmark.setBackground(getResources().getDrawable(R.drawable.button_click));
            ll_Bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmarkwehite));
            tvBookmark.setTextColor(getResources().getColor(R.color.blue));
        } else {
            ll_Allnews.setBackground(getResources().getDrawable(R.drawable.button_click));
            ll_Allnews.setImageDrawable(getResources().getDrawable(R.drawable.newswhite));
            tvAllNews.setTextColor(getResources().getColor(R.color.blue));
        }
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivRight:
                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
                break;
            case R.id.allnews:
                ll_Allnews.setBackground(getResources().getDrawable(R.drawable.button_click));
                ll_Allnews.setImageDrawable(getResources().getDrawable(R.drawable.newswhite));
                tvAllNews.setTextColor(getResources().getColor(R.color.blue));
                Intent intent = new Intent(getActivity(), MainActivity.class);
                mPreferenceSettings.setIsNeeded(false);
                getActivity().startActivity(intent);
                getActivity().finish();
                break;
            case R.id.bookmark:
                ll_Bookmark.setBackground(getResources().getDrawable(R.drawable.button_click));
                ll_Bookmark.setImageDrawable(getResources().getDrawable(R.drawable.bookmarkwehite));
                tvBookmark.setTextColor(getResources().getColor(R.color.blue));
                Intent bookmark = new Intent(getActivity(), MainActivity.class);
                mPreferenceSettings.setBookmark(true);
                getActivity().startActivity(bookmark);
                getActivity().finish();
                break;
        }
    }
}