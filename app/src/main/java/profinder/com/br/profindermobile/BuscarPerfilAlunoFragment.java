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


/**
 * A simple {@link Fragment} subclass.
 */
public class BuscarPerfilAlunoFragment extends Fragment {
    private List<Usuario> usuarios = new LinkedList<>();
    private RecyclerView recyclerView;
    private UsuarioAdapter mAdapter;
    private FirebaseFirestore fs;
    private DocumentReference dr;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    public BuscarPerfilAlunoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buscar_perfil, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fs = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        recyclerView = getView().findViewById(R.id.lista_alunos);
        mAdapter = new UsuarioAdapter(usuarios);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        fs.collection("users").whereEqualTo("type", "aluno").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots,
                                @javax.annotation.Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                usuarios.clear();
                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                    Usuario u = new Usuario();
                    u.setNome(doc.getString("nome"));
                    u.setEmail(doc.getString("email"));
                    u.setType(doc.getString("type"));
                    u.setMatricula(doc.getString("matricula"));
                    usuarios.add(u);
                }

                mAdapter.notifyDataSetChanged();

            }
        });
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    public UsuarioAdapter getmAdapter() {
        return mAdapter;
    }

    public void setmAdapter(UsuarioAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    public void setRecyclerViewList(List<Usuario> usuarios) {
        UsuarioAdapter usuarioAdapter = new UsuarioAdapter(usuarios);
        recyclerView.setAdapter(usuarioAdapter);
        mAdapter.notifyDataSetChanged();
    }
}
