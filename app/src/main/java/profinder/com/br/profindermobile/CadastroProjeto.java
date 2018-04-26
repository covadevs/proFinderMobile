package profinder.com.br.profindermobile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class CadastroProjeto extends AppCompatActivity {

    private EditText NomeProjeto, AreaProjeto, CoodenadorProjeto, DescricaoProjeto;
    private Button buttonCadProjeto;
    private SeekBar QtdAlunosProjeto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_projeto);

        NomeProjeto = findViewById(R.id.NomeProjeto);
        AreaProjeto = findViewById(R.id.AreaProjeto);
        CoodenadorProjeto = findViewById(R.id.CoodenadorProjeto);
        DescricaoProjeto = findViewById(R.id.DescricaoProjeto);

        buttonCadProjeto = findViewById(R.id.buttonCadProjeto);

        QtdAlunosProjeto = findViewById(R.id.QtdAlunosProjeto);

        buttonCadProjeto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
