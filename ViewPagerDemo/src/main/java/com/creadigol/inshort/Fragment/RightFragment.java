package com.creadigol.inshort.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.creadigol.inshort.MainActivity;
import com.creadigol.inshort.R;
import com.creadigol.inshort.Utils.CommonUtils;
import com.creadigol.inshort.Utils.MyApplication;

/**
 * Created by ADMIN on 06-Jan-17.
 */

public class RightFragment extends Fragment {
    public static WebView webView;
    private ProgressBar progressBar;
    private SwipeRefreshLayout refreshLayout;
    private float m_downX;
    LinearLayout ll_noNetwork;
    View rootView;
    public static TextView toolbarName;
    ImageView imageView;

    public RightFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_two, container,
                false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar_right);
        imageView = (ImageView) toolbar.findViewById(R.id.leftarrow);
        toolbarName = (TextView) toolbar.findViewById(R.id.tv_toolbar_name);
        imageView.setVisibility(View.VISIBLE);

        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(false);
                        Fragment frg = RightFragment.this;
                        final FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.detach(frg);
                        ft.attach(frg);
                        ft.commit();
                    }
                }, 1000);
            }
        });
        webView = (WebView) rootView.findViewById(R.id.webView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        ll_noNetwork = (LinearLayout) rootView.findViewById(R.id.ll_noNetwork);
        if (CommonUtils.isNetworkAvailable()) {
            initWebView();
            if (ViewpagerFragment.newsModelsArrayList != null && ViewpagerFragment.newsModelsArrayList.size() > 0 && ViewpagerFragment.newsModelsArrayList.size() > MyApplication.pageChnage) {
                webView.loadUrl(ViewpagerFragment.newsModelsArrayList.get(MyApplication.pageChnage).getLink());
                toolbarName.setText(CommonUtils.base64Decoding(ViewpagerFragment.newsModelsArrayList.get(MyApplication.pageChnage).getTitle()));
            }
        } else {
            ll_noNetwork.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            TextView tvBack = (TextView) rootView.findViewById(R.id.tvBack);
            tvBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MyApplication.isNeeded = false;
                    Intent upload = new Intent(getActivity(), MainActivity.class);
                    upload.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    getActivity().startActivity(upload);
                    getActivity().finish();
                }
            });
        }
        return rootView;
    }

    private void initWebView() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub
                Log.e("s", "ss");
                super.onReceivedError(view, errorCode, description, failingUrl);
            }
        });

        webView.clearCache(true);
        webView.clearHistory();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        m_downX = event.getX();
                    }
                    break;
                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }

                return false;
            }
        });

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
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mViewPager.setCurrentItem(1,true);

            }
        });
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    // handle back button's click listener
                    MainActivity.mViewPager.setCurrentItem(1,true);
                    return true;
                }
                return false;
            }
        });
    }
}