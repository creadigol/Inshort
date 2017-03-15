package com.creadigol.verticalviewpager.transforms;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by ADMIN on 05-Jan-17.
 */

public class ParallaxPageTransformer implements ViewPager.PageTransformer {

//    public void transformPage(View view, float position) {
//
//        int pageWidth = view.getWidth();
//
//
//        if (position < -1) { // [-Infinity,-1)
//            // This page is way off-screen to the left.
//            view.setAlpha(1);
//
//        } else if (position <= 1) { // [-1,1]
//
//            view.setTranslationX(-position * (pageWidth / 2)); //Half the normal speed
//
//        } else { // (1,+Infinity]
//            // This page is way off-screen to the right.
//            view.setAlpha(1);
//        }
//
//
//    }

    public void transformPage(View view, float position) {
        if(position <= -1.0F || position >= 1.0F) {
            view.setTranslationX(view.getWidth() * position);
            view.setAlpha(0.0F);
        } else if( position == 0.0F ) {
            view.setTranslationX(view.getWidth() * position);
            view.setAlpha(1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            view.setTranslationX(view.getWidth() * -position);
            view.setAlpha(1.0F - Math.abs(position));
        }
    }
}
