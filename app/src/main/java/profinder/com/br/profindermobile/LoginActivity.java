package profinder.com.br.profindermobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.novoda.merlin.Merlin;
import com.novoda.merlin.registerable.connection.Connectable;
import com.novoda.merlin.registerable.disconnection.Disconnectable;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class LoginActivity extends AppCompatActivity {
    private EditText mUsuario,mSenha;
    private TextView tvSignUp;
    private Button mLogin;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private FirebaseFirestore fs;
    private DocumentReference dr;
    private CircularProgressButton circularProgressButton;
    private Usuario usuario;
    private Merlin merlin;
    private boolean conectado;
    private Snackbar snackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponentes();
        onClicks();

        merlin.registerConnectable(new Connectable() {
            @Override
            public void onConnect() {
                if(!conectado) {
                    snackbar = Snackbar.make(findViewById(R.id.login_layout), "Com conexão", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
                conectado = true;
            }
        });

        merlin.registerDisconnectable(new Disconnectable() {
            @Override
            public void onDisconnect() {
                conectado = false;
                snackbar = Snackbar.make(findViewById(R.id.login_layout), "Sem conexão", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
            }
        });
    }

    private void onClicks() {
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(conectado) {
                    if (!setError()) {
                        circularProgressButton.startAnimation();
                        circularProgressButton.setEnabled(false);
                        TarefaLogar logar = new TarefaLogar();
                        logar.execute(mAuth);
                    }
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initComponentes() {
        merlin = new Merlin.Builder().withAllCallbacks().build(this);
        FirebaseApp.initializeApp(this);
        mUsuario = findViewById(R.id.usuario);
        mSenha = findViewById(R.id.senha);
        tvSignUp = findViewById(R.id.tv_cadastre_se);
        mAuth = FirebaseAuth.getInstance();
        circularProgressButton = findViewById(R.id.circularProgressButton);
        fs = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        merlin.bind();
    }

    @Override
    protected void onPause() {
        merlin.unbind();
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if(user != null && user.isEmailVerified()) {
            Intent intent = new Intent(LoginActivity.this, ProfessorActivity.class);
            startActivity(intent);
        }
    }

    private boolean setError() {
        boolean vazio = false;
        if(TextUtils.isEmpty(mUsuario.getText())) {
            mUsuario.setError("Campo vazio!");
            vazio = true;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mUsuario.getText()).matches() && !TextUtils.isEmpty(mUsuario.getText())) {
            mUsuario.setError("Email inválido");
            vazio = true;
        }

        if(TextUtils.isEmpty(mSenha.getText())) {
            mSenha.setError("Campo vazio!");
            vazio = true;
        }

        return vazio;
    }

    class TarefaLogar extends AsyncTask<FirebaseAuth, Void, Void> {
        private Snackbar snackbar;

        @Override
        protected Void doInBackground(FirebaseAuth... firebaseAuths) {
            mAuth.signInWithEmailAndPassword(mUsuario.getText().toString(), mSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user = mAuth.getCurrentUser();
                        if(user.isEmailVerified()) {
                            dr = fs.collection("users").document(user.getUid());
                            dr.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {
                                    usuario = documentSnapshot.toObject(Usuario.class);
                                    if(usuario.getType().equals("professor")) {
                                        Intent intent = new Intent(LoginActivity.this, ProfessorActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            circularProgressButton.revertAnimation();
                            circularProgressButton.setEnabled(true);
                            snackbar = Snackbar.make(findViewById(R.id.login_layout), "Email não verificado", Snackbar.LENGTH_INDEFINITE)
                                    .setAction("Reenviar", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            user.sendEmailVerification();
                                            snackbar.dismiss();
                                        }
                                    });
                            snackbar.show();
                        }
                    } else {
                        circularProgressButton.revertAnimation();
                        circularProgressButton.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return null;
        }
    }

}