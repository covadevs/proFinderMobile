package profinder.com.br.profindermobile;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.annimon.stream.Collectors;
import com.annimon.stream.Stream;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.LinkedList;
import java.util.List;

public class BuscaAcitvity extends AppCompatActivity {

    private MaterialSearchView materialSearchView;
    private int tabIndex = 0;
    private FragmentManager fragmentManager;
    private BuscarPerfilAlunoFragment buscarPerfilAlunoFragment = new BuscarPerfilAlunoFragment();
    private BuscarPerfilProfessorFragment buscarPerfilProfessorFragment = new BuscarPerfilProfessorFragment();
    private BuscaProjetosFragment buscaProjetosFragment = new BuscaProjetosFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca_acitvity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        materialSearchView = findViewById(R.id.search_view_2);

        fragmentManager = getSupportFragmentManager();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tabIndex = tab.getPosition();
                if(tabIndex == 0) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.framelayout_2, buscarPerfilAlunoFragment)
                            .commit();
                } else if(tabIndex == 1) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.framelayout_2, buscarPerfilProfessorFragment)
                            .commit();
                } else if(tabIndex == 2){
                    fragmentManager.beginTransaction()
                            .replace(R.id.framelayout_2, buscaProjetosFragment)
                            .commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        searchListeners();
    }

    private void searchListeners() {
        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(tabIndex == 0) {
                    buscarPerfilAlunoFragment.setRecyclerViewList(
                        Stream.of(buscarPerfilAlunoFragment.getUsuarios())
                            .filter(a -> a.getNome().contains(query))
                            .collect(Collectors.toList()));
                } else if(tabIndex == 1) {
                    buscarPerfilProfessorFragment.setRecyclerViewList(
                        Stream.of(buscarPerfilProfessorFragment.getUsuarios())
                            .filter(p -> p.getNome().contains(query))
                            .collect(Collectors.toList()));
                } else if(tabIndex == 2) {
                    buscaProjetosFragment.setRecyclerViewList(
                        Stream.of(buscaProjetosFragment.getProjetos())
                            .filter(p -> p.getNome().contains(query))
                            .collect(Collectors.toList()));
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(tabIndex == 0) {
                    buscarPerfilAlunoFragment.setRecyclerViewList(
                    Stream.of(buscarPerfilAlunoFragment.getUsuarios())
                            .filter(a -> a.getNome().contains(newText))
                            .collect(Collectors.toList()));
                } else if (tabIndex == 1) {
                    buscarPerfilProfessorFragment.setRecyclerViewList(
                        Stream.of(buscarPerfilProfessorFragment.getUsuarios())
                            .filter(p -> p.getNome().contains(newText))
                            .collect(Collectors.toList()));
                } else if(tabIndex == 2) {
                    buscaProjetosFragment.setRecyclerViewList(
                        Stream.of(buscaProjetosFragment.getProjetos())
                            .filter(p -> p.getNome().contains(newText))
                            .collect(Collectors.toList()));
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);
        materialSearchView.setMenuItem(menuItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
