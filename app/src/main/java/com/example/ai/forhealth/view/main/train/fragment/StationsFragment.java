package com.example.ai.forhealth.view.main.train.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ai.forhealth.R;

/**
 * Created by ai on 2016/12/14.
 */

public class StationsFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_train_f_stations,null);
        view.setTag(1);


        return view;
    }
}
