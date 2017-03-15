package com.creadigol.inshort.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.creadigol.inshort.R;
import com.creadigol.inshort.Utils.CommonUtils;
import com.creadigol.inshort.Utils.MyApplication;

/**
 * Created by ADMIN on 06-Jan-17.
 */

public class RightFragment extends Fragment {
    public static WebView webView;
    private ProgressBar progressBar;
    String url;
    private float m_downX;
    LinearLayout ll_noNetwork;

    public RightFragment(String url) {
        // Required empty public constructor
        this.url = url;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_two, container,
                false);
        webView = (WebView) rootView.findViewById(R.id.webView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        ll_noNetwork = (LinearLayout) rootView.findViewById(R.id.ll_noNetwork);
        if (CommonUtils.isNetworkAvailable()) {
            initWebView();
            if (ViewpagerFragment.newsModelsArrayList != null && ViewpagerFragment.newsModelsArrayList.size() > 0)
                webView.loadUrl(ViewpagerFragment.newsModelsArrayList.get(MyApplication.pageChnage).getLink());
        } else {
            ll_noNetwork.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            TextView tvBack = (TextView) rootView.findViewById(R.id.tvBack);
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
}