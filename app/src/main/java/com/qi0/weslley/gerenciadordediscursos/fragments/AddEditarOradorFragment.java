package com.qi0.weslley.gerenciadordediscursos.fragments;


import android.Manifest;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.qi0.weslley.gerenciadordediscursos.Config.ConfiguracaoFirebase;
import com.qi0.weslley.gerenciadordediscursos.R;
import com.qi0.weslley.gerenciadordediscursos.activitys.AdicionarEditarActivity;
import com.qi0.weslley.gerenciadordediscursos.helper.Mask;
import com.qi0.weslley.gerenciadordediscursos.helper.Permissao;
import com.qi0.weslley.gerenciadordediscursos.helper.VerificaConeccao;
import com.qi0.weslley.gerenciadordediscursos.model.Congregacao;
import com.qi0.weslley.gerenciadordediscursos.model.Orador;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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

    // Variaveis
    Orador orador;
    byte[] dadosImagem;
    float ratingOrador = 3;
    Congregacao congregacaoSelecionada;
    List<Congregacao> congregacoesList = new ArrayList();
    String idOrador, userUid, urlFotoOrador, nomeOrador, congregacaoOrador, telefoneOrador, emailOrador;

    // Instancias Firebase
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    private StorageReference storageReference;

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
        storageReference = ConfiguracaoFirebase.getFirebaseStorage();
        firebaseAuth = ConfiguracaoFirebase.getAuth();
        databaseReference = ConfiguracaoFirebase.getFirebaseDatabase();

        //Validar permissões
        Permissao.validarPermissoes(permissoesNecessarias, getActivity(), 1);

        orador = (Orador) getArguments().getSerializable("oradorSelecionado");
        mokeOradores();

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

        if (orador != null) {
            congregacaoSelecionada = orador.getCongregacao();
            setValoresNoFormulario();
        }

        edtTelefone.addTextChangedListener(Mask.insert("(##)#####-####", edtTelefone));
        edtCongregacao.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    dialogSelecionarCongregação();
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

                validarSalvarOrador();
            }
        });

        return view;
    }

    private void dialogSelecionarCongregação() {

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
                        congregacaoSelecionada = congregacoesList.get(pos);
                    }
                }).setPositiveButton("Add Nova Congregação", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intentAddCongregacao = new Intent(getActivity(), AdicionarEditarActivity.class);
                intentAddCongregacao.putExtra("qualFragmentAbrir", "AddCongregacaoFragment");
                startActivity(intentAddCongregacao);
            }
        });

        alertbox.show();
    }

    private void setValoresNoFormulario() {
        Congregacao congregacao = orador.getCongregacao();
        edtNome.setText(orador.getNome());
        edtCongregacao.setText(congregacao.getNomeCongregacao());
        edtTelefone.setText(orador.getTelefone());
        edtEmail.setText(orador.getEmail());
        ratingBarOrador.setRating(orador.getRatingOrador());

        if (orador.getUrlFotoOrador() != null) {
            pegarFoto();
            Uri uri = Uri.parse(orador.getUrlFotoOrador());
            Glide.with(getContext())
                    .load(uri)
                    .into(fotoCadastroOrador);

        }

        switch ((int) orador.getRatingOrador()) {
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

    private void pegarFoto() {

        Glide.with(getContext())
                .load(orador.getUrlFotoOrador())
                .asBitmap()
                .into(target);

    }

    private void validarSalvarOrador(){
        userUid = firebaseAuth.getCurrentUser().getUid();
        idOrador = databaseReference.child("user_data").child(userUid).child("oradores").push().getKey();

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

                if (orador == null) {
                    if (dadosImagem != null) {
                        if (VerificaConeccao.isOnline(getActivity())) {
                            uploadFotoOrador(dadosImagem);
                        } else {
                            uploadOrador();
                        }
                    } else {
                        uploadOrador();
                    }
                } else {
                    idOrador = orador.getId();
                    if (dadosImagem != null) {
                        if (VerificaConeccao.isOnline(getActivity())) {
                            deleteImageAtual(orador.getUrlFotoOrador());
                            uploadFotoOrador(dadosImagem);

                        } else {
                            uploadOrador();
                            //atualizarOrador();
                        }
                    } else {
                        uploadOrador();
                        //atualizarOrador();
                    }

                }

            } else {
                Toast.makeText(getContext(), "Escolha uma Congregação", Toast.LENGTH_SHORT).show();
                dialogSelecionarCongregação();
            }
        } else {
            Toast.makeText(getContext(), "Adicione um Nome", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadOrador() {

        orador = new Orador();

        orador.setId(idOrador);
        orador.setNome(nomeOrador);
        orador.setCongregacao(congregacaoSelecionada);
        orador.setTelefone(telefoneOrador);
        orador.setRatingOrador(ratingOrador);
        orador.setEmail(emailOrador);
        orador.setUrlFotoOrador(urlFotoOrador);
        orador.setUltimaVisita("");

        databaseReference.child("user_data")
                .child(userUid)
                .child("oradores")
                .child(idOrador)
                .setValue(orador);

        progressBar.setVisibility(View.GONE);
        getActivity().finish();
    }

    private void deleteImageAtual(String uri) {

        StorageReference imagemRef = storageReference
                .child("imagens")
                .child(userUid)
                .child("orador_perfil")
                .child(orador.getId() + ".jpeg");

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
                .child(userUid)
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

    private void mokeOradores() {
        for (int i = 0; i <= 20; i++) {
            Congregacao congregacao = new Congregacao();
            congregacao.setNomeCongregacao("Nome da Congregação " + i);
            congregacao.setCidadeCongregação("Nome da Cidade");
            congregacao.setQuantOradores(i++);
            congregacoesList.add(congregacao);
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
}
