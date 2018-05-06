package com.qi0.weslley.gerenciadordediscursos.activitys;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.qi0.weslley.gerenciadordediscursos.R;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.bt_login_usuario);
        TextView tvCadastrese = findViewById(R.id.tv_ir_cadastro_usuario);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendToMain();
            }
        });

        tvCadastrese.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentcCadastrarActivity = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
                startActivity(intentcCadastrarActivity);
            }
        });
    }

    public void sendToMain (){
        Intent intentMainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intentMainActivity);
        finish();
    }
}
