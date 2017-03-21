package com.creadigol.inshort.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creadigol.inshort.Utils.AppUrl;
import com.creadigol.inshort.Utils.CommonUtils;
import com.creadigol.inshort.Utils.Constant;
import com.creadigol.verticalviewpager.VerticalViewPager;
import com.creadigol.inshort.ContentFragment;
import com.creadigol.inshort.ContentFragmentAdapter;
import com.creadigol.inshort.DataBase.DatabaseHelper;
import com.creadigol.inshort.MainActivity;
import com.creadigol.inshort.Model.NewsModel;
import com.creadigol.inshort.R;
import com.creadigol.inshort.Utils.MyApplication;
import com.creadigol.inshort.Utils.PreferenceSettings;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ADMIN on 06-Jan-17.
 */

public class ViewpagerFragment extends Fragment implements ViewPager.OnPageChangeListener {
    DatabaseHelper mDatabaseHelper;
    public static ArrayList<NewsModel> newsModelsArrayList;
    VerticalViewPager viewPager;
    PreferenceSettings mPreferenceSettings;
    private AdView mAdMobAdView;
    View rootView;
    ContentFragmentAdapter contentFragmentAdapter;
    AdRequest adRequest;
    int pos = 0, id;
    private SwipeRefreshLayout swipeRefreshLayout;
    InterstitialAd mInterstitialAd;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_three, container,
                false);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.pulToRefresh);
        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        mDatabaseHelper = new DatabaseHelper(getActivity());
        if (mPreferenceSettings.getBookmark() == true) {
            Toast.makeText(getActivity(), "Bookmarked", Toast.LENGTH_LONG).show();
            newsModelsArrayList = mDatabaseHelper.getBookmarkData();
        } else {
            newsModelsArrayList = mDatabaseHelper.getAllNews();
            if (MainActivity.newsId != null && !MainActivity.newsId.equalsIgnoreCase("")) {
//                id = mDatabaseHelper.getPostion(Integer.parseInt(MainActivity.newsId));
                for (int i = 0; i < newsModelsArrayList.size(); i++) {
                    if (newsModelsArrayList.get(i).getS_id() == Integer.parseInt(MainActivity.newsId)) {
                        id = i;
                        Log.e("postion", "" + i);
                        break;
                    }
                }
            }

        }
        getActivity().setTitle("");
        initViewPager(rootView);

        return rootView;
    }

    /*old*/
    private void initViewPager(View rootView) {
        viewPager = (VerticalViewPager) rootView.findViewById(R.id.vertical_viewpager);
        String title = "Content";
        if (!mPreferenceSettings.getIsFirst()) {
            contentFragmentAdapter = new ContentFragmentAdapter.Holder(getActivity().getSupportFragmentManager()).set(newsModelsArrayList);
            viewPager.setAdapter(contentFragmentAdapter);
            //If you setting other scroll mode, the scrolled fade is shown from either side of display.
            viewPager.setOverScrollMode(View.OVER_SCROLL_NEVER);
            viewPager.addOnPageChangeListener(this);
            if (id != 0) {
                viewPager.setCurrentItem(id, true);
            }
            mAdMobAdView = (AdView) rootView.findViewById(R.id.adview);
            if (CommonUtils.isNetworkAvailable()) {
                mAdMobAdView.setVisibility(View.VISIBLE);
            } else {
                mAdMobAdView.setVisibility(View.GONE);
            }

            adRequest = new AdRequest.Builder()
                    .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                    .addTestDevice("44444444444444444444444")// Add your real device id here
                    .build();
            mAdMobAdView.loadAd(adRequest);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mPreferenceSettings.getBookmark() == true) {
                            swipeRefreshLayout.setRefreshing(false);
                            swipeRefreshLayout.setEnabled(false);
                            MyApplication.isNeeded = false;
                            Toast.makeText(getActivity(), "Referesh successfully", Toast.LENGTH_SHORT).show();
                            Intent upload = new Intent(getActivity(), MainActivity.class);
                            getActivity().overridePendingTransition(0, 0);
                            upload.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            getActivity().overridePendingTransition(0, 0);
                            getActivity().startActivity(upload);
                            getActivity().finish();
                        } else {
                            getNewsa();
                        }
                    }
                }, 1000);

            }
        });

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        MyApplication.pageChnage = position;
        if (position == 0) {
            swipeRefreshLayout.setEnabled(true);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mPreferenceSettings.getBookmark() == true) {
                                swipeRefreshLayout.setRefreshing(false);
                                swipeRefreshLayout.setEnabled(false);
                                MyApplication.isNeeded = false;
                                Toast.makeText(getActivity(), "Referesh successfully", Toast.LENGTH_SHORT).show();
                                Intent upload = new Intent(getActivity(), MainActivity.class);
                                getActivity().overridePendingTransition(0, 0);
                                upload.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                getActivity().overridePendingTransition(0, 0);
                                getActivity().startActivity(upload);
                                getActivity().finish();
                            } else {
                                getNewsa();
                            }
                        }
                    }, 1000);

                }
            });
            ContentFragment.btnSync.setVisibility(View.VISIBLE);
            ContentFragment.upload.setVisibility(View.GONE);
        } else if (position > 0) {
            if (id == 0) {
                swipeRefreshLayout.setEnabled(false);
                ContentFragment.btnSync.setVisibility(View.GONE);
                ContentFragment.upload.setVisibility(View.VISIBLE);
            }
        }
        pos++;
        if (pos > 4) {
            pos = 0;
            mInterstitialAd = new InterstitialAd(getActivity());
            // set the ad unit ID
            mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen_ad));
            // Load ads into Interstitial Ads
            mInterstitialAd.loadAd(adRequest);

            mInterstitialAd.setAdListener(new AdListener() {
                public void onAdLoaded() {
                    showInterstitial();
                }
            });
        }
        if (newsModelsArrayList != null && newsModelsArrayList.size() > 0 && newsModelsArrayList.size() > position) {
            if (id == 0) {
                RightFragment.webView.loadUrl(newsModelsArrayList.get(position).getLink());
                RightFragment.toolbarName.setText(CommonUtils.base64Decoding(newsModelsArrayList.get(position).getTitle()));
            }else id=0;
        }


