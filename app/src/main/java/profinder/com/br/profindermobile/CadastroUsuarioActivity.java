package profinder.com.br.profindermobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        mNome = findViewById(R.id.nome);
        mEmail = findViewById(R.id.email);
        mSenha = findViewById(R.id.senha);
        mRetypesenha = findViewById(R.id.retypesenha);
        mMatricula = findViewById(R.id.matricula);

        tipoUsuario = findViewById(R.id.tipoUsuario);

        radioButtonProfessor = findViewById(R.id.radioButtonProfessor);
        radioButtonAluno = findViewById(R.id.radioButtonAluno);

        buttonsignup = findViewById(R.id.circularProgressButton2);

        this.db = FirebaseFirestore.getInstance();
        this.mAuth = FirebaseAuth.getInstance();

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

    private boolean setError() {
        boolean vazio = false;
        if(mNome.getText().toString().isEmpty()) {
            mNome.setError("Campo vazio!");
            vazio = true;
        }

        if(mEmail.getText().toString().isEmpty()) {
            mEmail.setError("Campo vazio!");
        } else if(!Patterns.EMAIL_ADDRESS.matcher(mEmail.getText()).matches()) {
            mEmail.setError("Email inválido!");
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
                                    mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
                                        @Override
                                        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                                            user = mAuth.getCurrentUser();
                                            if(user != null) {
                                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(mNome.getText().toString()).build();
                                                user.updateProfile(profileUpdates);
                                                Intent intent = new Intent(CadastroUsuarioActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                                } else {
                                    Toast.makeText(CadastroUsuarioActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(CadastroUsuarioActivity.this, "Authentication failed.",
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