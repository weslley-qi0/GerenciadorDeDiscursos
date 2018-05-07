package com.qi0.weslley.gerenciadordediscursos.activitys;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.qi0.weslley.gerenciadordediscursos.Config.ConfiguraçãoFirebase;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.model.Usuario;

public class CadastroUsuarioActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String userUID;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        firebaseAuth = ConfiguraçãoFirebase.getFirebaseAuth();

        Button btnCadastrar = findViewById(R.id.bt_cadastrar_usuario);
        TextView tvVoltar = findViewById(R.id.tv_voltar_cadastro_usuario);
        final EditText edtNomeCadasro = findViewById(R.id.edt_nome_usuario_cadastro);
        final EditText edtCongregacaoCadasro = findViewById(R.id.edt_nome_congregacao_usuario_cadastro);
        final EditText edtEmailCadasro = findViewById(R.id.edt_email_usuario_cadastro);
        final EditText edtSenhaCadasro = findViewById(R.id.edt_senha_usuario_cadastro);
        final EditText edtConfirmarSenhaCadasro = findViewById(R.id.edt_confirm_senha_usuario_cadastro);


        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txtNomeCadastro = edtNomeCadasro.getText().toString().trim();
                String txtCongregacaoCadastro = edtCongregacaoCadasro.getText().toString().trim();
                String txtEmailCadastro = edtEmailCadasro.getText().toString().trim();
                String txtSenhaCadastro = edtSenhaCadasro.getText().toString().trim();
                String txtConfirmarSenhaCadastro = edtConfirmarSenhaCadasro.getText().toString().trim();

                usuario = new Usuario();
                usuario.setNome(txtNomeCadastro);
                usuario.setCongregacao(txtCongregacaoCadastro);
                usuario.setEmail(txtEmailCadastro);
                usuario.setSenha(txtSenhaCadastro);

                validarForm(txtNomeCadastro
                        ,txtCongregacaoCadastro
                        ,txtEmailCadastro
                        ,txtSenhaCadastro
                        ,txtConfirmarSenhaCadastro);
            }
        });

        tvVoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void validarForm(String txtNomeCadastro, String txtCongregacaoCadastro, String txtEmailCadastro, String txtSenhaCadastro, String txtConfirmarSenhaCadastro) {
            if (!txtNomeCadastro.isEmpty()){
                if (!txtCongregacaoCadastro.isEmpty()){
                    if (!txtEmailCadastro.isEmpty()){
                        if (!txtSenhaCadastro.isEmpty()){
                            if (!txtConfirmarSenhaCadastro.isEmpty()){
                                if (txtConfirmarSenhaCadastro.equals(txtSenhaCadastro)){
                                    cadastrarUsuario();
                                }else {
                                    Toast.makeText(this, "As senhas não corespondem!", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(this, "Confirme a senha", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(this, "Preencha o campo Senha", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "Preencha o campo Email", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(this, "Preencha o campo Congregação", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(this, "Preencha o campo Nome", Toast.LENGTH_SHORT).show();
            }
    }

    public void sendToMain (){
        Intent intentMainActivity = new Intent(CadastroUsuarioActivity.this, MainActivity.class);
        startActivity(intentMainActivity);
        finish();
    }

    public void cadastrarUsuario(){
        firebaseAuth.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    userUID = firebaseAuth.getCurrentUser().getUid();
                    salvarUsuario();
                    sendToMain();
                }else {
                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao= "Por favor, digite um e-mail válido";
                    }catch ( FirebaseAuthUserCollisionException e){
                        excecao = "Este conta já foi cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastroUsuarioActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void salvarUsuario(){
        databaseReference = ConfiguraçãoFirebase.getFirebaseDatabase();
        databaseReference.child("usuarios").child(userUID).setValue(usuario);
        databaseReference.child("user_data").child(userUID).child("oradores").push();


    }
}