//        if (pos > 3) {
//            if (!mPreferenceSettings.getPreviesDate().contains(mPreferenceSettings.getCurrentDate())) {
//                mPreferenceSettings.setPreviesDate(mPreferenceSettings.getCurrentDate());
//                count++;
//                pos = 0;
//                mInterstitialAd = new InterstitialAd(getActivity());
//                // set the ad unit ID
//                mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen_ad));
//                // Load ads into Interstitial Ads
//                mInterstitialAd.loadAd(adRequest);
//
//                mInterstitialAd.setAdListener(new AdListener() {
//                    public void onAdLoaded() {
//                        showInterstitial();
//                    }
//                });
//            }else if(mPreferenceSettings.getCurrentDate().equalsIgnoreCase(mPreferenceSettings.getPreviesDate())&&count<3){
//                count++;
//                pos = 0;
//                mInterstitialAd = new InterstitialAd(getActivity());
//                // set the ad unit ID
//                mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen_ad));
//                // Load ads into Interstitial Ads
//                mInterstitialAd.loadAd(adRequest);
//
//                mInterstitialAd.setAdListener(new AdListener() {
//                    public void onAdLoaded() {
//                        showInterstitial();
//                    }
//                });
//            }
//        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }


    public void getNewsa() {
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_GETDATA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject main = new JSONObject(response);
                    Log.e("Response", main + "");
                    int status_code = main.getInt("status_code");

                    if (status_code == 1) {
                        JSONArray data1 = main.getJSONArray("knowledge");
                        for (int i = 0; i < data1.length(); i++) {
                            JSONObject object = data1.getJSONObject(i);
                            String serverId = object.optString("id");
                            String title = object.optString("title");
                            String description = object.optString("description");
                            String image = object.optString("image");
                            String link = object.optString("link");
                            String createde = object.optString("created");
                            MyApplication.syncTime = String.valueOf(MyApplication.getInstance().getPreferenceSettings().getLastSyncTime());
                            if (!mDatabaseHelper.getAlreadyData(serverId))
                                insertInDatabase(serverId, title, description, image, link, createde);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                        MyApplication.isNeeded = false;
                        Intent upload = new Intent(getActivity(), MainActivity.class);
                        upload.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().startActivity(upload);
                        Toast.makeText(getActivity(), "Referesh successfully", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    } else if (status_code == 0) {
                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "No new updates check it later", Toast.LENGTH_SHORT).show();
                        Log.e("Error_in", "else");
                    }
                } catch (JSONException e) {
                    Log.e("Error_in", "catch");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(getActivity(), "Your network is slow please check and try again", Toast.LENGTH_SHORT).show();
                Log.e("Error_in", "onErrorResponse");
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("syntime", MyApplication.syncTime);
                Log.e("para", ": " + params.toString());
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest, "main");
    }

    public void insertInDatabase(String s_id, String title, String desc, String image, String link, String createdDate) {
        NewsModel newsModel = new NewsModel();
        newsModel.setS_id(Integer.parseInt(s_id));
        newsModel.setTitle(title);
        newsModel.setDiscription(desc);
        newsModel.setLink(link);
        newsModel.setCreatedTime(createdDate);
        newsModel.setIsRead(Constant.NOT_READ);
        newsModel.setIsBookmarked(Constant.NOT_BOOKMARK);
        newsModel.setLike(Constant.NOT_LIKE);
        newsModel.setServer_image_path(image);
        mDatabaseHelper.insertNews(newsModel);
    }
}