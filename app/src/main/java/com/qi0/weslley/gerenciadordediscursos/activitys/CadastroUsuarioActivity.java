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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DatabaseReference;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.model.Agenda;
import com.qi0.weslley.gerenciadordediscursos.model.Usuario;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import es.dmoral.toasty.Toasty;

public class CadastroUsuarioActivity extends BaseActivity {

    FrameLayout frameLayoutProgressBar;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    String userUID;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        firebaseAuth = ConfiguracaoFirebase.getAuth();

        Button btnCadastrar = findViewById(R.id.bt_cadastrar_usuario);
        TextView tvVoltar = findViewById(R.id.tv_voltar_cadastro_usuario);
        final EditText edtNomeCadasro = findViewById(R.id.edt_nome_usuario_cadastro);
        final EditText edtCongregacaoCadasro = findViewById(R.id.edt_nome_congregacao_usuario_cadastro);
        final EditText edtEmailCadasro = findViewById(R.id.edt_email_usuario_cadastro);
        final EditText edtSenhaCadasro = findViewById(R.id.edt_senha_usuario_cadastro);
        final EditText edtConfirmarSenhaCadasro = findViewById(R.id.edt_confirm_senha_usuario_cadastro);
        frameLayoutProgressBar = findViewById(R.id.frame_progress_cadastro);
        progressBar = findViewById(R.id.progress_bar_cadastrar);


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
                                    Toasty.info(CadastroUsuarioActivity.this, "As senhas não corespondem!", Toast.LENGTH_SHORT, true).show();
                                }
                            }else {
                                Toasty.info(CadastroUsuarioActivity.this, "Confirme a senha", Toast.LENGTH_SHORT, true).show();
                            }
                        }else {
                            Toasty.info(CadastroUsuarioActivity.this, "Preencha o campo Senha", Toast.LENGTH_SHORT, true).show();
                        }
                    }else {
                        Toasty.info(CadastroUsuarioActivity.this, "Preencha o campo Email", Toast.LENGTH_SHORT, true).show();
                    }
                }else {
                    Toasty.info(CadastroUsuarioActivity.this, "Preencha o campo Congregação", Toast.LENGTH_SHORT, true).show();
                }
            }else {
                Toasty.info(CadastroUsuarioActivity.this, "Preencha o campo Nome", Toast.LENGTH_SHORT, true).show();
            }
    }

    public void sendToMain (){
        Intent intentMainActivity = new Intent(CadastroUsuarioActivity.this, MainActivity.class);
        startActivity(intentMainActivity);
        criarAgenda();
        finish();
    }

    public void cadastrarUsuario(){

        frameLayoutProgressBar.setBackgroundResource(R.color.backgroud_recycle_agenda);
        progressBar.setVisibility(View.VISIBLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

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
                    Toasty.info(CadastroUsuarioActivity.this, excecao, Toast.LENGTH_SHORT, true).show();
                }

                frameLayoutProgressBar.setBackgroundResource(R.color.transparent);
                progressBar.setVisibility(View.GONE);
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            }
        });
    }

    private void criarAgenda() {
        Calendar calendar = Calendar.getInstance();
        int ano = calendar.get(Calendar.YEAR);
        for (int i = 0; i <= 11; i++) {
            criarAgendaNoBanco(i, ano);
        }
    }

    public void criarAgendaNoBanco(int mes, int ano) {
        userUID = firebaseAuth.getCurrentUser().getUid();
        Agenda agenda = new Agenda();

        // cria um calendário na data 01/mes/ano
        Calendar c = new GregorianCalendar(ano, mes, 1);
        // Pega a Data e formata de Acordo com a Região Ex: 00/00/00
        //DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);

        do {
            // o dia da semana ecolhido é domingo?
            if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
                SimpleDateFormat dataSDF = new SimpleDateFormat("dd-MM-yyyy");
                SimpleDateFormat mesSDF = new SimpleDateFormat("MM");
                String dataFormatada = dataSDF.format(c.getTime());
                String mesFormatado = mesSDF.format(c.getTime());

                agenda.setData(dataFormatada);
                agenda.setIdCongregacao("");
                agenda.setIdOrador("");
                agenda.setIdDiscurso("");

                databaseReference.child("user_data")
                        .child(userUID)
                        .child("agenda")
                        .child(String.valueOf(ano))
                        .child(mesFormatado)
                        .child(dataFormatada)
                        .setValue(agenda);

            }
            // incrementa um dia no calendário
            c.roll(Calendar.DAY_OF_MONTH, true);

            // enquanto o dia do mês atual for diferente de 1
        } while (c.get(Calendar.DAY_OF_MONTH) != 1);
    }

    public void salvarUsuario(){
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();
        databaseReference.child("usuarios").child(userUID).setValue(usuario);
        databaseReference.child("user_data").child(userUID).child("oradores").push();
    }
}
