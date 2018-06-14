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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;

import profinder.com.br.profindermobile.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificacaoFragment extends Fragment {
    private RecyclerView recyclerView;
    private NotificacaoAdapter notificacaoAdapter;
    private FirebaseFirestore fs;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private List<Notificacao> notificacoes = new LinkedList<>();

    public NotificacaoFragment() {
        // Required empty public constructor
    }

    public int getAdapterSize() {
        return notificacaoAdapter.getItemCount();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notificacao, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fs = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        recyclerView = getView().findViewById(R.id.notificacoes_list_fragment);
        notificacoes = new LinkedList<>();
        notificacaoAdapter = new NotificacaoAdapter(notificacoes);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(notificacaoAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        fs.collection("notifications").whereEqualTo("projeto.uid", user.getUid())
                .whereEqualTo("read", false)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    notificacoes.add(doc.toObject(Notificacao.class));
                }

                notificacaoAdapter.notifyDataSetChanged();
            }
        });
    }
}
