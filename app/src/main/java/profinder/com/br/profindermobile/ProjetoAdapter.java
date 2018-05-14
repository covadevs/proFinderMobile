package profinder.com.br.profindermobile;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class ProjetoAdapter extends RecyclerView.Adapter<ProjetoAdapter.MyViewHolder> {

    private List<Projeto> projetos;
    private MaterialDialog materialDialog;

    public ProjetoAdapter(List<Projeto> projetos) {
        this.projetos = projetos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projeto_list_row, parent, false);
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
                        .title("Deletar projeto")
                        .positiveText("Sim")
                        .negativeText("Não")
                        .content("Deseja realmente deletar o "+projeto.getNome()+"?")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                                FirebaseFirestore fs = FirebaseFirestore.getInstance();
                                fs.collection("projects").document(projeto.getId()).delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()) {
                                                    Toast.makeText(dialog.getContext(), "Projeto deletado com sucesso", Toast.LENGTH_SHORT)
                                                            .show();
                                                } else {
                                                    Toast.makeText(dialog.getContext(), "Falha ao deletar projeto.", Toast.LENGTH_SHORT)
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
    }

    @Override
    public int getItemCount() {
        return projetos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo;
        public FancyButton mEditar;
        public FancyButton mDeletar;

        public MyViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo_projeto);
            mEditar = itemView.findViewById(R.id.editarProjeto);
            mDeletar = itemView.findViewById(R.id.deletarProjeto);
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
        }
    }
}
