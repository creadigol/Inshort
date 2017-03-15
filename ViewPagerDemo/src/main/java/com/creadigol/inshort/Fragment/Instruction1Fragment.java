package com.creadigol.inshort.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.creadigol.inshort.R;

/**
 * Created by ADMIN on 06-Jan-17.
 */

public class Instruction1Fragment extends Fragment {
    LinearLayout linearLayout, ll_noNetwork;
    Boolean clickFlag = true;
    Toolbar toolbar;
    ImageView upload;

    String url;

    public Instruction1Fragment(String url) {
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

        View rootView = inflater.inflate(R.layout.instructionpage2, container,
                false);

        return rootView;
    }

}