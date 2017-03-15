package com.creadigol.inshort.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.creadigol.inshort.R;

/**
 * Created by ADMIN on 06-Jan-17.
 */

public class InstructionFragment extends Fragment {
    String url;

    public InstructionFragment(String url) {
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

        View rootView = inflater.inflate(R.layout.instructionpage1, container,
                false);

        return rootView;
    }
}