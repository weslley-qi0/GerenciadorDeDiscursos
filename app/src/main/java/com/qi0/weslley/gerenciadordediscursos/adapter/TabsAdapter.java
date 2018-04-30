package com.qi0.weslley.gerenciadordediscursos.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.fragments.MessesFragment;

public class TabsAdapter extends FragmentStatePagerAdapter {

    private Context context;

    public TabsAdapter (Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle args = new Bundle();
        Fragment f = null;

        switch (position){
            case 0:
                f = new MessesFragment();
                args.putInt("mes", position);
                break;
            case 1:
                f = new MessesFragment();
                args.putInt("mes", position);
                break;
            case 2:
                f = new MessesFragment();
                args.putInt("mes", position);
                break;
            case 3:
                f = new MessesFragment();
                args.putInt("mes", position);
                break;
            case 4:
                f = new MessesFragment();
                args.putInt("mes", position);
                break;
            case 5:
                f = new MessesFragment();
                args.putInt("mes", position);
                break;
            case 6:
                f = new MessesFragment();
                args.putInt("mes", position);
                break;
            case 7:
                f = new MessesFragment();
                args.putInt("mes", position);
                break;
            case 8:
                f = new MessesFragment();
                args.putInt("mes", position);
                break;
            case 9:
                f = new MessesFragment();
                args.putInt("mes", position);
                break;
            case 10:
                f = new MessesFragment();
                args.putInt("mes", position);
                break;
            case 11:
                f = new MessesFragment();
                args.putInt("mes", position);
                break;

        }

        f.setArguments(args);

        return f;
    }

    @Override
    public int getCount() {
        return 12;
    }


    @Override
    public CharSequence getPageTitle(int position) {

        switch (position){
            case 0:
                return context.getString(R.string.txt_mes_jan);
            case 1:
                return context.getString(R.string.txt_mes_fev);
            case 2:
                return context.getString(R.string.txt_mes_mar);
            case 3:
                return context.getString(R.string.txt_mes_abr);
            case 4:
                return context.getString(R.string.txt_mes_mai);
            case 5:
                return context.getString(R.string.txt_mes_jun);
            case 6:
                return context.getString(R.string.txt_mes_jul);
            case 7:
                return context.getString(R.string.txt_mes_ago);
            case 8:
                return context.getString(R.string.txt_mes_set);
            case 9:
                return context.getString(R.string.txt_mes_out);
            case 10:
                return context.getString(R.string.txt_mes_nov );
            case 11:
                return context.getString(R.string.txt_mes_dez);

        }

        return null;
    }
}
