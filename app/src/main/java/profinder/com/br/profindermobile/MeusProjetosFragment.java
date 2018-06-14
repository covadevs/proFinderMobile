package profinder.com.br.profindermobile;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeusProjetosFragment extends Fragment {
    private List<Projeto> projetos = new LinkedList<>();
    private List<Notificacao> notificacoes = new LinkedList<>();
    private RecyclerView recyclerView;
    private ProjetoAdapter mAdapter;
    private FirebaseFirestore fs;
    private DocumentReference dr;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private ProgressBar progressBar;
    private Usuario usuario;
//    private CarregarProjetos carregarProjetos;

    public MeusProjetosFragment() {
        // Required empty public constructor
    }

    public void setRole(Usuario usuario) {
        this.usuario = usuario;
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

//        carregarProjetos = new CarregarProjetos();

        progressBar = getView().findViewById(R.id.progressBarListaProjetos);
        recyclerView = getView().findViewById(R.id.lista_projetos);

        mAdapter = new ProjetoAdapter(projetos, usuario);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);

//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if(carregarProjetos.getStatus() == AsyncTask.Status.PENDING) {
//                    carregarProjetos.execute();
//                }
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        fs = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        fs.collection("users").document(mUser.getUid()).addSnapshotListener((snapshot, e) -> {
            if(e != null) {
                return;
            }

            usuario = snapshot.toObject(Usuario.class);
            if(usuario.getType().equalsIgnoreCase("professor")) {
                fs.collection("projects").whereEqualTo("uid", mUser.getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                        @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        projetos.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Projeto p = new Projeto();
                            p.setNome(doc.getString("nome"));
                            p.setArea(doc.getString("area"));
                            p.setCoordenador(doc.getString("coordenador"));
                            p.setQntAlunos(doc.getLong("qntAlunos").intValue());
                            p.setDescricao(doc.getString("descricao"));
                            p.setUID(doc.getString("uid"));
                            p.setAlunos((ArrayList<Usuario>) doc.get("alunos"));
                            p.setId(doc.getId());
                            projetos.add(p);
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                });
            } else if (usuario.getType().equalsIgnoreCase("aluno")) {
                fs.collection("projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                        @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            return;
                        }

                        projetos.clear();
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            Projeto p = new Projeto();
                            p.setNome(doc.getString("nome"));
                            p.setArea(doc.getString("area"));
                            p.setCoordenador(doc.getString("coordenador"));
                            p.setQntAlunos(doc.getLong("qntAlunos").intValue());
                            p.setDescricao(doc.getString("descricao"));
                            p.setUID(doc.getString("uid"));
                            p.setAlunos((ArrayList<Usuario>) doc.get("alunos"));
                            p.setId(doc.getId());
                            projetos.add(p);
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(List<Projeto> projetos) {
        this.projetos = projetos;
    }

    public ProjetoAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(ProjetoAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public void setRecyclerViewList(List<Projeto> projetos) {
        ProjetoAdapter projetoAdapter = new ProjetoAdapter(projetos, usuario);
        recyclerView.setAdapter(projetoAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
