package com.qi0.weslley.gerenciadordediscursos.activitys;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.qi0.weslley.gerenciadordediscursos.Config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.fragments.AddEditarOradorFragment;
import com.qi0.weslley.gerenciadordediscursos.helper.Mask;
import com.qi0.weslley.gerenciadordediscursos.model.Congregacao;
import com.qi0.weslley.gerenciadordediscursos.model.Discurso;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

import es.dmoral.toasty.Toasty;

public class AdicionarEditarActivity extends BaseActivity {

    EditText edtNomeCongregacao;
    EditText edtCidadeCongregacao;
    EditText edtNumeroDiscurso;
    EditText edtTemaDiscurso;

    Orador oradorSelecionado;
    Congregacao congregacaoNova;
    Congregacao congregacaoelecionada;
    Discurso discursoNovo;
    Discurso discursoSelecionado;
    String userUID;

    String idDisurso;
    String numeroDiscurso;
    String temaDiscurso;

    String idCongregacao;
    String nomeCongregacao;
    String cidadeCongregacao;

    // Instancias Firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_editar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pricipal);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        firebaseAuth = ConfiguracaoFirebase.getAuth();
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();

        userUID = firebaseAuth.getCurrentUser().getUid();

        Intent intent = getIntent();
        oradorSelecionado = (Orador) intent.getSerializableExtra("oradorSelecionado");
        congregacaoelecionada = (Congregacao) intent.getSerializableExtra("congregacaoelecionada");
        discursoSelecionado = (Discurso) intent.getSerializableExtra("discursoSelecionado");

        String fragmentAbrir = (String) getIntent().getCharSequenceExtra("qualFragmentAbrir");

