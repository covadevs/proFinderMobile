package profinder.com.br.profindermobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class CadastroActivity extends AppCompatActivity {

    private EditText nome, email, senha, retypesenha, matricula;
    private RadioGroup tipoUsuario;
    private RadioButton radioButtonProfessor, radioButtonAluno;
    private Button buttonsignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        retypesenha = findViewById(R.id.retypsenha);
        matricula = findViewById(R.id.matricula);

        tipoUsuario = findViewById(R.id.tipoUsuario);

        radioButtonProfessor = findViewById(R.id.radioButtonProfessor);
        radioButtonAluno = findViewById(R.id.radioButtonAluno);

        buttonsignup = findViewById(R.id.buttonsingup);

        buttonsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });


    }
}
