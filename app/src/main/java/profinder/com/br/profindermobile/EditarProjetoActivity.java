package profinder.com.br.profindermobile;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.HashMap;
import java.util.Map;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import profinder.com.br.profindermobile.R;

public class EditarProjetoActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_editar_projeto);
        initComponents();
        populateFields();
        listeners();
    }

    private void initComponents() {
        NomeProjeto = findViewById(R.id.editNomeProjeto);
        AreaProjeto = findViewById(R.id.editAreaProjeto);
        CoodenadorProjeto = findViewById(R.id.editCoodenadorProjeto);
        DescricaoProjeto = findViewById(R.id.editDescricaoProjeto);

        qntVagas = findViewById(R.id.editQtdAlunosProjeto);
        circularProgressButton = findViewById(R.id.editAtualizarProjeto);
        projeto = (Projeto) getIntent().getSerializableExtra("projeto");

        this.db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    private void populateFields() {
        NomeProjeto.setText(projeto.getNome());
        AreaProjeto.setText(projeto.getArea());
        CoodenadorProjeto.setText(projeto.getCoordenador());
        DescricaoProjeto.setText(projeto.getDescricao());
        qntVagas.setValue(projeto.getQntAlunos());
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
    private void listeners() {
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!setError()) {
                    AtualizarProjeto atualizarProjeto = new AtualizarProjeto();
                    atualizarProjeto.execute();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    class AtualizarProjeto extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            circularProgressButton.startAnimation();
            circularProgressButton.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            DocumentReference docRef = db.collection("projects").document(projeto.getId());
            Map<String, Object> projetoMap = new HashMap<>();
            projetoMap.put("area", AreaProjeto.getText().toString());
            projetoMap.put("coordenador",CoodenadorProjeto.getText().toString());
            projetoMap.put("descricao", DescricaoProjeto.getText().toString());
            projetoMap.put("nome", NomeProjeto.getText().toString());
            projetoMap.put("qntAlunos", qntVagas.getValue());
            docRef.update(projetoMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "Editado com sucesso", Toast.LENGTH_SHORT)
                                .show();
                        finish();
                    }
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            circularProgressButton.revertAnimation();
            circularProgressButton.setEnabled(true);
        }
    }
}