        switch (fragmentAbrir) {
            case "AddCongregacaoFragment":

                /*replaceFragment(R.id.fragment_container_add_editar, new Fragment());
                getSupportActionBar().setTitle("Nova Congregação");*/

                dialogoAddCongregacao();

                break;
            case "AddOradorFragment":

                Bundle bundle = new Bundle();
                bundle.putSerializable("oradorSelecionado", oradorSelecionado);
                AddEditarOradorFragment addEditarOradorFragment = new AddEditarOradorFragment();
                addEditarOradorFragment.setArguments(bundle);
                replaceFragment(R.id.fragment_container_add_editar, addEditarOradorFragment);
                getSupportActionBar().setTitle("Novo Orador");

                break;
            case "AddDiscursosFragment":

                /*replaceFragment(R.id.fragment_container_add_editar, new Fragment());
                getSupportActionBar().setTitle("Novo Discurso");*/

                dialogoAddDiscurso();

                break;
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_editar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //dialogoAddCongregacao2();
                Toast.makeText(AdicionarEditarActivity.this, "Salvando...", Toast.LENGTH_SHORT).show();
                //finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (int permissaoResultado : grantResults) {
            if (permissaoResultado == PackageManager.PERMISSION_DENIED) {
                alertaValidacaoPermissao();
            }
        }

    }

    private void alertaValidacaoPermissao() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @SuppressLint("CutPasteId")
    public void dialogoAddCongregacao() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogoView = inflater.inflate(R.layout.dialog_add_editar_congregacao, null);
        dialogo.setView(dialogoView);
        dialogo.setCancelable(false);
        dialogo.setTitle("Adicionar Congregação");

        getSupportActionBar().setTitle("");

        edtNomeCongregacao = dialogoView.findViewById(R.id.edt_dialog_add_nome_congregação);
        edtCidadeCongregacao = dialogoView.findViewById(R.id.edt_dialog_add_cidade_congregação);


        if (congregacaoelecionada != null) {
            edtNomeCongregacao.setText(congregacaoelecionada.getNomeCongregacao());
            edtCidadeCongregacao.setText(congregacaoelecionada.getCidadeCongregação());
        }

        dialogo.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                nomeCongregacao = edtNomeCongregacao.getText().toString().trim();
                cidadeCongregacao = edtCidadeCongregacao.getText().toString().trim();
                if (congregacaoelecionada != null){
                    idCongregacao = congregacaoelecionada.getIdCongregacao();
                    salvarCongregacao();
                }else {
                    idCongregacao = databaseReference.child("user_data").child(userUID).child("congregacoes").push().getKey();
                    salvarCongregacao();
                }
                dialog.dismiss();

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        final AlertDialog alertDialog = dialogo.create();

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        edtCidadeCongregacao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() >= 1) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }

            }
        });
        edtNomeCongregacao.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() >= 1) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }

            }
        });
    }

    public void salvarCongregacao() {

        if (!nomeCongregacao.isEmpty()) {
            if (!cidadeCongregacao.isEmpty()) {

                congregacaoNova = new Congregacao();
                congregacaoNova.setIdCongregacao(idCongregacao);
                congregacaoNova.setNomeCongregacao(nomeCongregacao);
                congregacaoNova.setCidadeCongregação(cidadeCongregacao);

                databaseReference.child("user_data")
                        .child(userUID)
                        .child("congregacoes")
                        .child(idCongregacao)
                        .setValue(congregacaoNova);
                Toasty.success(AdicionarEditarActivity.this, "Congregação Salva", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toasty.info(AdicionarEditarActivity.this, "Preencha todos os Campos!", Toast.LENGTH_SHORT).show();
                //Toasty.info(AdicionarEditarActivity.this, "Digite o nome da Cidade!", Toast.LENGTH_SHORT).show();
                //dialogoAddCongregacao();
            }
        } else {
            Toasty.info(AdicionarEditarActivity.this, "Preencha todos os Campos!", Toast.LENGTH_SHORT).show();
            //dialogoAddCongregacao();
        }
    }

    private void dialogoAddDiscurso() {

        AlertDialog.Builder dialogo = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogoView = inflater.inflate(R.layout.dialog_add_editar_discurso, null);
        dialogo.setView(dialogoView);
        dialogo.setCancelable(false);
        dialogo.setTitle("Adicionar Discurso");

        getSupportActionBar().setTitle("");

        edtNumeroDiscurso = dialogoView.findViewById(R.id.edt_dialog_add_numero_discurso);
        edtTemaDiscurso = dialogoView.findViewById(R.id.eedt_dialog_add_tema_discurso);


        if (discursoSelecionado != null) {
            edtNumeroDiscurso.setText(discursoSelecionado.getNumero());
            edtTemaDiscurso.setText(discursoSelecionado.getTema());
        }

        dialogo.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                numeroDiscurso = edtNumeroDiscurso.getText().toString().trim();
                temaDiscurso = edtTemaDiscurso.getText().toString().trim();
                if (discursoSelecionado != null){
                    idDisurso = discursoSelecionado.getIdDiscurso();
                    salvarDiscurso();
                }else {
                    idDisurso = databaseReference.child("user_data").child(userUID).child("discursos").push().getKey();
                    salvarDiscurso();
                }
                dialog.dismiss();

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });


        final AlertDialog alertDialog = dialogo.create();

        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        edtNumeroDiscurso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() >= 1) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }

            }
        });
        edtTemaDiscurso.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() >= 1) {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                } else {
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                }

            }
        });
    }

    private void salvarDiscurso() {

        if (!numeroDiscurso.isEmpty()) {
            if (!temaDiscurso.isEmpty()) {

                discursoNovo = new Discurso();
                discursoNovo.setIdDiscurso(idDisurso);
                discursoNovo.setNumero(numeroDiscurso);
                discursoNovo.setTema(temaDiscurso);

                databaseReference.child("user_data")
                        .child(userUID)
                        .child("discursos")
                        .child(idDisurso)
                        .setValue(discursoNovo);
                Toasty.success(AdicionarEditarActivity.this, "Discurso Salvo", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toasty.info(AdicionarEditarActivity.this, "Preencha todos os Campos!", Toast.LENGTH_SHORT).show();
                //Toasty.info(AdicionarEditarActivity.this, "Digite o nome da Cidade!", Toast.LENGTH_SHORT).show();
                //dialogoAddCongregacao();
            }
        } else {
            Toasty.info(AdicionarEditarActivity.this, "Preencha todos os Campos!", Toast.LENGTH_SHORT).show();
            //dialogoAddCongregacao();
        }
    }

}

