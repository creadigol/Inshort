package com.creadigol.inshort;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creadigol.inshort.DataBase.DatabaseHelper;
import com.creadigol.inshort.Fragment.LeftFragment;
import com.creadigol.inshort.Fragment.ViewpagerFragment;
import com.creadigol.inshort.Fragment.RightFragment;
import com.creadigol.inshort.Model.NewsModel;
import com.creadigol.inshort.Utils.AppUrl;
import com.creadigol.inshort.Utils.CommonUtils;
import com.creadigol.inshort.Utils.Constant;
import com.creadigol.inshort.Utils.MyApplication;
import com.creadigol.inshort.Utils.PreferenceSettings;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public static ViewPager mViewPager;
    DatabaseHelper dbHelper;
    PreferenceSettings mPreferenceSettings;
    ProgressBar progressBar;
    String TAG = MainActivity.class.getSimpleName();
    public static String newsId;
    private int countTimeOut = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        dbHelper = new DatabaseHelper(MainActivity.this);
        MyApplication.pageChnage = 0;
        mPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        Date d = new Date();
        CharSequence currentDate = DateFormat.format("MMMM d, yyyy ", d.getTime());
        mPreferenceSettings.setCurrentDate(String.valueOf(currentDate));

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String number = telephonyManager.getDeviceId();
        String token = FirebaseInstanceId.getInstance().getToken();
        // Log and toast
        String msg = getString(R.string.msg_token_fmt, token);
        Log.d(TAG, msg);
        if (token != null && token.trim().length() > 0)
            sendRegistrationToServer(token, number);
/** set the adapter for ViewPager */
        if (CommonUtils.isNetworkAvailable() && MyApplication.isNeeded && mPreferenceSettings.getBookmark() == false) {
            Intent intent = getIntent();
            newsId = intent.getStringExtra("newsId");
            Log.e("newsId","d= "+intent.getStringExtra("newsId"));
            if (mPreferenceSettings.getIsFirst()) {
                progressBar.setVisibility(View.VISIBLE);
            } else {
                progressBar.setVisibility(View.GONE);
            }
            mPreferenceSettings.setIsFirst(false);
            getNewsa();
        } else {
            progressBar.setVisibility(View.GONE);
            mViewPager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager()));
            mViewPager.setCurrentItem(1);
        }
        Calendar calendar = Calendar.getInstance();
        MyApplication.getInstance().getPreferenceSettings().setLastSyncTime(calendar.getTimeInMillis());
        Log.e("SyncTime", "" + MyApplication.getInstance().getPreferenceSettings().getLastSyncTime());
    }

    /*INSERT VALUES IN DATABASE*/
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
                            if (!dbHelper.getAlreadyData(serverId))
                                insertInDatabase(serverId, title, description, image, link, createde);
                            progressBar.setVisibility(View.GONE);
                        }
                        MyApplication.syncTime = String.valueOf(MyApplication.getInstance().getPreferenceSettings().getLastSyncTime());
                        Log.e("time", "" + MyApplication.syncTime);

                        mViewPager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager()));
                        mViewPager.setCurrentItem(1);
                    } else if (status_code == 0) {
                        mViewPager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager()));
                        mViewPager.setCurrentItem(1);
                        Log.e("Error_in", "else");
                    }
                    progressBar.setVisibility(View.GONE);
                } catch (JSONException e) {
                    Log.e("Error_in", "catch");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("Error_in", "onErrorResponse");
                mViewPager.setAdapter(new SamplePagerAdapter(getSupportFragmentManager()));
                mViewPager.setCurrentItem(1);
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
        dbHelper.insertNews(newsModel);
    }

    public class SamplePagerAdapter extends FragmentPagerAdapter {

        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            if (position == 0) {
                return new LeftFragment();
            } else if (position == 1) {

                return new ViewpagerFragment();
            } else {
                return new RightFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 3;
        }
    }

    private void sendRegistrationToServer(final String token, final String number) {
        //You can implement this method to store the token on your server
        //Not required for current project
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, AppUrl.URL_SETGCM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    Log.e(TAG, "Response: " + response);
                    JSONObject jsonObject = new JSONObject(response);
                    String statusCode = jsonObject.optString("status_code");
                    if (statusCode.equalsIgnoreCase("1")) {
                    } else {
                    }
                } catch (JSONException e) {

                }
                //mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (countTimeOut >= 3) {
                    countTimeOut = 0;
                } else {
                    countTimeOut++;
                    sendRegistrationToServer(token, number);
                }
                //mProgressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("gcmtoken", token);
                params.put("imei", number);
                Log.e(TAG, "reqSetGcmId params: " + params.toString());
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(jsonObjectRequest, TAG);
    }

}
