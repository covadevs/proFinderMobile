package profinder.com.br.profindermobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText mUsuario,mSenha;
    private TextView tvSignUp;
    private Button mLogin;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mUsuario = findViewById(R.id.usuario);
        mSenha = findViewById(R.id.senha);
        tvSignUp = findViewById(R.id.tv_cadastre_se);
        mLogin = findViewById(R.id.buttonlogin);
        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);
        progressBar.setIndeterminate(true);

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!setError()) {
                    mLogin.setEnabled(false);
                    exibirProgressBar(true);
                    TarefaLogar logar = new TarefaLogar();
                    logar.execute(mAuth);
                }
            }
        });

        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, CadastroUsuarioActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if(user != null) {
            Intent intent = new Intent(LoginActivity.this, ProfessorActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void exibirProgressBar(boolean exibir) {
        progressBar.setVisibility(exibir ? View.VISIBLE : View.GONE);
    }

    private boolean setError() {
        boolean vazio = false;
        if(TextUtils.isEmpty(mUsuario.getText())) {
            mUsuario.setError("Campo vazio!");
            vazio = true;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(mUsuario.getText()).matches()) {
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


        @Override
        protected Void doInBackground(FirebaseAuth... firebaseAuths) {
            mAuth.signInWithEmailAndPassword(mUsuario.getText().toString(), mSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user = mAuth.getCurrentUser();
                        Intent intent = new Intent(LoginActivity.this, ProfessorActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        exibirProgressBar(false);
                        mLogin.setEnabled(true);
                        Toast.makeText(LoginActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
            return null;
        }
    }

}