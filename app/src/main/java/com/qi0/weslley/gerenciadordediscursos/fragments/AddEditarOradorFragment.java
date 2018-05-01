package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qi0.weslley.gerenciadordediscursos.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEditarOradorFragment extends Fragment {

    TextView tvRatingOrador;


    public AddEditarOradorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_editar_orador, container, false);

        RatingBar ratingBar = view.findViewById(R.id.ratindBar_orador);
        tvRatingOrador = view.findViewById(R.id.tv_rating_orador);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                tvRatingOrador.setText(String.valueOf(rating));

                switch ((int) ratingBar.getRating()) {
                    case 1:
                        tvRatingOrador.setText("Fraco");
                        break;
                    case 2:
                        tvRatingOrador.setText("Regular");
                        break;
                    case 3:
                        tvRatingOrador.setText("Padrão");
                        break;
                    case 4:
                        tvRatingOrador.setText("Bom");
                        break;
                    case 5:
                        tvRatingOrador.setText("Exelente");
                        break;
                    default:
                        tvRatingOrador.setText("");
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_camera_fragment_novo_orador);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Camera", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
