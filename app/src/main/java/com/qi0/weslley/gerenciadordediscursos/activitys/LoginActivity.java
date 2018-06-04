package com.qi0.weslley.gerenciadordediscursos.activitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.model.Usuario;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends BaseActivity {

    FrameLayout frameLayoutProgressBar;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = findViewById(R.id.bt_login_usuario);
        TextView tvCadastrese = findViewById(R.id.tv_ir_cadastro_usuario);
        final EditText edtEmailLogin = findViewById(R.id.edt_email_usuario_login);
        final EditText edtSenhaLogin = findViewById(R.id.edt_senha_usuario_login);
        frameLayoutProgressBar = findViewById(R.id.frame_progress_cadastro);
        progressBar = findViewById(R.id.progress_bar_logim);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtEmail = edtEmailLogin.getText().toString().trim();
                String txtSenha = edtSenhaLogin.getText().toString().trim();

                usuario = new Usuario();
                usuario.setEmail(txtEmail);
                usuario.setSenha(txtSenha);

                validarForm(txtEmail, txtSenha);

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

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = ConfiguracaoFirebase.getAuth();
        if (firebaseAuth.getCurrentUser() != null){
            sendToMain();
        }
    }

    private void validarForm(String txtEmail, String txtSenha) {
        if (!txtEmail.isEmpty()){
            if(!txtSenha.isEmpty()){
                logarUsuario();
            }else {
                Toasty.info(LoginActivity.this, "Prencha o Campo Senha!", Toast.LENGTH_SHORT, true).show();
            }
        }else {
            Toasty.info(LoginActivity.this, "Digite um Email!", Toast.LENGTH_SHORT, true).show();
        }
    }

    public void logarUsuario(){

        frameLayoutProgressBar.setBackgroundResource(R.color.backgroud_recycle_agenda);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

        firebaseAuth = ConfiguracaoFirebase.getAuth();
        firebaseAuth.signInWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    sendToMain();
                }else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthInvalidUserException e ) {
                        excecao = "Usuário não está cadastrado.";
                    }catch ( FirebaseAuthInvalidCredentialsException e ){
                        excecao = "E-mail e senha não correspondem!";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }
                    Toasty.info(LoginActivity.this, excecao, Toast.LENGTH_SHORT, true).show();
                }
                frameLayoutProgressBar.setBackgroundResource(R.color.transparent);
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }

    public void sendToMain (){
        Intent intentMainActivity = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intentMainActivity);
        finish();
    }
}
