package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.activitys.AdicionarEditarActivity;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEditarOradorFragment extends Fragment {

    TextView tvRatingOrador;
    EditText edtNome, edtCongregacao, edtTelefone, edtEmail;
    Orador orador;


    public AddEditarOradorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_editar_orador, container, false);

        edtNome = view.findViewById(R.id.edt_nome_orador_fragmente_add_orador);
        edtCongregacao = view.findViewById(R.id.edt_congregacao_fragmente_add_orador);
        edtTelefone = view.findViewById(R.id.edt_telefone_fragmente_add_orador);
        edtEmail = view.findViewById(R.id.edt_emale_fragmente_add_orador);


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
                salvarOrador();
                Toast.makeText(getContext(), "Camera", Toast.LENGTH_SHORT).show();
            }
        });

        final View viewFabDetalhe = getActivity().findViewById(R.id.fab_add_editar);
        viewFabDetalhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                salvarOrador();
                Toast.makeText(getContext(), orador.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void salvarOrador() {
        orador = new Orador();

        orador.setNome(edtNome.getText().toString());
        orador.setCongregacaoTest(edtCongregacao.getText().toString());
        orador.setTelefone(edtTelefone.getText().toString());
        orador.setEmail(edtEmail.getText().toString());
    }

}
