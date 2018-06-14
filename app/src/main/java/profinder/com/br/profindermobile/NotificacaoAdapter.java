package profinder.com.br.profindermobile;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.LinkedList;
import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class NotificacaoAdapter extends RecyclerView.Adapter<NotificacaoAdapter.MyViewHolder> {
    private List<Notificacao> noticacoes = new LinkedList<>();

    public NotificacaoAdapter(List<Notificacao> noticacoes) {
        this.noticacoes = noticacoes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificacaoAdapter.MyViewHolder holder, int position) {
        final Notificacao notificacao = noticacoes.get(position);
        holder.mNomeProjeto.setText(notificacao.getProjeto().getNome());
        holder.mNomeAluno.setText(notificacao.getUsuario().getNome());

        holder.mAceitarAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        holder.mRecusarAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return noticacoes.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView mNomeProjeto;
        public TextView mNomeAluno;
        public FancyButton mAceitarAluno;
        public FancyButton mRecusarAluno;

        public MyViewHolder(View itemView) {
            super(itemView);

            mNomeProjeto = itemView.findViewById(R.id.titulo_projeto_notificacao);
            mNomeAluno = itemView.findViewById(R.id.nome_aluno_notificacao);
            mAceitarAluno = itemView.findViewById(R.id.aceitarAluno);
            mRecusarAluno = itemView.findViewById(R.id.recusarAluno);

            IconicsDrawable aceitar = new IconicsDrawable(itemView.getContext()).icon(GoogleMaterial.Icon.gmd_add)
                    .color(itemView.getResources().getColor(R.color.md_white_1000));
            Drawable editarIcon = new BitmapDrawable(itemView.getResources(), aceitar.toBitmap());
            mAceitarAluno.setIconResource(editarIcon);
            mAceitarAluno.setFontIconSize(40);
            mAceitarAluno.setIconPadding(4,4,4,4);

            IconicsDrawable recusar = new IconicsDrawable(itemView.getContext()).icon(GoogleMaterial.Icon.gmd_remove)
                    .color(itemView.getResources().getColor(R.color.md_white_1000));
            Drawable deletarIcon = new BitmapDrawable(itemView.getResources(), recusar.toBitmap());
            mRecusarAluno.setIconResource(deletarIcon);
            mRecusarAluno.setFontIconSize(40);
            mRecusarAluno.setIconPadding(4,4,4,4);
        }
    }
}
