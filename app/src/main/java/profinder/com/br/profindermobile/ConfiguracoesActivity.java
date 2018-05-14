package profinder.com.br.profindermobile;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;

public class ConfiguracoesActivity extends AppCompatActivity {
    private EditText displayName;
    private EditText newEmail;
    private EditText newPassword;
    private CircularProgressButton atualizarPerfilButton;
    private Snackbar snackbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);
        initComponents();
        listeners();

    }

    private void listeners() {
        atualizarPerfilButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AtualizarInformacoes atualizarInformacoes = new AtualizarInformacoes();
                atualizarInformacoes.execute();
            }
        });
    }

    private void verifyEmptyFields() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null) {
            if (!TextUtils.isEmpty(displayName.getText())) {
                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName.getText().toString()).build();
                user.updateProfile(userProfileChangeRequest).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        snackbar = Snackbar.make(findViewById(R.id.configsLayout), "Falha ao alterar nome de perfil.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }

            if (!TextUtils.isEmpty(newEmail.getText())) {
                user.updateEmail(newEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            FirebaseAuth.getInstance().getCurrentUser().sendEmailVerification();
                            snackbar = Snackbar.make(findViewById(R.id.configsLayout), "Verifique seu novo e-mail.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            snackbar = Snackbar.make(findViewById(R.id.configsLayout), "Falha ao trocar e-mail.", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        }
                    }
                });

            }

            if (!TextUtils.isEmpty(newPassword.getText())) {
                user.updatePassword(newPassword.getText().toString()).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        snackbar = Snackbar.make(findViewById(R.id.configsLayout), "Falha ao alterar sua senha.", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                });
            }
        }
    }

    private void initComponents() {
        displayName = findViewById(R.id.nomeUsuarioConf);
        newEmail = findViewById(R.id.emailConf);
        newPassword = findViewById(R.id.novaSenha);

        atualizarPerfilButton = findViewById(R.id.circularProgressButton4);
    }

    class AtualizarInformacoes extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            atualizarPerfilButton.startAnimation();
            atualizarPerfilButton.setEnabled(false);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            verifyEmptyFields();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    atualizarPerfilButton.revertAnimation();
                    atualizarPerfilButton.setEnabled(true);
                    FirebaseAuth auth =  FirebaseAuth.getInstance();
                    auth.signOut();
                    Intent intent = new Intent(ConfiguracoesActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3500);
        }
    }
}
