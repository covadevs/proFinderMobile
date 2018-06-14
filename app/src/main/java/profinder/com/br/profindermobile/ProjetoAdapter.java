package profinder.com.br.profindermobile;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.ea.async.Async;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import mehdi.sakout.fancybuttons.FancyButton;

import static com.ea.async.Async.await;

public class ProjetoAdapter extends RecyclerView.Adapter<ProjetoAdapter.MyViewHolder> {

    private List<Projeto> projetos;
    private MaterialDialog materialDialog;
    private View itemView;
    private FirebaseFirestore fs;
    private Usuario usuario;

    public ProjetoAdapter(List<Projeto> projetos, Usuario usuario) {
        this.projetos = projetos;
        this.usuario = usuario;
    }

    public void setType(Usuario usuario) {
        this.usuario = usuario;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projeto_list_row, parent, false);
        fs = FirebaseFirestore.getInstance();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Projeto projeto = projetos.get(position);
        holder.titulo.setText(projeto.getNome());

        holder.mEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.itemView.getContext(), EditarProjetoActivity.class);
                intent.putExtra("projeto", projeto);
                holder.itemView.getContext().startActivity(intent);

            }
        });

        holder.mDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDialog = new MaterialDialog.Builder(holder.itemView.getContext())
                        .title("Remover projeto")
                        .positiveText("Sim")
                        .negativeText("Não")
                        .content("Deseja realmente remover "+projeto.getNome()+"?")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                                FirebaseFirestore fs = FirebaseFirestore.getInstance();
                                fs.collection("projects").document(projeto.getId()).delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    Toast.makeText(dialog.getContext(), "Projeto excluido com sucesso", Toast.LENGTH_SHORT)
                                                            .show();
                                                } else {
                                                    Toast.makeText(dialog.getContext(), "Falha ao remover o projeto.", Toast.LENGTH_SHORT)
                                                            .show();
                                                }
                                            }
                                        });
                            }
                        }).onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        }).build();
                materialDialog.show();
            }
        });

        holder.mIncrever.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.mIncrever.getTag(R.id.increver_projeto).equals("undone")) {


                    IconicsDrawable done = new IconicsDrawable(itemView.getContext()).icon(GoogleMaterial.Icon.gmd_done)
                            .color(itemView.getResources().getColor(R.color.md_white_1000));
                    Drawable doneIcon = new BitmapDrawable(itemView.getResources(), done.toBitmap());
                    holder.mIncrever.setIconResource(doneIcon);
                    holder.mIncrever.setFontIconSize(40);
                    holder.mIncrever.setIconPadding(4, 4, 4, 4);

                    holder.mIncrever.setTag(R.id.increver_projeto, "done");
                    Notificacao notificacao = new Notificacao();
                    fs.collection("notifications").add(notificacao).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                        }
                    });

                } else if(holder.mIncrever.getTag(R.id.increver_projeto).equals("done")){
                    IconicsDrawable inscrever = new IconicsDrawable(itemView.getContext()).icon(GoogleMaterial.Icon.gmd_add)
                            .color(itemView.getResources().getColor(R.color.md_white_1000));
                    Drawable increverIcon = new BitmapDrawable(itemView.getResources(), inscrever.toBitmap());
                    holder.mIncrever.setIconResource(increverIcon);
                    holder.mIncrever.setFontIconSize(40);
                    holder.mIncrever.setIconPadding(4,4,4,4);
                    holder.mIncrever.setTag(R.id.increver_projeto, "undone");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return projetos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo;
        public FancyButton mEditar;
        public FancyButton mDeletar;
        public FancyButton mIncrever;

        public MyViewHolder(View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.titulo_projeto);
            mEditar = itemView.findViewById(R.id.editarProjeto);
            mDeletar = itemView.findViewById(R.id.deletarProjeto);
            mIncrever = itemView.findViewById(R.id.increver_projeto);

            if(usuario.getType().equalsIgnoreCase("professor")) {
                mIncrever.setVisibility(View.GONE);
                IconicsDrawable editar = new IconicsDrawable(itemView.getContext()).icon(GoogleMaterial.Icon.gmd_edit)
                        .color(itemView.getResources().getColor(R.color.md_white_1000));
                Drawable editarIcon = new BitmapDrawable(itemView.getResources(), editar.toBitmap());
                mEditar.setIconResource(editarIcon);
                mEditar.setFontIconSize(40);
                mEditar.setIconPadding(4,4,4,4);

                IconicsDrawable deletar = new IconicsDrawable(itemView.getContext()).icon(GoogleMaterial.Icon.gmd_close)
                        .color(itemView.getResources().getColor(R.color.md_white_1000));
                Drawable deletarIcon = new BitmapDrawable(itemView.getResources(), deletar.toBitmap());
                mDeletar.setIconResource(deletarIcon);
                mDeletar.setFontIconSize(40);
                mDeletar.setIconPadding(4,4,4,4);
            } else if(usuario.getType().equalsIgnoreCase("aluno")) {
                mIncrever.setVisibility(View.VISIBLE);
                mEditar.setVisibility(View.GONE);
                mDeletar.setVisibility(View.GONE);

                IconicsDrawable inscrever = new IconicsDrawable(itemView.getContext()).icon(GoogleMaterial.Icon.gmd_add)
                        .color(itemView.getResources().getColor(R.color.md_white_1000));
                Drawable increverIcon = new BitmapDrawable(itemView.getResources(), inscrever.toBitmap());
                mIncrever.setIconResource(increverIcon);
                mIncrever.setFontIconSize(40);
                mIncrever.setIconPadding(4,4,4,4);
                mIncrever.setTag(R.id.increver_projeto, "undone");
            }


        }
    }
}
