package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.qi0.weslley.gerenciadordediscursos.activitys.AdicionarEditarActivity;
import com.qi0.weslley.gerenciadordediscursos.config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.adapter.CongregacaoAdapter;
import com.qi0.weslley.gerenciadordediscursos.helper.Mask;
import com.qi0.weslley.gerenciadordediscursos.helper.Permissao;
import com.qi0.weslley.gerenciadordediscursos.helper.RecyclerItemClickListener;
import com.qi0.weslley.gerenciadordediscursos.helper.VerificaConeccao;
import com.qi0.weslley.gerenciadordediscursos.model.Congregacao;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddEditarOradorFragment extends BaseFragment {

    private File localArquivoFoto = null;

    // Constantes
    private static final int SELECAO_GALERIA = 1;
    private static final int SELECAO_CAMERA = 2;

    // Views
    TextView tvRatingOrador;
    EditText edtNome, edtCongregacao, edtTelefone, edtEmail;
    CircleImageView fotoCadastroOrador;
    ProgressBar progressBar;
    FrameLayout frameLayoutProgressBar;
    RatingBar ratingBarOrador;
    EditText edtNomeCongregacao;
    EditText edtCidadeCongregacao;

    // Variaveis
    //Orador orador;
    Orador oradorNovo;
    Orador oradorSelecionado;
    byte[] dadosImagem;
    float ratingOrador = 3;
    String idCongregacaoSelecionada;
    Congregacao congregacaoSelecionada;
    Congregacao congregacaoNova;
    CongregacaoAdapter congregacaoAdapter;
    String idCongregacao;
    String nomeCongregacao;
    String cidadeCongregacao;
    List<Congregacao> congregacoesList = new ArrayList();
    List<Orador> oradoresList = new ArrayList();
    ArrayList discursos = new ArrayList();
    String idOrador, userUID, urlFotoOrador, nomeOrador, congregacaoOrador, telefoneOrador, emailOrador;

    // Instancias Firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private StorageReference storageReference;
    ValueEventListener valueEventListenerOradores;

    // Array de Permissôes
    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    public AddEditarOradorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_editar_orador, container, false);

        //Configurações iniciais
        firebaseAuth = ConfiguracaoFirebase.getAuth();
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();

        //Validar permissões
        Permissao.validarPermissoes(permissoesNecessarias, getActivity(), 1);

        oradorSelecionado = (Orador) getArguments().getSerializable("oradorSelecionado");
        userUID = firebaseAuth.getCurrentUser().getUid();
        pegarCongregacoesDoBanco();


        // Mapeamento das Views
        edtNome = view.findViewById(R.id.edt_nome_orador_fragmente_add_orador);
        edtCongregacao = view.findViewById(R.id.edt_congregacao_fragmente_add_orador);
        edtTelefone = view.findViewById(R.id.edt_telefone_fragmente_add_orador);
        edtEmail = view.findViewById(R.id.edt_emale_fragmente_add_orador);
        progressBar = view.findViewById(R.id.progressBar_cadastro_orador);
        frameLayoutProgressBar = view.findViewById(R.id.frame_progress_cadastro);
        fotoCadastroOrador = view.findViewById(R.id.img_orador_cadastro_fragment);
        ratingBarOrador = view.findViewById(R.id.ratindBar_orador);
        tvRatingOrador = view.findViewById(R.id.tv_rating_orador);

        if (oradorSelecionado != null) {
            idCongregacaoSelecionada = oradorSelecionado.getIdCongregacao();
            setValoresNoFormulario();
        }

        edtTelefone.addTextChangedListener(Mask.insert("(##)#####-####", edtTelefone));
        edtCongregacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogoEscolherCongregacao();
            }
        });
        edtCongregacao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialogoEscolherCongregacao();
                } else {
                }
            }
        });

        ratingBarOrador.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
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
                ratingOrador = rating;


            }
        });

        FloatingActionButton fab = view.findViewById(R.id.fab_camera_fragment_novo_orador);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogoAlterarFoto();
            }
        });

        final View viewFabDetalhe = getActivity().findViewById(R.id.fab_add_editar);
        viewFabDetalhe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //setDatas(); // ToDo Remover depois
                validarSalvarOrador();
            }
        });

        return view;
    }

    private void pegarCongregacoesDoBanco() {

        valueEventListenerOradores = databaseReference.child("user_data").child(userUID).child("congregacoes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                congregacoesList.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Congregacao congregacao = dados.getValue(Congregacao.class);
                    congregacoesList.add(congregacao);
                    Collections.sort(congregacoesList);

                    if (oradorSelecionado != null) {
                        if (congregacao.getIdCongregacao().equals(oradorSelecionado.getIdCongregacao())) {
                            congregacaoSelecionada = congregacao;
                            //setValoresNoFormulario();
                            edtCongregacao.setText(congregacaoSelecionada.getNomeCongregacao());
                        }
                    }
                }

                //adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void dialogoEscolherCongregacao() {
        oradoresList = Orador.pegarOradoresDoBanco(userUID);

        final AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        final View dialogoView = inflater.inflate(R.layout.dialog_congregacao_list, null);
        dialogo.setView(dialogoView);
        dialogo.setCancelable(false);
        dialogo.setTitle("Escolha uma Congregação");

        RecyclerView recyclerViewDialogo = dialogoView.findViewById(R.id.lista_congregacao_dialogo_agenda);
        LinearLayoutManager layoutManagerDialogo = new LinearLayoutManager(getActivity());
        recyclerViewDialogo.setLayoutManager(layoutManagerDialogo);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerViewDialogo.setHasFixedSize(true);

        congregacaoAdapter = new CongregacaoAdapter(congregacoesList, oradoresList, getContext());

        recyclerViewDialogo.setAdapter(congregacaoAdapter);

        dialogo.setPositiveButton("Add Nova Congregação", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogoAddNovaCongregacao();
                dialog.dismiss();
            }
        });

        final AlertDialog alertDialog = dialogo.create();
        alertDialog.show();

        recyclerViewDialogo.addOnItemTouchListener(new RecyclerItemClickListener(
                getContext(), recyclerViewDialogo, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                congregacaoSelecionada = congregacoesList.get(position);
                idCongregacaoSelecionada = congregacaoSelecionada.getIdCongregacao();
                edtCongregacao.setText(congregacaoSelecionada.getNomeCongregacao());
                alertDialog.dismiss();

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        }));
    }

    private void setValoresNoFormulario() {

        edtNome.setText(oradorSelecionado.getNome());
        //edtCongregacao.setText(congregacaoSelecionada.getNomeCongregacao());
        edtTelefone.setText(oradorSelecionado.getTelefone());
        edtEmail.setText(oradorSelecionado.getEmail());
        ratingBarOrador.setRating(oradorSelecionado.getRatingOrador());

        /*if (VerificaConeccao.isOnline(getActivity())) {
            if (oradorSelecionado.getUrlFotoOrador() != null) {
                pegarFoto();
                Uri uri = Uri.parse(oradorSelecionado.getUrlFotoOrador());
                Glide.with(getContext())
                        .load(uri)
                        .error(R.drawable.img_padrao)
                        .into(fotoCadastroOrador);
            }
        } else {
            Uri uri = Uri.parse(oradorSelecionado.getUrlFotoOrador());
            Glide.with(getContext())
                    .load(uri)
                    .error(R.drawable.img_padrao)
                    .into(fotoCadastroOrador);
        }*/

        if (oradorSelecionado.getUrlFotoOrador() != null) {
            //pegarFoto();
            Uri uri = Uri.parse(oradorSelecionado.getUrlFotoOrador());
            Glide.with(getContext())
                    .load(uri)
                    .error(R.drawable.img_padrao)
                    .into(fotoCadastroOrador);

        }

        switch ((int) oradorSelecionado.getRatingOrador()) {
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
                tvRatingOrador.setText("Padrão");
        }
    }

    private void validarSalvarOrador() {

        userUID = firebaseAuth.getCurrentUser().getUid();
        idOrador = databaseReference.child("user_data").child(userUID).child("oradores").push().getKey();

        nomeOrador = edtNome.getText().toString();
        congregacaoOrador = edtCongregacao.getText().toString();
        telefoneOrador = edtTelefone.getText().toString();
        emailOrador = edtEmail.getText().toString();

        if (!nomeOrador.isEmpty()) {
            if (!congregacaoOrador.isEmpty()) {

                frameLayoutProgressBar.setBackgroundResource(R.color.backgroud_recycle_agenda);
                progressBar.setVisibility(View.VISIBLE);
                getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                if (oradorSelecionado == null) {
                    if (dadosImagem != null) {
                        if (VerificaConeccao.isOnline(getActivity())) {
                            uploadFotoOrador(dadosImagem);
                        } else {
                            uploadOrador();
                            Toasty.error(getContext(), "A Foto nao Foi Salva", Toast.LENGTH_SHORT).show();
                            Toasty.info(getContext(), "Verifique sua Conecção", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        uploadOrador();
                    }
                } else {

                    idOrador = oradorSelecionado.getId();

                    if (oradorSelecionado.getUrlFotoOrador() != null) {
                        if (VerificaConeccao.isOnline(getActivity())) {
                            if (dadosImagem == null) {
                                urlFotoOrador = oradorSelecionado.getUrlFotoOrador();
                                uploadOrador();
                            } else {
                                deleteImageAtual(oradorSelecionado.getUrlFotoOrador());
                                uploadFotoOrador(dadosImagem);
                            }
                        } else {
                            if (dadosImagem != null){
                                urlFotoOrador = oradorSelecionado.getUrlFotoOrador();
                                uploadOrador();
                                Toasty.error(getContext(), "A Foto Não Foi Atualizada", Toast.LENGTH_SHORT).show();
                                Toasty.info(getContext(), "Verifique sua Conecção", Toast.LENGTH_SHORT).show();
                            }else {
                                urlFotoOrador = oradorSelecionado.getUrlFotoOrador();
                                uploadOrador();
                            }

                        }
                    } else {
                        if (dadosImagem != null) {
                            if (VerificaConeccao.isOnline(getActivity())) {
                                uploadFotoOrador(dadosImagem);
                            } else {
                                uploadOrador();
                                Toasty.error(getContext(), "A Foto Não Foi Atualizada", Toast.LENGTH_SHORT).show();
                                Toasty.info(getContext(), "Verifique sua Conecção", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            uploadOrador();
                        }

                    }

                    //Todo Remover esse bloco de oodigo se o de cima fucionar sem bugs
                    /*if (dadosImagem != null) {
                        if (VerificaConeccao.isOnline(getActivity())) {
                            if (oradorSelecionado.getUrlFotoOrador() != null){
                                urlFotoOrador = oradorSelecionado.getUrlFotoOrador();
                                uploadOrador();
                            }else {
                                deleteImageAtual(oradorSelecionado.getUrlFotoOrador());
                                uploadFotoOrador(dadosImagem);
                            }

                        } else {
                            uploadOrador();
                        }
                    } else {
                        uploadOrador();
                    }*/
                }

            } else {
                dialogoEscolherCongregacao();
            }
        } else {
            Toasty.info(getContext(), "Adicione um Nome", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadOrador() {

        oradorNovo = new Orador();

        oradorNovo.setId(idOrador);
        oradorNovo.setNome(nomeOrador);
        oradorNovo.setIdCongregacao(idCongregacaoSelecionada);// Todo trocar por id da congregaçao
        oradorNovo.setTelefone(telefoneOrador);
        oradorNovo.setEmail(emailOrador);
        oradorNovo.setUltimaVisita("");
        oradorNovo.setUrlFotoOrador(urlFotoOrador);
        oradorNovo.setDiscursoListOrador(discursos);
        oradorNovo.setRatingOrador(ratingOrador);

        databaseReference.child("user_data")
                .child(userUID)
                .child("oradores")
                .child(idOrador)
                .setValue(oradorNovo);

        progressBar.setVisibility(View.GONE);

        Intent devolve = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable("oradorSelecionado", oradorNovo);
        devolve.putExtra("oradorSelecionado", bundle);
        getActivity().setResult(RESULT_OK, devolve);

        if (oradorSelecionado == null) {
            Toasty.success(getContext(), "Orador Salvo", Toast.LENGTH_SHORT).show();
        } else {
            Toasty.success(getContext(), "Orador Atualizado", Toast.LENGTH_SHORT).show();
        }

        getActivity().finish();
    }

    private void deleteImageAtual(String uri) {

        StorageReference imagemRef = storageReference
                .child("imagens")
                .child(userUID)
                .child("orador_perfil")
                .child(oradorSelecionado.getId() + ".jpeg");

        imagemRef.delete().addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        }).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

            }
        });
    }

    private void dialogoAlterarFoto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.alterar_foto);
        builder.setCancelable(true);
        builder.setItems(R.array.array_foto_formulario_options, new DialogInterface.OnClickListener() {
            String msg = "";

            @RequiresApi(api = Build.VERSION_CODES.N)
            public void onClick(DialogInterface dialog, int item) {
                switch (item) {
                    case 0:
                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                            startActivityForResult(takePictureIntent, SELECAO_CAMERA);
                        }
                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, SELECAO_GALERIA);
                        break;
                    default:
                        break;
                }
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap imagem = null;

            try {

                switch (requestCode) {
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALERIA:
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localImagemSelecionada);
                        break;
                }

                if (imagem != null) {

                    fotoCadastroOrador.setImageBitmap(imagem);

                    //Recuperar dados da imagem para o firebase
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 80, baos);
                    dadosImagem = baos.toByteArray();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void uploadFotoOrador(byte[] dadosImagem) {

        //Salvar imagem no firebase
        StorageReference imagemRef = storageReference
                .child("imagens")
                .child(userUID)
                .child("orador_perfil")
                .child(idOrador + ".jpeg");


        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(),
                        "Erro ao fazer upload da imagem",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Uri url = taskSnapshot.getDownloadUrl();
                urlFotoOrador = url.toString();

                uploadOrador();
            }
        });
    }

    public void dialogoAddNovaCongregacao() {

        final AlertDialog.Builder dialogo = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogoView = inflater.inflate(R.layout.dialog_add_editar_congregacao, null);
        dialogo.setView(dialogoView);
        dialogo.setCancelable(false);
        dialogo.setTitle("Adicionar Congregação");

        edtNomeCongregacao = dialogoView.findViewById(R.id.edt_dialog_add_nome_congregação);
        edtCidadeCongregacao = dialogoView.findViewById(R.id.edt_dialog_add_cidade_congregação);

        dialogo.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                nomeCongregacao = edtNomeCongregacao.getText().toString().trim();
                cidadeCongregacao = edtCidadeCongregacao.getText().toString().trim();
                salvarCongregacao();
                dialog.dismiss();

            }
        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
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

        idCongregacao = databaseReference.child("user_data").child(userUID).child("congregacoes").push().getKey();

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

                Toasty.success(getContext(), "Congregação Salva", Toast.LENGTH_SHORT).show();

                edtCongregacao.setText(congregacaoNova.getNomeCongregacao());
                idCongregacaoSelecionada = congregacaoNova.getIdCongregacao();

            } else {
                Toasty.info(getContext(), "Preencha todos os Campos!", Toast.LENGTH_SHORT).show();
                //Toasty.info(AdicionarEditarActivity.this, "Digite o nome da Cidade!", Toast.LENGTH_SHORT).show();
                //dialogoAddNovaCongregacao();
            }
        } else {
            Toasty.info(getContext(), "Preencha todos os Campos!", Toast.LENGTH_SHORT).show();
            //dialogoAddNovaCongregacao();
        }
    }

    private SimpleTarget target = new SimpleTarget<Bitmap>() {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation<? super Bitmap> glideAnimation) {
            //fotoCadastroOrador.setImageBitmap(bitmap);
            //Recuperar dados da imagem para o firebase
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            dadosImagem = baos.toByteArray();
        }
    };

    // ToDo Remover depois
    private void pegarCongregacaoPeloID() {

        databaseReference.child("user_data").child(userUID).child("congregacoes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot dados : dataSnapshot.getChildren()) {
                    Congregacao congregacao = dados.getValue(Congregacao.class);
                    congregacoesList.add(congregacao);
                    if (congregacao.getIdCongregacao().equals(oradorSelecionado.getIdCongregacao())){
                        congregacaoSelecionada = congregacao;
                        edtCongregacao.setText(congregacaoSelecionada.getNomeCongregacao());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void pegarFoto() {

        Glide.with(getContext())
                .load(oradorSelecionado.getUrlFotoOrador())
                .asBitmap()
                .into(target);

    }
    private void dialogSelecionarCongregacao() {

        //userUID = firebaseAuth.getCurrentUser().getUid();
        //pegarCongregacoesDoBanco();

        List<String> nomeCongregacoesList = new ArrayList<>();

        for (Congregacao congregacao : congregacoesList) {
            nomeCongregacoesList.add(congregacao.getNomeCongregacao());
        }

        final String[] nomeCongArray = nomeCongregacoesList.toArray(new String[nomeCongregacoesList.size()]);

        AlertDialog.Builder alertbox = new AlertDialog.Builder(getContext());
        alertbox.setTitle("Escolha uma Congregação")
                .setItems(nomeCongArray, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int pos) {

                        String nome = nomeCongArray[pos];
                        edtCongregacao.setText(nome);
                        Congregacao congregacao = congregacoesList.get(pos);
                        idCongregacaoSelecionada = congregacao.getIdCongregacao();
                    }
                }).setPositiveButton("Add Nova Congregação", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialogoAddNovaCongregacao();
            }
        });

        alertbox.show();
    }
    private void setDatas() {

        for (int i = 0; i <= 11; i++) {
            pegarListaDataDoDomingos(i, 2018);
        }
    }
    public void pegarListaDataDoDomingos(int mes, int ano) {
        userUID = firebaseAuth.getCurrentUser().getUid();

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

                databaseReference.child("user_data")
                        .child(userUID)
                        .child("Agenda")
                        .child(String.valueOf(ano))
                        .child(mesFormatado)
                        .child(dataFormatada)
                        .setValue("Dados da Agenda Aqui!");

            }
            // incrementa um dia no calendário
            c.roll(Calendar.DAY_OF_MONTH, true);

            // enquanto o dia do mês atual for diferente de 1
        } while (c.get(Calendar.DAY_OF_MONTH) != 1);
    }

}
