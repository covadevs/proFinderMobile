package profinder.com.br.profindermobile;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class ProfessorActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private AccountHeader accountHeader;
    private Drawer result;
    private boolean duploBackParaSair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.mAuth = FirebaseAuth.getInstance();
        this.duploBackParaSair = false;

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withIdentifier(1).withName("Meus Projetos");
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withIdentifier(2).withName("Ultimos Projetos").withIcon(GoogleMaterial.Icon.gmd_public);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withIdentifier(3).withName("Notificações").withIcon(GoogleMaterial.Icon.gmd_notifications);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withIdentifier(4).withName("Sair");


        accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionListEnabledForSingleProfile(false)
                .withHeaderBackground(getResources().getDrawable(R.drawable.profilebackground, null))
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                }).build();


        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withInnerShadow(true)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        item1.withIcon(GoogleMaterial.Icon.gmd_inbox),
                        item2,
                        new DividerDrawerItem(),
                        item3,
                        new DividerDrawerItem(),
                        item4.withIcon(GoogleMaterial.Icon.gmd_exit_to_app)
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        switch ((int)drawerItem.getIdentifier()){
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                mAuth.signOut();
                                Intent intent = new Intent(ProfessorActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                        }
                        return false;
                    }
                })
                .build();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfessorActivity.this, CadastroProjetoActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        this.user = mAuth.getCurrentUser();
        if(user != null) {
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        updateUI(user);
                    }
                }
            });
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if(duploBackParaSair) {
            super.onBackPressed();
            return;
        }

        if(result.isDrawerOpen()) {
            result.closeDrawer();
        }

        duploBackParaSair = true;
        Toast.makeText(this, "Toque novamente em voltar para sair", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                duploBackParaSair = false;
            }
        }, 2000);
    }

    private void updateUI(FirebaseUser user) {
        accountHeader.addProfile(
                new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail()).withIcon(getResources().getDrawable(R.drawable.profile, null)), 0
        );
    }
}