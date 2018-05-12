package com.qi0.weslley.gerenciadordediscursos.activitys;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.fragments.AddEditarOradorFragment;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

public class AdicionarEditarActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_editar);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_pricipal);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Orador oradorSelecionado = (Orador) intent.getSerializableExtra("oradorSelecionado");

        String fragmentAbrir = (String) getIntent().getCharSequenceExtra("qualFragmentAbrir");

        if (fragmentAbrir.equals("AddCongregacaoFragment")){

            replaceFragment(R.id.fragment_container_add_editar, new Fragment());
            getSupportActionBar().setTitle("Nova Congregação");

        }else if (fragmentAbrir.equals("AddOradorFragment")){

            Bundle bundle = new Bundle();
            bundle.putSerializable("oradorSelecionado", oradorSelecionado);
            AddEditarOradorFragment addEditarOradorFragment = new AddEditarOradorFragment();
            addEditarOradorFragment.setArguments(bundle);
            replaceFragment(R.id.fragment_container_add_editar, addEditarOradorFragment);
            getSupportActionBar().setTitle("Novo Orador");

        }else if (fragmentAbrir.equals("AddDiscursosFragment")){

            replaceFragment(R.id.fragment_container_add_editar, new Fragment());
            getSupportActionBar().setTitle("Novo Discurso");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_editar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(AdicionarEditarActivity.this, "Salvando...", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for ( int permissaoResultado : grantResults ){
            if ( permissaoResultado == PackageManager.PERMISSION_DENIED ){
                alertaValidacaoPermissao();
            }
        }

    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
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

}
