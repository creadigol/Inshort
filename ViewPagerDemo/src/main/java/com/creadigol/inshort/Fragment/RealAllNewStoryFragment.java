package com.creadigol.inshort.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.creadigol.inshort.DataBase.DatabaseHelper;
import com.creadigol.inshort.MainActivity;
import com.creadigol.inshort.R;
import com.creadigol.inshort.Utils.MyApplication;

/**
 * Created by ADMIN on 06-Jan-17.
 */

public class RealAllNewStoryFragment extends Fragment {
    LinearLayout linearLayout, ll_ReadNews;
    Boolean clickFlag = true;
    DatabaseHelper databaseHelper;

    public RealAllNewStoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.readall_fragment, container,
                false);

        databaseHelper = new DatabaseHelper(getActivity());

        linearLayout = (LinearLayout) rootView.findViewById(R.id.ll_newsactionbar);
        ll_ReadNews = (LinearLayout) rootView.findViewById(R.id.ll_realnews);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.newstoolbar);
        ImageView upload = (ImageView) rootView.findViewById(R.id.redirect);
        ImageView menu = (ImageView) rootView.findViewById(R.id.menu);
        toolbar.setTitle("All News");
        ll_ReadNews.setVisibility(View.VISIBLE);
        ll_ReadNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (clickFlag) {
                    SlidDown(v);
                    clickFlag = false;
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    slidUp(v);
                    linearLayout.setVisibility(View.GONE);
                    clickFlag = true;
                }
            }
        });
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mViewPager.setCurrentItem(-1, true);
            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.isNeeded = false;
                Intent upload = new Intent(getActivity(), MainActivity.class);
                upload.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(upload, 0);
                getActivity().overridePendingTransition(R.anim.slide_down_page, R.anim.stable);
                getActivity().finish();
            }
        });
        return rootView;
    }

    public void slidUp(View view) {
        try {


            Animation animation1 =
                    AnimationUtils.loadAnimation(getActivity(),
                            R.anim.slide_up);
            linearLayout.startAnimation(animation1);
        } catch (Exception e) {
        }
    }

    public void SlidDown(View view) {
        Animation animation1 =
                AnimationUtils.loadAnimation(getActivity(),
                        R.anim.slide_down);
        linearLayout.startAnimation(animation1);
    }
}