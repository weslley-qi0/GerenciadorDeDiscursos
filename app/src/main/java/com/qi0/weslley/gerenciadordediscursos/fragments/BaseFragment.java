package com.qi0.weslley.gerenciadordediscursos.fragments;

import android.support.v4.app.Fragment;

public class BaseFragment extends Fragment {

    protected void replaceFragment(int  container, Fragment fragment) {
        getChildFragmentManager().beginTransaction().replace(container, fragment).commit();
    }
}
