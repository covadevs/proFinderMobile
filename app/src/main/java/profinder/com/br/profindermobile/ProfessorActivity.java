package profinder.com.br.profindermobile;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
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
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;

public class ProfessorActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private AccountHeader accountHeader;
    private Drawer result;
    private boolean duploBackParaSair;
    private FirebaseStorage mStorage;
    private boolean isAlive;
    private Bitmap profilePic;

    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_professor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.mAuth = FirebaseAuth.getInstance();
        this.duploBackParaSair = false;
        this.isAlive = false;

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
                }).withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) { ;
                        CropImage.activity()
                                .setGuidelines(CropImageView.Guidelines.ON)
                                .setFixAspectRatio(true)
                                .setCropShape(CropImageView.CropShape.OVAL)
                                .start(ProfessorActivity.this);
                        return false;
                    }
                }).build();


        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
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
                                setTitle("Meus Projetos");
                                MeusProjetosFragment meusProjetosFragment = new MeusProjetosFragment();
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.framelayout, meusProjetosFragment)
                                        .commit();
                                break;
                            case 2:
                                setTitle("Ultimos Projetos");
                                UltimosProjetosFragment ultimosProjetosFragment = new UltimosProjetosFragment();
                                fragmentManager = getSupportFragmentManager();
                                fragmentManager.beginTransaction()
                                        .replace(R.id.framelayout, ultimosProjetosFragment)
                                        .commit();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                mStorage = FirebaseStorage.getInstance();
                StorageReference uploadImage = mStorage.getReference("profile/"+user.getUid());
                uploadImage.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if(task.isSuccessful()) {
                            downloadProfilePic();
                        }
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void downloadProfilePic() {
            mStorage = FirebaseStorage.getInstance();
            StorageReference downloadImage = mStorage.getReference("profile/" + user.getUid());
            try {
                final File img = File.createTempFile("profile", "jpge");
                downloadImage.getFile(img).addOnCompleteListener(new OnCompleteListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<FileDownloadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            profilePic = BitmapFactory.decodeFile(img.getPath());
                            accountHeader.removeProfile(0);
                            accountHeader.addProfile(
                                    new ProfileDrawerItem().withName(user.getDisplayName()).withEmail(user.getEmail()).withIcon(profilePic), 0
                            );
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!this.isAlive) {
            this.user = mAuth.getCurrentUser();
            if (user != null) {
                user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
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
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.isAlive = true;
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
        downloadProfilePic();
    }
}