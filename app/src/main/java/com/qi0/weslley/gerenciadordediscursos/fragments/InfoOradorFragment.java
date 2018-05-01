package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.activitys.AdicionarEditarActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class InfoOradorFragment extends Fragment {


    public InfoOradorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_info_orador, container, false);

        return view;
    }

}
