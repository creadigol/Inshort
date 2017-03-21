package com.creadigol.inshort.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.creadigol.inshort.R;
import com.creadigol.inshort.Utils.MyApplication;

/**
 * Created by ADMIN on 06-Jan-17.
 */

public class NodataFragment extends Fragment {
    LinearLayout ll_noNetwork;

    public NodataFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("link", "" + MyApplication.link);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.nodatafragment, container,
                false);
        ll_noNetwork = (LinearLayout) rootView.findViewById(R.id.ll_nonetwork);
        TextView tvBack = (TextView) rootView.findViewById(R.id.tvfinish);
        tvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        return rootView;
    }
}