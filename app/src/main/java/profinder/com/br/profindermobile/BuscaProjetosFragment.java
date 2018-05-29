package profinder.com.br.profindermobile;

import android.content.Context;
import android.net.Uri;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

public class BuscaProjetosFragment extends Fragment {
    private List<Projeto> projetos = new LinkedList<>();
    private RecyclerView recyclerView;
    private ProjetoBuscarAdapter mAdapter;
    private FirebaseFirestore fs;
    private DocumentReference dr;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public BuscaProjetosFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_busca_projetos, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fs = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        recyclerView = getView().findViewById(R.id.lista_projetos_buscar);

        mAdapter = new ProjetoBuscarAdapter(projetos);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        fs.collection("projects").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(e != null) {
                    return;
                }

                projetos.clear();
                for(QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Projeto p = new Projeto();
                    p.setNome(doc.getString("nome"));
                    p.setArea(doc.getString("area"));
                    p.setCoordenador(doc.getString("coordenador"));
                    p.setQntAlunos(doc.getLong("qntAlunos").intValue());
                    p.setDescricao(doc.getString("descricao"));
                    projetos.add(p);
                }

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public List<Projeto> getProjetos() {
        return projetos;
    }

    public void setProjetos(List<Projeto> projetos) {
        this.projetos = projetos;
    }

    public ProjetoBuscarAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(ProjetoBuscarAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public void setRecyclerViewList(List<Projeto> projetos) {
        ProjetoBuscarAdapter projetoAdapter = new ProjetoBuscarAdapter(projetos);
        recyclerView.setAdapter(projetoAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
