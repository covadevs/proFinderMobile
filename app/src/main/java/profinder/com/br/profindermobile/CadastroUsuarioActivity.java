package profinder.com.br.profindermobile;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CadastroUsuarioActivity extends AppCompatActivity {

    private EditText mNome, mEmail, mSenha, mRetypesenha, mMatricula;
    private RadioGroup tipoUsuario;
    private RadioButton radioButtonProfessor, radioButtonAluno;
    private Button buttonsignup;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mNome = findViewById(R.id.nome);
        mEmail = findViewById(R.id.email);
        mSenha = findViewById(R.id.senha);
        mRetypesenha = findViewById(R.id.retypesenha);
        mMatricula = findViewById(R.id.matricula);

        tipoUsuario = findViewById(R.id.tipoUsuario);

        radioButtonProfessor = findViewById(R.id.radioButtonProfessor);
        radioButtonAluno = findViewById(R.id.radioButtonAluno);

        buttonsignup = findViewById(R.id.buttonsignup);

        this.firebaseDatabase = FirebaseDatabase.getInstance();
        this.databaseReference = this.firebaseDatabase.getReference("users");
        this.mAuth = FirebaseAuth.getInstance();

        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signup(mEmail.getText().toString(), mSenha.getText().toString());
            }
        });
    }

    private void signup(String email, String senha) {
        this.mAuth.createUserWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

                    databaseReference.child(user.getUid()).setValue(u).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                finish();
                            } else {
                                Toast.makeText(CadastroUsuarioActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(CadastroUsuarioActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}