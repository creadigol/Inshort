package com.creadigol.inshort;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creadigol.inshort.DataBase.DatabaseHelper;
import com.creadigol.inshort.Model.NewsModel;
import com.creadigol.inshort.Utils.AppUrl;
import com.creadigol.inshort.Utils.CommonUtils;
import com.creadigol.inshort.Utils.Constant;
import com.creadigol.inshort.Utils.MyApplication;
import com.creadigol.inshort.Utils.PreferenceSettings;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ContentFragment extends Fragment implements View.OnClickListener {
    public NewsModel arrayListt;
    LinearLayout bottomCtionBar, actionBar;
    ImageView newsImage, imgShare, imgBookmark, btnmune;
    public static ImageView btnSync, upload;
    CardView card_view;
    ProgressBar pb_search;
    Boolean clickFlag = true;
    DatabaseHelper mDatabaseHelper;
    TextView tvTitle, tvDesc, tvLink, know_more;
    int newsId, bookmark;
    Boolean bookee = false;
    View rootView;
    PreferenceSettings mpPreferenceSettings;

    public ContentFragment() {
        // Required empty public constructor
    }

    public ContentFragment(NewsModel arrayList) {
        arrayListt = arrayList;
    }

    public static Fragment newInstance(String title, NewsModel arrayList, int position, String test) {
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("position", position);
        ContentFragment fragment = new ContentFragment(arrayList);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_content, container, false);
        mpPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        initToolbar(rootView);
        initRecyclerView(rootView);

        card_view.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgBookmark.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
        btnSync.setOnClickListener(this);
        upload.setOnClickListener(this);
        know_more.setOnClickListener(this);
        btnmune.setOnClickListener(this);
        bottomCtionBar.setOnClickListener(this);

        return rootView;
    }

    public void setProfileImage(String userImageUrl) {

        com.nostra13.universalimageloader.core.ImageLoader imageLoader = MyApplication.getInstance().getImageLoader();
        DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).resetViewBeforeLoading(true)
                .showImageForEmptyUri(R.drawable.blankpic)
                .showImageOnFail(R.drawable.blankpic)
                .showImageOnLoading(R.drawable.blankpic).build();
        //download and display image from url
        imageLoader.displayImage(userImageUrl, newsImage, options);
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.main_card:
                if (clickFlag) {
                    SlidDown(v);
                    slidUpFooter(v);
                    clickFlag = false;
                    actionBar.setVisibility(View.VISIBLE);
                    bottomCtionBar.setVisibility(View.VISIBLE);
                } else {
                    slidUp(v);
                    SlidDownFooter(v);
                    actionBar.setVisibility(View.GONE);
                    bottomCtionBar.setVisibility(View.GONE);
                    clickFlag = true;
                }
                break;
            case R.id.imgShare:
                Bitmap bit = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.newsimage);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Sex knowledge-Android Apps on Google Play");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, bit);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=com.creadigol.inshort");
                startActivity(Intent.createChooser(sharingIntent, "Share Inshort via"));

                break;
            case R.id.imgBookmark:
                if (bookee == false) {
                    bookee = true;
                    imgBookmark.setImageResource(R.drawable.bookmarkedfill);
                    mDatabaseHelper.UpdateBookmark(newsId, Constant.BOOKMARK);
                    tvTitle.setTextColor(getResources().getColor(R.color.blue));
                    Toast.makeText(getActivity(), "News bookmarked", Toast.LENGTH_SHORT).show();

                } else {
                    bookee = false;
                    Toast.makeText(getActivity(), "Bookmark removed", Toast.LENGTH_SHORT).show();
                    imgBookmark.setImageResource(R.drawable.bookmark);
                    tvTitle.setTextColor(Color.BLACK);
                    mDatabaseHelper.UpdateBookmark(newsId, Constant.NOT_BOOKMARK);
                }
                break;
            case R.id.tvTitle:
                if (bookee == false) {
                    bookee = true;
                    imgBookmark.setImageResource(R.drawable.bookmarkedfill);
                    mDatabaseHelper.UpdateBookmark(newsId, Constant.BOOKMARK);
                    tvTitle.setTextColor(getResources().getColor(R.color.blue));
                    Toast.makeText(getActivity(), "News bookmarked", Toast.LENGTH_SHORT).show();

                } else {
                    bookee = false;
                    Toast.makeText(getActivity(), "Bookmark removed", Toast.LENGTH_SHORT).show();
                    imgBookmark.setImageResource(R.drawable.bookmark);
                    tvTitle.setTextColor(Color.BLACK);
                    mDatabaseHelper.UpdateBookmark(newsId, Constant.NOT_BOOKMARK);
                }
                break;
            case R.id.btnSync:
                btnSync.setVisibility(View.GONE);
                pb_search.setVisibility(View.VISIBLE);
                if (mpPreferenceSettings.getBookmark() == true) {
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
                break;
            case R.id.containtUpload:
                MyApplication.isNeeded = false;
                Intent upload = new Intent(getActivity(), MainActivity.class);
                upload.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivityForResult(upload, 0);
                getActivity().overridePendingTransition(R.anim.slide_down_page, R.anim.stable);
                getActivity().finish();
                break;
            case R.id.llBottom:
                MainActivity.mViewPager.setCurrentItem(getId()+1,true);
                break;
            case R.id.bottomCtionBar:
                break;
            case R.id.know_more:
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                View view = inflater.inflate(R.layout.containt_description, null);
                TextView full_desc = (TextView) view.findViewById(R.id.fulldesc);
                TextView ok = (TextView) view.findViewById(R.id.ok);
                full_desc.setText(CommonUtils.base64Decoding(arrayListt.getDiscription()));
                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                alertDialog.show();
                break;
            case R.id.btnmune:
                MainActivity.mViewPager.setCurrentItem(-1,true);
                break;
        }
    }

    private void initToolbar(View view) {
        PreferenceSettings mpPreferenceSettings = MyApplication.getInstance().getPreferenceSettings();
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.contenttoolbar);
        if (mpPreferenceSettings.getBookmark()) {
            toolbar.setTitle("Bookmark");
        } else {
            toolbar.setTitle("All News");
        }
    }

    private void initRecyclerView(View view) {
        newsImage = (ImageView) view.findViewById(R.id.newsImage);
        imgShare = (ImageView) view.findViewById(R.id.imgShare);
        imgBookmark = (ImageView) view.findViewById(R.id.imgBookmark);
        card_view = (CardView) view.findViewById(R.id.main_card);
        LinearLayout llBottom = (LinearLayout) view.findViewById(R.id.llBottom);
        actionBar = (LinearLayout) view.findViewById(R.id.actionBar);
        bottomCtionBar = (LinearLayout) view.findViewById(R.id.bottomCtionBar);
        tvTitle = (TextView) view.findViewById(R.id.tvTitle);
        pb_search = (ProgressBar) view.findViewById(R.id.progress_search);
        tvDesc = (TextView) view.findViewById(R.id.tvDesc);
        know_more = (TextView) view.findViewById(R.id.know_more);
        tvLink = (TextView) view.findViewById(R.id.tvLink);
        btnSync = (ImageView) view.findViewById(R.id.btnSync);
        upload = (ImageView) view.findViewById(R.id.containtUpload);
        btnmune = (ImageView) view.findViewById(R.id.btnmune);
        mDatabaseHelper = new DatabaseHelper(getActivity());
        btnSync.setVisibility(View.VISIBLE);
        llBottom.setOnClickListener(this);
        tvTitle.setText(CommonUtils.base64Decoding(arrayListt.getTitle()));
        tvDesc.setText(CommonUtils.base64Decoding(arrayListt.getDiscription()));
        newsId = arrayListt.getId();
        tvLink.setText(arrayListt.getLink());
        bookmark = mDatabaseHelper.getBookmakdata(arrayListt.getId());
        setProfileImage(arrayListt.getServer_image_path());

        if (bookmark == Constant.BOOKMARK) {
            bookee = true;
            imgBookmark.setImageResource(R.drawable.bookmarkedfill);
            tvTitle.setTextColor(getResources().getColor(R.color.blue));
        } else if (bookmark == Constant.NOT_BOOKMARK) {
            imgBookmark.setImageResource(R.drawable.bookmark);
            tvTitle.setTextColor(Color.BLACK);
            bookee = false;
        }

    }

    public String getTitle() {
        return getArguments().getString("title");
    }

    public int getPosition() {
        return getArguments().getInt("position");
    }

    public void clockwise(View view) {
        Animation animation = AnimationUtils.loadAnimation(getActivity(),
                R.anim.myanimation);
        bottomCtionBar.startAnimation(animation);
        actionBar.startAnimation(animation);
    }

    public void zoom(View view) {
        Animation animation1 = AnimationUtils.loadAnimation(getActivity(),
                R.anim.clockwise);
        bottomCtionBar.startAnimation(animation1);
        actionBar.startAnimation(animation1);
    }

    public void fade(View view) {
        Animation animation1 =
                AnimationUtils.loadAnimation(getActivity(),
                        R.anim.fade);
        bottomCtionBar.startAnimation(animation1);
        actionBar.startAnimation(animation1);
    }

    public void blink(View view) {
        Animation animation1 =
                AnimationUtils.loadAnimation(getActivity(),
                        R.anim.blink);
        bottomCtionBar.startAnimation(animation1);
        actionBar.startAnimation(animation1);
    }

    public void move(View view) {
        Animation animation1 =
                AnimationUtils.loadAnimation(getActivity(), R.anim.move);
        bottomCtionBar.startAnimation(animation1);
        actionBar.startAnimation(animation1);
    }

    public void slide(View view) {
        Animation animation1 =
                AnimationUtils.loadAnimation(getActivity(), R.anim.slide);
        bottomCtionBar.startAnimation(animation1);
        actionBar.startAnimation(animation1);
    }

    public void slidUp(View view) {
        try {
            Animation animation1 =
                    AnimationUtils.loadAnimation(getActivity(),
                            R.anim.slide_up);
            actionBar.startAnimation(animation1);
        } catch (Exception e) {
        }
    }

    public void SlidDown(View view) {
        Animation animation1 =
                AnimationUtils.loadAnimation(getActivity(),
                        R.anim.slide_down);
        actionBar.startAnimation(animation1);
    }

    public void slidUpFooter(View view) {
        Animation animation1 =
                AnimationUtils.loadAnimation(getActivity(),
                        R.anim.slide_up_footer);
        bottomCtionBar.startAnimation(animation1);
    }

    public void SlidDownFooter(View view) {
        try {
            Animation animation1 =
                    AnimationUtils.loadAnimation(getActivity(),
                            R.anim.slide_down_footer);
            bottomCtionBar.startAnimation(animation1);
        } catch (Exception e) {
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
                        MyApplication.isNeeded = false;
                        Intent upload = new Intent(getActivity(), MainActivity.class);
                        upload.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        getActivity().startActivity(upload);
                        Toast.makeText(getActivity(), "Referesh successfully", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    } else if (status_code == 0) {
                        btnSync.setVisibility(View.VISIBLE);
                        pb_search.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "No new updates check it later", Toast.LENGTH_SHORT).show();
                        Log.e("Error_in", "else");
                    }
                } catch (JSONException e) {
                    btnSync.setVisibility(View.VISIBLE);
                    pb_search.setVisibility(View.GONE);
                    Log.e("Error_in", "catch");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                btnSync.setVisibility(View.VISIBLE);
                pb_search.setVisibility(View.GONE);
                Toast.makeText(getActivity(), "Please check your network! and retry.", Toast.LENGTH_SHORT).show();
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
