package profinder.com.br.profindermobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shawnlin.numberpicker.NumberPicker;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class CadastroProjetoActivity extends AppCompatActivity {

    private EditText NomeProjeto, AreaProjeto, CoodenadorProjeto, DescricaoProjeto;
    private NumberPicker qntVagas;
    private CircularProgressButton circularProgressButton;
    private FirebaseFirestore db;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private Projeto projeto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_projeto);

        NomeProjeto = findViewById(R.id.NomeProjeto);
        AreaProjeto = findViewById(R.id.AreaProjeto);
        CoodenadorProjeto = findViewById(R.id.CoodenadorProjeto);
        DescricaoProjeto = findViewById(R.id.DescricaoProjeto);

        qntVagas = findViewById(R.id.QtdAlunosProjeto);
        circularProgressButton = findViewById(R.id.cadastrarProjeto);

        this.db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!setError()) {
                    circularProgressButton.setEnabled(false);
                    circularProgressButton.startAnimation();
                    projeto = new Projeto();
                    projeto.setUID(user.getUid());
                    projeto.setNome(NomeProjeto.getText().toString());
                    projeto.setArea(AreaProjeto.getText().toString());
                    projeto.setCoordenador(CoodenadorProjeto.getText().toString());
                    projeto.setDescricao(DescricaoProjeto.getText().toString());
                    projeto.setQntAlunos(qntVagas.getValue());
                    TarefaCadastrarProjeto tarefaCadastrarProjeto = new TarefaCadastrarProjeto();
                    tarefaCadastrarProjeto.execute(projeto);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        if(user != null) {
            updateUI(user);
        } else {
            Intent intent = new Intent(CadastroProjetoActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void updateUI(FirebaseUser user) {
        CoodenadorProjeto.setText(user.getDisplayName());
    }

    private boolean setError() {
        boolean vazio = false;
        if(NomeProjeto.getText().toString().isEmpty()) {
            NomeProjeto.setError("Campo vazio!");
            vazio = true;
        }

        if(AreaProjeto.getText().toString().isEmpty()) {
            AreaProjeto.setError("Campo vazio!");
            vazio = true;
        }

        if(CoodenadorProjeto.getText().toString().isEmpty()) {
            CoodenadorProjeto.setError("Campo vazio!");
            vazio = true;
        }

        if(DescricaoProjeto.getText().toString().isEmpty()) {
            DescricaoProjeto.setError("Campo vazio!");
            vazio = true;
        }

        return vazio;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == event.KEYCODE_BACK) {
            Intent intent = new Intent(CadastroProjetoActivity.this, ProfessorActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onKeyDown(keyCode, event);

    }

    class TarefaCadastrarProjeto extends AsyncTask<Projeto, Void, Void> {

        @Override
        protected Void doInBackground(Projeto... projetos) {
            db.collection("projects").add(projetos[0]).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()) {
                        Intent intent = new Intent(CadastroProjetoActivity.this, ProfessorActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CadastroProjetoActivity.this, "Falha na criação do projeto.",
                                Toast.LENGTH_SHORT).show();
                        circularProgressButton.revertAnimation();
                        circularProgressButton.setEnabled(true);
                    }
                }
            });
            return null;
        }
    }
}
