package com.qi0.weslley.gerenciadordediscursos.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qi0.weslley.gerenciadordediscursos.R;

public class CadastroUsuarioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        Button btnCadastrar = findViewById(R.id.bt_cadastrar_usuario);
        TextView tvVoltar = findViewById(R.id.tv_voltar_cadastro_usuario);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToMain();
            }
        });

        tvVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void sendToMain (){
        Intent intentMainActivity = new Intent(CadastroUsuarioActivity.this, MainActivity.class);
        startActivity(intentMainActivity);
        finish();
    }
}
