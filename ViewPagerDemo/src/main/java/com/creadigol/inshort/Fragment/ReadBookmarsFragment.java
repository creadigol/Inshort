package com.creadigol.inshort.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
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

public class ReadBookmarsFragment extends Fragment {
    LinearLayout linearLayout, ll_Readbookmark, llBookmark;
    Boolean clickFlag = true;
    DatabaseHelper databaseHelper;
    View rootView;

    public ReadBookmarsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.extra_fragment, container,
                false);

        databaseHelper = new DatabaseHelper(getActivity());

        linearLayout = (LinearLayout) rootView.findViewById(R.id.ll_actionbar);
        ll_Readbookmark = (LinearLayout) rootView.findViewById(R.id.ll_Readbookmark);
        llBookmark = (LinearLayout) rootView.findViewById(R.id.llnobookmark);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.extraToolbar);
        ImageView upload = (ImageView) rootView.findViewById(R.id.upload);
        ImageView menubookmark = (ImageView) rootView.findViewById(R.id.menubookmark);
        toolbar.setTitle("Bookmark");
        if (databaseHelper.getBookmarkData().size() == 0) {
            llBookmark.setVisibility(View.VISIBLE);
            ll_Readbookmark.setVisibility(View.GONE);
        } else {
            llBookmark.setVisibility(View.GONE);
            ll_Readbookmark.setVisibility(View.VISIBLE);
        }
        ll_Readbookmark.setOnClickListener(new View.OnClickListener() {
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

        menubookmark.setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onResume() {
        super.onResume();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {

                }
                return false;
            }
        });

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });

    }
}