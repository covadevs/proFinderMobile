package profinder.com.br.profindermobile;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeusProjetosFragment extends Fragment {
    private List<Projeto> projetos = new LinkedList<>();
    private RecyclerView recyclerView;
    private ProjetoAdapter mAdapter;
    private FirebaseFirestore fs;
    private DocumentReference dr;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressBar progressBar;
    private CarregarProjetos carregarProjetos;

    public MeusProjetosFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_meus_projetos, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fs = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        carregarProjetos = new CarregarProjetos();

        progressBar = getView().findViewById(R.id.progressBarListaProjetos);
        recyclerView = getView().findViewById(R.id.lista_projetos);

        mAdapter = new ProjetoAdapter(projetos);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(carregarProjetos.getStatus() == AsyncTask.Status.PENDING) {
                    carregarProjetos.execute();
                }
            }
        });
    }

    class CarregarProjetos extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            prepareProjectData();
            return null;
        }
    }

    private void prepareProjectData() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setEnabled(false);
        if(mUser != null) {
            fs.collection("projects")
                    .whereEqualTo("uid", mUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isSuccessful()) {
                                for(QueryDocumentSnapshot document: task.getResult()) {
                                    Projeto projeto = document.toObject(Projeto.class);
                                    projetos.add(projeto);
                                }

                                mAdapter.notifyDataSetChanged();
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setEnabled(true);
                            } else {
                                progressBar.setVisibility(View.GONE);
                                recyclerView.setEnabled(true);
                                Snackbar.make(getView(), "Falha ao recuperar projetos", Snackbar.LENGTH_INDEFINITE)
                                        .setAction("Tentar novamente", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                prepareProjectData();
                                            }
                                        });
                            }
                        }
                    });
        }
    }

}
