package profinder.com.br.profindermobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText mNome, mEmail, mSenha, mRetypesenha, mMatricula;
    private RadioGroup tipoUsuario;
    private RadioButton radioButtonProfessor, radioButtonAluno;

    private FirebaseFirestore db;
    private DocumentReference dr;

    private CircularProgressButton buttonsignup;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_usuario);

        initComponentes();
        onClicks();
        listeners();
    }

    private void listeners() {
        mRetypesenha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(!charSequence.toString().equals(mSenha.getText().toString())) {
                    mRetypesenha.setError("Senha não confere");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void onClicks() {
        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!setError()) {
                    buttonsignup.startAnimation();
                    buttonsignup.setEnabled(false);
                    TarefaCadastrarUsuario tarefaCadastrarUsuario = new TarefaCadastrarUsuario();
                    tarefaCadastrarUsuario.execute(mAuth);
                }
            }
        });
    }

    private void initComponentes() {
        mNome = findViewById(R.id.nome);
        mEmail = findViewById(R.id.email);
        mSenha = findViewById(R.id.senha);
        mRetypesenha = findViewById(R.id.retypesenha);
        mMatricula = findViewById(R.id.matricula);

        tipoUsuario = findViewById(R.id.tipoUsuario);

        radioButtonProfessor = findViewById(R.id.radioButtonProfessor);
        radioButtonAluno = findViewById(R.id.radioButtonAluno);

        buttonsignup = findViewById(R.id.circularProgressButton2);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private boolean setError() {
        boolean vazio = false;
        if(mNome.getText().toString().isEmpty()) {
            mNome.setError("Campo vazio!");
            vazio = true;
        }

        if(mEmail.getText().toString().isEmpty()) {
            mEmail.setError("Campo vazio!");
        } else if(!Patterns.EMAIL_ADDRESS.matcher(mEmail.getText()).matches()) {
            mEmail.setError("E-mail inválido!");
            vazio = true;
        }

        if(mSenha.getText().toString().isEmpty()) {
            mSenha.setError("Campo vazio!");
            vazio = true;
        }

        if(mRetypesenha.getText().toString().isEmpty()) {
            mRetypesenha.setError("Campo vazio!");
            vazio = true;
        }

        if(mMatricula.getText().toString().isEmpty()) {
            mMatricula.setError("Campo vazio!");
            vazio = true;
        }

        return vazio;
    }

    class TarefaCadastrarUsuario extends AsyncTask<FirebaseAuth, Void, Void> {

        @Override
        protected Void doInBackground(FirebaseAuth... firebaseAuths) {
            mAuth.createUserWithEmailAndPassword(mEmail.getText().toString(), mSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        user = mAuth.getCurrentUser();
                        String nome, email, senha, matricula;
                        nome = mNome.getText().toString();
                        email = mEmail.getText().toString();
                        senha = mSenha.getText().toString();
                        matricula = mMatricula.getText().toString();
                        Usuario u = null;
                        if(radioButtonProfessor.isChecked()) {
                            u = new Usuario(nome, email, senha, matricula, "professor");
                        } else {
                            u = new Usuario(nome, email, senha, matricula, "aluno");
                        }
                        user = mAuth.getCurrentUser();
                        String path = "users/"+user.getUid();
                        dr = db.document(path);
                        dr.set(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()) {
                                    user = mAuth.getCurrentUser();
                                    if(user != null) {
                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(mNome.getText().toString()).build();
                                        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    user.reload();
                                                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if(task.isSuccessful()) {
                                                                Snackbar.make(findViewById(R.id.cadastro_usuario_layout), "Link de verificação enviado ao seu e-mail.", Snackbar.LENGTH_LONG).show();
                                                                new Handler().postDelayed(new Runnable() {
                                                                    @Override
                                                                    public void run() {
                                                                        Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
                                                                        startActivity(intent);
                                                                        finish();
                                                                    }
                                                                }, 2000);
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    }
                                } else {
                                    Toast.makeText(CadastroUsuarioActivity.this, "Falha ao adicionar usuário ao banco de dados.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(CadastroUsuarioActivity.this, "Falha ao cadastrar usuário.",
                                Toast.LENGTH_SHORT).show();
                        buttonsignup.revertAnimation();
                        buttonsignup.setEnabled(true);
                    }
                }
            });
            return null;
        }
    }
}