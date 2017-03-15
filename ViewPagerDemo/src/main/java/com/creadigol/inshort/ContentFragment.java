package com.creadigol.inshort;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.creadigol.inshort.DataBase.DatabaseHelper;
import com.creadigol.inshort.Model.NewsModel;
import com.creadigol.inshort.Utils.Constant;
import com.creadigol.inshort.Utils.MyApplication;
import com.creadigol.inshort.Utils.PreferenceSettings;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class ContentFragment extends Fragment implements View.OnClickListener {
    public NewsModel arrayListt;
    LinearLayout bottomCtionBar, actionBar;
    ImageView newsImage, imgShare, imgBookmark;
    public static ImageView btnSync, upload;
    CardView card_view;
    Boolean clickFlag = true;
    DatabaseHelper mDatabaseHelper;
    TextView tvTitle, tvDesc, tvLink;
    int newsId, bookmark;
    Boolean bookee = false;
    private AdView mAdMobAdView;

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
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        initToolbar(view);
        initRecyclerView(view);

        card_view.setOnClickListener(this);
        imgShare.setOnClickListener(this);
        imgBookmark.setOnClickListener(this);
        tvTitle.setOnClickListener(this);
        btnSync.setOnClickListener(this);
        upload.setOnClickListener(this);
        bottomCtionBar.setOnClickListener(this);

        return view;
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
                Handler h = null;
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
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Inshort");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, bit);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "hello");
                startActivity(Intent.createChooser(sharingIntent, "Share Inshort via"));

//                String text = "Look at my awesome picture";
//                Uri pictureUri = Uri.parse(String.valueOf(R.drawable.bookmarkedfill));
//                Intent shareIntent = new Intent();
//                shareIntent.setAction(Intent.ACTION_SEND);
//                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
//                shareIntent.putExtra(Intent.EXTRA_STREAM, pictureUri);
//                shareIntent.setType("image/jpeg");
//                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                startActivity(Intent.createChooser(shareIntent, "Share images..."));

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
                MyApplication.redirectTop = true;
                Intent intent = new Intent(getActivity(), MainActivity.class);
                getActivity().overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(intent);
                Toast.makeText(getActivity(), "Refreshed successfully", Toast.LENGTH_SHORT).show();
                getActivity().finish();
                break;
            case R.id.containtUpload:
                MyApplication.redirectTop = false;
                Intent upload = new Intent(getActivity(), MainActivity.class);
                getActivity().overridePendingTransition(0, 0);
                upload.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                getActivity().overridePendingTransition(0, 0);
                getActivity().startActivity(upload);
                getActivity().finish();
                break;
            case R.id.llBottom:
                Intent i = new Intent(getActivity(), WebviewActivity.class);
                i.putExtra("link", arrayListt.getLink());
                startActivity(i);
                break;
            case R.id.bottomCtionBar:
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
        tvDesc = (TextView) view.findViewById(R.id.tvDesc);
        tvLink = (TextView) view.findViewById(R.id.tvLink);
        btnSync = (ImageView) view.findViewById(R.id.btnSync);
        upload = (ImageView) view.findViewById(R.id.containtUpload);
        mDatabaseHelper = new DatabaseHelper(getActivity());

        mAdMobAdView = (AdView) view.findViewById(R.id.admob_adview);
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
        llBottom.setOnClickListener(this);
        tvTitle.setText(arrayListt.getTitle());
        tvDesc.setText(arrayListt.getDiscription());
        newsId = arrayListt.getId();
        bookmark = arrayListt.getIsBookmarked();
        Log.e("bookmark ", " " + bookmark);
        tvLink.setText(arrayListt.getLink());

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

        mAdMobAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
            }

            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdOpened() {
            }
        });
    }

    @Override
    public void onPause() {
        if (mAdMobAdView != null) {
            mAdMobAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdMobAdView != null) {
            mAdMobAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdMobAdView != null) {
            mAdMobAdView.destroy();
        }
        super.onDestroy();
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


}
